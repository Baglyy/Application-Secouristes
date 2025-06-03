package model.dao;

import model.data.*;
import model.dao.*;

import java.sql.Time;
import java.util.List;

public class TestDPSDAO {

    public static void main(String[] args) {
        SiteDAO siteDAO = new SiteDAO();
        SportDAO sportDAO = new SportDAO();
        JourneeDAO journeeDAO = new JourneeDAO();
        DPSDAO dpsDAO = new DPSDAO();

        // Création des objets associés
        Site site = new Site("SITE001", "Palais des Sports", 2.35f, 48.85f);
        Sport sport = new Sport("SP001", "Handball");
        Journee journee = new Journee(3, 6, 2025); // 3 juin 2025

        siteDAO.create(site);
        sportDAO.create(sport);
        journeeDAO.create(journee);

        // Création d’un DPS
        System.out.println("========== TEST CREATE ==========");
        Time debut = Time.valueOf("13:30:00");
        Time fin = Time.valueOf("17:00:00");
        DPS dps = new DPS(0, debut, fin, site, sport, journee);
        int result = dpsDAO.create(dps);
        if(result > 0) {
            System.out.println("Création réussie");
        }
        if (dps != null && dps.getId() > 0) {
            System.out.println("Création réussie : " + dps);
        } else {
            System.out.println("Échec de la création.");
        }

        // Recherche par ID
        System.out.println("\n========== TEST FIND BY ID ==========");
        DPS retrieved = dpsDAO.findByID(dps.getId());
        if (retrieved != null) {
            System.out.println("DPS trouvé : " + retrieved);
        } else {
            System.out.println("Aucun DPS trouvé avec l’ID " + dps.getId());
        }

        // Mise à jour
        System.out.println("\n========== TEST UPDATE ==========");
        dps.setHoraireFin(Time.valueOf("18:00:00"));
        dpsDAO.update(dps);
        DPS updated = dpsDAO.findByID(dps.getId());
        if (updated != null && updated.getHoraireFin().equals(Time.valueOf("18:00:00"))) {
            System.out.println("Mise à jour réussie : " + updated);
        } else {
            System.out.println("Échec de la mise à jour.");
        }

        // Récupération de tous les DPS
        System.out.println("\n========== TEST FIND ALL ==========");
        List<DPS> all = dpsDAO.findAll();
        System.out.println("Nombre total de DPS : " + all.size());
        for (DPS d : all) {
            System.out.println(d);
        }

        // Suppression
        System.out.println("\n========== TEST DELETE ==========");
        dpsDAO.delete(dps);
        DPS deleted = dpsDAO.findByID(dps.getId());
        if (deleted == null) {
            System.out.println("Suppression réussie.");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
}
