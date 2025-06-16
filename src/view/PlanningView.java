package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import controller.PlanningController;

public class PlanningView {
    
    private AnchorPane root;
    private Label nomUtilisateurLabel;
    private Button precedentMoisButton;
    private Button suivantMoisButton;
    private Button aujourdHuiButton;
    private Button homeButton;
    private Label moisAnneeLabel;
    private GridPane calendrierGrid;
    private PlanningController controller;
    
    public PlanningView(String nomUtilisateur) {
        createView();
        setupController(nomUtilisateur);
        loadStylesheet();
    }
    
    private void createView() {
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("disponibilites-root");
        
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("disponibilites-header");
        
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        Label titleLabel = new Label("Planning");
        titleLabel.getStyleClass().add("disponibilites-title");
        
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
        
        HBox legende = new HBox();
        legende.setAlignment(Pos.CENTER_LEFT);
        legende.setSpacing(30);
        legende.setPadding(new Insets(0, 0, 10, 0));
        
        HBox affectationInfo = new HBox();
        affectationInfo.setAlignment(Pos.CENTER_LEFT);
        affectationInfo.setSpacing(8);
        Label affectationCircle = new Label("●");
        affectationCircle.getStyleClass().add("legende-affectation");
        Label affectationText = new Label("Avec affectation");
        affectationText.getStyleClass().add("legende-text");
        affectationInfo.getChildren().addAll(affectationCircle, affectationText);
        
        HBox aujourdHuiInfo = new HBox();
        aujourdHuiInfo.setAlignment(Pos.CENTER_LEFT);
        aujourdHuiInfo.setSpacing(8);
        Label aujourdHuiCircle = new Label("●");
        aujourdHuiCircle.getStyleClass().add("legende-aujourdhui");
        Label aujourdHuiText = new Label("Aujourd'hui");
        aujourdHuiText.getStyleClass().add("legende-text");
        aujourdHuiInfo.getChildren().addAll(aujourdHuiCircle, aujourdHuiText);
        
        legende.getChildren().addAll(affectationInfo, aujourdHuiInfo);
        
        HBox navigationMois = new HBox();
        navigationMois.setAlignment(Pos.CENTER);
        navigationMois.setSpacing(20);
        navigationMois.setPadding(new Insets(10, 0, 20, 0));
        
        precedentMoisButton = new Button("❮");
        precedentMoisButton.getStyleClass().add("navigation-button");
        
        moisAnneeLabel = new Label("Janvier 2025");
        moisAnneeLabel.getStyleClass().add("mois-label");
        
        suivantMoisButton = new Button("❯");
        suivantMoisButton.getStyleClass().add("navigation-button");
        
        aujourdHuiButton = new Button("Aujourd'hui");
        aujourdHuiButton.getStyleClass().add("retour-button");
        
        navigationMois.getChildren().addAll(precedentMoisButton, moisAnneeLabel, suivantMoisButton, aujourdHuiButton);
        
        VBox calendrierContainer = new VBox();
        calendrierContainer.setAlignment(Pos.CENTER);
        calendrierContainer.getStyleClass().add("calendrier-container");
        
        Label calendrierTitre = new Label("CALENDRIER MENSUEL DES AFFECTATIONS");
        calendrierTitre.getStyleClass().add("calendrier-titre");
        
        calendrierGrid = new GridPane();
        calendrierGrid.getStyleClass().add("calendrier-grid");
        
        createCalendrierGrid();
        
        calendrierContainer.getChildren().addAll(calendrierTitre, calendrierGrid);
        
        mainContent.getChildren().addAll(legende, navigationMois, calendrierContainer);
        
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        
        AnchorPane.setTopAnchor(mainContent, 70.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        
        root.getChildren().addAll(header, mainContent);
    }
    
    private void createCalendrierGrid() {
        String[] joursSemaine = {"LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE"};
        
        for (int col = 0; col < joursSemaine.length; col++) {
            Label header = new Label(joursSemaine[col]);
            header.getStyleClass().add("calendrier-header");
            calendrierGrid.add(header, col, 0);
        }
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new PlanningController(nomUtilisateurLabel, homeButton,precedentMoisButton, suivantMoisButton, aujourdHuiButton,moisAnneeLabel, calendrierGrid, nomUtilisateur);
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