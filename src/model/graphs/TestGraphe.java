package model.graphs; // Assurez-vous que ce package est correct si TestGraphe est ici

import model.data.*;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

public class TestGraphe {

    public static void main(String[] args) {
        System.out.println("===== DÉBUT DES TESTS D'AFFECTATION DES ALGORITHMES =====");
        testBasique();
        testGloutonPeutEchouerExhaustifReussit(); // Renommé pour plus de clarté
        testPerformanceMemeDonnees();            // Renommé et modifié
        System.out.println("\n===== FIN DES TESTS D'AFFECTATION =====");
    }

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

        List<DPS> dpsList = Arrays.asList(dpsA, dpsB, dpsC); // Ordre de traitement pour le glouton (s'il ne trie pas les DPS)

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

        // Attente : L'exhaustif devrait trouver la solution S3->dpsA, S1->dpsB, S2->dpsC (3 postes pourvus, 3 DPS couverts).
        // Le glouton pourrait faire S1->dpsA, puis S1 n'est plus dispo pour dpsB (que seul S1 peut faire), et S2->dpsC (2 postes pourvus, 2 DPS couverts).
    }

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


        Random rand = new Random(42); // Seed pour la reproductibilité
        List<Secouriste> secouristesCommuns = new ArrayList<>();
        // Réduire le nombre de secouristes pour que le backtracking soit testable
        int NB_SECOURISTES_PERF = 8; // Essayez avec 7, 8, 9 pour voir l'impact
        for (int i = 0; i < NB_SECOURISTES_PERF; i++) {
            Set<Competence> cs = new HashSet<>();
            int nbCompParSecouriste = 1 + rand.nextInt(3); // 1 à 3 compétences par secouriste
            while (cs.size() < nbCompParSecouriste && cs.size() < competences.size()) {
                cs.add(competences.get(rand.nextInt(competences.size())));
            }
            secouristesCommuns.add(creerSecouriste(i + 1, "S" + i, "Perf", new ArrayList<>(cs)));
        }

        List<DPS> dpsCommuns = new ArrayList<>();
        Site site = new Site("PERF", "Site Perf Test", 0, 0);
        Sport sport = new Sport("SPP_T", "Test Sport Perf");
        Journee jour = new Journee(20, 6, 2025);
        int NB_DPS_PERF = 4; // Nombre de DPS
        for (int i = 0; i < NB_DPS_PERF; i++) {
            DPS dps = new DPS(200 + i, Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), site, sport, jour);
            int nbBesoinsParDps = 1 + rand.nextInt(2); // 1 ou 2 besoins par DPS
            for (int j = 0; j < nbBesoinsParDps; j++) {
                Competence c = competences.get(rand.nextInt(competences.size()));
                int nbRequisPourBesoin = 1 + rand.nextInt(2); // 1 ou 2 secouristes par besoin
                dps.ajouterBesoin(new Besoin(dps, c, nbRequisPourBesoin));
            }
            if (dps.getBesoins().isEmpty()){ // S'assurer qu'il y a au moins un besoin si nbBesoinsParDps était 0
                 dps.ajouterBesoin(new Besoin(dps, competences.get(0), 1));
            }
            dpsCommuns.add(dps);
        }

        System.out.println("Configuration pour Test Performance:");
        System.out.println("  Nombre de Secouristes: " + secouristesCommuns.size());
        System.out.println("  Nombre de DPS: " + dpsCommuns.size());


        System.out.println("---- Glouton (Test Performance) ----");
        long t1 = System.nanoTime();
        Map<DPS, List<Secouriste>> glouton = graphe.affectationGloutonne(new ArrayList<>(secouristesCommuns), new ArrayList<>(dpsCommuns));
        long t2 = System.nanoTime();
        afficherStatistiques("Glouton", "Performance", graphe, secouristesCommuns, dpsCommuns, glouton, (t2 - t1) / 1_000_000);
        afficherDetailsAffectation("Glouton - Performance", glouton);


        System.out.println("\n---- Exhaustif (Test Performance) ----");
        System.out.println("      (AVERTISSEMENT: Peut être très long avec " + NB_SECOURISTES_PERF + " secouristes et " + NB_DPS_PERF + " DPS !)");
        long t3 = System.nanoTime();
        Map<DPS, List<Secouriste>> exhaustif = graphe.affectationExhaustive(new ArrayList<>(secouristesCommuns), new ArrayList<>(dpsCommuns));
        long t4 = System.nanoTime();
        afficherStatistiques("Exhaustif", "Performance", graphe, secouristesCommuns, dpsCommuns, exhaustif, (t4 - t3) / 1_000_000);
        afficherDetailsAffectation("Exhaustif - Performance", exhaustif);
    }
}
