package model.dao;

import java.sql.*;
import java.util.*;

import model.data.*;

/**
 * DAO pour la gestion des objets {@link DPS} dans la base de données.
 * Fournit les opérations CRUD ainsi que le chargement des besoins liés à un DPS.
 */
public class DPSDAO extends DAO<DPS> {

    private JourneeDAO journeeDAO;
    private SiteDAO siteDAO;
    private SportDAO sportDAO;

    /**
     * Constructeur du DAO DPS.
     * Initialise les DAO nécessaires à la reconstruction complète des objets DPS.
     */
    public DPSDAO() {
        this.journeeDAO = new JourneeDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
    }

    /**
     * Insère un nouveau DPS dans la base de données.
     *
     * @param dps l'objet DPS à insérer
     * @return nombre de lignes affectées ou -1 en cas d'erreur
     */
    @Override
    public int create(DPS dps) {
        String query = "INSERT INTO DPS(ID, HORAIRE_DEPART, HORAIRE_FIN, LESITE, LESPORT, JOUR, MOIS, ANNEE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, dps.getId());
            pst.setTime(2, dps.getHoraireDep());
            pst.setTime(3, dps.getHoraireFin());
            pst.setString(4, dps.getSite().getCode());
            pst.setString(5, dps.getSport().getCode());
            pst.setInt(6, dps.getJournee().getJour());
            pst.setInt(7, dps.getJournee().getMois());
            pst.setInt(8, dps.getJournee().getAnnee());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Met à jour un DPS existant dans la base.
     *
     * @param dps le DPS à mettre à jour
     * @return nombre de lignes affectées ou -1 en cas d'erreur
     */
    @Override
    public int update(DPS dps) {
        String query = "UPDATE DPS SET HORAIRE_DEPART = ?, HORAIRE_FIN = ?, LESITE = ?, LESPORT = ?, JOUR = ?, MOIS = ?, ANNEE = ? WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setTime(1, dps.getHoraireDep());
            pst.setTime(2, dps.getHoraireFin());
            pst.setString(3, dps.getSite().getCode());
            pst.setString(4, dps.getSport().getCode());
            pst.setInt(5, dps.getJournee().getJour());
            pst.setInt(6, dps.getJournee().getMois());
            pst.setInt(7, dps.getJournee().getAnnee());
            pst.setLong(8, dps.getId());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Supprime un DPS de la base de données.
     *
     * @param dps le DPS à supprimer
     * @return nombre de lignes supprimées ou -1 en cas d'erreur
     */
    @Override
    public int delete(DPS dps) {
        String query = "DELETE FROM DPS WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, dps.getId());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Récupère tous les DPS enregistrés dans la base de données.
     *
     * @return liste de tous les objets DPS
     */
    @Override
    public List<DPS> findAll() {
        List<DPS> dpsList = new ArrayList<>();
        String query = "SELECT * FROM DPS";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                long id = rs.getLong("ID");
                Time horaireDep = rs.getTime("HORAIRE_DEPART");
                Time horaireFin = rs.getTime("HORAIRE_FIN");
                String codeSite = rs.getString("LESITE");
                String codeSport = rs.getString("LESPORT");
                int jour = rs.getInt("JOUR");
                int mois = rs.getInt("MOIS");
                int annee = rs.getInt("ANNEE");

                Site site = siteDAO.findByID(codeSite);
                Sport sport = sportDAO.findByID(codeSport);
                Journee journee = journeeDAO.findByID(jour, mois, annee);

                DPS dps = new DPS(id, horaireDep, horaireFin, site, sport, journee);
                chargerBesoinsPourDPS(dps, con);
                dpsList.add(dps);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dpsList;
    }

    /**
     * Recherche un DPS par son identifiant.
     *
     * @param keys tableau contenant l'ID du DPS
     * @return l'objet DPS trouvé ou null si non trouvé
     */
    @Override
    public DPS findByID(Object... keys) {
        long id = (long) keys[0];
        DPS dps = null;

        String query = "SELECT * FROM DPS WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Time horaireDep = rs.getTime("HORAIRE_DEPART");
                    Time horaireFin = rs.getTime("HORAIRE_FIN");
                    String codeSite = rs.getString("LESITE");
                    String codeSport = rs.getString("LESPORT");
                    int jour = rs.getInt("JOUR");
                    int mois = rs.getInt("MOIS");
                    int annee = rs.getInt("ANNEE");

                    Site site = siteDAO.findByID(codeSite);
                    Sport sport = sportDAO.findByID(codeSport);
                    Journee journee = journeeDAO.findByID(jour, mois, annee);
                    dps = new DPS(id, horaireDep, horaireFin, site, sport, journee);
                    chargerBesoinsPourDPS(dps, con);

                    return dps;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Charge les besoins associés à un DPS donné depuis la base de données.
     *
     * @param dps le DPS concerné
     * @param con la connexion ouverte à la base de données
     * @throws SQLException en cas d'erreur SQL
     */
    private void chargerBesoinsPourDPS(DPS dps, Connection con) throws SQLException {
        if (dps == null) return;

        String queryBesoins = "SELECT b.intituleComp, b.nombre FROM Besoin b WHERE b.idDPS = ?";

        try (PreparedStatement pstBesoins = con.prepareStatement(queryBesoins)) {
            pstBesoins.setLong(1, dps.getId());
            try (ResultSet rsBesoins = pstBesoins.executeQuery()) {
                while (rsBesoins.next()) {
                    Competence competenceDuBesoin = new Competence(rsBesoins.getString("intituleComp"));
                    int nombreNecessaire = rsBesoins.getInt("nombre");

                    Besoin besoin = new Besoin(dps, competenceDuBesoin, nombreNecessaire);
                    dps.getBesoins().add(besoin);
                }
            }
        }
    }
}
