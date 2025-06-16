package model.dao;

import model.data.DPS;
import model.data.Secouriste;
import model.data.Site;
import model.AdminAffectationsModel;

import java.sql.*;
import java.util.*;

public class AffectationDAO extends DAO<Object> {
    
    private DPSDAO dpsDAO = new DPSDAO();
    private SecouristeDAO secouristeDAO = new SecouristeDAO();
    private JourneeDAO journeeDAO = new JourneeDAO();
    private SiteDAO siteDAO = new SiteDAO();

    public int deleteAllAffectations() {
        String query = "DELETE FROM Affectation";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            System.out.println("Suppression de toutes les affectations existantes...");
            int rowsAffected = pst.executeUpdate();
            System.out.println(rowsAffected + " affectation(s) supprimée(s).");
            return rowsAffected;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de toutes les affectations: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public List<AdminAffectationsModel.Affectation> findAffectationsForSecoursiteAndMonth(long idSecouriste, int month, int year) {
        List<AdminAffectationsModel.Affectation> affectations = new ArrayList<>();
        
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
                while (rs.next()) {
                    int jour = rs.getInt("jour");
                    int mois = rs.getInt("mois");
                    int annee = rs.getInt("annee");
                    String site = rs.getString("site");
                    String secouristes = rs.getString("secouristes");
                    
                    String dateStr = String.format("%02d/%02d/%d", jour, mois, annee);
                    affectations.add(new AdminAffectationsModel.Affectation(
                        dateStr, 
                        site, 
                        secouristes.replace(",", "\n")
                    ));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des affectations : " + e.getMessage());
            e.printStackTrace();
        }
        
        return affectations;
    }

    public int createAffectation(long idSecouriste, long idDps) {
        String query = "INSERT INTO Affectation(idSecouriste, idDps) VALUES (?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setLong(1, idSecouriste);
            pst.setLong(2, idDps);
            return pst.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int create(Object obj) {
        throw new UnsupportedOperationException("Use createAffectation instead");
    }

    @Override
    public int update(Object obj) {
        throw new UnsupportedOperationException("Updates not supported for affectations");
    }

    @Override
    public int delete(Object o) {
        throw new UnsupportedOperationException("Use specific delete method if needed");
    }

    public int deleteAffectation(long idSecouriste, long idDps) {
        String query = "DELETE FROM Affectation WHERE idSecouriste = ? AND idDps = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            pstmt.setLong(1, idSecouriste);
            pstmt.setLong(2, idDps);
            return pstmt.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Object> findAll() {
        List<Object> affectations = new ArrayList<>();
        affectations.addAll(findAllAffectations());
        return affectations;
    }

    public List<AdminAffectationsModel.Affectation> findAllAffectations() {
        List<AdminAffectationsModel.Affectation> affectations = new ArrayList<>();
        String query = "SELECT a.idDps, d.jour, d.mois, d.annee, d.leSite, GROUP_CONCAT(s.nom || ' ' || s.prenom) as secouristes " +
                      "FROM Affectation a " +
                      "JOIN DPS d ON a.idDps = d.id " +
                      "JOIN Secouriste s ON a.idSecouriste = s.id " +
                      "JOIN Site si ON d.leSite = si.code " +
                      "GROUP BY a.idDps";
        
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            while (rs.next()) {
                long idDps = rs.getLong("idDps");
                int jour = rs.getInt("jour");
                int mois = rs.getInt("mois");
                int annee = rs.getInt("annee");
                String siteCode = rs.getString("leSite");
                String secouristes = rs.getString("secouristes");
                
                Site site = siteDAO.findByID(siteCode);
                String dateStr = String.format("%02d/%02d/%d", jour, mois, annee);
                String siteNom = site != null ? site.getNom() : siteCode;
                
                affectations.add(new AdminAffectationsModel.Affectation(
                    dateStr,
                    siteNom,
                    secouristes.replace(",", "\n")
                ));
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return affectations;
    }

    @Override
    public Object findByID(Object... keys) {
        throw new UnsupportedOperationException("Use specific find methods if needed");
    }

    public boolean isSecouristeAvailable(long id, int jour, int mois, int annee) {
        String query = "SELECT * FROM Disponibilite WHERE idSecouriste = ? AND jour = ? AND mois = ? AND annee = ?";
        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            
            pstmt.setLong(1, id);
            pstmt.setInt(2, jour);
            pstmt.setInt(3, mois);
            pstmt.setInt(4, annee);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
