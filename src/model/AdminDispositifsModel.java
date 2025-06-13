package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminDispositifsModel {
    
    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<DispositifView> dispositifs = FXCollections.observableArrayList();
    private final ObservableList<Site> sites = FXCollections.observableArrayList();
    private final ObservableList<Sport> sports = FXCollections.observableArrayList();
    
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
        // Charger les dispositifs
        dispositifs.clear();
        List<DPS> dpsList = dpsDAO.findAll();
        
        for (DPS dps : dpsList) {
            DispositifView dispositifView = new DispositifView(
                dps.getId(),
                dps.getHoraireDep(),
                dps.getHoraireFin(),
                dps.getSite(),
                dps.getSport(),
                dps.getJournee()
            );
            dispositifs.add(dispositifView);
        }
        
        // Charger les sites, triés par nom
        sites.clear();
        List<Site> siteList = siteDAO.findAll();
        siteList.sort(Comparator.comparing(Site::getNom));
        sites.addAll(siteList);
        
        // Charger les sports, triés par nom
        sports.clear();
        List<Sport> sportList = sportDAO.findAll();
        sportList.sort(Comparator.comparing(Sport::getNom));
        sports.addAll(sportList);
    }
    
    // Getters pour les propriétés
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }
    
    public ObservableList<DispositifView> getDispositifs() {
        return dispositifs;
    }
    
    public ObservableList<Site> getSites() {
        return sites;
    }
    
    public ObservableList<Sport> getSports() {
        return sports;
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
    public boolean ajouterDispositif(long id, Time horaireDep, Time horaireFin, Site site, Sport sport, int jour, int mois, int annee) {
        try {
            // Valider la journée
            Journee journee = new Journee(jour, mois, annee); // Validation de la date
            
            // Vérifier si la journée existe dans la base
            Journee existingJournee = journeeDAO.findByID(jour, mois, annee);
            if (existingJournee == null) {
                System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + 
                    " La journée " + jour + "/" + mois + "/" + annee + " n'existe pas dans la base.");
                return false; // Ne pas créer la journée, retourner false
            }
            journee = existingJournee;
            
            // Créer le DPS
            DPS nouveauDPS = new DPS(id, horaireDep, horaireFin, site, sport, journee);
            
            // Sauvegarder en base
            int dpsResult = dpsDAO.create(nouveauDPS);
            
            if (dpsResult > 0) {
                // Ajouter à la liste observable
                DispositifView dispositifView = new DispositifView(id, horaireDep, horaireFin, site, sport, journee);
                dispositifs.add(dispositifView);
                return true;
            } else {
                System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + 
                    " Échec de la création du DPS ID: " + id);
                return false;
            }
            
        } catch (IllegalArgumentException e) {
            System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + 
                " Erreur de validation de la date: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + 
                " Erreur lors de l'ajout du dispositif: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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
            System.err.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()) + 
                " Erreur lors de la suppression: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    // Méthode pour rafraîchir depuis la base
    public void refreshFromDatabase() {
        loadDataFromDatabase();
    }
    
    // Classe pour l'affichage dans la vue (wrapper autour de DPS)
    public static class DispositifView {
        private final long id;
        private final Time horaireDep;
        private final Time horaireFin;
        private final Site site;
        private final Sport sport;
        private final Journee journee;
        private final DPS dps;
        
        public DispositifView(long id, Time horaireDep, Time horaireFin, Site site, Sport sport, Journee journee) {
            this.id = id;
            this.horaireDep = horaireDep;
            this.horaireFin = horaireFin;
            this.site = site;
            this.sport = sport;
            this.journee = journee;
            this.dps = new DPS(id, horaireDep, horaireFin, site, sport, journee);
        }
        
        // Getters
        public long getId() { return id; }
        public Time getHoraireDep() { return horaireDep; }
        public Time getHoraireFin() { return horaireFin; }
        public Site getSite() { return site; }
        public Sport getSport() { return sport; }
        public Journee getJournee() { return journee; }
        public DPS getDps() { return dps; }
        
        @Override
        public String toString() {
            return "DPS-" + id + " (" + site.getNom() + ", " + sport.getNom() + ", " + journee.toString() + ")";
        }
    }
}