package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminSecouristesModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Secouriste> secouristes = FXCollections.observableArrayList();
    
    public AdminSecouristesModel() {
        // Constructeur par défaut
        initializeData();
    }
    
    public AdminSecouristesModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeData();
    }
    
    private void initializeData() {
        // Données d'exemple correspondant aux noms visibles dans l'image
        secouristes.addAll(
            // Première ligne
            new Secouriste("Armand", "Barret", "Caron", "Delacroix", "Étienne"),
            new Secouriste("Aubert", "Boucher", "Chantier", "Dubreuil", "Evrard"),
            new Secouriste("Antoine", "Baudry", "Courtois", "Durand", "Eymard"),
            new Secouriste("Allard", "Blanchet", "Chassagne", "Deschamps", "Escobar"),
            new Secouriste("Augustin", "Brunet", "Colin", "Dufresne", "Eustache"),
            
            // Deuxième ligne  
            new Secouriste("Fabre", "Gauthier", "Hubert", "Imbert", "Joubert"),
            new Secouriste("Fontaine", "Grandin", "Hérault", "Isard", "Janvier"),
            new Secouriste("Ferrand", "Garnier", "Huet", "Iribarren", "Jacquet"),
            new Secouriste("Fleury", "Giraud", "Hémon", "Icard", "Jourdain"),
            new Secouriste("Fournier", "Grimaud", "Houdin", "Isnard", "Jolivet")
        );
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<Secouriste> getSecouristes() {
        return secouristes;
    }
    
    // Getters pour les valeurs
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    // Setters
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    // Méthodes CRUD pour les secouristes
    public void ajouterSecouriste(Secouriste secouriste) {
        secouristes.add(secouriste);
    }
    
    public void supprimerSecouriste(Secouriste secouriste) {
        secouristes.remove(secouriste);
    }
    
    public void modifierSecouriste(int index, Secouriste nouveauSecouriste) {
        if (index >= 0 && index < secouristes.size()) {
            secouristes.set(index, nouveauSecouriste);
        }
    }
    
    // Classe interne pour représenter un secouriste
    public static class Secouriste {
        private final StringProperty nom1 = new SimpleStringProperty();
        private final StringProperty nom2 = new SimpleStringProperty();
        private final StringProperty nom3 = new SimpleStringProperty();
        private final StringProperty nom4 = new SimpleStringProperty();
        private final StringProperty nom5 = new SimpleStringProperty();
        
        public Secouriste(String nom1, String nom2, String nom3, String nom4, String nom5) {
            this.nom1.set(nom1);
            this.nom2.set(nom2);
            this.nom3.set(nom3);
            this.nom4.set(nom4);
            this.nom5.set(nom5);
        }
        
        // Getters pour les propriétés
        public StringProperty nom1Property() { return nom1; }
        public StringProperty nom2Property() { return nom2; }
        public StringProperty nom3Property() { return nom3; }
        public StringProperty nom4Property() { return nom4; }
        public StringProperty nom5Property() { return nom5; }
        
        // Getters pour les valeurs
        public String getNom1() { return nom1.get(); }
        public String getNom2() { return nom2.get(); }
        public String getNom3() { return nom3.get(); }
        public String getNom4() { return nom4.get(); }
        public String getNom5() { return nom5.get(); }
        
        // Setters
        public void setNom1(String nom1) { this.nom1.set(nom1); }
        public void setNom2(String nom2) { this.nom2.set(nom2); }
        public void setNom3(String nom3) { this.nom3.set(nom3); }
        public void setNom4(String nom4) { this.nom4.set(nom4); }
        public void setNom5(String nom5) { this.nom5.set(nom5); }
    }
}