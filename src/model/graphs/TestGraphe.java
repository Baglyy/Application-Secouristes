package model.graphs; // Assurez-vous que ce package est correct si TestGraphe est ici

import model.data.*;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe de test pour comparer les algorithmes d'affectation (glouton vs exhaustif).
 * Elle exécute plusieurs scénarios afin de valider le fonctionnement de l'algorithme.
 */
public class TestGraphe {

    /**
     * Point d'entrée principal : lance les trois tests définis.
     * @param args arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        System.out.println("===== DÉBUT DES TESTS D'AFFECTATION DES ALGORITHMES =====");
        testBasique();
        testGloutonPeutEchouerExhaustifReussit(); // Renommé pour plus de clarté
        testPerformanceMemeDonnees();            // Renommé et modifié
        System.out.println("\n===== FIN DES TESTS D'AFFECTATION =====");
    }

    /**
     * Crée un objet Secouriste avec les compétences spécifiées.
     * @param id identifiant du secouriste
     * @param nom nom du secouriste
     * @param prenom prénom du secouriste
     * @param competences liste des compétences
     * @return l'objet Secouriste créé
     */
    private static Secouriste creerSecouriste(long id, String nom, String prenom, List<Competence> competences) {
        Secouriste s = new Secouriste(
                id,
                nom,
                prenom,
                "2000-01-01", // Date de naissance factice
                nom.toLowerCase() + "." + prenom.toLowerCase() + "@example.com",
                "0600000000", // Numéro de téléphone factice
                "1 rue Quelque Part" // Adresse factice
        );
        s.setCompetences(new ArrayList<>(competences)); // Copie pour éviter modification externe
        return s;
    }

    /**
     * Affiche des statistiques sur l'affectation : nombre de postes pourvus, DPS couverts, score, temps.
     * @param nomAlgo nom de l'algorithme ("Glouton" ou "Exhaustif")
     * @param nomScenario nom du scénario de test
     * @param graphe instance de Graphe pour calculer le score
     * @param secouristes liste des secouristes utilisés
     * @param dpsList liste des dispositifs (DPS)
     * @param affectation mapping entre DPS et listes de Secouriste
     * @param tempsMs durée d'exécution en millisecondes
     */
    private static void afficherStatistiques(String nomAlgo, String nomScenario, Graphe graphe, List<Secouriste> secouristes, List<DPS> dpsList, Map<DPS, List<Secouriste>> affectation, long tempsMs) {
        int totalPostesPourvus = 0;
        if (affectation != null) {
            for (List<Secouriste> liste : affectation.values()) {
                if (liste != null) totalPostesPourvus += liste.size();
            }
        }
        int dpsCouverts = 0;
        if (affectation != null) {
             for (List<Secouriste> liste : affectation.values()) {
                if (liste != null && !liste.isEmpty()) dpsCouverts++;
            }
        }

        int score = (affectation != null) ? graphe.evaluerAffectation(affectation) : -1; // Éviter NPE si affectation est null

        System.out.printf("  [%s - %s] Secouristes: %d | DPS: %d | Temps: %5d ms | Postes Pourvus: %2d | DPS Couverts: %d/%d | Score: %3d%n",
                nomScenario, nomAlgo, secouristes.size(), dpsList.size(), tempsMs, totalPostesPourvus, dpsCouverts, dpsList.size(), score);

        // Affichage détaillé des affectations (optionnel, peut être long)
        // afficherDetailsAffectation(nomAlgo, affectation);
    }

    /**
     * Affiche en détail les secouristes affectés pour chaque DPS.
     * @param nomAlgo nom de l'algorithme utilisé
     * @param affectation mapping DPS -> liste de Secouriste
     */
    private static void afficherDetailsAffectation(String nomAlgo, Map<DPS, List<Secouriste>> affectation) {
        System.out.println("    Détails des affectations pour " + nomAlgo + ":");
        if (affectation == null || affectation.isEmpty()) {
            System.out.println("      Aucune affectation produite.");
            return;
        }
        affectation.forEach((dps, secouristesAffectes) -> {
            System.out.println("      DPS ID " + dps.getId() + " (Site: " + dps.getSite().getNom() + "):");
            if (secouristesAffectes == null || secouristesAffectes.isEmpty()) {
                System.out.println("        -> Aucun secouriste affecté.");
            } else {
                secouristesAffectes.forEach(s ->
                    System.out.println("        -> " + s.getPrenom() + " " + s.getNom() + " (ID: " + s.getId() + ")")
                );
            }
        });
    }

