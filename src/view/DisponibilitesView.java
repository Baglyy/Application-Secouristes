package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import controller.DisponibilitesController;
import model.dao.SecouristeDAO;
import model.data.Secouriste;

public class DisponibilitesView {
    
    private AnchorPane root;
    private Label nomUtilisateurLabel;
    private Label moisLabel;
    private Button precedentButton;
    private Button suivantButton;
    private Button retourButton;
    private Button homeButton;
    private GridPane calendrierGrid;
    private DisponibilitesController controller;
    
    // Constructeur principal
    public DisponibilitesView(String nomUtilisateur) {
        long idSecouriste = getIdSecouristeFromDatabase(nomUtilisateur);
        createView();
        setupController(nomUtilisateur, idSecouriste);
        loadStylesheet();
    }
    
    // Constructeur avec ID explicite
    public DisponibilitesView(String nomUtilisateur, long idSecouriste) {
        createView();
        setupController(nomUtilisateur, idSecouriste);
        loadStylesheet();
    }
    
    private long getIdSecouristeFromDatabase(String nomUtilisateur) {
        try {
            SecouristeDAO dao = new SecouristeDAO();
            
            System.out.println("Recherche ID secouriste pour: '" + nomUtilisateur + "'");
            
            // Utiliser la nouvelle méthode findByFullName
            Secouriste secouriste = dao.findByFullName(nomUtilisateur);
            
            if (secouriste != null) {
                System.out.println("Secouriste trouvé - ID: " + secouriste.getId() + 
                                ", Nom: " + secouriste.getNom() + 
                                ", Prénom: " + secouriste.getPrenom());
                return secouriste.getId();
            } else {
                System.err.println("Aucun secouriste trouvé avec le nom complet: '" + nomUtilisateur + "'");
                
                // Si ça ne marche pas, essayer avec l'ancienne méthode (juste le nom)
                String[] parts = nomUtilisateur.trim().split("\\s+");
                if (parts.length >= 2) {
                    String nomSeul = parts[parts.length - 1];
                    System.out.println("Tentative avec le nom seul: '" + nomSeul + "'");
                    secouriste = dao.findByNom(nomSeul);
                    if (secouriste != null) {
                        System.out.println("Secouriste trouvé avec nom seul - ID: " + secouriste.getId());
                        return secouriste.getId();
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'ID du secouriste pour '" + nomUtilisateur + "' : " + e.getMessage());
            e.printStackTrace();
        }
        
        System.err.println("ATTENTION : ID secouriste non trouvé pour '" + nomUtilisateur + "', utilisation de -1");
        return -1;
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
        
        Label titleLabel = new Label("Disponibilité");
        titleLabel.getStyleClass().add("disponibilites-title");
        
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
        
        homeButton = new Button("🏠");
        homeButton.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeButton);
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
        
        HBox disponibleInfo = new HBox();
        disponibleInfo.setAlignment(Pos.CENTER_LEFT);
        disponibleInfo.setSpacing(8);
        Label disponibleCircle = new Label("●");
        disponibleCircle.getStyleClass().add("legende-disponible");
        Label disponibleText = new Label("Disponible");
        disponibleText.getStyleClass().add("legende-text");
        disponibleInfo.getChildren().addAll(disponibleCircle, disponibleText);
        
        HBox indisponibleInfo = new HBox();
        indisponibleInfo.setAlignment(Pos.CENTER_LEFT);
        indisponibleInfo.setSpacing(8);
        Label indisponibleCircle = new Label("●");
        indisponibleCircle.getStyleClass().add("legende-indisponible");
        Label indisponibleText = new Label("Indisponible");
        indisponibleText.getStyleClass().add("legende-text");
        indisponibleInfo.getChildren().addAll(indisponibleCircle, indisponibleText);
        
        legende.getChildren().addAll(disponibleInfo, indisponibleInfo);
        
        HBox navigationMois = new HBox();
        navigationMois.setAlignment(Pos.CENTER);
        navigationMois.setSpacing(20);
        navigationMois.setPadding(new Insets(10, 0, 20, 0));
        
        precedentButton = new Button("❮");
        precedentButton.getStyleClass().add("navigation-button");
        
        moisLabel = new Label("Janvier 2025");
        moisLabel.getStyleClass().add("mois-label");
        
        suivantButton = new Button("❯");
        suivantButton.getStyleClass().add("navigation-button");
        
        navigationMois.getChildren().addAll(precedentButton, moisLabel, suivantButton);
        
        VBox calendrierContainer = new VBox();
        calendrierContainer.setAlignment(Pos.CENTER);
        calendrierContainer.getStyleClass().add("calendrier-container");
        
        Label calendrierTitre = new Label("CALENDRIER MENSUEL DES DISPONIBILITÉS");
        calendrierTitre.getStyleClass().add("calendrier-titre");
        
        calendrierGrid = new GridPane();
        calendrierGrid.getStyleClass().add("calendrier-grid");
        
        createCalendrierGrid();
        
        calendrierContainer.getChildren().addAll(calendrierTitre, calendrierGrid);
        
        HBox retourContainer = new HBox();
        retourContainer.setAlignment(Pos.CENTER_LEFT);
        retourContainer.setPadding(new Insets(20, 0, 0, 0));
        
        retourButton = new Button("← Retour au tableau de bord");
        retourButton.getStyleClass().add("retour-button");
        
        retourContainer.getChildren().add(retourButton);
        
        mainContent.getChildren().addAll(legende, navigationMois, calendrierContainer, retourContainer);
        
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
    
    private void setupController(String nomUtilisateur, long idSecouriste) {
        controller = new DisponibilitesController(
            nomUtilisateurLabel, moisLabel, precedentButton, suivantButton, 
            retourButton, calendrierGrid, nomUtilisateur, homeButton, idSecouriste
        );
    }
    
    public void setIdSecouriste(long idSecouriste) {
        if (controller != null) {
            controller.setIdSecouriste(idSecouriste);
        }
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
    
    public DisponibilitesController getController() {
        return controller;
    }
}