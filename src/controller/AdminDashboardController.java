package controller;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.AdminDashboardModel;
import view.AdminDashboardView;
import view.AdminSecouristesView;
import view.AdminAffectationsView;
import view.AdminDispositifsView;

public class AdminDashboardController {
    
    private Button dispositifsButton;
    private Button affectationsSecouristesButton;
    private Button secouristesButton;
    private Label nomUtilisateurLabel;
    private AdminDashboardModel model;
    
    public AdminDashboardController(Button dispositifsButton, Button affectationsSecouristesButton, 
                                   Button secouristesButton, Label nomUtilisateurLabel) {
        this.dispositifsButton = dispositifsButton;
        this.affectationsSecouristesButton = affectationsSecouristesButton;
        this.secouristesButton = secouristesButton;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.model = new AdminDashboardModel();
        
        setupBindings();
        setupListeners();
        updateButtonStyles();
    }
    
    public AdminDashboardController(Button dispositifsButton, Button affectationsSecouristesButton, 
                                   Button secouristesButton, Label nomUtilisateurLabel, 
                                   String nomUtilisateur) {
        this(dispositifsButton, affectationsSecouristesButton, secouristesButton, nomUtilisateurLabel);
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
        dispositifsButton.setOnAction(this::handleDispositifs);
        affectationsSecouristesButton.setOnAction(this::handleAffectationsSecouristes);
        secouristesButton.setOnAction(this::handleSecouristes);
    }
    
    private void handleDispositifs(ActionEvent event) {
        System.out.println("Navigation vers Dispositifs de secours");
        model.activerDispositifs();
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) dispositifsButton.getScene().getWindow();
        
        // Créer la nouvelle vue
        AdminDispositifsView adminDispositifsView = new AdminDispositifsView(model.getNomUtilisateur());
        
        // Configurer le retour vers le dashboard
        adminDispositifsView.getController().setOnRetourCallback(() -> {
            // Recréer la vue du dashboard
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        
        // Changer la scène
        Scene dispositifsScene = new Scene(adminDispositifsView.getRoot(), 1024, 600);
        currentStage.setScene(dispositifsScene);
    }
    
    private void handleAffectationsSecouristes(ActionEvent event) {
        System.out.println("Navigation vers Affectations secouristes");
        model.activerAffectationsSecouristes();
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) affectationsSecouristesButton.getScene().getWindow();
        
        // Créer la nouvelle vue
        AdminAffectationsView adminAffectationsView = new AdminAffectationsView(model.getNomUtilisateur());
        
        // Configurer le retour vers le dashboard
        adminAffectationsView.getController().setOnRetourCallback(() -> {
            // Recréer la vue du dashboard
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        
        // Changer la scène
        Scene affectationsScene = new Scene(adminAffectationsView.getRoot(), 1024, 600);
        currentStage.setScene(affectationsScene);
    }
    
    private void handleSecouristes(ActionEvent event) {
        System.out.println("Navigation vers Secouristes");
        model.activerSecouristes();
        
        // Récupérer la fenêtre actuelle
        Stage currentStage = (Stage) secouristesButton.getScene().getWindow();
        
        // Créer la nouvelle vue
        AdminSecouristesView adminSecouristesView = new AdminSecouristesView(model.getNomUtilisateur());
        
        // Configurer le retour vers le dashboard
        adminSecouristesView.getController().setOnRetourCallback(() -> {
            // Recréer la vue du dashboard
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        
        // Changer la scène
        Scene secouristesScene = new Scene(adminSecouristesView.getRoot(), 1024, 600);
        currentStage.setScene(secouristesScene);
    }
    
    private void updateButtonStyles() {
        // Réinitialiser tous les boutons
        dispositifsButton.getStyleClass().removeAll("active-button");
        affectationsSecouristesButton.getStyleClass().removeAll("active-button");
        secouristesButton.getStyleClass().removeAll("active-button");
        
        // Ajouter la classe active au bouton sélectionné
        switch (model.getSectionActive()) {
            case "dispositifs":
                if (!dispositifsButton.getStyleClass().contains("active-button")) {
                    dispositifsButton.getStyleClass().add("active-button");
                }
                break;
            case "affectations_secouristes":
                if (!affectationsSecouristesButton.getStyleClass().contains("active-button")) {
                    affectationsSecouristesButton.getStyleClass().add("active-button");
                }
                break;
            case "secouristes":
                if (!secouristesButton.getStyleClass().contains("active-button")) {
                    secouristesButton.getStyleClass().add("active-button");
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
    
    public AdminDashboardModel getModel() {
        return model;
    }
}