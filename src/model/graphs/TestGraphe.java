package model.graphs;

import model.data.*;
import model.graphs.DAG;
import model.graphs.Graphe;

import java.sql.Time;
import java.util.*;

public class TestGraphe {

    public static void main(String[] args) {
        testBasique();
        testGloutonEchoueMaisPasExhaustif();
        testPerformance();
    }

    private static Secouriste creerSecouriste(long id, String nom, String prenom, List<Competence> competences) {
        Secouriste s = new Secouriste(
                id,
                nom,
                prenom,
                "2000-01-01",
                nom.toLowerCase() + "." + prenom.toLowerCase() + "@mail.com",
                "0600000000",
                "1 rue de la République"
        );
        s.setCompetences(competences);
        return s;
    }

    public static void testBasique() {
        System.out.println("=== Test 1 : Cas basique ===");

        Competence c1 = new Competence("C1");

        Secouriste s1 = creerSecouriste(1, "Alice", "A", List.of(c1));
        Secouriste s2 = creerSecouriste(2, "Bob", "B", List.of(c1));
        List<Secouriste> secouristes = List.of(s1, s2);

        Site site = new Site("SITE1", "Site A", 0, 0);
        Sport sport = new Sport("SP1", "Foot");
        Journee jour = new Journee(18, 6, 2025);
        DPS dps = new DPS(1L, Time.valueOf("10:00:00"), Time.valueOf("12:00:00"), site, sport, jour);
        dps.ajouterBesoin(new Besoin(dps, c1, 1));
        List<DPS> dpsList = List.of(dps);

        Graphe graphe = new Graphe(new DAG());
        Map<DPS, List<Secouriste>> glouton = graphe.affectationGloutonne(new ArrayList<>(secouristes), dpsList);
        Map<DPS, List<Secouriste>> exhaustif = graphe.affectationExhaustive(new ArrayList<>(secouristes), dpsList);

        System.out.println("Glouton : " + glouton);
        System.out.println("Exhaustif : " + exhaustif);
    }

    public static void testGloutonEchoueMaisPasExhaustif() {
        System.out.println("\n=== Test 2 : Glouton échoue, Exhaustif réussit ===");

        Competence c1 = new Competence("C1");
        Competence c2 = new Competence("C2");

        Secouriste s1 = creerSecouriste(1, "Alice", "A", List.of(c1));
        Secouriste s2 = creerSecouriste(2, "Bob", "B", List.of(c2));
        Secouriste s3 = creerSecouriste(3, "Charlie", "C", List.of(c1, c2));

        List<Secouriste> secouristes = List.of(s3, s1, s2);

        Site site = new Site("SITE2", "Site B", 0, 0);
        Sport sport = new Sport("SP2", "Marathon");
        Journee jour = new Journee(18, 6, 2025);

        DPS dps1 = new DPS(10L, Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), site, sport, jour);
        dps1.ajouterBesoin(new Besoin(dps1, c1, 1));

        DPS dps2 = new DPS(11L, Time.valueOf("11:30:00"), Time.valueOf("13:30:00"), site, sport, jour);
        dps2.ajouterBesoin(new Besoin(dps2, c2, 1));

        List<DPS> dpsList = List.of(dps1, dps2);

        Graphe graphe = new Graphe(new DAG());
        Map<DPS, List<Secouriste>> glouton = graphe.affectationGloutonne(new ArrayList<>(secouristes), dpsList);
        Map<DPS, List<Secouriste>> exhaustif = graphe.affectationExhaustive(new ArrayList<>(secouristes), dpsList);

        System.out.println("Glouton : " + glouton);
        System.out.println("Exhaustif : " + exhaustif);
    }

    public static void testPerformance() {
        System.out.println("\n=== Test 3 : Performance ===");

        List<Competence> competences = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            competences.add(new Competence("C" + i));
        }

        Random rand = new Random(0);

        // Glouton avec 100 secouristes
        List<Secouriste> secouristesGlouton = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Set<Competence> cs = new HashSet<>();
            while (cs.size() < 3) {
                cs.add(competences.get(rand.nextInt(competences.size())));
            }
            secouristesGlouton.add(creerSecouriste(i + 1, "S" + i, "Perf", new ArrayList<>(cs)));
        }

        // 5 DPS chacun avec 2 besoins
        List<DPS> dpsList = new ArrayList<>();
        Site site = new Site("PERF", "Site Perf", 0, 0);
        Sport sport = new Sport("SPP", "TestSport");
        Journee jour = new Journee(18, 6, 2025);

        for (int i = 0; i < 5; i++) {
            DPS dps = new DPS(100 + i, Time.valueOf("08:00:00"), Time.valueOf("10:00:00"), site, sport, jour);
            for (int j = 0; j < 2; j++) {
                Competence c = competences.get(rand.nextInt(competences.size()));
                dps.ajouterBesoin(new Besoin(dps, c, 1));
            }
            dpsList.add(dps);
        }

        Graphe graphe = new Graphe(new DAG());

        System.out.println("---- Glouton ----");
        long t1 = System.currentTimeMillis();
        graphe.affectationGloutonne(new ArrayList<>(secouristesGlouton), dpsList);
        long t2 = System.currentTimeMillis();
        System.out.println("Temps Glouton (100 secouristes) : " + (t2 - t1) + " ms");

        System.out.println("---- Exhaustif (limité) ----");

        // Exhaustif avec seulement 7 secouristes
        List<Secouriste> secouristesExhaustif = secouristesGlouton.subList(0, 7);

        long t3 = System.currentTimeMillis();
        graphe.affectationExhaustive(new ArrayList<>(secouristesExhaustif), dpsList);
        long t4 = System.currentTimeMillis();
        System.out.println("Temps Exhaustif (7 secouristes) : " + (t4 - t3) + " ms");
    }

}
