package model.dao;

import java.sql.*;
import java.util.*;

import model.data.DPS;
import model.data.Site;
import model.data.Sport;
import model.data.Journee;

public class DPSDAO extends DAO<DPS> {

    private JourneeDAO journeeDAO;
    private SiteDAO siteDAO;
    private SportDAO sportDAO;

    public DPSDAO() {
        this.journeeDAO = new JourneeDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
    }

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

    @Override
    public List<DPS> findAll() {
        List<DPS> dpss = new LinkedList<>();
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

                dpss.add(new DPS(id, horaireDep, horaireFin, site, sport, journee));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return dpss;
    }

    @Override
    public DPS findByID(Object... keys) {
        long id = (long) keys[0];

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

                    return new DPS(id, horaireDep, horaireFin, site, sport, journee);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
