package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import controller.AffectationsController;

public class AffectationsView {
    
    private AnchorPane root;
    private Label nomUtilisateurLabel;
    private Button retourButton;
    private Button homeButton;
    private VBox affectationsContainer;
    private AffectationsController controller;
    
    public AffectationsView(String nomUtilisateur) {
        createView();
        setupController(nomUtilisateur);
        loadStylesheet();
    }
    
    private void createView() {
        // Conteneur principal
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("affectations-root");
        
        // Header avec image de fond de montagnes
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("affectations-header");
        
        // Container du header
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        // Titre "Affectations"
        Label titleLabel = new Label("Affectations");
        titleLabel.getStyleClass().add("affectations-title");
        
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
        
        // Icône de notification avec badge
        Label notificationIcon = new Label("🔔");
        notificationIcon.getStyleClass().add("profile-icon");
        
        Label notificationBadge = new Label("1");
        notificationBadge.getStyleClass().add("notification-badge");
        
        AnchorPane notificationContainer = new AnchorPane();
        notificationContainer.getChildren().addAll(notificationIcon, notificationBadge);
        AnchorPane.setTopAnchor(notificationBadge, -5.0);
        AnchorPane.setRightAnchor(notificationBadge, -5.0);
        
        // Bouton Home
        homeButton = new Button("🏠");
        homeButton.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeButton);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        // Container principal pour le contenu
        VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        // Titre du tableau
        Label tableauTitre = new Label("TABLEAU DES AFFECTATIONS");
        tableauTitre.getStyleClass().add("tableau-titre");
        
        // Container pour le tableau avec scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("affectations-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Container pour les affectations
        VBox tableauContainer = new VBox();
        tableauContainer.getStyleClass().add("tableau-container");
        tableauContainer.setSpacing(0);
        
        // Header du tableau
        HBox tableauHeader = new HBox();
        tableauHeader.getStyleClass().add("tableau-header");
        tableauHeader.setAlignment(Pos.CENTER_LEFT);
        tableauHeader.setPrefHeight(50);
        
        Label dateHeader = new Label("Date");
        dateHeader.getStyleClass().add("tableau-header-cell");
        dateHeader.setPrefWidth(200);
        
        Label siteHeader = new Label("Site Olympique");
        siteHeader.getStyleClass().add("tableau-header-cell");
        siteHeader.setPrefWidth(300);
        
        Label typeHeader = new Label("Type de Dispositif");
        typeHeader.getStyleClass().add("tableau-header-cell");
        typeHeader.setPrefWidth(324);
        
        tableauHeader.getChildren().addAll(dateHeader, siteHeader, typeHeader);
        
        // Container pour les lignes d'affectations
        affectationsContainer = new VBox();
        affectationsContainer.setSpacing(0);
        
        tableauContainer.getChildren().addAll(tableauHeader, affectationsContainer);
        scrollPane.setContent(tableauContainer);
        
        // Boutons de navigation
        HBox navigationContainer = new HBox();
        navigationContainer.setAlignment(Pos.CENTER);
        navigationContainer.setSpacing(20);
        navigationContainer.setPadding(new Insets(20, 0, 0, 0));
        
        Button precedentButton = new Button("❮");
        precedentButton.getStyleClass().add("navigation-button");
        
        Button suivantButton = new Button("❯");
        suivantButton.getStyleClass().add("navigation-button");
        
        navigationContainer.getChildren().addAll(precedentButton, suivantButton);
        
        mainContent.getChildren().addAll(tableauTitre, scrollPane, navigationContainer);
        
        // Positionnement des éléments
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        
        AnchorPane.setTopAnchor(mainContent, 70.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        
        root.getChildren().addAll(header, mainContent);
    }
    
    private void setupController(String nomUtilisateur) {
        // Le bouton retour est maintenant null car il n'existe plus
        controller = new AffectationsController(nomUtilisateurLabel, null, 
                                             affectationsContainer, nomUtilisateur, homeButton);
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
    
    public AffectationsController getController() {
        return controller;
    }
}