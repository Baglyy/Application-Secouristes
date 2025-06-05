package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Secouriste;

public class SecouristeDAO extends DAO<Secouriste> {


    public Secouriste findByIdAndNom(long id, String nom) {
        String query = "SELECT * FROM Secouriste WHERE ID = ? AND NOM = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            pst.setString(2, nom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String prenom = rs.getString("PRENOM");
                    String dateDeNaissance = rs.getString("DATENAISSANCE");
                    String email = rs.getString("EMAIL");
                    String tel = rs.getString("TEL");
                    String adresse = rs.getString("ADRESSE");

                    return new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int create(Secouriste secouriste) {
        String query = "INSERT INTO Secouriste(ID, NOM, PRENOM, DATENAISSANCE, EMAIL, TEL, ADRESSE) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); 
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, secouriste.getId());
            pst.setString(2, secouriste.getNom());
            pst.setString(3, secouriste.getPrenom());
            pst.setString(4, secouriste.getDateDeNaissance());
            pst.setString(5, secouriste.getEmail());
            pst.setString(6, secouriste.getTel());
            pst.setString(7, secouriste.getAdresse());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(Secouriste secouriste) {
        String query = "UPDATE Secouriste SET NOM = ?, PRENOM = ?, DATENAISSANCE = ?, EMAIL = ?, TEL = ?, ADRESSE = ? WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, secouriste.getNom());
            pst.setString(2, secouriste.getPrenom());
            pst.setString(3, secouriste.getDateDeNaissance());
            pst.setString(4, secouriste.getEmail());
            pst.setString(5, secouriste.getTel());
            pst.setString(6, secouriste.getAdresse());
            pst.setLong(7, secouriste.getId());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int delete(Secouriste secouriste) {
        String query = "DELETE FROM Secouriste WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, secouriste.getId());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Secouriste> findAll() {
        List<Secouriste> secouristes = new LinkedList<>();
        String query = "SELECT * FROM Secouriste";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                long id = rs.getLong("ID");
                String nom = rs.getString("NOM");
                String prenom = rs.getString("PRENOM");
                String dateDeNaissance = rs.getString("DATENAISSANCE");
                String email = rs.getString("EMAIL");
                String tel = rs.getString("TEL");
                String adresse = rs.getString("ADRESSE");

                secouristes.add(new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return secouristes;
    }

    @Override
    public Secouriste findByID(Object... keys) {
        long id = (long) keys[0];
        String query = "SELECT * FROM Secouriste WHERE ID = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("NOM");
                    String prenom = rs.getString("PRENOM");
                    String dateDeNaissance = rs.getString("DATENAISSANCE");
                    String email = rs.getString("EMAIL");
                    String tel = rs.getString("TEL");
                    String adresse = rs.getString("ADRESSE");

                    return new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
