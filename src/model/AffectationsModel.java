package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.AffectationDAO;
import model.dao.SecouristeDAO;
import model.data.Secouriste;
import model.AdminAffectationsModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AffectationsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Affectation> affectations = FXCollections.observableArrayList();
    private final AffectationDAO affectationDAO = new AffectationDAO();
    private final SecouristeDAO secouristeDAO = new SecouristeDAO();
    private long idSecouriste = -1;
    
    public AffectationsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeSecouristeId();
        initializeAffectations();
    }
    
    private void initializeSecouristeId() {
        Secouriste secouriste = secouristeDAO.findByNom(nomUtilisateur.get());
        if (secouriste != null) {
            this.idSecouriste = secouriste.getId();
        } else {
            System.err.println("Secouriste not found for nomUtilisateur: " + nomUtilisateur.get());
        }
    }
    
    private void initializeAffectations() {
        if (idSecouriste == -1) {
            return;
        }
        affectations.clear();
        
        // Fetch affectations for a reasonable range (e.g., 2024–2030)
        List<AdminAffectationsModel.Affectation> allAffectations = new ArrayList<>();
        for (int year = 2024; year <= 2030; year++) {
            for (int month = 1; month <= 12; month++) {
                List<AdminAffectationsModel.Affectation> monthAffectations = 
                    affectationDAO.findAffectationsForSecoursiteAndMonth(idSecouriste, month, year);
                allAffectations.addAll(monthAffectations);
            }
        }
        
        // Sort by date (earliest first)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        allAffectations.sort((a1, a2) -> {
            try {
                LocalDate date1 = LocalDate.parse(a1.getDate(), formatter);
                LocalDate date2 = LocalDate.parse(a2.getDate(), formatter);
                return date1.compareTo(date2);
            } catch (Exception e) {
                return 0; // Fallback if parsing fails
            }
        });
        
        // Map to model.Affectation
        for (AdminAffectationsModel.Affectation dbAffectation : allAffectations) {
            affectations.add(new Affectation(
                dbAffectation.getDate(),
                dbAffectation.getSitesOlympiques(),
                dbAffectation.getSecouristes()
            ));
        }
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
        initializeSecouristeId();
        initializeAffectations();
    }
    
    public static class Affectation {
        private final String date;
        private final String siteOlympique;
        private final String secouristes;
        
        public Affectation(String date, String siteOlympique, String secouristes) {
            this.date = date;
            this.siteOlympique = siteOlympique;
            this.secouristes = secouristes;
        }
        
        public String getDate() { return date; }
        public String getSiteOlympique() { return siteOlympique; }
        public String getSecouristes() { return secouristes; }
    }
}