package controller;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import model.AdminSecouristesModel;
import model.data.Secouriste;
import model.data.Competence;
import view.AdminDashboardView;

/**
 * Contrôleur pour la gestion des secouristes dans l'interface administrateur.
 * Gère les actions d'ajout, suppression, et modification des compétences des secouristes.
 */
public class AdminSecouristesController {

    private Button modifierCompetencesButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<Secouriste> listView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminSecouristesModel model;
    private Runnable onRetourCallback;

    /**
     * Constructeur du contrôleur.
     *
     * @param modifierCompetencesButton bouton pour modifier les compétences
     * @param ajouterButton             bouton pour ajouter un secouriste
     * @param supprimerButton           bouton pour supprimer un secouriste
     * @param listView                  liste des secouristes affichés
     * @param nomUtilisateurLabel       label affichant le nom de l'utilisateur
     * @param homeIcon                  icône permettant de revenir à l'accueil
     * @param nomUtilisateur            nom de l'utilisateur connecté
     */
    public AdminSecouristesController(
            Button modifierCompetencesButton,
            Button ajouterButton,
            Button supprimerButton,
            ListView<Secouriste> listView,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.modifierCompetencesButton = modifierCompetencesButton;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.listView = listView;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminSecouristesModel(nomUtilisateur);

        setupBindings();
        setupListeners();
    }

    /**
     * Met en place les liaisons entre le modèle et la vue.
     */
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        listView.setItems(model.getSecouristes());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            modifierCompetencesButton.setDisable(!isSelected);
            supprimerButton.setDisable(!isSelected);
        });
    }

    /**
     * Met en place les écouteurs d’événements.
     */
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
        modifierCompetencesButton.setOnAction(this::handleModifierCompetences);
        ajouterButton.setOnAction(this::handleAjouter);
        supprimerButton.setOnAction(this::handleSupprimer);
    }

    /**
     * Gère le retour vers le tableau de bord administrateur.
     */
    private void handleRetour() {
        System.out.println("Retour vers le tableau de bord administrateur");
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            Stage currentStage = (Stage) homeIcon.getScene().getWindow();
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        }
    }

    /**
     * Gère la modification des compétences d’un secouriste.
     *
     * @param event événement déclenché
     */
    private void handleModifierCompetences(ActionEvent event) {
        Secouriste selectedSecouriste = listView.getSelectionModel().getSelectedItem();
        if (selectedSecouriste == null) return;

        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier les compétences");

        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));

        Label title = new Label("Compétences de " + selectedSecouriste.getNom() + " " + selectedSecouriste.getPrenom());

        VBox competencesBox = new VBox(5);
        for (Competence comp : model.getAllCompetences()) {
            CheckBox checkBox = new CheckBox(comp.getIntitule());
            checkBox.setUserData(comp);
            if (selectedSecouriste.getCompetences() != null &&
                selectedSecouriste.getCompetences().contains(comp)) {
                checkBox.setSelected(true);
            }
            competencesBox.getChildren().add(checkBox);
        }

        Button saveButton = new Button("Enregistrer");
        saveButton.getStyleClass().addAll("dashboard-button", "active-button");
        saveButton.setOnAction(e -> {
            ObservableList<Competence> selectedCompetences = FXCollections.observableArrayList();
            for (javafx.scene.Node node : competencesBox.getChildren()) {
                if (node instanceof CheckBox checkBox && checkBox.isSelected()) {
                    selectedCompetences.add((Competence) checkBox.getUserData());
                }
            }

            model.updateSecouristeCompetences(selectedSecouriste, selectedCompetences);
            listView.refresh();
            popupStage.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Compétences mises à jour");
            alert.setContentText("Les compétences ont été mises à jour avec succès.");
            alert.showAndWait();
        });

        popupContent.getChildren().addAll(title, competencesBox, saveButton);

        Scene popupScene = new Scene(popupContent, 400, 400);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    /**
     * Gère l'ajout d’un nouveau secouriste.
     *
     * @param event événement déclenché
     */
    private void handleAjouter(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter un secouriste");

        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("Identifiant (numérique)");
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        TextField dateNaissanceField = new TextField();
        dateNaissanceField.setPromptText("Date de naissance (yyyy-MM-dd)");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField telField = new TextField();
        telField.setPromptText("Téléphone");
        TextField adresseField = new TextField();
        adresseField.setPromptText("Adresse");

        Button saveButton = new Button("Ajouter");
        saveButton.getStyleClass().addAll("dashboard-button", "active-button");
        saveButton.setOnAction(e -> {
            try {
                String idText = idField.getText().trim();
                long id;
                try {
                    id = Long.parseLong(idText);
                    if (id <= 0) {
                        throw new IllegalArgumentException("L'identifiant doit être un nombre positif.");
                    }
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("L'identifiant doit être un nombre valide.");
                }

                if (model.idExists(id)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Identifiant existant");
                    alert.setContentText("Un secouriste avec cet identifiant existe déjà.");
                    alert.showAndWait();
                    return;
                }

                Secouriste newSecouriste = new Secouriste(
                    id,
                    nomField.getText(),
                    prenomField.getText(),
                    dateNaissanceField.getText(),
                    emailField.getText(),
                    telField.getText(),
                    adresseField.getText()
                );

                if (model.secouristeExists(newSecouriste.getNom(), newSecouriste.getPrenom())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Secouriste existant");
                    alert.setContentText("Un secouriste avec ce nom et prénom existe déjà.");
                    alert.showAndWait();
                    return;
                }

                model.ajouterSecouriste(newSecouriste);
                popupStage.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Secouriste ajouté");
                alert.setContentText("Le secouriste a été ajouté avec succès.");
                alert.showAndWait();

                listView.getSelectionModel().selectLast();
                listView.scrollTo(newSecouriste);
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Données invalides");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });

        popupContent.getChildren().addAll(
            new Label("Nouveau secouriste"),
            idField,
            nomField,
            prenomField,
            dateNaissanceField,
            emailField,
            telField,
            adresseField,
            saveButton
        );

        Scene popupScene = new Scene(popupContent, 300, 450);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    /**
     * Gère la suppression d’un secouriste sélectionné.
     *
     * @param event événement déclenché
     */
    private void handleSupprimer(ActionEvent event) {
        Secouriste selectedSecouriste = listView.getSelectionModel().getSelectedItem();
        if (selectedSecouriste == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun secouriste sélectionné");
            alert.setContentText("Veuillez sélectionner un secouriste à supprimer.");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer le secouriste");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce secouriste ?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                model.supprimerSecouriste(selectedSecouriste);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Secouriste supprimé");
                successAlert.setHeaderText("Suppression réussie");
                successAlert.setContentText("Le secouriste a été supprimé de la liste.");
                successAlert.showAndWait();
            }
        });
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
     * Définit la fonction de rappel à exécuter au retour.
     *
     * @param callback fonction de rappel
     */
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }

    /**
     * Retourne le modèle associé à ce contrôleur.
     *
     * @return modèle admin secouristes
     */
    public AdminSecouristesModel getModel() {
        return model;
    }
}
