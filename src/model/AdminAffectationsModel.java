package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminAffectationsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Affectation> affectations = FXCollections.observableArrayList();
    
    public AdminAffectationsModel() {
        // Constructeur par défaut
        initializeData();
    }
    
    public AdminAffectationsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeData();
    }
    
    private void initializeData() {
        // Données d'exemple correspondant à la maquette
        affectations.addAll(
            new Affectation("06/02/2030", "Stade Ski Alpin\nStade Ski Nordique", "Sophie Dupont\nMarc Laurent"),
            new Affectation("10/02/2030", "Piste Bobsleigh\nTremplin Saut à Ski", "Emma Rousseau\nThomas Martin"),
            new Affectation("15/02/2030", "Village Olympique\nCentre Méd. Principal", "Léa Moreau\nAlexandre Petit"),
            new Affectation("20/02/2030", "Site Curling\nPatinoire Vitesse", "Sophie Dupont\nMarc Laurent")
        );
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<Affectation> getAffectations() {
        return affectations;
    }
    
    // Getters pour les valeurs
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    // Setters
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    // Méthodes CRUD pour les affectations
    public void ajouterAffectation(Affectation affectation) {
        affectations.add(affectation);
    }
    
    public void supprimerAffectation(Affectation affectation) {
        affectations.remove(affectation);
    }
    
    public void modifierAffectation(int index, Affectation nouvelleAffectation) {
        if (index >= 0 && index < affectations.size()) {
            affectations.set(index, nouvelleAffectation);
        }
    }
    
    // Classe interne pour représenter une affectation
    public static class Affectation {
        private final StringProperty date = new SimpleStringProperty();
        private final StringProperty sitesOlympiques = new SimpleStringProperty();
        private final StringProperty secouristes = new SimpleStringProperty();
        
        public Affectation(String date, String sitesOlympiques, String secouristes) {
            this.date.set(date);
            this.sitesOlympiques.set(sitesOlympiques);
            this.secouristes.set(secouristes);
        }
        
        // Getters pour les propriétés
        public StringProperty dateProperty() { return date; }
        public StringProperty sitesOlympiquesProperty() { return sitesOlympiques; }
        public StringProperty secouristesProperty() { return secouristes; }
        
        // Getters pour les valeurs
        public String getDate() { return date.get(); }
        public String getSitesOlympiques() { return sitesOlympiques.get(); }
        public String getSecouristes() { return secouristes.get(); }
        
        // Setters
        public void setDate(String date) { this.date.set(date); }
        public void setSitesOlympiques(String sitesOlympiques) { this.sitesOlympiques.set(sitesOlympiques); }
        public void setSecouristes(String secouristes) { this.secouristes.set(secouristes); }
    }
}