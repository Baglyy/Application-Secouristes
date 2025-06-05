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

public class AdminSecouristesModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Secouriste> secouristes = FXCollections.observableArrayList();
    private final ObservableList<Competence> competences = FXCollections.observableArrayList();
    private final SecouristeDAO secouristeDAO;
    private final CompetenceDAO competenceDAO;
    
    public AdminSecouristesModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.secouristeDAO = new SecouristeDAO();
        this.competenceDAO = new CompetenceDAO();
        initializeData();
    }

    private void initializeData() {
        secouristes.addAll(secouristeDAO.findAll());
        competences.addAll(competenceDAO.findAll());
    }
    
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<Secouriste> getSecouristes() {
        return secouristes;
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
    
    public void ajouterSecouriste(Secouriste secouriste) {
        if (secouristeDAO.create(secouriste) > 0) {
            secouristes.add(secouriste);
        }
    }
    
    public void supprimerSecouriste(Secouriste secouriste) {
        if (secouristeDAO.delete(secouriste) > 0) {
            secouristes.remove(secouriste);
        }
    }
    
    public void updateSecouristeCompetences(Secouriste secouriste, ObservableList<Competence> newCompetences) {
        // Update competences in the in-memory model
        secouriste.setCompetences(new ArrayList<>(newCompetences));
        
        // Update the secouriste in the database
        // Note: This assumes SecouristeDAO.update handles competences internally.
        // For a proper implementation, SecouristeDAO should have a method to manage
        // the SecouristeCompetence junction table directly.
        secouristeDAO.update(secouriste);
        
        // Refresh the secouriste's data in the in-memory list
        int index = secouristes.indexOf(secouriste);
        if (index >= 0) {
            secouristes.set(index, secouriste);
        }
    }
    
    public boolean secouristeExists(String nom, String prenom) {
        return secouristeDAO.findByIdAndNom(0, nom) != null && 
               secouristeDAO.findByIdAndNom(0, nom).getPrenom().equals(prenom);
    }
}