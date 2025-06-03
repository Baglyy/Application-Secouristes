package test;

import model.data.Competence;

public class TestCompetence {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            Competence c1 = new Competence("Premiers secours");
            System.out.println("Création OK : " + c1);

            Competence c2 = new Competence("Logistique");
            System.out.println("Création OK : " + c2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            Competence cErr1 = new Competence(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (null) : " + e.getMessage());
        }

        try {
            Competence cErr2 = new Competence("");
        } catch (Exception e) {
            System.out.println("Erreur attendue (vide) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            Competence cLimite1 = new Competence("  Médical  "); // avec espaces
            System.out.println("Trim appliqué : " + cLimite1);
        } catch (Exception e) {
            System.out.println("Erreur inattendue (limite 1) : " + e.getMessage());
        }

        try {
            Competence cLimite2 = new Competence("Sécurité-Alpha");
            System.out.println("Intitulé spécial OK : " + cLimite2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue (limite 2) : " + e.getMessage());
        }
    }
}
