package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.CompetenceDAO;
import model.data.Competence;

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
    
    public boolean competenceExists(String intitule) {
        return competenceDAO.findByID(intitule) != null;
    }
}