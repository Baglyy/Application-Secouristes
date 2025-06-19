package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.*;
import model.data.*;
import controller.AdminDispositifsController.BesoinInput;

import java.sql.Time;
import java.util.List;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Modèle utilisé pour gérer l'administration des dispositifs dans l'application.
 * Il centralise les opérations de lecture et d'écriture en base pour les DPS, sites,
 * sports, compétences, journées et besoins.
 */
public class AdminDispositifsModel {

    private final StringProperty nomUtilisateur = new SimpleStringProperty("");
    private final ObservableList<DispositifView> dispositifs = FXCollections.observableArrayList();
    private final ObservableList<Site> sites = FXCollections.observableArrayList();
    private final ObservableList<Sport> sports = FXCollections.observableArrayList();
    private final ObservableList<Competence> competences = FXCollections.observableArrayList();

    private final DPSDAO dpsDAO;
    private final SiteDAO siteDAO;
    private final SportDAO sportDAO;
    private final JourneeDAO journeeDAO;
    private final CompetenceDAO competenceDAO;
    private final BesoinDAO besoinDAO;

    /**
     * Constructeur par défaut.
     */
    public AdminDispositifsModel() {
        this.dpsDAO = new DPSDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        this.competenceDAO = new CompetenceDAO();
        this.besoinDAO = new BesoinDAO();
        loadDataFromDatabase();
    }

    /**
     * Constructeur avec nom d'utilisateur.
     * @param nomUtilisateur nom de l'utilisateur
     */
    public AdminDispositifsModel(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
        this.dpsDAO = new DPSDAO();
        this.siteDAO = new SiteDAO();
        this.sportDAO = new SportDAO();
        this.journeeDAO = new JourneeDAO();
        this.competenceDAO = new CompetenceDAO();
        this.besoinDAO = new BesoinDAO();
        loadDataFromDatabase();
    }

    /**
     * Charge les données depuis la base de données (DPS, sites, sports, compétences).
     */
    private void loadDataFromDatabase() {
        dispositifs.clear();
        List<DPS> dpsList = dpsDAO.findAll();
        for (DPS dps : dpsList) {
            dispositifs.add(new DispositifView(
                dps.getId(), dps.getHoraireDep(), dps.getHoraireFin(), dps.getSite(), dps.getSport(), dps.getJournee()
            ));
        }

        sites.setAll(siteDAO.findAll().stream().sorted(Comparator.comparing(Site::getNom)).toList());
        sports.setAll(sportDAO.findAll().stream().sorted(Comparator.comparing(Sport::getNom)).toList());
        competences.setAll(competenceDAO.findAll().stream().sorted(Comparator.comparing(Competence::getIntitule)).toList());
    }

    /** @return propriété nom de l'utilisateur. */
    public StringProperty nomUtilisateurProperty() {
        return nomUtilisateur;
    }

    /** @return liste observable des dispositifs. */
    public ObservableList<DispositifView> getDispositifs() {
        return dispositifs;
    }

    /** @return liste observable des sites. */
    public ObservableList<Site> getSites() {
        return sites;
    }

    /** @return liste observable des sports. */
    public ObservableList<Sport> getSports() {
        return sports;
    }

    /** @return liste observable des compétences. */
    public ObservableList<Competence> getCompetences() {
        return competences;
    }

    /** @return nom actuel de l'utilisateur. */
    public String getNomUtilisateur() {
        return nomUtilisateur.get();
    }

    /** Définit le nom de l'utilisateur. */
    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur.set(nomUtilisateur);
    }

    /**
     * Tente d'ajouter un nouveau dispositif (DPS) avec ses besoins associés.
     *
     * @param id identifiant du DPS
     * @param horaireDep horaire de départ
     * @param horaireFin horaire de fin
     * @param site site concerné
     * @param sport sport associé
     * @param jour jour de la journée
     * @param mois mois de la journée
     * @param annee année de la journée
     * @param besoinsInputs liste des besoins
     * @return true si l'ajout est un succès, false sinon
     */
    public boolean ajouterDispositif(long id, Time horaireDep, Time horaireFin, Site site, Sport sport, int jour, int mois, int annee, List<BesoinInput> besoinsInputs) {
        try {
            Journee journee = journeeDAO.findByID(jour, mois, annee);
            if (journee == null) {
                System.err.println("La journée n'existe pas.");
                return false;
            }

            DPS nouveauDPS = new DPS(id, horaireDep, horaireFin, site, sport, journee);
            if (dpsDAO.create(nouveauDPS) <= 0) return false;

            boolean allOk = true;
            for (BesoinInput besoinInput : besoinsInputs) {
                Besoin besoin = new Besoin(nouveauDPS, besoinInput.getCompetence(), besoinInput.getNombre());
                if (besoinDAO.create(besoin) <= 0) allOk = false;
            }

            if (!allOk) {
                dpsDAO.delete(nouveauDPS);
                return false;
            }

            dispositifs.add(new DispositifView(id, horaireDep, horaireFin, site, sport, journee));
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un dispositif (DPS) et ses besoins associés.
     * @param dispositif le dispositif à supprimer
     * @return true si suppression réussie, false sinon
     */
    public boolean supprimerDispositif(DispositifView dispositif) {
        try {
            if (dispositif.getDps() != null) {
                List<Besoin> besoins = besoinDAO.findAll().stream()
                        .filter(b -> b.getDps().getId() == dispositif.getId())
                        .toList();
                for (Besoin besoin : besoins) {
                    besoinDAO.delete(besoin);
                }

                if (dpsDAO.delete(dispositif.getDps()) > 0) {
                    dispositifs.remove(dispositif);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Recharge toutes les données depuis la base (utile après ajout ou suppression).
     */
    public void refreshFromDatabase() {
        loadDataFromDatabase();
    }

    /**
     * Représentation d’un dispositif pour l’interface graphique.
     */
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
