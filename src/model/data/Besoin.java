package model.data;

public class Besoin {

    private int nombre;
    private DPS dps;
    private Competence competence;

    // Constructeur défensif
    public Besoin(DPS dps, Competence competence, int nombre) {
        setDps(dps);
        setCompetence(competence);
        setNombre(nombre);
    }

    // Getter nbr
    public int getNombre() {
        return nombre;
    }

    // Setter nbr
    public void setNombre(int nombre) {
        if (nombre <= 0) {
            throw new IllegalArgumentException("Nombre invalide");
        }
        this.nombre = nombre;
    }

    // Getter dps
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
        return "Besoin de " + nombre + " personnes avec la compétence " +
               competence.getIntitule() + " pour le DPS n°" + dps.getId();
    }
}
