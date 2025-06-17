package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Competence;

public class CompetenceDAO extends DAO<Competence> {

    @Override
    public int create(Competence competence) {
        String query = "INSERT INTO Competence(INTITULE) VALUES (?)";
        int competenceResult = 0;
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, competence.getIntitule());
            competenceResult = pst.executeUpdate();

            if (competenceResult > 0 && competence.getPrerequis() != null && !competence.getPrerequis().isEmpty()) {
                createPrerequis(competence);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return competenceResult;
    }

    @Override
    public int update(Competence competence) {
        System.out.println("Mise à jour impossible pour la compétence");
        return -1;
    }

    @Override
    public int delete(Competence competence) {
        String query = "DELETE FROM Competence WHERE INTITULE = ?";
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, competence.getIntitule());
            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Competence> findAll() {
        List<Competence> competences = new LinkedList<>();
        String query = "SELECT * FROM Competence";

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                String intitule = rs.getString("INTITULE");
                competences.add(new Competence(intitule));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return competences;
    }

    @Override
    public Competence findByID(Object... keys) {

        String intitule = (String) keys[0];
        
        String query = "SELECT * FROM Competence WHERE INTITULE = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, intitule);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Competence(rs.getString("INTITULE"));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public int createPrerequis(Competence competence) {
        String query = "INSERT INTO Necessite(COMPETENCE, COMPETENCEREQUISE) VALUES (?, ?)";
        int totalLignesAffectees = 0;
        try (Connection con = getConnection(); 
            PreparedStatement pst = con.prepareStatement(query)) {

            for (Competence prerequis : competence.getPrerequis()) {
                if (prerequis != null && prerequis.getIntitule() != null) {
                    pst.setString(1, competence.getIntitule()); // La compétence qui a ce prérequis
                    pst.setString(2, prerequis.getIntitule());  // La compétence qui EST le prérequis

                    // Exécuter l'insertion pour CE prérequis
                    int lignesPourCePrerequis = pst.executeUpdate();
                    if (lignesPourCePrerequis > 0) {
                        totalLignesAffectees += lignesPourCePrerequis;
                    }
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
        return totalLignesAffectees;
    }
}
