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
        // This method is not used directly for updating with old/new competence.
        // Instead, use updateWithOldCompetence for full update logic.
        throw new UnsupportedOperationException("Use updateWithOldCompetence for updating competences.");
    }

    public int updateWithOldCompetence(Competence ancienneCompetence, Competence nouvelleCompetence) {
        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false); // Start transaction

            String oldIntitule = ancienneCompetence.getIntitule();
            String newIntitule = nouvelleCompetence.getIntitule().trim();

            // Update references in Necessite where this competence is a prerequisite
            String updateNecessiteQuery = "UPDATE Necessite SET COMPETENCEREQUISE = ? WHERE COMPETENCEREQUISE = ?";
            try (PreparedStatement pst = con.prepareStatement(updateNecessiteQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                pst.executeUpdate();
            }

            // Update references in Besoin
            String updateBesoinQuery = "UPDATE Besoin SET INTITULE seasoning = ? WHERE INTITULECOMP = ?";
            try (PreparedStatement pst = con.prepareStatement(updateBesoinQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                pst.executeUpdate();
            }

            // Update references in Possede
            String updatePossedeQuery = "UPDATE Possede SET competence = ? WHERE competence = ?";
            try (PreparedStatement pst = con.prepareStatement(updatePossedeQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                pst.executeUpdate();
            }

            // Update the competence itself
            String updateCompetenceQuery = "UPDATE Competence SET INTITULE = ? WHERE INTITULE = ?";
            int result = 0;
            try (PreparedStatement pst = con.prepareStatement(updateCompetenceQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                result = pst.executeUpdate();
            }

            if (result > 0) {
                // Delete existing prerequisites
                String deletePrereqQuery = "DELETE FROM Necessite WHERE COMPETENCE = ?";
                try (PreparedStatement prereqPst = con.prepareStatement(deletePrereqQuery)) {
                    prereqPst.setString(1, newIntitule);
                    prereqPst.executeUpdate();
                }
                // Recreate prerequisites if any
                if (nouvelleCompetence.getPrerequis() != null && !nouvelleCompetence.getPrerequis().isEmpty()) {
                    createPrerequis(nouvelleCompetence);
                }
            } else {
                throw new SQLException("Échec de la mise à jour de la compétence.");
            }

            con.commit();
            return result;

        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            ex.printStackTrace();
            return -1;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
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
                Competence competence = new Competence(intitule);
                // Load prerequisites
                String prereqQuery = "SELECT COMPETENCEREQUISE FROM Necessite WHERE COMPETENCE = ?";
                try (PreparedStatement prereqPst = con.prepareStatement(prereqQuery)) {
                    prereqPst.setString(1, intitule);
                    ResultSet prereqRs = prereqPst.executeQuery();
                    List<Competence> prerequis = new ArrayList<>();
                    while (prereqRs.next()) {
                        String prereqIntitule = prereqRs.getString("COMPETENCEREQUISE");
                        Competence prereq = findByID(prereqIntitule);
                        if (prereq != null) {
                            prerequis.add(prereq);
                        }
                    }
                    competence.setPrerequis(prerequis);
                }
                competences.add(competence);
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
                    Competence competence = new Competence(rs.getString("INTITULE"));
                    // Load prerequisites
                    String prereqQuery = "SELECT COMPETENCEREQUISE FROM Necessite WHERE COMPETENCE = ?";
                    try (PreparedStatement prereqPst = con.prepareStatement(prereqQuery)) {
                        prereqPst.setString(1, intitule);
                        ResultSet prereqRs = prereqPst.executeQuery();
                        List<Competence> prerequis = new ArrayList<>();
                        while (prereqRs.next()) {
                            String prereqIntitule = prereqRs.getString("COMPETENCEREQUISE");
                            Competence prereq = findByID(prereqIntitule);
                            if (prereq != null) {
                                prerequis.add(prereq);
                            }
                        }
                        competence.setPrerequis(prerequis);
                    }
                    return competence;
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
                    pst.setString(1, competence.getIntitule());
                    pst.setString(2, prerequis.getIntitule());
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