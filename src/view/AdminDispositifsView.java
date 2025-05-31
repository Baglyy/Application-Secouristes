package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import controller.AdminDispositifsController;
import model.AdminDispositifsModel;

public class AdminDispositifsView {
    
    private AnchorPane root;
    private TextField nomTextField;
    private TextField latitudeTextField;
    private TextField longitudeTextField;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<AdminDispositifsModel.Dispositif> deviceListView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private WebView mapWebView;
    private AdminDispositifsController controller;
    
    public AdminDispositifsView(String nomUtilisateur) {
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
        AnchorPane header = createHeader();
        
        // Container principal pour le contenu
        VBox mainContent = new VBox();
        mainContent.setSpacing(20);
        mainContent.setPadding(new Insets(20, 30, 20, 30));
        
        // Container horizontal pour le formulaire et la liste
        HBox topSection = new HBox();
        topSection.setSpacing(30);
        topSection.setAlignment(Pos.TOP_LEFT);
        
        // Formulaire d'ajout (partie gauche)
        VBox formulaire = createFormulaire();
        
        // Liste des dispositifs (partie droite)
        VBox listeSection = createListeSection();
        
        topSection.getChildren().addAll(formulaire, listeSection);
        
        // Carte interactive (partie basse)
        VBox carteSection = createCarteSection();
        
        mainContent.getChildren().addAll(topSection, carteSection);
        
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
    
    private AnchorPane createHeader() {
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("dashboard-header");
        
        // Container du header
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        // Titre
        Label titleLabel = new Label("Gestion des dispositifs de secours");
        titleLabel.getStyleClass().add("dashboard-title");
        
        // Spacer
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
        
        // Icône maison pour retour
        homeIcon = new Label("🏠");
        homeIcon.getStyleClass().addAll("notification-icon", "clickable");
        homeIcon.setStyle("-fx-cursor: hand;");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeIcon);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        // Ajout du contenu au header
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        return header;
    }
    
    private VBox createFormulaire() {
        VBox formulaire = new VBox();
        formulaire.setSpacing(15);
        formulaire.setPrefWidth(300);
        formulaire.getStyleClass().add("form-container");
        formulaire.setPadding(new Insets(20));
        
        // Titre du formulaire
        Label formTitle = new Label("Ajouter un dispositif");
        formTitle.getStyleClass().add("form-title");
        
        // Champ nom
        Label nomLabel = new Label("Nom du dispositif:");
        nomLabel.getStyleClass().add("form-label");
        nomTextField = new TextField();
        nomTextField.setPromptText("Ex: AlpinAlerte");
        nomTextField.getStyleClass().add("form-textfield");
        
        // Champ latitude
        Label latLabel = new Label("Latitude:");
        latLabel.getStyleClass().add("form-label");
        latitudeTextField = new TextField();
        latitudeTextField.setPromptText("Ex: 45.9237");
        latitudeTextField.getStyleClass().add("form-textfield");
        
        // Champ longitude
        Label lngLabel = new Label("Longitude:");
        lngLabel.getStyleClass().add("form-label");
        longitudeTextField = new TextField();
        longitudeTextField.setPromptText("Ex: 6.8694");
        longitudeTextField.getStyleClass().add("form-textfield");
        
        // Bouton ajouter
        ajouterButton = new Button("➕ Ajouter dispositif");
        ajouterButton.getStyleClass().addAll("dashboard-button", "active-button");
        ajouterButton.setPrefWidth(250);
        
        formulaire.getChildren().addAll(
            formTitle, 
            nomLabel, nomTextField,
            latLabel, latitudeTextField,
            lngLabel, longitudeTextField,
            ajouterButton
        );
        
        return formulaire;
    }
    
    private VBox createListeSection() {
        VBox listeSection = new VBox();
        listeSection.setSpacing(15);
        listeSection.setPrefWidth(400);
        
        // Titre et bouton supprimer
        HBox titleRow = new HBox();
        titleRow.setAlignment(Pos.CENTER_LEFT);
        titleRow.setSpacing(20);
        
        Label listeTitle = new Label("Liste des dispositifs");
        listeTitle.getStyleClass().add("form-title");
        
        supprimerButton = new Button("🗑️ Supprimer");
        supprimerButton.getStyleClass().add("dashboard-button");
        supprimerButton.setDisable(true);
        
        titleRow.getChildren().addAll(listeTitle, supprimerButton);
        
        // Liste des dispositifs
        deviceListView = new ListView<>();
        deviceListView.getStyleClass().add("device-list");
        deviceListView.setPrefHeight(200);
        
        // Personnaliser l'affichage des éléments de la liste
        deviceListView.setCellFactory(listView -> new ListCell<AdminDispositifsModel.Dispositif>() {
            @Override
            protected void updateItem(AdminDispositifsModel.Dispositif item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox content = new VBox(2);
                    Label nameLabel = new Label(item.getNom());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
                    Label coordsLabel = new Label(String.format("%.4f, %.4f", item.getLatitude(), item.getLongitude()));
                    coordsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
                    content.getChildren().addAll(nameLabel, coordsLabel);
                    setGraphic(content);
                    setText(null);
                }
            }
        });
        
        listeSection.getChildren().addAll(titleRow, deviceListView);
        
        return listeSection;
    }
    
    private VBox createCarteSection() {
        VBox carteSection = new VBox();
        carteSection.setSpacing(10);
        
        // Titre de la carte
        Label carteTitle = new Label("🗺️ Localisation des dispositifs");
        carteTitle.getStyleClass().add("form-title");
        
        // Carte interactive
        mapWebView = new WebView();
        mapWebView.setMinHeight(250);
        mapWebView.setPrefHeight(250);
        mapWebView.setMaxHeight(250); 
        mapWebView.getStyleClass().add("map-view");
        
        carteSection.getChildren().addAll(carteTitle, mapWebView);
        
        return carteSection;
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new AdminDispositifsController(
                nomTextField,
                latitudeTextField,
                longitudeTextField,
                ajouterButton,
                supprimerButton,
                deviceListView,
                nomUtilisateurLabel,
                homeIcon,
                mapWebView,
                nomUtilisateur);
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
    
    public AdminDispositifsController getController() {
        return controller;
    }
}