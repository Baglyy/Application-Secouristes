package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DisponibilitesModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final StringProperty semaineSelectionnee = new SimpleStringProperty("Semaine 14");
    private final ObservableList<CreneauDisponibilite> creneaux = FXCollections.observableArrayList();
    
    public DisponibilitesModel() {
        initializeCreneaux();
    }
    
    public DisponibilitesModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeCreneaux();
    }
    
    private void initializeCreneaux() {
        // Initialisation des créneaux avec les valeurs par défaut comme dans l'image
        String[] heures = {"9H - 10H", "10H - 12H", "12H - 14H", "14H - 16H"};
        String[] jours = {"LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI"};
        
        // État initial basé sur l'image fournie
        boolean[][] disponibilites = {
            {false, true, false, true, true},   // 9H - 10H
            {false, true, true, false, false},  // 10H - 12H
            {true, false, false, false, true},  // 12H - 14H
            {true, true, false, true, true}     // 14H - 16H
        };
        
        for (int i = 0; i < heures.length; i++) {
            for (int j = 0; j < jours.length; j++) {
                creneaux.add(new CreneauDisponibilite(heures[i], jours[j], disponibilites[i][j]));
            }
        }
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public StringProperty semaineSelectionneeProperty() {
        return semaineSelectionnee;
    }
    
    public ObservableList<CreneauDisponibilite> getCreneaux() {
        return creneaux;
    }
    
    // Getters pour les valeurs
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public String getSemaineSelectionnee() {
        return semaineSelectionnee.get();
    }
    
    // Setters
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public void setSemaineSelectionnee(String semaine) {
        this.semaineSelectionnee.set(semaine);
    }
    
    // Méthodes pour gérer les disponibilités
    public void toggleDisponibilite(String heure, String jour) {
        creneaux.stream()
            .filter(c -> c.getHeure().equals(heure) && c.getJour().equals(jour))
            .findFirst()
            .ifPresent(c -> c.setDisponible(!c.isDisponible()));
    }
    
    public boolean isDisponible(String heure, String jour) {
        return creneaux.stream()
            .filter(c -> c.getHeure().equals(heure) && c.getJour().equals(jour))
            .findFirst()
            .map(CreneauDisponibilite::isDisponible)
            .orElse(false);
    }
    
    // Classe interne pour représenter un créneau
    public static class CreneauDisponibilite {
        private final String heure;
        private final String jour;
        private boolean disponible;
        
        public CreneauDisponibilite(String heure, String jour, boolean disponible) {
            this.heure = heure;
            this.jour = jour;
            this.disponible = disponible;
        }
        
        public String getHeure() { return heure; }
        public String getJour() { return jour; }
        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }
    }
}