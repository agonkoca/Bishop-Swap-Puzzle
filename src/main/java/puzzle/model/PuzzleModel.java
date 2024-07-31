package puzzle.model;

import javafx.beans.property.*;
import puzzle.TwoPhaseMoveState;

import java.util.*;


/**
 * Represents the model of the Bishop Swap Puzzle.
 * <p>
 * This class manages the state of the puzzle board and provides methods to interact with it, including checking move legality,
 * making moves, keeping track of the number of moves and determining if the puzzle has been solved among others.
 * </p>
 * <p>
 * The board consists of a 5x4 grid where black and white bishops are initially placed in specific positions.
 * The goal of the game is to swap the positions of the black and white bishops by following the rules of chess movement.
 * </p>
 */
public class PuzzleModel implements TwoPhaseMoveState<Position> {

    /**
     * The number of rows on the board.
     */
    private static final int BOARD_ROWS = 5;

    /**
     * The number of columns on the board.
     */
    private static final int BOARD_COLS = 4;
    /**
     * Property representing the number of moves made by the player.
     */
    private final IntegerProperty moves = new SimpleIntegerProperty(this, "moves", 0);
    /**
     * Represents the board, storing the bishops' positions.
     */
    private ReadOnlyObjectWrapper<Bishop>[][] board;

    private Position position = null;


    /**
     * Constructs a new PuzzleModel object.
     * <p>
     * Initializes the puzzle board with bishops placed in their initial positions.
     * </p>
     */
    public PuzzleModel() {
        board = new ReadOnlyObjectWrapper[BOARD_ROWS][BOARD_COLS];
        for (var i = 0; i < BOARD_ROWS; i++) {
            for (var j = 0; j < BOARD_COLS; j++) {
                if (i == 0 && (j == 1 || j == 3)) {
                    board[i][j] = new ReadOnlyObjectWrapper<>(Bishop.BLACK);
                } else if (i == 4 && (j == 1 || j == 3)) {
                    board[i][j] = new ReadOnlyObjectWrapper<>(Bishop.WHITE);
                } else {
                    board[i][j] = new ReadOnlyObjectWrapper<>(Bishop.NONE);
                }

            }
        }
    }

    /**
     * Checks if a position is within the bounds of the puzzle board.
     *
     * @param p The position to check.
     * @return {@code true} if the position is within the board, {@code false} otherwise.
     */
    public static boolean isOnBoard(Position p) {
        return 0 <= p.row() && p.row() < BOARD_ROWS && 0 <= p.col() && p.col() < BOARD_COLS;
    }

    /**
     * Retrieves the property representing the bishop at the specified position on the board.
     *
     * @param row The row index of the position.
     * @param col The column index of the position.
     * @return The read-only object property representing the bishop at the specified position.
     */
    public ReadOnlyObjectProperty<Bishop> bishopProperty(int row, int col) {
        return board[row][col].getReadOnlyProperty();
    }

    /**
     * Gets the number of moves made by the player.
     *
     * @return The number of moves made.
     */
    public final int getMoves() {
        return moves.get();
    }

    /**
     * Sets the number of moves made by the player.
     *
     * @param number The number of moves to set.
     */
    public final void setMoves(int number) {
        moves.set(number);
    }

    /**
     * Gets the move property.
     *
     * @return The move property.
     */
    public IntegerProperty move() {
        return moves;
    }

    /**
     * Increments the move count by one.
     */
    public void addMoves() {
        setMoves(getMoves() + 1);
    }

    /**
     * Retrieves the bishop at the specified position on the board.
     *
     * @param p The position to retrieve the bishop from.
     * @return The bishop at the specified position.
     */
    public Bishop getBishop(Position p) {
        return board[p.row()][p.col()].get();
    }

    /**
     * Sets the bishop at the specified position on the board.
     *
     * @param p      The position where the bishop will be set.
     * @param bishop The bishop to be set at the position.
     */
    private void setBishop(Position p, Bishop bishop) {
        board[p.row()][p.col()].set(bishop);
    }

    /**
     * Checks if a move from the specified position is legal.
     *
     * @param from The position to move from.
     * @return {@code true} if the move is legal, {@code false} otherwise.
     */
    @Override
    public boolean isLegalToMoveFrom(Position from) {
        return isOnBoard(from) && !isEmpty(from);
    }

    /**
     * Checks if a position on the puzzle board is empty.
     *
     * @param p The position to check.
     * @return {@code true} if the position is empty, {@code false} otherwise.
     */
    public boolean isEmpty(Position p) {
        return getBishop(p) == Bishop.NONE;
    }

    /**
     * Checks if the puzzle has been solved.
     *
     * @return {@code true} if the puzzle is solved, {@code false} otherwise.
     */
    @Override
    public boolean isSolved() {
        boolean blackBishopsFinalPositions = getBishop(new Position(4, 1)) == Bishop.BLACK && getBishop(new Position(4, 3)) == Bishop.BLACK;
        boolean whiteBishopsFinalPositions = getBishop(new Position(0, 1)) == Bishop.WHITE && getBishop(new Position(0, 3)) == Bishop.WHITE;

        return blackBishopsFinalPositions && whiteBishopsFinalPositions;
    }

