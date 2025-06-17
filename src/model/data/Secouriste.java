package model.data;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Secouriste{
    private long id;
    private String nom;
    private String prenom;
    private String dateDeNaissance;
    private String email;
    private String tel;
    private String adresse;
    private List<Competence> competences;

    public Secouriste(long id, String nom, String prenom, String dateDeNaissance, String email, String tel, String adresse){
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
        this.dpsAffectes = new ArrayList<>();
    }

    public int calculerAge(){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate naissance = LocalDate.parse(this.dateDeNaissance, formatter);
            LocalDate aujourdHui = LocalDate.now();
            return Period.between(naissance, aujourdHui).getYears();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide. Attendu : yyyy-MM-dd");
        }
    }


    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public void setNom(String nom){
        if(nom == null){
            throw new NullPointerException("Erreur : le nom ne peut pas être null.");
        }
        this.nom = nom;
    }

    public String getNom(){
        return this.nom;
    }

    public void setPrenom(String prenom){
        if(prenom == null){
            throw new NullPointerException("Erreur : le prénom ne peut pas être null.");
        }
        this.prenom = prenom;
    }

    public String getPrenom(){
        return this.prenom;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        if(dateDeNaissance == null){
            throw new NullPointerException("Erreur : la date de naissance ne peut pas être null.");
        }
        this.dateDeNaissance = dateDeNaissance;
    }

    public String getDateDeNaissance(){
        return this.dateDeNaissance;
    }

    public void setEmail(String email) {
        if(email == null){
            throw new NullPointerException("Erreur : l'email ne peut pas être null.");
        }
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setTel(String tel){
        if(tel == null){
            throw new NullPointerException("Erreur : le numéro de téléphone ne peut pas être null.");
        }
        this.tel = tel;
    }

    public String getTel(){
        return this.tel;
    }

    public void setAdresse(String adresse) {
        if(adresse == null){
            throw new NullPointerException("Erreur : l'adresse ne peut pas être null.");
        }        
        this.adresse = adresse;
    }

    public String getAdresse(){
        return this.adresse;
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public void setCompetences(List<Competence> competences) {
        this.competences = competences;
    }

    @Override
    public String toString() {
        return "Secouriste{id = " + this.id + ", Nom = " + this.nom + ", Prénom = " + this.prenom + ", Date de naissance = " 
        + this.dateDeNaissance + ", Email = " + this.email + ", Téléphone = " + this.tel + ", Adresse = " + this.adresse + "}";
    }
}
