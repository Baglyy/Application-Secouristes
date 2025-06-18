package test;

import model.data.*;

import java.sql.Time;

public class TestDPS {
    public static void main(String[] args) {

        System.out.println("=== CAS NORMAUX ===");
        try {
            Journee journee = new Journee(15, 6, 2025);
            Site site = new Site("S001", "Stade", 2.5f, 48.8f);
            Sport sport = new Sport("FB", "Football");

            Time dep = Time.valueOf("10:00:00");
            Time fin = Time.valueOf("12:30:00");

            DPS dps = new DPS(1, dep, fin, site, sport, journee);
            System.out.println("Création OK : " + dps);
            System.out.println("Durée (min) : " + dps.calculTemps());

            // Ajout besoin
            Competence comp = new Competence("Chef d'équipe");
            Besoin besoin = new Besoin(dps, comp, 3);
            dps.ajouterBesoin(besoin);

            System.out.println("Besoin ajouté : " + dps.getBesoins().get(0));
            System.out.println("Compétences requises : " + dps.getCompetencesRequises());
            System.out.println("Total secouristes requis : " + dps.getNbSecouristesRequis());

            // Ajout secouriste
            Secouriste s = new Secouriste(1, "Doe", "John", "2000-01-01", "john@example.com", "0601020304", "1 rue de Paris");
            dps.ajouterSecouriste(s);
            System.out.println("Secouriste affecté : " + dps.getSecouristesAffectes().get(0));

        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n=== CAS ERREURS ===");

        try {
            new DPS(2, null, Time.valueOf("12:00:00"), null, null, null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (départ null) : " + e.getMessage());
        }

        try {
            new DPS(3, Time.valueOf("13:00:00"), Time.valueOf("12:00:00"), null, null, null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (horaire fin < départ) : " + e.getMessage());
        }

        try {
            Journee j = new Journee(1, 1, 2025);
            DPS dpsErreur = new DPS(4, Time.valueOf("09:00:00"), Time.valueOf("10:00:00"), new Site("X", "Y", 0, 0), new Sport("C", "Sport"), j);
            dpsErreur.ajouterBesoin(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (besoin null) : " + e.getMessage());
        }

        try {
            Journee j = new Journee(1, 1, 2025);
            DPS dpsErreur = new DPS(5, Time.valueOf("09:00:00"), Time.valueOf("10:00:00"), new Site("X", "Y", 0, 0), new Sport("C", "Sport"), j);
            dpsErreur.ajouterSecouriste(null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (secouriste null) : " + e.getMessage());
        }

        System.out.println("\n=== CAS LIMITES ===");

        try {
            DPS dpsLimite = new DPS(6, Time.valueOf("00:00:00"), Time.valueOf("00:01:00"),
                    new Site("S", "Nom", 0f, 0f),
                    new Sport("X", "Trampo"),
                    new Journee(1, 1, 2025));
            System.out.println("DPS limite créé : " + dpsLimite + ", durée : " + dpsLimite.calculTemps() + " min");
        } catch (Exception e) {
            System.out.println("Erreur inattendue limite : " + e.getMessage());
        }
    }
}
