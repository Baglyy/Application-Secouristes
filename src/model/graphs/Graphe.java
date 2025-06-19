package model.graphs;

import model.data.*;
import java.util.*;

/**
 * Classe représentant un graphe utilisé pour l'affectation de secouristes à des dispositifs (DPS).
 * Contient deux algorithmes d'affectation : un exhaustif (backtracking) et un glouton.
 */
public class Graphe {
    private DAG dag;

    /**
     * Constructeur du graphe prenant en paramètre un DAG représentant les relations de compétences.
     * @param dag le DAG des compétences
     */
    public Graphe(DAG dag) {
        this.dag = dag;
    }

    //============================================================ AFFECTATION EXHAUSTIVE ============================================================\\

    /**
     * Algorithme exhaustif (backtracking) pour trouver la meilleure affectation possible
     * des secouristes aux dispositifs selon leurs compétences.
     * @param secouristes la liste de tous les secouristes
     * @param dps la liste des dispositifs à couvrir
     * @return une map DPS → Liste des secouristes affectés
     */
    public Map<DPS, List<Secouriste>> affectationExhaustive(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> meilleureAffectation = new HashMap<>();
        int[] meilleurScore = {-1};

        Map<DPS, List<Secouriste>> affectationCourante = new HashMap<>();
        for (DPS d : dps) {
            affectationCourante.put(d, new ArrayList<>());
        }

        Set<Secouriste> dejaAffectesGlobal = new HashSet<>();

        backtrack(dps, 0, secouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);
        return meilleureAffectation;
    }

    /**
     * Méthode récursive de backtracking pour explorer toutes les affectations possibles.
     */
    private void backtrack(List<DPS> dps, int index, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                           Map<DPS, List<Secouriste>> affectationCourante, Map<DPS, List<Secouriste>> meilleureAffectation,
                           int[] meilleurScore) {

        if (index == dps.size()) {
            int scoreActuel = evaluerAffectation(affectationCourante);
            if (scoreActuel > meilleurScore[0]) {
                meilleurScore[0] = scoreActuel;
                meilleureAffectation.clear();
                for (DPS dpsKey : affectationCourante.keySet()) {
                    meilleureAffectation.put(dpsKey, new ArrayList<>(affectationCourante.get(dpsKey)));
                }
            }
            return;
        }

        DPS dpsActuel = dps.get(index);
        List<List<Secouriste>> toutesLesCombinaisons = genererCombinaisonsOptimalesPourDPS(dpsActuel, tousLesSecouristes, dejaAffectesGlobal);

        if (toutesLesCombinaisons.isEmpty()) {
            affectationCourante.put(dpsActuel, new ArrayList<>());
            backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);
        }

