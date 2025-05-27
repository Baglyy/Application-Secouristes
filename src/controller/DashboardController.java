package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import model.DashboardModel;

public class DashboardController {
    
    private Button affectationsButton;
    private Button planningButton;
    private Button disponibilitesButton;
    private Label nomUtilisateurLabel;
    private DashboardModel model;
    
    public DashboardController(Button affectationsButton, Button planningButton, 
                              Button disponibilitesButton, Label nomUtilisateurLabel) {
        this.affectationsButton = affectationsButton;
        this.planningButton = planningButton;
        this.disponibilitesButton = disponibilitesButton;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.model = new DashboardModel();
        
        setupBindings();
        setupListeners();
        updateButtonStyles();
    }
    
    public DashboardController(Button affectationsButton, Button planningButton, 
                              Button disponibilitesButton, Label nomUtilisateurLabel, 
                              String nomUtilisateur) {
        this(affectationsButton, planningButton, disponibilitesButton, nomUtilisateurLabel);
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        
        // Écouter les changements de section pour mettre à jour les styles
        model.sectionActiveProperty().addListener((obs, oldSection, newSection) -> {
            updateButtonStyles();
        });
    }
    
    private void setupListeners() {
        // Listeners pour les boutons
        affectationsButton.setOnAction(this::handleAffectations);
        planningButton.setOnAction(this::handlePlanning);
        disponibilitesButton.setOnAction(this::handleDisponibilites);
    }
    
    private void handleAffectations(ActionEvent event) {
        System.out.println("Navigation vers Affectations");
        model.activerAffectations();
        // Ici on pourrait ajouter la logique pour afficher le contenu des affectations
    }
    
    private void handlePlanning(ActionEvent event) {
        System.out.println("Navigation vers Planning");
        model.activerPlanning();
        // Ici on pourrait ajouter la logique pour afficher le contenu du planning
    }
    
    private void handleDisponibilites(ActionEvent event) {
        System.out.println("Navigation vers Disponibilités");
        model.activerDisponibilites();
        // Ici on pourrait ajouter la logique pour afficher le contenu des disponibilités
    }
    
    private void updateButtonStyles() {
        // Réinitialiser tous les boutons
        affectationsButton.getStyleClass().removeAll("active-button");
        planningButton.getStyleClass().removeAll("active-button");
        disponibilitesButton.getStyleClass().removeAll("active-button");
        
        // Ajouter la classe active au bouton sélectionné
        switch (model.getSectionActive()) {
            case "affectations":
                if (!affectationsButton.getStyleClass().contains("active-button")) {
                    affectationsButton.getStyleClass().add("active-button");
                }
                break;
            case "planning":
                if (!planningButton.getStyleClass().contains("active-button")) {
                    planningButton.getStyleClass().add("active-button");
                }
                break;
            case "disponibilites":
                if (!disponibilitesButton.getStyleClass().contains("active-button")) {
                    disponibilitesButton.getStyleClass().add("active-button");
                }
                break;
        }
    }
    
    // Méthodes publiques pour interaction externe
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public String getSectionActive() {
        return model.getSectionActive();
    }
    
    public DashboardModel getModel() {
        return model;
    }
}