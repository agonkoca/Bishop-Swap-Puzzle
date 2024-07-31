package puzzle.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.tinylog.Logger;
import gameresult.*;
import gameresult.manager.*;
import gameresult.manager.json.*;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Controller class for managing and displaying puzzle game results.
 */
public class PuzzleResultController {

    @Getter
    private final OnePlayerGameResultManager resultManager = new JsonOnePlayerGameResultManager(Paths.get("puzzle_results.json"));
    @Getter
    @Setter
    @FXML
    private Stage stage;
    @FXML
    private TableView<OnePlayerGameResult> resultTable;
    @FXML
    private TableColumn<OnePlayerGameResult, String> playerName;
    @FXML
    private TableColumn<OnePlayerGameResult, Integer> moves;
    @FXML
    private TableColumn<GameResult, String> duration;
    @FXML
    private TableColumn<GameResult, String> created;
    @FXML
    private TableColumn<OnePlayerGameResult, Boolean> solved;


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */

    @FXML
    private void initialize() throws IOException {
        playerName.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        solved.setCellValueFactory(new PropertyValueFactory<>("solved"));
        moves.setCellValueFactory(new PropertyValueFactory<>("numberOfMoves"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));

        try {
            resultTable.getItems().setAll(resultManager.getAll());
            Logger.info("Loaded puzzle results.");
        } catch (IOException e) {
            Logger.error("Failed to load puzzle results: {}", e.getMessage());
            throw e;
        }
    }
}

