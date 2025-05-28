public class Possede {

    private Secouriste secouriste;
    private Competence competence;

    // Constructeur
    public Possede(Secouriste secouriste, Competence competence) {
        setSecouriste(secouriste);
        setCompetence(competence);
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
            throw new IllegalArgumentException("Secouriste ne peuyt pas etre null");
        }
        this.secouriste = secouriste;
    }

    public void setCompetence(Competence competence) {
        if (competence == null) {
            throw new IllegalArgumentException("Competence ne peut pas etre null");
        }
        this.competence = competence;
    }

    @Override
    public String toString() {
        return secouriste.getNom() + " possede la competence " + competence.getIntitule();
    }
}
