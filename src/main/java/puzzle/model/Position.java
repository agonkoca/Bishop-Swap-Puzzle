package puzzle.model;

/**
 * Represents a position on the puzzle board.
 * <p>
 * This record encapsulates a position on the puzzle board, defined by its row and column coordinates.
 * </p>
 *
 * @param row The row index of the position.
 * @param col The column index of the position.
 */
public record Position(int row, int col) {

    /**
     * Returns a string representation of the position.
     *
     * @return A string representing the position in the format "(row,col)".
     */
    @Override
    public String toString() {
        return String.format("(%d,%d)", row, col);
    }

}