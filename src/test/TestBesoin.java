package test;

import model.data.*;
import java.sql.Time;

/**
 * Classe de test pour la classe {@link Besoin}.
 * Elle vérifie les cas normaux, les cas d'erreur et les cas limites lors de la création ou modification d'un Besoin.
 */
public class TestBesoin {
    public static void main(String[] args) {
        // Initialisation d'un DPS valide
        Time dep = Time.valueOf("09:30:00");
        Time fin = Time.valueOf("12:00:00");
        Site site = new Site("ST01", "Alpes Arena", 6.65f, 45.1f);
        Sport sport = new Sport("SKI", "Ski Alpin");
        Journee journee = new Journee(10, 2, 2026);

        DPS dps = new DPS(1L, dep, fin, site, sport, journee);
        Competence comp = new Competence("Secourisme");

        System.out.println("=== CAS NORMAUX ===");
        try {
            // Test de création avec un besoin normal
            Besoin b1 = new Besoin(dps, comp, 5);
            System.out.println("Création OK : besoin de " + b1.getNombre() + " personnes avec " + b1.getCompetence().getIntitule());

            // Test avec un nombre minimal valide
            Besoin b2 = new Besoin(dps, comp, 1);
            System.out.println("Création OK : " + b2.getNombre());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            // Cas où le DPS est null
            Besoin bErr1 = new Besoin(null, comp, 3);
        } catch (Exception e) {
            System.out.println("Erreur attendue (DPS null) : " + e.getMessage());
        }

        try {
            // Cas où la compétence est null
            Besoin bErr2 = new Besoin(dps, null, 3);
        } catch (Exception e) {
            System.out.println("Erreur attendue (Compétence null) : " + e.getMessage());
        }

        try {
            // Cas où le nombre est invalide (0)
            Besoin bErr3 = new Besoin(dps, comp, 0);
        } catch (Exception e) {
            System.out.println("Erreur attendue (nombre ≤ 0) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            // Test avec valeur limite minimale, puis modification du nombre via setter
            Besoin bLim = new Besoin(dps, comp, 1);
            bLim.setNombre(10); // test setter
            System.out.println("Modification OK : " + bLim.getNombre());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
