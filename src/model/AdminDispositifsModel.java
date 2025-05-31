package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminDispositifsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<Dispositif> dispositifs = FXCollections.observableArrayList();
    
    public AdminDispositifsModel() {
        initializeData();
    }
    
    public AdminDispositifsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        initializeData();
    }
    
    private void initializeData() {
        // Dispositifs d'exemple correspondant aux données de la maquette
        dispositifs.addAll(
            new Dispositif("AlpinAlerte", 45.9237, 6.8694),
            new Dispositif("AvalancheFilet", 45.8326, 6.8652),
            new Dispositif("AltitudeMoniteur", 45.9162, 6.8722),
            new Dispositif("ArctiqueRéponse", 45.9341, 6.8591),
            new Dispositif("AuraBoussole", 45.8912, 6.8433),
            new Dispositif("BoreaScan", 45.9076, 6.8712),
            new Dispositif("BlizzardDrone", 45.8745, 6.8589),
            new Dispositif("BiométriquePorte", 45.9201, 6.8634),
            new Dispositif("BaliseImpulsion", 45.8834, 6.8501),
            new Dispositif("BioniqueAssist", 45.9123, 6.8723)
        );
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<Dispositif> getDispositifs() {
        return dispositifs;
    }
    
    // Getters pour les valeurs
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }
    
    // Setters
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }
    
    // Méthodes CRUD pour les dispositifs
    public void ajouterDispositif(Dispositif dispositif) {
        dispositifs.add(dispositif);
    }
    
    public void supprimerDispositif(Dispositif dispositif) {
        dispositifs.remove(dispositif);
    }
    
    public void modifierDispositif(int index, Dispositif nouveauDispositif) {
        if (index >= 0 && index < dispositifs.size()) {
            dispositifs.set(index, nouveauDispositif);
        }
    }
    
    // Classe interne pour représenter un dispositif
    public static class Dispositif {
        private final StringProperty nom = new SimpleStringProperty();
        private final DoubleProperty latitude = new SimpleDoubleProperty();
        private final DoubleProperty longitude = new SimpleDoubleProperty();
        
        public Dispositif(String nom, double latitude, double longitude) {
            this.nom.set(nom);
            this.latitude.set(latitude);
            this.longitude.set(longitude);
        }
        
        // Getters pour les propriétés
        public StringProperty nomProperty() { return nom; }
        public DoubleProperty latitudeProperty() { return latitude; }
        public DoubleProperty longitudeProperty() { return longitude; }
        
        // Getters pour les valeurs
        public String getNom() { return nom.get(); }
        public double getLatitude() { return latitude.get(); }
        public double getLongitude() { return longitude.get(); }
        
        // Setters
        public void setNom(String nom) { this.nom.set(nom); }
        public void setLatitude(double latitude) { this.latitude.set(latitude); }
        public void setLongitude(double longitude) { this.longitude.set(longitude); }
        
        @Override
        public String toString() {
            return nom.get() + " (" + String.format("%.4f", latitude.get()) + ", " + String.format("%.4f", longitude.get()) + ")";
        }
    }
}