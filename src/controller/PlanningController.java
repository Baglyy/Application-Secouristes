package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import model.PlanningModel;
import model.AffectationsModel;
import view.DashboardView;
import java.time.LocalDate;
import java.time.YearMonth;

public class PlanningController {
    
    private Label nomUtilisateurLabel;
    private Button retourButton;
    private Button homeButton;
    private Button precedentMoisButton;
    private Button suivantMoisButton;
    private Button aujourdHuiButton;
    private Label moisAnneeLabel;
    private GridPane calendrierGrid;
    private PlanningModel model;
    private Runnable onRetourCallback;
    
    public PlanningController(Label nomUtilisateurLabel, Button retourButton, Button homeButton,
                             Button precedentMoisButton, Button suivantMoisButton, Button aujourdHuiButton,
                             Label moisAnneeLabel, GridPane calendrierGrid, String nomUtilisateur) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.retourButton = retourButton;
        this.homeButton = homeButton;
        this.precedentMoisButton = precedentMoisButton;
        this.suivantMoisButton = suivantMoisButton;
        this.aujourdHuiButton = aujourdHuiButton;
        this.moisAnneeLabel = moisAnneeLabel;
        this.calendrierGrid = calendrierGrid;
        this.model = new PlanningModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
        
        // Écouter les changements de mois pour mettre à jour le calendrier
        model.moisActuelProperty().addListener((obs, oldMois, newMois) -> {
            updateCalendrier();
        });
    }
    
    private void setupListeners() {
        // Listeners pour les boutons de navigation
        if (retourButton != null) {
            retourButton.setOnAction(this::handleRetour);
        }
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
        
        // Listeners pour la navigation du calendrier
        precedentMoisButton.setOnAction(this::handleMoisPrecedent);
        suivantMoisButton.setOnAction(this::handleMoisSuivant);
        aujourdHuiButton.setOnAction(this::handleAujourdHui);
    }
    
    private void updateCalendrier() {
        // Mettre à jour le label du mois
        String moisAnneeText = model.getMoisAnneeString();
        moisAnneeLabel.setText(moisAnneeText.substring(0, 1).toUpperCase() + moisAnneeText.substring(1));
        
        // Vider le calendrier
        calendrierGrid.getChildren().clear();
        
        YearMonth moisActuel = model.getMoisActuel();
        LocalDate premierJour = model.getPremierJourDuMois();
        int nombreJours = model.getNombreJoursDansMois();
        int premierJourSemaine = model.getPremierJourSemaine();
        LocalDate aujourdhui = LocalDate.now();
        
        int ligne = 0;
        int colonne = premierJourSemaine - 1; // Ajuster pour commencer au bon jour de la semaine
        
        // Créer les cellules pour chaque jour du mois
        for (int jour = 1; jour <= nombreJours; jour++) {
            LocalDate dateJour = premierJour.withDayOfMonth(jour);
            
            VBox celluleJour = createCelluleJour(dateJour, aujourdhui);
            
            calendrierGrid.add(celluleJour, colonne, ligne);
            
            colonne++;
            if (colonne >= 7) {
                colonne = 0;
                ligne++;
            }
        }
    }
    
    private VBox createCelluleJour(LocalDate date, LocalDate aujourdhui) {
        VBox cellule = new VBox();
        cellule.setAlignment(Pos.TOP_CENTER);
        cellule.setPrefSize(120, 80);
        cellule.setPadding(new Insets(5));
        cellule.getStyleClass().add("calendrier-cellule");
        
        // Ajouter la classe pour aujourd'hui
        if (date.equals(aujourdhui)) {
            cellule.getStyleClass().add("calendrier-cellule-aujourdhui");
        }
        
        // Label pour le numéro du jour
        Label numeroJour = new Label(String.valueOf(date.getDayOfMonth()));
        numeroJour.getStyleClass().add("calendrier-numero-jour");
        
        // Vérifier s'il y a une affectation pour cette date
        AffectationsModel.Affectation affectation = model.getAffectationPourDate(date);
        if (affectation != null) {
            cellule.getStyleClass().add("calendrier-cellule-affectation");
            
            // Indicateur d'affectation
            Label indicateur = new Label("●");
            indicateur.getStyleClass().add("calendrier-indicateur-affectation");
            
            // Tooltip avec les détails de l'affectation
            Tooltip tooltip = new Tooltip(
                "Affectation du " + affectation.getDate() + "\n" +
                "Site: " + affectation.getSiteOlympique() + "\n" +
                "Type: " + affectation.getTypeDispositif()
            );
            tooltip.getStyleClass().add("calendrier-tooltip");
            Tooltip.install(cellule, tooltip);
            
            cellule.getChildren().addAll(numeroJour, indicateur);
        } else {
            cellule.getChildren().add(numeroJour);
        }
        
        return cellule;
    }
    
    private void handleMoisPrecedent(ActionEvent event) {
        System.out.println("Navigation vers le mois précédent");
        model.moisPrecedent();
    }
    
    private void handleMoisSuivant(ActionEvent event) {
        System.out.println("Navigation vers le mois suivant");
        model.moisSuivant();
    }
    
    private void handleAujourdHui(ActionEvent event) {
        System.out.println("Navigation vers le mois actuel");
        model.allerAujourdHui();
    }
    
    private void handleRetour(ActionEvent event) {
        System.out.println("Retour au tableau de bord depuis Planning");
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
    
    public PlanningModel getModel() {
        return model;
    }
    
    public void refreshCalendrier() {
        updateCalendrier();
    }
}