package model.data;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;

/**
 * Représente un Dispositif Prévisionnel de Secours (DPS), lié à un événement sportif.
 * Il est défini par un identifiant, des horaires, une journée, un site, un sport,
 * ainsi qu'une liste de secouristes affectés et de besoins en compétences.
 */
public class DPS {

    /** Identifiant unique du DPS */
    private long id;

    /** Horaire de début du DPS */
    private Time horaireDep;

    /** Horaire de fin du DPS */
    private Time horaireFin;

    /** Journée à laquelle a lieu le DPS */
    private Journee journee;

    /** Site où se déroule le DPS */
    private Site site;

    /** Sport concerné par le DPS */
    private Sport sport;

    /** Liste des secouristes affectés à ce DPS */
    private List<Secouriste> secouristesAffectes;

    /** Liste des besoins en compétences pour ce DPS */
    private List<Besoin> besoins;

    /**
     * Construit un nouveau DPS.
     *
     * @param id         l'identifiant du DPS
     * @param horaireDep l'heure de début (non null)
     * @param horaireFin l'heure de fin (non null et après l'heure de début)
     * @param site       le site concerné
     * @param sport      le sport associé
     * @param journee    la journée du DPS
     * @throws IllegalArgumentException si les horaires sont invalides
     */
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

    /**
     * Retourne la liste des compétences requises par le DPS (extraites des besoins).
     *
     * @return liste des intitulés de compétences
     */
    public List<String> getCompetencesRequises() {
        List<String> competences = new ArrayList<>();
        for (Besoin b : besoins) {
            competences.add(b.getCompetence().getIntitule());
        }
        return competences;
    }

    /**
     * Calcule le nombre total de secouristes requis pour ce DPS.
     *
     * @return le total de secouristes requis
     */
    public int getNbSecouristesRequis() {
        int total = 0;
        for (Besoin b : besoins) {
            total += b.getNombre();
        }
        return total;
    }

    /**
     * Ajoute un secouriste affecté à ce DPS.
     *
     * @param secouriste le secouriste à ajouter (non null)
     * @throws IllegalArgumentException si le secouriste est null
     */
    public void ajouterSecouriste(Secouriste secouriste) {
        if (secouriste == null) {
            throw new IllegalArgumentException("Le secouriste ne peut pas être null.");
        }
        secouristesAffectes.add(secouriste);
    }

    /**
     * Ajoute un besoin de compétence à ce DPS.
     *
     * @param besoin le besoin à ajouter (non null)
     * @throws IllegalArgumentException si le besoin est null
     */
    public void ajouterBesoin(Besoin besoin) {
        if (besoin == null) {
            throw new IllegalArgumentException("Le besoin ne peut pas être null.");
        }
        besoins.add(besoin);
    }

    /**
     * Calcule la durée totale du DPS en minutes.
     *
     * @return la durée en minutes
     */
    public int calculTemps() {
        long millisDiff = horaireFin.getTime() - horaireDep.getTime();
        return (int) (millisDiff / (1000 * 60));
    }

    // === Getters et Setters ===

    /** @return l'identifiant du DPS */
    public long getId() {
        return id;
    }

    /** @param id nouveau numéro de DPS */
    public void setId(long id){
        this.id = id;
    }

    /** @return l'heure de début du DPS */
    public Time getHoraireDep() {
        return this.horaireDep;
    }

    /** @param horaireDep l'heure de début à définir */
    public void setHoraireDep(Time horaireDep) {
        this.horaireDep = horaireDep;
    }

    /** @return l'heure de fin du DPS */
    public Time getHoraireFin() {
        return horaireFin;
    }

    /** @param horaireFin l'heure de fin à définir */
    public void setHoraireFin(Time horaireFin) {
        this.horaireFin = horaireFin;
    }

    /** @return le site du DPS */
    public Site getSite() {
        return this.site;
    }

    /** @param site le site à définir */
    public void setSite(Site site) { 
        this.site = site; 
    }

    /** @return le sport concerné par ce DPS */
    public Sport getSport() {
        return this.sport;
    }

    /** @param sport le sport à définir */
    public void setSport(Sport sport) { 
        this.sport = sport; 
    }

    /** @return la journée du DPS */
    public Journee getJournee() {
        return this.journee;
    }

    /** @param journee la journée à définir */
    public void setJournee(Journee journee) { 
        this.journee = journee; 
    }

    /** @return la liste des secouristes affectés */
    public List<Secouriste> getSecouristesAffectes() { 
        return this.secouristesAffectes; 
    }

    /** @param secouristesAffectes la nouvelle liste des secouristes */
    public void setSecouristesAffectes(List<Secouriste> secouristesAffectes) { 
        this.secouristesAffectes = secouristesAffectes; 
    }

    /** @return la liste des besoins en compétences */
    public List<Besoin> getBesoins() { 
        return this.besoins; 
    }

    /** @param besoins la nouvelle liste des besoins */
    public void setBesoins(List<Besoin> besoins) { 
        this.besoins = besoins; 
    }

    /**
     * Retourne le nombre de secouristes nécessaires pour une compétence spécifique.
     *
     * @param competence la compétence recherchée
     * @return le nombre de personnes nécessaires pour cette compétence
     */
    public int getBesoinPour(Competence competence) {
        for (Besoin besoin : besoins) {
            if (besoin.getCompetence().equals(competence)) {
                return besoin.getNombre();
            }
        }
        return 0;
    }

    /**
     * Retourne une représentation textuelle du DPS.
     *
     * @return une chaîne décrivant le DPS
     */
    @Override
    public String toString() {
        return "DPS n°" + id + " de " + horaireDep + " à " + horaireFin;
    }
}
