import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginView;

/**
 * Classe principale de l'application SecuOptix.
 * Lance l'application JavaFX et initialise la vue de connexion.
 */
public class Main extends Application {
    
    /**
     * Point d'entrée principal pour JavaFX.
     * Initialise la scène principale avec l'écran de connexion.
     *
     * @param primaryStage la fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Créer la vue de connexion avec la référence au stage principal
            LoginView loginView = new LoginView(primaryStage);
            
            // Créer la scène
            Scene scene = new Scene(loginView.getRoot());
            
            // Appliquer les styles CSS
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            
            // Configurer la fenêtre
            primaryStage.setTitle("SecuOptix - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Méthode main qui lance l'application.
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }
}
