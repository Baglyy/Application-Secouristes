package test;

import model.data.Journee;

/**
 * Classe de test pour la classe {@link Journee}.
 * Vérifie la création correcte d'objets Journee dans des cas normaux, erronés et limites.
 */
public class TestJournee {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            // Cas classique : 15 juin 2025
            Journee j1 = new Journee(15, 6, 2025);
            System.out.println("Création OK : " + j1);

            // Cas bissextile : 29 février 2028
            Journee j2 = new Journee(29, 2, 2028);
            System.out.println("Création OK : " + j2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");

        try {
            // Erreur : février non bissextile n'a pas 30 jours
            Journee err1 = new Journee(30, 2, 2023);
        } catch (Exception e) {
            System.out.println("Erreur attendue (février non bissextile) : " + e.getMessage());
        }

        try {
            // Erreur : avril n’a que 30 jours
            Journee err2 = new Journee(31, 4, 2025);
        } catch (Exception e) {
            System.out.println("Erreur attendue (mois à 30 jours) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");

        try {
            // Borne inférieure autorisée
            Journee limite1 = new Journee(1, 1, 2025);
            System.out.println("Limite basse OK : " + limite1);

            // Borne supérieure autorisée
            Journee limite2 = new Journee(31, 12, 2100);
            System.out.println("Limite haute OK : " + limite2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
