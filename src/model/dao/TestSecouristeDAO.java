import model.dao.SecouristeDAO;
import model.data.Secouriste;

public class TestSecouristeDAO {
    public static void main(String[] args) {
        SecouristeDAO dao = new SecouristeDAO();

        // Création d'un nouveau secouriste
        Secouriste s1 = new Secouriste(123L, "Dupont", "Jean", "1990-01-01", "jean.dupont@example.com", "0601020304", "123 rue Paris");
        int resultCreate = dao.create(s1);
        System.out.println("Résultat création : " + resultCreate);

        // Recherche par ID
        Secouriste found = dao.findByID(123L);
        if (found != null) {
            System.out.println("Secouriste trouvé : " + found.getNom() + " " + found.getPrenom());
        } else {
            System.out.println("Aucun secouriste trouvé avec l'ID 123");
        }

        // Modification
        s1.setNom("Durand");
        int resultUpdate = dao.update(s1);
        System.out.println("Résultat mise à jour : " + resultUpdate);

        // Suppression
        int resultDelete = dao.delete(s1);
        System.out.println("Résultat suppression : " + resultDelete);
    }
}
