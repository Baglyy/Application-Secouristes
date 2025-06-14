package model.dao;

import java.sql.*;
import java.util.*;

import model.data.Secouriste;
import model.data.Competence;


public class SecouristeDAO extends DAO<Secouriste> {


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

    @Override
    public List<Secouriste> findAll() {
        // Utiliser ArrayList est souvent un meilleur choix par défaut que LinkedList pour ce cas.
        List<Secouriste> secouristesList = new ArrayList<>();
        // Map pour regrouper les données et éviter de créer plusieurs fois le même objet Secouriste
        Map<Long, Secouriste> secouristesMap = new HashMap<>();

        String query = "SELECT s.ID as secouriste_id, s.NOM as secouriste_nom, s.PRENOM, s.DATENAISSANCE, " +
                    "s.EMAIL, s.TEL, s.ADRESSE, c.intitule as competence_intitule " +
                    "FROM Secouriste s " +
                    "LEFT JOIN Possede p ON s.ID = p.idSecouriste " +
                    "LEFT JOIN Competence c ON p.competence = c.intitule " +
                    "ORDER BY s.ID"; // L'ordre est utile pour le traitement

        try (Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                long secouristeId = rs.getLong("secouriste_id");
                Secouriste secouriste = secouristesMap.get(secouristeId);

                if (secouriste == null) {
                    secouriste = new Secouriste(
                            secouristeId,
                            rs.getString("secouriste_nom"),
                            rs.getString("PRENOM"), // Utiliser les noms de colonnes exacts de votre BDD
                            rs.getString("DATENAISSANCE"),
                            rs.getString("EMAIL"),
                            rs.getString("TEL"),
                            rs.getString("ADRESSE")
                    );
                    // La liste de compétences est initialisée à vide par getCompetences() la première fois.
                    secouristesMap.put(secouristeId, secouriste);
                    secouristesList.add(secouriste);
                }

                String competenceIntitule = rs.getString("competence_intitule");
                if (competenceIntitule != null) {
                    // Crée un objet Competence et l'ajoute à la liste du secouriste
                    // La méthode getCompetences() s'assure que la liste n'est pas null
                    secouriste.getCompetences().add(new Competence(competenceIntitule));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); // Envisagez un meilleur logging ou la propagation de l'exception
        }
        return secouristesList;
    }

    @Override
    public Secouriste findByID(Object... keys) {
        long id = (long) keys[0];
        String query = "SELECT * FROM Secouriste WHERE ID = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setLong(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String nom = rs.getString("NOM");
                    String prenom = rs.getString("PRENOM");
                    String dateDeNaissance = rs.getString("DATENAISSANCE");
                    String email = rs.getString("EMAIL");
                    String tel = rs.getString("TEL");
                    String adresse = rs.getString("ADRESSE");

                    return new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Secouriste findByNom(String nom) {
        String query = "SELECT * FROM Secouriste WHERE NOM = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, nom);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("ID");
                    String prenom = rs.getString("PRENOM");
                    String dateDeNaissance = rs.getString("DATENAISSANCE");
                    String email = rs.getString("EMAIL");
                    String tel = rs.getString("TEL");
                    String adresse = rs.getString("ADRESSE");
                    return new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du secouriste par nom : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trouve un secouriste par son prénom et nom
     */
    public Secouriste findByPrenomAndNom(String prenom, String nom) {
        String query = "SELECT * FROM Secouriste WHERE UPPER(PRENOM) = ? AND UPPER(NOM) = ?";
        try (Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(query)) {
            
            pst.setString(1, prenom.toUpperCase().trim());
            pst.setString(2, nom.toUpperCase().trim());
            
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("ID");
                    String dateDeNaissance = rs.getString("DATENAISSANCE");
                    String email = rs.getString("EMAIL");
                    String tel = rs.getString("TEL");
                    String adresse = rs.getString("ADRESSE");
                    
                    return new Secouriste(id, nom, prenom, dateDeNaissance, email, tel, adresse);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du secouriste par prénom et nom : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Trouve un secouriste par son nom complet au format "PRÉNOM NOM"
     * Compatible avec le format utilisé dans l'application
     */
    public Secouriste findByFullName(String fullName) {
        try {
            String[] parts = fullName.trim().split("\\s+"); // Split sur les espaces
            if (parts.length >= 2) {
                String prenom = parts[0];
                String nom = parts[parts.length - 1]; // Dernier mot = nom de famille
                
                System.out.println("Recherche secouriste - Prénom: '" + prenom + "', Nom: '" + nom + "'");
                return findByPrenomAndNom(prenom, nom);
            } else {
                System.err.println("Format de nom invalide: '" + fullName + "' (doit contenir prénom et nom)");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'analyse du nom complet '" + fullName + "' : " + e.getMessage());
            return null;
        }
    }

    /**
     * Récupère uniquement l'ID d'un secouriste par son nom complet
     * Méthode optimisée si on n'a besoin que de l'ID
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
