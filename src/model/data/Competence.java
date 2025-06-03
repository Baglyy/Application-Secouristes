package model.data;

import java.util.ArrayList;
import java.util.List;


public class Competence{

    private String intitule;

    private List<Secouriste> secouristes;
    private List<Besoin> besoins;

    // Constructeur
    public Competence(String intitule) {
        if(intitule == null || intitule.isEmpty()) throw new IllegalArgumentException("Erreur : l'intitule de la compétence doit être renseigné.");
        this.intitule = intitule;
    }

    // Getter
    public String getIntitule() {
        return intitule;
    }

    public List<Secouriste> getSecouristes() {
        return secouristes; 
    }
        
    public List<Besoin> getBesoins() {
        return besoins; 
    }


    // Setter
    public void setIntitule(String intitule) {
        if (intitule == null || intitule.trim().isEmpty()) {
            throw new IllegalArgumentException("Intitulé invalide");
        }
        this.intitule = intitule.trim();
    }

    public void setSecouristes(List<Secouriste> secouristes) {
        this.secouristes = secouristes; 
    }

    public void setBesoins(List<Besoin> besoins) { 
        this.besoins = besoins; 
    }

    // toString
    @Override
    public String toString() {
        return "Compétence : " + intitule;
    }
}
