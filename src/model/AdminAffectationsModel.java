package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.*;
import model.data.*;
import model.graphs.DAG;
import model.graphs.Graphe;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminAffectationsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Affectation> affectations = FXCollections.observableArrayList();
    private final DPSDAO dpsDAO;
    private final SecouristeDAO secouristeDAO;
    private final AffectationDAO affectationDAO;
    private final Graphe graphe;
    
    public AdminAffectationsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.dpsDAO = new DPSDAO();
        this.secouristeDAO = new SecouristeDAO();
        this.affectationDAO = new AffectationDAO();
        this.graphe = new Graphe(new DAG());
        initializeData();
    }
    
    private void initializeData() {
        List<Affectation> dbAffectations = affectationDAO.findAllAffectations();
        affectations.addAll(dbAffectations);
    }

    public boolean nettoyerAffectations() {
        int result = affectationDAO.deleteAllAffectations();
        if (result >= 0) {
            affectations.clear(); // Clear the ObservableList to update the table view
            return true;
        }
        return false;
    }
    
    public void createAffectation(DPS dps, LocalDate date, ObservableList<Secouriste> secouristes) {
        String dateStr = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String siteStr = dps.getSite().getNom();
        String secouristesStr = secouristes.stream()
                .map(s -> s.getPrenom() + " " + s.getNom())
                .collect(Collectors.joining("\n"));
                
        Affectation affectation = new Affectation(dateStr, siteStr, secouristesStr);
        affectations.add(affectation);
        
        // Save to database
        for (Secouriste secouriste : secouristes) {
            affectationDAO.createAffectation(secouriste.getId(), dps.getId());
        }
    }
    
    public ObservableList<DPS> getAllDPS() {
        return FXCollections.observableArrayList(dpsDAO.findAll());
    }
    
    public ObservableList<Secouriste> searchCompetentSecouristes(DPS dps, LocalDate date) {
        List<Secouriste> allSecouristes = secouristeDAO.findAll();
        List<DPS> dpsList = List.of(dps);
        
        // Use graphe to find optimal assignment
        Map<DPS, List<Secouriste>> result = graphe.affectationGloutonne(allSecouristes, dpsList);
        
        // Filter by availability
        List<Secouriste> available = result.getOrDefault(dps, List.of()).stream()
                .filter(s -> isSecouristeAvailable(s, date))
                .collect(Collectors.toList());
                
        return FXCollections.observableArrayList(available);
    }
    
    public boolean isSecouristeAvailable(Secouriste secouriste, LocalDate date) {
        return affectationDAO.isSecouristeAvailable(
            secouriste.getId(), 
            date.getDayOfMonth(), 
            date.getMonthValue(), 
            date.getYear()
        );
    }
    
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<Affectation> getAffectations() {
        return affectations;
    }
    
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public Graphe getGraphe() {
        return graphe;
    }
    
    public SecouristeDAO getSecouristeDAO() {
        return secouristeDAO;
    }
    
    public static class Affectation {
        private final StringProperty date = new SimpleStringProperty();
        private final StringProperty sitesOlympiques = new SimpleStringProperty();
        private final StringProperty secouristes = new SimpleStringProperty();
        
        public Affectation(String date, String sitesOlympiques, String secouristes) {
            this.date.set(date);
            this.sitesOlympiques.set(sitesOlympiques);
            this.secouristes.set(secouristes);
        }
        
        public StringProperty dateProperty() { return date; }
        public StringProperty sitesOlympiquesProperty() { return sitesOlympiques; }
        public StringProperty secouristesProperty() { return secouristes; }
        
        public String getDate() { return date.get(); }
        public String getSitesOlympiques() { return sitesOlympiques.get(); }
        public String getSecouristes() { return secouristes.get(); }
        
        public void setDate(String date) { this.date.set(date); }
        public void setSitesOlympiques(String sitesOlympiques) { this.sitesOlympiques.set(sitesOlympiques); }
        public void setSecouristes(String secouristes) { this.secouristes.set(secouristes); }
    }
}