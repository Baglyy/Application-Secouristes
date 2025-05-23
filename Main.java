import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LoginView;
import java.net.URL;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            LoginView loginView = new LoginView();
            Scene scene = new Scene(loginView.getRoot(), 1024, 600);
            
            // Vérification de l'existence du fichier CSS avant de l'ajouter
            URL cssResource = getClass().getResource("style.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
                System.out.println("CSS chargé avec succès");
            } else {
                System.out.println("Attention : fichier CSS non trouvé - /view/style.css");
                System.out.println("L'interface fonctionnera sans styles personnalisés");
            }
            
            primaryStage.setTitle("SecuOptix - Connexion");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}