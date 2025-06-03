package test;

import model.data.*;

public class TestBesoin {
    public static void main(String[] args) {
        int[] dep = {9, 30};
        int[] fin = {12, 0};
        DPS dps = new DPS(dep, fin);

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
            Besoin bLim = new Besoin(dps, comp, 1); // borne basse valide
            bLim.setNombre(10); // test setter
            System.out.println("Modification OK : " + bLim.getNombre());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
