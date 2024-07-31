# Bishop Swap Puzzle

Consider a game board consisting of 5 rows and 4 columns. The game is played with 2 black bishop and 2 white bishop chess pieces. Initially, the 2 black bishops are placed in the top row, while the 2 white bishops are placed in the bottom row as shown below (B's represent black bishops, and W's represent white bishops, respectively):

```
+---+---+---+---+
|   | B |   | B |
+---+---+---+---+
|   |   |   |   |
+---+---+---+---+
|   |   |   |   |
+---+---+---+---+
|   |   |   |   |
+---+---+---+---+
|   | W |   | W |
+---+---+---+---+
```

The goal of the game is to swap the pieces on the board, i.e., to obtain the following configuration:

```
+---+---+---+---+
|   | W |   | W |
+---+---+---+---+
|   |   |   |   |
+---+---+---+---+
|   |   |   |   |
+---+---+---+---+
|   |   |   |   |
+---+---+---+---+
|   | B |   | B |
+---+---+---+---+
```


The pieces move according to the rules of the chess. A piece is not allowed to be moved to a square that is under attack by any piece of the opposite color. Black pieces and white pieces are not required to move in turn. Any of the pieces can be moved in the first move.

When the game starts it asks for player name inputs and stores the player name in a Json file and also displays it in the UI, after the puzzle starts the player can choose to solve it or press the finish button.
Everytime a move is made the counter increments to display the number of moves a player makes in the UI.
If the button is pressed it will redirect the player to a high score table, which shows the first 10 players with the least amount of moves.
The filtering is made firstly to show the players who solved with the smallest number of moves and then the players who didn't solve it in decreasing number of moves.

## Player Interface

- The game starts by asking for the player's name which is stored in a JSON file.

## Puzzle Interface
- After the name is entered, the puzzle starts.
- When the puzzle is correctly solved, an alert is shown, and it takes the user to the puzzle results which contains the player name, number of moves made and the time it took to finish the puzzle.
- This was done by using the TwoPhaseMoveState interface.

## Result Table Interface
- Displays the results with the following information: player names, the time the puzzle started, whether the puzzle was solved, and duration it took each player to finish the puzzle.
- It can rank the players based on the number of moves made, the amount of duration, the time of the creation of the puzzle, and by player name alphabetically.
- This was done by using the JsonOnePlayerGameResultManager and OnePlayerGameResult.

## Solution
- Implemented 'clone()' to create a copy of the currect puzzle model.
- Implemented 'equals(Object o)' to check if two puzzle models are equal.
- Implemented 'hashCode()' which generates a hash code for the puzzle model.
- Implemented 'toString()' which provides a string representation of the puzzle board and the solution.
- Implemented BreadthFirstSearch by using the above-mentioned methods.

```
Puzzle Board:
0 1 2 3
0 . B . B
1 . . . .
2 . . . .
3 . . . .
4 . W . W
```

```
TwoPhaseMove from=(0,1), to=(1,2)
Puzzle Board:
0 1 2 3
0 . . . B
1 . . B .
2 . . . .
3 . . . .
4 . W . W
```

```
TwoPhaseMove from=(4,3), to=(1,0)
Puzzle Board:
0 1 2 3
0 . . . B
1 W . B .
2 . . . .
3 . . . .
4 . W . .
```

```
TwoPhaseMove from=(4,1), to=(3,2)
Puzzle Board:
0 1 2 3
0 . . . B
1 W . B .
2 . . . .
3 . . W .
4 . . . .
```

```
TwoPhaseMovefrom=(1,2), to=(3,0)
Puzzle Board:
0 1 2 3
0 . . . B
1 W . . .
2 . . . .
3 B . W .
4 . . . .
```

```
TwoPhaseMovefrom=(1,0), to=(0,1)
Puzzle Board:
0 1 2 3
0 . W . B
1 . . . .
2 . . . .
3 B . W .
4 . . . .
```

```
TwoPhaseMovefrom=(3,2), to=(2,3)
Puzzle Board:
0 1 2 3
0 . W . B
1 . . . .
2 . . . W
3 B . . .
4 . . . .
```

```
TwoPhaseMovefrom=(3,0), to=(2,1)
Puzzle Board:
0 1 2 3
0 . W . B
1 . . . .
2 . B . W
3 . . . .
4 . . . .
```

```
TwoPhaseMovefrom=(2,1), to=(4,3)
Puzzle Board:
0 1 2 3
0 . W . B
1 . . . .
2 . . . W
3 . . . .
4 . . . B
```

```
TwoPhaseMovefrom=(0,3), to=(2,1)
Puzzle Board:
0 1 2 3
0 . W . .
1 . . . .
2 . B . W
3 . . . .
4 . . . B
```

```
TwoPhaseMovefrom=(2,3), to=(4,1)
Puzzle Board:
0 1 2 3
0 . W . .
1 . . . .
2 . B . .
3 . . . .
4 . W . B
```

```
TwoPhaseMovefrom=(0,1), to=(2,3)
Puzzle Board:
0 1 2 3
0 . . . .
1 . . . .
2 . B . W
3 . . . .
4 . W . B
```

```
TwoPhaseMovefrom=(2,1), to=(1,0)
Puzzle Board:
0 1 2 3
0 . . . .
1 B . . .
2 . . . W
3 . . . .
4 . W . B
```

```
TwoPhaseMovefrom=(2,3), to=(1,2)
Puzzle Board:
0 1 2 3
0 . . . .
1 B . W .
2 . . . .
3 . . . .
4 . W . B
```

```
TwoPhaseMovefrom=(4,1), to=(3,0)
Puzzle Board:
0 1 2 3
0 . . . .
1 B . W .
2 . . . .
3 W . . .
4 . . . B
```

```
TwoPhaseMovefrom=(1,0), to=(3,2)
Puzzle Board:
0 1 2 3
0 . . . .
1 . . W .
2 . . . .
3 W . B .
4 . . . B
```

```
TwoPhaseMovefrom=(1,2), to=(0,1)
Puzzle Board:
0 1 2 3
0 . W . .
1 . . . .
2 . . . .
3 W . B .
4 . . . B
```

```
TwoPhaseMovefrom=(3,0), to=(0,3)
Puzzle Board:
0 1 2 3
0 . W . W
1 . . . .
2 . . . .
3 . . B .
4 . . . B
```

```
TwoPhaseMovefrom=(3,2), to=(4,1)
Puzzle Board:
0 1 2 3
0 . W . W
1 . . . .
2 . . . .
3 . . . .
4 . B . B 
```


