package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DisponibilitesModel;
import view.DashboardView;

import java.time.LocalDate;

/**
 * Contrôleur de la vue des disponibilités.
 * Gère l'affichage d'un calendrier mensuel interactif et permet de marquer les jours de disponibilité pour un secouriste.
 */
public class DisponibilitesController {
    
    private Label nomUtilisateurLabel;
    private Label moisLabel;
    private Button precedentButton;
    private Button suivantButton;
    private Button retourButton;
    private Button homeButton;
    private GridPane calendrierGrid;
    private DisponibilitesModel model;
    private Runnable onRetourCallback;

    /**
     * Constructeur principal avec ID temporaire (-1).
     *
     * @param nomUtilisateurLabel Label pour afficher le nom d'utilisateur
     * @param moisLabel           Label affichant le mois courant
     * @param precedentButton     Bouton pour passer au mois précédent
     * @param suivantButton       Bouton pour passer au mois suivant
     * @param retourButton        Bouton de retour
     * @param calendrierGrid      Grille du calendrier
     * @param nomUtilisateur      Nom de l'utilisateur connecté
     * @param homeButton          Bouton d’accueil
     */
    public DisponibilitesController(Label nomUtilisateurLabel, Label moisLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid,
                                  String nomUtilisateur, Button homeButton) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.moisLabel = moisLabel;
        this.precedentButton = precedentButton;
        this.suivantButton = suivantButton;
        this.retourButton = retourButton;
        this.calendrierGrid = calendrierGrid;
        this.homeButton = homeButton;
        
        // Initialiser avec un ID temporaire, devra être mis à jour
        this.model = new DisponibilitesModel(nomUtilisateur, -1);
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }

    /**
     * Constructeur alternatif avec ID secouriste connu.
     *
     * @param nomUtilisateurLabel Label pour afficher le nom d'utilisateur
     * @param moisLabel           Label affichant le mois courant
     * @param precedentButton     Bouton pour passer au mois précédent
     * @param suivantButton       Bouton pour passer au mois suivant
     * @param retourButton        Bouton de retour
     * @param calendrierGrid      Grille du calendrier
     * @param nomUtilisateur      Nom de l'utilisateur connecté
     * @param homeButton          Bouton d’accueil
     * @param idSecouriste        ID du secouriste à manipuler
     */
    public DisponibilitesController(Label nomUtilisateurLabel, Label moisLabel,
                                  Button precedentButton, Button suivantButton, 
                                  Button retourButton, GridPane calendrierGrid,
                                  String nomUtilisateur, Button homeButton, long idSecouriste) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.moisLabel = moisLabel;
        this.precedentButton = precedentButton;
        this.suivantButton = suivantButton;
        this.retourButton = retourButton;
        this.calendrierGrid = calendrierGrid;
        this.homeButton = homeButton;
        
        // Initialiser avec l'ID secouriste correct
        this.model = new DisponibilitesModel(nomUtilisateur, idSecouriste);
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }

    /**
     * Définit dynamiquement l'ID du secouriste (si inconnu lors de la création).
     *
     * @param idSecouriste l'identifiant du secouriste
     */
    public void setIdSecouriste(long idSecouriste) {
        model.setIdSecouriste(idSecouriste);
        updateCalendrier(); // Recharger le calendrier avec les nouvelles données
    }

    /**
     * Initialise les liaisons entre les propriétés du modèle et les composants JavaFX.
     */
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        moisLabel.textProperty().bind(model.moisSelectionneProperty());
    }

    /**
     * Initialise les écouteurs d'événements pour les boutons.
     */
    private void setupListeners() {
        precedentButton.setOnAction(this::handlePrecedent);
        suivantButton.setOnAction(this::handleSuivant);
        retourButton.setOnAction(this::handleRetour);
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
    }

    /**
     * Met à jour l'affichage du calendrier avec les jours du mois courant.
     */
    private void updateCalendrier() {
        calendrierGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        LocalDate date = LocalDate.of(model.getCurrentYear(), model.getCurrentMonth(), 1);
        int firstDayOfWeek = date.getDayOfWeek().getValue() - 1; // 0 = Lundi
        int daysInMonth = date.lengthOfMonth();

        int row = 1;
        int col = firstDayOfWeek;
        for (int day = 1; day <= daysInMonth; day++) {
            Button cellButton = new Button(String.valueOf(day));
            cellButton.getStyleClass().add("calendrier-cell");

            final int currentDay = day;
            cellButton.setOnAction(e -> handleCellClick(currentDay));

            boolean disponible = model.isDisponible(currentDay);
            updateCellStyle(cellButton, disponible);

            calendrierGrid.add(cellButton, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Gère le clic sur le bouton de mois précédent.
     */
    private void handlePrecedent(ActionEvent event) {
        model.previousMonth();
        updateCalendrier();
    }

    /**
     * Gère le clic sur le bouton de mois suivant.
     */
    private void handleSuivant(ActionEvent event) {
        model.nextMonth();
        updateCalendrier();
    }

    /**
     * Gère le clic sur le bouton de retour.
     */
    private void handleRetour(ActionEvent event) {
        if (onRetourCallback != null) {
            onRetourCallback.run();
        } else {
            navigateToDashboard();
        }
    }

    /**
     * Gère le clic sur le bouton "Home".
     */
    private void handleHome(ActionEvent event) {
        navigateToDashboard();
    }

    /**
     * Navigue vers la vue du tableau de bord.
     */
    private void navigateToDashboard() {
        try {
            Stage currentStage = (Stage) (homeButton != null ? homeButton.getScene().getWindow() : 
                                         retourButton.getScene().getWindow());
            DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gère le clic sur une cellule de jour dans le calendrier.
     *
     * @param day jour du mois sélectionné
     */
    private void handleCellClick(int day) {
        System.out.println("Clic sur le jour " + day + " (idSecouriste=" + model.getIdSecouriste() + ")");
        model.toggleDisponibilite(day);
        updateCalendrier();
    }

    /**
     * Applique les styles visuels sur une cellule du calendrier en fonction de la disponibilité.
     *
     * @param button     bouton représentant le jour
     * @param disponible true si le jour est disponible, false sinon
     */
    private void updateCellStyle(Button button, boolean disponible) {
        button.getStyleClass().removeAll("disponible-cell", "indisponible-cell");
        if (disponible) {
            button.getStyleClass().add("disponible-cell");
        } else {
            button.getStyleClass().add("indisponible-cell");
        }
    }

    /**
     * Met à jour le nom d'utilisateur dans le modèle.
     *
     * @param nomUtilisateur nom d'utilisateur à définir
     */
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }

    /**
     * Définit une action personnalisée à exécuter lors du retour.
     *
     * @param callback fonction de rappel
     */
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }

    /**
     * Retourne le modèle de disponibilités utilisé par le contrôleur.
     *
     * @return le modèle associé
     */
    public DisponibilitesModel getModel() {
        return model;
    }
}
