package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AffectationsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Affectation> affectations = FXCollections.observableArrayList();
    
    public AffectationsModel() {
        initializeAffectations();
    }
    
    public AffectationsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeAffectations();
    }
    
    private void initializeAffectations() {
        // Initialisation avec les données de l'image
        affectations.add(new Affectation("06/02/2030", "Stade de Ski Alpin", "Poste de Secours Avancé"));
        affectations.add(new Affectation("10/02/2030", "Piste de Bobsleigh", "Équipe Médicale Urgence"));
        affectations.add(new Affectation("15/02/2030", "Village Olympique", "Poste Coordination Médicale"));
        affectations.add(new Affectation("20/02/2030", "Site Saut à Ski", "Équipe Réanimation"));
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
    
    // Méthodes pour gérer les affectations
    public void ajouterAffectation(Affectation affectation) {
        affectations.add(affectation);
    }
    
    public void supprimerAffectation(Affectation affectation) {
        affectations.remove(affectation);
    }
    
    // Classe interne pour représenter une affectation
    public static class Affectation {
        private final String date;
        private final String siteOlympique;
        private final String typeDispositif;
        
        public Affectation(String date, String siteOlympique, String typeDispositif) {
            this.date = date;
            this.siteOlympique = siteOlympique;
            this.typeDispositif = typeDispositif;
        }
        
        public String getDate() { return date; }
        public String getSiteOlympique() { return siteOlympique; }
        public String getTypeDispositif() { return typeDispositif; }
    }
}