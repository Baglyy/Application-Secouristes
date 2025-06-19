package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Site;

/**
 * DAO (Data Access Object) pour gérer les opérations CRUD sur les sites.
 */
public class SiteDAO extends DAO<Site> {

    /**
     * Insère un nouveau site dans la base de données.
     * 
     * @param site le site à insérer
     * @return le nombre de lignes affectées, ou -1 en cas d'erreur
     */
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

    /**
     * Met à jour les informations d'un site existant.
     * 
     * @param site le site à mettre à jour
     * @return le nombre de lignes affectées, ou -1 en cas d'erreur
     */
    @Override
    public int update(Site site) {
        String query = "UPDATE Site SET NOM = ?, LONGITUDE = ?, LATITUDE = ? WHERE CODE = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, site.getNom());
            pst.setFloat(2, site.getLongitude());
            pst.setFloat(3, site.getLatitude());
            pst.setString(4, site.getCode());

            return pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        } 
    }

    /**
     * Supprime un site de la base de données.
     * 
     * @param site le site à supprimer
     * @return le nombre de lignes supprimées, ou -1 en cas d'erreur
     */
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

    /**
     * Récupère la liste de tous les sites présents en base.
     * 
     * @return une liste de sites
     */
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

    /**
     * Recherche un site à partir de son code.
     * 
     * @param keys les clés d'identification, ici un seul String représentant le code
     * @return le site trouvé ou null si aucun résultat
     */
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
