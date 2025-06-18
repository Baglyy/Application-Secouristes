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
    
    public void updateCompetence(Competence oldCompetence, Competence newCompetence) {
        // Supprimer l'ancienne compétence
        if (competenceDAO.delete(oldCompetence) > 0) {
            competences.remove(oldCompetence);
            // Créer la nouvelle compétence
            if (competenceDAO.create(newCompetence) > 0) {
                competences.add(newCompetence);
            } else {
                // En cas d'échec de la création, tenter de recréer l'ancienne compétence
                competenceDAO.create(oldCompetence);
                competences.add(oldCompetence);
                throw new RuntimeException("Échec de la création de la nouvelle compétence.");
            }
        } else {
            throw new RuntimeException("Échec de la suppression de l'ancienne compétence.");
        }
    }
    
    public boolean competenceExists(String intitule) {
        return competenceDAO.findByID(intitule) != null;
    }
}