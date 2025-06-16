package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import model.AdminAffectationsModel;
import model.data.DPS;
import model.data.Secouriste;
import view.AdminDashboardView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminAffectationsController {
    
    private Button greedyButton;
    private Button exhaustiveButton;
    private TableView<AdminAffectationsModel.Affectation> tableView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminAffectationsModel model;
    private Runnable onRetourCallback;
    private TableColumn<AdminAffectationsModel.Affectation, String> colDate;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes;
    
    public AdminAffectationsController(
            Button greedyButton,
            Button exhaustiveButton,
            TableView<AdminAffectationsModel.Affectation> tableView,
            TableColumn<AdminAffectationsModel.Affectation, String> colDate,
            TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques,
            TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.greedyButton = greedyButton;
        this.exhaustiveButton = exhaustiveButton;
        this.tableView = tableView;
        this.colDate = colDate;
        this.colSitesOlympiques = colSitesOlympiques;
        this.colSecouristes = colSecouristes;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminAffectationsModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
    }
    
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        tableView.setItems(model.getAffectations());
    }
    
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
        greedyButton.setOnAction(e -> handleGenerate(true));
        exhaustiveButton.setOnAction(e -> handleGenerate(false));
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
    
    private void handleGenerate(boolean useGreedy) {
        generateAffectations(useGreedy);
    }
    
    public void generateAffectations(boolean useGreedy) {
        ObservableList<DPS> allDPS = model.getAllDPS();
        if (allDPS.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun DPS");
            alert.setHeaderText("Impossible de générer les affectations");
            alert.setContentText("Aucun dispositif (DPS) n'est disponible dans la base de données.");
            alert.showAndWait();
            return;
        }
        
        // Get all secouristes
        ObservableList<Secouriste> allSecouristes = FXCollections.observableArrayList(model.getSecouristeDAO().findAll());
        if (allSecouristes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun secouriste");
            alert.setHeaderText("Impossible de générer les affectations");
            alert.setContentText("Aucun secouriste n'est disponible dans la base de données.");
            alert.showAndWait();
            return;
        }
        
        // Call the appropriate algorithm
        Map<DPS, List<Secouriste>> assignments;
        if (useGreedy) {
            assignments = model.getGraphe().affectationGloutonne(allSecouristes, allDPS);
        } else {
            assignments = model.getGraphe().affectationExhaustive(allSecouristes, allDPS);
        }
        
        if (assignments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Échec des affectations");
            alert.setHeaderText("Aucune affectation générée");
            alert.setContentText("L'algorithme n'a pas pu générer d'affectations valides.");
            alert.showAndWait();
            return;
        }
        
        // Process each DPS and save affectations
        int totalAffectations = 0;
        for (Map.Entry<DPS, List<Secouriste>> entry : assignments.entrySet()) {
            DPS dps = entry.getKey();
            List<Secouriste> secouristes = entry.getValue();
            
            if (secouristes != null && !secouristes.isEmpty()) {
                // Convert Journee to LocalDate
                LocalDate date = LocalDate.of(
                    dps.getJournee().getAnnee(),
                    dps.getJournee().getMois(),
                    dps.getJournee().getJour()
                );
                
                // Filter secouristes by availability
                List<Secouriste> availableSecouristes = secouristes.stream()
                    .filter(s -> model.isSecouristeAvailable(s, date))
                    .collect(Collectors.toList());
                
                if (!availableSecouristes.isEmpty()) {
                    // Save to database and update model
                    model.createAffectation(dps, date, FXCollections.observableArrayList(availableSecouristes));
                    totalAffectations += availableSecouristes.size();
                }
            }
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Affectations générées");
        alert.setContentText(totalAffectations + " affectations ont été générées et enregistrées avec succès.");
        alert.showAndWait();
    }
    
    public ObservableList<DPS> getAllDPS() {
        return model.getAllDPS();
    }
    
    public ObservableList<Secouriste> searchCompetentSecouristes(DPS dps, LocalDate date) {
        return model.searchCompetentSecouristes(dps, date);
    }
    
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