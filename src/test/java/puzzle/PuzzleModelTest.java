package puzzle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import puzzle.model.Bishop;
import puzzle.model.Position;
import puzzle.model.PuzzleModel;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleModelTest {

    private PuzzleModel puzzleModel;

    @BeforeEach
    void setUp() {
        puzzleModel = new PuzzleModel();
    }

    @Test
    void testIsOnBoard() {
        assertTrue(PuzzleModel.isOnBoard(new Position(0, 0)));
        assertTrue(PuzzleModel.isOnBoard(new Position(4, 3)));
        assertFalse(PuzzleModel.isOnBoard(new Position(5, 0)));
        assertFalse(PuzzleModel.isOnBoard(new Position(0, 4)));
        assertFalse(PuzzleModel.isOnBoard(new Position(-1, -1)));
    }

    @Test
    void testGetMoves() {
        assertEquals(0, puzzleModel.getMoves());
        puzzleModel.setMoves(10);
        assertEquals(10, puzzleModel.getMoves());
    }

    @Test
    void testAddMoves() {
        assertEquals(0, puzzleModel.getMoves());
        puzzleModel.addMoves();
        assertEquals(1, puzzleModel.getMoves());
        puzzleModel.addMoves();
        assertEquals(2, puzzleModel.getMoves());
    }

    @Test
    void testGetBishop() {
        assertEquals(Bishop.BLACK, puzzleModel.getBishop(new Position(0, 1)));
        assertEquals(Bishop.NONE, puzzleModel.getBishop(new Position(2, 2)));
        assertEquals(Bishop.WHITE, puzzleModel.getBishop(new Position(4, 1)));
    }

    @Test
    void testIsLegalToMoveFrom() {
        assertTrue(puzzleModel.isLegalToMoveFrom(new Position(0, 1)));
        assertFalse(puzzleModel.isLegalToMoveFrom(new Position(2, 2)));
        assertFalse(puzzleModel.isLegalToMoveFrom(new Position(5, 5)));
    }

    @Test
    void testIsEmpty() {
        assertTrue(puzzleModel.isEmpty(new Position(2, 2)));
        assertFalse(puzzleModel.isEmpty(new Position(0, 1)));
    }

    @Test
    void testIsSolved() {
        assertFalse(puzzleModel.isSolved());

        puzzleModel.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(4, 1), new Position(0, 1)));
        puzzleModel.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(4, 3), new Position(0, 3)));
        puzzleModel.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 1), new Position(4, 1)));
        puzzleModel.makeMove(new TwoPhaseMoveState.TwoPhaseMove<>(new Position(0, 3), new Position(4, 3)));

        assertFalse(puzzleModel.isSolved());
    }

    @Test
    void testIsLegalMove() {
        Position from = new Position(0, 1);
        Position to = new Position(1, 2);
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(from, to);
        assertTrue(puzzleModel.isLegalMove(move));

        from = new Position(2, 3);
        to = new Position(3, 5);
        move = new TwoPhaseMoveState.TwoPhaseMove<>(from, to);
        assertFalse(puzzleModel.isLegalMove(move));

        from = new Position(1, 2);
        to = new Position(2, 3);
        move = new TwoPhaseMoveState.TwoPhaseMove<>(from, to);
        assertFalse(puzzleModel.isLegalMove(move));
    }

    @Test
    void testMakeMove() {
        Position from = new Position(0, 1);
        Position to = new Position(1, 2);
        TwoPhaseMoveState.TwoPhaseMove<Position> move = new TwoPhaseMoveState.TwoPhaseMove<>(from, to);

        puzzleModel.makeMove(move);
        assertEquals(Bishop.NONE, puzzleModel.getBishop(from));
        assertEquals(Bishop.BLACK, puzzleModel.getBishop(to));
        assertNotEquals(3, puzzleModel.getMoves());
    }

    @Test
    void testGetLegalMoves() {
        Set<TwoPhaseMoveState.TwoPhaseMove<Position>> legalMoves = puzzleModel.getLegalMoves();
        assertFalse(legalMoves.isEmpty());
        for (TwoPhaseMoveState.TwoPhaseMove<Position> move : legalMoves) {
            assertTrue(puzzleModel.isLegalMove(move));
        }
    }

    @Test
    void testClone() {
        puzzleModel.setMoves(5);
        PuzzleModel clonedModel = puzzleModel.clone();
        assertEquals(puzzleModel.getMoves(), clonedModel.getMoves());
        assertEquals(puzzleModel, clonedModel);
    }

    @Test
    void testIsBishopMove() {
        assertTrue(puzzleModel.isBishopMove(new Position(0, 1), new Position(1, 2)));
        assertTrue(puzzleModel.isBishopMove(new Position(0, 1), new Position(2, 3)));
        assertFalse(puzzleModel.isBishopMove(new Position(0, 1), new Position(0, 2)));
    }

    @Test
    void testIsMoveAllowedByOpposite() {
        Position destination = new Position(2, 3);
        assertFalse(puzzleModel.isMoveAllowedByOpposite(destination, Bishop.BLACK));
        Position opponentPos = new Position(2, 3);
        TwoPhaseMoveState.TwoPhaseMove<Position> opponentMove = new TwoPhaseMoveState.TwoPhaseMove<>(opponentPos, new Position(2, 3));
        puzzleModel.makeMove(opponentMove);
        assertFalse(puzzleModel.isMoveAllowedByOpposite(destination, Bishop.BLACK));
    }
}
