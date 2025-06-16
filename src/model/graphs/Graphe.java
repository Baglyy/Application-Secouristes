package model.graphs;

import model.data.*;
import java.util.*;

public class Graphe {
    private DAG dag;

    public Graphe(DAG dag) {
        this.dag = dag;
    }

 //============================================================ AFFECTATION EXHAUSTIVE ============================================================\\

    public Map<DPS, List<Secouriste>> affectationExhaustive(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> meilleureAffectation = new HashMap<>(); // Map pour stocker la meilleure affectation possible
        int[] meilleurScore = {-1}; // Tableau d'entiers pour stocker le meilleur score

        Map<DPS, List<Secouriste>> affectationCourante = new HashMap<>(); // Map pour stocker l'affectation en cours (backtraking)
        for (DPS d : dps) { // Pour chaque DPS
            affectationCourante.put(d, new ArrayList<>()); // Ajout d'une liste vide de secouristes
        }

        Set<Secouriste> dejaAffectesGlobal = new HashSet<>(); // Sauvegarde de l'ensemble de secouristes déjà affectés à un DPS pour éviter les doublons


        backtrack(dps, 0, secouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore); // Début du backtraking

        return meilleureAffectation; // Retourner la meilleure affectation trouvé
    }

    private void backtrack(List<DPS> dps, int index, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                             Map<DPS, List<Secouriste>> affectationCourante, Map<DPS, List<Secouriste>> meilleureAffectation,
                             int[] meilleurScore) {

        // Cas où tous les DPS ont été traités
        if (index == dps.size()) {
            int scoreActuel = evaluerAffectation(affectationCourante); // Evaluation du score
            if (scoreActuel > meilleurScore[0]) { // Si le score est meilleur que celui actuel
                meilleurScore[0] = scoreActuel; // MAJ du meilleur score
                meilleureAffectation.clear(); // Efface l'ancienne meilleure affectation 
                
                for (DPS dpsKey : affectationCourante.keySet()) { // Pour chaque DPS
                    List<Secouriste> secouristesAffectes = affectationCourante.get(dpsKey); // Récupération des secouristes affectés au DPS
                    meilleureAffectation.put(dpsKey, new ArrayList<>(secouristesAffectes)); // MAJ de la nouvelle meilleure affectation
                }
            }
            return;
        }

        DPS dpsActuel = dps.get(index); // DPS à traiter

        // Génère les combinaisons valides de secouristes pour le DPS
        List<List<Secouriste>> toutesLesCombinaisonsPourCeDPS = genererCombinaisonsOptimalesPourDPS(dpsActuel, tousLesSecouristes, dejaAffectesGlobal);

        // Cas où aucune combinaison n'est possible
        if (toutesLesCombinaisonsPourCeDPS.isEmpty()) { 
            affectationCourante.put(dpsActuel, new ArrayList<>()); // Mettre une liste vide de secouristes pour ce DPS dans l'affectation courante
            backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore); // Appel récursif pour passer au DPS suivant
        }

