package puzzle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main application class for the Puzzle game.
 * <p>
 * This class extends the JavaFX {@link Application} class and is responsible for initializing the JavaFX application,
 * loading the user interface from the FXML file, and displaying the main puzzle window.
 * </p>
 */
public class PuzzleApplication extends Application {

    /**
     * Initializes and starts the JavaFX application.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Player.fxml"));
        stage.setTitle("Bishop Swap Puzzle");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}