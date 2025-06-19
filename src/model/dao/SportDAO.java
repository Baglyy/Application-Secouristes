package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Sport;

/**
 * DAO (Data Access Object) pour gérer les opérations CRUD sur les entités Sport.
 */
public class SportDAO extends DAO<Sport> {

    /**
     * Crée un nouveau sport dans la base de données.
     *
     * @param sport le sport à créer
     * @return le nombre de lignes insérées, ou -1 en cas d'erreur
     */
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

    /**
     * Met à jour un sport existant dans la base de données.
     *
     * ⚠️ **ERREUR** : les paramètres sont inversés dans la requête `UPDATE`.  
     * `pst.setString(1, sport.getNom())` et `pst.setString(2, sport.getCode())` devraient être utilisés à la place.
     *
     * @param sport le sport à mettre à jour
     * @return le nombre de lignes mises à jour, ou -1 en cas d'erreur
     */
    @Override
    public int update(Sport sport) {
        String query = "UPDATE Sport SET NOM = ? WHERE CODE = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, sport.getNom());   // Correct
            pst.setString(2, sport.getCode());  // Correct

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Supprime un sport de la base de données.
     *
     * @param sport le sport à supprimer
     * @return le nombre de lignes supprimées, ou -1 en cas d'erreur
     */
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

    /**
     * Récupère tous les sports présents dans la base de données.
     *
     * @return une liste de tous les sports
     */
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

    /**
     * Recherche un sport en fonction de son code.
     *
     * @param keys un seul paramètre attendu : le code (String)
     * @return le sport correspondant ou null si non trouvé
     */
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
