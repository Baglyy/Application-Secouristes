package controller;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import model.LoginModel;

public class LoginController {
    
    private TextField identifiantField;
    private PasswordField motDePasseField;
    private Button seConnecterButton;
    private LoginModel model;
    
    public LoginController(TextField identifiantField, PasswordField motDePasseField, Button seConnecterButton) {
        this.identifiantField = identifiantField;
        this.motDePasseField = motDePasseField;
        this.seConnecterButton = seConnecterButton;
        this.model = new LoginModel();
        
        setupBindings();
        setupListeners();
    }
    
    private void setupBindings() {
        // Liaison bidirectionnelle entre les champs et le modèle
        identifiantField.textProperty().bindBidirectional(model.identifiantProperty());
        motDePasseField.textProperty().bindBidirectional(model.motDePasseProperty());
    }
    
    private void setupListeners() {
        // Listener sur le bouton de connexion
        seConnecterButton.setOnAction(this::handleSeConnecter);
        
        // Listener pour activer/désactiver le bouton selon le contenu des champs
        identifiantField.textProperty().addListener((obs, oldText, newText) -> updateButtonState());
        motDePasseField.textProperty().addListener((obs, oldText, newText) -> updateButtonState());
        
        // Permettre la connexion avec Entrée
        identifiantField.setOnAction(this::handleSeConnecter);
        motDePasseField.setOnAction(this::handleSeConnecter);
        
        // État initial du bouton
        updateButtonState();
    }
    
    private void handleSeConnecter(ActionEvent event) {
        System.out.println("Tentative de connexion avec:");
        System.out.println("Identifiant: " + model.getIdentifiant());
        System.out.println("Mot de passe: [PROTÉGÉ]");
        
        // Vider les champs après clic sur "Se connecter"
        model.clearFields();
        
        // Ici, on pourrait ajouter la logique de connexion réelle
        // Par exemple: appel à un service d'authentification
    }
    
    private void updateButtonState() {
        // Active le bouton seulement si les deux champs sont remplis
        boolean fieldsNotEmpty = !identifiantField.getText().trim().isEmpty() 
                               && !motDePasseField.getText().trim().isEmpty();
        seConnecterButton.setDisable(!fieldsNotEmpty);
    }
    
    // Méthodes publiques pour interaction externe (si nécessaire)
    public void clearForm() {
        model.clearFields();
    }
    
    public LoginModel getModel() {
        return model;
    }
}