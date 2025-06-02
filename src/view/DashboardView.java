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
import controller.DashboardController;

public class DashboardView {
    
    private AnchorPane root;
    private Button affectationsButton;
    private Button planningButton;
    private Button disponibilitesButton;
    private Button deconnexionButton;
    private Label nomUtilisateurLabel;
    private DashboardController controller;
    

    
    public DashboardView(String nomUtilisateur) {
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
        
        // Titre "Tableau de bord"
        Label titleLabel = new Label("Tableau de bord");
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
        
        // Première ligne de boutons (Affectations et Planning)
        HBox firstRow = new HBox();
        firstRow.setAlignment(Pos.CENTER);
        firstRow.setSpacing(60);
        
        // Bouton Affectations
        affectationsButton = new Button();
        affectationsButton.setPrefSize(280, 100);
        affectationsButton.getStyleClass().addAll("dashboard-button", "active-button");
        
        // Container pour le contenu du bouton Affectations
        VBox affectationsContent = new VBox();
        affectationsContent.setAlignment(Pos.CENTER);
        affectationsContent.setSpacing(8);
        
        Label affectationsIcon = new Label("👁");
        affectationsIcon.getStyleClass().add("button-icon");
        
        Label affectationsText = new Label("Affectations");
        affectationsText.getStyleClass().add("button-text");
        
        affectationsContent.getChildren().addAll(affectationsIcon, affectationsText);
        affectationsButton.setGraphic(affectationsContent);
        
        // Bouton Planning
        planningButton = new Button();
        planningButton.setPrefSize(280, 100);
        planningButton.getStyleClass().add("dashboard-button");
        
        // Container pour le contenu du bouton Planning
        VBox planningContent = new VBox();
        planningContent.setAlignment(Pos.CENTER);
        planningContent.setSpacing(8);
        
        Label planningIcon = new Label("👁");
        planningIcon.getStyleClass().add("button-icon");
        
        Label planningText = new Label("Planning");
        planningText.getStyleClass().add("button-text");
        
        planningContent.getChildren().addAll(planningIcon, planningText);
        planningButton.setGraphic(planningContent);
        
        firstRow.getChildren().addAll(affectationsButton, planningButton);
        
        // Deuxième ligne (Disponibilités - centré)
        HBox secondRow = new HBox();
        secondRow.setAlignment(Pos.CENTER);
        
        // Bouton Disponibilités
        disponibilitesButton = new Button();
        disponibilitesButton.setPrefSize(280, 100);
        disponibilitesButton.getStyleClass().add("dashboard-button");
        
        // Container pour le contenu du bouton Disponibilités
        VBox disponibilitesContent = new VBox();
        disponibilitesContent.setAlignment(Pos.CENTER);
        disponibilitesContent.setSpacing(8);
        
        Label disponibilitesIcon = new Label("✏");
        disponibilitesIcon.getStyleClass().add("button-icon");
        
        Label disponibilitesText = new Label("Disponibilités");
        disponibilitesText.getStyleClass().add("button-text");
        
        disponibilitesContent.getChildren().addAll(disponibilitesIcon, disponibilitesText);
        disponibilitesButton.setGraphic(disponibilitesContent);
        
        secondRow.getChildren().add(disponibilitesButton);
        
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
    
    private void setupController() {
        controller = new DashboardController(affectationsButton, planningButton, 
                                           disponibilitesButton, deconnexionButton, nomUtilisateurLabel);
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new DashboardController(affectationsButton, planningButton, 
                                           disponibilitesButton, deconnexionButton, nomUtilisateurLabel, nomUtilisateur);
    }
    
    private void loadStylesheet() {
        try {
            String cssPath = getClass().getResource("../style.css").toExternalForm();
            root.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Impossible de charger le fichier CSS dashboard.css");
            e.printStackTrace();
        }
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public DashboardController getController() {
        return controller;
    }
}