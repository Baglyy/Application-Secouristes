package model.dao;

import java.sql.*;
import java.util.*;
import model.data.Journee;

/**
 * DAO pour la gestion des disponibilités des secouristes.
 * Permet de créer, supprimer et rechercher des disponibilités associées à une journée spécifique.
 */
public class DisponibiliteDAO extends DAO<Void> {
    
    private SecouristeDAO secouristeDAO = new SecouristeDAO();
    private JourneeDAO journeeDAO = new JourneeDAO();

    /**
     * Crée une nouvelle disponibilité pour un secouriste à une date donnée.
     * Si la journée n'existe pas, elle est automatiquement créée.
     *
     * @param idSecouriste identifiant du secouriste
     * @param jour jour de la disponibilité
     * @param mois mois de la disponibilité
     * @param annee année de la disponibilité
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean createDisponibilite(long idSecouriste, int jour, int mois, int annee) {
        // Vérifier et créer la journée si nécessaire
        if (!ensureJourneeExists(jour, mois, annee)) {
            System.err.println("Impossible de créer ou vérifier l'existence de la journée " + jour + "/" + mois + "/" + annee);
            return false;
        }

        String sql = 
            "INSERT INTO Disponibilite (idSecouriste, jour, mois, annee) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, jour);
            pstmt.setInt(3, mois);
            pstmt.setInt(4, annee);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de disponibilité : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie si une journée existe dans la table Journee, et la crée si nécessaire.
     *
     * @param jour jour
     * @param mois mois
     * @param annee année
     * @return true si la journée existe ou a été créée, false sinon
     */
    private boolean ensureJourneeExists(int jour, int mois, int annee) {
        try {
            Journee existingJournee = journeeDAO.findByID(jour, mois, annee);
            if (existingJournee != null) {
                return true;
            }

            Journee newJournee = new Journee(jour, mois, annee);
            int result = journeeDAO.create(newJournee);

            if (result > 0) {
                System.out.println("Journée créée automatiquement : " + jour + "/" + mois + "/" + annee);
                return true;
            } else {
                System.err.println("Échec de la création de la journée : " + jour + "/" + mois + "/" + annee);
                return false;
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification/création de la journée : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime une disponibilité d’un secouriste pour une date donnée.
     *
     * @param idSecouriste identifiant du secouriste
     * @param jour jour
     * @param mois mois
     * @param annee année
     * @return true si une ligne a été supprimée, false sinon
     */
    public boolean deleteDisponibilite(long idSecouriste, int jour, int mois, int annee) {
        String sql = 
            "DELETE FROM Disponibilite WHERE idSecouriste = ? AND jour = ? AND mois = ? AND annee = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, jour);
            pstmt.setInt(3, mois);
            pstmt.setInt(4, annee);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de disponibilité : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recherche les disponibilités d’un secouriste pour un mois et une année donnés.
     *
     * @param idSecouriste identifiant du secouriste
     * @param mois mois recherché
     * @param annee année recherchée
     * @return liste de maps représentant chaque date disponible (jour, mois, annee)
     */
    public List<Map<String, Object>> findDisponibilitesBySecouristeAndMonth(long idSecouriste, int mois, int annee) {
        List<Map<String, Object>> dispos = new ArrayList<>();
        String sql = 
            "SELECT jour, mois, annee FROM Disponibilite WHERE idSecouriste = ? AND mois = ? AND annee = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idSecouriste);
            pstmt.setInt(2, mois);
            pstmt.setInt(3, annee);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dispo = new HashMap<>();
                    dispo.put("jour", rs.getInt("jour"));
                    dispo.put("mois", rs.getInt("mois"));
                    dispo.put("annee", rs.getInt("annee"));
                    dispos.add(dispo);
                }
            }
            return dispos;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche des disponibilités : " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public int create(Void v) {
        throw new UnsupportedOperationException("Use createDisponibilite instead");
    }

    @Override
    public int update(Void v) {
        throw new UnsupportedOperationException("Updates not supported");
    }

    @Override
    public int delete(Void v) {
        throw new UnsupportedOperationException("Use specific deleteDisponibilite method");
    }

    @Override
    public Void findByID(Object... keys) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<Void> findAll() {
        throw new UnsupportedOperationException("Not implemented");
    }
}
