package model.dao;

import model.data.Journee;

public class TestJourneeDAO {
    public static void main(String[] args) {
        JourneeDAO dao = new JourneeDAO();

        System.out.println("========== TEST CREATE ==========");
        Journee j1 = new Journee(4, 6, 2025); // 4 juin 2025
        if (dao.create(j1) > 0) {
            System.out.println("Création réussie : " + j1);
        } else {
            System.out.println("Échec de la création.");
        }

        System.out.println("\n========== TEST FIND BY ID ==========");
        Journee j2 = dao.findByID(4, 6, 2025);
        if (j2 != null) {
            System.out.println("Journée trouvée : " + j2);
        } else {
            System.out.println("Aucune journée trouvée.");
        }

        System.out.println("\n========== TEST FIND ALL ==========");
        for (Journee j : dao.findAll()) {
            System.out.println("Jour : " + j);
        }

        System.out.println("\n========== TEST DELETE ==========");
        if (dao.delete(j1) > 0) {
            System.out.println("Suppression réussie.");
        } else {
            System.out.println("Échec de la suppression.");
        }
    }
}
