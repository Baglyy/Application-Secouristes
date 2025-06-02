package test;

import model.data.Secouriste;

public class TestSecouriste {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            Secouriste s1 = new Secouriste(1, "Dupont", "Jean", "2000-05-15", "jean@mail.com", "0612345678", "10 rue de Paris");
            System.out.println("Création OK : " + s1);
            System.out.println("Âge : " + s1.calculerAge());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        try {
            Secouriste s2 = new Secouriste(2, "Martin", "Sophie", "1995-12-01", "sophie@mail.com", "0699988776", "5 avenue Lyon");
            System.out.println("Création OK : " + s2);
            System.out.println("Âge : " + s2.calculerAge());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            Secouriste err1 = new Secouriste(0, "Durand", "Alice", "2000-01-01", "alice@mail.com", "0600000000", ""); // ID invalide
        } catch (Exception e) {
            System.out.println("Erreur attendue (ID) : " + e.getMessage());
        }

        try {
            Secouriste err2 = new Secouriste(3, "Leroy", "Max", "2000-13-01", "max@mail.com", "0600000000", "Rue X"); // Date invalide
            System.out.println("Créé ? : " + err2);
            System.out.println("Âge : " + err2.calculerAge()); // Va planter ici
        } catch (Exception e) {
            System.out.println("Erreur attendue (date) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            // Borne basse âge : 1900 → très vieux
            Secouriste limite1 = new Secouriste(4, "Old", "Man", "1900-01-01", "old@mail.com", "0600000000", "Rue des dinos");
            System.out.println("Limite basse OK : " + limite1);
            System.out.println("Âge : " + limite1.calculerAge());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        try {
            // Limite haute (juste aujourd’hui)
            String today = java.time.LocalDate.now().toString();
            Secouriste limite2 = new Secouriste(5, "New", "Born", today, "baby@mail.com", "0611223344", "Maternité");
            System.out.println("Limite haute OK : " + limite2);
            System.out.println("Âge : " + limite2.calculerAge());
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
