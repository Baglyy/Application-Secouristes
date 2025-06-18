package test;

import model.data.*;
import java.sql.Time;

public class TestBesoin {
    public static void main(String[] args) {
        Time dep = Time.valueOf("09:30:00");
        Time fin = Time.valueOf("12:00:00");
        Site site = new Site("ST01", "Alpes Arena", 6.65f, 45.1f);
        Sport sport = new Sport("SKI", "Ski Alpin");
        Journee journee = new Journee(10, 2, 2026);

        DPS dps = new DPS(1L, dep, fin, site, sport, journee);
        Competence comp = new Competence("Secourisme");

        System.out.println("=== CAS NORMAUX ===");
        try {
            Besoin b1 = new Besoin(dps, comp, 5);
            System.out.println("Création OK : besoin de " + b1.getNombre() + " personnes avec " + b1.getCompetence().getIntitule());

            Besoin b2 = new Besoin(dps, comp, 1);
            System.out.println("Création OK : " + b2.getNombre());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            Besoin bErr1 = new Besoin(null, comp, 3);
        } catch (Exception e) {
            System.out.println("Erreur attendue (DPS null) : " + e.getMessage());
        }

        try {
            Besoin bErr2 = new Besoin(dps, null, 3);
        } catch (Exception e) {
            System.out.println("Erreur attendue (Compétence null) : " + e.getMessage());
        }

        try {
            Besoin bErr3 = new Besoin(dps, comp, 0);
        } catch (Exception e) {
            System.out.println("Erreur attendue (nombre ≤ 0) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            Besoin bLim = new Besoin(dps, comp, 1);
            bLim.setNombre(10); // test setter
            System.out.println("Modification OK : " + bLim.getNombre());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
