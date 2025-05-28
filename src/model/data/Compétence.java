package model.data


public class Compétence{
    
    private String intitule;

    // Constructeur avec validation
    public Competence(String intitule) {
        setIntitule(intitule);
    }

    // Getter
    public String getIntitule() {
        return intitule;
    }

    // Setter défensif
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