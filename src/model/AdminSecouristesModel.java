package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.CompetenceDAO;
import model.dao.SecouristeDAO;
import model.data.Competence;
import model.data.Secouriste;
import java.util.ArrayList;

/**
 * Modèle utilisé pour gérer les secouristes et leurs compétences dans l'application.
 * Fournit les opérations de lecture, ajout, suppression et mise à jour pour les secouristes.
 */
public class AdminSecouristesModel {

    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Secouriste> secouristes = FXCollections.observableArrayList();
    private final ObservableList<Competence> competences = FXCollections.observableArrayList();
    private final SecouristeDAO secouristeDAO;
    private final CompetenceDAO competenceDAO;

    /**
     * Constructeur avec nom d'utilisateur.
     * Initialise les DAOs et charge les données depuis la base.
     *
     * @param nomUtilisateur le nom de l'utilisateur connecté
     */
    public AdminSecouristesModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.secouristeDAO = new SecouristeDAO();
        this.competenceDAO = new CompetenceDAO();
        initializeData();
    }

    /**
     * Initialise les données de la base (secouristes et compétences).
     */
    private void initializeData() {
        secouristes.addAll(secouristeDAO.findAll());
        competences.addAll(competenceDAO.findAll());
    }

    /**
     * @return propriété observable du nom de l'utilisateur
     */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /**
     * @return liste observable des secouristes
     */
    public ObservableList<Secouriste> getSecouristes() {
        return secouristes;
    }

    /**
     * @return liste observable de toutes les compétences
     */
    public ObservableList<Competence> getAllCompetences() {
        return competences;
    }

    /**
     * @return le nom actuel de l'utilisateur
     */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    /**
     * Modifie le nom de l'utilisateur connecté.
     *
     * @param nomUtilisateur nouveau nom d'utilisateur
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Ajoute un secouriste à la base et à la liste observable.
     *
     * @param secouriste le secouriste à ajouter
     */
    public void ajouterSecouriste(Secouriste secouriste) {
        if (secouristeDAO.create(secouriste) > 0) {
            secouristes.add(secouriste);
        }
    }

    /**
     * Supprime un secouriste de la base et de la liste observable.
     *
     * @param secouriste le secouriste à supprimer
     */
    public void supprimerSecouriste(Secouriste secouriste) {
        if (secouristeDAO.delete(secouriste) > 0) {
            secouristes.remove(secouriste);
        }
    }

    /**
     * Met à jour les compétences d'un secouriste et synchronise avec la base.
     * Note : suppose que {@code SecouristeDAO.update()} gère les compétences.
     *
     * @param secouriste le secouriste à modifier
     * @param newCompetences nouvelles compétences à lui associer
     */
    public void updateSecouristeCompetences(Secouriste secouriste, ObservableList<Competence> newCompetences) {
        secouriste.setCompetences(new ArrayList<>(newCompetences));
        secouristeDAO.update(secouriste);
        int index = secouristes.indexOf(secouriste);
        if (index >= 0) {
            secouristes.set(index, secouriste);
        }
    }

    /**
     * Vérifie si un secouriste existe en fonction de son nom et prénom.
     * Note : cette méthode utilise un ID de 0 pour la recherche, ce qui est fragile si les IDs sont obligatoires.
     *
     * @param nom nom du secouriste
     * @param prenom prénom du secouriste
     * @return true si un secouriste correspondant existe
     */
    public boolean secouristeExists(String nom, String prenom) {
        return secouristeDAO.findByIdAndNom(0, nom) != null &&
               secouristeDAO.findByIdAndNom(0, nom).getPrenom().equals(prenom);
    }

    /**
     * Vérifie si un identifiant de secouriste existe déjà en base.
     *
     * @param id identifiant à vérifier
     * @return true si le secouriste existe
     */
    public boolean idExists(long id) {
        return secouristeDAO.findByID(id) != null;
    }
}
