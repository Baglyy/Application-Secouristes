package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import controller.AdminAffectationsController;
import model.AdminAffectationsModel;

public class AdminAffectationsView {
    
    private AnchorPane root;
    private Button greedyButton;
    private Button exhaustiveButton;
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
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("dashboard-root");
        
        // Header
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("dashboard-header");
        
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        Label titleLabel = new Label("Affectations");
        titleLabel.getStyleClass().add("dashboard-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox userInfo = new HBox();
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        userInfo.setSpacing(15);
        
        nomUtilisateurLabel = new Label("NOM PRÉNOM DE L'UTILISATEUR");
        nomUtilisateurLabel.getStyleClass().add("user-name");
        
        Label notificationIcon = new Label("🔔");
        notificationIcon.getStyleClass().add("profile-icon");
        
        Label notificationBadge = new Label("1");
        notificationBadge.getStyleClass().add("notification-badge");
        
        AnchorPane notificationContainer = new AnchorPane();
        notificationContainer.getChildren().addAll(notificationIcon, notificationBadge);
        AnchorPane.setTopAnchor(notificationBadge, -5.0);
        AnchorPane.setRightAnchor(notificationBadge, -5.0);
        
        homeIcon = new Label("🏠");
        homeIcon.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeIcon);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        // Main content
        VBox mainContent = new VBox();
        mainContent.setSpacing(30);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        HBox contentContainer = new HBox();
        contentContainer.setSpacing(40);
        contentContainer.setAlignment(Pos.TOP_LEFT);
        
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.TOP_LEFT);
        buttonContainer.setSpacing(10);
        
        greedyButton = new Button("Générer via Algorithme Glouton");
        greedyButton.getStyleClass().addAll("dashboard-button", "active-button");
        greedyButton.setPrefSize(200, 60);
        
        exhaustiveButton = new Button("Générer via Algorithme Exhaustif");
        exhaustiveButton.getStyleClass().addAll("dashboard-button", "active-button");
        exhaustiveButton.setPrefSize(200, 60);
        
        buttonContainer.getChildren().addAll(greedyButton, exhaustiveButton);
        
        VBox tableContainer = new VBox();
        tableContainer.setAlignment(Pos.TOP_LEFT);
        
        tableView = new TableView<>();
        tableView.getStyleClass().add("secouristes-table");
        tableView.setEditable(false);
        
        colDate = new TableColumn<>("Date");
        colSitesOlympiques = new TableColumn<>("Sites Olympiques");
        colSecouristes = new TableColumn<>("Secouristes");
        
        colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        colSitesOlympiques.setCellValueFactory(cellData -> cellData.getValue().sitesOlympiquesProperty());
        colSecouristes.setCellValueFactory(cellData -> cellData.getValue().secouristesProperty());
        
        colDate.setPrefWidth(120);
        colSitesOlympiques.setPrefWidth(250);
        colSecouristes.setPrefWidth(200);
        
        tableView.getColumns().addAll(colDate, colSitesOlympiques, colSecouristes);
        
        tableView.setPrefHeight(350);
        tableView.setPrefWidth(570);
        
        tableView.setRowFactory(tv -> {
            TableRow<AdminAffectationsModel.Affectation> row = new TableRow<>();
            row.setPrefHeight(60);
            return row;
        });
        
        tableContainer.getChildren().add(tableView);
        
        contentContainer.getChildren().addAll(buttonContainer, tableContainer);
        mainContent.getChildren().add(contentContainer);
        
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        
        AnchorPane.setTopAnchor(mainContent, 80.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        
        root.getChildren().addAll(header, mainContent);
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
    
    public Button getGreedyButton() {
        return greedyButton;
    }
    
    public Button getExhaustiveButton() {
        return exhaustiveButton;
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new AdminAffectationsController(
                greedyButton,
                exhaustiveButton,
                tableView,
                colDate,
                colSitesOlympiques,
                colSecouristes,
                nomUtilisateurLabel,
                homeIcon,
                nomUtilisateur);
    }
}