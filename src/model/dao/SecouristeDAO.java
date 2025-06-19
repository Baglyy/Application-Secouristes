package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Secouriste;
import model.data.Competence;

/**
 * DAO (Data Access Object) pour la gestion des entités Secouriste.
 * Permet les opérations CRUD ainsi que des recherches personnalisées.
 */
public class SecouristeDAO extends DAO<Secouriste> {

    /**
     * Recherche un secouriste par son identifiant et son nom.
     * @param id identifiant du secouriste
     * @param nom nom du secouriste
     * @return le secouriste trouvé ou null si aucun correspondant
     */
    public Secouriste findByIdAndNom(long id, String nom) {
        String query = "SELECT * FROM Secouriste WHERE ID = ? AND NOM = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            pst.setString(2, nom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String prenom = rs.getString("PRENOM");
                    String dateDeNaissance = rs.getString("DATENAISSANCE");
                    String email = rs.getString("EMAIL");
                    String tel = rs.getString("TEL");
                    String adresse = rs.getString("ADRESSE");

                    return new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Crée un nouveau secouriste en base.
     * @param secouriste le secouriste à enregistrer
     * @return 1 si succès, -1 sinon
     */
    @Override
    public int create(Secouriste secouriste) {
        String query = "INSERT INTO Secouriste(ID, NOM, PRENOM, DATENAISSANCE, EMAIL, TEL, ADRESSE) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); 
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, secouriste.getId());
            pst.setString(2, secouriste.getNom());
            pst.setString(3, secouriste.getPrenom());
            pst.setString(4, secouriste.getDateDeNaissance());
            pst.setString(5, secouriste.getEmail());
            pst.setString(6, secouriste.getTel());
            pst.setString(7, secouriste.getAdresse());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Met à jour un secouriste existant.
     * @param secouriste secouriste à mettre à jour
     * @return nombre de lignes affectées ou -1 si erreur
     */
    @Override
    public int update(Secouriste secouriste) {
        String query = "UPDATE Secouriste SET NOM = ?, PRENOM = ?, DATENAISSANCE = ?, EMAIL = ?, TEL = ?, ADRESSE = ? WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, secouriste.getNom());
            pst.setString(2, secouriste.getPrenom());
            pst.setString(3, secouriste.getDateDeNaissance());
            pst.setString(4, secouriste.getEmail());
            pst.setString(5, secouriste.getTel());
            pst.setString(6, secouriste.getAdresse());
            pst.setLong(7, secouriste.getId());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Supprime un secouriste de la base.
     * @param secouriste secouriste à supprimer
     * @return 1 si succès, -1 sinon
     */
    @Override
    public int delete(Secouriste secouriste) {
        String query = "DELETE FROM Secouriste WHERE ID = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, secouriste.getId());

            return pst.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    /**
     * Récupère tous les secouristes avec leurs compétences.
     * @return liste de secouristes
     */
    @Override
    public List<Secouriste> findAll() {
        List<Secouriste> secouristesList = new ArrayList<>();
        Map<Long, Secouriste> secouristesMap = new HashMap<>();

        String query = "SELECT s.ID as secouriste_id, s.NOM as secouriste_nom, s.PRENOM, s.DATENAISSANCE, " +
                       "s.EMAIL, s.TEL, s.ADRESSE, c.intitule as competence_intitule " +
                       "FROM Secouriste s " +
                       "LEFT JOIN Possede p ON s.ID = p.idSecouriste " +
                       "LEFT JOIN Competence c ON p.competence = c.intitule " +
                       "ORDER BY s.ID";

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                long id = rs.getLong("secouriste_id");
                Secouriste secouriste = secouristesMap.get(id);

                if (secouriste == null) {
                    secouriste = new Secouriste(
                        id,
                        rs.getString("secouriste_nom"),
                        rs.getString("PRENOM"),
                        rs.getString("DATENAISSANCE"),
                        rs.getString("EMAIL"),
                        rs.getString("TEL"),
                        rs.getString("ADRESSE")
                    );
                    secouristesMap.put(id, secouriste);
                    secouristesList.add(secouriste);
                }

                String comp = rs.getString("competence_intitule");
                if (comp != null) {
                    secouriste.getCompetences().add(new Competence(comp));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return secouristesList;
    }

    /**
     * Recherche un secouriste par son ID.
     * @param keys un tableau avec l'identifiant du secouriste
     * @return le secouriste trouvé ou null
     */
    @Override
    public Secouriste findByID(Object... keys) {
        long id = (long) keys[0];
        String query = "SELECT * FROM Secouriste WHERE ID = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Secouriste(
                        id,
                        rs.getString("NOM"),
                        rs.getString("PRENOM"),
                        rs.getString("DATENAISSANCE"),
                        rs.getString("EMAIL"),
                        rs.getString("TEL"),
                        rs.getString("ADRESSE")
                    );
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Recherche un secouriste par son nom.
     * @param nom le nom du secouriste
     * @return le secouriste ou null
     */
    public Secouriste findByNom(String nom) {
        String query = "SELECT * FROM Secouriste WHERE NOM = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, nom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Secouriste(
                        rs.getLong("ID"),
                        nom,
                        rs.getString("PRENOM"),
                        rs.getString("DATENAISSANCE"),
                        rs.getString("EMAIL"),
                        rs.getString("TEL"),
                        rs.getString("ADRESSE")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du secouriste par nom : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recherche un secouriste par prénom et nom, sans tenir compte de la casse.
     * @param prenom prénom du secouriste
     * @param nom nom du secouriste
     * @return le secouriste ou null
     */
    public Secouriste findByPrenomAndNom(String prenom, String nom) {
        String query = "SELECT * FROM Secouriste WHERE UPPER(PRENOM) = ? AND UPPER(NOM) = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, prenom.toUpperCase().trim());
            pst.setString(2, nom.toUpperCase().trim());

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Secouriste(
                        rs.getLong("ID"),
                        nom,
                        prenom,
                        rs.getString("DATENAISSANCE"),
                        rs.getString("EMAIL"),
                        rs.getString("TEL"),
                        rs.getString("ADRESSE")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du secouriste par prénom et nom : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Recherche un secouriste à partir de son nom complet.
     * @param fullName le nom complet "Prénom Nom"
     * @return le secouriste ou null
     */
    public Secouriste findByFullName(String fullName) {
        try {
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length >= 2) {
                String prenom = parts[0];
                String nom = parts[parts.length - 1];
                return findByPrenomAndNom(prenom, nom);
            } else {
                System.err.println("Format de nom invalide: '" + fullName + "' (doit contenir prénom et nom)");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'analyse du nom complet '" + fullName + "' : " + e.getMessage());
        }
        return null;
    }

    /**
     * Récupère uniquement l'ID d’un secouriste à partir de son nom complet.
     * @param fullName nom complet "Prénom Nom"
     * @return l'ID du secouriste ou null
     */
    public Long getIdByFullName(String fullName) {
        try {
            String[] parts = fullName.trim().split("\\s+");
            if (parts.length >= 2) {
                String prenom = parts[0];
                String nom = parts[parts.length - 1];

                String query = "SELECT ID FROM Secouriste WHERE UPPER(PRENOM) = ? AND UPPER(NOM) = ?";
                try (Connection con = getConnection();
                     PreparedStatement pst = con.prepareStatement(query)) {

                    pst.setString(1, prenom.toUpperCase().trim());
                    pst.setString(2, nom.toUpperCase().trim());

                    try (ResultSet rs = pst.executeQuery()) {
                        if (rs.next()) {
                            return rs.getLong("ID");
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'ID pour '" + fullName + "' : " + e.getMessage());
        }
        return null;
    }
}
