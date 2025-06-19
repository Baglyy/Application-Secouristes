package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.CompetenceDAO;
import model.data.Competence;

/**
 * Modèle utilisé pour la gestion des compétences par l'administrateur.
 */
public class AdminCompetencesModel {

    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Competence> competences = FXCollections.observableArrayList();
    private final CompetenceDAO competenceDAO;

    /**
     * Constructeur du modèle AdminCompetencesModel.
     * @param nomUtilisateur nom de l'utilisateur connecté
     */
    public AdminCompetencesModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.competenceDAO = new CompetenceDAO();
        initializeData();
    }

    /**
     * Initialise les compétences à partir de la base de données.
     */
    private void initializeData() {
        competences.addAll(competenceDAO.findAll());
    }

    /**
     * Accès à la propriété JavaFX du nom d'utilisateur.
     * @return propriété nomUtilisateur
     */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /**
     * Récupère la liste observable des compétences.
     * @return liste observable des compétences
     */
    public ObservableList<Competence> getCompetences() {
        return competences;
    }

    /**
     * Alias de {@link #getCompetences()}.
     * @return liste observable des compétences
     */
    public ObservableList<Competence> getAllCompetences() {
        return competences;
    }

    /**
     * Retourne le nom de l'utilisateur.
     * @return nom de l'utilisateur
     */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    /**
     * Modifie le nom de l'utilisateur.
     * @param nomUtilisateur nouveau nom
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Ajoute une nouvelle compétence à la base et à la liste observable.
     * @param competence compétence à ajouter
     */
    public void ajouterCompetence(Competence competence) {
        if (competenceDAO.create(competence) > 0) {
            competences.add(competence);
        }
    }

    /**
     * Supprime une compétence de la base et de la liste observable.
     * @param competence compétence à supprimer
     */
    public void supprimerCompetence(Competence competence) {
        if (competenceDAO.delete(competence) > 0) {
            competences.remove(competence);
        }
    }

    /**
     * Modifie une compétence existante dans la base et met à jour la liste observable.
     * @param ancienneCompetence l'ancienne compétence
     * @param nouvelleCompetence la nouvelle compétence
     * @throws Exception si la compétence n'existe pas dans la liste observable ou en cas d'échec de mise à jour
     */
    public void modifierCompetence(Competence ancienneCompetence, Competence nouvelleCompetence) throws Exception {
        String oldIntitule = ancienneCompetence.getIntitule();
        nouvelleCompetence.setIntitule(nouvelleCompetence.getIntitule().trim());

        if (competenceDAO.updateWithOldCompetence(ancienneCompetence, nouvelleCompetence) > 0) {
            int index = competences.indexOf(ancienneCompetence);
            if (index != -1) {
                competences.set(index, nouvelleCompetence);
            } else {
                throw new Exception("Compétence non trouvée dans la liste observable.");
            }
        } else {
            throw new Exception("Échec de la mise à jour de la compétence.");
        }
    }

    /**
     * Vérifie si une compétence est utilisée comme prérequis pour une autre.
     * @param competence compétence à vérifier
     * @return true si la compétence est utilisée comme prérequis, false sinon
     */
    private boolean isCompetenceUsedAsPrerequisite(Competence competence) {
        return competences.stream()
                .anyMatch(c -> c.getPrerequis() != null &&
                         c.getPrerequis().contains(competence));
    }

    /**
     * Vérifie si une compétence existe dans la base.
     * @param intitule nom de la compétence
     * @return true si elle existe, false sinon
     */
    public boolean competenceExists(String intitule) {
        return competenceDAO.findByID(intitule) != null;
    }

    /**
     * Détermine si une compétence peut être supprimée (non utilisée comme prérequis).
     * @param competence compétence à tester
     * @return true si elle peut être supprimée, false sinon
     */
    public boolean canDeleteCompetence(Competence competence) {
        return !isCompetenceUsedAsPrerequisite(competence);
    }
}
