package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DisponibilitesModel;
import view.DashboardView;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DisponibilitesController {
    
    private Label nomUtilisateurLabel;
    private Label moisLabel;
    private Button precedentButton;
    private Button suivantButton;
    private Button retourButton;
    private Button homeButton;
    private GridPane calendrierGrid;
    private DisponibilitesModel model;
    private Runnable onRetourCallback;
    
    public DisponibilitesController(Label nomUtilisateurLabel, Label moisLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid,
                                  String nomUtilisateur, Button homeButton) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.moisLabel = moisLabel;
        this.precedentButton = precedentButton;
        this.suivantButton = suivantButton;
        this.retourButton = retourButton;
        this.calendrierGrid = calendrierGrid;
        this.homeButton = homeButton;
        
        // Initialiser avec un ID temporaire, devra être mis à jour
        this.model = new DisponibilitesModel(nomUtilisateur, -1);
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }
    
    // Constructeur alternatif avec ID secouriste
    public DisponibilitesController(Label nomUtilisateurLabel, Label moisLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid,
                                  String nomUtilisateur, Button homeButton, long idSecouriste) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.moisLabel = moisLabel;
        this.precedentButton = precedentButton;
        this.suivantButton = suivantButton;
        this.retourButton = retourButton;
        this.calendrierGrid = calendrierGrid;
        this.homeButton = homeButton;
        
        // Initialiser avec l'ID secouriste correct
        this.model = new DisponibilitesModel(nomUtilisateur, idSecouriste);
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }
    
    // Méthode pour définir l'ID du secouriste après construction
    public void setIdSecouriste(long idSecouriste) {
        model.setIdSecouriste(idSecouriste);
        updateCalendrier(); // Recharger le calendrier avec les nouvelles données
    }
    
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        moisLabel.textProperty().bind(model.moisSelectionneProperty());
    }
    
    private void setupListeners() {
        precedentButton.setOnAction(this::handlePrecedent);
        suivantButton.setOnAction(this::handleSuivant);
        retourButton.setOnAction(this::handleRetour);
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
    }
    
    private void updateCalendrier() {
        calendrierGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        
        LocalDate date = LocalDate.of(model.getCurrentYear(), model.getCurrentMonth(), 1);
        int firstDayOfWeek = date.getDayOfWeek().getValue() - 1; // 0 = Monday
        int daysInMonth = date.lengthOfMonth();
        
        int row = 1;
        int col = firstDayOfWeek;
        for (int day = 1; day <= daysInMonth; day++) {
            Button cellButton = new Button(String.valueOf(day));
            cellButton.getStyleClass().add("calendrier-cell");
            
            final int currentDay = day;
            cellButton.setOnAction(e -> handleCellClick(currentDay));
            
            boolean disponible = model.isDisponible(currentDay);
            updateCellStyle(cellButton, disponible);
            
            calendrierGrid.add(cellButton, col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
    
    private void handlePrecedent(ActionEvent event) {
        model.previousMonth();
        updateCalendrier();
    }
    
    private void handleSuivant(ActionEvent event) {
        model.nextMonth();
        updateCalendrier();
    }
    
    private void handleRetour(ActionEvent event) {
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            navigateToDashboard();
        }
    }
    
    private void handleHome(ActionEvent event) {
        navigateToDashboard();
    }
    
    private void navigateToDashboard() {
        try {
            Stage currentStage = (Stage) (homeButton != null ? homeButton.getScene().getWindow() : 
                                         retourButton.getScene().getWindow());
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleCellClick(int day) {
        System.out.println("Clic sur le jour " + day + " (idSecouriste=" + model.getIdSecouriste() + ")");
        model.toggleDisponibilite(day);
        updateCalendrier();
    }
    
    private void updateCellStyle(Button button, boolean disponible) {
        button.getStyleClass().removeAll("disponible-cell", "indisponible-cell");
        if (disponible) {
            button.getStyleClass().add("disponible-cell");
        } else {
            button.getStyleClass().add("indisponible-cell");
        }
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public DisponibilitesModel getModel() {
        return model;
    }
}