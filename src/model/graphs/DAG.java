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
        prerequis.put("SSA", new ArrayList<>());
        prerequis.put("VPSP", new ArrayList<>());
        prerequis.put("PBF", new ArrayList<>()); 

        prerequis.put("PSE1", Arrays.asList("SSA")); 
        prerequis.put("PSE2", Arrays.asList("PSE1", "VPSP")); 
        prerequis.put("CE", Arrays.asList("PSE2"));
        prerequis.put("CP", Arrays.asList("CE"));
        prerequis.put("CO", Arrays.asList("CP"));
        prerequis.put("PBC", Arrays.asList("PBF"));
    }
    
    public boolean verifierDAG() {
        Set<String> toutesLesCompetencesInitiales = new HashSet<>(prerequis.keySet()); 
        Set<String> ensembleBlanc = new HashSet<>(prerequis.keySet()); 
        Set<String> ensembleGris = new HashSet<>();
        Set<String> ensembleNoir = new HashSet<>();

        for (String competence : toutesLesCompetencesInitiales) {
            if (ensembleBlanc.contains(competence)) {
                if (detecterCycle(competence, ensembleBlanc, ensembleGris, ensembleNoir)) {
                    return false;
                }
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
