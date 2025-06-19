package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.DisponibiliteDAO;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Modèle pour gérer les disponibilités mensuelles d'un secouriste.
 * Permet de consulter, ajouter ou supprimer des créneaux de disponibilité.
 */
public class DisponibilitesModel {

    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final StringProperty moisSelectionne = new SimpleStringProperty("");
    private final ObservableList<CreneauDisponibilite> creneaux = FXCollections.observableArrayList();
    private final DisponibiliteDAO disponibiliteDAO = new DisponibiliteDAO();
    private long idSecouriste;
    private int currentMonth;
    private int currentYear;

    /**
     * Constructeur avec nom d'utilisateur et identifiant du secouriste.
     *
     * @param nomUtilisateur le nom affiché dans l'interface
     * @param idSecouriste l'identifiant du secouriste
     */
    public DisponibilitesModel(String nomUtilisateur, long idSecouriste) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.idSecouriste = idSecouriste;
        LocalDate now = LocalDate.now();
        this.currentMonth = now.getMonthValue();
        this.currentYear = now.getYear();
        updateMoisLabel();
        initializeCreneaux();
    }

    // Ajout d'une méthode pour définir l'ID du secouriste après construction
    /**
     * Définit l'identifiant du secouriste à utiliser pour les disponibilités.
     * @param idSecouriste identifiant du secouriste
     */
    public void setIdSecouriste(long idSecouriste) {
        this.idSecouriste = idSecouriste;
        // Recharger les disponibilités avec le nouvel ID
        initializeCreneaux();
    }

    /**
     * Retourne l'identifiant du secouriste.
     * @return identifiant du secouriste
     */
    public long getIdSecouriste() {
        return idSecouriste;
    }

    // Met à jour le libellé du mois affiché
    private void updateMoisLabel() {
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        String moisNom = date.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
        moisSelectionne.set(moisNom + " " + currentYear);
    }

    // Initialise la liste des créneaux pour le mois en cours
    private void initializeCreneaux() {
        creneaux.clear();
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        int daysInMonth = date.lengthOfMonth();

        List<Map<String, Object>> dbDispos = new ArrayList<>();
        if (idSecouriste > 0) { // Seulement si on a un ID valide
            dbDispos = disponibiliteDAO.findDisponibilitesBySecouristeAndMonth(
                idSecouriste, currentMonth, currentYear
            );
        }

        Set<Integer> dispoDays = new HashSet<>();
        for (Map<String, Object> dispo : dbDispos) {
            dispoDays.add((Integer) dispo.get("jour"));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            boolean disponible = dispoDays.contains(day);
            creneaux.add(new CreneauDisponibilite(day, disponible));
        }
    }

    /**
     * Bascule l'état de disponibilité du secouriste pour un jour donné.
     * Si le secouriste est disponible, le rend indisponible, et inversement.
     *
     * @param day le jour à modifier
     */
    public void toggleDisponibilite(int day) {
        CreneauDisponibilite creneau = creneaux.stream()
            .filter(c -> c.getDay() == day)
            .findFirst()
            .orElse(null);

        if (creneau != null) {
            if (idSecouriste <= 0) {
                System.err.println("Erreur : ID secouriste non défini (idSecouriste=" + idSecouriste + ")");
                return;
            }

            boolean newState = !creneau.isDisponible();
            creneau.setDisponible(newState);

            try {
                if (newState) {
                    boolean success = disponibiliteDAO.createDisponibilite(
                        idSecouriste, day, currentMonth, currentYear
                    );
                    if (!success) {
                        System.err.println("Échec de la création de disponibilité pour idSecouriste=" + idSecouriste +
                                           ", jour=" + day + ", mois=" + currentMonth + ", année=" + currentYear);
                        creneau.setDisponible(!newState);
                    } else {
                        System.out.println("Disponibilité créée avec succès pour le " + day + "/" + currentMonth + "/" + currentYear);
                    }
                } else {
                    boolean success = disponibiliteDAO.deleteDisponibilite(
                        idSecouriste, day, currentMonth, currentYear
                    );
                    if (!success) {
                        System.err.println("Échec de la suppression de disponibilité pour idSecouriste=" + idSecouriste +
                                           ", jour=" + day + ", mois=" + currentMonth + ", année=" + currentYear);
                        creneau.setDisponible(!newState);
                    } else {
                        System.out.println("Disponibilité supprimée avec succès pour le " + day + "/" + currentMonth + "/" + currentYear);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour de disponibilité : " + e.getMessage());
                e.printStackTrace();
                creneau.setDisponible(!newState);
            }
        } else {
            System.err.println("Créneau non trouvé pour le jour " + day);
        }
    }

    /**
     * Vérifie si un jour donné est marqué comme disponible.
     * @param day le jour à vérifier
     * @return true si disponible, sinon false
     */
    public boolean isDisponible(int day) {
        return creneaux.stream()
            .filter(c -> c.getDay() == day)
            .findFirst()
            .map(CreneauDisponibilite::isDisponible)
            .orElse(false);
    }

    /**
     * Passe au mois précédent et recharge les créneaux.
     */
    public void previousMonth() {
        currentMonth--;
        if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        updateMoisLabel();
        initializeCreneaux();
    }

    /**
     * Passe au mois suivant et recharge les créneaux.
     */
    public void nextMonth() {
        currentMonth++;
        if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        updateMoisLabel();
        initializeCreneaux();
    }

    /**
     * Retourne le mois actuellement affiché.
     * @return mois en cours (1–12)
     */
    public int getCurrentMonth() {
        return currentMonth;
    }

    /**
     * Retourne l'année actuellement affichée.
     * @return année courante
     */
    public int getCurrentYear() {
        return currentYear;
    }

    /**
     * Propriété JavaFX du nom de l'utilisateur.
     * @return propriété nom utilisateur
     */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /**
     * Propriété JavaFX du mois sélectionné (ex: "Mars 2025").
     * @return propriété du mois affiché
     */
    public StringProperty moisSelectionneProperty() {
        return moisSelectionne;
    }

    /**
     * Liste observable des créneaux affichés pour le mois.
     * @return liste observable des disponibilités
     */
    public ObservableList<CreneauDisponibilite> getCreneaux() {
        return creneaux;
    }

    /**
     * Retourne le nom de l'utilisateur.
     * @return nom utilisateur
     */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    /**
     * Définit un nouveau nom d'utilisateur.
     * @param nomUtilisateur nouveau nom
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Représente un créneau de disponibilité pour un jour donné du mois.
     */
    public static class CreneauDisponibilite {
        private final int day;
        private boolean disponible;

        /**
         * Constructeur de créneau de disponibilité.
         * @param day jour du mois
         * @param disponible état de disponibilité
         */
        public CreneauDisponibilite(int day, boolean disponible) {
            this.day = day;
            this.disponible = disponible;
        }

        /**
         * Retourne le jour du mois.
         * @return jour (1–31)
         */
        public int getDay() { return day; }

        /**
         * Indique si le jour est disponible.
         * @return true si disponible
         */
        public boolean isDisponible() { return disponible; }

        /**
         * Modifie l'état de disponibilité.
         * @param disponible true si disponible
         */
        public void setDisponible(boolean disponible) { this.disponible = disponible; }
    }
}
