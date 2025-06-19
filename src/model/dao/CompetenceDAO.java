package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Competence;

/**
 * DAO pour gérer les opérations CRUD sur les objets {@link Competence}.
 * Gère aussi les relations de prérequis via la table Necessite.
 */
public class CompetenceDAO extends DAO<Competence> {

    /**
     * Crée une compétence dans la base de données, avec ses éventuels prérequis.
     *
     * @param competence la compétence à créer
     * @return 1 si succès, -1 en cas d’erreur
     */
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

    /**
     * Lève une exception car cette méthode n'est pas utilisée.
     * Utiliser {@link #updateWithOldCompetence(Competence, Competence)} à la place.
     */
    @Override
    public int update(Competence competence) {
        throw new UnsupportedOperationException("Use updateWithOldCompetence for updating competences.");
    }

    /**
     * Met à jour une compétence (intitulé et prérequis) en gérant les dépendances liées.
     *
     * @param ancienneCompetence l’ancienne compétence
     * @param nouvelleCompetence la nouvelle compétence
     * @return nombre de lignes modifiées, ou -1 si erreur
     */
    public int updateWithOldCompetence(Competence ancienneCompetence, Competence nouvelleCompetence) {
        Connection con = null;
        try {
            con = getConnection();
            con.setAutoCommit(false); // Start transaction

            String oldIntitule = ancienneCompetence.getIntitule();
            String newIntitule = nouvelleCompetence.getIntitule().trim();

            // Mise à jour des références dans les tables associées
            String updateNecessiteQuery = "UPDATE Necessite SET COMPETENCEREQUISE = ? WHERE COMPETENCEREQUISE = ?";
            try (PreparedStatement pst = con.prepareStatement(updateNecessiteQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                pst.executeUpdate();
            }

            String updateBesoinQuery = "UPDATE Besoin SET INTITULECOMP = ? WHERE INTITULECOMP = ?";
            try (PreparedStatement pst = con.prepareStatement(updateBesoinQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                pst.executeUpdate();
            }

            String updatePossedeQuery = "UPDATE Possede SET competence = ? WHERE competence = ?";
            try (PreparedStatement pst = con.prepareStatement(updatePossedeQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                pst.executeUpdate();
            }

            // Mise à jour de la compétence elle-même
            String updateCompetenceQuery = "UPDATE Competence SET INTITULE = ? WHERE INTITULE = ?";
            int result = 0;
            try (PreparedStatement pst = con.prepareStatement(updateCompetenceQuery)) {
                pst.setString(1, newIntitule);
                pst.setString(2, oldIntitule);
                result = pst.executeUpdate();
            }

            if (result > 0) {
                String deletePrereqQuery = "DELETE FROM Necessite WHERE COMPETENCE = ?";
                try (PreparedStatement prereqPst = con.prepareStatement(deletePrereqQuery)) {
                    prereqPst.setString(1, newIntitule);
                    prereqPst.executeUpdate();
                }

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

    /**
     * Supprime une compétence de la base.
     *
     * @param competence la compétence à supprimer
     * @return 1 si succès, -1 sinon
     */
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

    /**
     * Récupère toutes les compétences et leurs prérequis.
     *
     * @return liste des compétences
     */
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

    /**
     * Recherche une compétence par son intitulé et charge ses prérequis.
     *
     * @param keys tableau avec l’intitulé en première position
     * @return la compétence ou null si non trouvée
     */
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

    /**
     * Crée les prérequis d'une compétence dans la table Necessite.
     *
     * @param competence la compétence contenant des prérequis
     * @return nombre de lignes insérées, ou -1 en cas d’erreur
     */
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
