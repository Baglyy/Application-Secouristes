package model.data;

public class Possede {

    private Secouriste secouriste;
    private Competence competence;

    // Constructeur
    public Possede(Secouriste secouriste, Competence competence) {
        if(secouriste == null) throw new NullPointerException("Erreur : le secouriste ne peut pas être null.");
        if(competence == null) throw new NullPointerException("Erreur : la compétence ne peut pas être null.");
        this.secouriste = secouriste;
        this.competence = competence;
    }

    // Getter
    public Secouriste getSecouriste() {
        return secouriste;
    }

    public Competence getCompetence() {
        return competence;
    }

    // Setter
    public void setSecouriste(Secouriste secouriste) {
        if (secouriste == null) {
            throw new IllegalArgumentException("Secouriste ne peut pas être null");
        }
        this.secouriste = secouriste;
    }

    public void setCompetence(Competence competence) {
        if (competence == null) {
            throw new IllegalArgumentException("Competence ne peut pas être null");
        }
        this.competence = competence;
    }

    @Override
    public String toString() {
        return secouriste.getNom() + " possede la competence " + competence.getIntitule();
    }
}
