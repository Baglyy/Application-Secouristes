package model.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un site géographique pouvant accueillir un ou plusieurs DPS (Dispositifs Prévisionnels de Secours).
 * Chaque site est identifié par un code, un nom, une position géographique (latitude, longitude) 
 * et une liste de DPS organisés sur ce site.
 */
public class Site {

    /** Code unique identifiant le site */
    private String code;

    /** Nom du site */
    private String nom;

    /** Longitude du site (comprise entre -180 et 180) */
    private float longitude;

    /** Latitude du site (comprise entre -90 et 90) */
    private float latitude;

    /** Liste des DPS organisés sur ce site */
    private List<DPS> dpsOrganises;

    /**
     * Construit un site avec son code, son nom et ses coordonnées géographiques.
     *
     * @param code      le code du site (non null, non vide)
     * @param nom       le nom du site (non null, non vide)
     * @param longitude la longitude du site (entre -180 et 180)
     * @param latitude  la latitude du site (entre -90 et 90)
     * @throws IllegalArgumentException si les coordonnées sont hors des bornes ou si les champs textuels sont invalides
     */
    public Site(String code, String nom, float longitude, float latitude) {
        this.code = code;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dpsOrganises = new ArrayList<>();
    }

    /**
     * Ajoute un DPS organisé sur ce site.
     *
     * @param dps le DPS à ajouter (non null)
     * @throws IllegalArgumentException si le DPS est null
     */
    public void ajouterDPS(DPS dps) {
        if (dps == null) {
            throw new IllegalArgumentException("Le DPS ne peut pas être null.");
        }
        dpsOrganises.add(dps);
    }

    /**
     * Retourne le nom du site.
     *
     * @return le nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne la longitude du site.
     *
     * @return la longitude
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Retourne la latitude du site.
     *
     * @return la latitude
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Retourne le nombre de DPS organisés sur ce site.
     *
     * @return le nombre de DPS
     */
    public int getNombreDPS() {
        return dpsOrganises.size();
    }

    /**
     * Modifie le nom du site.
     *
     * @param nom le nouveau nom (non null, non vide)
     * @throws IllegalArgumentException si le nom est invalide
     */
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom invalide");
        }
        this.nom = nom.trim();
    }

    /**
     * Modifie la longitude du site.
     *
     * @param longitude la nouvelle longitude (doit être entre -180 et 180)
     * @throws IllegalArgumentException si la valeur est hors bornes
     */
    public void setLongitude(float longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude invalide.");
        }
        this.longitude = longitude;
    }

    /**
     * Modifie la latitude du site.
     *
     * @param latitude la nouvelle latitude (doit être entre -90 et 90)
     * @throws IllegalArgumentException si la valeur est hors bornes
     */
    public void setLatitude(float latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude invalide.");
        }
        this.latitude = latitude;
    }

    /**
     * Modifie le code du site.
     *
     * @param code le nouveau code (non null, non vide)
     * @throws IllegalArgumentException si le code est invalide
     */
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code invalide");
        }
        this.code = code.trim();
    }

    /**
     * Retourne le code du site.
     *
     * @return le code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Retourne une chaîne décrivant ce site et ses coordonnées.
     *
     * @return une représentation textuelle du site
     */
    @Override
    public String toString() {
        return "Site " + nom + " (" + code + "), Coordonnées : [" +
               latitude + ", " + longitude + "], DPS organisés : " + getNombreDPS();
    }
}
