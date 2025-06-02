package controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.DashboardModel;
import view.DashboardView;
import view.DisponibilitesView;
import view.AffectationsView;
import view.PlanningView;
import view.LoginView;

public class DashboardController {
    
    private Button affectationsButton;
    private Button planningButton;
    private Button disponibilitesButton;
    private Button deconnexionButton;
    private Label nomUtilisateurLabel;
    private DashboardModel model;
    
    public DashboardController(Button affectationsButton, Button planningButton, 
                              Button disponibilitesButton, Button deconnexionButton, Label nomUtilisateurLabel) {
        this.affectationsButton = affectationsButton;
        this.planningButton = planningButton;
        this.disponibilitesButton = disponibilitesButton;
        this.deconnexionButton = deconnexionButton;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.model = new DashboardModel();
        
        setupBindings();
        setupListeners();
        updateButtonStyles();
    }
    
    public DashboardController(Button affectationsButton, Button planningButton, 
                              Button disponibilitesButton, Button deconnexionButton, Label nomUtilisateurLabel, 
                              String nomUtilisateur) {
        this(affectationsButton, planningButton, disponibilitesButton, deconnexionButton, nomUtilisateurLabel);
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
        deconnexionButton.setOnAction(this::handleDeconnexion);
    }
    
    private void handleAffectations(ActionEvent event) {
        System.out.println("Navigation vers Affectations");
        model.activerAffectations();
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) affectationsButton.getScene().getWindow();
        
        // Créer la nouvelle vue
        AffectationsView affectationsView = new AffectationsView(model.getNomUtilisateur());
        
        // Configurer le retour vers le dashboard
        affectationsView.getController().setOnRetourCallback(() -> {
            // Recréer la vue du dashboard
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        
        // Changer la scène
        Scene affectationsScene = new Scene(affectationsView.getRoot(), 1024, 600);
        currentStage.setScene(affectationsScene);
    }
    
    private void handlePlanning(ActionEvent event) {
        System.out.println("Navigation vers Planning");
        model.activerPlanning();
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) planningButton.getScene().getWindow();
        
        // Créer la nouvelle vue
        PlanningView planningView = new PlanningView(model.getNomUtilisateur());
        
        // Configurer le retour vers le dashboard
        planningView.getController().setOnRetourCallback(() -> {
            // Recréer la vue du dashboard
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        
        // Changer la scène
        Scene planningScene = new Scene(planningView.getRoot(), 1024, 600);
        currentStage.setScene(planningScene);
    }
    
    private void handleDisponibilites(ActionEvent event) {
        System.out.println("Navigation vers Disponibilités");
        model.activerDisponibilites();
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) disponibilitesButton.getScene().getWindow();
        
        // Créer la nouvelle vue
        DisponibilitesView disponibilitesView = new DisponibilitesView(model.getNomUtilisateur());
        
        // Configurer le retour vers le dashboard
        disponibilitesView.getController().setOnRetourCallback(() -> {
            // Recréer la vue du dashboard
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        
        // Changer la scène
        Scene disponibilitesScene = new Scene(disponibilitesView.getRoot(), 1024, 600);
        currentStage.setScene(disponibilitesScene);
    }
    
    private void handleDeconnexion(ActionEvent event) {
        System.out.println("Déconnexion de l'utilisateur");
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) deconnexionButton.getScene().getWindow();
        
        // Créer la vue de connexion
        LoginView loginView = new LoginView(currentStage);
        
        // Créer une nouvelle scène avec la vue de connexion
        Scene loginScene = new Scene(loginView.getRoot(), 1024, 600);
        
        // Essayer d'appliquer le CSS pour le login
        try {
            loginScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        } catch (Exception cssException) {
            System.out.println("Attention: Fichier CSS style.css non trouvé, styles par défaut appliqués");
        }
        
        // Changer la scène
        currentStage.setScene(loginScene);
        currentStage.setTitle("SecuOptix - Connexion");
        
        System.out.println("Retour à la page de connexion réussi !");
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