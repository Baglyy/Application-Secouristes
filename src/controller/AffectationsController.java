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

/**
 * Contrôleur de l'affichage des affectations pour un secouriste.
 * Gère la logique d'affichage, de retour au tableau de bord, et de rafraîchissement des affectations.
 */
public class AffectationsController {

    private Label nomUtilisateurLabel;
    private Button retourButton;
    private Button homeButton;
    private VBox affectationsContainer;
    private AffectationsModel model;
    private Runnable onRetourCallback;

    /**
     * Constructeur du contrôleur AffectationsController.
     *
     * @param nomUtilisateurLabel    Label contenant le nom de l'utilisateur connecté
     * @param retourButton           Bouton de retour à l'écran précédent
     * @param affectationsContainer  Conteneur VBox pour afficher les lignes d'affectation
     * @param nomUtilisateur         Nom de l'utilisateur connecté
     * @param homeButton             Bouton de retour à l'accueil (dashboard)
     */
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

    /**
     * Liaisons entre les propriétés du modèle et les composants de la vue.
     */
    private void setupBindings() {
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
    }

    /**
     * Mise en place des gestionnaires d'événements pour les boutons.
     */
    private void setupListeners() {
        if (retourButton != null) {
            retourButton.setOnAction(this::handleRetour);
        }
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
    }

    /**
     * Remplit le conteneur d'affichage avec les données d'affectation du modèle.
     */
    private void populateAffectations() {
        affectationsContainer.getChildren().clear();
        for (AffectationsModel.Affectation affectation : model.getAffectations()) {
            HBox affectationRow = createAffectationRow(affectation);
            affectationsContainer.getChildren().add(affectationRow);
        }
    }

    /**
     * Crée une ligne d'affichage HBox pour une affectation donnée.
     *
     * @param affectation L'affectation à afficher
     * @return HBox contenant les informations de l'affectation
     */
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

        // MODIFICATION: Calcul du nombre de secouristes + 1 (pour inclure l'utilisateur actuel)
        String secouristesStr = affectation.getSecouristes();
        int nombreSecouristes = 1; // L'utilisateur actuel est toujours compté

        if (secouristesStr != null && !secouristesStr.trim().isEmpty()) {
            // Compter le nombre de lignes (retours à la ligne) pour avoir le nombre de secouristes
            String[] lignes = secouristesStr.split("\n");
            nombreSecouristes = lignes.length + 1; // +1 pour inclure l'utilisateur actuel
        }

        Label secouristesCell = new Label(String.valueOf(nombreSecouristes));
        secouristesCell.getStyleClass().add("tableau-cell");
        secouristesCell.setPrefWidth(324);

        row.getChildren().addAll(dateCell, siteCell, secouristesCell);

        return row;
    }

    /**
     * Gère le clic sur le bouton retour.
     *
     * @param event L'événement ActionEvent déclenché
     */
    private void handleRetour(ActionEvent event) {
        System.out.println("Retour au tableau de bord depuis Affectations");
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            navigateToDashboard();
        }
    }

    /**
     * Gère le clic sur le bouton home (retour à l'accueil).
     *
     * @param event L'événement ActionEvent déclenché
     */
    private void handleHome(ActionEvent event) {
        System.out.println("Navigation vers le tableau de bord via bouton Home");
        navigateToDashboard();
    }

    /**
     * Navigue vers le tableau de bord (DashboardView).
     */
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

    /**
     * Définit le nom de l'utilisateur dans le modèle.
     *
     * @param nomUtilisateur nom de l'utilisateur
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Définit le comportement à exécuter lors du retour.
     *
     * @param callback action à exécuter au retour
     */
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }

    /**
     * Retourne le modèle associé à ce contrôleur.
     *
     * @return le modèle AffectationsModel
     */
    public AffectationsModel getModel() {
        return model;
    }

    /**
     * Rafraîchit les données affichées des affectations.
     */
    public void refreshAffectations() {
        populateAffectations();
    }
}
