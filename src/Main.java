import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginView;

public class Main extends Application {
    
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
    
    public static void main(String[] args) {
        launch(args);
    }
}