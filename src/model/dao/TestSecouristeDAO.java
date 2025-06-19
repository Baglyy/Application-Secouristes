package model.dao;

import model.dao.SecouristeDAO;
import model.data.Secouriste;

/**
 * Classe de test pour le DAO {@link SecouristeDAO}.
 * Ce test manuel permet de vérifier le bon fonctionnement des opérations de base
 * sur l'entité {@link Secouriste}, à savoir : création, lecture, mise à jour et suppression (CRUD).
 */
public class TestSecouristeDAO {

    /**
     * Point d'entrée principal pour le test du DAO Secouriste.
     * <p>Ce test couvre les cas suivants :</p>
     * <ul>
     *     <li>Création d'un secouriste</li>
     *     <li>Recherche d'un secouriste par ID</li>
     *     <li>Mise à jour des informations du secouriste</li>
     *     <li>Suppression du secouriste</li>
     * </ul>
     *
     * @param args les arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        SecouristeDAO dao = new SecouristeDAO();

        // Création d'un nouveau secouriste
        Secouriste s1 = new Secouriste(123L, "Dupont", "Jean", "1990-01-01", "jean.dupont@example.com", "0601020304", "123 rue Paris");
        int resultCreate = dao.create(s1);
        System.out.println("Résultat création : " + resultCreate);

        // Recherche par ID
        Secouriste found = dao.findByID(123L);
        if (found != null) {
            System.out.println("Secouriste trouvé : " + found.getNom() + " " + found.getPrenom());
        } else {
            System.out.println("Aucun secouriste trouvé avec l'ID 123");
        }

        // Modification
        s1.setNom("Durand");
        int resultUpdate = dao.update(s1);
        System.out.println("Résultat mise à jour : " + resultUpdate);

        // Suppression
        int resultDelete = dao.delete(s1);
        System.out.println("Résultat suppression : " + resultDelete);
    }
}
