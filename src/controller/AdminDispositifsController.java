package controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import javafx.stage.Modality;
import javafx.application.Platform;
import model.AdminDispositifsModel;
import model.data.Site;
import model.data.Sport;
import model.data.Competence;
import model.data.Besoin;
import view.AdminDashboardView;
import java.util.ArrayList;
import java.util.List;

public class AdminDispositifsController {
    
    private TextField idTextField;
    private TextField horaireDepTextField;
    private TextField horaireFinTextField;
    private ComboBox<Site> siteComboBox;
    private ComboBox<Sport> sportComboBox;
    private TextField jourTextField;
    private TextField moisTextField;
    private TextField anneeTextField;
    private VBox besoinsContainer;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<AdminDispositifsModel.DispositifView> deviceListView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private WebView mapWebView;
    private AdminDispositifsModel model;
    private Runnable onRetourCallback;
    private boolean mapInitialized = false;
    private List<BesoinInput> besoinsInputs; // List to track needs inputs
    
    public AdminDispositifsController(
            TextField idTextField,
            TextField horaireDepTextField,
            TextField horaireFinTextField,
            ComboBox<Site> siteComboBox,
            ComboBox<Sport> sportComboBox,
            TextField jourTextField,
            TextField moisTextField,
            TextField anneeTextField,
            VBox besoinsContainer,
            Button ajouterButton,
            Button supprimerButton,
            ListView<AdminDispositifsModel.DispositifView> deviceListView,
            Label nomUtilisateurLabel,
            Label homeIcon,
            WebView mapWebView,
            String nomUtilisateur) {
        this.idTextField = idTextField;
        this.horaireDepTextField = horaireDepTextField;
        this.horaireFinTextField = horaireFinTextField;
        this.siteComboBox = siteComboBox;
        this.sportComboBox = sportComboBox;
        this.jourTextField = jourTextField;
        this.moisTextField = moisTextField;
        this.anneeTextField = anneeTextField;
        this.besoinsContainer = besoinsContainer;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.deviceListView = deviceListView;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.mapWebView = mapWebView;
        this.model = new AdminDispositifsModel(nomUtilisateur);
        this.besoinsInputs = new ArrayList<>();
        
        setupBindings();
        setupListeners();
        populateComboBoxes();
        setupBesoinsContainer();
    }
    
