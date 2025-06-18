package test;

import model.data.Sport;

public class TestSport {
    public static void main(String[] args) {

        System.out.println("==== CAS NORMAUX ====");
        try {
            Sport ski = new Sport("SKI", "Ski Alpin");
            System.out.println("Création OK : " + ski);

            ski.setCode("SB");
            ski.setNom("Snowboard");
            System.out.println("Modification OK : " + ski);
        } catch (Exception e) {
            System.out.println("Erreur inattendue : " + e.getMessage());
        }

        System.out.println("\n==== CAS ERREURS ====");
        try {
            new Sport(null, "Luge");
        } catch (Exception e) {
            System.out.println("Erreur attendue (code null) : " + e.getMessage());
        }

        try {
            new Sport("LU", null);
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom null) : " + e.getMessage());
        }

        try {
            Sport hockey = new Sport("HK", "Hockey");
            hockey.setCode("   ");
        } catch (Exception e) {
            System.out.println("Erreur attendue (code vide) : " + e.getMessage());
        }

        try {
            Sport curling = new Sport("CRL", "Curling");
            curling.setNom("");
        } catch (Exception e) {
            System.out.println("Erreur attendue (nom vide) : " + e.getMessage());
        }

        System.out.println("\n==== CAS LIMITES ====");
        try {
            Sport codeCourt = new Sport("X", "Biathlon");
            System.out.println("Sport code court OK : " + codeCourt);

            Sport nomLong = new Sport("BI", "Ski de fond avec tir de précision longue distance");
            System.out.println("Sport nom long OK : " + nomLong);
        } catch (Exception e) {
            System.out.println("Erreur inattendue (limites) : " + e.getMessage());
        }
    }
}
