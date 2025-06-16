package model.data;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;


public class DPS {

    private long id;
    private Time horaireDep;
    private Time horaireFin;

    private Journee journee;
    private Site site;
    private Sport sport;

    private List<Secouriste> secouristesAffectes;
    private List<Besoin> besoins;

    // Constructeur 
    public DPS(long id, Time horaireDep, Time horaireFin, Site site, Sport sport, Journee journee) {
        
        if (horaireDep == null || horaireFin == null || !horaireFin.after(horaireDep)) {
            throw new IllegalArgumentException("Horaires invalides : départ = " + horaireDep + ", fin = " + horaireFin);
        }

        this.id = id;
        this.horaireDep = horaireDep;
        this.horaireFin = horaireFin;
        this.site = site;
        this.sport = sport;
        this.journee = journee;
        this.besoins = new ArrayList<>();
        this.secouristesAffectes = new ArrayList<>();
    }

    // Retourne les compétences requises (pour l'algo)
    public List<String> getCompetencesRequises() {
        List<String> competences = new ArrayList<>();
        for (Besoin b : besoins) {
            competences.add(b.getCompetence().getIntitule());
        }
        return competences;
    }

    // Retourne le total de secouristes requis (pour l'algo)
    public int getNbSecouristesRequis() {
        int total = 0;
        for (Besoin b : besoins) {
            total += b.getNombre();
        }
        return total;
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
        long millisDiff = horaireFin.getTime() - horaireDep.getTime();
        return (int) (millisDiff / (1000 * 60));
    }

    // Getters et Setters
    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public Time getHoraireDep() {
        return this.horaireDep;
    }

    public void setHoraireDep(Time horaireDep) {
        this.horaireDep = horaireDep;
    }

    public Time getHoraireFin() {
        return horaireFin;
    }

    public void setHoraireFin(Time horaireFin) {
        this.horaireFin = horaireFin;
    }


    public Site getSite() {
        return this.site;
    }

    public void setSite(Site site) { 
        this.site = site; 
    }


    public Sport getSport() {
        return this.sport;
    }

    public void setSport(Sport sport) { 
        this.sport = sport; 
    }

    public Journee getJournee() {
        return this.journee;
    }

    public void setJournee(Journee journee) { 
        this.journee = journee; 
    }

    public List<Secouriste> getSecouristesAffectes() { 
        return this.secouristesAffectes; 
    }

    public void setSecouristesAffectes(List<Secouriste> secouristesAffectes) { 
        this.secouristesAffectes = secouristesAffectes; 
    }

    public List<Besoin> getBesoins() { 
        return this.besoins; 
    }

    public void setBesoins(List<Besoin> besoins) { 
        this.besoins = besoins; 
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
        return "DPS n°" + id + " de " + horaireDep +
               " à " + horaireFin;
    }
}
