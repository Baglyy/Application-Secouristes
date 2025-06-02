package model.data;

public class Necessite {

    private DPS dps;
    private Competence competence;

    // Constructeur 
    public Necessite(DPS dps, Competence competence) {
        if (dps == null) throw new IllegalArgumentException("DPS invalide");
        this.dps = dps;
        
        if (competence == null) throw new IllegalArgumentException("Compétence invalide");
        this.competence = competence;
    }

    // Getter DPS
    public DPS getDps() {
        return dps;
    }

    // Setter dps
    public void setDps(DPS dps) {
        if (dps == null) {
            throw new IllegalArgumentException("DPS invalide");
        }
        this.dps = dps;
    }

    // Getter cpt
    public Competence getCompetence() {
        return competence;
    }

    // Setter cpt
    public void setCompetence(Competence competence) {
        if (competence == null) {
            throw new IllegalArgumentException("Compétence invalide");
        }
        this.competence = competence;
    }

    @Override
    public String toString() {
        return "Le DPS n°" + dps.getId() + " nécessite la compétence : " + competence.getIntitule();
    }
}
