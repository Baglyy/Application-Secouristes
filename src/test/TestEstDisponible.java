package test;

import model.data.estDisponible;
import model.data.Journee;

public class TestEstDisponible {
    public static void main(String[] args) {

        System.out.println("==== CAS NORMAUX ====");
        try {
            estDisponible dispo1 = new estDisponible(1, 15, 6, 2025);
            System.out.println("Création OK : " + dispo1);

            estDisponible dispo2 = new estDisponible(2, 1, 1, 2030);
            System.out.println("Création OK : " + dispo2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n==== CAS ERREURS ====");
        try {
            estDisponible dispoErreur1 = new estDisponible(-1, 15, 6, 2025); // ID invalide
        } catch (Exception e) {
            System.out.println("Erreur attendue (ID) : " + e.getMessage());
        }

        try {
            estDisponible dispoErreur2 = new estDisponible(1, 32, 6, 2025); // Jour invalide
        } catch (Exception e) {
            System.out.println("Erreur attendue (Jour) : " + e.getMessage());
        }

        System.out.println("\n==== CAS LIMITES ====");
        try {
            estDisponible limite1 = new estDisponible(99, 31, 12, 2100); // Borne haute valide
            System.out.println("Limite haute OK : " + limite1);

            estDisponible limite2 = new estDisponible(42, 1, 1, 2030); // Borne basse valide
            System.out.println("Limite basse OK : " + limite2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
