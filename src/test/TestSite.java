package test;

import model.data.*;

/**
 * Classe de test pour la classe {@link Site}.
 * Vérifie les cas normaux, les cas d’erreurs attendues et les cas limites liés à un site géographique.
 */
public class TestSite {
    public static void main(String[] args) {

        System.out.println("==== CAS NORMAUX ====");
        try {
            // Création d'un site valide
            Site site = new Site("S001", "Stade de Glace", 6.86f, 45.91f);
            System.out.println("Création OK : " + site);

            // Ajout d’un DPS avec un sport d’hiver
            Journee journee = new Journee(10, 2, 2026);
            Sport sport = new Sport("PA", "Patinage Artistique");
            java.sql.Time debut = java.sql.Time.valueOf("10:00:00");
            java.sql.Time fin = java.sql.Time.valueOf("14:30:00");
            DPS dps = new DPS(1, debut, fin, site, sport, journee);

            site.ajouterDPS(dps);
            System.out.println("Ajout DPS OK : " + site);

            // Modifications des attributs du site
            site.setNom("Arène de Patinage");
            site.setLatitude(46.0f);
            site.setLongitude(6.9f);
            site.setCode("S002");
            System.out.println("Modifications OK : " + site);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n==== CAS ERREURS ====");
        try {
            // Erreur : code null
            new Site(null, "Nom", 0, 0);
        } catch (Exception e) {
            System.out.println("Erreur attendue (code null) : " + e.getMessage());
        }

        try {
            // Erreur : nom vide
            new Site("C", "", 0, 0);
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom vide) : " + e.getMessage());
        }

        try {
            // Erreur : latitude hors borne
            Site site = new Site("S", "Test", 0, 0);
            site.setLatitude(-100);
        } catch (Exception e) {
            System.out.println("Erreur attendue (latitude hors borne) : " + e.getMessage());
        }

        try {
            // Erreur : longitude hors borne
            Site site = new Site("S", "Test", 0, 0);
            site.setLongitude(200);
        } catch (Exception e) {
            System.out.println("Erreur attendue (longitude hors borne) : " + e.getMessage());
        }

        try {
            // Erreur : ajout d’un DPS null
            Site site = new Site("S", "Test", 0, 0);
            site.ajouterDPS(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (ajout DPS null) : " + e.getMessage());
        }

        System.out.println("\n==== CAS LIMITES ====");
        try {
            // Création d’un site avec les coordonnées maximales
            Site extremSite = new Site("MAX", "Stade Nordique", 180, 90);
            System.out.println("Création avec coordonnées max OK : " + extremSite);

            // Modification vers les coordonnées minimales
            extremSite.setLatitude(-90);
            extremSite.setLongitude(-180);
            System.out.println("Modification avec coordonnées min OK : " + extremSite);
        } catch (Exception e) {
            System.out.println("Erreur inattendue (cas limite) : " + e.getMessage());
        }
    }
}
