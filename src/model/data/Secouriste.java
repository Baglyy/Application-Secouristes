package model.data;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un secouriste avec ses informations personnelles et ses compétences.
 */
public class Secouriste {
    /** Identifiant unique du secouriste */
    private long id;

    /** Nom du secouriste */
    private String nom;

    /** Prénom du secouriste */
    private String prenom;

    /** Date de naissance au format yyyy-MM-dd */
    private String dateDeNaissance;

    /** Adresse e-mail du secouriste */
    private String email;

    /** Numéro de téléphone du secouriste */
    private String tel;

    /** Adresse du secouriste */
    private String adresse;

    /** Liste des compétences associées au secouriste */
    private List<Competence> competences;

    /**
     * Construit un nouveau secouriste.
     *
     * @param id               identifiant du secouriste (doit être > 0)
     * @param nom              nom du secouriste (non null, non vide)
     * @param prenom           prénom du secouriste (non null, non vide)
     * @param dateDeNaissance  date de naissance au format yyyy-MM-dd (non null, non vide)
     * @param email            email du secouriste (non null, non vide)
     * @param tel              numéro de téléphone
     * @param adresse          adresse postale
     * @throws IllegalArgumentException si les paramètres obligatoires sont invalides
     */
    public Secouriste(long id, String nom, String prenom, String dateDeNaissance, String email, String tel, String adresse) {
        if (id <= 0) {
            throw new IllegalArgumentException("L'ID doit être un nombre positif non null");
        }
        if (nom == null || nom.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide");
        }
        if (prenom == null || prenom.isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être null ou vide");
        }
        if (dateDeNaissance == null || dateDeNaissance.isEmpty()) {
            throw new IllegalArgumentException("La date de naissance ne peut pas être null ou vide");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être null ou vide");
        }

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.competences = new ArrayList<>();
    }

    /**
     * Calcule l'âge du secouriste à partir de sa date de naissance.
     *
     * @return l'âge en années
     * @throws IllegalArgumentException si le format de la date est invalide
     */
    public int calculerAge() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate naissance = LocalDate.parse(this.dateDeNaissance, formatter);
            LocalDate aujourdHui = LocalDate.now();
            return Period.between(naissance, aujourdHui).getYears();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Attendu : yyyy-MM-dd");
        }
    }

    /** @return l'identifiant */
    public long getId() {
        return this.id;
    }

    /** @param id nouveau identifiant */
    public void setId(long id) {
        this.id = id;
    }

    /** @return le nom */
    public String getNom() {
        return this.nom;
    }

    /** @param nom le nouveau nom */
    public void setNom(String nom) {
        if (nom == null) {
            throw new NullPointerException("Erreur : le nom ne peut pas être null.");
        }
        this.nom = nom;
    }

    /** @return le prénom */
    public String getPrenom() {
        return this.prenom;
    }

    /** @param prenom le nouveau prénom */
    public void setPrenom(String prenom) {
        if (prenom == null) {
            throw new NullPointerException("Erreur : le prénom ne peut pas être null.");
        }
        this.prenom = prenom;
    }

    /** @return la date de naissance */
    public String getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    /** @param dateDeNaissance la nouvelle date de naissance */
    public void setDateDeNaissance(String dateDeNaissance) {
        if (dateDeNaissance == null) {
            throw new NullPointerException("Erreur : la date de naissance ne peut pas être null.");
        }
        this.dateDeNaissance = dateDeNaissance;
    }

    /** @return l'adresse email */
    public String getEmail() {
        return this.email;
    }

    /** @param email nouvelle adresse email */
    public void setEmail(String email) {
        if (email == null) {
            throw new NullPointerException("Erreur : l'email ne peut pas être null.");
        }
        this.email = email;
    }

    /** @return le numéro de téléphone */
    public String getTel() {
        return this.tel;
    }

    /** @param tel le nouveau numéro de téléphone */
    public void setTel(String tel) {
        if (tel == null) {
            throw new NullPointerException("Erreur : le numéro de téléphone ne peut pas être null.");
        }
        this.tel = tel;
    }

    /** @return l'adresse postale */
    public String getAdresse() {
        return this.adresse;
    }

    /** @param adresse la nouvelle adresse postale */
    public void setAdresse(String adresse) {
        if (adresse == null) {
            throw new NullPointerException("Erreur : l'adresse ne peut pas être null.");
        }
        this.adresse = adresse;
    }

    /** @return la liste des compétences du secouriste */
    public List<Competence> getCompetences() {
        return competences;
    }

    /** @param competences la nouvelle liste de compétences */
    public void setCompetences(List<Competence> competences) {
        this.competences = competences;
    }

    /**
     * Retourne une représentation textuelle du secouriste.
     *
     * @return chaîne descriptive du secouriste
     */
    @Override
    public String toString() {
        return "Secouriste{id = " + this.id + ", Nom = " + this.nom + ", Prénom = " + this.prenom +
                ", Date de naissance = " + this.dateDeNaissance + ", Email = " + this.email +
                ", Téléphone = " + this.tel + ", Adresse = " + this.adresse + "}";
    }
}
