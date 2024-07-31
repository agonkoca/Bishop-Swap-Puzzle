package puzzle.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Controller class for handling the player field in the puzzle game.
 */
public class PlayerController {

    @FXML
    private TextField playerNameArea;

    /**
     * Handles the action of starting the puzzle game.
     * <p>
     * This method is called when the user initiates the start puzzle action.
     * It retrieves the player's name from the input field, and if the name is
     * not empty, it loads the puzzle game interface and sets the player's name
     * in the puzzle controller.
     * </p>
     */
    @FXML
    private void handleStartPuzzle() {
        String playerName = playerNameArea.getText();
        if (playerName.isEmpty()) {
            Logger.info("Please enter a name to start playing.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PuzzleView.fxml"));
            Scene scene = new Scene(loader.load());
            PuzzleController controller = loader.getController();
            controller.setPlayerName(playerName);
            Logger.info("Player name: " + playerName);
            Stage stage = (Stage) playerNameArea.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger.error("Failed to load the puzzle interface", e);
        }

    }
}
