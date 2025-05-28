package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import controller.DisponibilitesController;

public class DisponibilitesView {
    
    private AnchorPane root;
    private Label nomUtilisateurLabel;
    private Label semaineLabel;
    private Button precedentButton;
    private Button suivantButton;
    private Button retourButton;
    private Button homeButton;
    private GridPane calendrierGrid;
    private DisponibilitesController controller;
    
    public DisponibilitesView(String nomUtilisateur) {
        createView();
        setupController(nomUtilisateur);
        loadStylesheet();
    }
    
    private void createView() {
        // Conteneur principal
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("disponibilites-root");
        
        // Header avec image de fond de montagnes
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("disponibilites-header");
        
        // Container du header
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        // Titre "Disponibilité"
        Label titleLabel = new Label("Disponibilité");
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
        
        // Bouton Home (remplace l'icône de profil)
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
        
        // Légende
        HBox legende = new HBox();
        legende.setAlignment(Pos.CENTER_LEFT);
        legende.setSpacing(30);
        legende.setPadding(new Insets(0, 0, 10, 0));
        
        // Disponible
        HBox disponibleInfo = new HBox();
        disponibleInfo.setAlignment(Pos.CENTER_LEFT);
        disponibleInfo.setSpacing(8);
        
        Label disponibleCircle = new Label("●");
        disponibleCircle.getStyleClass().add("legende-disponible");
        
        Label disponibleText = new Label("Disponible");
        disponibleText.getStyleClass().add("legende-text");
        
        disponibleInfo.getChildren().addAll(disponibleCircle, disponibleText);
        
        // Indisponible
        HBox indisponibleInfo = new HBox();
        indisponibleInfo.setAlignment(Pos.CENTER_LEFT);
        indisponibleInfo.setSpacing(8);
        
        Label indisponibleCircle = new Label("●");
        indisponibleCircle.getStyleClass().add("legende-indisponible");
        
        Label indisponibleText = new Label("Indisponible");
        indisponibleText.getStyleClass().add("legende-text");
        
        indisponibleInfo.getChildren().addAll(indisponibleCircle, indisponibleText);
        
        legende.getChildren().addAll(disponibleInfo, indisponibleInfo);
        
        // Navigation semaine
        HBox navigationSemaine = new HBox();
        navigationSemaine.setAlignment(Pos.CENTER);
        navigationSemaine.setSpacing(20);
        navigationSemaine.setPadding(new Insets(10, 0, 20, 0));
        
        precedentButton = new Button("❮");
        precedentButton.getStyleClass().add("navigation-button");
        
        semaineLabel = new Label("Semaine 14");
        semaineLabel.getStyleClass().add("semaine-label");
        
        suivantButton = new Button("❯");
        suivantButton.getStyleClass().add("navigation-button");
        
        navigationSemaine.getChildren().addAll(precedentButton, semaineLabel, suivantButton);
        
        // Calendrier
        VBox calendrierContainer = new VBox();
        calendrierContainer.setAlignment(Pos.CENTER);
        calendrierContainer.getStyleClass().add("calendrier-container");
        
        // Titre du calendrier
        Label calendrierTitre = new Label("CALENDRIER HEBDOMADAIRE DES INTERVENTIONS");
        calendrierTitre.getStyleClass().add("calendrier-titre");
        
        // Grille du calendrier
        calendrierGrid = new GridPane();
        calendrierGrid.getStyleClass().add("calendrier-grid");
        
        createCalendrierGrid();
        
        calendrierContainer.getChildren().addAll(calendrierTitre, calendrierGrid);
        
        // Bouton retour
        HBox retourContainer = new HBox();
        retourContainer.setAlignment(Pos.CENTER_LEFT);
        retourContainer.setPadding(new Insets(20, 0, 0, 0));
        
        retourButton = new Button("← Retour au tableau de bord");
        retourButton.getStyleClass().add("retour-button");
        
        retourContainer.getChildren().add(retourButton);
        
        mainContent.getChildren().addAll(legende, navigationSemaine, calendrierContainer, retourContainer);
        
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
    
    private void createCalendrierGrid() {
        // Headers
        String[] jours = {"SUJET", "LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI"};
        String[] heures = {"9H - 10H", "10H - 12H", "12H - 14H", "14H - 16H"};
        
        // État initial des disponibilités (basé sur l'image)
        boolean[][] disponibilites = {
            {false, true, false, true, true},   // 9H - 10H
            {false, true, true, false, false},  // 10H - 12H
            {true, false, false, false, true},  // 12H - 14H
            {true, true, false, true, true}     // 14H - 16H
        };
        
        // Créer les headers des jours
        for (int col = 0; col < jours.length; col++) {
            Label header = new Label(jours[col]);
            header.getStyleClass().add("calendrier-header");
            calendrierGrid.add(header, col, 0);
        }
        
        // Créer les lignes avec heures et cellules
        for (int row = 0; row < heures.length; row++) {
            // Header de l'heure
            Label heureLabel = new Label(heures[row]);
            heureLabel.getStyleClass().add("calendrier-header");
            calendrierGrid.add(heureLabel, 0, row + 1);
            
            // Créer les cellules pour chaque jour
            for (int col = 1; col < jours.length; col++) {
                Button cellButton = new Button();
                cellButton.getStyleClass().add("calendrier-cell");
                
                // Appliquer le style basé sur la disponibilité initiale
                if (disponibilites[row][col - 1]) {
                    cellButton.getStyleClass().add("disponible-cell");
                } else {
                    cellButton.getStyleClass().add("indisponible-cell");
                }
                
                calendrierGrid.add(cellButton, col, row + 1);
            }
        }
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new DisponibilitesController(nomUtilisateurLabel, semaineLabel,
                                                precedentButton, suivantButton, 
                                                retourButton, calendrierGrid, nomUtilisateur, homeButton);
    }
    
    private void loadStylesheet() {
        try {
            String cssPath = getClass().getResource("../style.css").toExternalForm();
            root.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Impossible de charger le fichier CSS disponibilites.css");
            e.printStackTrace();
        }
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public DisponibilitesController getController() {
        return controller;
    }
}