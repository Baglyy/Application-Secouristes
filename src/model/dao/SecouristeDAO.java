package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util .*;
import model.data.Secouriste;

public class SecouristeDAO extends DAO<Secouriste>{
    public int create(Secouriste secouriste) {
        String query = "INSERT INTO Secouriste(ID , NOM, PRENOM, DATEDENAISSANCE, EMAIL, TEL, ADRESSE) VALUES ('" + secouriste.getId() + "','" 
        + secouriste.getNom() + "','" + secouriste.getPrenom() + "','" + secouriste.getDateDeNaissance() + "','" + secouriste.getEmail() + "','" + secouriste.getTel() + "','" + secouriste.getAdresse() +"')";
        try (Connection con = getConnection (); 
        Statement st = con.createStatement ()) {
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return -1;
        }
    }

    public int update(Secouriste secouriste) {
        String query = "UPDATE SECOURISTE SET NOM='" + secouriste.getNom() + "', PRENOM='" + secouriste.getPrenom() +  "', DATEDENAISSANCE='" 
        + secouriste.getDateDeNaissance() +  "', EMAIL='" + secouriste.getEmail() +  "', TEL='" + secouriste.getTel() 
        +  "', ADRESSE='" + secouriste.getAdresse() + "' WHERE ID='" + secouriste.getId() + "'";
        try (Connection con = getConnection ();
        Statement st = con.createStatement ()) {
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return -1;
        }
    }

    public int delete(Secouriste secouriste) {
        String query = "DELETE FROM SECOURISTE WHERE ID='" + secouriste.getId() + "'";
        try (Connection con = getConnection ();
        Statement st = con.createStatement ()) {
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return -1;
        }
    }

    public List <Secouriste > findAll () {
        List <Secouriste > secouristes = new LinkedList <>();
        try (Connection con = getConnection ();
        Statement st = con.createStatement ();
        ResultSet rs = st.executeQuery("SELECT * FROM SECOURISTE")) {
            while (rs.next()) {
                String id = rs.getLong("ID");
                String nom = rs.getString("NOM");
                String prenom = rs.getString("PRENOM");
                String dateDeNaissance = rs.getString("DATEDENAISSANCE");
                String email = rs.getString("EMAIL");
                String tel = rs.getString("TEL");
                String adresse = rs.getString("ADRESSE");
                secouristes.add(new Secouriste(id , nom, prenom, dateDeNaissance, email, tel, adresse));
            }
        } catch (SQLException ex) {
            ex.printStackTrace ();
        }
        return secouristes;
    }

    public Secouriste findId(String id) {
        try (Connection con = getConnection ();
        PreparedStatement st = con.prepareStatement("SELECT * FROM SECOURISTE WHERE ID= ?")) {
            st.setString(1, id);
            try(ResultSet rs = st.executeQuery ()){
                if (rs.next()) {
                    String l = rs.getString("ID");
                    return new Secouriste(l);
                }
            } catch (SQLException ex) { ex.printStackTrace (); }
        } catch (SQLException ex) { 
            ex.printStackTrace (); 
        }
        return null;
    }
}