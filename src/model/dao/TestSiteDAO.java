package model.dao;

import model.data.Site;

/**
 * Classe de test pour le DAO {@link SiteDAO}.
 * <p>
 * Ce test permet de vérifier les principales opérations CRUD sur l'entité {@link Site}.
 * Il teste la création, la lecture (findByID, findAll), la mise à jour et la suppression.
 * </p>
 */
public class TestSiteDAO {

    /**
     * Méthode principale exécutant les tests du DAO {@link SiteDAO}.
     *
     * @param args arguments de la ligne de commande (non utilisés ici)
     */
    public static void main(String[] args) {
        SiteDAO dao = new SiteDAO();

        System.out.println("========== TEST CREATE ==========");
        Site site = new Site("S1", "Parc central", 1.234f, 2.345f);
        if (dao.create(site) > 0) {
            System.out.println("Création réussie : " + site.getCode());
        } else {
            System.out.println("Échec de la création.");
        }

        System.out.println("\n========== TEST FIND BY ID ==========");
        Site s2 = dao.findByID("S1");
        if (s2 != null) {
            System.out.println("Site trouvé : " + s2.getNom());
        } else {
            System.out.println("Aucun site trouvé.");
        }

        System.out.println("\n========== TEST UPDATE ==========");
        site.setNom("Parc rénové");
        site.setLatitude(3.1415f);
        if (dao.update(site) > 0) {
            System.out.println("Mise à jour réussie.");
        } else {
            System.out.println("Échec de la mise à jour.");
        }

        System.out.println("\n========== TEST FIND ALL ==========");
        for (Site s : dao.findAll()) {
            System.out.println("Site : " + s.getCode() + ", " + s.getNom());
        }

        System.out.println("\n========== TEST DELETE ==========");
        if (dao.delete(site) > 0) {
            System.out.println("Suppression réussie.");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
}
