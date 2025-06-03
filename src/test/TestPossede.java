package test;

import model.data.*;

public class TestPossede {
    public static void main(String[] args) {

        // Préparation d'objets valides pour les tests
        Secouriste s = new Secouriste(1, "Durand", "Claire", "2001-03-25", "claire@mail.com", "0612345678", "5 rue du Lac");
        Competence c = new Competence("Logistique");

        System.out.println("=== CAS NORMAUX ===");
        try {
            Possede p1 = new Possede(s, c);
            System.out.println("Création OK : " + p1);

            Possede p2 = new Possede(s, new Competence("Premiers soins"));
            System.out.println("Création OK : " + p2);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            Possede pErr1 = new Possede(null, c);
        } catch (Exception e) {
            System.out.println("Erreur attendue (Secouriste null) : " + e.getMessage());
        }

        try {
            Possede pErr2 = new Possede(s, null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (Competence null) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            Possede p3 = new Possede(s, c);
            System.out.println("Avant modification : " + p3);
            p3.setCompetence(new Competence("Radio"));
            System.out.println("Après setCompetence : " + p3);
            p3.setSecouriste(new Secouriste(2, "Martin", "Paul", "1998-07-07", "paul@mail.com", "0699988776", "7 impasse des Lilas"));
            System.out.println("Après setSecouriste : " + p3);
        } catch (Exception e) {
            System.out.println("Erreur inattendue (limite) : " + e.getMessage());
        }
    }
}
