package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PlanningModel;
import model.AdminAffectationsModel;
import view.DashboardView;
import java.time.LocalDate;
import java.time.YearMonth;

public class PlanningController {
    
    private Label nomUtilisateurLabel;
    private Button homeButton;
    private Button precedentMoisButton;
    private Button suivantMoisButton;
    private Button aujourdHuiButton;
    private Label moisAnneeLabel;
    private GridPane calendrierGrid;
    private PlanningModel model;
    private Runnable onRetourCallback;
    
    public PlanningController(Label nomUtilisateurLabel, Button homeButton,
                             Button precedentMoisButton, Button suivantMoisButton, Button aujourdHuiButton,
                             Label moisAnneeLabel, GridPane calendrierGrid, String nomUtilisateur) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeButton = homeButton;
        this.precedentMoisButton = precedentMoisButton;
        this.suivantMoisButton = suivantMoisButton;
        this.aujourdHuiButton = aujourdHuiButton;
        this.moisAnneeLabel = moisAnneeLabel;
        this.calendrierGrid = calendrierGrid;
        this.model = new PlanningModel(nomUtilisateur, -1); // Placeholder idSecouriste
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }
    
    private void setupBindings() {
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
        model.moisActuelProperty().addListener((obs, oldMois, newMois) -> {
            updateCalendrier();
        });
    }
    
    private void setupListeners() {
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
        precedentMoisButton.setOnAction(this::handleMoisPrecedent);
        suivantMoisButton.setOnAction(this::handleMoisSuivant);
        aujourdHuiButton.setOnAction(this::handleAujourdHui);
    }
    
    private void updateCalendrier() {
        calendrierGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        
        YearMonth moisActuel = model.getMoisActuel();
        LocalDate premierJour = model.getPremierJourDuMois();
        int nombreJours = model.getNombreJoursDansMois();
        int premierJourSemaine = model.getPremierJourSemaine();
        LocalDate aujourdhui = LocalDate.now();
        
        int row = 1;
        int col = premierJourSemaine - 1;
        
        for (int jour = 1; jour <= nombreJours; jour++) {
            LocalDate dateJour = premierJour.withDayOfMonth(jour);
            Button cellButton = new Button(String.valueOf(jour));
            cellButton.getStyleClass().add("calendrier-cell");
            
            if (dateJour.equals(aujourdhui)) {
                cellButton.getStyleClass().add("aujourdhui-cell");
            } else if (model.hasAffectationPourDate(dateJour)) {
                cellButton.getStyleClass().add("affectation-cell");
                AdminAffectationsModel.Affectation affectation = model.getAffectationPourDate(dateJour);
                Tooltip tooltip = new Tooltip(
                    "Affectation du " + affectation.getDate() + "\n" +
                    "Site: " + affectation.getSitesOlympiques() + "\n" +
                    "Secouristes: " + affectation.getSecouristes()
                );
                cellButton.setTooltip(tooltip);
            } else {
                cellButton.getStyleClass().add("neutre-cell");
            }
            
            calendrierGrid.add(cellButton, col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
        
        String moisAnneeText = model.getMoisAnneeString();
        moisAnneeLabel.setText(moisAnneeText.substring(0, 1).toUpperCase() + moisAnneeText.substring(1));
    }
    
    private void handleMoisPrecedent(ActionEvent event) {
        model.moisPrecedent();
    }
    
    private void handleMoisSuivant(ActionEvent event) {
        model.moisSuivant();
    }
    
    private void handleAujourdHui(ActionEvent event) {
        model.allerAujourdHui();
    }
    
    private void handleHome(ActionEvent event) {
        navigateToDashboard();
    }
    
    private void navigateToDashboard() {
        try {
            Stage currentStage = (Stage) (homeButton != null ? homeButton.getScene().getWindow() : null);
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
    
    public PlanningModel getModel() {
        return model;
    }
    
    public void refreshCalendrier() {
        updateCalendrier();
    }
}