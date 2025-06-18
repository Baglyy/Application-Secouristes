package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.CompetenceDAO;
import model.data.Competence;
import java.util.ArrayList;

public class AdminCompetencesModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Competence> competences = FXCollections.observableArrayList();
    private final CompetenceDAO competenceDAO;
    
    public AdminCompetencesModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.competenceDAO = new CompetenceDAO();
        initializeData();
    }
    
    private void initializeData() {
        competences.addAll(competenceDAO.findAll());
    }
    
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<Competence> getCompetences() {
        return competences;
    }
    
    public ObservableList<Competence> getAllCompetences() {
        return competences;
    }
    
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public void ajouterCompetence(Competence competence) {
        if (competenceDAO.create(competence) > 0) {
            competences.add(competence);
        }
    }
    
    public void supprimerCompetence(Competence competence) {
        if (competenceDAO.delete(competence) > 0) {
            competences.remove(competence);
        }
    }
    
    public void modifierCompetence(Competence ancienneCompetence, Competence nouvelleCompetence) throws Exception {
        // Vérifier si la compétence est utilisée comme prérequis
        if (isCompetenceUsedAsPrerequisite(ancienneCompetence)) {
            // Si elle est utilisée comme prérequis, on met à jour directement
            nouvelleCompetence.setIntitule(nouvelleCompetence.getIntitule()); // Ensure intitule is set
            if (competenceDAO.update(nouvelleCompetence) > 0) {
                // Mettre à jour dans la liste observable
                int index = competences.indexOf(ancienneCompetence);
                if (index != -1) {
                    competences.set(index, nouvelleCompetence);
                }
            } else {
                throw new Exception("Échec de la mise à jour de la compétence.");
            }
        } else {
            // Si elle n'est pas utilisée comme prérequis, on peut faire supprimer/recréer
            if (competenceDAO.delete(ancienneCompetence) > 0) {
                competences.remove(ancienneCompetence);
                if (competenceDAO.create(nouvelleCompetence) > 0) {
                    competences.add(nouvelleCompetence);
                } else {
                    // En cas d'échec, restaurer l'ancienne compétence
                    competenceDAO.create(ancienneCompetence);
                    competences.add(ancienneCompetence);
                    throw new Exception("Échec de la création de la nouvelle compétence.");
                }
            } else {
                throw new Exception("Échec de la suppression de l'ancienne compétence.");
            }
        }
    }
    
    private boolean isCompetenceUsedAsPrerequisite(Competence competence) {
        // Vérifier si cette compétence est utilisée comme prérequis par d'autres compétences
        return competences.stream()
                .anyMatch(c -> c.getPrerequis() != null && 
                         c.getPrerequis().contains(competence));
    }
    
    public boolean competenceExists(String intitule) {
        return competenceDAO.findByID(intitule) != null;
    }
    
    public boolean canDeleteCompetence(Competence competence) {
        return !isCompetenceUsedAsPrerequisite(competence);
    }
}