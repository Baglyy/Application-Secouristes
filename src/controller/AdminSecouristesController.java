package controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import model.AdminSecouristesModel;
import view.AdminDashboardView;

public class AdminSecouristesController {
    
    private Button editerButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private TableView<AdminSecouristesModel.Secouriste> tableView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminSecouristesModel model;
    private Runnable onRetourCallback;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colA;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colB;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colC;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colD;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colE;
    
    public AdminSecouristesController(
            Button editerButton, 
            Button ajouterButton,
            Button supprimerButton, 
            TableView<AdminSecouristesModel.Secouriste> tableView,
            TableColumn<AdminSecouristesModel.Secouriste, String> colA,
            TableColumn<AdminSecouristesModel.Secouriste, String> colB,
            TableColumn<AdminSecouristesModel.Secouriste, String> colC,
            TableColumn<AdminSecouristesModel.Secouriste, String> colD,
            TableColumn<AdminSecouristesModel.Secouriste, String> colE,
            Label nomUtilisateurLabel, 
            Label homeIcon, 
            String nomUtilisateur) {
        this.editerButton = editerButton;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.tableView = tableView;
        this.colA = colA;
        this.colB = colB;
        this.colC = colC;
        this.colD = colD;
        this.colE = colE;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminSecouristesModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
        setupTableEditing();
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        
        // Liaison des données avec la table
        tableView.setItems(model.getSecouristes());
    }
    
    private void setupListeners() {
        // Listeners pour les boutons
        homeIcon.setOnMouseClicked(event -> handleRetour());
        editerButton.setOnAction(this::handleEditer);
        ajouterButton.setOnAction(this::handleAjouter);
        supprimerButton.setOnAction(this::handleSupprimer);
    }
    
    private void setupTableEditing() {
        // Configure edit commit handlers for each column
        colA.setOnEditCommit(event -> {
            AdminSecouristesModel.Secouriste secouriste = event.getRowValue();
            secouriste.setNom1(event.getNewValue());
        });
        colB.setOnEditCommit(event -> {
            AdminSecouristesModel.Secouriste secouriste = event.getRowValue();
            secouriste.setNom2(event.getNewValue());
        });
        colC.setOnEditCommit(event -> {
            AdminSecouristesModel.Secouriste secouriste = event.getRowValue();
            secouriste.setNom3(event.getNewValue());
        });
        colD.setOnEditCommit(event -> {
            AdminSecouristesModel.Secouriste secouriste = event.getRowValue();
            secouriste.setNom4(event.getNewValue());
        });
        colE.setOnEditCommit(event -> {
            AdminSecouristesModel.Secouriste secouriste = event.getRowValue();
            secouriste.setNom5(event.getNewValue());
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
    
    private void handleEditer(ActionEvent event) {
        System.out.println("Édition des spécificités des secouristes activée");
        
        // Activer le mode édition de la table
        tableView.setEditable(true);
        
        // Afficher un message d'information
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mode édition");
        alert.setHeaderText("Mode édition activé");
        alert.setContentText("Vous pouvez maintenant cliquer sur les cellules pour les modifier.");
        alert.showAndWait();
    }
    
    private void handleAjouter(ActionEvent event) {
        System.out.println("Ajout d'un nouveau secouriste");
        
        // Créer un nouveau secouriste avec des valeurs par défaut
        AdminSecouristesModel.Secouriste nouveauSecouriste = 
            new AdminSecouristesModel.Secouriste("Nouveau", "Nouveau", "Nouveau", "Nouveau", "Nouveau");
        
        // Ajouter le secouriste au modèle
        model.ajouterSecouriste(nouveauSecouriste);
        
        // Sélectionner la nouvelle ligne
        tableView.getSelectionModel().selectLast();
        tableView.scrollTo(nouveauSecouriste);
        
        // Afficher un message de confirmation
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Secouriste ajouté");
        alert.setHeaderText("Nouveau secouriste ajouté");
        alert.setContentText("Un nouveau secouriste a été ajouté à la liste. Vous pouvez maintenant modifier ses informations.");
        alert.showAndWait();
    }
    
    private void handleSupprimer(ActionEvent event) {
        System.out.println("Suppression d'un secouriste");
        
        // Vérifier qu'un secouriste est sélectionné
        AdminSecouristesModel.Secouriste secouristeSelectionne = tableView.getSelectionModel().getSelectedItem();
        
        if (secouristeSelectionne == null) {
            // Aucun secouriste sélectionné
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun secouriste sélectionné");
            alert.setContentText("Veuillez sélectionner un secouriste à supprimer.");
            alert.showAndWait();
            return;
        }
        
        // Demander confirmation
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer le secouriste");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce secouriste ?");
        
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                // Supprimer le secouriste du modèle
                model.supprimerSecouriste(secouristeSelectionne);
                
                // Afficher un message de confirmation
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Secouriste supprimé");
                successAlert.setHeaderText("Suppression réussie");
                successAlert.setContentText("Le secouriste a été supprimé de la liste.");
                successAlert.showAndWait();
            }
        });
    }
    
    // Méthodes publiques pour interaction externe
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