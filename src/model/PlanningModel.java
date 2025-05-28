package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PlanningModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObjectProperty<YearMonth> moisActuel = new SimpleObjectProperty<>(YearMonth.now());
    private final ObservableList<AffectationsModel.Affectation> affectations = FXCollections.observableArrayList();
    private final Map<LocalDate, AffectationsModel.Affectation> affectationsParDate = new HashMap<>();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public PlanningModel() {
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    public PlanningModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeAffectations();
        mapAffectationsParDate();
    }
    
    private void initializeAffectations() {
        // Utilisation des mêmes affectations que dans AffectationsModel
        affectations.add(new AffectationsModel.Affectation("06/02/2030", "Stade de Ski Alpin", "Poste de Secours Avancé"));
        affectations.add(new AffectationsModel.Affectation("10/02/2030", "Piste de Bobsleigh", "Équipe Médicale Urgence"));
        affectations.add(new AffectationsModel.Affectation("15/02/2030", "Village Olympique", "Poste Coordination Médicale"));
        affectations.add(new AffectationsModel.Affectation("20/02/2030", "Site Saut à Ski", "Équipe Réanimation"));
    }
    
    private void mapAffectationsParDate() {
        affectationsParDate.clear();
        for (AffectationsModel.Affectation affectation : affectations) {
            try {
                LocalDate date = LocalDate.parse(affectation.getDate(), dateFormatter);
                affectationsParDate.put(date, affectation);
            } catch (Exception e) {
                System.err.println("Erreur lors du parsing de la date : " + affectation.getDate());
            }
        }
    }
    
    // Méthodes de navigation dans le calendrier
    public void moisPrecedent() {
        moisActuel.set(moisActuel.get().minusMonths(1));
    }
    
    public void moisSuivant() {
        moisActuel.set(moisActuel.get().plusMonths(1));
    }
    
    public void allerAujourdHui() {
        moisActuel.set(YearMonth.now());
    }
    
    // Méthodes pour obtenir les affectations
    public AffectationsModel.Affectation getAffectationPourDate(LocalDate date) {
        return affectationsParDate.get(date);
    }
    
    public boolean hasAffectationPourDate(LocalDate date) {
        return affectationsParDate.containsKey(date);
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObjectProperty<YearMonth> moisActuelProperty() {
        return moisActuel;
    }
    
    public ObservableList<AffectationsModel.Affectation> getAffectations() {
        return affectations;
    }
    
    // Getters pour les valeurs
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    public YearMonth getMoisActuel() {
        return moisActuel.get();
    }
    
    // Setters
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public void setMoisActuel(YearMonth mois) {
        this.moisActuel.set(mois);
    }
    
    // Méthodes utilitaires pour le calendrier
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
        // Retourne le jour de la semaine du premier jour du mois (1 = Lundi, 7 = Dimanche)
        return getPremierJourDuMois().getDayOfWeek().getValue();
    }
}