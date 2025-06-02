package controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import javafx.application.Platform;
import model.AdminDispositifsModel;
import view.AdminDashboardView;

public class AdminDispositifsController {
    
    private TextField nomTextField;
    private TextField latitudeTextField;
    private TextField longitudeTextField;
    private Button ajouterButton;
    private Button supprimerButton;
    private ListView<AdminDispositifsModel.Dispositif> deviceListView;
    private Label nomUtilisateurLabel;
    private Label homeIcon;
    private WebView mapWebView;
    private AdminDispositifsModel model;
    private Runnable onRetourCallback;
    private boolean mapInitialized = false;
    
    public AdminDispositifsController(
            TextField nomTextField,
            TextField latitudeTextField,
            TextField longitudeTextField,
            Button ajouterButton,
            Button supprimerButton,
            ListView<AdminDispositifsModel.Dispositif> deviceListView,
            Label nomUtilisateurLabel,
            Label homeIcon,
            WebView mapWebView,
            String nomUtilisateur) {
        this.nomTextField = nomTextField;
        this.latitudeTextField = latitudeTextField;
        this.longitudeTextField = longitudeTextField;
        this.ajouterButton = ajouterButton;
        this.supprimerButton = supprimerButton;
        this.deviceListView = deviceListView;
        this.nomUtilisateurLabel = nomUtilisateurLabel;
        this.homeIcon = homeIcon;
        this.mapWebView = mapWebView;
        this.model = new AdminDispositifsModel(nomUtilisateur);
        
        setupBindings();
        setupListeners();
    }
    
