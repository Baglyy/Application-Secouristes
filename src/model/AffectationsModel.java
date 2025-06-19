package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.AffectationDAO;
import model.dao.SecouristeDAO;
import model.data.Secouriste;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Modèle chargé de gérer les affectations des secouristes pour un utilisateur donné.
 * Il initialise l'ID du secouriste depuis la base, charge ses affectations,
 * et permet de rafraîchir ou modifier l'utilisateur courant.
 */
public class AffectationsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Affectation> affectations = FXCollections.observableArrayList();
    private final AffectationDAO affectationDAO = new AffectationDAO();
    private final SecouristeDAO secouristeDAO = new SecouristeDAO();
    private long idSecouriste = -1;
    
    /**
     * Constructeur principal.
     * @param nomUtilisateur le nom ou prénom.nom à rechercher dans la base
     */
    public AffectationsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        System.out.println("Initialisation AffectationsModel pour: " + nomUtilisateur);
        initializeSecouristeId();
        initializeAffectations();
    }
    
    /**
     * Initialise l'ID du secouriste dans la base, en essayant plusieurs formats de nom.
     * Conserve les commentaires de débogage existants.
     */
    private void initializeSecouristeId() {
        System.out.println("Recherche du secouriste avec le nom: '" + nomUtilisateur.get() + "'");
        
        Secouriste secouriste = null;
        String nomUtilisateurStr = nomUtilisateur.get().trim();
        
        // 1. D'abord par nom complet si ça contient un espace (format "PRÉNOM NOM")
        if (nomUtilisateurStr.contains(" ")) {
            System.out.println("Recherche par nom complet: " + nomUtilisateurStr);
            secouriste = secouristeDAO.findByFullName(nomUtilisateurStr);
        }
        
        // 2. Si pas trouvé, essayer par nom seul
        if (secouriste == null) {
            System.out.println("Recherche par nom seul: " + nomUtilisateurStr);
            secouriste = secouristeDAO.findByNom(nomUtilisateurStr);
        }
        
        // 3. Si toujours pas trouvé et que le nom ne contient pas d'espace, 
        //    essayer de voir si c'est un prénom et chercher dans tous les secouristes
        if (secouriste == null && !nomUtilisateurStr.contains(" ")) {
            System.out.println("Recherche par prénom dans tous les secouristes...");
            List<Secouriste> tousSecouristes = secouristeDAO.findAll();
            for (Secouriste s : tousSecouristes) {
                if (s.getPrenom().equalsIgnoreCase(nomUtilisateurStr) || 
                    s.getNom().equalsIgnoreCase(nomUtilisateurStr)) {
                    secouriste = s;
                    System.out.println("Secouriste trouvé par correspondance: " + s.getPrenom() + " " + s.getNom());
                    break;
                }
            }
        }
        
        if (secouriste != null) {
            this.idSecouriste = secouriste.getId();
            System.out.println("✓ Secouriste trouvé - ID: " + idSecouriste + ", Nom: " + secouriste.getPrenom() + " " + secouriste.getNom());
        } else {
            System.err.println("✗ Secouriste non trouvé pour nomUtilisateur: '" + nomUtilisateur.get() + "'");
            
            // Debug: lister tous les secouristes disponibles
            List<Secouriste> tousSecouristes = secouristeDAO.findAll();
            System.out.println("Secouristes disponibles en base (" + tousSecouristes.size() + "):");
            for (Secouriste s : tousSecouristes) {
                System.out.println("  - ID: " + s.getId() + ", Nom: '" + s.getNom() + "', Prénom: '" + s.getPrenom() + "'");
            }
        }
    }
    
    /**
     * Charge les affectations du secouriste courant dans l'ObservableList.
     * Tri et formatage pour affichage dans l'interface.
     * Conserve les commentaires existants.
     */
    private void initializeAffectations() {
        if (idSecouriste == -1) {
            System.err.println("Impossible de charger les affectations - ID secouriste non trouvé");
            return;
        }
        
        System.out.println("Chargement des affectations pour l'ID secouriste: " + idSecouriste);
        affectations.clear();
        
        // Fetch affectations for a reasonable range (e.g., 2024–2030)
        List<AdminAffectationsModel.Affectation> allAffectations = new ArrayList<>();
        int affectationsCount = 0;
        
        for (int year = 2024; year <= 2030; year++) {
            for (int month = 1; month <= 12; month++) {
                List<AdminAffectationsModel.Affectation> monthAffectations = 
                    affectationDAO.findAffectationsForSecoursiteAndMonth(idSecouriste, month, year);
                allAffectations.addAll(monthAffectations);
                affectationsCount += monthAffectations.size();
                
                if (!monthAffectations.isEmpty()) {
                    System.out.println("Trouvé " + monthAffectations.size() + " affectation(s) pour " + month + "/" + year);
                }
            }
        }
        
        System.out.println("Total des affectations trouvées: " + affectationsCount);
        
        if (allAffectations.isEmpty()) {
            System.out.println("Aucune affectation trouvée pour ce secouriste");
            // Vérification debug
            debugAffectationsDatabase();
            return;
        }
        
        // Sort by date (earliest first)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        allAffectations.sort((a1, a2) -> {
            try {
                LocalDate date1 = LocalDate.parse(a1.getDate(), formatter);
                LocalDate date2 = LocalDate.parse(a2.getDate(), formatter);
                return date1.compareTo(date2);
            } catch (Exception e) {
                System.err.println("Erreur de parsing de date: " + a1.getDate() + " ou " + a2.getDate());
                return 0; // Fallback if parsing fails
            }
        });
        
        // Map to model.Affectation
        for (AdminAffectationsModel.Affectation dbAffectation : allAffectations) {
            affectations.add(new Affectation(
                dbAffectation.getDate(),
                dbAffectation.getSitesOlympiques(),
                dbAffectation.getSecouristes()
            ));
        }
        
        System.out.println("Affectations ajoutées au modèle: " + affectations.size());
    }
    
    /**
     * Affiche les affectations existantes dans la base pour débogage.
     */
    private void debugAffectationsDatabase() {
        System.out.println("=== DEBUG AFFECTATIONS ===");
        try {
            List<AdminAffectationsModel.Affectation> testAffectations = 
                affectationDAO.findAllAffectations();
            
            System.out.println("Total affectations dans la base: " + testAffectations.size());
            
            String nomUtilisateurUpper = nomUtilisateur.get().toUpperCase();
            for (AdminAffectationsModel.Affectation aff : testAffectations) {
                if (aff.getSecouristes().toUpperCase().contains(nomUtilisateurUpper)) {
                    System.out.println("Affectation trouvée avec ce nom: " + aff.getDate() + " - " + aff.getSecouristes());
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors du debug: " + e.getMessage());
        }
        System.out.println("=== FIN DEBUG ===");
    }
    
    /** @return propriété observable du nom d'utilisateur */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    /** @return liste observable des affectations chargées */
    public ObservableList<Affectation> getAffectations() {
        return affectations;
    }
    
    /** @return le nom courant de l'utilisateur */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    /**
     * Change l'utilisateur courant et recharge son profil et affectations.
     * @param nomUtilisateur le nouveau nom/prénom à définir
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        System.out.println("Changement de nom utilisateur: " + nomUtilisateur);
        this.nomUtilisateur.set(nomUtilisateur);
        initializeSecouristeId();
        initializeAffectations();
    }
    
    /** Méthode pour forcer le rechargement des affectations */
    public void reloadAffectations() {
        System.out.println("Rechargement forcé des affectations");
        initializeAffectations();
    }
    
    /**
     * Représentation d'une affectation pour l'interface.
     */
    public static class Affectation {
        private final String date;
        private final String siteOlympique;
        private final String secouristes;
        
        public Affectation(String date, String siteOlympique, String secouristes) {
            this.date = date;
            this.siteOlympique = siteOlympique;
            this.secouristes = secouristes;
        }
        
        public String getDate() { return date; }
        public String getSiteOlympique() { return siteOlympique; }
        public String getSecouristes() { return secouristes; }
        
        @Override
        public String toString() {
            return "Affectation{date='" + date + "', site='" + siteOlympique + "', secouristes='" + secouristes + "'}";
        }
    }
}
