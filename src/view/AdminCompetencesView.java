package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import controller.AdminCompetencesController;
import model.data.Competence;

public class AdminCompetencesView {
    
    private AnchorPane root;
    private Button modifierButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<Competence> listView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminCompetencesController controller;
    
    public AdminCompetencesView(String nomUtilisateur) {
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
        
        // Titre "Gestion des compétences"
        Label titleLabel = new Label("Gestion des compétences");
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
        
        // Icône maison
        homeIcon = new Label("");
        homeIcon.getStyleClass().add("return-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, homeIcon);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        // Ajout du contenu au header
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        // Container principal pour le contenu
        VBox mainContent = new VBox();
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        // Container pour les boutons d'action
        HBox buttonsContainer = new HBox();
        buttonsContainer.setAlignment(Pos.CENTER_LEFT);
        buttonsContainer.setSpacing(20);
        
        // Bouton Modifier compétence
        modifierButton = new Button("Modifier compétence");
        modifierButton.getStyleClass().addAll("dashboard-button", "active-button");
        modifierButton.setPrefSize(200, 40);
        modifierButton.setDisable(true); // Désactivé par défaut
        
        // Bouton Ajouter une compétence
        ajouterButton = new Button("Ajouter une compétence");
        ajouterButton.getStyleClass().addAll("dashboard-button", "active-button");
        ajouterButton.setPrefSize(200, 40);
        
        // Bouton Supprimer une compétence
        supprimerButton = new Button("Supprimer une compétence");
        supprimerButton.getStyleClass().addAll("dashboard-button", "active-button");
        supprimerButton.setPrefSize(200, 40);
        supprimerButton.setDisable(true); // Désactivé par défaut
        
        buttonsContainer.getChildren().addAll(modifierButton, ajouterButton, supprimerButton);
        
        // ListView pour afficher les compétences
        listView = new ListView<>();
        listView.getStyleClass().add("secouristes-table");
        listView.setPrefHeight(350);
        
        // Configuration de l'affichage personnalisé
        listView.setCellFactory(param -> new CompetenceListCell());
        
        // Ajout des éléments au contenu principal
        mainContent.getChildren().addAll(buttonsContainer, listView);
        
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
    
    private void loadStylesheet() {
        try {
            String cssPath = getClass().getResource("../style.css").toExternalForm();
            root.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Impossible de charger le fichier CSS style.css");
            e.printStackTrace();
        }
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new AdminCompetencesController(
                modifierButton,
                ajouterButton,
                supprimerButton,
                listView,
                nomUtilisateurLabel,
                homeIcon,
                nomUtilisateur);
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public AdminCompetencesController getController() {
        return controller;
    }
    
    // Classe interne pour personnaliser l'affichage des cellules
    private static class CompetenceListCell extends javafx.scene.control.ListCell<Competence> {
        @Override
        protected void updateItem(Competence competence, boolean empty) {
            super.updateItem(competence, empty);
            if (empty || competence == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Créer un VBox pour l'affichage structuré
                VBox cellContent = new VBox(5);
                cellContent.setPadding(new Insets(5));
                
                // Intitulé de la compétence
                Label intituleLabel = new Label("Compétence: " + competence.getIntitule());
                intituleLabel.getStyleClass().add("secouriste-label");
                
                // Prérequis
                Label prerequisLabel = new Label("Prérequis: ");
                prerequisLabel.getStyleClass().add("secouriste-label");
                String prerequisText = competence.getPrerequis() != null && !competence.getPrerequis().isEmpty()
                    ? String.join(", ", competence.getPrerequis().stream()
                        .map(Competence::getIntitule)
                        .toList())
                    : "Aucun prérequis";
                Label prerequisValue = new Label(prerequisText);
                
                cellContent.getChildren().addAll(intituleLabel, prerequisLabel, prerequisValue);
                
                setGraphic(cellContent);
                setText(null);
            }
        }
    }
}