        for (List<Secouriste> candidats : toutesLesCombinaisons) {
            if (candidats.stream().noneMatch(dejaAffectesGlobal::contains)) {
                affectationCourante.put(dpsActuel, new ArrayList<>(candidats));
                dejaAffectesGlobal.addAll(candidats);

                backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);

                dejaAffectesGlobal.removeAll(candidats);
                affectationCourante.put(dpsActuel, new ArrayList<>());
            }
        }
    }

    /**
     * Génère toutes les combinaisons valides de secouristes pour couvrir les besoins d’un DPS.
     */
    private List<List<Secouriste>> genererCombinaisonsOptimalesPourDPS(DPS dps, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal) {
        List<List<Secouriste>> resultats = new ArrayList<>();
        genererCombinaisonsRec(dps.getBesoins(), 0, tousLesSecouristes, dejaAffectesGlobal, new ArrayList<>(), new HashSet<>(), resultats);
        return resultats;
    }

    /**
     * Méthode récursive pour générer toutes les combinaisons de secouristes répondant aux besoins.
     */
    private void genererCombinaisonsRec(List<Besoin> besoins, int besoinIndex,
                                        List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                                        List<Secouriste> currentAssignment, Set<Secouriste> tempUsed,
                                        List<List<Secouriste>> resultats) {

        if (besoinIndex == besoins.size()) {
            resultats.add(new ArrayList<>(currentAssignment));
            return;
        }

        Besoin besoin = besoins.get(besoinIndex);
        String comp = besoin.getCompetence().getIntitule();
        int nb = besoin.getNombre();

        if (nb > 0) {
            List<Secouriste> candidats = new ArrayList<>();
            for (Secouriste s : tousLesSecouristes) {
                if (!dejaAffectesGlobal.contains(s) && !tempUsed.contains(s) && dag.possederCompetence(s, comp)) {
                    candidats.add(s);
                }
            }

            List<List<Secouriste>> combinaisons = new ArrayList<>();
            genererCombinaisonsSimples(candidats, nb, 0, new ArrayList<>(), combinaisons);

            for (List<Secouriste> choix : combinaisons) {
                currentAssignment.addAll(choix);
                tempUsed.addAll(choix);
                genererCombinaisonsRec(besoins, besoinIndex + 1, tousLesSecouristes, dejaAffectesGlobal, currentAssignment, tempUsed, resultats);
                currentAssignment.removeAll(choix);
                tempUsed.removeAll(choix);
            }
        }

        genererCombinaisonsRec(besoins, besoinIndex + 1, tousLesSecouristes, dejaAffectesGlobal, currentAssignment, tempUsed, resultats);
    }

    /**
     * Génère toutes les combinaisons simples de k éléments à partir d’une liste donnée.
     */
    private void genererCombinaisonsSimples(List<Secouriste> liste, int k, int start,
                                            List<Secouriste> current, List<List<Secouriste>> resultats) {
        if (current.size() == k) {
            resultats.add(new ArrayList<>(current));
            return;
        }
        if (start >= liste.size()) return;

        for (int i = start; i < liste.size(); i++) {
            current.add(liste.get(i));
            genererCombinaisonsSimples(liste, k, i + 1, current, resultats);
            current.remove(current.size() - 1);
        }
    }

    //============================================================= AFFECTATION GLOUTONNE =============================================================\\

    /**
     * Algorithme glouton pour affecter rapidement les secouristes aux DPS
     * en tentant de satisfaire le plus grand nombre de besoins.
     * @param secouristes la liste de tous les secouristes
     * @param dps la liste des dispositifs
     * @return une map DPS → Liste des secouristes affectés
     */
    public Map<DPS, List<Secouriste>> affectationGloutonne(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> affectation = new HashMap<>();
        Set<Secouriste> dejaAffectes = new HashSet<>();

        List<DPS> dpsTries = new ArrayList<>(dps);
        dpsTries.sort((d1, d2) -> Integer.compare(d2.getNbSecouristesRequis(), d1.getNbSecouristesRequis()));

        for (DPS d : dpsTries) {
            List<Secouriste> affectes = new ArrayList<>();
            List<Secouriste> eligibles = new ArrayList<>();
            for (Secouriste s : secouristes) {
                if (!dejaAffectes.contains(s)) eligibles.add(s);
            }

            for (Besoin besoin : d.getBesoins()) {
                int requis = besoin.getNombre();
                String comp = besoin.getCompetence().getIntitule();
                int couverts = 0;

                for (Secouriste s : affectes) {
                    if (dag.possederCompetence(s, comp)) {
                        couverts++;
                        if (couverts >= requis) break;
                    }
                }

                int encore = requis - couverts;
                if (encore > 0) {
                    for (Secouriste s : eligibles) {
                        if (!affectes.contains(s) && dag.possederCompetence(s, comp)) {
                            affectes.add(s);
                            encore--;
                            if (encore <= 0) break;
                        }
                    }
                }
            }

            affectation.put(d, affectes);
            dejaAffectes.addAll(affectes);
        }

        return affectation;
    }

    /**
     * Évalue la qualité d'une affectation en fonction du nombre de besoins satisfaits.
     * @param affectation la map DPS → Liste des secouristes affectés
     * @return un score global
     */
    public int evaluerAffectation(Map<DPS, List<Secouriste>> affectation) {
        int score = 0;

        for (DPS dps : affectation.keySet()) {
            List<Secouriste> affectes = affectation.get(dps);
            if (affectes == null) affectes = new ArrayList<>();

            for (Besoin besoin : dps.getBesoins()) {
                String comp = besoin.getCompetence().getIntitule();
                int requis = besoin.getNombre();
                int couverts = 0;

                for (Secouriste s : affectes) {
                    if (dag.possederCompetence(s, comp)) {
                        couverts++;
                    }
                }

                score += Math.min(couverts, requis) * 10;
                if (couverts >= requis) score += 5;
            }
        }

        return score;
    }
}
