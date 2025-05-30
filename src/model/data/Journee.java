package model.data;

public class Journee {

    private int jour;
    private int mois;
    private int annee;

    // Constructeur
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

    private boolean estBissextile(int annee) {
        return (annee % 4 == 0 && annee % 100 != 0) || (annee % 400 == 0);
    }

    // Getters
    public int getJour() {
        return jour;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }

    // Setter
    public void setJour(int jour) {
        if (jour < 1 || jour > nbJoursDansMois(this.mois, this.annee)) {
            throw new IllegalArgumentException("Jour invalide : " + jour + " pour le mois " + this.mois + " et l'année " + this.annee);
        }
        this.jour = jour;
    }

    public void setMois(int mois) {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Mois invalide : " + mois);
        }
        if (this.jour > nbJoursDansMois(mois, this.annee)) {
            throw new IllegalArgumentException("Le jour actuel (" + this.jour + ") n'est pas valide pour le nouveau mois : " + mois);
        }
        this.mois = mois;
    }

    public void setAnnee(int annee) {
        if (annee < 1900 || annee > 2100) {
            throw new IllegalArgumentException("Année invalide : " + annee);
        }
        if (this.jour > nbJoursDansMois(this.mois, annee)) {
            throw new IllegalArgumentException("Le jour actuel (" + this.jour + ") n'est pas valide pour l'année : " + annee);
        }
        this.annee = annee;
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%d", jour, mois, annee);
    }
}
