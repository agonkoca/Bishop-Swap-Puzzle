package puzzle.controllers;

import gameresult.manager.OnePlayerGameResultManager;
import gameresult.manager.json.JsonOnePlayerGameResultManager;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.tinylog.Logger;
import puzzle.TwoPhaseMoveState;
import puzzle.model.Bishop;
import puzzle.model.Position;
import puzzle.model.PuzzleModel;
import gameresult.OnePlayerGameResult;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.ZonedDateTime;


/**
 * Controller class for the Puzzle application.
 * <p>
 * This class handles user interactions with the puzzle board, such as mouse clicks and selection highlighting.
 * It also manages the puzzle model and enforces game rules.
 * </p>
 */
public class PuzzleController {
    private final PuzzleModel model = new PuzzleModel();
    @Getter
    private final OnePlayerGameResultManager resultManager = new JsonOnePlayerGameResultManager(Paths.get("puzzle_results.json"));
    ZonedDateTime startTime = ZonedDateTime.now();
    @FXML
    private GridPane board;
    private Position currentPosition;
    @Getter
    @Setter
    private String playerName;

    /**
     * Initializes the puzzle board.
     */
    @FXML
    private void initialize() {
        startTime = ZonedDateTime.now();
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
    }

    /**
     * Creates a square representing a position on the puzzle board.
     *
     * @param row The row index of the square.
     * @param col The column index of the square.
     * @return The StackPane representing the square.
     */
    private StackPane createSquare(int row, int col) {
        var square = new StackPane();
        square.getStyleClass().add("square");

        if ((row + col) % 2 == 0) {
            square.setStyle("-fx-background-color: #ffffe0;");
        } else {
            square.setStyle("-fx-background-color: #d2f8d2;");
        }

        square.setOnMouseEntered(event -> {
            if ((row + col) % 2 == 0) {
                square.setStyle(square.getStyle() + "-fx-background-color: #ffffa5;");
            }
            if ((row + col) % 2 != 0) {
                square.setStyle(square.getStyle() + "-fx-background-color: #b0f3b0;");
            }
        });


        square.setOnMouseExited(event -> {
            if ((row + col) % 2 == 0) {
                square.setStyle("-fx-background-color: #ffffe0;");
            } else {
                square.setStyle("-fx-background-color: #d2f8d2;");
            }
        });

        var piece = new Circle(40);
        piece.fillProperty().bind(createBishopBinding(model.bishopProperty(row, col)));
        if (model.getBishop(new Position(row, col)) != Bishop.NONE) {
            square.getStyleClass().add("bishop-border");
        }
        square.getChildren().add(piece);
        square.setOnMouseClicked(event -> handleMouseClick(row, col));
        return square;
    }

    /**
     * Creates a binding for the color of the bishop piece based on its property.
     *
     * @param bishopProperty The property representing the bishop at a position.
     * @return The binding for the color of the bishop piece.
     */
    private ObjectBinding<Paint> createBishopBinding(ReadOnlyObjectProperty<Bishop> bishopProperty) {
        return new ObjectBinding<>() {
            {
                super.bind(bishopProperty);
            }

            @Override
            protected Paint computeValue() {
                return switch (bishopProperty.get()) {
                    case NONE -> Color.TRANSPARENT;
                    case BLACK -> Color.BLACK;
                    case WHITE -> Color.WHITE;
                };
            }
        };
    }

    /**
     * Handles mouse clicks.
     *
     * @param row The row index when clicking the square.
     * @param col The column index when clicking the square.
     */
    @FXML
    private void handleMouseClick(int row, int col) {
        Position position = new Position(row, col);
        Logger.info("Clicked on row {}, column {}", row + 1, col + 1);

        if (currentPosition == null) {
            handleFirstSelection(position);
        } else {
            handleSecondSelection(position);
        }

        if (model.isSolved()) {
            Logger.info("Puzzle is solved.");
            saveGameResult();
            showResults();
        }
    }

