package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.*;
import model.data.*;
import model.graphs.DAG;
import model.graphs.Graphe;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Modèle pour la gestion des affectations d'un administrateur.
 */
public class AdminAffectationsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Affectation> affectations = FXCollections.observableArrayList();
    private final DPSDAO dpsDAO;
    private final SecouristeDAO secouristeDAO;
    private final AffectationDAO affectationDAO;
    private final Graphe graphe;
    
    /**
     * Constructeur principal du modèle.
     * @param nomUtilisateur le nom de l'utilisateur connecté
     */
    public AdminAffectationsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.dpsDAO = new DPSDAO();
        this.secouristeDAO = new SecouristeDAO();
        this.affectationDAO = new AffectationDAO();
        this.graphe = new Graphe(new DAG());
        initializeData();
    }

    /**
     * Initialise les affectations depuis la base de données.
     */
    private void initializeData() {
        List<Affectation> dbAffectations = affectationDAO.findAllAffectations();
        affectations.addAll(dbAffectations);
    }

    /**
     * Supprime toutes les affectations de la base de données et vide la liste observable.
     * @return true si la suppression a réussi, false sinon
     */
    public boolean nettoyerAffectations() {
        int result = affectationDAO.deleteAllAffectations();
        if (result >= 0) {
            affectations.clear();
            return true;
        }
        return false;
    }

    /**
     * Crée une affectation et l’enregistre dans la base.
     * @param dps le DPS concerné
     * @param date la date de l’affectation
     * @param secouristes les secouristes affectés
     */
    public void createAffectation(DPS dps, LocalDate date, ObservableList<Secouriste> secouristes) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String siteStr = dps.getSite().getNom();
        String secouristesStr = secouristes.stream()
                .map(s -> s.getPrenom() + " " + s.getNom())
                .collect(Collectors.joining("\n"));
                
        Affectation affectation = new Affectation(dateStr, siteStr, secouristesStr);
        affectations.add(affectation);

        for (Secouriste secouriste : secouristes) {
            affectationDAO.createAffectation(secouriste.getId(), dps.getId());
        }
    }

    /**
     * Récupère tous les DPS depuis la base.
     * @return la liste observable des DPS
     */
    public ObservableList<DPS> getAllDPS() {
        return FXCollections.observableArrayList(dpsDAO.findAll());
    }

    /**
     * Recherche les secouristes compétents et disponibles pour un DPS donné à une date donnée.
     * @param dps le DPS ciblé
     * @param date la date de l’événement
     * @return liste observable de secouristes disponibles
     */
    public ObservableList<Secouriste> searchCompetentSecouristes(DPS dps, LocalDate date) {
        List<Secouriste> allSecouristes = secouristeDAO.findAll();
        List<DPS> dpsList = List.of(dps);

        Map<DPS, List<Secouriste>> result = graphe.affectationGloutonne(allSecouristes, dpsList);

        List<Secouriste> available = result.getOrDefault(dps, List.of()).stream()
                .filter(s -> isSecouristeAvailable(s, date))
                .collect(Collectors.toList());

        return FXCollections.observableArrayList(available);
    }

    /**
     * Vérifie si un secouriste est disponible à une date donnée.
     * @param secouriste le secouriste à vérifier
     * @param date la date cible
     * @return true s’il est disponible, false sinon
     */
    public boolean isSecouristeAvailable(Secouriste secouriste, LocalDate date) {
        return affectationDAO.isSecouristeAvailable(
            secouriste.getId(), 
            date.getDayOfMonth(), 
            date.getMonthValue(), 
            date.getYear()
        );
    }

    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    public ObservableList<Affectation> getAffectations() {
        return affectations;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    public Graphe getGraphe() {
        return graphe;
    }

    public SecouristeDAO getSecouristeDAO() {
        return secouristeDAO;
    }

    /**
     * Classe interne représentant une affectation affichée.
     */
    public static class Affectation {
        private final StringProperty date = new SimpleStringProperty();
        private final StringProperty sitesOlympiques = new SimpleStringProperty();
        private final StringProperty secouristes = new SimpleStringProperty();

        /**
         * Constructeur de l'affectation.
         * @param date date de l’affectation
         * @param sitesOlympiques nom du site
         * @param secouristes liste des secouristes en texte
         */
        public Affectation(String date, String sitesOlympiques, String secouristes) {
            this.date.set(date);
            this.sitesOlympiques.set(sitesOlympiques);
            this.secouristes.set(secouristes);
        }

        public StringProperty dateProperty() { return date; }
        public StringProperty sitesOlympiquesProperty() { return sitesOlympiques; }
        public StringProperty secouristesProperty() { return secouristes; }

        public String getDate() { return date.get(); }
        public String getSitesOlympiques() { return sitesOlympiques.get(); }
        public String getSecouristes() { return secouristes.get(); }

        public void setDate(String date) { this.date.set(date); }
        public void setSitesOlympiques(String sitesOlympiques) { this.sitesOlympiques.set(sitesOlympiques); }
        public void setSecouristes(String secouristes) { this.secouristes.set(secouristes); }
    }
}
