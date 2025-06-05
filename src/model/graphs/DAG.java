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
        prerequis.put("PSC1", new ArrayList<>());
        prerequis.put("PSE1", Arrays.asList("PSC1"));
        prerequis.put("PSE2", Arrays.asList("PSE1"));
        prerequis.put("PAE", Arrays.asList("PSE2"));
        prerequis.put("SECOURISME_MONTAGNE", Arrays.asList("PSE1"));
        prerequis.put("SECOURISME_AQUATIQUE", Arrays.asList("PSE1"));
    }
    
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
    
    private boolean detecterCycle(String competence, Set<String> blanc, Set<String> gris, Set<String> noir) {
        blanc.remove(competence);
        gris.add(competence);
        
        List<String> prereq = prerequis.get(competence);
        if (prereq != null) {
            for (String dep : prereq) {
                if (gris.contains(dep)) {
                    return true;
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
    
    public boolean ajouterCompetence(String nouvelleComp, List<String> nouveauxPrereq) {
        Map<String, List<String>> backup = new HashMap<>(prerequis);
        prerequis.put(nouvelleComp, nouveauxPrereq);
        
        if (verifierDAG()) {
            return true;
        } else {
            prerequis = backup;
            return false;
        }
    }
    
    public boolean possederCompetence(Secouriste secouriste, String competenceRequise) {
        if (secouriste.getCompetences() == null) {
            secouriste.setCompetences(new ArrayList<>());
            return false;
        }
        
        for (Competence c : secouriste.getCompetences()) {
            if (c.getIntitule().equals(competenceRequise)) {
                return true;
            }
            if (implique(c.getIntitule(), competenceRequise)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean implique(String competencePossedee, String competenceRequise) {
        List<String> prereq = prerequis.get(competencePossedee);
        if (prereq == null) return false;
        
        return prereq.contains(competenceRequise) || 
               prereq.stream().anyMatch(p -> implique(p, competenceRequise));
    }
    
    public Map<String, List<String>> getPrerequisGraphe() {
        return new HashMap<>(prerequis);
    }
}