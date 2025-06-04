package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Site;

public class SiteDAO extends DAO<Site> {

    @Override
    public int create(Site site) {
        String query = "INSERT INTO Site(CODE, NOM, LONGITUDE, LATITUDE) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, site.getCode());
            pst.setString(2, site.getNom());
            pst.setFloat(3, site.getLongitude());
            pst.setFloat(4, site.getLatitude());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public int update(Site site) {
        String query = "UPDATE Site SET NOM = ?, LONGITUDE = ?, LATITUDE = ? WHERE CODE = ?";
        try (Connection con = getConnection ();
            PreparedStatement pst = con.prepareStatement (query)) {
            
            pst.setString(1, site.getNom());
            pst.setFloat(2, site.getLongitude());
            pst.setFloat(3, site.getLatitude());
            pst.setString(4, site.getCode());

            return pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace ();
            return -1;
        } 
    }

    @Override
    public int delete(Site site) {
        String query = "DELETE FROM Site WHERE CODE = ?";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, site.getCode());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Site> findAll() {
        List<Site> sites = new LinkedList<>();
        String query = "SELECT * FROM Site";

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String code = rs.getString("CODE");
                String nom = rs.getString("NOM");
                float longitude = rs.getFloat("LONGITUDE");
                float latitude = rs.getFloat("LATITUDE");
                sites.add(new Site(code, nom, longitude, latitude));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return sites;
    }

    @Override
    public Site findByID(Object... keys) {

        String codeString = (String) keys[0];
        
        String query = "SELECT * FROM Site WHERE CODE = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, codeString);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String code = rs.getString("CODE");
                    String nom = rs.getString("NOM");
                    float longitude = rs.getFloat("LONGITUDE");
                    float latitude = rs.getFloat("LATITUDE");
                    return new Site(code, nom, longitude, latitude);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
