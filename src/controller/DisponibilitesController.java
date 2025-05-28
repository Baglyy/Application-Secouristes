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

public class DisponibilitesController {
    
    private Label nomUtilisateurLabel;
    private Label semaineLabel;
    private Button precedentButton;
    private Button suivantButton;
    private Button retourButton;
    private Button homeButton;
    private GridPane calendrierGrid;
    private DisponibilitesModel model;
    private Runnable onRetourCallback;
    
    public DisponibilitesController(Label nomUtilisateurLabel, Label semaineLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.semaineLabel = semaineLabel;
        this.precedentButton = precedentButton;
        this.suivantButton = suivantButton;
        this.retourButton = retourButton;
        this.calendrierGrid = calendrierGrid;
        this.model = new DisponibilitesModel();
        
        setupBindings();
        setupListeners();
        setupCalendrierListeners();
    }
    
    public DisponibilitesController(Label nomUtilisateurLabel, Label semaineLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid,
                                  String nomUtilisateur) {
        this(nomUtilisateurLabel, semaineLabel, precedentButton, suivantButton, 
             retourButton, calendrierGrid);
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public DisponibilitesController(Label nomUtilisateurLabel, Label semaineLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid,
                                  String nomUtilisateur, Button homeButton) {
        this(nomUtilisateurLabel, semaineLabel, precedentButton, suivantButton, 
             retourButton, calendrierGrid, nomUtilisateur);
        this.homeButton = homeButton;
        setupHomeButtonListener();
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
        
        // Liaison de la semaine avec le label
        semaineLabel.textProperty().bind(model.semaineSelectionneeProperty());
    }
    
    private void setupListeners() {
        // Listeners pour les boutons de navigation
        precedentButton.setOnAction(this::handlePrecedent);
        suivantButton.setOnAction(this::handleSuivant);
        retourButton.setOnAction(this::handleRetour);
    }
    
    private void setupHomeButtonListener() {
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
    }
    
    private void setupCalendrierListeners() {
        // Ajouter des listeners aux cellules du calendrier
        for (Node node : calendrierGrid.getChildren()) {
            if (node instanceof Button) {
                Button cellButton = (Button) node;
                cellButton.setOnAction(this::handleCellClick);
            }
        }
    }
    
    private void handlePrecedent(ActionEvent event) {
        // Logique pour aller à la semaine précédente
        String semaineActuelle = model.getSemaineSelectionnee();
        int numeroSemaine = extractWeekNumber(semaineActuelle);
        if (numeroSemaine > 1) {
            model.setSemaineSelectionnee("Semaine " + (numeroSemaine - 1));
        }
        System.out.println("Navigation vers semaine précédente");
    }
    
    private void handleSuivant(ActionEvent event) {
        // Logique pour aller à la semaine suivante
        String semaineActuelle = model.getSemaineSelectionnee();
        int numeroSemaine = extractWeekNumber(semaineActuelle);
        if (numeroSemaine < 52) {
            model.setSemaineSelectionnee("Semaine " + (numeroSemaine + 1));
        }
        System.out.println("Navigation vers semaine suivante");
    }
    
    private void handleRetour(ActionEvent event) {
        System.out.println("Retour au tableau de bord");
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
                                         retourButton.getScene().getWindow());
            
            // Créer la nouvelle vue du dashboard
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleCellClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        
        // Récupérer la position de la cellule dans la grille
        Integer rowIndex = GridPane.getRowIndex(clickedButton);
        Integer colIndex = GridPane.getColumnIndex(clickedButton);
        
        if (rowIndex != null && colIndex != null && rowIndex > 0 && colIndex > 0) {
            // Déterminer l'heure et le jour basés sur les indices
            String[] heures = {"9H - 10H", "10H - 12H", "12H - 14H", "14H - 16H"};
            String[] jours = {"LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI"};
            
            if (rowIndex - 1 < heures.length && colIndex - 1 < jours.length) {
                String heure = heures[rowIndex - 1];
                String jour = jours[colIndex - 1];
                
                // Toggle la disponibilité
                model.toggleDisponibilite(heure, jour);
                
                // Mettre à jour le style du bouton
                updateCellStyle(clickedButton, model.isDisponible(heure, jour));
            }
        }
    }
    
    private void updateCellStyle(Button button, boolean disponible) {
        button.getStyleClass().removeAll("disponible-cell", "indisponible-cell");
        if (disponible) {
            button.getStyleClass().add("disponible-cell");
        } else {
            button.getStyleClass().add("indisponible-cell");
        }
    }
    
    private int extractWeekNumber(String semaine) {
        try {
            return Integer.parseInt(semaine.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 14; // Valeur par défaut
        }
    }
    
    // Méthodes publiques pour interaction externe
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