package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import model.AdminAffectationsModel;
import model.data.DPS;
import model.data.Secouriste;
import view.AdminDashboardView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Contrôleur de l'interface d'administration des affectations.
 * Permet de générer les affectations de secouristes aux dispositifs (DPS)
 * en utilisant soit un algorithme glouton, soit un algorithme exhaustif.
 */
public class AdminAffectationsController {
    
    private Button greedyButton;
    private Button exhaustiveButton;
    private TableView<AdminAffectationsModel.Affectation> tableView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private AdminAffectationsModel model;
    private Runnable onRetourCallback;
    private TableColumn<AdminAffectationsModel.Affectation, String> colDate;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques;
    private TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes;

    /**
     * Constructeur du contrôleur.
     * 
     * @param greedyButton bouton pour lancer l'algorithme glouton
     * @param exhaustiveButton bouton pour lancer l'algorithme exhaustif
     * @param tableView table d'affichage des affectations
     * @param colDate colonne pour la date
     * @param colSitesOlympiques colonne pour le site
     * @param colSecouristes colonne pour les secouristes
     * @param nomUtilisateurLabel label affichant le nom de l'utilisateur
     * @param homeIcon icône pour revenir au tableau de bord
     * @param nomUtilisateur nom de l'utilisateur actuel
     */
    public AdminAffectationsController(
            Button greedyButton,
            Button exhaustiveButton,
            TableView<AdminAffectationsModel.Affectation> tableView,
            TableColumn<AdminAffectationsModel.Affectation, String> colDate,
            TableColumn<AdminAffectationsModel.Affectation, String> colSitesOlympiques,
            TableColumn<AdminAffectationsModel.Affectation, String> colSecouristes,
            Label nomUtilisateurLabel,
            Label homeIcon,
            String nomUtilisateur) {
        this.greedyButton = greedyButton;
        this.exhaustiveButton = exhaustiveButton;
        this.tableView = tableView;
        this.colDate = colDate;
        this.colSitesOlympiques = colSitesOlympiques;
        this.colSecouristes = colSecouristes;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.model = new AdminAffectationsModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
    }

    /**
     * Initialise les liaisons entre les propriétés du modèle et les composants de la vue.
     */
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        tableView.setItems(model.getAffectations());
    }

    /**
     * Initialise les écouteurs d'événements pour les boutons et icônes.
     */
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
        greedyButton.setOnAction(e -> handleGenerate(true));
        exhaustiveButton.setOnAction(e -> handleGenerate(false));
    }

    /**
     * Gère le retour vers le tableau de bord de l'administrateur.
     */
    private void handleRetour() {
        System.out.println("Retour vers le tableau de bord administrateur");
        
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            Stage currentStage = (Stage) homeIcon.getScene().getWindow();
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        }
    }

    /**
     * Lance la génération des affectations selon l'algorithme choisi.
     *
     * @param useGreedy true pour l'algorithme glouton, false pour l'exhaustif
     */
    private void handleGenerate(boolean useGreedy) {
        generateAffectations(useGreedy);
    }

    /**
     * Génère les affectations des secouristes aux DPS en utilisant l'algorithme spécifié.
     *
     * @param useGreedy true pour utiliser l’algorithme glouton, false pour l’exhaustif
     */
    public void generateAffectations(boolean useGreedy) {

        System.out.println("Contrôleur: Tentative de suppression des affectations existantes...");
        boolean nettoyageReussi = model.nettoyerAffectations();
        if (!nettoyageReussi) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de Préparation");
            alert.setHeaderText("Nettoyage Échoué");
            alert.setContentText("Impossible de supprimer les affectations précédentes de la base de données. Veuillez vérifier les logs.");
            alert.showAndWait();
            return;
        }
        System.out.println("Contrôleur: Affectations existantes supprimées (ou aucune à supprimer).");

        ObservableList<DPS> allDPS = model.getAllDPS();
        if (allDPS.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun DPS");
            alert.setHeaderText("Impossible de générer les affectations");
            alert.setContentText("Aucun dispositif (DPS) n'est disponible dans la base de données.");
            alert.showAndWait();
            return;
        }

        ObservableList<Secouriste> allSecouristes = FXCollections.observableArrayList(model.getSecouristeDAO().findAll());
        if (allSecouristes.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun secouriste");
            alert.setHeaderText("Impossible de générer les affectations");
            alert.setContentText("Aucun secouriste n'est disponible dans la base de données.");
            alert.showAndWait();
            return;
        }

        Map<DPS, List<Secouriste>> assignments;
        if (useGreedy) {
            assignments = model.getGraphe().affectationGloutonne(allSecouristes, allDPS);
        } else {
            assignments = model.getGraphe().affectationExhaustive(allSecouristes, allDPS);
        }

        if (assignments.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Échec des affectations");
            alert.setHeaderText("Aucune affectation générée");
            alert.setContentText("L'algorithme n'a pas pu générer d'affectations valides.");
            alert.showAndWait();
            return;
        }

        int totalAffectations = 0;
        for (Map.Entry<DPS, List<Secouriste>> entry : assignments.entrySet()) {
            DPS dps = entry.getKey();
            List<Secouriste> secouristes = entry.getValue();

            if (secouristes != null && !secouristes.isEmpty()) {
                LocalDate date = LocalDate.of(
                    dps.getJournee().getAnnee(),
                    dps.getJournee().getMois(),
                    dps.getJournee().getJour()
                );

                List<Secouriste> availableSecouristes = secouristes.stream()
                    .filter(s -> model.isSecouristeAvailable(s, date))
                    .collect(Collectors.toList());

                if (!availableSecouristes.isEmpty()) {
                    model.createAffectation(dps, date, FXCollections.observableArrayList(availableSecouristes));
                    totalAffectations += availableSecouristes.size();
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Affectations générées");
        alert.setContentText(totalAffectations + " affectations ont été générées et enregistrées avec succès.");
        alert.showAndWait();
    }

    /**
     * Récupère la liste de tous les dispositifs disponibles.
     *
     * @return liste observable de tous les DPS
     */
    public ObservableList<DPS> getAllDPS() {
        return model.getAllDPS();
    }

    /**
     * Recherche les secouristes compétents et disponibles pour un DPS donné à une date donnée.
     *
     * @param dps le dispositif concerné
     * @param date la date du dispositif
     * @return liste observable des secouristes compétents et disponibles
     */
    public ObservableList<Secouriste> searchCompetentSecouristes(DPS dps, LocalDate date) {
        return model.searchCompetentSecouristes(dps, date);
    }

    /**
     * Met à jour le nom de l'utilisateur.
     *
     * @param nomUtilisateur nouveau nom de l'utilisateur
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Définit le callback à exécuter lors du retour vers le tableau de bord.
     *
     * @param callback action à exécuter au retour
     */
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }

    /**
     * Récupère le modèle utilisé par ce contrôleur.
     *
     * @return le modèle {@link AdminAffectationsModel}
     */
    public AdminAffectationsModel getModel() {
        return model;
    }
}