        for (List<Secouriste> candidatsPourCeDPS : toutesLesCombinaisonsPourCeDPS) { // Pour chaque combinaison valides 

            // Vérification si un secouriste de la combinaison n'est pas déjà affecté
            boolean estAffectable = true;
            for (Secouriste s : candidatsPourCeDPS) { // Pour chaque secouriste de la combinaison
                if (dejaAffectesGlobal.contains(s)) { // Si secouriste déjà affecté
                    estAffectable = false; // Combinaison invalide
                    break;
                }
            }

            if (estAffectable) { // Si tous les secouristes de la combinaison sont affectables
                affectationCourante.put(dpsActuel, new ArrayList<>(candidatsPourCeDPS)); // Affecte la combinaison au DPS dans l'affectation courante
                dejaAffectesGlobal.addAll(candidatsPourCeDPS); // Ajoute les secouristes à la liste des affectés

                // Appel récursif pour le DPS suivant
                backtrack(dps, index + 1, tousLesSecouristes, dejaAffectesGlobal, affectationCourante, meilleureAffectation, meilleurScore);

                // Réinitialisaiton des affectations pour essayer d'autres solutions (backtrack)
                dejaAffectesGlobal.removeAll(candidatsPourCeDPS); 
                affectationCourante.put(dpsActuel, new ArrayList<>()); 
            }
        }
    }

    private List<List<Secouriste>> genererCombinaisonsOptimalesPourDPS(DPS dps, List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal) {
        List<List<Secouriste>> resultatsCombinaisons = new ArrayList<>(); // Liste de combinaisons valides
        List<Besoin> besoins = dps.getBesoins(); // Liste des besoins du DPS (compétence et nombre requis)

        // Méthode récursive pour construire les combinaisons
        genererCombinaisonsRec(besoins, 0, tousLesSecouristes, dejaAffectesGlobal, new ArrayList<>(), new HashSet<>(), resultatsCombinaisons);

        return resultatsCombinaisons;
    }

    private void genererCombinaisonsRec(List<Besoin> besoins, int besoinIndex,
                                         List<Secouriste> tousLesSecouristes, Set<Secouriste> dejaAffectesGlobal,
                                         List<Secouriste> currentDPSAssignment, Set<Secouriste> currentDPSTempUsed,
                                         List<List<Secouriste>> resultats) {

        // Cas où tous les besoins du DPS ont été satisfaits
        if (besoinIndex == besoins.size()) {
            resultats.add(new ArrayList<>(currentDPSAssignment)); // Ajout de la combinaison actuelle à la liste des résultats
            return;
        }

        Besoin besoinActuel = besoins.get(besoinIndex); // Récupération du besoin à satisfaire
        String compRequise = besoinActuel.getCompetence().getIntitule(); // Récupération du nom de la compétence requise pour ce besoin
        int nbRequis = besoinActuel.getNombre(); // Obtention du nombre de secouristes requis pour cette compétence

        List<Secouriste> candidatsPourCeBesoin = new ArrayList<>(); // Liste des secouristes potentiels pour ce besoin
        for (Secouriste s : tousLesSecouristes) { // Pour chaque secouriste disponibles
            // Si le secouriste n'est pas déjà affecté, pas déjà utilisé pour ce DPS et possède la compétence requise
            if (!dejaAffectesGlobal.contains(s) && !currentDPSTempUsed.contains(s) && dag.possederCompetence(s, compRequise)) {
                candidatsPourCeBesoin.add(s); // Ajout du secouriste à la liste des candidats 
            }
        }

        // Si pas assez de candidats pour satisfaire le besoin
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
        List<List<Secouriste>> combinaisonsSimples = new ArrayList<>(); // Liste des sous-combinaisons de secouristes répondant au besoin
        genererCombinaisonsSimples(candidatsPourCeBesoin, nbRequis, 0, new ArrayList<>(), combinaisonsSimples); // Trouver toutes les façons possibles de choisir un 'nbRequis' secouriste parmis les candidats

        for (List<Secouriste> choix : combinaisonsSimples) { // Pour chaque combinaison de secouristes trouvée pour le besoin actuel
            currentDPSAssignment.addAll(choix); // Ajout des secouristes à l'affectation temporaire du DPS
            currentDPSTempUsed.addAll(choix); // Marquer ces secouristes comme utilisés pour ce DPS (on évite de les réutiliser pour d'autres besoins du DPS)

            // Appel récursif pour le besoin suivant
            genererCombinaisonsRec(besoins, besoinIndex + 1, tousLesSecouristes, dejaAffectesGlobal, currentDPSAssignment, currentDPSTempUsed, resultats);

            // Retirer les secouristes pour explorer d'autres choix (backtrack)
            currentDPSAssignment.removeAll(choix); // Retire les secouristes de l'affectation temporaire du DPS
            currentDPSTempUsed.removeAll(choix); // Rend disponible tous les ecouristes pour d'autres combinaisons de ce DPS
        }
    }


    // Cette méthode reste inchangée, elle génère des combinaisons sans contrainte de compétence
    private void genererCombinaisonsSimples(List<Secouriste> liste, int k, int start,
                                            List<Secouriste> current, List<List<Secouriste>> resultats) {
        // Cas où la combinaison a atteint la taille k
        if (current.size() == k) {
            resultats.add(new ArrayList<>(current)); // Ajout de la combinaison à la liste des résultats
            return;
        }
        if (start >= liste.size()) { // Condition pour gérer le cas où k est trop grand ou si la liste est épuisée
            return;
        }
        for (int i = start; i < liste.size(); i++) { // Parcourt des éléments de la liste à partir de start
            current.add(liste.get(i)); // Ajout de l'élément actuel à la combinaison en cours
            genererCombinaisonsSimples(liste, k, i + 1, current, resultats); // Appel récursif pour choisir les éléments suivants en commençant à i + 1 pour éviter les doublons et les ordres différents
            current.remove(current.size() - 1); // Retire le dernier élément ajouté pour essayer d'autre combinaisons (backtrack)
        }
    }

 //============================================================= AFFECTATION GLOUTONNE =============================================================\\

    public Map<DPS, List<Secouriste>> affectationGloutonne(List<Secouriste> secouristes, List<DPS> dps) {
        Map<DPS, List<Secouriste>> affectation = new HashMap<>(); // Map pour stocker l'affectation finale
        Set<Secouriste> dejaAffectes = new HashSet<>(); // Ensemble des secouristes déjà affectés 

        List<DPS> dpsTries = new ArrayList<>(dps); // Copie de la liste des DPS
        dpsTries.sort((d1, d2) -> Integer.compare(d2.getNbSecouristesRequis(), d1.getNbSecouristesRequis())); // Tri des DPS par ordre décroissant du nombre total de secouristes requis

        for (DPS d : dpsTries) { // Pour chaque DPS dans dpsTries
            List<Secouriste> affectesPourCeDps = new ArrayList<>(); // Liste pour stocker les secouristes affectés au DPS actuel
            List<Secouriste> secouristesEligiblesPourCeDps = new ArrayList<>(); // Liste temporaire des secouristes pas encore affectés
            for(Secouriste s : secouristes) { // Pour chaque secouriste
                if (!dejaAffectes.contains(s)) { // S'il n'est pas affecté à au autre DPS
                    secouristesEligiblesPourCeDps.add(s); // On l'ajoute à la liste des secouristes disponibles
                }
            }

            for (Besoin besoin : d.getBesoins()) { // Pour chaque besoin du DPS
                int nbRequis = besoin.getNombre(); // Récupérer le nombre de secouristes nécessaire
                String compRequise = besoin.getCompetence().getIntitule(); // Récupérer les compétences requises

                int nbDejaCouvert = 0;

                for (Secouriste s : affectesPourCeDps) { // Pour chaque secouriste disponible pour ce DPS
                    if (nbRequis == 0) break; // Si le besoin est satisfait, passer au suivant
                    if (dag.possederCompetence(s, compRequise) && !affectesPourCeDps.contains(s)) { // Si le secouriste possède la compétence requise et n'est pas affecté au DPS 
                        affectesPourCeDps.add(s); // On l'affecte au DPS
                        nbDejaCouvert++; // Le nombre requis augmente
                    }
                }

                int nbEncoreAMobiliserPourCeBesoin = nbRequis - nbDejaCouvert;

                if (nbEncoreAMobiliserPourCeBesoin > 0) {              
                    for (Secouriste candidat : secouristesEligiblesPourCeDps) { 
                        if (nbEncoreAMobiliserPourCeBesoin <= 0) {
                            break; // Assez de secouristes trouvés pour ce besoin spécifique
                        }

                        // S'il n'est pas affecté au DPS et qu'il a la compétence requise
                        if (!affectesPourCeDps.contains(candidat) && dag.possederCompetence(candidat, compRequise)) {
                            affectesPourCeDps.add(candidat); // On l'affecte au DPS
                            nbEncoreAMobiliserPourCeBesoin--; // Le nombre requis pour ce besoin spécifique diminue
                        }
                    }
                }
            }
            // Après avoir traité tous les besoins du DPS
            for (Secouriste s : affectesPourCeDps) { // Pour chaque secouriste affecté à ce DPS
                dejaAffectes.add(s); // On l'ajoute à la liste des secouristes affectés globalement
            }
            affectation.put(d, affectesPourCeDps); // Enregistre l'affectation pour ce DPS dans la Map finale
        }

        return affectation;
    }

    public int evaluerAffectation(Map<DPS, List<Secouriste>> affectation) {
        int score = 0; // Score de l'affectation

        for (DPS dps : affectation.keySet()) { // Pour chaque DPS dans la Map
            List<Secouriste> secouristesAffectesAuDps = affectation.get(dps); // Récupère les secouristes affectés à ce DPS.
            if (secouristesAffectesAuDps == null){
                secouristesAffectesAuDps = new ArrayList<>();
            } 

            for (Besoin besoin : dps.getBesoins()) { // Pour chaque besoin d'un DPS
                String compRequise = besoin.getCompetence().getIntitule(); // Nom de la compétence requise
                int nbRequis = besoin.getNombre(); // Nombre de secouristes nécessaires pouyr cette compétence
                int nbCouvert = 0; // Compteur pour le nombre de secouristes qui satisfont le besoin

                for (Secouriste s : secouristesAffectesAuDps) { // Pour chaque secouriste affectés au DPS
                    if (dag.possederCompetence(s, compRequise)) { // Si le secouriste possède la compétence requise
                        nbCouvert++; // Un secouriste de plus satisfait le besoin
                    }
                }

                score += Math.min(nbCouvert, nbRequis) * 10; // Calcul du score : minimum entre nbCouvert et nRequis * 10
                
                if (nbCouvert >= nbRequis) { // Si le besoin est entièrement satisfait
                    score += 5; // Ajoute 5 points de bonus
                }
            }
        }
        return score;
    }
}
