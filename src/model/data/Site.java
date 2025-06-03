package model.data;

import java.util.ArrayList;
import java.util.List;

public class Site {

    private String code;
    private String nom;
    private float longitude;
    private float latitude;
    private List<DPS> dpsOrganises;

    // Constructeur 
    public Site(String code, String nom, float longitude, float latitude) {
        this.code = code;
        this.nom = nom;
        this.longitude = longitude;
        this.latitude = latitude;
        this.dpsOrganises = new ArrayList<>();
    }

    // Ajouter  DPS
    public void ajouterDPS(DPS dps) {
        if (dps == null) {
            throw new IllegalArgumentException("Le DPS ne peut pas être null.");
        }
        dpsOrganises.add(dps);
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getNombreDPS() {
        return dpsOrganises.size();
    }

    // Setters 
    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom invalide");
        }
        this.nom = nom.trim();
    }

    public void setLongitude(float longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude invalide.");
        }
        this.longitude = longitude;
    }

    public void setLatitude(float latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude invalide.");
        }
        this.latitude = latitude;
    }

    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code invalide");
        }
        this.code = code.trim();
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return "Site " + nom + " (" + code + "), Coordonnées : [" +
               latitude + ", " + longitude + "], DPS organisés : " + getNombreDPS();
    }
}
