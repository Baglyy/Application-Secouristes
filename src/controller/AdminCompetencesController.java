package controller;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.util.ArrayList;

import model.AdminCompetencesModel;
import model.data.Competence;
import view.AdminDashboardView;

public class AdminCompetencesController {
    
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<Competence> listView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminCompetencesModel model;
    private Runnable onRetourCallback;
    
    public AdminCompetencesController(
            Button ajouterButton,
            Button supprimerButton,
            ListView<Competence> listView,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.listView = listView;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminCompetencesModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
    }
    
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        listView.setItems(model.getCompetences());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            supprimerButton.setDisable(!isSelected);
        });
    }
    
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
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
    
    private void handleAjouter(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter une compétence");
        
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));
        
        TextField intituleField = new TextField();
        intituleField.setPromptText("Intitulé de la compétence");
        
        // Liste des prérequis
        Label prerequisLabel = new Label("Prérequis:");
        ListView<Competence> prerequisListView = new ListView<>();
        prerequisListView.setItems(model.getAllCompetences());
        prerequisListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        prerequisListView.setPrefHeight(150);
        
        Button saveButton = new Button("Ajouter");
        saveButton.getStyleClass().addAll("dashboard-button", "active-button");
        saveButton.setOnAction(e -> {
            try {
                String intitule = intituleField.getText().trim();
                if (intitule.isEmpty()) {
                    throw new IllegalArgumentException("L'intitulé de la compétence est requis.");
                }
                
                // Vérifier si la compétence existe déjà
                if (model.competenceExists(intitule)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Compétence existante");
                    alert.setContentText("Une compétence avec cet intitulé existe déjà.");
                    alert.showAndWait();
                    return;
                }
                
                Competence newCompetence = new Competence(intitule);
                ObservableList<Competence> selectedPrerequis = prerequisListView.getSelectionModel().getSelectedItems();
                newCompetence.setPrerequis(new ArrayList<>(selectedPrerequis));
                
                model.ajouterCompetence(newCompetence);
                popupStage.close();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Compétence ajoutée");
                alert.setContentText("La compétence a été ajoutée avec succès.");
                alert.showAndWait();
                
                listView.getSelectionModel().selectLast();
                listView.scrollTo(newCompetence);
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Données invalides");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        });
        
        popupContent.getChildren().addAll(
            new Label("Nouvelle compétence"),
            intituleField,
            prerequisLabel,
            prerequisListView,
            saveButton
        );
        
        Scene popupScene = new Scene(popupContent, 300, 350);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    private void handleSupprimer(ActionEvent event) {
        Competence selectedCompetence = listView.getSelectionModel().getSelectedItem();
        if (selectedCompetence == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune compétence sélectionnée");
            alert.setContentText("Veuillez sélectionner une compétence à supprimer.");
            alert.showAndWait();
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer la compétence");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette compétence ?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                model.supprimerCompetence(selectedCompetence);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Compétence supprimée");
                successAlert.setHeaderText("Suppression réussie");
                successAlert.setContentText("La compétence a été supprimée de la liste.");
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
    
    public AdminCompetencesModel getModel() {
        return model;
    }
}