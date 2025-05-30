package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminDashboardModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final StringProperty sectionActive = new SimpleStringProperty("dispositifs");
    
    public AdminDashboardModel() {
        // Constructeur par défaut
    }
    
    public AdminDashboardModel(String nomUtilisateur) {
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
    
    // Méthodes pour gérer les sections admin
    public boolean isDispositifsActive() {
        return "dispositifs".equals(getSectionActive());
    }
    
    public boolean isAffectationsSecouristesActive() {
        return "affectations_secouristes".equals(getSectionActive());
    }
    
    public boolean isSecouristesActive() {
        return "secouristes".equals(getSectionActive());
    }
    
    public void activerDispositifs() {
        setSectionActive("dispositifs");
    }
    
    public void activerAffectationsSecouristes() {
        setSectionActive("affectations_secouristes");
    }
    
    public void activerSecouristes() {
        setSectionActive("secouristes");
    }
}