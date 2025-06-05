package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.scene.layout.GridPane;
import controller.AdminSecouristesController;
import model.data.Secouriste;

public class AdminSecouristesView {
    
    private AnchorPane root;
    private Button modifierCompetencesButton;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<Secouriste> listView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminSecouristesController controller;
    
    public AdminSecouristesView(String nomUtilisateur) {
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
        
        // Titre "Gestion des secouristes"
        Label titleLabel = new Label("Gestion des secouristes");
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
        
        // Icône maison
        homeIcon = new Label("🏠");
        homeIcon.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeIcon);
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
        
        // Bouton Modifier compétences
        modifierCompetencesButton = new Button("Modifier compétences");
        modifierCompetencesButton.getStyleClass().addAll("action-button", "edit-button");
        modifierCompetencesButton.setPrefSize(300, 40);
        modifierCompetencesButton.setDisable(true); // Désactivé par défaut
        
        // Bouton Ajouter des secouristes
        ajouterButton = new Button("Ajouter des secouristes");
        ajouterButton.getStyleClass().addAll("dashboard-button", "active-button");
        ajouterButton.setPrefSize(200, 40);
        
        // Bouton Supprimer des secouristes
        supprimerButton = new Button("Supprimer des secouristes");
        supprimerButton.getStyleClass().addAll("dashboard-button", "active-button");
        supprimerButton.setPrefSize(200, 40);
        supprimerButton.setDisable(true); // Désactivé par défaut
        
        buttonsContainer.getChildren().addAll(modifierCompetencesButton, ajouterButton, supprimerButton);
        
        // ListView pour afficher les secouristes
        listView = new ListView<>();
        listView.getStyleClass().add("secouristes-table");
        listView.setPrefHeight(350);
        
        // Configuration de l'affichage personnalisé
        listView.setCellFactory(param -> new SecouristeListCell());
        
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
        controller = new AdminSecouristesController(
                modifierCompetencesButton,
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
    
    public AdminSecouristesController getController() {
        return controller;
    }
    
    // Classe interne pour personnaliser l'affichage des cellules
    private static class SecouristeListCell extends javafx.scene.control.ListCell<Secouriste> {
        @Override
        protected void updateItem(Secouriste secouriste, boolean empty) {
            super.updateItem(secouriste, empty);
            if (empty || secouriste == null) {
                setText(null);
                setGraphic(null);
            } else {
                // Create a GridPane for structured display
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(5);
                grid.setPadding(new Insets(5));
                
                // Add labels for each attribute
                int row = 0;
                
                // ID
                Label idLabel = new Label("ID:");
                idLabel.getStyleClass().add("secouriste-label");
                Label idValue = new Label(String.valueOf(secouriste.getId()));
                grid.add(idLabel, 0, row);
                grid.add(idValue, 1, row++);
                
                // Nom
                Label nomLabel = new Label("Nom:");
                nomLabel.getStyleClass().add("secouriste-label");
                Label nomValue = new Label(secouriste.getNom());
                grid.add(nomLabel, 0, row);
                grid.add(nomValue, 1, row++);
                
                // Prénom
                Label prenomLabel = new Label("Prénom:");
                prenomLabel.getStyleClass().add("secouriste-label");
                Label prenomValue = new Label(secouriste.getPrenom());
                grid.add(prenomLabel, 0, row);
                grid.add(prenomValue, 1, row++);
                
                // Date de naissance
                Label dateNaissanceLabel = new Label("Date de naissance:");
                dateNaissanceLabel.getStyleClass().add("secouriste-label");
                Label dateNaissanceValue = new Label(secouriste.getDateDeNaissance());
                grid.add(dateNaissanceLabel, 0, row);
                grid.add(dateNaissanceValue, 1, row++);
                
                // Email
                Label emailLabel = new Label("Email:");
                emailLabel.getStyleClass().add("secouriste-label");
                Label emailValue = new Label(secouriste.getEmail());
                grid.add(emailLabel, 0, row);
                grid.add(emailValue, 1, row++);
                
                // Téléphone
                Label telLabel = new Label("Téléphone:");
                telLabel.getStyleClass().add("secouriste-label");
                Label telValue = new Label(secouriste.getTel());
                grid.add(telLabel, 0, row);
                grid.add(telValue, 1, row++);
                
                // Adresse
                Label adresseLabel = new Label("Adresse:");
                adresseLabel.getStyleClass().add("secouriste-label");
                Label adresseValue = new Label(secouriste.getAdresse());
                grid.add(adresseLabel, 0, row);
                grid.add(adresseValue, 1, row++);
                
                // Compétences
                Label competencesLabel = new Label("Compétences:");
                competencesLabel.getStyleClass().add("secouriste-label");
                String competences = secouriste.getCompetences() != null
                    ? String.join(", ", secouriste.getCompetences().stream()
                        .map(model.data.Competence::getIntitule)
                        .toList())
                    : "Aucune compétence";
                Label competencesValue = new Label(competences);
                grid.add(competencesLabel, 0, row);
                grid.add(competencesValue, 1, row);
                
                // Set the GridPane as the cell's graphic
                setGraphic(grid);
                setText(null);
            }
        }
    }
}