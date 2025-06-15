package model.graphs;

import model.data.*;
import java.util.*;

public class DAG {
    private Map<String, List<String>> prerequis;
    
    public DAG() {
        this.prerequis = new HashMap<>();
        initialiserGrapheCompetences();
    }
    
    private void initialiserGrapheCompetences() {
        // Compétences sans prérequis
        prerequis.put("SSA", new ArrayList<>());
        prerequis.put("VPSP", new ArrayList<>());
        prerequis.put("PBF", new ArrayList<>()); 

        // Compétence avec prerequis
        prerequis.put("PSE1", Arrays.asList("SSA")); 
        prerequis.put("PSE2", Arrays.asList("PSE1", "VPSP")); 
        prerequis.put("CE", Arrays.asList("PSE2"));
        prerequis.put("CP", Arrays.asList("CE"));
        prerequis.put("CO", Arrays.asList("CP"));
        prerequis.put("PBC", Arrays.asList("PBF"));
    }
    
    public boolean verifierDAG() {
        Set<String> toutesLesCompetencesInitiales = new HashSet<>(prerequis.keySet()); // Ensemble de toutes les compétences
        Set<String> ensembleBlanc = new HashSet<>(prerequis.keySet()); // Ensemble blanc plein au départ
        Set<String> ensembleGris = new HashSet<>(); // Ensemble gris
        Set<String> ensembleNoir = new HashSet<>(); // Ensemble noir

        for (String competence : toutesLesCompetencesInitiales) { // Pour chaque compétence du graphe
            if (ensembleBlanc.contains(competence)) { // Si la compétence est blanche
                if (detecterCycle(competence, ensembleBlanc, ensembleGris, ensembleNoir)) { // Appel de detecterCycle (DFS)
                    return false; // En cas de cycle on retourne false
                }
            }
        }
        return true; 
    }
    
    private boolean detecterCycle(String competence, Set<String> blanc, Set<String> gris, Set<String> noir) {

        // MAJ des couleurs
        blanc.remove(competence);
        gris.add(competence);
        
        List<String> prereq = prerequis.get(competence); // Liste des prerequis directs de la compétence
        if (prereq != null) { // Vérifie si la compétence à des prerequis
            for (String dep : prereq) {
                if (gris.contains(dep)) { // Si un prérequis est gris, il y a un cycle
                    return true;
                }
                if (blanc.contains(dep) && detecterCycle(dep, blanc, gris, noir)) { // Si un prérequis est blanc, on appel récursivement detecterCycle
                    return true;
                }
            }
        }
        
        // MAJ des couleurs
        gris.remove(competence);
        noir.add(competence);
    
        return false;
    }
    
    public boolean ajouterCompetence(String nouvelleComp, List<String> nouveauxPrereq) {
        Map<String, List<String>> backup = new HashMap<>(prerequis); // Backup de la Map prerequis
        prerequis.put(nouvelleComp, nouveauxPrereq); // Ajoute la nouvelle compétence à la Map prerequis
        
        if (verifierDAG()) { // Vérifie s'il y a un cycle
            return true;
        } else { // Si ça crée un cycle
            prerequis = backup; // Utilisation de la backup
            return false; // Ajout invalide
        }
    }
    
    public boolean possederCompetence(Secouriste secouriste, String competenceRequise) {
        // Simple vérification mais getCompetences() ne retourne normalement jamais null
        if (secouriste.getCompetences() == null) {
            secouriste.setCompetences(new ArrayList<>());
            return false;
        }
        
        for (Competence c : secouriste.getCompetences()) {
            // Si le secouriste possède la compétence
            if (c.getIntitule().equals(competenceRequise)) {
                return true;
            }
            // Si le secouriste possède une compétence qui implique celle souhaitée
            if (implique(c.getIntitule(), competenceRequise)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean implique(String competencePossedee, String competenceRequise) {
        List<String> prereq = prerequis.get(competencePossedee);
        if (prereq == null) return false;
        
        return prereq.contains(competenceRequise) || // Vérifie si la compétence requise est directement dans la liste des prérequis de competencePossedee
               prereq.stream().anyMatch(p -> implique(p, competenceRequise)); // Sinon vérifie récursivement si l'un des prérequis de competencePossedee implique la competenceRequise
    }
    
    public Map<String, List<String>> getPrerequisGraphe() {
        return new HashMap<>(prerequis);
    }
}
