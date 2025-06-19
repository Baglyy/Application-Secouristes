import model.data.*;
import model.dao.*;

import model.LoginModel;
import model.AdminDashboardModel;
import model.AdminDispositifsModel; 

import java.sql.SQLException; 
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Cette classe simule un scénario complet de test pour l'application SecuOptix.
 * Elle effectue une connexion en tant qu'administrateur, accède au tableau de bord,
 * navigue vers la gestion des dispositifs (DPS), saisit manuellement un nouveau DPS 
 * avec ses besoins, puis tente d'enregistrer ces données dans la base via les DAO.
 * <p>
 * Ce scénario est utile pour valider l'intégration entre les différentes couches : 
 * modèle, DAO, et logique métier côté administration.
 * </p>
 */
public class Scenario {

    /**
     * Point d'entrée principal du scénario de test.
     * 
     * @param args Arguments passés en ligne de commande (non utilisés ici).
     */
    public static void main(String[] args) {
        System.out.println("--- DÉBUT DU SCÉNARIO COMPLET : Connexion, Navigation et Création DPS ---");

        // --- Initialisation des DAOs ---
        SiteDAO siteDAO = new SiteDAO();
        SportDAO sportDAO = new SportDAO();
        JourneeDAO journeeDAO = new JourneeDAO();
        CompetenceDAO competenceDAO = new CompetenceDAO();
        DPSDAO dpsDAO = new DPSDAO();
        BesoinDAO besoinDAO = new BesoinDAO();

        // --- ÉTAPE 1: Connexion de l'Administrateur ---
        System.out.println("\n[ÉTAPE 1] Tentative de Connexion Administrateur...");
        LoginModel loginModel = new LoginModel();
        boolean connexionReussie = loginModel.validerAdminPourTest("admin", "JO2030"); 

        String utilisateurConnecte = null;
        if (connexionReussie) {
            utilisateurConnecte = loginModel.getUtilisateurConnectePourTest(); // MÉTHODE À AJOUTER/ADAPTER DANS LoginModel
            System.out.println("  Connexion réussie pour : " + utilisateurConnecte);
        } else {
            System.err.println("  ÉCHEC de la connexion. Scénario arrêté.");
            return;
        }

        // --- ÉTAPE 2: Accès au Tableau de Bord Administrateur ---
        // Normalement, LoginController instancierait AdminDashboardView, qui instancierait AdminDashboardController, etc.
        // Nous simulons que le contrôleur du dashboard est actif et a son modèle.
        System.out.println("\n[ÉTAPE 2] Accès au Tableau de Bord Administrateur...");
        AdminDashboardModel adminDashboardModel = new AdminDashboardModel(utilisateurConnecte);
        System.out.println("  Tableau de bord chargé pour : " + adminDashboardModel.getNomUtilisateur());
        // Ici, l'admin verrait les options du dashboard.

        // --- ÉTAPE 3: Navigation vers la Gestion des Dispositifs ---
        // L'admin clique sur un bouton "Gérer les Dispositifs".
        // AdminDashboardController instancierait AdminDispositifsView, qui instancierait AdminDispositifsController,
        // qui lui-même instancierait AdminDispositifsModel.
        System.out.println("\n[ÉTAPE 3] Navigation vers la Gestion des Dispositifs...");
        AdminDispositifsModel adminDispositifsModel = new AdminDispositifsModel(utilisateurConnecte);
        System.out.println("  Module de gestion des dispositifs (DPS) chargé.");
        // L'admin est maintenant sur l'écran pour ajouter/modifier/supprimer des DPS.

        // --- ÉTAPE 4: L'Administrateur initie la création d'un nouveau DPS ---
        System.out.println("\n[ÉTAPE 4] L'Administrateur saisit les informations pour un nouveau DPS...");
        long nouveauDpsId = 704L; 
        Time horaireDepart = Time.valueOf("08:00:00");
        Time horaireFin = Time.valueOf("16:30:00");
        String codeSiteExistant = "STADMAJ";
        String codeSportExistant = "ATHLFIN";
        Journee journeePourDPS = new Journee(10, 7, 2024); 

        List<Map<String, Object>> donneesBesoinsSaisis = new ArrayList<>();
        Map<String, Object> besoin1Saisi = new HashMap<>();
        besoin1Saisi.put("intituleCompetence", "PSE2"); 
        besoin1Saisi.put("nombre", 1);
        donneesBesoinsSaisis.add(besoin1Saisi);

        Map<String, Object> besoin2Saisi = new HashMap<>();
        besoin2Saisi.put("intituleCompetence", "Chef de Poste");
        besoin2Saisi.put("nombre", 1);
        donneesBesoinsSaisis.add(besoin2Saisi);

        System.out.println("  Données pour DPS ID " + nouveauDpsId + " : Site=" + codeSiteExistant + ", Sport=" + codeSportExistant + ", Journee=" + journeePourDPS.getJour()+"/"+journeePourDPS.getMois()+"/"+journeePourDPS.getAnnee());
        System.out.print("    Besoins saisis: ");
        for (int i = 0; i < donneesBesoinsSaisis.size(); i++) {
            Map<String, Object> bData = donneesBesoinsSaisis.get(i);
            System.out.print(bData.get("nombre") + " x " + bData.get("intituleCompetence"));
            if (i < donneesBesoinsSaisis.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();

        // --- ÉTAPE 5: Appel à la logique de création (simule l'appel du Contrôleur au Modèle) ---
        System.out.println("\n[ÉTAPE 5] Tentative de création du DPS et de ses besoins...");
        boolean creationGlobaleReussie = false;
        DPS dpsCreeEnMemoire = null;

        try {
            Site site = siteDAO.findByID(codeSiteExistant);
            Sport sport = sportDAO.findByID(codeSportExistant);
            Journee journee = journeeDAO.findByID(journeePourDPS.getJour(), journeePourDPS.getMois(), journeePourDPS.getAnnee());

            if (site != null && sport != null && journee != null) {
                dpsCreeEnMemoire = new DPS(nouveauDpsId, horaireDepart, horaireFin, site, sport, journee);
                System.out.println("    Insertion du DPS principal ID: " + dpsCreeEnMemoire.getId() + "...");
                int dpsResult = dpsDAO.create(dpsCreeEnMemoire); 

                if (dpsResult > 0) {
                    System.out.println("    DPS principal inséré avec succès.");
                    boolean tousBesoinsTraitesAvecSucces = true; // Devient false si un besoin échoue

                    for (Map<String, Object> dataBesoin : donneesBesoinsSaisis) {
                        String intituleComp = (String) dataBesoin.get("intituleCompetence");
                        Integer nombre = (Integer) dataBesoin.get("nombre");
                        boolean ceBesoinEstCree = false;

                        if (intituleComp != null && nombre != null && nombre > 0) {
                            Competence competencePourBesoin = competenceDAO.findByID(intituleComp);
                            if (competencePourBesoin != null) {
                                Besoin besoin = new Besoin(dpsCreeEnMemoire, competencePourBesoin, nombre);
                                System.out.println("      Insertion du Besoin: " + nombre + "x " + intituleComp + " pour DPS ID " + dpsCreeEnMemoire.getId());
                                int besoinResult = besoinDAO.create(besoin);
                                if (besoinResult > 0) {
                                    dpsCreeEnMemoire.getBesoins().add(besoin); // Mettre à jour l'objet en mémoire
                                    ceBesoinEstCree = true;
                                } else {
                                    System.err.println("      ERREUR: Échec de l'insertion du besoin '" + intituleComp + "'.");
                                    tousBesoinsTraitesAvecSucces = false;
                                }
                            } else {
                                System.err.println("      ERREUR: Compétence '" + intituleComp + "' non trouvée pour un besoin. Ce besoin n'est pas créé.");
                                tousBesoinsTraitesAvecSucces = false;
                            }
                        } else {
                             System.err.println("      ERREUR: Données de besoin invalides (intitulé ou nombre). Besoin ignoré.");
                             tousBesoinsTraitesAvecSucces = false;
                        }
                    } 

                    creationGlobaleReussie = tousBesoinsTraitesAvecSucces;

                } else {
                    System.err.println("  ERREUR: Échec de l'insertion du DPS principal.");
                }
            } else {
                 System.err.println("  ERREUR: Site, Sport ou Journée de référence non trouvé(e)(s) en BDD lors de la préparation du DPS. Création annulée.");
            }
        } catch (Exception e) {
            System.err.println("  ERREUR Exception majeure pendant la création: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n--- FIN DU SCÉNARIO ---");
    }
}
