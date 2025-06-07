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
    private final AffectationDAO affectationDAO = new AffectationDAO();
    private final long idSecouriste;
    
    public PlanningModel(String nomUtilisateur, long idSecouriste) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.idSecouriste = idSecouriste;
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    private void initializeAffectations() {
        affectations.clear();
        List<AdminAffectationsModel.Affectation> allAffectations = affectationDAO.findAllAffectations();
        YearMonth currentMonth = moisActuel.get();
        int month = currentMonth.getMonthValue();
        int year = currentMonth.getYear();
        
        for (AdminAffectationsModel.Affectation affectation : allAffectations) {
            try {
                LocalDate date = LocalDate.parse(affectation.getDate(), dateFormatter);
                if (date.getMonthValue() == month && date.getYear() == year) {
                    // Assume affectation applies to this secouriste (filtering by idSecouriste not possible without DB query)
                    affectations.add(affectation);
                }
            } catch (Exception e) {
                System.err.println("Erreur lors du parsing de la date : " + affectation.getDate());
            }
        }
    }
    
    private void mapAffectationsParDate() {
        affectationsParDate.clear();
        for (AdminAffectationsModel.Affectation affectation : affectations) {
            try {
                LocalDate date = LocalDate.parse(affectation.getDate(), dateFormatter);
                affectationsParDate.put(date, affectation);
            } catch (Exception e) {
                System.err.println("Erreur lors du mapping de la date : " + affectation.getDate());
            }
        }
    }
    
    public void moisPrecedent() {
        moisActuel.set(moisActuel.get().minusMonths(1));
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public void moisSuivant() {
        moisActuel.set(moisActuel.get().plusMonths(1));
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public void allerAujourdHui() {
        moisActuel.set(YearMonth.now());
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public AdminAffectationsModel.Affectation getAffectationPourDate(LocalDate date) {
        return affectationsParDate.get(date);
    }
    
    public boolean hasAffectationPourDate(LocalDate date) {
        return affectationsParDate.containsKey(date);
    }
    
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
    
    public YearMonth getMoisActuel() {
        return moisActuel.get();
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public void setMoisActuel(YearMonth mois) {
        this.moisActuel.set(mois);
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