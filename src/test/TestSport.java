package test;

import model.data.Sport;

/**
 * Classe de test pour la classe {@link Sport}.
 * Vérifie les cas normaux, les cas d’erreurs attendues et les cas limites liés aux sports d'hiver.
 */
public class TestSport {
    public static void main(String[] args) {

        System.out.println("==== CAS NORMAUX ====");
        try {
            // Création d’un sport valide
            Sport ski = new Sport("SKI", "Ski Alpin");
            System.out.println("Création OK : " + ski);

            // Modification du code et du nom du sport
            ski.setCode("SB");
            ski.setNom("Snowboard");
            System.out.println("Modification OK : " + ski);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n==== CAS ERREURS ====");
        try {
            // Erreur : code null
            new Sport(null, "Luge");
        } catch (Exception e) {
            System.out.println("Erreur attendue (code null) : " + e.getMessage());
        }

        try {
            // Erreur : nom null
            new Sport("LU", null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom null) : " + e.getMessage());
        }

        try {
            // Erreur : code vide après trim
            Sport hockey = new Sport("HK", "Hockey");
            hockey.setCode("   ");
        } catch (Exception e) {
            System.out.println("Erreur attendue (code vide) : " + e.getMessage());
        }

        try {
            // Erreur : nom vide
            Sport curling = new Sport("CRL", "Curling");
            curling.setNom("");
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom vide) : " + e.getMessage());
        }

        System.out.println("\n==== CAS LIMITES ====");
        try {
            // Cas limite : code court
            Sport codeCourt = new Sport("X", "Biathlon");
            System.out.println("Sport code court OK : " + codeCourt);

            // Cas limite : nom très long
            Sport nomLong = new Sport("BI", "Ski de fond avec tir de précision longue distance");
            System.out.println("Sport nom long OK : " + nomLong);
        } catch (Exception e) {
            System.out.println("Erreur inattendue (limites) : " + e.getMessage());
        }
    }
}
