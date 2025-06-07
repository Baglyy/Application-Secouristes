package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.DisponibiliteDAO;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class DisponibilitesModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final StringProperty moisSelectionne = new SimpleStringProperty("");
    private final ObservableList<CreneauDisponibilite> creneaux = FXCollections.observableArrayList();
    private final DisponibiliteDAO disponibiliteDAO = new DisponibiliteDAO();
    private final long idSecouriste;
    private int currentMonth;
    private int currentYear;
    
    public DisponibilitesModel(String nomUtilisateur, long idSecouriste) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.idSecouriste = idSecouriste;
        LocalDate now = LocalDate.now();
        this.currentMonth = now.getMonthValue();
        this.currentYear = now.getYear();
        updateMoisLabel();
        initializeCreneaux();
    }
    
    private void updateMoisLabel() {
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        String moisNom = date.getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH);
        moisSelectionne.set(moisNom + " " + currentYear);
    }
    
    private void initializeCreneaux() {
        creneaux.clear();
        LocalDate date = LocalDate.of(currentYear, currentMonth, 1);
        int daysInMonth = date.lengthOfMonth();
        
        List<Map<String, Object>> dbDispos = disponibiliteDAO.findDisponibilitesBySecouristeAndMonth(
            idSecouriste, currentMonth, currentYear
        );
        
        Set<Integer> dispoDays = new HashSet<>();
        for (Map<String, Object> dispo : dbDispos) {
            dispoDays.add((Integer) dispo.get("jour"));
        }
        
        for (int day = 1; day <= daysInMonth; day++) {
            boolean disponible = dispoDays.contains(day);
            creneaux.add(new CreneauDisponibilite(day, disponible));
        }
    }
    
    public void toggleDisponibilite(int day) {
        CreneauDisponibilite creneau = creneaux.stream()
            .filter(c -> c.getDay() == day)
            .findFirst()
            .orElse(null);
        
        if (creneau != null && idSecouriste != -1) {
            boolean newState = !creneau.isDisponible();
            creneau.setDisponible(newState);
            
            try {
                if (newState) {
                    boolean success = disponibiliteDAO.createDisponibilite(
                        idSecouriste, day, currentMonth, currentYear
                    );
                    if (!success) {
                        System.err.println("Échec de la création de disponibilité pour idSecouriste=" + idSecouriste +
                                           ", jour=" + day + ", mois=" + currentMonth + ", année=" + currentYear);
                    }
                } else {
                    boolean success = disponibiliteDAO.deleteDisponibilite(
                        idSecouriste, day, currentMonth, currentYear
                    );
                    if (!success) {
                        System.err.println("Échec de la suppression de disponibilité pour idSecouriste=" + idSecouriste +
                                           ", jour=" + day + ", mois=" + currentMonth + ", année=" + currentYear);
                    }
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour de disponibilité : " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Toggle ignoré : creneau=" + creneau + ", idSecouriste=" + idSecouriste);
        }
    }
    
    public boolean isDisponible(int day) {
        return creneaux.stream()
            .filter(c -> c.getDay() == day)
            .findFirst()
            .map(CreneauDisponibilite::isDisponible)
            .orElse(false);
    }
    
    public void previousMonth() {
        currentMonth--;
        if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        updateMoisLabel();
        initializeCreneaux();
    }
    
    public void nextMonth() {
        currentMonth++;
        if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        updateMoisLabel();
        initializeCreneaux();
    }
    
    public int getCurrentMonth() {
        return currentMonth;
    }
    
    public int getCurrentYear() {
        return currentYear;
    }
    
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public StringProperty moisSelectionneProperty() {
        return moisSelectionne;
    }
    
    public ObservableList<CreneauDisponibilite> getCreneaux() {
        return creneaux;
    }
    
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public static class CreneauDisponibilite {
        private final int day;
        private boolean disponible;
        
        public CreneauDisponibilite(int day, boolean disponible) {
            this.day = day;
            this.disponible = disponible;
        }
        
        public int getDay() { return day; }
        public boolean isDisponible() { return disponible; }
        public void setDisponible(boolean disponible) { this.disponible = disponible; }
    }
}