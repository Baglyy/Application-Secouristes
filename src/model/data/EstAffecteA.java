package model.data;

public class EstAffecteA {

    private Secouriste secouriste;
    private DPS dps;
    private Journee journee;

    // Constructeur 
    public EstAffecteA(Secouriste secouriste, DPS dps, Journee journee) {
        if (secouriste == null) throw new IllegalArgumentException("Secouriste invalide");
        this.secouriste = secouriste;
        
        if (dps == null) throw new IllegalArgumentException("DPS invalide");
        this.dps = dps;
        
        if (journee == null) throw new IllegalArgumentException("La journée est invalide");
        this.journee = journee;
    }

    // Getters
    public Secouriste getSecouriste() {
        return secouriste;
    }

    public DPS getDps() {
        return dps;
    }

    public Journee getJournee() {
        return journee;
    }

    // Setters 
    public void setSecouriste(Secouriste secouriste) {
        if (secouriste == null) {
            throw new IllegalArgumentException("Secouriste invalide");
        }
        this.secouriste = secouriste;
    }

    public void setDps(DPS dps) {
        if (dps == null) {
            throw new IllegalArgumentException("DPS invalide");
        }
        this.dps = dps;
    }

    public void setJournee(Journee journee) {
        if (journee == null) {
            throw new IllegalArgumentException("La journée est invalide");
        }
        this.journee = journee;
    }

    // Méthode pour vérifier affectation
    public boolean concerne(Secouriste s, Journee j) {
        return this.secouriste.equals(s) && this.journee.equals(j);
    }

    @Override
    public String toString() {
        return secouriste.getNom() + " affecté au DPS " + dps.getId() +
               " le " + journee.toString();
    }
}
