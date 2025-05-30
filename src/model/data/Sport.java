package model.data;

public class Sport {

    private String code;
    private String nom;

    // Constructeur 
    public Sport(String code, String nom) {
        setCode(code);
        setNom(nom);
    }

    // Getter  code
    public String getCode() {
        return code;
    }

    // Setter code
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code Sport invalide");
        }
        this.code = code.trim();
    }

    // Getter nom
    public String getNom() {
        return nom;
    }

    // Setter  nom
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome Sport invalide");
        }
        this.nom = nom.trim();
    }

    // toString affichage
    @Override
    public String toString() {
        return "Sport : " + nom + " (code : " + code + ")";
    }
}
