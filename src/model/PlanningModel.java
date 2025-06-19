package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.AffectationDAO;
import model.AdminAffectationsModel;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Modèle de données pour la vue planning d’un secouriste.
 * Gère les affectations selon le mois courant pour un secouriste donné.
 */
public class PlanningModel {

    /** Nom de l'utilisateur connecté */
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");

    /** Mois actuellement affiché dans le calendrier */
    private final ObjectProperty<YearMonth> moisActuel = new SimpleObjectProperty<>(YearMonth.now());

    /** Liste observable des affectations du mois affiché */
    private final ObservableList<AdminAffectationsModel.Affectation> affectations = FXCollections.observableArrayList();

    /** Mapping des dates vers les affectations correspondantes */
    private final Map<LocalDate, AdminAffectationsModel.Affectation> affectationsParDate = new HashMap<>();

    /** Format utilisé pour parser les dates des affectations */
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** DAO pour la récupération des affectations */
    private final AffectationDAO affectationDAO;

    /** Identifiant du secouriste concerné */
    private final long idSecouriste;

    /**
     * Constructeur principal du modèle.
     * @param nomUtilisateur le nom de l'utilisateur
     * @param idSecouriste l'identifiant du secouriste
     */
    public PlanningModel(String nomUtilisateur, long idSecouriste) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.idSecouriste = idSecouriste;
        this.affectationDAO = new AffectationDAO();
        initializeAffectations();
        mapAffectationsParDate();
    }

    /**
     * Initialise la liste des affectations depuis la base pour le mois actuel.
     */
    private void initializeAffectations() {
        // IMPORTANT: Vider complètement les collections avant de recharger
        affectations.clear();
        affectationsParDate.clear(); // Ajout de cette ligne cruciale

        YearMonth currentMonth = moisActuel.get();

        System.out.println("=== DEBUG initializeAffectations ===");
        System.out.println("Chargement des affectations pour idSecouriste=" + idSecouriste + 
                          ", mois=" + currentMonth.getMonthValue() + 
                          ", année=" + currentMonth.getYear());

        List<AdminAffectationsModel.Affectation> affectationsDuMois = 
            affectationDAO.findAffectationsForSecoursiteAndMonth(
                idSecouriste, 
                currentMonth.getMonthValue(), 
                currentMonth.getYear()
            );

        affectations.addAll(affectationsDuMois);
        System.out.println("Chargé " + affectationsDuMois.size() + " affectations");

        for (AdminAffectationsModel.Affectation aff : affectationsDuMois) {
            System.out.println("  - Date: " + aff.getDate() + ", Site: " + aff.getSitesOlympiques());
        }
        System.out.println("=== FIN DEBUG initializeAffectations ===");
    }

    /**
     * Construit le mapping entre dates et affectations.
     */
    private void mapAffectationsParDate() {
        // Le mapping est maintenant vidé dans initializeAffectations()
        System.out.println("=== DEBUG mapAffectationsParDate ===");
        System.out.println("Nombre d'affectations à mapper: " + affectations.size());
        System.out.println("État du mapping avant traitement: " + affectationsParDate.size() + " entrées");

        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yy"),
            DateTimeFormatter.ofPattern("d/M/yy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };

        for (AdminAffectationsModel.Affectation affectation : affectations) {
            String dateString = affectation.getDate();
            System.out.println("Traitement affectation - Date string: '" + dateString + "'");

            LocalDate date = null;
            for (DateTimeFormatter formatter : formatters) {
                try {
                    date = LocalDate.parse(dateString, formatter);
                    System.out.println("  -> Date parsée avec succès: " + date + " (format: " + formatter + ")");
                    break;
                } catch (Exception ignored) {}
            }

            if (date != null) {
                affectationsParDate.put(date, affectation);
                System.out.println("  -> Ajoutée au mapping: " + date + " - Site: " + affectation.getSitesOlympiques());
            } else {
                System.err.println("  -> ERREUR: Impossible de parser la date: " + dateString);
            }
        }

        System.out.println("Mapping terminé. Nombre de dates mappées: " + affectationsParDate.size());
        System.out.println("Dates mappées:");
        for (LocalDate date : affectationsParDate.keySet()) {
            System.out.println("  - " + date + " : " + affectationsParDate.get(date).getSitesOlympiques());
        }
        System.out.println("=== FIN DEBUG mapAffectationsParDate ===");
    }

    /**
     * Bascule vers le mois précédent.
     */
    public void moisPrecedent() {
        System.out.println("=== Changement vers mois précédent ===");
        YearMonth ancienMois = moisActuel.get();
        moisActuel.set(moisActuel.get().minusMonths(1));
        YearMonth nouveauMois = moisActuel.get();
        System.out.println("Changement de mois: " + ancienMois + " -> " + nouveauMois);
        initializeAffectations();
        mapAffectationsParDate();
    }

    /**
     * Bascule vers le mois suivant.
     */
    public void moisSuivant() {
        System.out.println("=== Changement vers mois suivant ===");
        YearMonth ancienMois = moisActuel.get();
        moisActuel.set(moisActuel.get().plusMonths(1));
        YearMonth nouveauMois = moisActuel.get();
        System.out.println("Changement de mois: " + ancienMois + " -> " + nouveauMois);
        initializeAffectations();
        mapAffectationsParDate();
    }

    /**
     * Revient au mois courant (aujourd'hui).
     */
    public void allerAujourdHui() {
        System.out.println("=== Retour au mois actuel ===");
        YearMonth ancienMois = moisActuel.get();
        moisActuel.set(YearMonth.now());
        YearMonth nouveauMois = moisActuel.get();
        System.out.println("Changement de mois: " + ancienMois + " -> " + nouveauMois);
        initializeAffectations();
        mapAffectationsParDate();
    }

    /**
     * Récupère l'affectation correspondant à une date.
     * @param date la date concernée
     * @return l'affectation ou null si aucune
     */
    public AdminAffectationsModel.Affectation getAffectationPourDate(LocalDate date) {
        return affectationsParDate.get(date);
    }

    /**
     * Vérifie si une date possède une affectation.
     * @param date la date à tester
     * @return true si une affectation est présente
     */
    public boolean hasAffectationPourDate(LocalDate date) {
        boolean result = affectationsParDate.containsKey(date);
        System.out.println("hasAffectationPourDate(" + date + ") = " + result);
        if (!result && !affectationsParDate.isEmpty()) {
            System.out.println("  Dates disponibles dans le mapping:");
            for (LocalDate mappedDate : affectationsParDate.keySet()) {
                System.out.println("    - " + mappedDate);
            }
        }
        return result;
    }

    // Getters et Setters

    /**
     * @return la propriété du nom utilisateur
     */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /**
     * @return la propriété du mois actuellement sélectionné
     */
    public ObjectProperty<YearMonth> moisActuelProperty() {
        return moisActuel;
    }

    /**
     * @return la liste observable des affectations
     */
    public ObservableList<AdminAffectationsModel.Affectation> getAffectations() {
        return affectations;
    }

    /**
     * @return le nom de l'utilisateur
     */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    /**
     * Définit le nom de l'utilisateur.
     * @param nomUtilisateur le nom à définir
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * @return le mois actuellement sélectionné
     */
    public YearMonth getMoisActuel() {
        return moisActuel.get();
    }

    /**
     * Définit le mois courant et recharge les affectations.
     * @param mois le nouveau mois à afficher
     */
    public void setMoisActuel(YearMonth mois) {
        YearMonth ancienMois = this.moisActuel.get();
        this.moisActuel.set(mois);
        System.out.println("setMoisActuel: " + ancienMois + " -> " + mois);
        initializeAffectations();
        mapAffectationsParDate();
    }

    /**
     * @return le nom du mois et l'année formatés (ex : "juin 2025")
     */
    public String getMoisAnneeString() {
        return moisActuel.get().format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH));
    }

    /**
     * @return le premier jour du mois affiché
     */
    public LocalDate getPremierJourDuMois() {
        return moisActuel.get().atDay(1);
    }

    /**
     * @return le nombre de jours dans le mois affiché
     */
    public int getNombreJoursDansMois() {
        return moisActuel.get().lengthOfMonth();
    }

    /**
     * @return le jour de la semaine du premier jour du mois (1 = Lundi)
     */
    public int getPremierJourSemaine() {
        return getPremierJourDuMois().getDayOfWeek().getValue();
    }
}
