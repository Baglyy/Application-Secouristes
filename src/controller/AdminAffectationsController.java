package controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import model.AdminAffectationsModel;
import view.AdminDashboardView;

public class AdminAffectationsController {
    
    private Button modifierButton;
    private TableView<AdminAffectationsModel.Affectation> tableView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminAffectationsModel model;
    private Runnable onRetourCallback;
    private TableColumn<AdminAffectationsModel.Affectation, String> colDate;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes;
    
    public AdminAffectationsController(
            Button modifierButton,
            TableView<AdminAffectationsModel.Affectation> tableView,
            TableColumn<AdminAffectationsModel.Affectation, String> colDate,
            TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques,
            TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.modifierButton = modifierButton;
        this.tableView = tableView;
        this.colDate = colDate;
        this.colSitesOlympiques = colSitesOlympiques;
        this.colSecouristes = colSecouristes;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminAffectationsModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
        setupTableEditing();
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        
        // Liaison des données avec la table
        tableView.setItems(model.getAffectations());
    }
    
    private void setupListeners() {
        // Listeners pour les boutons
        homeIcon.setOnMouseClicked(event -> handleRetour());
        modifierButton.setOnAction(this::handleModifier);
    }
    
    private void setupTableEditing() {
        // Configure edit commit handlers for each column
        colDate.setOnEditCommit(event -> {
            AdminAffectationsModel.Affectation affectation = event.getRowValue();
            affectation.setDate(event.getNewValue());
        });
        colSitesOlympiques.setOnEditCommit(event -> {
            AdminAffectationsModel.Affectation affectation = event.getRowValue();
            affectation.setSitesOlympiques(event.getNewValue());
        });
        colSecouristes.setOnEditCommit(event -> {
            AdminAffectationsModel.Affectation affectation = event.getRowValue();
            affectation.setSecouristes(event.getNewValue());
        });
    }
    
    private void handleRetour() {
        System.out.println("Retour vers le tableau de bord administrateur");
        
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            // Navigation par défaut vers le dashboard
            Stage currentStage = (Stage) homeIcon.getScene().getWindow();
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        }
    }
    
    private void handleModifier(ActionEvent event) {
        System.out.println("Modification des affectations activée");
        
        // Activer le mode édition de la table
        tableView.setEditable(true);
        
        // Afficher un message d'information
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mode édition");
        alert.setHeaderText("Mode édition activé");
        alert.setContentText("Vous pouvez maintenant cliquer sur les cellules pour modifier les affectations.");
        alert.showAndWait();
    }
    
    // Méthodes publiques pour interaction externe
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public AdminAffectationsModel getModel() {
        return model;
    }
}