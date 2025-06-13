package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controller.AdminDispositifsController;
import model.AdminDispositifsModel;
import model.data.Site;
import model.data.Sport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDispositifsView {
    
    private AnchorPane root;
    private TextField idTextField;
    private TextField horaireDepTextField;
    private TextField horaireFinTextField;
    private ComboBox<Site> siteComboBox;
    private ComboBox<Sport> sportComboBox;
    private TextField jourTextField;
    private TextField moisTextField;
    private TextField anneeTextField;
    private Button ajouterButton;
    private Button supprimerButton;
    private Button afficherCarteButton;
    private ListView<AdminDispositifsModel.DispositifView> deviceListView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private WebView mapWebView;
    private AdminDispositifsController controller;
    private Stage mapStage;
    
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
        
        mainContent.getChildren().add(topSection);
        
        // Positionnement des éléments
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        
        AnchorPane.setTopAnchor(mainContent, 80.0);
        AnchorPane.setLeftAnchor(mainContent, 0.0);
        AnchorPane.setRightAnchor(mainContent, 0.0);
        AnchorPane.setBottomAnchor(mainContent, 0.0);
        
        root.getChildren().addAll(header, mainContent);
        
        // Créer la WebView pour la popup (cachée)
        createMapWebView();
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
        notificationIcon.getStyleClass().add("profile-icon");
        
        Label notificationBadge = new Label("1");
        notificationBadge.getStyleClass().add("notification-badge");
        
        AnchorPane notificationContainer = new AnchorPane();
        notificationContainer.getChildren().addAll(notificationIcon, notificationBadge);
        AnchorPane.setTopAnchor(notificationBadge, -5.0);
        AnchorPane.setRightAnchor(notificationBadge, -5.0);
        
        // Icône maison pour retour
        homeIcon = new Label("🏠");
        homeIcon.getStyleClass().addAll("profile-icon", "clickable");
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
        formulaire.setSpacing(4);
        formulaire.setPrefWidth(300);
        formulaire.getStyleClass().add("form-container");
        formulaire.setPadding(new Insets(20));
        
        // Titre du formulaire
        Label formTitle = new Label("Ajouter un dispositif");
        formTitle.getStyleClass().add("form-title");
        
        // Champ ID
        Label idLabel = new Label("ID du dispositif:");
        idLabel.getStyleClass().add("form-label");
        idTextField = new TextField();
        idTextField.setPromptText("Ex: 123456789");
        idTextField.getStyleClass().add("form-textfield");
        
        // Champ horaire départ
        Label horaireDepLabel = new Label("Horaire de départ:");
        horaireDepLabel.getStyleClass().add("form-label");
        horaireDepTextField = new TextField();
        horaireDepTextField.setPromptText("Ex: 08:00:00");
        horaireDepTextField.getStyleClass().add("form-textfield");
        
        // Champ horaire fin
        Label horaireFinLabel = new Label("Horaire de fin:");
        horaireFinLabel.getStyleClass().add("form-label");
        horaireFinTextField = new TextField();
        horaireFinTextField.setPromptText("Ex: 18:00:00");
        horaireFinTextField.getStyleClass().add("form-textfield");
        
        // ComboBox site
        Label siteLabel = new Label("Site:");
        siteLabel.getStyleClass().add("form-label");
        siteComboBox = new ComboBox<>();
        siteComboBox.setPromptText("Sélectionner un site");
        siteComboBox.getStyleClass().add("form-textfield");
        siteComboBox.setPrefWidth(250);
        
        // ComboBox sport
        Label sportLabel = new Label("Sport:");
        sportLabel.getStyleClass().add("form-label");
        sportComboBox = new ComboBox<>();
        sportComboBox.setPromptText("Sélectionner un sport");
        sportComboBox.getStyleClass().add("form-textfield");
        sportComboBox.setPrefWidth(250);
        
        // Champ jour
        Label jourLabel = new Label("Jour:");
        jourLabel.getStyleClass().add("form-label");
        jourTextField = new TextField();
        jourTextField.setPromptText("Ex: 13");
        jourTextField.getStyleClass().add("form-textfield");
        
        // Champ mois
        Label moisLabel = new Label("Mois:");
        moisLabel.getStyleClass().add("form-label");
        moisTextField = new TextField();
        moisTextField.setPromptText("Ex: 6");
        moisTextField.getStyleClass().add("form-textfield");
        
        // Champ année
        Label anneeLabel = new Label("Année:");
        anneeLabel.getStyleClass().add("form-label");
        anneeTextField = new TextField();
        anneeTextField.setPromptText("Ex: 2025");
        anneeTextField.getStyleClass().add("form-textfield");
        
        // Bouton ajouter
        ajouterButton = new Button("➕ Ajouter dispositif");
        ajouterButton.getStyleClass().addAll("dashboard-button", "active-button");
        ajouterButton.setPrefWidth(250);
        
        formulaire.getChildren().addAll(
            formTitle,
            idLabel, idTextField,
            horaireDepLabel, horaireDepTextField,
            horaireFinLabel, horaireFinTextField,
            siteLabel, siteComboBox,
            sportLabel, sportComboBox,
            jourLabel, jourTextField,
            moisLabel, moisTextField,
            anneeLabel, anneeTextField,
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
        deviceListView.setPrefHeight(280);
        
        // Personnaliser l'affichage des éléments de la liste
        deviceListView.setCellFactory(listView -> new ListCell<AdminDispositifsModel.DispositifView>() {
            @Override
            protected void updateItem(AdminDispositifsModel.DispositifView item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox content = new VBox(2);
                    Label nameLabel = new Label("DPS-" + item.getId());
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
                    Label detailsLabel = new Label(String.format("%s, %s, %s",
                        item.getSite().getNom(),
                        item.getSport().getNom(),
                        item.getJournee().toString()));
                    detailsLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
                    content.getChildren().addAll(nameLabel, detailsLabel);
                    setGraphic(content);
                    setText(null);
                }
            }
        });
        
        // Bouton pour afficher la carte
        afficherCarteButton = new Button("🗺️ Afficher la carte");
        afficherCarteButton.getStyleClass().addAll("dashboard-button", "active-button");
        afficherCarteButton.setPrefWidth(200);
        afficherCarteButton.setPrefHeight(40);
        afficherCarteButton.setOnAction(e -> openMapPopup());
        
        listeSection.getChildren().addAll(titleRow, deviceListView, afficherCarteButton);
        
        return listeSection;
    }
    
    private void createMapWebView() {
        mapWebView = new WebView();
        mapWebView.setPrefSize(900, 600);
    }
    
    private void openMapPopup() {
        if (mapStage == null) {
            mapStage = new Stage();
            mapStage.setTitle("Carte des dispositifs de secours");
            mapStage.initModality(Modality.APPLICATION_MODAL);
            mapStage.setResizable(true);
            mapStage.setMinWidth(800);
            mapStage.setMinHeight(600);
            
            // Container pour la popup
            VBox popupContent = new VBox();
            popupContent.setSpacing(10);
            popupContent.setPadding(new Insets(10));
            
            // Header de la popup avec titre et bouton fermer
            HBox popupHeader = new HBox();
            popupHeader.setAlignment(Pos.CENTER_LEFT);
            popupHeader.setSpacing(10);
            popupHeader.setPadding(new Insets(10));
            popupHeader.getStyleClass().add("popup-header");
            
            Label popupTitle = new Label("🗺️ Localisation des dispositifs");
            popupTitle.getStyleClass().add("form-title");
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            Button fermerButton = new Button("✖ Fermer");
            fermerButton.getStyleClass().add("dashboard-button");
            fermerButton.setOnAction(e -> mapStage.close());
            
            popupHeader.getChildren().addAll(popupTitle, spacer, fermerButton);
            
            // Ajout de la WebView
            VBox.setVgrow(mapWebView, Priority.ALWAYS);
            
            popupContent.getChildren().addAll(popupHeader, mapWebView);
            
            Scene popupScene = new Scene(popupContent, 900, 650);
            
            // Appliquer le même style que la fenêtre principale
            try {
                String cssPath = getClass().getResource("../style.css").toExternalForm();
                popupScene.getStylesheets().add(cssPath);
            } catch (Exception ex) {
                System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + " Impossible de charger le fichier CSS pour la popup");
                ex.printStackTrace();
            }
            
            mapStage.setScene(popupScene);
            
            // Centrer la popup par rapport à la fenêtre principale
            mapStage.setOnShowing(e -> {
                Stage parentStage = (Stage) root.getScene().getWindow();
                if (parentStage != null) {
                    mapStage.setX(parentStage.getX() + (parentStage.getWidth() - mapStage.getWidth()) / 2);
                    mapStage.setY(parentStage.getY() + (parentStage.getHeight() - mapStage.getHeight()) / 2);
                }
            });
        }
        
        mapStage.show();
        
        // Rafraîchir la carte quand la popup s'ouvre
        if (controller != null) {
            controller.refreshMap();
        }
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new AdminDispositifsController(
                idTextField,
                horaireDepTextField,
                horaireFinTextField,
                siteComboBox,
                sportComboBox,
                jourTextField,
                moisTextField,
                anneeTextField,
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
            System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + " Impossible de charger le fichier CSS style.css");
            e.printStackTrace();
        }
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public AdminDispositifsController getController() {
        return controller;
    }
    
    public void closeMapPopup() {
        if (mapStage != null && mapStage.isShowing()) {
            mapStage.close();
        }
    }
}