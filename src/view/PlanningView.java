package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import controller.PlanningController;

public class PlanningView {
    
    private AnchorPane root;
    private Label nomUtilisateurLabel;
    private Button retourButton;
    private Button homeButton;
    private Button precedentMoisButton;
    private Button suivantMoisButton;
    private Button aujourdHuiButton;
    private Label moisAnneeLabel;
    private GridPane calendrierGrid;
    private PlanningController controller;
    
    public PlanningView(String nomUtilisateur) {
        createView();
        setupController(nomUtilisateur);
        loadStylesheet();
    }
    
    private void createView() {
        // Conteneur principal
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("planning-root");
        
        // Header avec image de fond de montagnes
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("planning-header");
        
        // Container du header
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        // Titre "Planning"
        Label titleLabel = new Label("Planning");
        titleLabel.getStyleClass().add("disponibilites-title");
        
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
        notificationIcon.getStyleClass().add("notification-icon");
        
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
        
        // Titre du calendrier
        Label calendrierTitre = new Label("CALENDRIER DU PLANNING");
        calendrierTitre.getStyleClass().add("tableau-titre");
        
        // Container pour les contrôles de navigation du calendrier
        HBox navigationCalendrier = new HBox();
        navigationCalendrier.setAlignment(Pos.CENTER);
        navigationCalendrier.setSpacing(20);
        navigationCalendrier.setPadding(new Insets(10, 0, 10, 0));
        
        // Bouton mois précédent
        precedentMoisButton = new Button("❮");
        precedentMoisButton.getStyleClass().add("navigation-button");
        
        // Label pour afficher le mois et l'année
        moisAnneeLabel = new Label("Février 2030");
        moisAnneeLabel.getStyleClass().add("mois-annee-label");
        moisAnneeLabel.setPrefWidth(200);
        moisAnneeLabel.setAlignment(Pos.CENTER);
        
        // Bouton mois suivant
        suivantMoisButton = new Button("❯");
        suivantMoisButton.getStyleClass().add("navigation-button");
        
        // Bouton "Aujourd'hui"
        aujourdHuiButton = new Button("Aujourd'hui");
        aujourdHuiButton.getStyleClass().add("retour-button");
        
        navigationCalendrier.getChildren().addAll(precedentMoisButton, moisAnneeLabel, suivantMoisButton, aujourdHuiButton);
        
        // Container pour le calendrier avec scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("calendrier-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        // Container principal du calendrier
        VBox calendrierContainer = new VBox();
        calendrierContainer.getStyleClass().add("planning-container");
        calendrierContainer.setSpacing(0);
        
        // Header du calendrier (jours de la semaine)
        HBox joursHeader = new HBox();
        joursHeader.getStyleClass().add("jours-header");
        joursHeader.setAlignment(Pos.CENTER);
        joursHeader.setPrefHeight(40);
        
        String[] nomsJours = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (String jour : nomsJours) {
            Label jourLabel = new Label(jour);
            jourLabel.getStyleClass().add("jour-header-cell");
            jourLabel.setPrefWidth(120);
            jourLabel.setAlignment(Pos.CENTER);
            joursHeader.getChildren().add(jourLabel);
        }
        
        // Grid pour les jours du mois
        calendrierGrid = new GridPane();
        calendrierGrid.getStyleClass().add("planning-grid");
        calendrierGrid.setHgap(1);
        calendrierGrid.setVgap(1);
        calendrierGrid.setAlignment(Pos.CENTER);
        
        // Configurer les contraintes de colonne pour le grid
        for (int i = 0; i < 7; i++) {
            calendrierGrid.getColumnConstraints().add(new javafx.scene.layout.ColumnConstraints(120));
        }
        
        calendrierContainer.getChildren().addAll(joursHeader, calendrierGrid);
        scrollPane.setContent(calendrierContainer);
        
        // Légende
        HBox legendeContainer = new HBox();
        legendeContainer.setAlignment(Pos.CENTER);
        legendeContainer.setSpacing(30);
        legendeContainer.setPadding(new Insets(15, 0, 0, 0));
        
        HBox legendeAffectation = new HBox();
        legendeAffectation.setAlignment(Pos.CENTER_LEFT);
        legendeAffectation.setSpacing(8);
        
        Label legendeColorAffectation = new Label();
        legendeColorAffectation.getStyleClass().add("legende-color-affectation");
        legendeColorAffectation.setPrefSize(15, 15);
        
        Label legendeTextAffectation = new Label("Jour avec affectation");
        legendeTextAffectation.getStyleClass().add("legende-text");
        
        legendeAffectation.getChildren().addAll(legendeColorAffectation, legendeTextAffectation);
        
        HBox legendeAujourdhui = new HBox();
        legendeAujourdhui.setAlignment(Pos.CENTER_LEFT);
        legendeAujourdhui.setSpacing(8);
        
        Label legendeColorAujourdhui = new Label();
        legendeColorAujourdhui.getStyleClass().add("legende-color-aujourdhui");
        legendeColorAujourdhui.setPrefSize(15, 15);
        
        Label legendeTextAujourdhui = new Label("Aujourd'hui");
        legendeTextAujourdhui.getStyleClass().add("legende-text");
        
        legendeAujourdhui.getChildren().addAll(legendeColorAujourdhui, legendeTextAujourdhui);
        
        legendeContainer.getChildren().addAll(legendeAffectation, legendeAujourdhui);
        
        mainContent.getChildren().addAll(calendrierTitre, navigationCalendrier, scrollPane, legendeContainer);
        
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
        controller = new PlanningController(nomUtilisateurLabel, null, homeButton,
                                          precedentMoisButton, suivantMoisButton, aujourdHuiButton,
                                          moisAnneeLabel, calendrierGrid, nomUtilisateur);
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
    
    public PlanningController getController() {
        return controller;
    }
}