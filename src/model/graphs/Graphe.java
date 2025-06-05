package model.graphs;

import model.data.*;
import java.util.*;

public class Graphe {
    private DAG dag;

    public Graphe(DAG dag) {
        this.dag = dag;
    }

    public Map<DPS, List<Secouriste>> affectationExhaustive(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> meilleureAffectation = new HashMap<>();
        int meilleurScore = -1;
        
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
    
    private List<Map<DPS, List<Secouriste>>> genererToutesAffectations(List<Secouriste> secouristes, List<DPS> dps) {
        List<Map<DPS, List<Secouriste>>> affectations = new ArrayList<>();
        
        for (int i = 0; i < Math.min(100, Math.pow(2, secouristes.size())); i++) {
            Map<DPS, List<Secouriste>> affectation = new HashMap<>();
            
            for (DPS d : dps) {
                List<Secouriste> affectes = new ArrayList<>();
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
    
    public Map<DPS, List<Secouriste>> affectationGloutonne(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> affectation = new HashMap<>();
        List<Secouriste> disponibles = new ArrayList<>(secouristes);
        
        List<DPS> dpsTries = new ArrayList<>(dps);
        dpsTries.sort((d1, d2) -> Integer.compare(d2.getNbSecouristesRequis(), d1.getNbSecouristesRequis()));
        
        for (DPS d : dpsTries) {
            List<Secouriste> affectes = new ArrayList<>();
            
            for (Besoin besoin : d.getBesoins()) {
                int nbRequis = besoin.getNombre();
                String compRequise = besoin.getCompetence().getIntitule();
                
                Iterator<Secouriste> it = disponibles.iterator();
                while (it.hasNext() && nbRequis > 0) {
                    Secouriste s = it.next();
                    if (dag.possederCompetence(s, compRequise)) {
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
    
    private int evaluerAffectation(Map<DPS, List<Secouriste>> affectation) {
        int score = 0;
        
        for (Map.Entry<DPS, List<Secouriste>> entry : affectation.entrySet()) {
            DPS dps = entry.getKey();
            List<Secouriste> affectes = entry.getValue();
            
            for (Besoin besoin : dps.getBesoins()) {
                String compRequise = besoin.getCompetence().getIntitule();
                int nbRequis = besoin.getNombre();
                int nbCouvert = 0;
                
                for (Secouriste s : affectes) {
                    if (dag.possederCompetence(s, compRequise)) {
                        nbCouvert++;
                    }
                }
                
                score += Math.min(nbCouvert, nbRequis) * 10;
                if (nbCouvert >= nbRequis) score += 5;
            }
        }
        
        return score;
    }
}