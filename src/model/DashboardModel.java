package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DashboardModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final StringProperty sectionActive = new SimpleStringProperty("affectations");
    
    public DashboardModel() {
        // Constructeur par défaut
    }
    
    public DashboardModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public StringProperty sectionActiveProperty() {
        return sectionActive;
    }
    
    // Getters pour les valeurs
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public String getSectionActive() {
        return sectionActive.get();
    }
    
    // Setters
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public void setSectionActive(String sectionActive) {
        this.sectionActive.set(sectionActive);
    }
    
    // Méthodes pour gérer les sections
    public boolean isAffectationsActive() {
        return "affectations".equals(getSectionActive());
    }
    
    public boolean isPlanningActive() {
        return "planning".equals(getSectionActive());
    }
    
    public boolean isDisponibilitesActive() {
        return "disponibilites".equals(getSectionActive());
    }
    
    public void activerAffectations() {
        setSectionActive("affectations");
    }
    
    public void activerPlanning() {
        setSectionActive("planning");
    }
    
    public void activerDisponibilites() {
        setSectionActive("disponibilites");
    }
}