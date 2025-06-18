package test;

import model.data.*;

import java.util.ArrayList;
import java.util.List;

public class TestSecouriste {
    public static void main(String[] args) {

        System.out.println("==== CAS NORMAUX ====");
        try {
            Secouriste s1 = new Secouriste(1, "Martin", "Lucas", "2000-01-01", "lucas@exemple.com", "0612345678", "12 rue A");
            System.out.println("Création OK : " + s1);
            System.out.println("Âge : " + s1.calculerAge());

            Competence c1 = new Competence("Chef d'équipe");
            Competence c2 = new Competence("PSC1");

            List<Competence> liste = new ArrayList<>();
            liste.add(c1);
            liste.add(c2);
            s1.setCompetences(liste);

            System.out.println("Compétences : ");
            for (Competence c : s1.getCompetences()) {
                System.out.println("- " + c);
            }

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n==== CAS ERREURS ====");
        try {
            new Secouriste(-1, "Test", "Erreur", "2000-01-01", "test@ex.com", "0000000000", "Rue X");
        } catch (Exception e) {
            System.out.println("Erreur attendue (id négatif) : " + e.getMessage());
        }

        try {
            new Secouriste(2, "", "Nom", "2000-01-01", "test@ex.com", "0000000000", "Rue X");
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom vide) : " + e.getMessage());
        }

        try {
            Secouriste s = new Secouriste(3, "Nom", "Prenom", "invalid-date", "mail@x.fr", "0612345678", "Rue");
            s.calculerAge();
        } catch (Exception e) {
            System.out.println("Erreur attendue (format date) : " + e.getMessage());
        }

        try {
            Secouriste s = new Secouriste(4, "Nom", "Prenom", "2000-01-01", "mail@x.fr", "0612345678", "Rue");
            s.setNom(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom null) : " + e.getMessage());
        }

        System.out.println("\n==== CAS LIMITES ====");
        try {
            Secouriste jeune = new Secouriste(5, "Bebe", "Test", "2024-06-01", "bebe@x.fr", "0611223344", "Rue bébé");
            System.out.println("Âge (limite) : " + jeune.calculerAge() + " an(s)");
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
