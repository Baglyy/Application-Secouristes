package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Modèle pour gérer l'état du tableau de bord utilisateur (vue secouriste).
 * Gère le nom de l'utilisateur et la section actuellement active de l'application.
 */
public class DashboardModel {

    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final StringProperty sectionActive = new SimpleStringProperty("affectations");

    /**
     * Constructeur par défaut.
     */
    public DashboardModel() {
        // Constructeur par défaut
    }

    /**
     * Constructeur avec nom d'utilisateur.
     * @param nomUtilisateur le nom de l'utilisateur connecté
     */
    public DashboardModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    // Getters pour les propriétés

    /**
     * Retourne la propriété JavaFX liée au nom de l'utilisateur.
     * @return StringProperty du nom utilisateur
     */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /**
     * Retourne la propriété JavaFX liée à la section active.
     * @return StringProperty de la section active
     */
    public StringProperty sectionActiveProperty() {
        return sectionActive;
    }

    // Getters pour les valeurs

    /**
     * Retourne le nom actuel de l'utilisateur.
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

    // Setters

    /**
     * Définit un nouveau nom d'utilisateur.
     * @param nomUtilisateur le nom à définir
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Définit une nouvelle section active.
     * @param sectionActive le nom de la section à activer
     */
    public void setSectionActive(String sectionActive) {
        this.sectionActive.set(sectionActive);
    }

    // Méthodes pour gérer les sections

    /**
     * Vérifie si la section active est "affectations".
     * @return true si section "affectations" active
     */
    public boolean isAffectationsActive() {
        return "affectations".equals(getSectionActive());
    }

    /**
     * Vérifie si la section active est "planning".
     * @return true si section "planning" active
     */
    public boolean isPlanningActive() {
        return "planning".equals(getSectionActive());
    }

    /**
     * Vérifie si la section active est "disponibilites".
     * @return true si section "disponibilites" active
     */
    public boolean isDisponibilitesActive() {
        return "disponibilites".equals(getSectionActive());
    }

    /**
     * Active la section "affectations".
     */
    public void activerAffectations() {
        setSectionActive("affectations");
    }

    /**
     * Active la section "planning".
     */
    public void activerPlanning() {
        setSectionActive("planning");
    }

    /**
     * Active la section "disponibilites".
     */
    public void activerDisponibilites() {
        setSectionActive("disponibilites");
    }
}
