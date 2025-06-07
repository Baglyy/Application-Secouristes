package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.AffectationDAO;
import model.AdminAffectationsModel;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlanningModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObjectProperty<YearMonth> moisActuel = new SimpleObjectProperty<>(YearMonth.now());
    private final ObservableList<AdminAffectationsModel.Affectation> affectations = FXCollections.observableArrayList();
    private final Map<LocalDate, AdminAffectationsModel.Affectation> affectationsParDate = new HashMap<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final AffectationDAO affectationDAO;
    private final long idSecouriste;
    
    public PlanningModel(String nomUtilisateur, long idSecouriste) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.idSecouriste = idSecouriste;
        this.affectationDAO = new AffectationDAO();
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    private void initializeAffectations() {
        // IMPORTANT: Vider complètement les collections avant de recharger
        affectations.clear();
        affectationsParDate.clear(); // Ajout de cette ligne cruciale
        
        YearMonth currentMonth = moisActuel.get();
        
        System.out.println("=== DEBUG initializeAffectations ===");
        System.out.println("Chargement des affectations pour idSecouriste=" + idSecouriste + 
                          ", mois=" + currentMonth.getMonthValue() + 
                          ", année=" + currentMonth.getYear());
        
        // Utiliser le DAO au lieu de faire la requête directement
        List<AdminAffectationsModel.Affectation> affectationsDuMois = 
            affectationDAO.findAffectationsForSecoursiteAndMonth(
                idSecouriste, 
                currentMonth.getMonthValue(), 
                currentMonth.getYear()
            );
        
        affectations.addAll(affectationsDuMois);
        System.out.println("Chargé " + affectationsDuMois.size() + " affectations");
        
        for (AdminAffectationsModel.Affectation aff : affectationsDuMois) {
            System.out.println("  - Date: " + aff.getDate() + ", Site: " + aff.getSitesOlympiques());
        }
        System.out.println("=== FIN DEBUG initializeAffectations ===");
    }
    
    private void mapAffectationsParDate() {
        // Le mapping est maintenant vidé dans initializeAffectations()
        // On ne fait plus affectationsParDate.clear() ici pour éviter la duplication
        System.out.println("=== DEBUG mapAffectationsParDate ===");
        System.out.println("Nombre d'affectations à mapper: " + affectations.size());
        System.out.println("État du mapping avant traitement: " + affectationsParDate.size() + " entrées");
        
        // Différents formats de date possibles
        DateTimeFormatter[] formatters = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("dd/MM/yy"),
            DateTimeFormatter.ofPattern("d/M/yy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        };
        
        for (AdminAffectationsModel.Affectation affectation : affectations) {
            String dateString = affectation.getDate();
            System.out.println("Traitement affectation - Date string: '" + dateString + "'");
            
            LocalDate date = null;
            for (DateTimeFormatter formatter : formatters) {
                try {
                    date = LocalDate.parse(dateString, formatter);
                    System.out.println("  -> Date parsée avec succès: " + date + " (format: " + formatter.toString() + ")");
                    break;
                } catch (Exception e) {
                    // Continuer avec le prochain format
                }
            }
            
            if (date != null) {
                affectationsParDate.put(date, affectation);
                System.out.println("  -> Ajoutée au mapping: " + date + " - Site: " + affectation.getSitesOlympiques());
            } else {
                System.err.println("  -> ERREUR: Impossible de parser la date: " + dateString);
            }
        }
        
        System.out.println("Mapping terminé. Nombre de dates mappées: " + affectationsParDate.size());
        System.out.println("Dates mappées:");
        for (LocalDate date : affectationsParDate.keySet()) {
            System.out.println("  - " + date + " : " + affectationsParDate.get(date).getSitesOlympiques());
        }
        System.out.println("=== FIN DEBUG mapAffectationsParDate ===");
    }
    
    public void moisPrecedent() {
        System.out.println("=== Changement vers mois précédent ===");
        YearMonth ancienMois = moisActuel.get();
        moisActuel.set(moisActuel.get().minusMonths(1));
        YearMonth nouveauMois = moisActuel.get();
        System.out.println("Changement de mois: " + ancienMois + " -> " + nouveauMois);
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public void moisSuivant() {
        System.out.println("=== Changement vers mois suivant ===");
        YearMonth ancienMois = moisActuel.get();
        moisActuel.set(moisActuel.get().plusMonths(1));
        YearMonth nouveauMois = moisActuel.get();
        System.out.println("Changement de mois: " + ancienMois + " -> " + nouveauMois);
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public void allerAujourdHui() {
        System.out.println("=== Retour au mois actuel ===");
        YearMonth ancienMois = moisActuel.get();
        moisActuel.set(YearMonth.now());
        YearMonth nouveauMois = moisActuel.get();
        System.out.println("Changement de mois: " + ancienMois + " -> " + nouveauMois);
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public AdminAffectationsModel.Affectation getAffectationPourDate(LocalDate date) {
        return affectationsParDate.get(date);
    }
    
    public boolean hasAffectationPourDate(LocalDate date) {
        boolean result = affectationsParDate.containsKey(date);
        System.out.println("hasAffectationPourDate(" + date + ") = " + result);
        if (!result && !affectationsParDate.isEmpty()) {
            System.out.println("  Dates disponibles dans le mapping:");
            for (LocalDate mappedDate : affectationsParDate.keySet()) {
                System.out.println("    - " + mappedDate);
            }
        }
        return result;
    }
    
    // Getters et Setters
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObjectProperty<YearMonth> moisActuelProperty() {
        return moisActuel;
    }
    
    public ObservableList<AdminAffectationsModel.Affectation> getAffectations() {
        return affectations;
    }
    
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public YearMonth getMoisActuel() {
        return moisActuel.get();
    }
    
    public void setMoisActuel(YearMonth mois) {
        YearMonth ancienMois = this.moisActuel.get();
        this.moisActuel.set(mois);
        System.out.println("setMoisActuel: " + ancienMois + " -> " + mois);
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public String getMoisAnneeString() {
        return moisActuel.get().format(DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale.FRENCH));
    }
    
    public LocalDate getPremierJourDuMois() {
        return moisActuel.get().atDay(1);
    }
    
    public int getNombreJoursDansMois() {
        return moisActuel.get().lengthOfMonth();
    }
    
    public int getPremierJourSemaine() {
        return getPremierJourDuMois().getDayOfWeek().getValue();
    }
}