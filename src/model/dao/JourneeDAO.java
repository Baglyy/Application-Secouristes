package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Journee;

/**
 * DAO pour la gestion des entités {@link Journee}.
 * Permet de créer, mettre à jour, supprimer et rechercher des journées en base.
 */
public class JourneeDAO extends DAO<Journee> {

    /**
     * Crée une nouvelle journée dans la base de données.
     *
     * @param journee la journée à insérer
     * @return 1 si l’insertion a réussi, -1 en cas d’erreur
     */
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

    /**
     * Met à jour une journée existante dans la base.
     *
     * @param journee la journée avec les nouvelles valeurs
     * @return nombre de lignes modifiées ou -1 en cas d'erreur
     */
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

    /**
     * Supprime une journée de la base de données.
     *
     * @param journee la journée à supprimer
     * @return 1 si la suppression a réussi, -1 sinon
     */
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

    /**
     * Récupère toutes les journées de la base.
     *
     * @return une liste de toutes les journées trouvées
     */
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

    /**
     * Recherche une journée par identifiant (jour, mois, année).
     *
     * @param keys les 3 entiers : jour, mois, année
     * @return la journée correspondante ou null si introuvable
     */
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
