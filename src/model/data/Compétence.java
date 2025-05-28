package model.data


public class Compétence{

    private String intitule;

    // Constructeur
    public Competence(String intitule) {
        setIntitule(intitule);
    }

    // Getter
    public String getIntitule() {
        return intitule;
    }

    // Setter
    public void setIntitule(String intitule) {
        if (intitule == null || intitule.trim().isEmpty()) {
            throw new IllegalArgumentException("Intitulé invalide");
        }
        this.intitule = intitule.trim();
    }

    // toString
    @Override
    public String toString() {
        return "Compétence : " + intitule;
    }
}
