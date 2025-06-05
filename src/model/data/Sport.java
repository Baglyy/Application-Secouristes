package model.data;

/**
 * Représente un sport avec un code unique et un nom.
 */
public class Sport {

    /** Le code du sport (identifiant unique) */
    private String code;

    /** Le nom du sport */
    private String nom;

    /**
     * Construit un nouveau sport avec un code et un nom.
     *
     * @param code le code du sport (non null, non vide)
     * @param nom  le nom du sport (non null, non vide)
     */
    public Sport(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    /**
     * Retourne le code du sport.
     *
     * @return le code du sport
     */
    public String getCode() {
        return code;
    }

    /**
     * Définit le code du sport.
     *
     * @param code le nouveau code (non null, non vide)
     * @throws IllegalArgumentException si le code est null ou vide
     */
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code Sport invalide");
        }
        this.code = code.trim();
    }

    /**
     * Retourne le nom du sport.
     *
     * @return le nom du sport
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du sport.
     *
     * @param nom le nouveau nom (non null, non vide)
     * @throws IllegalArgumentException si le nom est null ou vide
     */
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom Sport invalide");
        }
        this.nom = nom.trim();
    }

    /**
     * Retourne une représentation textuelle du sport.
     *
     * @return une chaîne représentant le sport
     */
    @Override
    public String toString() {
        return "Sport : " + nom + " (code : " + code + ")";
    }
}
