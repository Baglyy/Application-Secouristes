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
                                VBox affectationsContainer, String nomUtilisateur, 
                                Button homeButton) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.retourButton = retourButton;
        this.affectationsContainer = affectationsContainer;
        this.homeButton = homeButton;
        this.model = new AffectationsModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
        populateAffectations();
    }
    
    private void setupBindings() {
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
    }
    
    private void setupListeners() {
        if (retourButton != null) {
            retourButton.setOnAction(this::handleRetour);
        }
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
    }
    
    private void populateAffectations() {
        affectationsContainer.getChildren().clear();
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
        
        Label dateCell = new Label(affectation.getDate());
        dateCell.getStyleClass().add("tableau-cell");
        dateCell.setPrefWidth(200);
        
        Label siteCell = new Label(affectation.getSiteOlympique());
        siteCell.getStyleClass().add("tableau-cell");
        siteCell.setPrefWidth(300);
        
        Label secouristesCell = new Label(affectation.getSecouristes());
        secouristesCell.getStyleClass().add("tableau-cell");
        secouristesCell.setPrefWidth(324);
        
        row.getChildren().addAll(dateCell, siteCell, secouristesCell);
        
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
            Stage currentStage = (Stage) (homeButton != null ? homeButton.getScene().getWindow() : 
                                         (retourButton != null ? retourButton.getScene().getWindow() : null));
            
            if (currentStage != null) {
                DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
                Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
                currentStage.setScene(dashboardScene);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
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