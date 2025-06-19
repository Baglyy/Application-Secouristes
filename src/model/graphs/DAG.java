package model.graphs;

import model.data.*;
import java.util.*;

/**
 * Cette classe représente un graphe orienté acyclique (DAG) des compétences
 * et de leurs relations de prérequis.
 */
public class DAG {

    /** Map représentant les compétences et leurs prérequis directs */
    private Map<String, List<String>> prerequis;

    /**
     * Constructeur du graphe de compétences.
     * Initialise les compétences et leurs prérequis de manière statique.
     */
    public DAG() {
        this.prerequis = new HashMap<>();
        initialiserGrapheCompetences();
    }

    /**
     * Initialise le graphe avec un ensemble fixe de compétences et leurs prérequis.
     */
    private void initialiserGrapheCompetences() {
        // Compétences sans prérequis
        prerequis.put("SSA", new ArrayList<>());
        prerequis.put("VPSP", new ArrayList<>());
        prerequis.put("PBF", new ArrayList<>());

        // Compétences avec prerequis
        prerequis.put("PSE1", Arrays.asList("SSA"));
        prerequis.put("PSE2", Arrays.asList("PSE1", "VPSP"));
        prerequis.put("CE", Arrays.asList("PSE2"));
        prerequis.put("CP", Arrays.asList("CE"));
        prerequis.put("CO", Arrays.asList("CP"));
        prerequis.put("PBC", Arrays.asList("PBF"));
    }

    /**
     * Vérifie si le graphe des compétences est un vrai DAG (pas de cycle).
     *
     * @return true si le graphe est acyclique, false sinon
     */
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

    /**
     * Algorithme de détection de cycle dans un graphe via DFS.
     *
     * @param competence compétence courante
     * @param blanc ensemble blanc (non visité)
     * @param gris ensemble gris (en cours de visite)
     * @param noir ensemble noir (déjà visité)
     * @return true si un cycle est détecté
     */
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

    /**
     * Tente d'ajouter une compétence avec ses prérequis dans le graphe.
     * Si l'ajout introduit un cycle, il est annulé.
     *
     * @param nouvelleComp l'intitulé de la nouvelle compétence
     * @param nouveauxPrereq la liste des compétences prérequis
     * @return true si l'ajout est valide, false sinon
     */
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

    /**
     * Vérifie si un secouriste possède une compétence ou une compétence équivalente (par implication).
     *
     * @param secouriste le secouriste concerné
     * @param competenceRequise l'intitulé de la compétence requise
     * @return true si la compétence est présente ou équivalente, false sinon
     */
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

    /**
     * Vérifie récursivement si une compétence donnée en implique une autre.
     *
     * @param competencePossedee compétence possédée par le secouriste
     * @param competenceRequise compétence à vérifier
     * @return true si la compétence possédée implique la compétence requise
     */
    private boolean implique(String competencePossedee, String competenceRequise) {
        List<String> prereq = prerequis.get(competencePossedee);
        if (prereq == null) return false;

        return prereq.contains(competenceRequise) || // Vérifie si la compétence requise est directement dans la liste des prérequis de competencePossedee
               prereq.stream().anyMatch(p -> implique(p, competenceRequise)); // Sinon vérifie récursivement si l'un des prérequis de competencePossedee implique la competenceRequise
    }

    /**
     * Renvoie une copie du graphe des prérequis.
     *
     * @return Map des compétences avec leur liste de prérequis directs
     */
    public Map<String, List<String>> getPrerequisGraphe() {
        return new HashMap<>(prerequis);
    }
}
