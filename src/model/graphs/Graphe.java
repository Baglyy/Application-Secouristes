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
        int[] meilleurScore = {-1}; // Utilisation d'un tableau pour passer l'entier par référence

        // Initialisation de l'affectation courante avec des listes vides pour chaque DPS
        Map<DPS, List<Secouriste>> affectationCourante = new HashMap<>();
        for (DPS d : dps) {
            affectationCourante.put(d, new ArrayList<>());
        }

        // Le Set des secouristes déjà affectés temporairement pour éviter les doublons globaux
        Set<Secouriste> dejaAffectesGlobal = new HashSet<>();

        // Appel de la méthode de backtracking récursive
        backtrack(dps, 0, secouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);

        return meilleureAffectation;
    }

    private void backtrack(List<DPS> dps, int index, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                             Map<DPS, List<Secouriste>> affectationCourante, Map<DPS, List<Secouriste>> meilleureAffectation,
                             int[] meilleurScore) {

        // Cas de base : tous les DPS ont été traités
        if (index == dps.size()) {
            int scoreActuel = evaluerAffectation(affectationCourante);
            if (scoreActuel > meilleurScore[0]) {
                meilleurScore[0] = scoreActuel;
                // Copie profonde de l'affectation courante si elle est meilleure
                meilleureAffectation.clear();
                for (Map.Entry<DPS, List<Secouriste>> entry : affectationCourante.entrySet()) {
                    meilleureAffectation.put(entry.getKey(), new ArrayList<>(entry.getValue()));
                }
            }
            return; // Termine cette branche du backtracking
        }

        DPS dpsActuel = dps.get(index);

        // 1. Générer des combinaisons de secouristes pour le DPS actuel
        // On passe la liste de TOUS les secouristes et le set des déjà affectés globalement
        // La méthode genererCombinaisons améliorée filtrera ceux qui sont déjà pris.
        List<List<Secouriste>> toutesLesCombinaisonsPourCeDPS =
                genererCombinaisonsOptimalesPourDPS(dpsActuel, tousLesSecouristes, dejaAffectesGlobal);

        // Permettre le cas où aucun secouriste n'est affecté à ce DPS
        // Cela est essentiel pour ne pas bloquer si aucune combinaison complète n'est possible
        if (toutesLesCombinaisonsPourCeDPS.isEmpty()) {
             // Si aucune combinaison valide n'est trouvée pour ce DPS,
             // nous continuons avec une liste vide pour ce DPS et passons au suivant.
            affectationCourante.put(dpsActuel, new ArrayList<>()); // S'assure que le DPS est dans la map
            backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);
        }

        // 2. Itérer sur chaque combinaison possible pour le DPS actuel
        for (List<Secouriste> candidatsPourCeDPS : toutesLesCombinaisonsPourCeDPS) {
            // Vérifier si ces candidats n'ont pas déjà été affectés globalement
            boolean canAssign = true;
            for (Secouriste s : candidatsPourCeDPS) {
                if (dejaAffectesGlobal.contains(s)) {
                    canAssign = false;
                    break;
                }
            }

            if (canAssign) {
                // Affecter les secouristes au DPS actuel
                affectationCourante.put(dpsActuel, new ArrayList<>(candidatsPourCeDPS));
                dejaAffectesGlobal.addAll(candidatsPourCeDPS); // Marquer comme affectés globalement

                // Appel récursif pour le DPS suivant
                backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);

                // Backtrack : annuler l'affectation pour explorer d'autres chemins
                dejaAffectesGlobal.removeAll(candidatsPourCeDPS);
                affectationCourante.put(dpsActuel, new ArrayList<>()); // Réinitialiser pour le prochain essai
            }
        }
    }


    // Nouvelle méthode pour générer des combinaisons de secouristes pour UN SEUL DPS,
    // en tenant compte des besoins de ce DPS et des secouristes déjà affectés globalement.
    private List<List<Secouriste>> genererCombinaisonsOptimalesPourDPS(DPS dps, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal) {
        List<List<Secouriste>> resultatsCombinaisons = new ArrayList<>();
        List<Besoin> besoins = dps.getBesoins();

        // Cette méthode doit maintenant gérer la complexité de trouver des secouristes uniques
        // qui satisfont les besoins multiples d'un seul DPS.
        // C'est la partie la plus complexe de l'algorithme exhaustif.
        // Nous allons utiliser une approche récursive pour construire les combinaisons de secouristes.

        // Appeler une fonction auxiliaire de backtracking pour construire les combinaisons pour ce DPS
        genererCombinaisonsRec(besoins, 0, tousLesSecouristes, dejaAffectesGlobal, new ArrayList<>(), new HashSet<>(), resultatsCombinaisons);

        return resultatsCombinaisons;
    }

    // Fonction auxiliaire pour générer toutes les combinaisons VALIDES de secouristes
    // pour les besoins d'un SEUL DPS.
    private void genererCombinaisonsRec(List<Besoin> besoins, int besoinIndex,
                                         List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                                         List<Secouriste> currentDPSAssignment, Set<Secouriste> currentDPSTempUsed,
                                         List<List<Secouriste>> resultats) {

        // Cas de base : tous les besoins du DPS actuel ont été traités
        if (besoinIndex == besoins.size()) {
            resultats.add(new ArrayList<>(currentDPSAssignment));
            return;
        }

        Besoin besoinActuel = besoins.get(besoinIndex);
        String compRequise = besoinActuel.getCompetence().getIntitule();
        int nbRequis = besoinActuel.getNombre();

        // Trouver tous les secouristes disponibles pour ce besoin (non affectés globalement ou dans ce DPS)
        List<Secouriste> candidatsPourCeBesoin = new ArrayList<>();
        for (Secouriste s : tousLesSecouristes) {
            if (!dejaAffectesGlobal.contains(s) && !currentDPSTempUsed.contains(s) && dag.possederCompetence(s, compRequise)) {
                candidatsPourCeBesoin.add(s);
            }
        }

        // Si pas assez de candidats pour satisfaire le besoin, on ne peut pas continuer cette branche.
        // Ou si aucun secouriste n'est disponible et le besoin est de 1 ou plus, on ne peut pas satisfaire ce besoin.
        if (candidatsPourCeBesoin.size() < nbRequis) {
            // Si on ne peut pas couvrir le besoin, on ne peut pas former cette combinaison.
            // On ne peut pas simplement retourner ici comme avant, car cela bloquerait la recherche
            // de la MEILLEURE affectation. Il faut permettre d'explorer des chemins où ce besoin
            // n'est pas entièrement couvert, si d'autres combinaisons sont possibles plus tard.
            // Cependant, pour l'approche "exhaustive" qui cherche à couvrir au mieux,
            // si un besoin MINIMUM n'est pas rempli, cette branche ne sera pas optimale.
            // Pour l'instant, nous supposons qu'il faut au moins le nombre requis pour le besoin spécifique.
            // Si vous voulez des affectations partielles, il faudrait modifier la logique pour
            // choisir le minimum de secouristes disponibles jusqu'à nbRequis.
            // Pour le moment, nous allons simplement ne rien ajouter à `resultats` si le besoin minimum n'est pas couvert.
            return; // Cette branche n'est pas viable pour ce besoin spécifique.
        }

        // Générer toutes les combinaisons de 'nbRequis' secouristes parmi les candidats
        List<List<Secouriste>> combinaisonsSimples = new ArrayList<>();
        genererCombinaisonsSimples(candidatsPourCeBesoin, nbRequis, 0, new ArrayList<>(), combinaisonsSimples);

        // Pour chaque combinaison simple trouvée pour ce besoin...
        for (List<Secouriste> choix : combinaisonsSimples) {
            currentDPSAssignment.addAll(choix);
            currentDPSTempUsed.addAll(choix); // Marquer ces secouristes comme utilisés pour ce DPS

            // Appel récursif pour le besoin suivant
            genererCombinaisonsRec(besoins, besoinIndex + 1, tousLesSecouristes, dejaAffectesGlobal,
                                    currentDPSAssignment, currentDPSTempUsed, resultats);

            // Backtrack : retirer les secouristes pour explorer d'autres choix
            currentDPSAssignment.removeAll(choix);
            currentDPSTempUsed.removeAll(choix);
        }
    }


    // Cette méthode reste inchangée, elle génère des combinaisons sans contrainte de compétence
    private void genererCombinaisonsSimples(List<Secouriste> liste, int k, int start,
                                            List<Secouriste> current, List<List<Secouriste>> resultats) {
        if (current.size() == k) {
            resultats.add(new ArrayList<>(current));
            return;
        }
        if (start >= liste.size()) { // Ajout d'une condition pour éviter IndexOutOfBounds si k est trop grand
            return;
        }
        for (int i = start; i < liste.size(); i++) {
            current.add(liste.get(i));
            genererCombinaisonsSimples(liste, k, i + 1, current, resultats);
            current.remove(current.size() - 1);
        }
    }


    public Map<DPS, List<Secouriste>> affectationGloutonne(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> affectation = new HashMap<>();
        Set<Secouriste> dejaAffectes = new HashSet<>();

        List<DPS> dpsTries = new ArrayList<>(dps);
        dpsTries.sort((d1, d2) -> Integer.compare(d2.getNbSecouristesRequis(), d1.getNbSecouristesRequis()));

        for (DPS d : dpsTries) {
            List<Secouriste> affectes = new ArrayList<>();
            // Créer une liste de secouristes disponibles pour ce DPS
            // Trier les secouristes disponibles par un critère pertinent si désiré (ex: polyvalence, score...)
            List<Secouriste> secouristesDisponiblesPourCeDPS = new ArrayList<>();
            for(Secouriste s : secouristes) {
                if (!dejaAffectes.contains(s)) {
                    secouristesDisponiblesPourCeDPS.add(s);
                }
            }

            for (Besoin besoin : d.getBesoins()) {
                int nbRequis = besoin.getNombre();
                String compRequise = besoin.getCompetence().getIntitule();

                // Itérer sur les secouristes disponibles pour ce DPS
                for (Secouriste s : secouristesDisponiblesPourCeDPS) {
                    if (nbRequis == 0) break; // Si le besoin est satisfait, passer au suivant
                    if (dag.possederCompetence(s, compRequise) && !affectes.contains(s)) {
                        // S'il possède la compétence et n'est pas déjà affecté à ce DPS
                        affectes.add(s);
                        nbRequis--;
                    }
                }
            }
            // Mettre à jour les secouristes globalement affectés après avoir traité le DPS
            for (Secouriste s : affectes) {
                dejaAffectes.add(s);
            }
            affectation.put(d, affectes);
        }

        return affectation;
    }

    public int evaluerAffectation(Map<DPS, List<Secouriste>> affectation) {
        int score = 0;

        for (Map.Entry<DPS, List<Secouriste>> entry : affectation.entrySet()) {
            DPS dps = entry.getKey();
            List<Secouriste> affectes = entry.getValue();

            // S'assurer que les secouristes affectés à ce DPS sont uniques
            Set<Secouriste> secouristesUniquesDansDPS = new HashSet<>(affectes);

            for (Besoin besoin : dps.getBesoins()) {
                String compRequise = besoin.getCompetence().getIntitule();
                int nbRequis = besoin.getNombre();
                int nbCouvert = 0;

                // Compter combien de secouristes affectés ont la compétence requise
                for (Secouriste s : secouristesUniquesDansDPS) {
                    if (dag.possederCompetence(s, compRequise)) {
                        nbCouvert++;
                    }
                }

                // Ajuster le score : chaque secouriste contribuant au besoin rapporte 10 points
                score += Math.min(nbCouvert, nbRequis) * 10;
                // Bonus si le besoin est entièrement couvert
                if (nbCouvert >= nbRequis) {
                    score += 5;
                }
            }
        }
        return score;
    }
}
