package model.dao;

import model.data.Sport;

/**
 * Classe de test pour le DAO {@link SportDAO}.
 * <p>
 * Ce test permet de vérifier les opérations CRUD sur l'entité {@link Sport}.
 * Elle teste successivement la création, la lecture (par ID et tous), la mise à jour, puis la suppression.
 * </p>
 */
public class TestSportDAO {

    /**
     * Méthode principale exécutant les tests du DAO {@link SportDAO}.
     *
     * @param args arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        SportDAO dao = new SportDAO();

        System.out.println("========== TEST CREATE ==========");
        Sport s1 = new Sport("ATH", "Athlétisme");
        if (dao.create(s1) > 0) {
            System.out.println("Création réussie : " + s1);
        } else {
            System.out.println("Échec de la création.");
        }

        System.out.println("\n========== TEST FIND BY ID ==========");
        Sport s2 = dao.findByID("ATH");
        if (s2 != null) {
            System.out.println("Sport trouvé : " + s2);
        } else {
            System.out.println("Aucun sport trouvé.");
        }

        System.out.println("\n========== TEST UPDATE ==========");
        s1.setNom("Athlétisme modifié");
        if (dao.update(s1) > 0) {
            System.out.println("Mise à jour réussie : " + s1);
        } else {
            System.out.println("Échec de la mise à jour.");
        }

        System.out.println("\n========== TEST FIND ALL ==========");
        for (Sport s : dao.findAll()) {
            System.out.println(s);
        }

        System.out.println("\n========== TEST DELETE ==========");
        if (dao.delete(s1) > 0) {
            System.out.println("Suppression réussie.");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
}
