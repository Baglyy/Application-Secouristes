package model.dao;

import java.sql.*;
import java.util.*;

public class DisponibiliteDAO extends DAO<Void> {
    
    private SecouristeDAO secouristeDAO = new SecouristeDAO();

    public boolean createDisponibilite(long idSecouriste, int jour, int mois, int annee) {
        String sql = 
            "INSERT INTO Disponibilite (idSecouriste, jour, mois, annee) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, jour);
            pstmt.setInt(3, mois);
            pstmt.setInt(4, annee);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteDisponibilite(long idSecouriste, int jour, int mois, int annee) {
        String sql = 
            "DELETE FROM Disponibilite WHERE idSecouriste = ? AND jour = ? AND mois = ? AND annee = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, jour);
            pstmt.setInt(3, mois);
            pstmt.setInt(4, annee);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> findDisponibilitesBySecouristeAndMonth(long idSecouriste, int mois, int annee) {
        List<Map<String, Object>> dispos = new ArrayList<>();
        String sql = 
            "SELECT jour, mois, annee FROM Disponibilite WHERE idSecouriste = ? AND mois = ? AND annee = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, mois);
            pstmt.setInt(3, annee);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dispo = new HashMap<>();
                    dispo.put("jour", rs.getInt("jour"));
                    dispo.put("mois", rs.getInt("mois"));
                    dispo.put("annee", rs.getInt("annee"));
                    dispos.add(dispo);
                }
            }
            return dispos;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int create(Void v) {
        throw new UnsupportedOperationException("Use createDisponibilite instead");
    }

    @Override
    public int update(Void v) {
        throw new UnsupportedOperationException("Updates not supported");
    }

    @Override
    public int delete(Void v) {
        throw new UnsupportedOperationException("Use specific deleteDisponibilite method");
    }

    @Override
    public Void findByID(Object... keys) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Void> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}