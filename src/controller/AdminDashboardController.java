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
import view.AdminCompetencesView; // Ajout de l'import
import view.LoginView;
import java.sql.SQLException;
import model.ExportCSV;

public class AdminDashboardController {
    
    private Button dispositifsButton;
    private Button affectationsSecouristesButton;
    private Button secouristesButton;
    private Button competencesButton; // Nouveau bouton
    private Button deconnexionButton;
    private Label nomUtilisateurLabel;
    private Button exportCsvButton;
    private AdminDashboardModel model;
    
    public AdminDashboardController(Button dispositifsButton, Button affectationsSecouristesButton, 
                                   Button secouristesButton, Button competencesButton, 
                                   Button deconnexionButton, Label nomUtilisateurLabel, Button exportCsvButton) {
        this.dispositifsButton = dispositifsButton;
        this.affectationsSecouristesButton = affectationsSecouristesButton;
        this.secouristesButton = secouristesButton;
        this.competencesButton = competencesButton;
        this.deconnexionButton = deconnexionButton;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.exportCsvButton = exportCsvButton;
        this.model = new AdminDashboardModel();
        
        setupBindings();
        setupListeners();
        updateButtonStyles();
    }
    
    public AdminDashboardController(Button dispositifsButton, Button affectationsSecouristesButton, 
                                   Button secouristesButton, Button competencesButton, 
                                   Button deconnexionButton, Label nomUtilisateurLabel, Button exportCsvButton,
                                   String nomUtilisateur) {
        this(dispositifsButton, affectationsSecouristesButton, secouristesButton, competencesButton, 
             deconnexionButton, nomUtilisateurLabel, exportCsvButton);
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        model.sectionActiveProperty().addListener((obs, oldSection, newSection) -> {
            updateButtonStyles();
        });
    }
    
    private void setupListeners() {
        dispositifsButton.setOnAction(this::handleDispositifs);
        affectationsSecouristesButton.setOnAction(this::handleAffectationsSecouristes);
        secouristesButton.setOnAction(this::handleSecouristes);
        competencesButton.setOnAction(this::handleCompetences); // Nouveau listener
        deconnexionButton.setOnAction(this::handleDeconnexion);
        exportCsvButton.setOnAction(this::handleExportBDD);
    }
    
    private void handleDispositifs(ActionEvent event) {
        System.out.println("Navigation vers Dispositifs de secours");
        model.activerDispositifs();
        
        Stage currentStage = (Stage) dispositifsButton.getScene().getWindow();
        AdminDispositifsView adminDispositifsView = new AdminDispositifsView(model.getNomUtilisateur());
        adminDispositifsView.getController().setOnRetourCallback(() -> {
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        Scene dispositifsScene = new Scene(adminDispositifsView.getRoot(), 1024, 600);
        currentStage.setScene(dispositifsScene);
    }
    
    private void handleAffectationsSecouristes(ActionEvent event) {
        System.out.println("Navigation vers Affectations secouristes");
        model.activerAffectationsSecouristes();
        
        Stage currentStage = (Stage) affectationsSecouristesButton.getScene().getWindow();
        AdminAffectationsView adminAffectationsView = new AdminAffectationsView(model.getNomUtilisateur());
        adminAffectationsView.getController().setOnRetourCallback(() -> {
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        Scene affectationsScene = new Scene(adminAffectationsView.getRoot(), 1024, 600);
        currentStage.setScene(affectationsScene);
    }
    
    private void handleSecouristes(ActionEvent event) {
        System.out.println("Navigation vers Secouristes");
        model.activerSecouristes();
        
        Stage currentStage = (Stage) secouristesButton.getScene().getWindow();
        AdminSecouristesView adminSecouristesView = new AdminSecouristesView(model.getNomUtilisateur());
        adminSecouristesView.getController().setOnRetourCallback(() -> {
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        Scene secouristesScene = new Scene(adminSecouristesView.getRoot(), 1024, 600);
        currentStage.setScene(secouristesScene);
    }
    
    private void handleCompetences(ActionEvent event) {
        System.out.println("Navigation vers Compétences");
        model.setSectionActive("competences");
        
        Stage currentStage = (Stage) competencesButton.getScene().getWindow();
        AdminCompetencesView adminCompetencesView = new AdminCompetencesView(model.getNomUtilisateur());
        adminCompetencesView.getController().setOnRetourCallback(() -> {
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });
        Scene competencesScene = new Scene(adminCompetencesView.getRoot(), 1024, 600);
        currentStage.setScene(competencesScene);
    }
    
    private void handleDeconnexion(ActionEvent event) {
        System.out.println("Déconnexion de l'utilisateur");
        
        Stage currentStage = (Stage) deconnexionButton.getScene().getWindow();
        LoginView loginView = new LoginView(currentStage);
        Scene loginScene = new Scene(loginView.getRoot(), 1024, 600);
        
        try {
            loginScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        } catch (Exception cssException) {
            System.out.println("Attention: Fichier CSS style.css non trouvé, styles par défaut appliqués");
        }
        
        currentStage.setScene(loginScene);
        currentStage.setTitle("SecuOptix - Connexion");
        System.out.println("Retour à la page de connexion réussi !");
    }
    
    private void handleExportBDD(ActionEvent event) {
        try {
            ExportCSV.exporterTouteLaBase();
            System.out.println("Exportation réalisée avec succès.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void updateButtonStyles() {
        dispositifsButton.getStyleClass().removeAll("active-button");
        affectationsSecouristesButton.getStyleClass().removeAll("active-button");
        secouristesButton.getStyleClass().removeAll("active-button");
        competencesButton.getStyleClass().removeAll("active-button");
        exportCsvButton.getStyleClass().removeAll("active-button");
        
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
            case "competences":
                if (!competencesButton.getStyleClass().contains("active-button")) {
                    competencesButton.getStyleClass().add("active-button");
                }
                break;
            case "export":
                if (!exportCsvButton.getStyleClass().contains("active-button")) {
                    exportCsvButton.getStyleClass().add("active-button");
                }
                break;
        }
    }
    
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