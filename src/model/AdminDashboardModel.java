package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Modèle utilisé pour représenter l'état de la vue d'administration.
 * Permet de suivre le nom de l'utilisateur connecté et la section active de l'interface.
 */
public class AdminDashboardModel {

    /** Nom de l'utilisateur connecté */
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");

    /** Section actuellement active dans l'interface */
    private final StringProperty sectionActive = new SimpleStringProperty("dispositifs");

    /**
     * Constructeur par défaut.
     */
    public AdminDashboardModel() {
    }

    /**
     * Constructeur avec nom d'utilisateur initial.
     * @param nomUtilisateur nom de l'utilisateur connecté
     */
    public AdminDashboardModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Retourne la propriété JavaFX du nom d'utilisateur.
     * @return propriété nomUtilisateur
     */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /**
     * Retourne la propriété JavaFX de la section active.
     * @return propriété sectionActive
     */
    public StringProperty sectionActiveProperty() {
        return sectionActive;
    }

    /**
     * Retourne le nom de l'utilisateur.
     * @return nom de l'utilisateur
     */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    /**
     * Retourne la section actuellement active.
     * @return nom de la section active
     */
    public String getSectionActive() {
        return sectionActive.get();
    }

    /**
     * Définit le nom de l'utilisateur.
     * @param nomUtilisateur nouveau nom
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Définit la section active.
     * @param sectionActive nom de la section à activer
     */
    public void setSectionActive(String sectionActive) {
        this.sectionActive.set(sectionActive);
    }

    /**
     * Vérifie si la section active est "dispositifs".
     * @return true si "dispositifs" est active
     */
    public boolean isDispositifsActive() {
        return "dispositifs".equals(getSectionActive());
    }

    /**
     * Vérifie si la section active est "affectations_secouristes".
     * @return true si "affectations_secouristes" est active
     */
    public boolean isAffectationsSecouristesActive() {
        return "affectations_secouristes".equals(getSectionActive());
    }

    /**
     * Vérifie si la section active est "secouristes".
     * @return true si "secouristes" est active
     */
    public boolean isSecouristesActive() {
        return "secouristes".equals(getSectionActive());
    }

    /**
     * Vérifie si la section active est "competences".
     * @return true si "competences" est active
     */
    public boolean isCompetencesActive() {
        return "competences".equals(getSectionActive());
    }

    /**
     * Active la section "dispositifs".
     */
    public void activerDispositifs() {
        setSectionActive("dispositifs");
    }

    /**
     * Active la section "affectations_secouristes".
     */
    public void activerAffectationsSecouristes() {
        setSectionActive("affectations_secouristes");
    }

    /**
     * Active la section "secouristes".
     */
    public void activerSecouristes() {
        setSectionActive("secouristes");
    }

    /**
     * Active la section "competences".
     */
    public void activerCompetences() {
        setSectionActive("competences");
    }
}
