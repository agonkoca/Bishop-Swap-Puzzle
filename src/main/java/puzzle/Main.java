package puzzle;

import javafx.application.Application;
import puzzle.model.Position;
import puzzle.model.PuzzleModel;
import puzzle.solver.BreadthFirstSearch;

public class Main {

    public static void main(String[] args) {
        BreadthFirstSearch<TwoPhaseMoveState.TwoPhaseMove<Position>> breadthFirstSearch = new BreadthFirstSearch<>();
        new Thread(() -> breadthFirstSearch.solveAndPrintSolution(new PuzzleModel())).start();
        Application.launch(PuzzleApplication.class, args);
    }

}