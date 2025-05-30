package model.data;


public class Competence{

    private String intitule;

    // Constructeur
    public Competence(String intitule) {
        if(intitule == null || intitule.isEmpty()) throw new IllegalArgumentException("Erreur : l'intitule de la compétence doit être renseigné.");
        this.intitule = intitule;
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
