package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Sport;

public class SportDAO extends DAO<Sport> {

    @Override
    public int create(Sport sport) {
        String query = "INSERT INTO Sport(CODE, NOM) VALUES (?, ?)";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, sport.getCode());
            pst.setString(2, sport.getNom());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(Sport sport) {
        String query = "UPDATE Sport SET NOM = ? WHERE CODE = ?";
        try (Connection con = getConnection ();
            PreparedStatement pst = con.prepareStatement (query)) {
            pst.setString(1, sport.getCode());
            pst.setString(2, sport.getNom());

            return pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return -1;
        } 
    }

    @Override
    public int delete(Sport sport) {
        String query = "DELETE FROM Sport WHERE CODE = ?";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, sport.getCode());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Sport> findAll() {
        List<Sport> sports = new LinkedList<>();
        String query = "SELECT * FROM Sport";

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String code = rs.getString("CODE");
                String nom = rs.getString("NOM");
                sports.add(new Sport(code, nom));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sports;
    }

    @Override
    public Sport findByID(Object... keys) {

        String codeString = (String) keys[0];
        
        String query = "SELECT * FROM Sport WHERE CODE = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, codeString);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String code = rs.getString("CODE");
                    String nom = rs.getString("NOM");
                    return new Sport(code, nom);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
