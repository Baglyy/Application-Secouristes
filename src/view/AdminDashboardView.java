package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import controller.AdminDashboardController;

public class AdminDashboardView {
    
    private AnchorPane root;
    private Button dispositifsButton;
    private Button affectationsSecouristesButton;
    private Button secouristesButton;
    private Button deconnexionButton;
    private Label nomUtilisateurLabel;
    private AdminDashboardController controller;
    
    public AdminDashboardView(String nomUtilisateur) {
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
        
        // Container du header avec le titre et les infos utilisateur
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        // Titre "Tableau de bord - Administrateur"
        Label titleLabel = new Label("Tableau de bord - Administrateur");
        titleLabel.getStyleClass().add("dashboard-title");
        
        // Spacer pour pousser les éléments utilisateur à droite
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Container pour les informations utilisateur (nom + icônes)
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
        
        // Bouton de déconnexion (remplace l'icône de profil)
        deconnexionButton = new Button("Déconnexion");
        deconnexionButton.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, deconnexionButton);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        // Ajout du contenu au header
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        // Container principal pour les boutons
        VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setSpacing(40);
        mainContent.setPadding(new Insets(60, 50, 50, 50));
        
        // Première ligne de boutons (Dispositifs de secours et Affectations secouristes)
        HBox firstRow = new HBox();
        firstRow.setAlignment(Pos.CENTER);
        firstRow.setSpacing(60);
        
        // Bouton Dispositifs de secours
        dispositifsButton = new Button();
        dispositifsButton.setPrefSize(280, 100);
        dispositifsButton.getStyleClass().addAll("dashboard-button", "active-button");
        
        // Container pour le contenu du bouton Dispositifs
        VBox dispositifsContent = new VBox();
        dispositifsContent.setAlignment(Pos.CENTER);
        dispositifsContent.setSpacing(8);
        
        Label dispositifsIcon = new Label("🚑");
        dispositifsIcon.getStyleClass().add("button-icon");
        
        Label dispositifsText = new Label("Dispositifs de secours");
        dispositifsText.getStyleClass().add("button-text");
        
        dispositifsContent.getChildren().addAll(dispositifsIcon, dispositifsText);
        dispositifsButton.setGraphic(dispositifsContent);
        
        // Bouton Affectations secouristes
        affectationsSecouristesButton = new Button();
        affectationsSecouristesButton.setPrefSize(280, 100);
        affectationsSecouristesButton.getStyleClass().add("dashboard-button");
        
        // Container pour le contenu du bouton Affectations secouristes
        VBox affectationsSecouristesContent = new VBox();
        affectationsSecouristesContent.setAlignment(Pos.CENTER);
        affectationsSecouristesContent.setSpacing(8);
        
        Label affectationsSecouristesIcon = new Label("👁");
        affectationsSecouristesIcon.getStyleClass().add("button-icon");
        
        Label affectationsSecouristesText = new Label("Affectations secouristes");
        affectationsSecouristesText.getStyleClass().add("button-text");
        
        affectationsSecouristesContent.getChildren().addAll(affectationsSecouristesIcon, affectationsSecouristesText);
        affectationsSecouristesButton.setGraphic(affectationsSecouristesContent);
        
        firstRow.getChildren().addAll(dispositifsButton, affectationsSecouristesButton);
        
        // Deuxième ligne (Secouristes - centré)
        HBox secondRow = new HBox();
        secondRow.setAlignment(Pos.CENTER);
        
        // Bouton Secouristes
        secouristesButton = new Button();
        secouristesButton.setPrefSize(280, 100);
        secouristesButton.getStyleClass().add("dashboard-button");
        
        // Container pour le contenu du bouton Secouristes
        VBox secouristesContent = new VBox();
        secouristesContent.setAlignment(Pos.CENTER);
        secouristesContent.setSpacing(8);
        
        Label secouristesIcon = new Label("👨‍⚕️");
        secouristesIcon.getStyleClass().add("button-icon");
        
        Label secouristesText = new Label("Secouristes");
        secouristesText.getStyleClass().add("button-text");
        
        secouristesContent.getChildren().addAll(secouristesIcon, secouristesText);
        secouristesButton.setGraphic(secouristesContent);
        
        secondRow.getChildren().add(secouristesButton);
        
        // Ajout des lignes au contenu principal
        mainContent.getChildren().addAll(firstRow, secondRow);
        
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
        controller = new AdminDashboardController(dispositifsButton, affectationsSecouristesButton, 
                                                 secouristesButton, deconnexionButton, nomUtilisateurLabel, nomUtilisateur);
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
    
    public AdminDashboardController getController() {
        return controller;
    }
}