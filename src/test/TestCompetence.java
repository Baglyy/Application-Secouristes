package test;

import model.data.Competence;

import java.util.ArrayList;
import java.util.List;

public class TestCompetence {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            Competence c1 = new Competence("Chef d'équipe");
            System.out.println("Création OK : " + c1);

            c1.setIntitule("Secourisme avancé");
            System.out.println("Modification intitulé OK : " + c1.getIntitule());

            Competence prerequis = new Competence("Secourisme de base");
            c1.ajouterPrerequis(prerequis);
            System.out.println("Ajout prérequis OK : " + c1.getPrerequis());

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            new Competence(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (intitulé null) : " + e.getMessage());
        }

        try {
            Competence c2 = new Competence("Aide");
            c2.setIntitule("  "); // vide après trim
        } catch (Exception e) {
            System.out.println("Erreur attendue (intitulé vide) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            Competence c3 = new Competence("A");
            System.out.println("Intitulé minimal OK : " + c3);

            List<Competence> prerequisList = new ArrayList<>();
            c3.setPrerequis(prerequisList);
            System.out.println("Liste vide de prérequis OK : " + c3.getPrerequis());

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
