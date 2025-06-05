package model.data;

/**
 * Représente un besoin en personnel ayant certaines compétences pour un DPS donné.
 * Chaque besoin est défini par une compétence spécifique et un nombre de personnes requis.
 */
public class Besoin {

    /** Le nombre de personnes nécessaires pour ce besoin */
    private int nombre;

    /** La compétence requise pour ce besoin */
    private Competence competence;

    /** Le DPS auquel ce besoin est associé */
    private DPS dps;

    /**
     * Construit un nouvel objet Besoin.
     * 
     * @param dps         le DPS concerné (ne doit pas être {@code null})
     * @param competence  la compétence requise (ne doit pas être {@code null})
     * @param nombre      le nombre de personnes nécessaires (doit être strictement positif)
     * @throws IllegalArgumentException si {@code dps} ou {@code competence} est {@code null}, ou si {@code nombre} ≤ 0
     */
    public Besoin(DPS dps, Competence competence, int nombre) {
        this.nombre = nombre;
        this.competence = competence;
        this.dps = dps;
    }

    /**
     * Retourne le nombre de personnes nécessaires pour ce besoin.
     * 
     * @return le nombre de personnes
     */
    public int getNombre() {
        return nombre;
    }

    /**
     * Définit un nouveau nombre de personnes nécessaires.
     * 
     * @param nombre le nouveau nombre (doit être strictement positif)
     * @throws IllegalArgumentException si {@code nombre} ≤ 0
     */
    public void setNombre(int nombre) {
        if (nombre <= 0) {
            throw new IllegalArgumentException("Nombre invalide");
        }
        this.nombre = nombre;
    }

    /**
     * Retourne le DPS associé à ce besoin.
     * 
     * @return le DPS concerné
     */
    public DPS getDps() {
        return dps;
    }

    /**
     * Définit le DPS associé à ce besoin.
     * 
     * @param dps le nouveau DPS (ne doit pas être {@code null})
     * @throws IllegalArgumentException si {@code dps} est {@code null}
     */
    public void setDps(DPS dps) {
        if (dps == null) {
            throw new IllegalArgumentException("DPS invalide");
        }
        this.dps = dps;
    }

    /**
     * Retourne la compétence requise pour ce besoin.
     * 
     * @return la compétence concernée
     */
    public Competence getCompetence() {
        return competence;
    }

    /**
     * Définit la compétence requise pour ce besoin.
     * 
     * @param competence la nouvelle compétence (ne doit pas être {@code null})
     * @throws IllegalArgumentException si {@code competence} est {@code null}
     */
    public void setCompetence(Competence competence) {
        if (competence == null) {
            throw new IllegalArgumentException("Compétence invalide");
        }
        this.competence = competence;
    }

    /**
     * Retourne une représentation textuelle de ce besoin.
     * 
     * @return une chaîne décrivant le besoin (nombre, compétence, DPS)
     */
    @Override
    public String toString() {
        return "Besoin de " + nombre + " personnes avec la compétence " +
               competence.getIntitule() + " pour le DPS n°" + dps.getId();
    }
}
