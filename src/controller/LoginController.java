package controller;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.LoginModel;
import model.dao.SecouristeDAO;
import model.data.Secouriste;
import view.DashboardView;
import view.AdminDashboardView;

public class LoginController {
    
    private TextField identifiantField;
    private PasswordField motDePasseField;
    private Button seConnecterButton;
    private LoginModel model;
    private Stage primaryStage;
    private SecouristeDAO secouristeDAO;
    
    // Constructeur principal avec primaryStage
    public LoginController(TextField identifiantField, PasswordField motDePasseField, 
                          Button seConnecterButton, Stage primaryStage) {
        this.identifiantField = identifiantField;
        this.motDePasseField = motDePasseField;
        this.seConnecterButton = seConnecterButton;
        this.primaryStage = primaryStage;
        this.model = new LoginModel();
        this.secouristeDAO = new SecouristeDAO();
        
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
        if (!model.validateLogin()) {
            System.out.println("Connexion impossible : champs vides");
            return;
        }
        
        System.out.println("Tentative de connexion avec:");
        System.out.println("Identifiant: " + model.getIdentifiant());
        System.out.println("Mot de passe: [PROTÉGÉ]");
        
        // Authentification avec les nouvelles règles
        if (authenticateUser(model.getIdentifiant(), model.getMotDePasse())) {
            System.out.println("Connexion réussie ! Redirection vers le tableau de bord...");
            
            // Vérifier si l'utilisateur est admin
            if ("admin".equalsIgnoreCase(model.getIdentifiant().trim())) {
                navigateToAdminDashboard();
            } else {
                navigateToDashboard();
            }
        } else {
            System.out.println("Échec de la connexion : identifiants incorrects");
            // Ici, on pourrait afficher un message d'erreur à l'utilisateur
        }
    }
    
    private boolean authenticateUser(String identifiant, String motDePasse) {
        String cleanIdentifiant = identifiant.trim();
        String cleanMotDePasse = motDePasse.trim();
        
        // Vérifier si les champs ne sont pas vides
        if (cleanIdentifiant.isEmpty() || cleanMotDePasse.isEmpty()) {
            return false;
        }
        
        // Cas spécial pour l'administrateur
        if ("admin".equalsIgnoreCase(cleanIdentifiant)) {
            return "JO2030".equals(cleanMotDePasse);
        }
        
        // Pour les autres utilisateurs : vérifier dans la base de données
        try {
            // Essayer de convertir l'identifiant en ID (long)
            long secouristeId = Long.parseLong(cleanIdentifiant);
            
            // Chercher le secouriste par ID
            Secouriste secouriste = secouristeDAO.findByID(secouristeId);
            
            if (secouriste != null) {
                // Vérifier si le mot de passe correspond au nom du secouriste
                return cleanMotDePasse.equalsIgnoreCase(secouriste.getNom().trim());
            }
            
        } catch (NumberFormatException e) {
            // L'identifiant n'est pas un nombre valide
            System.out.println("Identifiant invalide : doit être un ID numérique pour les secouristes");
            return false;
        } catch (Exception e) {
            // Erreur lors de l'accès à la base de données
            System.err.println("Erreur lors de l'authentification : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
        return false;
    }
    
    private void navigateToDashboard() {
        if (primaryStage == null) {
            System.err.println("Erreur: primaryStage est null - impossible de naviguer vers le tableau de bord");
            // Vider les champs quand même
            model.clearFields();
            return;
        }
        
        try {
            // Récupérer le nom du secouriste pour l'affichage
            String displayName = getUserDisplayName();
            
            // Créer la vue du tableau de bord avec le nom d'utilisateur
            DashboardView dashboardView = new DashboardView(displayName);
            
            // Créer une nouvelle scène avec le tableau de bord
            Scene dashboardScene = new Scene(dashboardView.getRoot());
            
            // Essayer d'appliquer le CSS pour le dashboard
            try {
                dashboardScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
            } catch (Exception cssException) {
                System.out.println("Attention: Fichier CSS style.css non trouvé, styles par défaut appliqués");
            }
            
            // Changer la scène
            primaryStage.setScene(dashboardScene);
            primaryStage.setTitle("SecuOptix - Tableau de bord");
            
            // Vider les champs de connexion pour la sécurité
            model.clearFields();
            
            System.out.println("Navigation vers le tableau de bord utilisateur réussie !");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le tableau de bord: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void navigateToAdminDashboard() {
        if (primaryStage == null) {
            System.err.println("Erreur: primaryStage est null - impossible de naviguer vers le tableau de bord admin");
            // Vider les champs quand même
            model.clearFields();
            return;
        }
        
        try {
            // Créer la vue du tableau de bord admin avec le nom d'utilisateur
            AdminDashboardView adminDashboardView = new AdminDashboardView("ADMINISTRATEUR");
            
            // Créer une nouvelle scène avec le tableau de bord admin
            Scene adminDashboardScene = new Scene(adminDashboardView.getRoot());
            
            // Essayer d'appliquer le CSS pour le dashboard
            try {
                adminDashboardScene.getStylesheets().add(getClass().getResource("../style.css").toExternalForm());
            } catch (Exception cssException) {
                System.out.println("Attention: Fichier CSS style.css non trouvé, styles par défaut appliqués");
            }
            
            // Changer la scène
            primaryStage.setScene(adminDashboardScene);
            primaryStage.setTitle("SecuOptix - Tableau de bord Administrateur");
            
            // Vider les champs de connexion pour la sécurité
            model.clearFields();
            
            System.out.println("Navigation vers le tableau de bord administrateur réussie !");
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le tableau de bord admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String getUserDisplayName() {
        try {
            long secouristeId = Long.parseLong(model.getIdentifiant().trim());
            Secouriste secouriste = secouristeDAO.findByID(secouristeId);
            
            if (secouriste != null) {
                return (secouriste.getPrenom() + " " + secouriste.getNom()).toUpperCase();
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération du nom d'affichage : " + e.getMessage());
        }
        
        // Fallback si erreur
        return model.getIdentifiant().toUpperCase();
    }
    
    private void updateButtonState() {
        // Active le bouton seulement si les deux champs sont remplis
        boolean fieldsNotEmpty = !identifiantField.getText().trim().isEmpty() 
                               && !motDePasseField.getText().trim().isEmpty();
        seConnecterButton.setDisable(!fieldsNotEmpty);
    }
    
    // Méthodes publiques pour interaction externe
    public void clearForm() {
        model.clearFields();
    }
    
    public LoginModel getModel() {
        return model;
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}