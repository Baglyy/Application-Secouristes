package model.data

import java.util.ArrayList;
import java.util.List;

public class DPS {

    private static long compteur = 0;
    private long id;
    private int[] horaireDepart;
    private int[] horaireFin;
    private List<Besoin> besoins;
    private List<Secouriste> secouristesAffectes;

    // Constructeur 
    public DPS(int[] horaireDepart, int[] horaireFin) {
        if (horaireDepart == null || horaireDepart.length != 2 || horaireDepart[0] < 0 || horaireDepart[1] < 0) {
            throw new IllegalArgumentException("Horaire de départ invalide.");
        }
        if (horaireFin == null || horaireFin.length != 2 || horaireFin[0] < 0 || horaireFin[1] < 0) {
            throw new IllegalArgumentException("Horaire de fin invalide.");
        }

        this.id = ++compteur;
        this.horaireDepart = horaireDepart;
        this.horaireFin = horaireFin;
        this.besoins = new ArrayList<>();
        this.secouristesAffectes = new ArrayList<>();
    }

    // Ajouter un secouriste
    public void ajouterSecouriste(Secouriste secouriste) {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste ne peut pas être null.");
        }
        secouristesAffectes.add(secouriste);
    }

    // Ajouter un besoin
    public void ajouterBesoin(Besoin besoin) {
        if (besoin == null) {
            throw new IllegalArgumentException("Le besoin ne peut pas être null.");
        }
        besoins.add(besoin);
    }

    // Calcul de durée 
    public int calculTemps() {
        int hDepart = horaireDepart[0] * 60 + horaireDepart[1];
        int hFin = horaireFin[0] * 60 + horaireFin[1];
        return hFin - hDepart;
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public int[] getHoraireDep() {
        return horaireDepart;
    }

    public void setHoraireDep(int[] horaireDepart) {
        if (horaireDepart == null || horaireDepart.length != 2) {
            throw new IllegalArgumentException("Horaire de départ invalide.");
        }
        this.horaireDepart = horaireDepart;
    }

    public int[] getHoraireFin() {
        return horaireFin;
    }

    public void setHoraireFin(int[] horaireFin) {
        if (horaireFin == null || horaireFin.length != 2) {
            throw new IllegalArgumentException("Horaire de fin invalide.");
        }
        this.horaireFin = horaireFin;
    }

    // Obtenir le besoin pour une compétence
    public int getBesoinPour(Competence competence) {
        for (Besoin besoin : besoins) {
            if (besoin.getCompetence().equals(competence)) {
                return besoin.getNombre();
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "DPS n°" + id + " de " + horaireDepart[0] + "h" + horaireDepart[1] +
               " à " + horaireFin[0] + "h" + horaireFin[1];
    }
}
