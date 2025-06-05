package model.data;

/**
 * Représente une journée précise à l’aide d’un jour, mois et année,
 * avec vérifications de validité (mois, années limites, jours selon le mois et l’année bissextile).
 */
public class Journee {

    /** Jour du mois */
    private int jour;

    /** Mois (1 à 12) */
    private int mois;

    /** Année (entre 1900 et 2100 inclus) */
    private int annee;

    /**
     * Construit une nouvelle journée avec les valeurs données.
     *
     * @param jour  le jour du mois (1 à 31 selon le mois et l’année)
     * @param mois  le mois (1 à 12)
     * @param annee l’année (1900 à 2100)
     * @throws IllegalArgumentException si les valeurs ne forment pas une date valide
     */
    public Journee(int jour, int mois, int annee) {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Mois invalide : " + mois);
        }

        if (annee < 1900 || annee > 2100) {
            throw new IllegalArgumentException("Année invalide : " + annee);
        }

        if (jour < 1 || jour > nbJoursDansMois(mois, annee)) {
            throw new IllegalArgumentException("Jour invalide : " + jour + " pour le mois " + mois + " et l'année " + annee);
        }

        this.jour = jour;
        this.mois = mois;
        this.annee = annee;
    }

    /**
     * Calcule le nombre de jours dans un mois donné, en tenant compte des années bissextiles.
     *
     * @param mois  le mois concerné
     * @param annee l’année concernée
     * @return le nombre de jours dans ce mois
     */
    private int nbJoursDansMois(int mois, int annee) {
        int nbJours = 31;

        if (mois == 2) {
            if (estBissextile(annee)) {
                nbJours = 29;
            } else {
                nbJours = 28;
            }
        } else if (mois == 4 || mois == 6 || mois == 9 || mois == 11) {
            nbJours = 30;
        }

        return nbJours;
    }

    /**
     * Détermine si une année est bissextile.
     *
     * @param annee l’année à tester
     * @return true si l’année est bissextile, false sinon
     */
    private boolean estBissextile(int annee) {
        return (annee % 4 == 0 && annee % 100 != 0) || (annee % 400 == 0);
    }

    /**
     * Retourne le jour du mois.
     *
     * @return le jour
     */
    public int getJour() {
        return jour;
    }

    /**
     * Retourne le mois.
     *
     * @return le mois
     */
    public int getMois() {
        return mois;
    }

    /**
     * Retourne l’année.
     *
     * @return l’année
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * Modifie le jour de la date.
     *
     * @param jour le nouveau jour (doit être valide selon le mois et l’année)
     * @throws IllegalArgumentException si le jour est invalide
     */
    public void setJour(int jour) {
        if (jour < 1 || jour > nbJoursDansMois(this.mois, this.annee)) {
            throw new IllegalArgumentException("Jour invalide : " + jour + " pour le mois " + this.mois + " et l'année " + this.annee);
        }
        this.jour = jour;
    }

    /**
     * Modifie le mois de la date.
     *
     * @param mois le nouveau mois (1 à 12)
     * @throws IllegalArgumentException si le mois est invalide ou incompatible avec le jour actuel
     */
    public void setMois(int mois) {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Mois invalide : " + mois);
        }
        if (this.jour > nbJoursDansMois(mois, this.annee)) {
            throw new IllegalArgumentException("Le jour actuel (" + this.jour + ") n'est pas valide pour le nouveau mois : " + mois);
        }
        this.mois = mois;
    }

    /**
     * Modifie l’année de la date.
     *
     * @param annee la nouvelle année (1900 à 2100)
     * @throws IllegalArgumentException si l’année est invalide ou incompatible avec le jour actuel
     */
    public void setAnnee(int annee) {
        if (annee < 1900 || annee > 2100) {
            throw new IllegalArgumentException("Année invalide : " + annee);
        }
        if (this.jour > nbJoursDansMois(this.mois, annee)) {
            throw new IllegalArgumentException("Le jour actuel (" + this.jour + ") n'est pas valide pour l'année : " + annee);
        }
        this.annee = annee;
    }

    /**
     * Retourne une représentation textuelle de la date au format jj/mm/aaaa.
     *
     * @return une chaîne formatée représentant la date
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%d", jour, mois, annee);
    }
}