    private void handleFirstSelection(Position position) {
        if (model.isLegalToMoveFrom(position)) {
            currentPosition = position;
            showSelection(position);
        }
    }

    private void handleSecondSelection(Position position) {
        if (currentPosition.equals(position)) {
            deselectPosition();
            return;
        }

        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(currentPosition, position);
        if (model.isLegalMove(move)) {
            Logger.info("Moved from ({}, {}) to ({}, {})", currentPosition.row() + 1, currentPosition.col() + 1, position.row() + 1, position.col() + 1);
            try {
                model.makeMove(move);
                hideSelection(currentPosition);
                currentPosition = null;
            } catch (IllegalArgumentException e) {
                Logger.error("Illegal move: {}", e.getMessage());
                deselectPosition();
            }
        } else {
            Logger.error("Illegal move");
            deselectPosition();
        }
    }

    /**
     * Highlights the selected square on the puzzle board.
     *
     * @param position The position of the selected square.
     */
    private void showSelection(Position position) {
        var bishop = getBishop(position);
        bishop.getStyleClass().add("selected");
    }

    /**
     * Removes the selection from the selected square.
     *
     * @param position The position of the selected square.
     */
    private void hideSelection(Position position) {
        var bishop = getBishop(position);
        bishop.getStyleClass().remove("selected");
    }

    /**
     * Retrieves the StackPane representing the bishop piece at a position.
     *
     * @param position The position of the bishop.
     * @return The StackPane representing the bishop piece.
     * @throws AssertionError If the bishop is not found at the specified position.
     */
    private StackPane getBishop(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        throw new AssertionError("puzzle.model.Bishop not found");
    }

    /**
     * Deselects the selected position, removing any selection highlights.
     */
    private void deselectPosition() {
        if (currentPosition != null) {
            hideSelection(currentPosition);
            currentPosition = null;
        }
    }

    /**
     * Saves the game result to a JSON file.
     * <p>
     * <p>
     * It reads the existing results from the file, appends the new result, and writes them back to the file.
     * </p>
     */

    private void saveGameResult() {
        ZonedDateTime endTime = ZonedDateTime.now();
        Duration duration = Duration.between(startTime, endTime);

        OnePlayerGameResult result = OnePlayerGameResult.builder()
                .playerName(getPlayerName(playerName))
                .solved(model.isSolved())
                .numberOfMoves(model.getMoves())
                .duration(duration)
                .created(endTime)
                .build();

        try {
            resultManager.add(result);
            Logger.info("Puzzle result saved :{}", result);
        } catch (IOException e) {
            Logger.error("Failed to save puzzle result: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the player's name.
     *
     * @param playerName The player's name.
     * @return The player's name.
     */
    private String getPlayerName(String playerName) {
        return this.playerName = playerName;
    }

    /**
     * Displays an alert when the puzzle is solved and shows the results.
     * <p>
     * This method shows an alert dialog informing the user that the puzzle is solved.
     * If the user clicks OK, it will proceed to show the puzzle results.
     * </p>
     */
    private void showResults() {
        if (model.isSolved()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Puzzle Solved");
            alert.setHeaderText("Congratulations! You solved the puzzle");
            alert.setContentText("Click OK to view results.");
            alert.showAndWait().ifPresent(then -> {
                if (then == ButtonType.OK) {
                    showPuzzleResults();
                }
            });
        }
    }

    /**
     * Loads and displays the puzzle results.
     * <p>
     * This method loads the FXML layout for the puzzle results, initializes the controller,
     * and shows the results in a new stage.
     * </p>
     */
    private void showPuzzleResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PuzzleResult.fxml"));
            Parent root = loader.load();
            PuzzleResultController resultController = loader.getController();
            Stage resultStage = new Stage();
            resultController.setStage(resultStage);
            resultStage.setScene(new Scene(root));
            resultStage.show();
        } catch (IOException e) {
            Logger.error("Failed to load puzzle results: {}", e.getMessage());
        }
    }
}
