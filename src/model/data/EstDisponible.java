package model.data;

public class estDisponible{
    private long idSecouriste;
    private int jour;
    private int mois;
    private int annee;

    //constructeur
    public estDisponible( long idSecouriste, int jour, int mois, int annee){
        if (idSecouriste <= 0) throw new IllegalArgumentException("ID invalide");
        if (jour < 1 || jour > 31) throw new IllegalArgumentException("Jour invalide : " + jour);
        if (mois < 1 || mois > 12) throw new IllegalArgumentException("Mois invalide : " + mois);
        if (annee < 1900 || annee > 2100) throw new IllegalArgumentException("Année invalide : " + annee);

        this.idSecouriste = idSecouriste;
        this.jour = jour;
        this.mois = mois;
        this.annee = annee; 
    }

    // Getter
    public long idSecouriste() {
        return idSecouriste;
    }
    public int jour(){
        return jour;
    }
    public int mois(){
        return mois;
    }
    public int annee(){
        return annee;
    }

    // setter
    public void setIdSecouriste(long idSecouriste){
        if ( idSecouriste <= 0) throw new IllegalArgumentException("ID invalide");
        this.idSecouriste = idSecouriste;
    }
    public void setJour(int jour){
        if (jour <= 0 ) throw new IllegalArgumentException("Jour invalide : " + jour);
        this.jour = jour;
    }
    public void setMois(int mois){
        if (mois <= 0 )throw new IllegalArgumentException("Mois invalide : " + mois);
        this.mois = mois; 
    }
    public void setAnnee(int annee){
        if (annee <= 0) throw new IllegalArgumentException("Annee invalide : " + annee);
        this.annee = annee;
    }

    // pour compare
    public boolean correspondA(Journee journee) {
        if (journee == null) return false;
        return this.jour == journee.getJour()
            && this.mois == journee.getMois()
            && this.annee == journee.getAnnee();
    }

    @Override
    public String toString() {
        return "Disponible le " + String.format("%02d/%02d/%d", jour, mois, annee);
    }
}
