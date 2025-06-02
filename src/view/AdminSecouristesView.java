package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import controller.AdminSecouristesController;
import model.AdminSecouristesModel;
import javafx.beans.property.SimpleStringProperty;

public class AdminSecouristesView {
    
    private AnchorPane root;
    private Button editerButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private TableView<AdminSecouristesModel.Secouriste> tableView;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colA;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colB;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colC;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colD;
    private TableColumn<AdminSecouristesModel.Secouriste, String> colE;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminSecouristesController controller;
    
    public AdminSecouristesView(String nomUtilisateur) {
        createView();
        setupController(nomUtilisateur);
        loadStylesheet();
    }
    
    private void createView() {
        // Conteneur principal
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("dashboard-root");
        
        // Header avec image de fond de montagnes
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("dashboard-header");
        
        // Container du header
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        // Titre "Gestion des secouristes"
        Label titleLabel = new Label("Gestion des secouristes");
        titleLabel.getStyleClass().add("dashboard-title");
        
        // Spacer pour pousser les éléments utilisateur à droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Container pour les informations utilisateur
        HBox userInfo = new HBox();
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        userInfo.setSpacing(15);
        
        // Nom de l'utilisateur
        nomUtilisateurLabel = new Label("NOM PRÉNOM DE L'UTILISATEUR");
        nomUtilisateurLabel.getStyleClass().add("user-name");
        
        // Icône de notification
        Label notificationIcon = new Label("🔔");
        notificationIcon.getStyleClass().add("profile-icon");
        
        // Badge rouge sur la notification
        Label notificationBadge = new Label("1");
        notificationBadge.getStyleClass().add("notification-badge");
        
        // Container pour notification avec badge
        AnchorPane notificationContainer = new AnchorPane();
        notificationContainer.getChildren().addAll(notificationIcon, notificationBadge);
        AnchorPane.setTopAnchor(notificationBadge, -5.0);
        AnchorPane.setRightAnchor(notificationBadge, -5.0);
        
        // Icône maison
        homeIcon = new Label("🏠");
        homeIcon.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeIcon);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        // Ajout du contenu au header
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        // Container principal pour le contenu
        VBox mainContent = new VBox();
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        // Container pour les boutons d'action
        HBox buttonsContainer = new HBox();
        buttonsContainer.setAlignment(Pos.CENTER_LEFT);
        buttonsContainer.setSpacing(20);
        
        // Bouton Éditer les spécificités des secouristes
        editerButton = new Button("Éditer les spécificités des secouristes");
        editerButton.getStyleClass().addAll("action-button", "edit-button");
        editerButton.setPrefSize(300, 40);
        
        // Bouton Ajouter des secouristes
        ajouterButton = new Button("Ajouter des secouristes");
        ajouterButton.getStyleClass().addAll("dashboard-button", "active-button");
        ajouterButton.setPrefSize(200, 40);
        
        // Bouton Supprimer des secouristes
        supprimerButton = new Button("Supprimer des secouristes");
        supprimerButton.getStyleClass().addAll("dashboard-button", "active-button");
        supprimerButton.setPrefSize(200, 40);
        
        buttonsContainer.getChildren().addAll(editerButton, ajouterButton, supprimerButton);
        
        // TableView pour afficher les secouristes
        tableView = new TableView<>();
        tableView.getStyleClass().add("secouristes-table");
        tableView.setEditable(true);
        
        // Création des colonnes (A, B, C, D, E)
        colA = new TableColumn<>("A");
        colB = new TableColumn<>("B");
        colC = new TableColumn<>("C");
        colD = new TableColumn<>("D");
        colE = new TableColumn<>("E");
        
        // Configuration des colonnes avec lambda-based CellValueFactory
        colA.setCellValueFactory(cellData -> cellData.getValue().nom1Property());
        colB.setCellValueFactory(cellData -> cellData.getValue().nom2Property());
        colC.setCellValueFactory(cellData -> cellData.getValue().nom3Property());
        colD.setCellValueFactory(cellData -> cellData.getValue().nom4Property());
        colE.setCellValueFactory(cellData -> cellData.getValue().nom5Property());
        
        // Rendre les colonnes éditables
        colA.setCellFactory(TextFieldTableCell.forTableColumn());
        colB.setCellFactory(TextFieldTableCell.forTableColumn());
        colC.setCellFactory(TextFieldTableCell.forTableColumn());
        colD.setCellFactory(TextFieldTableCell.forTableColumn());
        colE.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // Définir la largeur des colonnes
        double columnWidth = 150;
        colA.setPrefWidth(columnWidth);
        colB.setPrefWidth(columnWidth);
        colC.setPrefWidth(columnWidth);
        colD.setPrefWidth(columnWidth);
        colE.setPrefWidth(columnWidth);
        
        // Ajouter les colonnes à la table
        tableView.getColumns().addAll(colA, colB, colC, colD, colE);
        
        // Configuration de la taille de la table
        tableView.setPrefHeight(350);
        
        // Ajout des éléments au contenu principal
        mainContent.getChildren().addAll(buttonsContainer, tableView);
        
        // Positionnement des éléments
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        
        AnchorPane.setTopAnchor(mainContent, 80.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        
        root.getChildren().addAll(header, mainContent);
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new AdminSecouristesController(
                editerButton, 
                ajouterButton, 
                supprimerButton, 
                tableView, 
                colA, 
                colB, 
                colC, 
                colD, 
                colE, 
                nomUtilisateurLabel, 
                homeIcon, 
                nomUtilisateur);
    }
    
    private void loadStylesheet() {
        try {
            String cssPath = getClass().getResource("../style.css").toExternalForm();
            root.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Impossible de charger le fichier CSS style.css");
            e.printStackTrace();
        }
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public AdminSecouristesController getController() {
        return controller;
    }
}