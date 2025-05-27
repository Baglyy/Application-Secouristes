package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.LoginController;

public class LoginView {
    
    private AnchorPane root;
    private TextField identifiantField;
    private PasswordField motDePasseField;
    private Button seConnecterButton;
    private LoginController controller;
    private Stage primaryStage;
    
    // Constructor requiring Stage
    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createView();
        setupController();
    }
    
    private void createView() {
        // Conteneur principal avec classe CSS
        root = new AnchorPane();
        root.setPrefSize(1024, 600);
        root.getStyleClass().add("root-pane");
        
        // Container principal du formulaire
        VBox loginContainer = new VBox();
        loginContainer.setAlignment(Pos.CENTER);
        loginContainer.setSpacing(15);
        loginContainer.setPrefSize(240, 280);
        loginContainer.setPadding(new Insets(30));
        loginContainer.getStyleClass().add("login-container");
        
        // Titre
        Label titleLabel = new Label("SecuOptix");
        titleLabel.getStyleClass().add("title");
        
        // Sous-titre
        Label subtitleLabel = new Label("Sécurité et Secours Optimisés");
        subtitleLabel.getStyleClass().add("subtitle");
        
        // Container du formulaire
        VBox formContainer = new VBox();
        formContainer.setSpacing(12);
        formContainer.setPadding(new Insets(20, 15, 15, 15));
        formContainer.getStyleClass().add("form-container");

        // Champ identifiant
        VBox identifiantBox = new VBox(3);
        Label identifiantLabel = new Label("Identifiant");
        identifiantLabel.getStyleClass().add("field-label");
        
        identifiantField = new TextField();
        identifiantField.setPrefHeight(28);
        identifiantField.getStyleClass().add("input-field");
        
        identifiantBox.getChildren().addAll(identifiantLabel, identifiantField);
        
        // Champ mot de passe
        VBox motDePasseBox = new VBox(3);
        Label motDePasseLabel = new Label("Mot de passe");
        motDePasseLabel.getStyleClass().add("field-label");
        
        motDePasseField = new PasswordField();
        motDePasseField.setPrefHeight(28);
        motDePasseField.getStyleClass().add("input-field");
        
        motDePasseBox.getChildren().addAll(motDePasseLabel, motDePasseField);
        
        // Bouton de connexion (initialement désactivé)
        seConnecterButton = new Button("Se connecter");
        seConnecterButton.setPrefSize(180, 32);
        seConnecterButton.getStyleClass().add("connect-button");
        seConnecterButton.setDisable(true); // Désactivé par défaut
        
        // Ajout des éléments au formulaire
        formContainer.getChildren().addAll(identifiantBox, motDePasseBox, seConnecterButton);
        
        // Ajout des éléments au container principal
        loginContainer.getChildren().addAll(titleLabel, subtitleLabel, formContainer);
        
        // Positionnement du container dans l'AnchorPane (centré)
        AnchorPane.setLeftAnchor(loginContainer, 392.0);
        AnchorPane.setRightAnchor(loginContainer, 392.0);
        AnchorPane.setTopAnchor(loginContainer, 160.0);
        
        root.getChildren().add(loginContainer);
    }
    
    private void setupController() {
        // Always use primaryStage to initialize the controller
        controller = new LoginController(identifiantField, motDePasseField, seConnecterButton, primaryStage);
    }
    
    public AnchorPane getRoot() {
        return root;
    }
    
    public LoginController getController() {
        return controller;
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        if (controller != null) {
            controller.setPrimaryStage(primaryStage);
        }
    }
}