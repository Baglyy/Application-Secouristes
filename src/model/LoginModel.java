package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginModel {
    
    private final StringProperty identifiant = new SimpleStringProperty("");
    private final StringProperty motDePasse = new SimpleStringProperty("");
    
    // Getters pour les propriétés
    public StringProperty identifiantProperty() {
        return identifiant;
    }
    
    public StringProperty motDePasseProperty() {
        return motDePasse;
    }
    
    // Getters pour les valeurs
    public String getIdentifiant() {
        return identifiant.get();
    }
    
    public String getMotDePasse() {
        return motDePasse.get();
    }
    
    // Setters
    public void setIdentifiant(String identifiant) {
        this.identifiant.set(identifiant);
    }
    
    public void setMotDePasse(String motDePasse) {
        this.motDePasse.set(motDePasse);
    }
    
    // Méthode pour vider les champs
    public void clearFields() {
        setIdentifiant("");
        setMotDePasse("");
    }
    
    // Méthode pour valider la connexion (pour plus tard)
    public boolean validateLogin() {
        return !getIdentifiant().trim().isEmpty() && !getMotDePasse().trim().isEmpty();
    }
}