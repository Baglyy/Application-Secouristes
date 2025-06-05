package controller;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.AdminSecouristesModel;
import model.data.Secouriste;
import model.data.Competence;
import view.AdminDashboardView;

public class AdminSecouristesController {
    
    private Button modifierCompetencesButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<Secouriste> listView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminSecouristesModel model;
    private Runnable onRetourCallback;
    
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
    
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        listView.setItems(model.getSecouristes());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            modifierCompetencesButton.setDisable(!isSelected);
            supprimerButton.setDisable(!isSelected);
        });
    }
    
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
        modifierCompetencesButton.setOnAction(this::handleModifierCompetences);
        ajouterButton.setOnAction(this::handleAjouter);
        supprimerButton.setOnAction(this::handleSupprimer);
    }
    
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
    
    private void handleModifierCompetences(ActionEvent event) {
        Secouriste selectedSecouriste = listView.getSelectionModel().getSelectedItem();
        if (selectedSecouriste == null) return;
        
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier les compétences");
        
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));
        
        Label title = new Label("Compétences de " + selectedSecouriste.getNom() + " " + selectedSecouriste.getPrenom());
        ListView<model.data.Competence> competencesList = new ListView<>();
        competencesList.setItems(model.getAllCompetences());
        
        competencesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (selectedSecouriste.getCompetences() != null) {
            for (Competence comp : selectedSecouriste.getCompetences()) {
                competencesList.getSelectionModel().select(comp);
            }
        }
        
        Button saveButton = new Button("Enregistrer");
        saveButton.getStyleClass().addAll("dashboard-button", "active-button");
        saveButton.setOnAction(e -> {
            model.updateSecouristeCompetences(
                selectedSecouriste,
                competencesList.getSelectionModel().getSelectedItems()
            );
            popupStage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText("Compétences mises à jour");
            alert.setContentText("Les compétences ont été mises à jour avec succès.");
            alert.showAndWait();
        });
        
        popupContent.getChildren().addAll(title, competencesList, saveButton);
        
        Scene popupScene = new Scene(popupContent, 400, 400);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    private void handleAjouter(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter un secouriste");
        
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));
        
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
                Secouriste newSecouriste = new Secouriste(
                    System.currentTimeMillis(),
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
            nomField,
            prenomField,
            dateNaissanceField,
            emailField,
            telField,
            adresseField,
            saveButton
        );
        
        Scene popupScene = new Scene(popupContent, 300, 400);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
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
    
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public AdminSecouristesModel getModel() {
        return model;
    }
}