    /**
     * Checks if a move is legal according to the rules of the puzzle.
     *
     * @param positionTwoPhaseMove The move to be checked.
     * @return {@code true} if the move is legal, {@code false} otherwise.
     */
    @Override
    public boolean isLegalMove(TwoPhaseMove<Position> positionTwoPhaseMove) {
        Position from = positionTwoPhaseMove.from();
        Position to = positionTwoPhaseMove.to();
        Bishop movingBishop = getBishop(from);

        return isLegalToMoveFrom(from) && isOnBoard(to) && isEmpty(to) && isBishopMove(from, to) && isMoveAllowedByOpposite(to, movingBishop);
    }

    /**
     * Performs a move on the puzzle board.
     *
     * @param move The move to be performed.
     */
    @Override
    public void makeMove(TwoPhaseMove<Position> move) {
        if (move != null && !isSolved()) {
            Position from = move.from();
            Position to = move.to();

            Bishop fromBishop = getBishop(from);

            setBishop(to, fromBishop);
            setBishop(from, Bishop.NONE);
            addMoves();
        }
    }

    /**
     * Gets the set of all legal moves available in the current puzzle state.
     *
     * @return The set of legal moves.
     */
    public Set<TwoPhaseMove<Position>> getLegalMoves() {
        Set<TwoPhaseMove<Position>> legalMoves = new HashSet<>();
        for (int fromRow = 0; fromRow < BOARD_ROWS; fromRow++) {
            for (int fromCol = 0; fromCol < BOARD_COLS; fromCol++) {
                Position from = new Position(fromRow, fromCol);
                if (isLegalToMoveFrom(from)) {
                    for (int targetRow = 0; targetRow < BOARD_ROWS; targetRow++) {
                        for (int targetCol = 0; targetCol < BOARD_COLS; targetCol++) {
                            Position to = new Position(targetRow, targetCol);
                            TwoPhaseMove<Position> move = new TwoPhaseMove<>(from, to);
                            if (isLegalMove(move)) {
                                legalMoves.add(move);
                            }
                        }
                    }
                }
            }
        }
        return legalMoves;
    }


    @Override
    public PuzzleModel clone() {
        PuzzleModel copy = new PuzzleModel();
        copy.setMoves(this.getMoves());
        copy.position = this.position != null ? new Position(this.position.row(), this.position.col()) : null;
        for (var i = 0; i < BOARD_ROWS; i++) {
            for (var j = 0; j < BOARD_COLS; j++) {
                copy.board[i][j] = new ReadOnlyObjectWrapper<>(board[i][j].get());
            }
        }
        return copy;
    }

    /**
     * Checks if the move from one position to another is a valid bishop move.
     *
     * @param from The starting position of the move.
     * @param to   The destination position of the move.
     * @return {@code true} if the move is a valid bishop move, {@code false} otherwise.
     */
    public boolean isBishopMove(Position from, Position to) {
        var dx = Math.abs(to.row() - from.row());
        var dy = Math.abs(to.col() - from.col());
        if (dx != dy) {
            return false;
        }

        int rowDirection = (to.row() - from.row()) > 0 ? 1 : -1;
        int colDirection = (to.col() - from.col()) > 0 ? 1 : -1;

        int currentRow = from.row() + rowDirection;
        int currentCol = from.col() + colDirection;

        while (currentRow != to.row() && currentCol != to.col()) {
            if (getBishop(new Position(currentRow, currentCol)) != Bishop.NONE) {
                return false;
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }
        return true;
    }

    /**
     * Checks if a move is allowed by the opposite colored bishop.
     *
     * @param destination  The destination position of the move.
     * @param movingBishop The bishop being moved.
     * @return {@code true} if the move is allowed by the opposite colored bishop, {@code false} otherwise.
     */
    public boolean isMoveAllowedByOpposite(Position destination, Bishop movingBishop) {
        Bishop oppositeBishop = (movingBishop == Bishop.BLACK) ? Bishop.WHITE : Bishop.BLACK;

        for (int i = 0; i < BOARD_ROWS; i++) {
            for (int j = 0; j < BOARD_COLS; j++) {
                Position attackerPosition = new Position(i, j);
                Bishop bishop = getBishop(attackerPosition);

                if (bishop == oppositeBishop && isBishopMove(attackerPosition, destination)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PuzzleModel that)) return false;
        if (moves.get() != that.moves.get()) return false;
        if (!Objects.equals(position, that.position)) return false;
        for (var i = 0; i < BOARD_ROWS; i++) {
            for (var j = 0; j < BOARD_COLS; j++) {
                if (board[i][j].get() != that.board[i][j].get()) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        var result = Objects.hash(moves.get(), position);
        for (var i = 0; i < BOARD_ROWS; i++) {
            for (var j = 0; j < BOARD_COLS; j++) {
                result = 31 * result + board[i][j].get().hashCode();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("\nPuzzle Board:\n");
        sb.append("  ");
        for (var col = 0; col < BOARD_COLS; col++) {
            sb.append(col).append(' ');
        }
        sb.append('\n');
        for (var i = 0; i < BOARD_ROWS; i++) {
            sb.append(i).append(' ');
            for (var j = 0; j < BOARD_COLS; j++) {
                switch (board[i][j].get()) {
                    case BLACK -> sb.append('B');
                    case WHITE -> sb.append('W');
                    case NONE -> sb.append('.');
                }
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

}
