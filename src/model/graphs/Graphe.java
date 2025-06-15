package model.graphs;

import model.data.*;
import java.util.*;

public class Graphe {
    private DAG dag;

    public Graphe(DAG dag) {
        this.dag = dag;
    }

    // Modifié pour retourner ResultatAffectation
    public ResultatAffectation affectationExhaustive(List<Secouriste> secouristes, List<DPS> dps) {
        long startTime = System.nanoTime(); // Début de la mesure du temps

        Map<DPS, List<Secouriste>> meilleureAffectation = new HashMap<>();
        int[] meilleurScore = {-1};

        Map<DPS, List<Secouriste>> affectationCourante = new HashMap<>();
        for (DPS d : dps) {
            affectationCourante.put(d, new ArrayList<>());
        }

        Set<Secouriste> dejaAffectesGlobal = new HashSet<>(); // Pour suivre les secouristes utilisés globalement

        backtrack(dps, 0, secouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);
            
        long endTime = System.nanoTime(); // Fin de la mesure du temps
        long durationMs = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

        // Retourne un objet ResultatAffectation
        return new ResultatAffectation(meilleureAffectation, durationMs);
    }

    private void backtrack(List<DPS> dps, int index, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                             Map<DPS, List<Secouriste>> affectationCourante, Map<DPS, List<Secouriste>> meilleureAffectation,
                             int[] meilleurScore) {

        if (index == dps.size()) {
            int score = evaluerAffectation(affectationCourante);
            if (score > meilleurScore[0]) {
                meilleurScore[0] = score;
                meilleureAffectation.clear();
                for (DPS d : affectationCourante.keySet()) {
                    meilleureAffectation.put(d, new ArrayList<>(affectationCourante.get(d)));
                }
            }
            return;
        }

        DPS dpsActuel = dps.get(index);
        List<Besoin> besoins = dpsActuel.getBesoins();

        List<Secouriste> secouristesDisponiblesPourCeDPS = new ArrayList<>();
        for (Secouriste s : tousLesSecouristes) {
            if (!dejaAffectesGlobal.contains(s)) {
                secouristesDisponiblesPourCeDPS.add(s);
            }
        }

        List<List<Secouriste>> resultatsCombinaisonsPourCeDPS = new ArrayList<>();
        genererCombinaisonsPourDPS(besoins, 0, secouristesDisponiblesPourCeDPS, new ArrayList<>(), new HashSet<>(), resultatsCombinaisonsPourCeDPS);

        // Option 1: Essayer d'affecter zéro secouriste à ce DPS et passer au suivant.
        affectationCourante.put(dpsActuel, new ArrayList<>());
        backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);

