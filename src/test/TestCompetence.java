package test;

import model.data.Competence;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de test pour la classe {@link Competence}.
 * Teste la création, la modification et les cas d'erreur des objets Competence.
 */
public class TestCompetence {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            // Création d'une compétence avec un intitulé valide
            Competence c1 = new Competence("Chef d'équipe");
            System.out.println("Création OK : " + c1);

            // Modification de l’intitulé
            c1.setIntitule("Secourisme avancé");
            System.out.println("Modification intitulé OK : " + c1.getIntitule());

            // Ajout de prérequis
            Competence prerequis = new Competence("Secourisme de base");
            c1.ajouterPrerequis(prerequis);
            System.out.println("Ajout prérequis OK : " + c1.getPrerequis());

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");
        try {
            // Test avec intitulé null
            new Competence(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (intitulé null) : " + e.getMessage());
        }

        try {
            // Test avec intitulé vide après trim
            Competence c2 = new Competence("Aide");
            c2.setIntitule("  ");
        } catch (Exception e) {
            System.out.println("Erreur attendue (intitulé vide) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");
        try {
            // Création avec intitulé minimal (1 caractère)
            Competence c3 = new Competence("A");
            System.out.println("Intitulé minimal OK : " + c3);

            // Définition d'une liste vide de prérequis
            List<Competence> prerequisList = new ArrayList<>();
            c3.setPrerequis(prerequisList);
            System.out.println("Liste vide de prérequis OK : " + c3.getPrerequis());

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }
    }
}
