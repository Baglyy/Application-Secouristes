package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.DPSDAO;
import model.dao.SiteDAO;
import model.dao.SportDAO;
import model.dao.JourneeDAO;
import model.data.DPS;
import model.data.Site;
import model.data.Sport;
import model.data.Journee;
import java.sql.Time;
import java.util.List;

public class AdminDispositifsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<DispositifView> dispositifs = FXCollections.observableArrayList();
    
    // DAOs
    private final DPSDAO dpsDAO;
    private final SiteDAO siteDAO;
    private final SportDAO sportDAO;
    private final JourneeDAO journeeDAO;
    
    public AdminDispositifsModel() {
        this.dpsDAO = new DPSDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        loadDataFromDatabase();
    }
    
    public AdminDispositifsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.dpsDAO = new DPSDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        loadDataFromDatabase();
    }
    
    private void loadDataFromDatabase() {
        dispositifs.clear();
        List<DPS> dpsList = dpsDAO.findAll();
        
        for (DPS dps : dpsList) {
            String nom = "DPS-" + dps.getId() + "-" + dps.getSite().getNom();
            double latitude = dps.getSite().getLatitude();
            double longitude = dps.getSite().getLongitude();
            
            DispositifView dispositifView = new DispositifView(nom, latitude, longitude, dps);
            dispositifs.add(dispositifView);
        }
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<DispositifView> getDispositifs() {
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
    public boolean ajouterDispositif(String nom, double latitude, double longitude) {
        try {
            // Créer ou récupérer le site
            String codeSite = "SITE_" + System.currentTimeMillis();
            Site site = new Site(codeSite, nom, (float) longitude, (float) latitude);
            
            // Vérifier si le site existe déjà, sinon le créer
            Site existingSite = siteDAO.findByID(codeSite);
            if (existingSite == null) {
                siteDAO.create(site);
            } else {
                site = existingSite;
            }
            
            // Créer ou récupérer un sport par défaut
            String codeSport = "SECOURS";
            Sport sport = sportDAO.findByID(codeSport);
            if (sport == null) {
                sport = new Sport(codeSport, "Secours général");
                sportDAO.create(sport);
            }
            
            // Créer une journée (date actuelle)
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int jour = cal.get(java.util.Calendar.DAY_OF_MONTH);
            int mois = cal.get(java.util.Calendar.MONTH) + 1;
            int annee = cal.get(java.util.Calendar.YEAR);
            
            Journee journee = journeeDAO.findByID(jour, mois, annee);
            if (journee == null) {
                journee = new Journee(jour, mois, annee);
                journeeDAO.create(journee);
            }
            
            // Créer le DPS avec des horaires par défaut
            long id = System.currentTimeMillis();
            Time horaireDep = Time.valueOf("08:00:00");
            Time horaireFin = Time.valueOf("18:00:00");
            
            DPS nouveauDPS = new DPS(id, horaireDep, horaireFin, site, sport, journee);
            
            // Sauvegarder en base
            int result = dpsDAO.create(nouveauDPS);
            
            if (result > 0) {
                // Ajouter à la liste observable
                DispositifView dispositifView = new DispositifView(nom, latitude, longitude, nouveauDPS);
                dispositifs.add(dispositifView);
                return true;
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public boolean supprimerDispositif(DispositifView dispositif) {
        try {
            if (dispositif.getDps() != null) {
                int result = dpsDAO.delete(dispositif.getDps());
                if (result > 0) {
                    dispositifs.remove(dispositif);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean modifierDispositif(int index, String nom, double latitude, double longitude) {
        if (index >= 0 && index < dispositifs.size()) {
            try {
                DispositifView dispositifView = dispositifs.get(index);
                DPS dps = dispositifView.getDps();
                
                if (dps != null) {
                    // Modifier le site
                    Site site = dps.getSite();
                    site.setNom(nom);
                    site.setLatitude((float) latitude);
                    site.setLongitude((float) longitude);
                    
                    // Mettre à jour en base
                    siteDAO.update(site);
                    int result = dpsDAO.update(dps);
                    
                    if (result > 0) {
                        // Mettre à jour la vue
                        dispositifView.setNom(nom);
                        dispositifView.setLatitude(latitude);
                        dispositifView.setLongitude(longitude);
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    // Méthode pour rafraîchir depuis la base
    public void refreshFromDatabase() {
        loadDataFromDatabase();
    }
    
    // Classe pour l'affichage dans la vue (wrapper autour de DPS)
    public static class DispositifView {
        private final StringProperty nom = new SimpleStringProperty();
        private final DoubleProperty latitude = new SimpleDoubleProperty();
        private final DoubleProperty longitude = new SimpleDoubleProperty();
        private DPS dps;
        
        public DispositifView(String nom, double latitude, double longitude, DPS dps) {
            this.nom.set(nom);
            this.latitude.set(latitude);
            this.longitude.set(longitude);
            this.dps = dps;
        }
        
        // Getters pour les propriétés
        public StringProperty nomProperty() { return nom; }
        public DoubleProperty latitudeProperty() { return latitude; }
        public DoubleProperty longitudeProperty() { return longitude; }
        
        // Getters pour les valeurs
        public String getNom() { return nom.get(); }
        public double getLatitude() { return latitude.get(); }
        public double getLongitude() { return longitude.get(); }
        public DPS getDps() { return dps; }
        
        // Setters
        public void setNom(String nom) { this.nom.set(nom); }
        public void setLatitude(double latitude) { this.latitude.set(latitude); }
        public void setLongitude(double longitude) { this.longitude.set(longitude); }
        public void setDps(DPS dps) { this.dps = dps; }
        
        @Override
        public String toString() {
            return nom.get() + " (" + String.format("%.4f", latitude.get()) + ", " + String.format("%.4f", longitude.get()) + ")";
        }
    }
}