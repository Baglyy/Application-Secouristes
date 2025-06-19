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

/**
 * Contrôleur de la vue du tableau de bord administrateur.
 * Gère la navigation entre les différentes sections (dispositifs, secouristes, compétences, etc.)
 * ainsi que les actions comme la déconnexion ou l'export de la base de données.
 */
public class AdminDashboardController {
    
    private Button dispositifsButton;
    private Button affectationsSecouristesButton;
    private Button secouristesButton;
    private Button competencesButton; // Nouveau bouton
    private Button deconnexionButton;
    private Label nomUtilisateurLabel;
    private Button exportCsvButton;
    private AdminDashboardModel model;

    /**
     * Constructeur principal du contrôleur.
     * @param dispositifsButton bouton pour accéder aux dispositifs
     * @param affectationsSecouristesButton bouton pour accéder aux affectations
     * @param secouristesButton bouton pour accéder aux secouristes
     * @param competencesButton bouton pour accéder aux compétences
     * @param deconnexionButton bouton de déconnexion
     * @param nomUtilisateurLabel label affichant le nom de l'utilisateur connecté
     * @param exportCsvButton bouton pour exporter la base de données en CSV
     */
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

    /**
     * Constructeur avec nom d'utilisateur.
     * @param dispositifsButton bouton pour les dispositifs
     * @param affectationsSecouristesButton bouton pour les affectations
     * @param secouristesButton bouton pour les secouristes
     * @param competencesButton bouton pour les compétences
     * @param deconnexionButton bouton de déconnexion
     * @param nomUtilisateurLabel label utilisateur
     * @param exportCsvButton bouton export
     * @param nomUtilisateur nom de l'utilisateur
     */
    public AdminDashboardController(Button dispositifsButton, Button affectationsSecouristesButton, 
                                   Button secouristesButton, Button competencesButton, 
                                   Button deconnexionButton, Label nomUtilisateurLabel, Button exportCsvButton,
                                   String nomUtilisateur) {
        this(dispositifsButton, affectationsSecouristesButton, secouristesButton, competencesButton, 
             deconnexionButton, nomUtilisateurLabel, exportCsvButton);
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Liaisons des propriétés JavaFX au modèle.
     */
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        model.sectionActiveProperty().addListener((obs, oldSection, newSection) -> {
            updateButtonStyles();
        });
    }

    /**
     * Mise en place des listeners sur les boutons.
     */
    private void setupListeners() {
        dispositifsButton.setOnAction(this::handleDispositifs);
        affectationsSecouristesButton.setOnAction(this::handleAffectationsSecouristes);
        secouristesButton.setOnAction(this::handleSecouristes);
        competencesButton.setOnAction(this::handleCompetences); // Nouveau listener
        deconnexionButton.setOnAction(this::handleDeconnexion);
        exportCsvButton.setOnAction(this::handleExportBDD);
    }

    /**
     * Gestion de la navigation vers la vue des dispositifs.
     * @param event événement déclencheur
     */
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

    /**
     * Gestion de la navigation vers la vue des affectations.
     * @param event événement déclencheur
     */
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

    /**
     * Gestion de la navigation vers la vue des secouristes.
     * @param event événement déclencheur
     */
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

    /**
     * Gestion de la navigation vers la vue des compétences.
     * @param event événement déclencheur
     */
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

    /**
     * Déconnecte l'utilisateur et retourne à l'écran de connexion.
     * @param event événement déclencheur
     */
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

    /**
     * Gère l'export de toute la base de données au format CSV.
     * @param event événement déclencheur
     */
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

    /**
     * Met à jour les styles CSS des boutons selon la section active.
     */
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

    /**
     * Définit le nom de l'utilisateur dans le modèle.
     * @param nomUtilisateur nom de l'utilisateur
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Retourne la section active actuelle.
     * @return section active
     */
    public String getSectionActive() {
        return model.getSectionActive();
    }

    /**
     * Retourne le modèle associé au contrôleur.
     * @return instance de AdminDashboardModel
     */
    public AdminDashboardModel getModel() {
        return model;
    }
}