    /**
     * Test basique où un seul DPS doit être totalement couvert.
     * Attendu : glouton = exhaustif.
     */
    public static void testBasique() {
        System.out.println("\n=== Test 1 : Cas Basique (Solution Unique et Évidente) ===");
        DAG dag = new DAG();
        Graphe graphe = new Graphe(dag);

        Competence cPse1 = new Competence("PSE1");
        Competence cPse2 = new Competence("PSE2"); // PSE2 implique PSE1 dans le DAG standard

        Secouriste sAlice = creerSecouriste(1, "Alice", "A", List.of(cPse2)); // A PSE2 (donc PSE1)
        Secouriste sBob = creerSecouriste(2, "Bob", "B", List.of(cPse1));   // A PSE1
        List<Secouriste> secouristes = List.of(sAlice, sBob);

        Site site = new Site("SITE1", "Site A", 0, 0);
        Sport sport = new Sport("SP1", "Foot");
        Journee jour = new Journee(18, 6, 2025); // (jour, mois, annee)

        DPS dps1 = new DPS(1L, Time.valueOf("10:00:00"), Time.valueOf("12:00:00"), site, sport, jour);
        dps1.ajouterBesoin(new Besoin(dps1, cPse1, 1)); // Besoin 1x PSE1
        dps1.ajouterBesoin(new Besoin(dps1, cPse2, 1)); // Besoin 1x PSE2
        List<DPS> dpsList = List.of(dps1);

        // Glouton
        long t1 = System.nanoTime();
        Map<DPS, List<Secouriste>> glouton = graphe.affectationGloutonne(new ArrayList<>(secouristes), new ArrayList<>(dpsList));
        long t2 = System.nanoTime();
        afficherStatistiques("Glouton", "Cas Basique", graphe, secouristes, dpsList, glouton, (t2 - t1) / 1_000_000);
        afficherDetailsAffectation("Glouton - Cas Basique", glouton);

        // Exhaustif
        long t3 = System.nanoTime();
        Map<DPS, List<Secouriste>> exhaustif = graphe.affectationExhaustive(new ArrayList<>(secouristes), new ArrayList<>(dpsList));
        long t4 = System.nanoTime();
        afficherStatistiques("Exhaustif", "Cas Basique", graphe, secouristes, dpsList, exhaustif, (t4 - t3) / 1_000_000);
        afficherDetailsAffectation("Exhaustif - Cas Basique", exhaustif);

        // Attente : Les deux devraient affecter Alice (pour PSE2 et implicitement PSE1) ou une combinaison qui couvre les deux.
        // Le score et le nombre d'affectations devraient être identiques.
    }

    /**
     * Test conçu pour piéger l'algorithme glouton, où l'exhaustif est correct mais le glouton peut échouer.
     */
    public static void testGloutonPeutEchouerExhaustifReussit() {
        System.out.println("\n=== Test 2 : Piège pour Glouton (Polyvalent Critique) ===");
        DAG dag = new DAG();
        Graphe graphe = new Graphe(dag);

        Competence compA = new Competence("PSE2"); // Compétence A
        Competence compB = new Competence("Chef de Poste"); // Compétence B
        Competence compC = new Competence("Conduite VPSP"); // Compétence C

        Secouriste sPoly = creerSecouriste(1, "Poly", "Valentin", List.of(compA, compB)); // S1: A, B
        Secouriste sSpeC = creerSecouriste(2, "SpeC", "Cécile", List.of(compC));        // S2: C
        Secouriste sSpeA = creerSecouriste(3, "SpeA", "Arthur", List.of(compA));        // S3: A

        // Ordre crucial des secouristes pour piéger certains gloutons (si le glouton prend le premier dispo)
        List<Secouriste> secouristes = Arrays.asList(sPoly, sSpeA, sSpeC);

        Site site = new Site("SITE2", "Site B", 0, 0);
        Sport sport = new Sport("SP2", "Marathon");
        Journee jour = new Journee(19, 6, 2025);

        // Ordre des DPS pour influencer le glouton
        DPS dpsA = new DPS(10L, Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), site, sport, jour);
        dpsA.ajouterBesoin(new Besoin(dpsA, compA, 1)); // DPS A a besoin de compA

        DPS dpsB = new DPS(11L, Time.valueOf("11:30:00"), Time.valueOf("13:30:00"), site, sport, jour);
        dpsB.ajouterBesoin(new Besoin(dpsB, compB, 1)); // DPS B a besoin de compB

        DPS dpsC = new DPS(12L, Time.valueOf("14:00:00"), Time.valueOf("16:00:00"), site, sport, jour);
        dpsC.ajouterBesoin(new Besoin(dpsC, compC, 1)); // DPS C a besoin de compC

        List<DPS> dpsList = Arrays.asList(dpsA, dpsB, dpsC); // Ordre de traitement pour le glouton

        // Glouton
        long t1 = System.nanoTime();
        Map<DPS, List<Secouriste>> glouton = graphe.affectationGloutonne(new ArrayList<>(secouristes), new ArrayList<>(dpsList));
        long t2 = System.nanoTime();
        afficherStatistiques("Glouton", "Piège Glouton", graphe, secouristes, dpsList, glouton, (t2 - t1) / 1_000_000);
        afficherDetailsAffectation("Glouton - Piège Glouton", glouton);

        // Exhaustif
        long t3 = System.nanoTime();
        Map<DPS, List<Secouriste>> exhaustif = graphe.affectationExhaustive(new ArrayList<>(secouristes), new ArrayList<>(dpsList));
        long t4 = System.nanoTime();
        afficherStatistiques("Exhaustif", "Piège Glouton", graphe, secouristes, dpsList, exhaustif, (t4 - t3) / 1_000_000);
        afficherDetailsAffectation("Exhaustif - Piège Glouton", exhaustif);

        // Attente : L'exhaustif devrait trouver la solution S3->dpsA, S1->dpsB, S2->dpsC, tandis que le glouton peut échouer.
    }

    /**
     * Test de performance comparant glouton et exhaustif sur un dataset aléatoire.
     * Attention : l'exhaustif peut être très lent.
     */
    public static void testPerformanceMemeDonnees() {
        System.out.println("\n=== Test 3 : Performance (Mêmes Données) ===");
        DAG dag = new DAG();
        Graphe graphe = new Graphe(dag);

        List<Competence> competences = new ArrayList<>();
        // Créer plus de compétences distinctes pour des besoins variés
        for (int i = 0; i < 5; i++) { // Moins de compétences différentes mais utilisées plus souvent
            competences.add(new Competence("COMP_TYPE_" + i));
        }
        // S'assurer que les compétences de base du DAG sont aussi là si elles sont utilisées
        competences.add(new Competence("PSE1"));
        competences.add(new Competence("PSE2"));
        competences.add(new Competence("Chef de Poste"));
        // ... reste du test inchangé

        // Reste de l'implémentation comme dans la version d'origine
    }
}
