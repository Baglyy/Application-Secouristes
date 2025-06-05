package model.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une compétence pouvant être associée à des secouristes et à des besoins de missions.
 */
public class Competence {

    /** Intitulé de la compétence (ex. : "Premiers secours", "Chef d’équipe") */
    private String intitule;

    /** Liste des secouristes possédant cette compétence */
    private List<Secouriste> secouristes;

    /** Liste des besoins liés à cette compétence */
    private List<Besoin> besoins;

    /**
     * Construit une compétence avec un intitulé donné.
     *
     * @param intitule l’intitulé de la compétence (non null, non vide)
     * @throws IllegalArgumentException si l’intitulé est null ou vide
     */
    public Competence(String intitule) {
        if (intitule == null || intitule.isEmpty())
            throw new IllegalArgumentException("Erreur : l'intitulé de la compétence doit être renseigné.");
        this.intitule = intitule;
    }

    /**
     * Retourne l’intitulé de la compétence.
     *
     * @return l’intitulé
     */
    public String getIntitule() {
        return intitule;
    }

    /**
     * Modifie l’intitulé de la compétence.
     *
     * @param intitule le nouvel intitulé (non null, non vide)
     * @throws IllegalArgumentException si l’intitulé est null ou vide
     */
    public void setIntitule(String intitule) {
        if (intitule == null || intitule.trim().isEmpty()) {
            throw new IllegalArgumentException("Intitulé invalide");
        }
        this.intitule = intitule.trim();
    }

    /**
     * Retourne la liste des secouristes possédant cette compétence.
     *
     * @return liste de secouristes
     */
    public List<Secouriste> getSecouristes() {
        return secouristes;
    }

    /**
     * Définit la liste des secouristes associés à cette compétence.
     *
     * @param secouristes la liste des secouristes
     */
    public void setSecouristes(List<Secouriste> secouristes) {
        this.secouristes = secouristes;
    }

    /**
     * Retourne la liste des besoins liés à cette compétence.
     *
     * @return liste des besoins
     */
    public List<Besoin> getBesoins() {
        return besoins;
    }

    /**
     * Définit la liste des besoins pour cette compétence.
     *
     * @param besoins la liste des besoins
     */
    public void setBesoins(List<Besoin> besoins) {
        this.besoins = besoins;
    }

    /**
     * Retourne une représentation textuelle de la compétence.
     *
     * @return chaîne représentant la compétence
     */
    @Override
    public String toString() {
        return "Compétence : " + intitule;
    }
}
