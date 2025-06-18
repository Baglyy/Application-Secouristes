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
    
    public void modifierCompetence(Competence ancienneCompetence, Competence nouvelleCompetence) throws Exception {
        String oldIntitule = ancienneCompetence.getIntitule();
        nouvelleCompetence.setIntitule(nouvelleCompetence.getIntitule().trim());
        
        // Update dependent tables and the competence itself in one transaction
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
    
    private boolean isCompetenceUsedAsPrerequisite(Competence competence) {
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