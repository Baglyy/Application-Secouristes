package model.dao;

import model.data.*;

import java.util.List;

public class TestBesoinDAO {

    public static void main(String[] args) {
        // DAO nécessaires
        CompetenceDAO competenceDAO = new CompetenceDAO();
        DPSDAO dpsDAO = new DPSDAO();
        BesoinDAO besoinDAO = new BesoinDAO();

        // Création des objets associés
        Competence comp = new Competence("Logistique");
        competenceDAO.create(comp);

        // Création d’un DPS pour lier au besoin
        Site site = new Site("SITE001", "Palais des Sports", 2.35f, 48.85f);
        Sport sport = new Sport("SP001", "Handball");
        Journee journee = new Journee(3, 6, 2025); // 3 juin 2025
        SiteDAO siteDAO = new SiteDAO();
        SportDAO sportDAO = new SportDAO();
        JourneeDAO journeeDAO = new JourneeDAO();

        siteDAO.create(site);
        sportDAO.create(sport);
        journeeDAO.create(journee);

        DPS dps = new DPS(0, java.sql.Time.valueOf("14:00:00"), java.sql.Time.valueOf("18:00:00"), site, sport, journee);
        dpsDAO.create(dps);

        // Création d’un besoin
        System.out.println("========== TEST CREATE ==========");
        Besoin besoin = new Besoin(dps, comp, 4);
        int result = besoinDAO.create(besoin);
        if (result > 0) {
            System.out.println("Création réussie");
        } else {
            System.out.println("Échec de la création.");
        }

        // Recherche par ID
        System.out.println("\n========== TEST FIND BY ID ==========");
        Besoin retrieved = besoinDAO.findByID(dps.getId(), comp.getIntitule());
        if (retrieved != null) {
            System.out.println("Besoin trouvé : " + retrieved);
        } else {
            System.out.println("Aucun besoin trouvé avec la clé composée.");
        }

        // Mise à jour
        System.out.println("\n========== TEST UPDATE ==========");
        besoin.setNombre(6);
        besoinDAO.update(besoin);
        Besoin updated = besoinDAO.findByID(dps.getId(), comp.getIntitule());
        if (updated != null && updated.getNombre() == 6) {
            System.out.println("Mise à jour réussie : " + updated);
        } else {
            System.out.println("Échec de la mise à jour.");
        }

        // Récupération de tous les besoins
        System.out.println("\n========== TEST FIND ALL ==========");
        List<Besoin> all = besoinDAO.findAll();
        System.out.println("Nombre total de besoins : " + all.size());
        for (Besoin b : all) {
            System.out.println(b);
        }

        // Suppression
        System.out.println("\n========== TEST DELETE ==========");
        besoinDAO.delete(besoin);
        Besoin deleted = besoinDAO.findByID(dps.getId(), comp.getIntitule());
        if (deleted == null) {
            System.out.println("Suppression réussie.");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
}
