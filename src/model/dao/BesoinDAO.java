package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Besoin;
import model.data.DPS;
import model.data.Competence;

/**
 * DAO pour gérer les opérations CRUD liées à la table Besoin en base de données.
 * Un besoin est caractérisé par un DPS, une compétence, et un nombre de secouristes requis.
 */
public class BesoinDAO extends DAO<Besoin> {

    private DPSDAO dpsDAO;
    private CompetenceDAO competenceDAO;

    /**
     * Constructeur par défaut initialisant les DAO nécessaires.
     */
    public BesoinDAO() {
        this.dpsDAO = new DPSDAO();
        this.competenceDAO = new CompetenceDAO();
    }

    /**
     * Crée un besoin en base de données.
     *
     * @param besoin le besoin à insérer
     * @return 1 si succès, -1 sinon
     */
    @Override
    public int create(Besoin besoin) {
        String query = "INSERT INTO Besoin(IDDPS, INTITULECOMP, NOMBRE) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, besoin.getDps().getId());
            pst.setString(2, besoin.getCompetence().getIntitule());
            pst.setInt(3, besoin.getNombre());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Met à jour un besoin en base (nombre de secouristes requis).
     *
     * @param besoin le besoin à mettre à jour
     * @return 1 si succès, -1 sinon
     */
    @Override
    public int update(Besoin besoin) {
        String query = "UPDATE Besoin SET NOMBRE = ? WHERE IDDPS = ? AND INTITULECOMP = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, besoin.getNombre());
            pst.setLong(2, besoin.getDps().getId());
            pst.setString(3, besoin.getCompetence().getIntitule());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Supprime un besoin de la base.
     *
     * @param besoin le besoin à supprimer
     * @return 1 si succès, -1 sinon
     */
    @Override
    public int delete(Besoin besoin) {
        String query = "DELETE FROM Besoin WHERE IDDPS = ? AND INTITULECOMP = ?";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, besoin.getDps().getId());
            pst.setString(2, besoin.getCompetence().getIntitule());
            
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Récupère tous les besoins enregistrés en base.
     *
     * @return une liste de tous les besoins
     */
    @Override
    public List<Besoin> findAll() {
        List<Besoin> besoins = new LinkedList<>();
        String query = "SELECT * FROM Besoin";

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                long id = rs.getLong("IDDPS");
                String comp = rs.getString("INTITULECOMP");
                int nombre = rs.getInt("NOMBRE");

                DPS idDps = dpsDAO.findByID(id);
                Competence competence = competenceDAO.findByID(comp);
                
                besoins.add(new Besoin(idDps, competence, nombre));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return besoins;
    }

    /**
     * Recherche un besoin en fonction de l'ID du DPS et de l'intitulé de la compétence.
     *
     * @param keys les paramètres : [0] = id du DPS, [1] = intitulé compétence
     * @return le besoin trouvé ou null si introuvable
     */
    @Override
    public Besoin findByID(Object... keys) {
        long idDps = (long) keys[0];
        String comp = (String) keys[1];

        String query = "SELECT * FROM Besoin WHERE IDDPS = ? AND INTITULECOMP = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, idDps);
            pst.setString(2, comp);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int nombre = rs.getInt("NOMBRE");
                    DPS dps = dpsDAO.findByID(idDps);
                    Competence competence = competenceDAO.findByID(comp);
                    
                    return new Besoin(dps, competence, nombre);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
