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

/**
 * Contrôleur du tableau de bord (Dashboard) pour les secouristes.
 * Permet de naviguer entre les vues : Affectations, Planning, Disponibilités et Connexion.
 */
public class DashboardController {
    
    private Button affectationsButton;
    private Button planningButton;
    private Button disponibilitesButton;
    private Button deconnexionButton;
    private Label nomUtilisateurLabel;
    private DashboardModel model;

    /**
     * Constructeur sans nom d'utilisateur défini.
     *
     * @param affectationsButton     Bouton pour accéder à la vue des affectations
     * @param planningButton         Bouton pour accéder à la vue du planning
     * @param disponibilitesButton   Bouton pour accéder à la vue des disponibilités
     * @param deconnexionButton      Bouton pour se déconnecter
     * @param nomUtilisateurLabel    Label affichant le nom de l'utilisateur
     */
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

    /**
     * Constructeur avec nom d'utilisateur défini.
     *
     * @param affectationsButton     Bouton pour accéder à la vue des affectations
     * @param planningButton         Bouton pour accéder à la vue du planning
     * @param disponibilitesButton   Bouton pour accéder à la vue des disponibilités
     * @param deconnexionButton      Bouton pour se déconnecter
     * @param nomUtilisateurLabel    Label affichant le nom de l'utilisateur
     * @param nomUtilisateur         Nom de l'utilisateur à afficher
     */
    public DashboardController(Button affectationsButton, Button planningButton, 
                              Button disponibilitesButton, Button deconnexionButton, Label nomUtilisateurLabel, 
                              String nomUtilisateur) {
        this(affectationsButton, planningButton, disponibilitesButton, deconnexionButton, nomUtilisateurLabel);
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Met en place les bindings entre le modèle et les éléments de la vue.
     */
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        
        // Écouter les changements de section pour mettre à jour les styles
        model.sectionActiveProperty().addListener((obs, oldSection, newSection) -> {
            updateButtonStyles();
        });
    }

    /**
     * Associe les événements aux boutons du tableau de bord.
     */
    private void setupListeners() {
        // Listeners pour les boutons
        affectationsButton.setOnAction(this::handleAffectations);
        planningButton.setOnAction(this::handlePlanning);
        disponibilitesButton.setOnAction(this::handleDisponibilites);
        deconnexionButton.setOnAction(this::handleDeconnexion);
    }

    /**
     * Gère la navigation vers la vue Affectations.
     *
     * @param event Événement de clic
     */
    private void handleAffectations(ActionEvent event) {
        System.out.println("Navigation vers Affectations");
        model.activerAffectations();

        Stage currentStage = (Stage) affectationsButton.getScene().getWindow();
        AffectationsView affectationsView = new AffectationsView(model.getNomUtilisateur());

        affectationsView.getController().setOnRetourCallback(() -> {
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });

        Scene affectationsScene = new Scene(affectationsView.getRoot(), 1024, 600);
        currentStage.setScene(affectationsScene);
    }

    /**
     * Gère la navigation vers la vue Planning.
     *
     * @param event Événement de clic
     */
    private void handlePlanning(ActionEvent event) {
        System.out.println("Navigation vers Planning");
        model.activerPlanning();

        Stage currentStage = (Stage) planningButton.getScene().getWindow();
        PlanningView planningView = new PlanningView(model.getNomUtilisateur());

        planningView.getController().setOnRetourCallback(() -> {
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });

        Scene planningScene = new Scene(planningView.getRoot(), 1024, 600);
        currentStage.setScene(planningScene);
    }

    /**
     * Gère la navigation vers la vue Disponibilités.
     *
     * @param event Événement de clic
     */
    private void handleDisponibilites(ActionEvent event) {
        System.out.println("Navigation vers Disponibilités");
        model.activerDisponibilites();

        Stage currentStage = (Stage) disponibilitesButton.getScene().getWindow();
        DisponibilitesView disponibilitesView = new DisponibilitesView(model.getNomUtilisateur());

        disponibilitesView.getController().setOnRetourCallback(() -> {
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        });

        Scene disponibilitesScene = new Scene(disponibilitesView.getRoot(), 1024, 600);
        currentStage.setScene(disponibilitesScene);
    }

    /**
     * Gère la déconnexion de l'utilisateur.
     *
     * @param event Événement de clic
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
     * Met à jour le style des boutons en fonction de la section active.
     */
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

    /**
     * Définit le nom d'utilisateur dans le modèle.
     *
     * @param nomUtilisateur le nom d'utilisateur
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Retourne la section active dans le modèle.
     *
     * @return la section active sous forme de chaîne
     */
    public String getSectionActive() {
        return model.getSectionActive();
    }

    /**
     * Retourne le modèle utilisé par le contrôleur.
     *
     * @return une instance de DashboardModel
     */
    public DashboardModel getModel() {
        return model;
    }
}
