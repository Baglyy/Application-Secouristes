package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import model.AffectationsModel;
import view.DashboardView;

public class AffectationsController {
    
    private Label nomUtilisateurLabel;
    private Button retourButton;
    private Button homeButton;
    private VBox affectationsContainer;
    private AffectationsModel model;
    private Runnable onRetourCallback;
    
    public AffectationsController(Label nomUtilisateurLabel, Button retourButton, 
                                VBox affectationsContainer, String nomUtilisateur) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.retourButton = retourButton;
        this.affectationsContainer = affectationsContainer;
        this.model = new AffectationsModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
        populateAffectations();
    }
    
    public AffectationsController(Label nomUtilisateurLabel, Button retourButton, 
                                VBox affectationsContainer, String nomUtilisateur, 
                                Button homeButton) {
        this(nomUtilisateurLabel, retourButton, affectationsContainer, nomUtilisateur);
        this.homeButton = homeButton;
        setupHomeButtonListener();
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
    }
    
    private void setupListeners() {
        // Listener pour le bouton retour (si il existe)
        if (retourButton != null) {
            retourButton.setOnAction(this::handleRetour);
        }
    }
    
    private void setupHomeButtonListener() {
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
    }
    
    private void populateAffectations() {
        // Vider le container
        affectationsContainer.getChildren().clear();
        
        // Ajouter chaque affectation
        for (AffectationsModel.Affectation affectation : model.getAffectations()) {
            HBox affectationRow = createAffectationRow(affectation);
            affectationsContainer.getChildren().add(affectationRow);
        }
    }
    
    private HBox createAffectationRow(AffectationsModel.Affectation affectation) {
        HBox row = new HBox();
        row.getStyleClass().add("tableau-row");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPrefHeight(50);
        
        // Cellule Date
        Label dateCell = new Label(affectation.getDate());
        dateCell.getStyleClass().add("tableau-cell");
        dateCell.setPrefWidth(200);
        
        // Cellule Site Olympique
        Label siteCell = new Label(affectation.getSiteOlympique());
        siteCell.getStyleClass().add("tableau-cell");
        siteCell.setPrefWidth(300);
        
        // Cellule Type de Dispositif
        Label typeCell = new Label(affectation.getTypeDispositif());
        typeCell.getStyleClass().add("tableau-cell");
        typeCell.setPrefWidth(324);
        
        row.getChildren().addAll(dateCell, siteCell, typeCell);
        
        return row;
    }
    
    private void handleRetour(ActionEvent event) {
        System.out.println("Retour au tableau de bord depuis Affectations");
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            navigateToDashboard();
        }
    }
    
    private void handleHome(ActionEvent event) {
        System.out.println("Navigation vers le tableau de bord via bouton Home");
        navigateToDashboard();
    }
    
    private void navigateToDashboard() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) (homeButton != null ? homeButton.getScene().getWindow() : 
                                         (retourButton != null ? retourButton.getScene().getWindow() : null));
            
            if (currentStage != null) {
                // Créer la nouvelle vue du dashboard
                DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
                Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
                currentStage.setScene(dashboardScene);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Méthodes publiques pour interaction externe
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public AffectationsModel getModel() {
        return model;
    }
    
    public void refreshAffectations() {
        populateAffectations();
    }
}