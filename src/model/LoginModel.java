package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Modèle de données pour la gestion du formulaire de connexion.
 * Contient les propriétés d'identifiant et de mot de passe, ainsi que les méthodes associées.
 */
public class LoginModel {

    private final StringProperty identifiant = new SimpleStringProperty("");
    private final StringProperty motDePasse = new SimpleStringProperty("");

    // Getters pour les propriétés

    /**
     * Retourne la propriété JavaFX de l'identifiant.
     * @return la propriété identifiant
     */
    public StringProperty identifiantProperty() {
        return identifiant;
    }

    /**
     * Retourne la propriété JavaFX du mot de passe.
     * @return la propriété motDePasse
     */
    public StringProperty motDePasseProperty() {
        return motDePasse;
    }

    // Getters pour les valeurs

    /**
     * Retourne la valeur de l'identifiant.
     * @return l'identifiant saisi
     */
    public String getIdentifiant() {
        return identifiant.get();
    }

    /**
     * Retourne la valeur du mot de passe.
     * @return le mot de passe saisi
     */
    public String getMotDePasse() {
        return motDePasse.get();
    }

    // Setters

    /**
     * Définit l'identifiant.
     * @param identifiant identifiant à définir
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant.set(identifiant);
    }

    /**
     * Définit le mot de passe.
     * @param motDePasse mot de passe à définir
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse.set(motDePasse);
    }

    // Méthode pour vider les champs

    /**
     * Réinitialise les champs identifiant et mot de passe à une chaîne vide.
     */
    public void clearFields() {
        setIdentifiant("");
        setMotDePasse("");
    }

    // Méthode pour valider la connexion (pour plus tard)

    /**
     * Valide que les champs identifiant et mot de passe ne sont pas vides.
     * Cette méthode peut être étendue pour une vérification plus poussée.
     * @return true si les champs sont valides, false sinon
     */
    public boolean validateLogin() {
        return !getIdentifiant().trim().isEmpty() && !getMotDePasse().trim().isEmpty();
    }

    
    // --- MÉTHODES POUR FACILITER LES TESTS DE SCÉNARIO ---

    /**
     * Simule une validation de connexion pour les tests de scénario.
     * @param testUsername Le nom d'utilisateur à tester.
     * @param testPassword Le mot de passe à tester.
     * @return true si les identifiants correspondent à des valeurs de test prédéfinies, false sinon.
     */
    public boolean validerAdminPourTest(String testUsername, String testPassword) {
        // Logique de test simple: vérifie si c'est "admin" / "JO2030"
        if ("admin".equals(testUsername) && "JO2030".equals(testPassword)) {
            this.utilisateurConnecteSimule = testUsername; // Stocker le nom pour getUtilisateurConnectePourTest
            return true;
        }
        this.utilisateurConnecteSimule = null;
        return false;
    }

    /**
     * Retourne le nom d'utilisateur qui a été "connecté" avec succès par validerAdminPourTest.
     * @return Le nom d'utilisateur ou null si la validation de test a échoué.
     */
    public String getUtilisateurConnectePourTest() {
        return this.utilisateurConnecteSimule;
    }
}
