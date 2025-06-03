package test;

import model.data.Journee;

public class TestJournee {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            Journee j1 = new Journee(15, 6, 2025); // 15 juin
            System.out.println("Création OK : " + j1);

            Journee j2 = new Journee(29, 2, 2028); // année bissextile
            System.out.println("Création OK : " + j2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            Journee err1 = new Journee(30, 2, 2023); // février non bissextile
        } catch (Exception e) {
            System.out.println("Erreur attendue (février non bissextile) : " + e.getMessage());
        }

        try {
            Journee err2 = new Journee(31, 4, 2025); // avril a 30 jours
        } catch (Exception e) {
            System.out.println("Erreur attendue (mois à 30 jours) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            Journee limite1 = new Journee(1, 1, 2025); // année limite basse
            System.out.println("Limite basse OK : " + limite1);

            Journee limite2 = new Journee(31, 12, 2100); // année limite haute
            System.out.println("Limite haute OK : " + limite2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
