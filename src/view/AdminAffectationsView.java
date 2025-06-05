package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import controller.AdminAffectationsController;
import model.AdminAffectationsModel;
import model.data.DPS;
import model.data.Secouriste;

public class AdminAffectationsView {
    
    private AnchorPane root;
    private Button createButton;
    private TableView<AdminAffectationsModel.Affectation> tableView;
    private TableColumn<AdminAffectationsModel.Affectation, String> colDate;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminAffectationsController controller;
    
    public AdminAffectationsView(String nomUtilisateur) {
        createView();
        setupController(nomUtilisateur);
        loadStylesheet();
    }
    
    private void createView() {
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("dashboard-root");
        
        // Header
        AnchorPane header = new AnchorPane();
        header.setPrefHeight(70);
        header.getStyleClass().add("dashboard-header");
        
        HBox headerContent = new HBox();
        headerContent.setAlignment(Pos.CENTER_LEFT);
        headerContent.setPadding(new Insets(15, 30, 15, 30));
        headerContent.setSpacing(20);
        
        Label titleLabel = new Label("Affectations");
        titleLabel.getStyleClass().add("dashboard-title");
        
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
        
        homeIcon = new Label("🏠");
        homeIcon.getStyleClass().add("profile-icon");
        
        userInfo.getChildren().addAll(nomUtilisateurLabel, notificationContainer, homeIcon);
        headerContent.getChildren().addAll(titleLabel, spacer, userInfo);
        
        AnchorPane.setLeftAnchor(headerContent, 0.0);
        AnchorPane.setRightAnchor(headerContent, 0.0);
        AnchorPane.setTopAnchor(headerContent, 0.0);
        AnchorPane.setBottomAnchor(headerContent, 0.0);
        header.getChildren().add(headerContent);
        
        // Main content
        VBox mainContent = new VBox();
        mainContent.setSpacing(30);
        mainContent.setPadding(new Insets(30, 50, 30, 50));
        
        HBox contentContainer = new HBox();
        contentContainer.setSpacing(40);
        contentContainer.setAlignment(Pos.TOP_LEFT);
        
        VBox buttonContainer = new VBox();
        buttonContainer.setAlignment(Pos.TOP_LEFT);
        
        createButton = new Button("➕\nCréer une affectation");
        createButton.getStyleClass().addAll("dashboard-button", "active-button");
        createButton.setPrefSize(200, 60);
        
        buttonContainer.getChildren().add(createButton);
        
        VBox tableContainer = new VBox();
        tableContainer.setAlignment(Pos.TOP_LEFT);
        
        tableView = new TableView<>();
        tableView.getStyleClass().add("secouristes-table");
        tableView.setEditable(false);
        
        colDate = new TableColumn<>("Date");
        colSitesOlympiques = new TableColumn<>("Sites Olympiques");
        colSecouristes = new TableColumn<>("Secouristes");
        
        colDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        colSitesOlympiques.setCellValueFactory(cellData -> cellData.getValue().sitesOlympiquesProperty());
        colSecouristes.setCellValueFactory(cellData -> cellData.getValue().secouristesProperty());
        
        colDate.setPrefWidth(120);
        colSitesOlympiques.setPrefWidth(250);
        colSecouristes.setPrefWidth(200);
        
        tableView.getColumns().addAll(colDate, colSitesOlympiques, colSecouristes);
        
        tableView.setPrefHeight(350);
        tableView.setPrefWidth(570);
        
        tableView.setRowFactory(tv -> {
            TableRow<AdminAffectationsModel.Affectation> row = new TableRow<>();
            row.setPrefHeight(60);
            return row;
        });
        
        tableContainer.getChildren().add(tableView);
        
        contentContainer.getChildren().addAll(buttonContainer, tableContainer);
        mainContent.getChildren().add(contentContainer);
        
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
    
    public void showCreateAffectationDialog(AdminAffectationsController controller) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Créer une affectation");
        
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20));
        
        ComboBox<DPS> dpsCombo = new ComboBox<>();
        dpsCombo.setPromptText("Sélectionner un DPS");
        dpsCombo.setItems(controller.getModel().getAllDPS());
        dpsCombo.setPrefWidth(300);
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Date de l'affectation");
        
        ListView<Secouriste> secouristesList = new ListView<>();
        secouristesList.setPrefHeight(200);
        secouristesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        Button searchButton = new Button("Rechercher des secouristes compétents");
        Button confirmButton = new Button("Créer l'affectation");
        confirmButton.setDisable(true);
        
        searchButton.setOnAction(e -> {
            DPS selectedDPS = dpsCombo.getValue();
            if (selectedDPS != null && datePicker.getValue() != null) {
                secouristesList.setItems(controller.searchCompetentSecouristes(selectedDPS, datePicker.getValue()));
            }
        });
        
        secouristesList.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            DPS selectedDPS = dpsCombo.getValue();
            if (selectedDPS != null) {
                int required = selectedDPS.getNbSecouristesRequis();
                int selected = secouristesList.getSelectionModel().getSelectedItems().size();
                confirmButton.setDisable(selected != required);
            }
        });
        
        confirmButton.setOnAction(e -> {
            controller.createAffectation(
                dpsCombo.getValue(),
                datePicker.getValue(),
                secouristesList.getSelectionModel().getSelectedItems()
            );
            dialog.close();
        });
        
        dialogVbox.getChildren().addAll(
            new Label("Dispositif:"), dpsCombo,
            new Label("Date:"), datePicker,
            searchButton, secouristesList, confirmButton
        );
        
        Scene dialogScene = new Scene(dialogVbox, 400, 500);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public AdminAffectationsController getController() {
        return controller;
    }
    
    public Button getCreateButton() {
        return createButton;
    }
    
    private void setupController(String nomUtilisateur) {
        controller = new AdminAffectationsController(
                createButton,
                tableView,
                colDate,
                colSitesOlympiques,
                colSecouristes,
                nomUtilisateurLabel,
                homeIcon,
                nomUtilisateur);
    }
}