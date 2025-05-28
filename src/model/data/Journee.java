public class Journee {

    private int jour;
    private int mois;
    private int annee;

    // Constructeur
    public Journee(int jour, int mois, int annee) {
        setJour(jour);
        setMois(mois);
        setAnnee(annee);
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
        if (jour < 1 || jour > 31) {
            throw new IllegalArgumentException("Jour invalide : " + jour);
        }
        this.jour = jour;
    }

    public void setMois(int mois) {
        if (mois < 1 || mois > 12) {
            throw new IllegalArgumentException("Mois invalide : " + mois);
        }
        this.mois = mois;
    }

    public void setAnnee(int annee) {
        if (annee < 1900 || annee > 2100) {
            throw new IllegalArgumentException("Année invalide : " + annee);
        }
        this.annee = annee;
    }

    @Override
    public String toString() {
        return String.format("%02d/%02d/%d", jour, mois, annee);
    }
}