    private void setupBindings() {
        // Liaison du nom d'utilisateur avec le label
        nomUtilisateurLabel.textProperty().bind(model.nomUtilisateurProperty());
        
        // Liaison des données avec la liste
        deviceListView.setItems(model.getDispositifs());
        
        // Écouter les changements dans la liste pour mettre à jour la carte
        model.getDispositifs().addListener(
            (javafx.collections.ListChangeListener<AdminDispositifsModel.Dispositif>) change -> {
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
        
        // Listener pour la sélection dans la liste
        deviceListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            supprimerButton.setDisable(newSelection == null);
            if (newSelection != null && mapInitialized) {
                // Centrer la carte sur le dispositif sélectionné
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
                <meta charset="utf-8">
                <title>Carte Interactive</title>
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                <style>
                    html, body {
                        margin: 0;
                        padding: 0;
                        width: 100%;
                        height: 100%;
                        overflow: hidden;
                    }

                    #map {
                        width: 100%;
                        height: 100%;
                        min-height: 500px;
                    }
                </style>
            </head>
            <body>
                <div id="map"></div>
                
                <script>
                    // Initialiser la carte centrée sur la France
                    var map = L.map('map').setView([46.603354, 1.888334], 6);
                    
                    // Ajouter les tuiles OpenStreetMap
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '© OpenStreetMap contributors',
                        maxZoom: 18
                    }).addTo(map);
                    
                    // Groupe pour gérer les marqueurs
                    var markerGroup = L.layerGroup().addTo(map);
                    
                    // Forcer Leaflet à recalculer sa taille après chargement
                    setTimeout(function() { 
                        map.invalidateSize(); 
                        console.log('Map size invalidated - dimensions:', 
                                   document.getElementById("map").clientWidth, 
                                   document.getElementById("map").clientHeight);
                    }, 500);
                    
                    // Fonction pour forcer le recalcul de la taille
                    window.invalidateMapSize = function() {
                        setTimeout(function() {
                            map.invalidateSize();
                            console.log('Map size invalidated manually');
                        }, 100);
                    };
                    
                    // Fonction pour mettre à jour les marqueurs
                    window.updateMarkers = function(devices) {
                        // Supprimer tous les marqueurs existants
                        markerGroup.clearLayers();
                        
                        if (devices.length === 0) {
                            return;
                        }
                        
                        var bounds = [];
                        
                        // Ajouter un marqueur pour chaque dispositif
                        devices.forEach(function(device) {
                            var marker = L.marker([device.latitude, device.longitude])
                                .bindPopup('<b>' + device.nom + '</b><br>Lat: ' + device.latitude.toFixed(4) + '<br>Lng: ' + device.longitude.toFixed(4));
                            
                            markerGroup.addLayer(marker);
                            bounds.push([device.latitude, device.longitude]);
                        });
                        
                        // Ajuster la vue pour voir tous les marqueurs
                        if (bounds.length > 1) {
                            map.fitBounds(bounds, {padding: [20, 20]});
                        } else if (bounds.length === 1) {
                            map.setView(bounds[0], 12);
                        }
                        
                        // Forcer un recalcul après mise à jour des marqueurs
                        setTimeout(function() {
                            map.invalidateSize();
                        }, 100);
                    };
                    
                    // Fonction pour centrer sur un dispositif spécifique
                    window.centerOnDevice = function(latitude, longitude) {
                        map.setView([latitude, longitude], 15);
                        // Forcer un recalcul après centrage
                        setTimeout(function() {
                            map.invalidateSize();
                        }, 100);
                    };
                    
                    // Gérer le redimensionnement de la fenêtre
                    window.addEventListener('resize', function() {
                        setTimeout(function() {
                            map.invalidateSize();
                        }, 100);
                    });
                    
                    console.log('Carte interactive initialisée');
                </script>
            </body>
            </html>
            """;
        
        mapWebView.getEngine().loadContent(mapHtml);
        
        // Attendre que la carte soit chargée
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
            // Construire le tableau JavaScript des dispositifs
            StringBuilder jsDevices = new StringBuilder("[");
            for (int i = 0; i < model.getDispositifs().size(); i++) {
                AdminDispositifsModel.Dispositif device = model.getDispositifs().get(i);
                if (i > 0) jsDevices.append(",");
                jsDevices.append("{")
                        .append("nom: '").append(escapeJavaScript(device.getNom())).append("',")
                        .append("latitude: ").append(device.getLatitude()).append(",")
                        .append("longitude: ").append(device.getLongitude())
                        .append("}");
            }
            jsDevices.append("]");
            
            String script = "if (typeof updateMarkers === 'function') { updateMarkers(" + jsDevices.toString() + "); }";
            mapWebView.getEngine().executeScript(script);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour des marqueurs: " + e.getMessage());
        }
    }
    
    private void centerMapOnDevice(AdminDispositifsModel.Dispositif device) {
        if (!mapInitialized) {
            return;
        }
        
        try {
            String script = String.format("if (typeof centerOnDevice === 'function') { centerOnDevice(%f, %f); }", 
                                        device.getLatitude(), device.getLongitude());
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
            // Navigation par défaut vers le dashboard
            Stage currentStage = (Stage) homeIcon.getScene().getWindow();
            AdminDashboardView dashboardView = new AdminDashboardView(model.getNomUtilisateur());
            Scene dashboardScene = new Scene(dashboardView.getRoot(), 1024, 600);
            currentStage.setScene(dashboardScene);
        }
    }
    
    private void handleAjouter(ActionEvent event) {
        try {
            String nom = nomTextField.getText().trim();
            String latitudeStr = latitudeTextField.getText().trim();
            String longitudeStr = longitudeTextField.getText().trim();
            
            if (nom.isEmpty() || latitudeStr.isEmpty() || longitudeStr.isEmpty()) {
                showAlert("Erreur", "Tous les champs sont obligatoires.", Alert.AlertType.ERROR);
                return;
            }
            
            double latitude = Double.parseDouble(latitudeStr);
            double longitude = Double.parseDouble(longitudeStr);
            
            // Validation des coordonnées
            if (latitude < -90 || latitude > 90) {
                showAlert("Erreur", "La latitude doit être comprise entre -90 et 90.", Alert.AlertType.ERROR);
                return;
            }
            
            if (longitude < -180 || longitude > 180) {
                showAlert("Erreur", "La longitude doit être comprise entre -180 et 180.", Alert.AlertType.ERROR);
                return;
            }
            
            // Vérifier que le nom n'existe pas déjà
            boolean nomExiste = model.getDispositifs().stream()
                .anyMatch(d -> d.getNom().equalsIgnoreCase(nom));
            
            if (nomExiste) {
                showAlert("Erreur", "Un dispositif avec ce nom existe déjà.", Alert.AlertType.ERROR);
                return;
            }
            
            AdminDispositifsModel.Dispositif nouveauDispositif = 
                new AdminDispositifsModel.Dispositif(nom, latitude, longitude);
            
            model.ajouterDispositif(nouveauDispositif);
            
            // Vider les champs après ajout
            nomTextField.clear();
            latitudeTextField.clear();
            longitudeTextField.clear();
            
            // Remettre le focus sur le premier champ
            nomTextField.requestFocus();
            
            showAlert("Succès", "Dispositif '" + nom + "' ajouté avec succès!", Alert.AlertType.INFORMATION);
            
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les coordonnées doivent être des nombres valides.\nFormat attendu: XX.XXXX", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout du dispositif: " + e.getMessage(), Alert.AlertType.ERROR);
            System.err.println("Erreur lors de l'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleSupprimer(ActionEvent event) {
        AdminDispositifsModel.Dispositif selectedDevice = deviceListView.getSelectionModel().getSelectedItem();
        
        if (selectedDevice != null) {
            try {
                String nomDispositif = selectedDevice.getNom();
                model.supprimerDispositif(selectedDevice);
                showAlert("Succès", "Dispositif '" + nomDispositif + "' supprimé avec succès!", Alert.AlertType.INFORMATION);
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
    
    // Méthodes publiques pour interaction externe
    public void setOnRetourCallback(Runnable callback) {
        this.onRetourCallback = callback;
    }
    
    public AdminDispositifsModel getModel() {
        return model;
    }
    
    // Méthode pour initialiser et rafraîchir la carte (appelée lors de l'ouverture de la popup)
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
    
    // Méthode pour forcer le recalcul de la taille de la carte
    public void invalidateMapSize() {
        Platform.runLater(() -> {
            if (mapInitialized) {
                mapWebView.getEngine().executeScript("if (typeof invalidateMapSize === 'function') { invalidateMapSize(); }");
            }
        });
    }
}