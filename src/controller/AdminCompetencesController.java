package controller;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import model.AdminCompetencesModel;
import model.data.Competence;
import model.graphs.DAG;
import view.AdminDashboardView;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCompetencesController {
    
    private Button modifierButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<Competence> listView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminCompetencesModel model;
    private Runnable onRetourCallback;
    
    /**
     * Constructeur du contrôleur des compétences.
     *
     * @param modifierButton      bouton pour modifier une compétence
     * @param ajouterButton       bouton pour ajouter une compétence
     * @param supprimerButton     bouton pour supprimer une compétence
     * @param listView            liste des compétences affichées
     * @param nomUtilisateurLabel label pour afficher le nom de l'utilisateur
     * @param homeIcon            icône permettant le retour à l'accueil
     * @param nomUtilisateur      nom de l'utilisateur actuel
     */
    public AdminCompetencesController(
            Button modifierButton,
            Button ajouterButton,
            Button supprimerButton,
            ListView<Competence> listView,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.modifierButton = modifierButton;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.listView = listView;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminCompetencesModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
    }
    
    /**
     * Initialise les bindings entre les propriétés du modèle et les composants de l'interface.
     */
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        listView.setItems(model.getCompetences());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isSelected = newSelection != null;
            modifierButton.setDisable(!isSelected);
            supprimerButton.setDisable(!isSelected);
        });
    }
    
    /**
     * Configure les gestionnaires d'événements pour les boutons et icônes.
     */
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
        modifierButton.setOnAction(this::handleModifier);
        ajouterButton.setOnAction(this::handleAjouter);
        supprimerButton.setOnAction(this::handleSupprimer);
    }
    
    /**
     * Gère l'action de retour au tableau de bord administrateur.
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
     * Gère la modification d'une compétence sélectionnée.
     *
     * @param event événement déclenché par l'action
     */
    private void handleModifier(ActionEvent event) {
        Competence selectedCompetence = listView.getSelectionModel().getSelectedItem();
        if (selectedCompetence == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucune compétence sélectionnée");
            alert.setContentText("Veuillez sélectionner une compétence à modifier.");
            alert.showAndWait();
            return;
        }
        
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Modifier la compétence");
        
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));
        
        TextField intituleField = new TextField(selectedCompetence.getIntitule());
        intituleField.setPromptText("Intitulé de la compétence");
        
        Label prerequisLabel = new Label("Prérequis (maintenez Ctrl pour sélectionner plusieurs):");
        prerequisLabel.getStyleClass().add("secouriste-label");
        ListView<Competence> prerequisListView = new ListView<>();
        prerequisListView.setItems(model.getAllCompetences());
        prerequisListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        prerequisListView.setPrefHeight(200);
        
        if (selectedCompetence.getPrerequis() != null) {
            for (Competence prereq : selectedCompetence.getPrerequis()) {
                prereq.setIntitule(prereq.getIntitule());
                prerequisListView.getSelectionModel().select(prereq);
            }
        }
        
        Button saveButton = new Button("Enregistrer");
        saveButton.getStyleClass().addAll("dashboard-button", "active-button");
        saveButton.setOnAction(e -> {
            try {
                String newIntitule = intituleField.getText().trim();
                if (newIntitule.isEmpty()) {
                    throw new IllegalArgumentException("L'intitulé doit être requis.");
                }
                
                if (!newIntitule.equals(selectedCompetence.getIntitule()) && model.competenceExists(newIntitule)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Compétence existante");
                    alert.setContentText("Une compétence avec cet intitulé existe déjà.");
                    alert.showAndWait();
                    return;
                }
                
                Competence updatedCompetence = new Competence(newIntitule);
                ObservableList<Competence> selectedPrerequis = prerequisListView.getSelectionModel().getSelectedItems();
                updatedCompetence.setPrerequis(new ArrayList<>(selectedPrerequis));
                
                // Check for cycles using DAG
                DAG dag = new DAG();
                for (Competence comp : model.getAllCompetences()) {
                    if (!comp.equals(selectedCompetence)) {
                        List<String> prereqIntitules = comp.getPrerequis().stream()
                            .map(Competence::getIntitule)
                            .collect(Collectors.toList());
                        dag.ajouterCompetence(comp.getIntitule(), prereqIntitules);
                    }
                }
                List<String> newPrereqIntitules = updatedCompetence.getPrerequis().stream()
                    .map(Competence::getIntitule)
                    .collect(Collectors.toList());
                if (!dag.ajouterCompetence(newIntitule, newPrereqIntitules)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Cycle détecté");
                    alert.setContentText("La modification de cette compétence créerait un cycle dans les prérequis.");
                    alert.showAndWait();
                    return;
                }
                
                model.modifierCompetence(selectedCompetence, updatedCompetence);
                listView.refresh();
                
                popupStage.close();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Compétence modifiée");
                alert.setContentText("La compétence a été modifiée avec succès.");
                alert.showAndWait();
                
            } catch (IllegalArgumentException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Données invalides");
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de modification");
                alert.setContentText("Impossible de modifier la compétence : " + ex.getMessage());
                alert.showAndWait();
            }
        });
        
        popupContent.getChildren().addAll(
            new Label("Modifier la compétence"),
            intituleField,
            prerequisLabel,
            prerequisListView,
            saveButton
        );
        
        Scene popupScene = new Scene(popupContent, 300, 600);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    /**
     * Gère l'ajout d'une nouvelle compétence.
     *
     * @param event événement déclenché par l'action
     */
    private void handleAjouter(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Ajouter une compétence");
        
        VBox popupContent = new VBox(10);
        popupContent.setPadding(new Insets(20));
        
        TextField intituleField = new TextField();
        intituleField.setPromptText("Intitulé de la compétence");
        
        Label prerequisLabel = new Label("Prérequis (maintenez Ctrl pour sélectionner plusieurs):");
        prerequisLabel.getStyleClass().add("secouriste-label");
        ListView<Competence> prerequisListView = new ListView<>();
        prerequisListView.setItems(model.getAllCompetences());
        prerequisListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        prerequisListView.setPrefHeight(200);
        
        Button saveButton = new Button("Ajouter");
        saveButton.getStyleClass().addAll("dashboard-button", "active-button");
        saveButton.setOnAction(e -> {
            try {
                String intitule = intituleField.getText().trim();
                if (intitule.isEmpty()) {
                    throw new IllegalArgumentException("L'intitulé de la compétence est requis.");
                }
                
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
                
                // Check for cycles using DAG
                DAG dag = new DAG();
                for (Competence comp : model.getAllCompetences()) {
                    List<String> prereqIntitules = comp.getPrerequis().stream()
                        .map(Competence::getIntitule)
                        .collect(Collectors.toList());
                    dag.ajouterCompetence(comp.getIntitule(), prereqIntitules);
                }
                List<String> newPrereqIntitules = newCompetence.getPrerequis().stream()
                    .map(Competence::getIntitule)
                    .collect(Collectors.toList());
                if (!dag.ajouterCompetence(intitule, newPrereqIntitules)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Cycle détecté");
                    alert.setContentText("L'ajout de cette compétence créerait un cycle dans les prérequis.");
                    alert.showAndWait();
                    return;
                }
                
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
        
        Scene popupScene = new Scene(popupContent, 300, 600);
        popupScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }
    
    /**
     * Gère la suppression d'une compétence sélectionnée.
     *
     * @param event événement déclenché par l'action
     */
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
        
        if (!model.canDeleteCompetence(selectedCompetence)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Suppression impossible");
            alert.setHeaderText("Compétence utilisée comme prérequis");
            alert.setContentText("Cette compétence ne peut pas être supprimée car elle est utilisée comme prérequis par d'autres compétences.");
            alert.showAndWait();
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer la compétence");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette compétence ?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                try {
                    model.supprimerCompetence(selectedCompetence);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Compétence supprimée");
                    successAlert.setHeaderText("Suppression réussie");
                    successAlert.setContentText("La compétence a été supprimée de la liste.");
                    successAlert.showAndWait();
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur de suppression");
                    errorAlert.setHeaderText("Impossible de supprimer");
                    errorAlert.setContentText("Erreur lors de la suppression : " + ex.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
    
    /**
     * Met à jour le nom de l'utilisateur dans le modèle.
     *
     * @param nomUtilisateur le nouveau nom d'utilisateur
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    /**
     * Définit le callback à exécuter lors du retour vers le tableau de bord.
     *
     * @param callback action à exécuter au retour
     */
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    /**
     * Retourne le modèle associé à ce contrôleur.
     *
     * @return modèle AdminCompetencesModel
     */
    public AdminCompetencesModel getModel() {
        return model;
    }
}
