package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Competence;

public class CompetenceDAO extends DAO<Competence> {

    @Override
    public int create(Competence competence) {
        String query = "INSERT INTO Competence(INTITULE) VALUES (?)";
        try (Connection con = getConnection(); 
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, competence.getIntitule());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(Competence competence, String ancienIntitule) {
        String query = "UPDATE Competence SET INTITULE = ? WHERE INTITULE = ?";
        try (Connection con = getConnection(); 
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, competence.getIntitule()); 
            pst.setString(2, ancienIntitule);           
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int delete(Competence competence) {
        String query = "DELETE FROM Competence WHERE INTITULE = ?";
        try (Connection con = getConnection(); 
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, competence.getIntitule());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> competences = new LinkedList<>();
        String query = "SELECT * FROM Competence";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String intitule = rs.getString("INTITULE");
                competences.add(new Competence(intitule));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return competences;
    }

    public Competence findByID(String intitule) {
        String query = "SELECT * FROM Competence WHERE INTITULE = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, intitule);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Competence(rs.getString("INTITULE"));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
