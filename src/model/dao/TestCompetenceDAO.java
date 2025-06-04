package model.dao;

import model.data.Competence; // Importez la classe Competence
import java.util.List; // Importez la classe List
// import model.data.Secouriste; // Cette classe n'est plus nécessaire pour tester CompetenceDAO

public class TestCompetenceDAO {
    public static void main(String[] args) {
        // Crée une instance de CompetenceDAO.
        // Assurez-vous que CompetenceDAO a un constructeur sans argument
        // ou adaptez si votre DAO nécessite une Connection passée en argument.
        // Étant donné votre classe DAO de base, un constructeur sans argument est attendu.
        CompetenceDAO dao = new CompetenceDAO();

        // --- Test de la méthode create ---
        // Crée une nouvelle compétence
        Competence c1 = new Competence("Premiers Secours"); // Assurez-vous que Competence a un constructeur avec String intitule
        System.out.println("--- Test de la création ---");
        int resultCreate = dao.create(c1);
        if (resultCreate > 0) {
            System.out.println("Compétence '" + c1.getIntitule() + "' créée avec succès.");
        } else {
            System.out.println("Échec de la création de la compétence '" + c1.getIntitule() + "'.");
        }

        // --- Test de la méthode findByID ---
        // Recherche la compétence par son intitulé (qui est sa clé primaire logique)
        System.out.println("\n--- Test de la recherche par ID (Intitulé) ---");
        // findByID dans CompetenceDAO attend Object... keys, et le premier est l'intitulé (String)
        Competence found = dao.findByID("Premiers Secours");
        if (found != null) {
            System.out.println("Compétence trouvée : " + found.getIntitule());
        } else {
            System.out.println("Aucune compétence trouvée avec l'intitulé 'Premiers Secours'.");
        }

        // --- Test de la méthode delete ---
        // Supprime la compétence
        System.out.println("\n--- Test de la suppression ---");
        // Utilisez la compétence avec le nouvel intitulé pour la suppression
        int resultDelete = dao.delete(c1);
        if (resultDelete > 0) {
            System.out.println("Compétence '" + c1.getIntitule() + "' supprimée avec succès.");
        } else {
            System.out.println("Échec de la suppression de la compétence '" + c1.getIntitule() + "'.");
        }

        // --- Test de la méthode findAll ---
        System.out.println("\n--- Test de findAll ---");
        List<Competence> allCompetences = dao.findAll();
        if (allCompetences.isEmpty()) {
            System.out.println("Aucune compétence trouvée dans la base de données.");
        } else {
            System.out.println("Liste de toutes les compétences :");
            for (Competence comp : allCompetences) {
                System.out.println("- " + comp.getIntitule());
            }
        }
    }
}
