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
import java.sql.*;
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
        YearMonth currentMonth = moisActuel.get();
        int month = currentMonth.getMonthValue();
        int year = currentMonth.getYear();
        
        String query = 
            "SELECT d.jour, d.mois, d.annee, si.nom AS site, GROUP_CONCAT(s.nom || ' ' || s.prenom) AS secouristes " +
            "FROM Affectation a " +
            "JOIN DPS d ON a.idDps = d.id " +
            "JOIN Secouriste s ON a.idSecouriste = s.id " +
            "JOIN Site si ON d.leSite = si.code " +
            "WHERE a.idSecouriste = ? AND d.mois = ? AND d.annee = ? " +
            "GROUP BY d.jour, d.mois, d.annee, si.nom";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, month);
            pstmt.setInt(3, year);
            try (ResultSet rs = pstmt.executeQuery()) {
                int count = 0;
                while (rs.next()) {
                    int jour = rs.getInt("jour");
                    int mois = rs.getInt("mois");
                    int annee = rs.getInt("annee");
                    String site = rs.getString("site");
                    String secouristes = rs.getString("secouristes");
                    String dateStr = String.format("%02d/%02d/%d", jour, mois, annee);
                    affectations.add(new AdminAffectationsModel.Affectation(dateStr, site, secouristes.replace(",", "\n")));
                    count++;
                }
                System.out.println("Chargé " + count + " affectations pour idSecouriste=" + idSecouriste +
                                   ", mois=" + month + ", année=" + year);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des affectations : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Connection getConnection() throws SQLException {
        // Placeholder: Adjust to match your database configuration
        String url = "jdbc:mysql://localhost:3306/secuoptix?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = ""; // Update with actual credentials
        return DriverManager.getConnection(url, user, password);
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
    
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    public YearMonth getMoisActuel() {
        return moisActuel.get();
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