    private void populateComboBoxes() {
        // Populate site ComboBox
        siteComboBox.setItems(model.getSites());
        siteComboBox.setCellFactory(lv -> new ListCell<Site>() {
            @Override
            protected void updateItem(Site item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
        siteComboBox.setButtonCell(new ListCell<Site>() {
            @Override
            protected void updateItem(Site item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
        
        // Populate sport ComboBox
        sportComboBox.setItems(model.getSports());
        sportComboBox.setCellFactory(lv -> new ListCell<Sport>() {
            @Override
            protected void updateItem(Sport item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
        sportComboBox.setButtonCell(new ListCell<Sport>() {
            @Override
            protected void updateItem(Sport item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
    }
    
    private void setupBesoinsContainer() {
        // Set action for the initial "+" button
        Button addButton = (Button) besoinsContainer.getChildren().get(0);
        addButton.setOnAction(e -> addBesoinInput());
    }
    
    private void addBesoinInput() {
        // Create a dialog to select competence and number
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Ajouter un besoin");
        
        VBox dialogContent = new VBox(10);
        dialogContent.setPadding(new Insets(10));
        
        ComboBox<Competence> competenceComboBox = new ComboBox<>();
        competenceComboBox.setItems(model.getCompetences());
        competenceComboBox.setPromptText("Sélectionner une compétence");
        competenceComboBox.setPrefWidth(200);
        
        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre de personnes (ex: 2)");
        
        Button confirmButton = new Button("Confirmer");
        confirmButton.setDisable(true); // Disable until both fields are filled
        
        // Enable confirm button when both fields are valid
        competenceComboBox.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue) -> {
            confirmButton.setDisable(newValue == null || nombreField.getText().trim().isEmpty());
        });
        nombreField.textProperty().addListener((obs, old, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty() || competenceComboBox.getSelectionModel().isEmpty());
        });
        
        confirmButton.setOnAction(e -> {
            try {
                int nombre = Integer.parseInt(nombreField.getText().trim());
                if (nombre <= 0) {
                    showAlert("Erreur", "Le nombre doit être positif.", Alert.AlertType.ERROR);
                    return;
                }
                Competence competence = competenceComboBox.getSelectionModel().getSelectedItem();
                
                // Create UI representation
                HBox besoinBox = new HBox(5);
                Label besoinLabel = new Label(competence.getIntitule() + " (" + nombre + ")");
                besoinLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5; -fx-border-radius: 3;");
                Button newAddButton = new Button("➕");
                newAddButton.getStyleClass().add("small-button");
                newAddButton.setOnAction(ev -> addBesoinInput());
                besoinBox.getChildren().addAll(besoinLabel, newAddButton);
                
                // Store input data
                BesoinInput besoinInput = new BesoinInput(competence, nombre);
                besoinsInputs.add(besoinInput);
                
                // Update UI
                besoinsContainer.getChildren().remove(besoinsContainer.getChildren().size() - 1); // Remove last "+" button
                besoinsContainer.getChildren().add(besoinBox);
                besoinsContainer.getChildren().add(newAddButton); // Add new "+" button
                
                dialog.close();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Le nombre doit être un entier valide.", Alert.AlertType.ERROR);
            }
        });
        
        dialogContent.getChildren().addAll(
            new Label("Compétence:"),
            competenceComboBox,
            new Label("Nombre de personnes:"),
            nombreField,
            confirmButton
        );
        
        Scene dialogScene = new Scene(dialogContent, 250, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    private void setupBindings() {
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        deviceListView.setItems(model.getDispositifs());
        
        model.getDispositifs().addListener(
            (javafx.collections.ListChangeListener) change -> {
                Platform.runLater(() -> {
                    if (mapInitialized) {
                        updateMapMarkers();
                    }
                });
            }
        );
    }
    
    private void setupListeners() {
        homeIcon.setOnMouseClicked(event -> handleRetour());
        ajouterButton.setOnAction(this::handleAjouter);
        supprimerButton.setOnAction(this::handleSupprimer);
        
        deviceListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            supprimerButton.setDisable(newSelection == null);
            if (newSelection != null && mapInitialized) {
                centerMapOnDevice(newSelection);
            }
        });
    }
    
    private void initializeInteractiveMap() {
        if (mapInitialized) {
            return;
        }
        
        String mapHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Carte des dispositifs</title>
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    #map { height: 100%; width: 100%; }
                    html, body { margin: 0; padding: 0; height: 100%; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script>
                    var map = L.map('map').setView([46.603354, 1.888334], 5);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: 'Map data © <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
                        maxZoom: 18
                    }).addTo(map);
                    
                    var markers = [];
                    
                    function updateMarkers(devices) {
                        markers.forEach(marker => map.removeLayer(marker));
                        markers = [];
                        devices.forEach(device => {
                            var marker = L.marker([device.latitude, device.longitude])
                                .addTo(map)
                                .bindPopup(device.nom);
                            markers.push(marker);
                        });
                        if (devices.length > 0) {
                            var group = new L.featureGroup(markers);
                            map.fitBounds(group.getBounds().pad(0.1));
                        }
                    }
                    
                    function centerOnDevice(lat, lng) {
                        map.setView([lat, lng], 15);
                    }
                    
                    function invalidateMapSize() {
                        map.invalidateSize();
                    }
                </script>
            </body>
            </html>
            """;
        
        mapWebView.getEngine().loadContent(mapHtml);
        
        mapWebView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                mapInitialized = true;
                Platform.runLater(() -> {
                    mapWebView.getEngine().executeScript("""
                        if (typeof invalidateMapSize === 'function') {
                            invalidateMapSize();
                        }
                        if (typeof updateMarkers === 'function') {
                            updateMarkers([]);
                        }
                    """);
                    updateMapMarkers();
                });
            }
        });
    }
    
    private void updateMapMarkers() {
        if (!mapInitialized) {
            return;
        }
        
        try {
            StringBuilder jsDevices = new StringBuilder("[");
            for (int i = 0; i < model.getDispositifs().size(); i++) {
                AdminDispositifsModel.DispositifView device = model.getDispositifs().get(i);
                if (i > 0) jsDevices.append(",");
                jsDevices.append("{")
                        .append("nom: 'DPS-").append(device.getId()).append("',")
                        .append("latitude: ").append(device.getSite().getLatitude()).append(",")
                        .append("longitude: ").append(device.getSite().getLongitude())
                        .append("}");
            }
            jsDevices.append("]");
            
            String script = "if (typeof updateMarkers === 'function') { updateMarkers(" + jsDevices.toString() + "); }";
            mapWebView.getEngine().executeScript(script);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des marqueurs: " + e.getMessage());
        }
    }
    
    private void centerMapOnDevice(AdminDispositifsModel.DispositifView device) {
        if (!mapInitialized) {
            return;
        }
        
        try {
            String script = String.format("if (typeof centerOnDevice === 'function') { centerOnDevice(%f, %f); }", 
                                        device.getSite().getLatitude(), device.getSite().getLongitude());
            mapWebView.getEngine().executeScript(script);
        } catch (Exception e) {
            System.err.println("Erreur lors du centrage sur le dispositif: " + e.getMessage());
        }
    }
    
    private String escapeJavaScript(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("'", "\\'")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }
    
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
    
    private void handleAjouter(ActionEvent event) {
        try {
            String idStr = idTextField.getText().trim();
            String horaireDepStr = horaireDepTextField.getText().trim();
            String horaireFinStr = horaireFinTextField.getText().trim();
            Site selectedSite = siteComboBox.getSelectionModel().getSelectedItem();
            Sport selectedSport = sportComboBox.getSelectionModel().getSelectedItem();
            String jourStr = jourTextField.getText().trim();
            String moisStr = moisTextField.getText().trim();
            String anneeStr = anneeTextField.getText().trim();
            
            // Validation des champs
            if (idStr.isEmpty() || horaireDepStr.isEmpty() || horaireFinStr.isEmpty() ||
                selectedSite == null || selectedSport == null ||
                jourStr.isEmpty() || moisStr.isEmpty() || anneeStr.isEmpty()) {
                showAlert("Erreur", "Tous les champs sont obligatoires.", Alert.AlertType.ERROR);
                return;
            }
            
            long id;
            try {
                id = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }
            
            java.sql.Time horaireDep;
            try {
                horaireDep = java.sql.Time.valueOf(horaireDepStr);
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "L'horaire de départ doit être au format HH:mm:ss.", Alert.AlertType.ERROR);
                return;
            }
            
            java.sql.Time horaireFin;
            try {
                horaireFin = java.sql.Time.valueOf(horaireFinStr);
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "L'horaire de fin doit être au format HH:mm:ss.", Alert.AlertType.ERROR);
                return;
            }
            
            if (!horaireFin.after(horaireDep)) {
                showAlert("Erreur", "L'horaire de fin doit être après l'horaire de départ.", Alert.AlertType.ERROR);
                return;
            }
            
            int jour;
            try {
                jour = Integer.parseInt(jourStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le jour doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }
            
            int mois;
            try {
                mois = Integer.parseInt(moisStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le mois doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }
            
            int annee;
            try {
                annee = Integer.parseInt(anneeStr);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'année doit être un nombre valide.", Alert.AlertType.ERROR);
                return;
            }
            
            // Validation de la date
            try {
                new model.data.Journee(jour, mois, annee);
            } catch (IllegalArgumentException e) {
                showAlert("Erreur", "La date entrée est invalide: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
            
            // Vérifier si l'ID existe déjà
            boolean idExiste = model.getDispositifs().stream()
                .anyMatch(d -> d.getId() == id);
            
            if (idExiste) {
                showAlert("Erreur", "Un dispositif avec cet ID existe déjà.", Alert.AlertType.ERROR);
                return;
            }
            
            // Collect besoins
            List<BesoinInput> besoins = new ArrayList<>(besoinsInputs);
            if (besoins.isEmpty()) {
                showAlert("Erreur", "Au moins un besoin en compétence doit être spécifié.", Alert.AlertType.ERROR);
                return;
            }
            
            // Ajouter le dispositif et les besoins via le modèle
            boolean success = model.ajouterDispositif(id, horaireDep, horaireFin, selectedSite, selectedSport, jour, mois, annee, besoins);
            
            if (success) {
                // Vider les champs après ajout
                idTextField.clear();
                horaireDepTextField.clear();
                horaireFinTextField.clear();
                siteComboBox.getSelectionModel().clearSelection();
                sportComboBox.getSelectionModel().clearSelection();
                jourTextField.clear();
                moisTextField.clear();
                anneeTextField.clear();
                besoinsContainer.getChildren().clear();
                besoinsInputs.clear();
                Button newAddButton = new Button("➕");
                newAddButton.getStyleClass().add("small-button");
                newAddButton.setOnAction(e -> addBesoinInput());
                besoinsContainer.getChildren().add(newAddButton);
                
                idTextField.requestFocus();
                
                showAlert("Succès", "Dispositif 'DPS-" + id + "' ajouté avec succès!", Alert.AlertType.INFORMATION);
            } else {
                if (model.getDispositifs().stream().noneMatch(d -> d.getJournee().getJour() == jour && 
                                                                  d.getJournee().getMois() == mois && 
                                                                  d.getJournee().getAnnee() == annee)) {
                    showAlert("Erreur", "La journée " + jour + "/" + mois + "/" + annee + " n'existe pas dans la base de données.", Alert.AlertType.ERROR);
                } else {
                    showAlert("Erreur", "Erreur lors de l'ajout du dispositif.", Alert.AlertType.ERROR);
                }
            }
            
        } catch (Exception e) {
            showAlert("Erreur", "Erreur inattendue lors de l'ajout du dispositif: " + e.getMessage(), Alert.AlertType.ERROR);
            System.err.println("Erreur lors de l'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleSupprimer(ActionEvent event) {
        AdminDispositifsModel.DispositifView selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
        
        if (selectedDevice != null) {
            try {
                long idDispositif = selectedDevice.getId();
                boolean success = model.supprimerDispositif(selectedDevice);
                
                if (success) {
                    showAlert("Succès", "Dispositif 'DPS-" + idDispositif + "' supprimé avec succès!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Erreur lors de la suppression en base de données.", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage(), Alert.AlertType.ERROR);
                System.err.println("Erreur lors de la suppression: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un dispositif à supprimer.", Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public AdminDispositifsModel getModel() {
        return model;
    }
    
    public void refreshMap() {
        Platform.runLater(() -> {
            if (!mapInitialized) {
                initializeInteractiveMap();
            } else {
                mapWebView.getEngine().executeScript("""
                    if (typeof invalidateMapSize === 'function') {
                        invalidateMapSize();
                    }
                """);
                updateMapMarkers();
            }
        });
    }
    
    public void invalidateMapSize() {
        Platform.runLater(() -> {
            if (mapInitialized) {
                mapWebView.getEngine().executeScript("if (typeof invalidateMapSize === 'function') { invalidateMapSize(); }");
            }
        });
    }
    
    public void refreshData() {
        model.refreshFromDatabase();
    }
    
    // Helper class to store besoin inputs
    public static class BesoinInput {
        private final Competence competence;
        private final int nombre;
        
        public BesoinInput(Competence competence, int nombre) {
            this.competence = competence;
            this.nombre = nombre;
        }
        
        public Competence getCompetence() {
            return competence;
        }
        
        public int getNombre() {
            return nombre;
        }
    }
}