package controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.PlanningModel;
import model.AdminAffectationsModel;
import model.dao.SecouristeDAO;
import model.data.Secouriste;
import view.DashboardView;
import java.time.LocalDate;
import java.time.YearMonth;

public class PlanningController {
    
    private Label nomUtilisateurLabel;
    private Button homeButton;
    private Button precedentMoisButton;
    private Button suivantMoisButton;
    private Button aujourdHuiButton;
    private Label moisAnneeLabel;
    private GridPane calendrierGrid;
    private PlanningModel model;
    private Runnable onRetourCallback;
    
    public PlanningController(Label nomUtilisateurLabel, Button homeButton,
                             Button precedentMoisButton, Button suivantMoisButton, Button aujourdHuiButton,
                             Label moisAnneeLabel, GridPane calendrierGrid, String nomUtilisateur) {
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeButton = homeButton;
        this.precedentMoisButton = precedentMoisButton;
        this.suivantMoisButton = suivantMoisButton;
        this.aujourdHuiButton = aujourdHuiButton;
        this.moisAnneeLabel = moisAnneeLabel;
        this.calendrierGrid = calendrierGrid;
        
        // Récupérer l'ID du secouriste depuis la base de données
        long idSecouriste = getIdSecouristeFromDatabase(nomUtilisateur);
        this.model = new PlanningModel(nomUtilisateur, idSecouriste);
        
        setupBindings();
        setupListeners();
        updateCalendrier();
    }
    
    /**
     * Récupère l'ID du secouriste depuis la base de données
     * Méthode identique à celle de DisponibilitesView
     */
    private long getIdSecouristeFromDatabase(String nomUtilisateur) {
        try {
            SecouristeDAO dao = new SecouristeDAO();
            
            System.out.println("Recherche ID secouriste pour: '" + nomUtilisateur + "'");
            
            // Utiliser la nouvelle méthode findByFullName
            Secouriste secouriste = dao.findByFullName(nomUtilisateur);
            
            if (secouriste != null) {
                System.out.println("Secouriste trouvé - ID: " + secouriste.getId() + 
                                ", Nom: " + secouriste.getNom() + 
                                ", Prénom: " + secouriste.getPrenom());
                return secouriste.getId();
            } else {
                System.err.println("Aucun secouriste trouvé avec le nom complet: '" + nomUtilisateur + "'");
                
                // Si ça ne marche pas, essayer avec l'ancienne méthode (juste le nom)
                String[] parts = nomUtilisateur.trim().split("\\s+");
                if (parts.length >= 2) {
                    String nomSeul = parts[parts.length - 1];
                    System.out.println("Tentative avec le nom seul: '" + nomSeul + "'");
                    secouriste = dao.findByNom(nomSeul);
                    if (secouriste != null) {
                        System.out.println("Secouriste trouvé avec nom seul - ID: " + secouriste.getId());
                        return secouriste.getId();
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de l'ID du secouriste pour '" + nomUtilisateur + "' : " + e.getMessage());
            e.printStackTrace();
        }
        
        System.err.println("ATTENTION : ID secouriste non trouvé pour '" + nomUtilisateur + "', utilisation de -1");
        return -1;
    }
    
    private void setupBindings() {
        if (nomUtilisateurLabel != null) {
            nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        }
        model.moisActuelProperty().addListener((obs, oldMois, newMois) -> {
            updateCalendrier();
        });
    }
    
    private void setupListeners() {
        if (homeButton != null) {
            homeButton.setOnAction(this::handleHome);
        }
        precedentMoisButton.setOnAction(this::handleMoisPrecedent);
        suivantMoisButton.setOnAction(this::handleMoisSuivant);
        aujourdHuiButton.setOnAction(this::handleAujourdHui);
    }
    
    private void updateCalendrier() {
        calendrierGrid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);
        
        YearMonth moisActuel = model.getMoisActuel();
        LocalDate premierJour = model.getPremierJourDuMois();
        int nombreJours = model.getNombreJoursDansMois();
        int premierJourSemaine = model.getPremierJourSemaine();
        LocalDate aujourdhui = LocalDate.now();
        
        // Debug : afficher les affectations chargées
        System.out.println("=== DEBUG updateCalendrier ===");
        System.out.println("Mois actuel: " + moisActuel);
        System.out.println("Nombre d'affectations dans le modèle: " + model.getAffectations().size());
        
        int row = 1;
        int col = premierJourSemaine - 1;
        
        for (int jour = 1; jour <= nombreJours; jour++) {
            LocalDate dateJour = premierJour.withDayOfMonth(jour);
            Button cellButton = new Button(String.valueOf(jour));
            cellButton.getStyleClass().add("calendrier-cell");
            
            // Debug : vérifier chaque jour
            boolean hasAffectation = model.hasAffectationPourDate(dateJour);
            System.out.println("Date: " + dateJour + " -> hasAffectation: " + hasAffectation);
            
            if (dateJour.equals(aujourdhui)) {
                cellButton.getStyleClass().add("aujourdhui-cell");
                System.out.println("  -> Ajout classe 'aujourdhui-cell'");
            } else if (hasAffectation) {
                cellButton.getStyleClass().add("affectation-cell");
                AdminAffectationsModel.Affectation affectation = model.getAffectationPourDate(dateJour);
                if (affectation != null) {
                    Tooltip tooltip = new Tooltip(
                        "Affectation du " + affectation.getDate() + "\n" +
                        "Site: " + affectation.getSitesOlympiques() + "\n" +
                        "Secouristes: " + affectation.getSecouristes()
                    );
                    cellButton.setTooltip(tooltip);
                }
                System.out.println("  -> Ajout classe 'affectation-cell'");
            } else {
                cellButton.getStyleClass().add("neutre-cell");
                System.out.println("  -> Ajout classe 'neutre-cell'");
            }
            
            calendrierGrid.add(cellButton, col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
        
        String moisAnneeText = model.getMoisAnneeString();
        moisAnneeLabel.setText(moisAnneeText.substring(0, 1).toUpperCase() + moisAnneeText.substring(1));
        
        System.out.println("=== FIN DEBUG updateCalendrier ===");
    }
    
    private void handleMoisPrecedent(ActionEvent event) {
        model.moisPrecedent();
    }
    
    private void handleMoisSuivant(ActionEvent event) {
        model.moisSuivant();
    }
    
    private void handleAujourdHui(ActionEvent event) {
        model.allerAujourdHui();
    }
    
    private void handleHome(ActionEvent event) {
        navigateToDashboard();
    }
    
    private void navigateToDashboard() {
        try {
            Stage currentStage = (Stage) (homeButton != null ? homeButton.getScene().getWindow() : null);
            if (currentStage != null) {
                DashboardView dashboardView = new DashboardView(model.getNomUtilisateur());
                Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
                currentStage.setScene(dashboardScene);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la navigation vers le dashboard : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void setNomUtilisateur(String nomUtilisateur) {
        model.setNomUtilisateur(nomUtilisateur);
    }
    
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public PlanningModel getModel() {
        return model;
    }
    
    public void refreshCalendrier() {
        updateCalendrier();
    }
}