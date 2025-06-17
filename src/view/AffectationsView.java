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
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("affectations-root");
        
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("affectations-header");
        
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        Label titleLabel = new Label("Mes Affectations");
        titleLabel.getStyleClass().add("affectations-title");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        HBox userInfo = new HBox();
        userInfo.setAlignment(Pos.CENTER_RIGHT);
        userInfo.setSpacing(15);
        
        nomUtilisateurLabel = new Label("NOM PRÉNOM DE L'UTILISATEUR");
        nomUtilisateurLabel.getStyleClass().add("user-name");
        
        homeButton = new Button("");
        homeButton.getStyleClass().add("return-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, homeButton);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        VBox mainContent = new VBox();
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        Label tableauTitre = new Label("TABLEAU DE MES AFFECTATIONS");
        tableauTitre.getStyleClass().add("tableau-titre");
        
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("affectations-scroll");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        VBox tableauContainer = new VBox();
        tableauContainer.getStyleClass().add("tableau-container");
        tableauContainer.setSpacing(0);
        
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
        
        Label secouristesHeader = new Label("Secouristes");
        secouristesHeader.getStyleClass().add("tableau-header-cell");
        secouristesHeader.setPrefWidth(324);
        
        tableauHeader.getChildren().addAll(dateHeader, siteHeader, secouristesHeader);
        
        affectationsContainer = new VBox();
        affectationsContainer.setSpacing(0);
        
        tableauContainer.getChildren().addAll(tableauHeader, affectationsContainer);
        scrollPane.setContent(tableauContainer);
        
        mainContent.getChildren().addAll(tableauTitre, scrollPane);
        
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
        controller = new AffectationsController(nomUtilisateurLabel, null, 
                                             affectationsContainer, nomUtilisateur, 
                                             homeButton);
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