package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Journee;

public class JourneeDAO extends DAO<Journee> {

    @Override
    public int create(Journee journee) {
        String query = "INSERT INTO Journee(JOUR, MOIS, ANNEE) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, journee.getJour());
            pst.setInt(2, journee.getMois());
            pst.setInt(3, journee.getAnnee());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(Journee journee) {
        String query = "UPDATE Journee SET JOUR = ?, MOIS = ?, ANNEE = ? WHERE JOUR = ? AND MOIS = ? AND ANNEE = ?";
        try (Connection con = getConnection ();
            Statement st = con.createStatement ()) {
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return -1;
        }    
    }

    @Override
    public int delete(Journee journee) {
        String query = "DELETE FROM Journee WHERE JOUR = ? AND MOIS = ? AND ANNEE = ?";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, journee.getJour()); 
            pst.setInt(2, journee.getMois());
            pst.setInt(3, journee.getAnnee());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Journee> findAll() {
        List<Journee> journees = new LinkedList<>();
        String query = "SELECT * FROM Journee";

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                int jour = rs.getInt("JOUR");
                int mois = rs.getInt("MOIS");
                int annee = rs.getInt("ANNEE");

                journees.add(new Journee(jour, mois, annee));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return journees;
    }

    @Override
    public Journee findByID(Object... keys) {
        
        int jour = (int) keys[0];
        int mois = (int) keys[1];
        int annee = (int) keys[2];

        
        String query = "SELECT * FROM Journee WHERE JOUR = ? AND MOIS = ? AND ANNEE = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setInt(1, jour); 
            pst.setInt(2, mois);
            pst.setInt(3, annee);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Journee(jour, mois, annee);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