        // Option 2: Essayer toutes les combinaisons trouvées pour ce DPS
        for (List<Secouriste> candidats : resultatsCombinaisonsPourCeDPS) {
            boolean allAvailable = true;
            for(Secouriste s : candidats) {
                if (dejaAffectesGlobal.contains(s)) {
                    allAvailable = false;
                    break;
                }
            }

            if (allAvailable) {
                affectationCourante.put(dpsActuel, new ArrayList<>(candidats));
                dejaAffectesGlobal.addAll(candidats);

                backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);

                dejaAffectesGlobal.removeAll(candidats);
                affectationCourante.put(dpsActuel, new ArrayList<>());
            }
        }
    }

    private void genererCombinaisonsPourDPS(List<Besoin> besoins, int besoinIndex, List<Secouriste> disponiblesPourCeDPS,
                                            List<Secouriste> currentAssignmentForDPS, Set<Secouriste> usedInCurrentDPS,
                                            List<List<Secouriste>> resultats) {
        if (besoinIndex == besoins.size()) {
            resultats.add(new ArrayList<>(currentAssignmentForDPS));
            return;
        }

        Besoin besoinActuel = besoins.get(besoinIndex);
        String compRequise = besoinActuel.getCompetence().getIntitule();
        int nbRequis = besoinActuel.getNombre();

        List<Secouriste> candidatsSpecifiquesPourCeBesoin = new ArrayList<>();
        for (Secouriste s : disponiblesPourCeDPS) {
            if (!usedInCurrentDPS.contains(s) && dag.possederCompetence(s, compRequise)) {
                candidatsSpecifiquesPourCeBesoin.add(s);
            }
        }

        for (int k = 0; k <= nbRequis; k++) {
            if (k > candidatsSpecifiquesPourCeBesoin.size()) {
                continue;
            }

            List<List<Secouriste>> combinaisonsK = new ArrayList<>();
            genererCombinaisonsSimples(candidatsSpecifiquesPourCeBesoin, k, 0, new ArrayList<>(), combinaisonsK);

            for (List<Secouriste> choixK : combinaisonsK) {
                currentAssignmentForDPS.addAll(choixK);
                usedInCurrentDPS.addAll(choixK);

                genererCombinaisonsPourDPS(besoins, besoinIndex + 1, disponiblesPourCeDPS,
                                            currentAssignmentForDPS, usedInCurrentDPS, resultats);

                currentAssignmentForDPS.removeAll(choixK);
                usedInCurrentDPS.removeAll(choixK);
            }
        }
    }

    private void genererCombinaisonsSimples(List<Secouriste> liste, int k, int start,
                                            List<Secouriste> current, List<List<Secouriste>> resultats) {
        if (current.size() == k) {
            resultats.add(new ArrayList<>(current));
            return;
        }
        if (start >= liste.size()) {
            return;
        }
        for (int i = start; i < liste.size(); i++) {
            current.add(liste.get(i));
            genererCombinaisonsSimples(liste, k, i + 1, current, resultats);
            current.remove(current.size() - 1);
        }
    }

    // Modifié pour retourner ResultatAffectation
    public ResultatAffectation affectationGloutonne(List<Secouriste> secouristes, List<DPS> dps) {
        long startTime = System.nanoTime(); // Début de la mesure du temps

        Map<DPS, List<Secouriste>> affectation = new HashMap<>();
        Set<Secouriste> dejaAffectes = new HashSet<>();

        List<DPS> dpsTries = new ArrayList<>(dps);
        dpsTries.sort((d1, d2) -> Integer.compare(d2.getNbSecouristesRequis(), d1.getNbSecouristesRequis()));

        for (DPS d : dpsTries) {
            List<Secouriste> affectes = new ArrayList<>();
            List<Secouriste> secouristesDisponiblesPourCeDPS = new ArrayList<>();
            for(Secouriste s : secouristes) {
                if (!dejaAffectes.contains(s)) {
                    secouristesDisponiblesPourCeDPS.add(s);
                }
            }

            for (Besoin besoin : d.getBesoins()) {
                int nbRequis = besoin.getNombre();
                String compRequise = besoin.getCompetence().getIntitule();

                for (Secouriste s : secouristesDisponiblesPourCeDPS) {
                    if (nbRequis > 0 && !affectes.contains(s) && dag.possederCompetence(s, compRequise)) {
                        affectes.add(s);
                        nbRequis--;
                    }
                }
            }
            for (Secouriste s : affectes) {
                dejaAffectes.add(s);
            }
            affectation.put(d, affectes);
        }

        long endTime = System.nanoTime(); // Fin de la mesure du temps
        long durationMs = (endTime - startTime) / 1_000_000; // Convertir en millisecondes

        // Retourne un objet ResultatAffectation
        return new ResultatAffectation(affectation, durationMs);
    }

    public int evaluerAffectation(Map<DPS, List<Secouriste>> affectation) {
        int score = 0;

        for (Map.Entry<DPS, List<Secouriste>> entry : affectation.entrySet()) {
            DPS dps = entry.getKey();
            List<Secouriste> affectes = entry.getValue();

            Set<Secouriste> secouristesUniquesDansDPS = new HashSet<>(affectes);

            for (Besoin besoin : dps.getBesoins()) {
                String compRequise = besoin.getCompetence().getIntitule();
                int nbRequis = besoin.getNombre();
                int nbCouvert = 0;

                for (Secouriste s : secouristesUniquesDansDPS) {
                    if (dag.possederCompetence(s, compRequise)) {
                        nbCouvert++;
                    }
                }

                score += Math.min(nbCouvert, nbRequis) * 10;
                if (nbCouvert >= nbRequis) {
                    score += 5;
                }
            }
        }
        return score;
    }
}
