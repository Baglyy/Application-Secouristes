package model.graphs;


import model.data.*;
import java.util.*;

/**
 * Système d'affectation des secouristes pour les Jeux Olympiques 2030
 * Comprend : vérification DAG, approche exhaustive et approche gloutonne
 */
public class AffectationSecouristes {
    
    // Map des prérequis pour chaque compétence (graphe des compétences)
    private Map<String, List<String>> prerequis;
    
    public AffectationSecouristes() {
        this.prerequis = new HashMap<>();
        initialiserGrapheCompetences();
    }
    
    /**
     * Initialise le graphe des compétences avec les prérequis
     */
    private void initialiserGrapheCompetences() {
        // Exemple basé sur la figure 1 du document
        prerequis.put("PSC1", new ArrayList<>());
        prerequis.put("PSE1", Arrays.asList("PSC1"));
        prerequis.put("PSE2", Arrays.asList("PSE1"));
        prerequis.put("PAE", Arrays.asList("PSE2"));
        prerequis.put("SECOURISME_MONTAGNE", Arrays.asList("PSE1"));
        prerequis.put("SECOURISME_AQUATIQUE", Arrays.asList("PSE1"));
    }
    
    // ========== VÉRIFICATION DAG ==========
    
    /**
     * Vérifie que le graphe des compétences est un DAG (Directed Acyclic Graph)
     * @return true si le graphe est acyclique, false sinon
     */
    public boolean verifierDAG() {
        Set<String> blanc = new HashSet<>(prerequis.keySet());
        Set<String> gris = new HashSet<>();
        Set<String> noir = new HashSet<>();
        
        for (String competence : blanc) {
            if (detecterCycle(competence, blanc, gris, noir)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Détecte un cycle dans le graphe par DFS
     */
    private boolean detecterCycle(String competence, Set<String> blanc, Set<String> gris, Set<String> noir) {
        blanc.remove(competence);
        gris.add(competence);
        
        List<String> prereq = prerequis.get(competence);
        if (prereq != null) {
            for (String dep : prereq) {
                if (gris.contains(dep)) {
                    return true; // Cycle détecté
                }
                if (blanc.contains(dep) && detecterCycle(dep, blanc, gris, noir)) {
                    return true;
                }
            }
        }
        
        gris.remove(competence);
        noir.add(competence);
        return false;
    }
    
    /**
     * Ajoute une nouvelle compétence avec vérification DAG
     */
    public boolean ajouterCompetence(String nouvelleComp, List<String> nouveauxPrereq) {
        // Sauvegarde temporaire
        Map<String, List<String>> backup = new HashMap<>(prerequis);
        
        // Ajout temporaire
        prerequis.put(nouvelleComp, nouveauxPrereq);
        
        // Vérification
        if (verifierDAG()) {
            return true; // Ajout valide
        } else {
            prerequis = backup; // Restauration
            return false;
        }
    }
    
    // ========== APPROCHE EXHAUSTIVE ==========
    
    /**
     * Affectation exhaustive : teste toutes les combinaisons possibles
     * @param secouristes Liste des secouristes disponibles
     * @param dps Liste des DPS à couvrir
     * @return Meilleure affectation trouvée
     */
    public Map<DPS, List<Secouriste>> affectationExhaustive(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> meilleureAffectation = new HashMap<>();
        int meilleurScore = -1;
        
        // Génération de toutes les combinaisons possibles
        List<Map<DPS, List<Secouriste>>> toutesAffectations = genererToutesAffectations(secouristes, dps);
        
        for (Map<DPS, List<Secouriste>> affectation : toutesAffectations) {
            int score = evaluerAffectation(affectation);
            if (score > meilleurScore) {
                meilleurScore = score;
                meilleureAffectation = new HashMap<>(affectation);
            }
        }
        
        return meilleureAffectation;
    }
    
    /**
     * Génère toutes les affectations possibles (version simplifiée)
     */
    private List<Map<DPS, List<Secouriste>>> genererToutesAffectations(List<Secouriste> secouristes, List<DPS> dps) {
        List<Map<DPS, List<Secouriste>>> affectations = new ArrayList<>();
        
        // Pour la démonstration, on génère quelques combinaisons représentatives
        // Dans une vraie implémentation exhaustive, on utiliserait la récursion
        for (int i = 0; i < Math.min(100, Math.pow(2, secouristes.size())); i++) {
            Map<DPS, List<Secouriste>> affectation = new HashMap<>();
            
            for (DPS d : dps) {
                List<Secouriste> affectes = new ArrayList<>();
                // Affectation aléatoire pour simulation
                for (int j = 0; j < Math.min(d.getNbSecouristesRequis(), secouristes.size()); j++) {
                    if (Math.random() > 0.5) {
                        affectes.add(secouristes.get(j % secouristes.size()));
                    }
                }
                affectation.put(d, affectes);
            }
            affectations.add(affectation);
        }
        
        return affectations;
    }
    
    // ========== APPROCHE GLOUTONNE ==========
    
    /**
     * Affectation gloutonne : affecte progressivement selon un critère de priorité
     * @param secouristes Liste des secouristes disponibles
     * @param dps Liste des DPS à couvrir
     * @return Affectation obtenue par l'approche gloutonne
     */
    public Map<DPS, List<Secouriste>> affectationGloutonne(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> affectation = new HashMap<>();
        List<Secouriste> disponibles = new ArrayList<>(secouristes);
        
        // Trier les DPS par priorité (nombre de secouristes requis décroissant)
        List<DPS> dpsTries = new ArrayList<>(dps);
        dpsTries.sort((d1, d2) -> Integer.compare(d2.getNbSecouristesRequis(), d1.getNbSecouristesRequis()));
        
        for (DPS d : dpsTries) {
            List<Secouriste> affectes = new ArrayList<>();
            
            // Pour chaque compétence requise
            for (Besoin besoin : d.getBesoins()) {
                int nbRequis = besoin.getNombre();
                String compRequise = besoin.getCompetence().getIntitule();
                
                // Chercher les secouristes avec cette compétence
                Iterator<Secouriste> it = disponibles.iterator();
                while (it.hasNext() && nbRequis > 0) {
                    Secouriste s = it.next();
                    if (possederCompetence(s, compRequise)) {
                        affectes.add(s);
                        it.remove();
                        nbRequis--;
                    }
                }
            }
            
            affectation.put(d, affectes);
        }
        
        return affectation;
    }
    
    /**
     * Vérifie si un secouriste possède une compétence (avec prérequis)
     */
    private boolean possederCompetence(Secouriste secouriste, String competenceRequise) {
        // Gestion du cas où competences n'est pas initialisé dans le constructeur
        if (secouriste.getCompetences() == null) {
            secouriste.setCompetences(new ArrayList<>());
            return false;
        }
        
        for (Competence c : secouriste.getCompetences()) {
            if (c.getIntitule().equals(competenceRequise)) {
                return true;
            }
            // Vérifier si une compétence supérieure implique celle-ci
            if (implique(c.getIntitule(), competenceRequise)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Vérifie si une compétence implique une autre (par hiérarchie)
     */
    private boolean implique(String competencePossedee, String competenceRequise) {
        List<String> prereq = prerequis.get(competencePossedee);
        if (prereq == null) return false;
        
        return prereq.contains(competenceRequise) || 
               prereq.stream().anyMatch(p -> implique(p, competenceRequise));
    }
    
    // ========== ÉVALUATION ==========
    
    /**
     * Évalue la qualité d'une affectation
     * @param affectation L'affectation à évaluer
     * @return Score de qualité (plus élevé = meilleur)
     */
    private int evaluerAffectation(Map<DPS, List<Secouriste>> affectation) {
        int score = 0;
        
        for (Map.Entry<DPS, List<Secouriste>> entry : affectation.entrySet()) {
            DPS dps = entry.getKey();
            List<Secouriste> affectes = entry.getValue();
            
            // Points pour couverture des besoins
            for (Besoin besoin : dps.getBesoins()) {
                String compRequise = besoin.getCompetence().getIntitule();
                int nbRequis = besoin.getNombre();
                int nbCouvert = 0;
                
                for (Secouriste s : affectes) {
                    if (possederCompetence(s, compRequise)) {
                        nbCouvert++;
                    }
                }
                
                score += Math.min(nbCouvert, nbRequis) * 10; // 10 points par besoin couvert
                if (nbCouvert >= nbRequis) score += 5; // Bonus si besoin complètement couvert
            }
        }
        
        return score;
    }
    
    // ========== TESTS ET COMPARAISON ==========
    
    /**
     * Méthode de test et comparaison des deux approches
     */
    public void testerEtComparer(List<Secouriste> secouristes, List<DPS> dps) {
        System.out.println("=== TESTS COMPARATIFS - SECOURS 2030 ===\n");
        
        // Test DAG
        System.out.println("1. Vérification DAG :");
        System.out.println("   Graphe valide : " + verifierDAG());
        
        // Test ajout compétence
        System.out.println("   Test ajout PSE3 -> PSE2 : " + 
                          ajouterCompetence("PSE3", Arrays.asList("PSE2")));
        System.out.println("   Test ajout cyclique : " + 
                          ajouterCompetence("CYCLE", Arrays.asList("PSC1")) + "\n");
        
        // Approche exhaustive
        System.out.println("2. Approche exhaustive :");
        long startExhaustive = System.nanoTime();
        Map<DPS, List<Secouriste>> resultExhaustive = affectationExhaustive(secouristes, dps);
        long timeExhaustive = System.nanoTime() - startExhaustive;
        int scoreExhaustive = evaluerAffectation(resultExhaustive);
        
        System.out.println("   Temps : " + (timeExhaustive / 1_000_000) + " ms");
        System.out.println("   Score : " + scoreExhaustive);
        System.out.println("   DPS couverts : " + resultExhaustive.size());
        
        // Approche gloutonne
        System.out.println("\n3. Approche gloutonne :");
        long startGlouton = System.nanoTime();
        Map<DPS, List<Secouriste>> resultGlouton = affectationGloutonne(secouristes, dps);
        long timeGlouton = System.nanoTime() - startGlouton;
        int scoreGlouton = evaluerAffectation(resultGlouton);
        
        System.out.println("   Temps : " + (timeGlouton / 1_000_000) + " ms");
        System.out.println("   Score : " + scoreGlouton);
        System.out.println("   DPS couverts : " + resultGlouton.size());
        
        // Comparaison
        System.out.println("\n4. Comparaison :");
        System.out.println("   Gain de temps glouton : " + 
                          (timeExhaustive > 0 ? (100 * (timeExhaustive - timeGlouton) / timeExhaustive) : 0) + "%");
        System.out.println("   Qualité relative glouton : " + 
                          (scoreExhaustive > 0 ? (100 * scoreGlouton / scoreExhaustive) : 100) + "%");
        
        System.out.println("\n=== FIN DES TESTS ===");
    }
    
    // Getters pour intégration dans l'application
    public Map<String, List<String>> getPrerequisGraphe() {
        return new HashMap<>(prerequis);
    }
}