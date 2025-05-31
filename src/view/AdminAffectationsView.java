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
import controller.AdminAffectationsController;
import model.AdminAffectationsModel;

public class AdminAffectationsView {
    
    private AnchorPane root;
    private Button modifierButton;
    private TableView<AdminAffectationsModel.Affectation> tableView;
    private TableColumn<AdminAffectationsModel.Affectation, String> colDate;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminAffectationsController controller;
    
    public AdminAffectationsView(String nomUtilisateur) {
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
        
        // Titre "Affectations"
        Label titleLabel = new Label("Affectations");
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
        notificationIcon.getStyleClass().add("notification-icon");
        
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
        homeIcon.getStyleClass().add("notification-icon");
        
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
        mainContent.setSpacing(30);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        // Container pour le bouton et la table
        HBox contentContainer = new HBox();
        contentContainer.setSpacing(40);
        contentContainer.setAlignment(Pos.TOP_LEFT);
        
        // Container pour le bouton (à gauche)
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.TOP_LEFT);
        
        // Bouton Modifier l'affectation
        modifierButton = new Button("⚙️\nModifier l'affectation");
        modifierButton.getStyleClass().addAll("dashboard-button", "active-button");
        modifierButton.setPrefSize(200, 60);
        modifierButton.setStyle("-fx-content-display: center; -fx-text-alignment: center;");
        
        buttonContainer.getChildren().add(modifierButton);
        
        // Container pour la table (à droite)
        VBox tableContainer = new VBox();
        tableContainer.setAlignment(Pos.TOP_LEFT);
        
        // TableView pour afficher les affectations
        tableView = new TableView<>();
        tableView.getStyleClass().add("secouristes-table");
        tableView.setEditable(true);
        
        // Création des colonnes
        colDate = new TableColumn<>("Date");
        colSitesOlympiques = new TableColumn<>("Sites Olympiques");
        colSecouristes = new TableColumn<>("Secouristes");
        
        // Configuration des colonnes avec lambda-based CellValueFactory
        colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        colSitesOlympiques.setCellValueFactory(cellData -> cellData.getValue().sitesOlympiquesProperty());
        colSecouristes.setCellValueFactory(cellData -> cellData.getValue().secouristesProperty());
        
        // Rendre les colonnes éditables
        colDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colSitesOlympiques.setCellFactory(TextFieldTableCell.forTableColumn());
        colSecouristes.setCellFactory(TextFieldTableCell.forTableColumn());
        
        // Définir la largeur des colonnes
        colDate.setPrefWidth(120);
        colSitesOlympiques.setPrefWidth(250);
        colSecouristes.setPrefWidth(200);
        
        // Ajouter les colonnes à la table
        tableView.getColumns().addAll(colDate, colSitesOlympiques, colSecouristes);
        
        // Configuration de la taille de la table
        tableView.setPrefHeight(350);
        tableView.setPrefWidth(570);
        
        // Style spécial pour les cellules multi-lignes
        tableView.setRowFactory(tv -> {
            javafx.scene.control.TableRow<AdminAffectationsModel.Affectation> row = new javafx.scene.control.TableRow<>();
            row.setPrefHeight(60); // Hauteur augmentée pour les cellules multi-lignes
            return row;
        });
        
        tableContainer.getChildren().add(tableView);
        
        // Ajout des containers au contenu principal
        contentContainer.getChildren().addAll(buttonContainer, tableContainer);
        mainContent.getChildren().add(contentContainer);
        
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
        controller = new AdminAffectationsController(
                modifierButton,
                tableView,
                colDate,
                colSitesOlympiques,
                colSecouristes,
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
    
    public AdminAffectationsController getController() {
        return controller;
    }
}