package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import model.AdminAffectationsModel;
import model.data.DPS;
import model.data.Secouriste;
import view.AdminAffectationsView;
import view.AdminDashboardView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;

public class AdminAffectationsController {
    
    private Button createButton;
    private TableView<AdminAffectationsModel.Affectation> tableView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminAffectationsModel model;
    private Runnable onRetourCallback;
    private TableColumn<AdminAffectationsModel.Affectation, String> colDate;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes;
    
    public AdminAffectationsController(
            Button createButton,
            TableView<AdminAffectationsModel.Affectation> tableView,
            TableColumn<AdminAffectationsModel.Affectation, String> colDate,
            TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques,
            TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.createButton = createButton;
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
        createButton.setOnAction(this::handleCreate);
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
    
    private void handleCreate(ActionEvent event) {
        AdminAffectationsView view = new AdminAffectationsView(model.getNomUtilisateur());
        view.showCreateAffectationDialog(this);
    }
    
    public void createAffectation(DPS dps, LocalDate date, ObservableList<Secouriste> secouristes) {
        model.createAffectation(dps, date, secouristes);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Affectation créée");
        alert.setContentText("L'affectation a été créée avec succès.");
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