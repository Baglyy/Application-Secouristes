package model.data

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

    // Getter 
    public int getNombre() {
        return nombre;
    }

    // Setter 
    public void setNombre(int nombre) {
        if (nombre <= 0) {
            throw new IllegalArgumentException("Nombre invalide");
        }
        this.nombre = nombre;
    }

    // Getter
    public DPS getDps() {
        return dps;
    }

    // Setter 
    public void setDps(DPS dps) {
        if (dps == null) {
            throw new IllegalArgumentException("DPS invalide");
        }
        this.dps = dps;
    }

    // Getter 
    public Competence getCompetence() {
        return competence;
    }

    // Setter 
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
