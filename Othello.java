package org.cis120.othello;

import java.util.*;
import java.io.*;
import java.nio.file.Paths;

/**
 * This class serves as the Model for Othello. OthelloGameBoard will use an
 * instance of this class to handle the internal state of the game
 */
public class Othello {

    // value 0 -> Empty Space
    // value 1 -> Black Space
    // value 2 -> White Space
    private int[][] board;
    private int numTurns;
    private boolean isP1Turn;
    private boolean gameOver;
    // helps track is passes occur, if two passes occur, it's gameover
    private boolean passOccurred;
    private Set<Pair> validMoves;
    // add synchonrous scoring?
    private int blackScore;
    private int whiteScore;
    private String winner;
    // stores the game model
    // head represents the most recent turn
    // game history needs to written to the csv files
    LinkedList<StorageOthello> gameHistory;

    public Othello() {
        reset();
    }

    /**
     * Scores the current game board
     */
    public void score() {
        int newBlackScore = 0;
        int newWhiteScore = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) {
                    newBlackScore++;
                }
                if (board[i][j] == 2) {
                    newWhiteScore++;
                }
            }
        }
        blackScore = newBlackScore;
        whiteScore = newWhiteScore;
    }

    /**
     * check the validity of the moves,
     * compares the requested move against the valid moves
     */
    private boolean validMove(int r, int c) {
        // check if space is open and game isn't over
        if (board[r][c] != 0 || gameOver) {
            return false;
        }

        // now check if the move is in ValidMoves
        Pair testMove = new Pair(r, c);
        if (validMoves.contains(testMove)) {
            return true;
        }

        return false;
    }

    /**
     * Helper Method to if a move is valid in a given direction
     * 
     * @param r
     * @param c
     * @param vertical
     * @param horizontal
     * @param currentColor
     * @param oppositePiece
     * @return
     */
    private boolean checkMoveInDirection(
            int r, int c, int vertical, int horizontal,
            int currentColor, int oppositePiece
    ) {
        // apply the direction
        int i = r + vertical;
        int j = c + horizontal;
        // check for bounds and check the immediate piece is opposite color
        if (!inBounds(i, j)) {
            return false;
        } else {
            if (board[i][j] != oppositePiece) {
                return false;
            }
        }
        // while we're in bounds, keep moving in the direction on the board
        // till we find the opposite piece
        while (inBounds(i, j)) {
            if (board[i][j] == 0) {
                return false;
            }
            if (board[i][j] == currentColor) {
                // add the original move in to the valid moves
                validMoves.add(new Pair(r, c));
                return true;
            }
            i = i + vertical;
            j = j + horizontal;
        }
        return false;
    }

    /**
     * Helper function to check if coordinates are within the game board
     * 
     * @param i - row index
     * @param j - col index
     * @return - boolean indicating if the coordinates are within the game board
     */
    private boolean inBounds(int i, int j) {
        return i >= 0 && i < board.length && j >= 0 && j < board[0].length;
    }

    /**
     * flips the pieces based on a valid move
     * 
     * @param r
     * @param c
     */
    private void flipPieces(int r, int c) {
        // flip the pieces based on the piece to be inserted
        // follow similar stuff as findValidMoves
        // base this on current turn too
        int currentColor = 0;
        int oppositePiece = 0;
        if (isP1Turn) {
            currentColor = 1;
            oppositePiece = 2;
        } else {
            currentColor = 2;
            oppositePiece = 1;
        }

        // ensure the direction is valid (has a piece of the current color at the end)
        if (checkMoveInDirection(r, c, -1, -1, currentColor, oppositePiece)) {
            flipInDirection(r, c, -1, -1, currentColor, oppositePiece);
        }

        if (checkMoveInDirection(r, c, -1, 0, currentColor, oppositePiece)) {
            flipInDirection(r, c, -1, 0, currentColor, oppositePiece);
        }

        if (checkMoveInDirection(r, c, -1, 1, currentColor, oppositePiece)) {
            flipInDirection(r, c, -1, 1, currentColor, oppositePiece);
        }

        if (checkMoveInDirection(r, c, 0, -1, currentColor, oppositePiece)) {
            flipInDirection(r, c, 0, -1, currentColor, oppositePiece);
        }

        if (checkMoveInDirection(r, c, 0, 1, currentColor, oppositePiece)) {
            flipInDirection(r, c, 0, 1, currentColor, oppositePiece);
        }

        if (checkMoveInDirection(r, c, 1, -1, currentColor, oppositePiece)) {
            flipInDirection(r, c, 1, -1, currentColor, oppositePiece);
        }
        if (checkMoveInDirection(r, c, 1, 0, currentColor, oppositePiece)) {
            flipInDirection(r, c, 1, 0, currentColor, oppositePiece);
        }
        if (checkMoveInDirection(r, c, 1, 1, currentColor, oppositePiece)) {
            flipInDirection(r, c, 1, 1, currentColor, oppositePiece);
        }
    }

    /**
     * Helper function that flips the piece colors given a direction
     * 
     * @param r
     * @param c
     * @param vertical
     * @param horizontal
     * @param currentColor
     * @param oppositePiece
     */
    private void flipInDirection(
            int r, int c, int vertical, int horizontal,
            int currentColor, int oppositePiece
    ) {
        // apply the direction
        int i = r + vertical;
        int j = c + horizontal;
        // do the same iteration
        while (inBounds(i, j)) {
            // flips pieces until the other color is reached
            if (board[i][j] == currentColor) {
                // exit
                return;
            }
            // set the pieces to the opposite color
            if (board[i][j] == oppositePiece) {
                board[i][j] = currentColor;
            }
            i = i + vertical;
            j = j + horizontal;
        }
    }

    /**
     * Populates the ValidMoves set with all the valid moves for the current
     * player's turn
     */
    private void findValidMoves() {
        validMoves.clear();
        int currentColor = 0;
        int oppositePiece = 0;
        if (isP1Turn) {
            currentColor = 1;
            oppositePiece = 2;
        } else {
            currentColor = 2;
            oppositePiece = 1;
        }
        // now check all the directions for every point
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] != 0) {
                    continue;
                }
                checkMoveInDirection(row, col, -1, -1, currentColor, oppositePiece);
                checkMoveInDirection(row, col, -1, 0, currentColor, oppositePiece);
                checkMoveInDirection(row, col, -1, 1, currentColor, oppositePiece);
                checkMoveInDirection(row, col, 0, -1, currentColor, oppositePiece);
                checkMoveInDirection(row, col, 0, 1, currentColor, oppositePiece);
                checkMoveInDirection(row, col, 1, -1, currentColor, oppositePiece);
                checkMoveInDirection(row, col, 1, 0, currentColor, oppositePiece);
                checkMoveInDirection(row, col, 1, 1, currentColor, oppositePiece);
            }
        }
    }

    /**
     * Begins the procedure for declaring a winning and prevents further interaction
     */
    public void checkPass() {
        // first check for passes
        // else if validMoves is empty and last turn wasn't a pass; switch the turn
        // else validMoves is empty and last turn was a pass, start the gameOver process
        score();
        if (!validMoves.isEmpty()) {
            passOccurred = false;
            return;
        }

        if (passOccurred) {
            gameOver();
            return;
        } else {
            isP1Turn = !isP1Turn;
            passOccurred = true;
            findValidMoves();
            return;
        }
    }

    /**
     * Begins the procedure for declaring a winning and prevent further interaction
     */
    private void gameOver() {
        gameOver = true;
        // determine the winner
        if (blackScore > whiteScore) {
            winner = "Black";
        } else if (blackScore < whiteScore) {
            winner = "White";
        } else {
            winner = "Draw";
        }
    }

    public boolean playTurn(int r, int c) {
        if (!inBounds(r, c)) {
            return false;
        }
        // first check if players must pass or if the game is over
        checkPass();
        // check if the turn is valid
        if (!validMove(r, c)) {
            return false;
        }
        // if it is, then add the piece
        if (isP1Turn) {
            board[r][c] = 1;
        } else {
            board[r][c] = 2;
        }
        // flip the piece colors
        flipPieces(r, c);
        // increment numTurns
        numTurns++;
        // flip the player turns
        isP1Turn = !isP1Turn;
        // recalculate valid moves
        findValidMoves();
        // record the gameState here
        StorageOthello newTurn = new StorageOthello(
                this.getGameBoard(), this.getNumTurns(),
                this.getCurrentPlayer(), this.getPassOccurred()
        );
        gameHistory.addFirst(newTurn);
        return true;
    }

    /**
     * reset to the default Othello board
     * black always goes first
     */
    public void reset() {
        board = new int[8][8];
        // add the 4 pieces in the middle
        board[3][3] = 2;
        board[4][4] = 2;
        board[3][4] = 1;
        board[4][3] = 1;

        numTurns = 0;
        isP1Turn = true;
        gameOver = false;
        passOccurred = false;
        validMoves = new TreeSet<Pair>();
        winner = "In Progress";
        gameHistory = new LinkedList<StorageOthello>();

        // put turn zero don't put in reference but in copies
        StorageOthello newTurn = new StorageOthello(
                this.getGameBoard(), this.getNumTurns(),
                this.getCurrentPlayer(), this.getPassOccurred()
        );
        gameHistory.addFirst(newTurn);

        findValidMoves();
        checkPass();
    }

    /**
     * undoes the previous move using builtIn LinkedList functionality
     */
    public boolean undoMove() {
        // cannot undo when a gameOver occurs
        if (gameOver) {
            return false;
        }
        // handles undoing with only 1 turn in history
        if (gameHistory.size() == 1) {
            return false;
        }
        // delete the current head
        gameHistory.removeFirst();
        // load the new head
        loadLastTurn();
        return true;
    }

    /**
     * allows the loading of game from a given gameBoard, number of turns,
     * currentTurn, and gameover state
     */
    private void loadLastTurn() {
        StorageOthello curr = gameHistory.peekFirst();
        this.board = curr.getBoard();
        this.numTurns = curr.getNumTurns();
        this.isP1Turn = curr.getIsP1Turn();
        this.passOccurred = curr.getPassOccurred();
        findValidMoves();
        score();
        checkPass();
    }

    public void saveToGameFile() {
        // write the at least the previous 4 turns down game history down
        File file = Paths.get("files/saved_othello.csv").toFile();
        BufferedWriter bw = null;
        try {
            FileWriter fw = new FileWriter(file, false);
            bw = new BufferedWriter(fw);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Oh noey.");
        } catch (IOException e) {
            System.out.println("Error occurred while constructing the Writer");
        }

        // calculate how many turns to write
        int turnsToWrite = 0;
        int turnsPlayed = gameHistory.size();

        if (turnsPlayed >= 4) {
            turnsToWrite = 4;
        } else {
            turnsToWrite = turnsPlayed;
        }
        // write the next four turns, most recent on top
        for (int i = 0; i < turnsToWrite; i++) {
            // use turns wrote to retrieve a turn
            StorageOthello o = gameHistory.get(i);
            // call generateStringsTo get each turn as 9 strings
            List<String> turnAsStrings = generateStringsForATurn(o);
            // write the list of strings to generate
            try {
                for (String toWrite : turnAsStrings) {
                    bw.write(toWrite);
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error occurred during the writing process");
                return;
            }
        }

        // flush the file
        try {
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("Error occurred when flushing and closing the file");
            return;
        }
    }

    /*
     * Converts a turn into a list of Strings
     */
    private List<String> generateStringsForATurn(StorageOthello o) {
        List<String> turnAsStrings = new LinkedList<String>();
        // 9 lines per turn
        // numTurns, isP1Turn, passOccurred
        String stateString = "" + o.getNumTurns() +
                "," + o.getIsP1Turn() + "," + o.getPassOccurred();
        turnAsStrings.add(stateString);

        int[][] turnBoard = o.getBoard();
        // 8 lines for the boardSpaces
        // make sure to read row by row (so increment by the first index of int[][])
        for (int i = 0; i < 8; i++) {
            // do the first board entry to prevent trailing commas
            String boardRow = "" + turnBoard[i][0];
            for (int j = 1; j < 8; j++) {
                boardRow = boardRow + "," + turnBoard[i][j];
            }
            turnAsStrings.add(boardRow);
        }

        return turnAsStrings;
    }

    public void loadFromGameFile() {
        File file = Paths.get("files/saved_othello.csv").toFile();
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            return;
        } catch (NullPointerException e) {
            System.out.println("Null file path");
            return;
        }

        // generate the gameHistory from the file

        // clear the existing game history
        this.gameHistory.clear();

        // the strings at the top will represent the most recent turn
        try {
            // handle the case where the file is empty
            while (br.ready()) {
                // array should only be 9 strings
                String[] turnAsString = new String[9];
                for (int i = 0; i < 9; i++) {
                    // feed a List of 9 lines to the
                    String line = br.readLine();
                    turnAsString[i] = line;
                }
                // convert the String to a StorageOthello for use
                StorageOthello turnToAdd = generateTurnFromString(turnAsString);
                gameHistory.addLast(turnToAdd);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error occurred while reading. Game reset");
            this.reset();
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Game reset: " + e.getMessage());
            this.reset();
            return;
        }
        loadLastTurn();
        // update the state to check for gameOver's
        checkPass();
    }

    private StorageOthello generateTurnFromString(String[] turnAsString) {

        // split each string into arrays, 1st array is length 3, next 8 are length 8
        // throw an illegalArugement Exception for inValidStrings
        String[] firstLine = turnAsString[0].split(",");
        if (firstLine.length > 3) {
            throw new IllegalArgumentException("Bad format of Game state line.");
        }
        for (int i = 0; i < 3; i++) {
            if (!checkValidRead(firstLine[i], true)) {
                throw new IllegalArgumentException("Illegal First Line: " + firstLine[i]);
            }
        }
        int numTurnsToAdd = Integer.parseInt(firstLine[0]);
        boolean isP1TurnToAdd = Boolean.parseBoolean(firstLine[1]);
        boolean passOccurredToAdd = Boolean.parseBoolean(firstLine[2]);

        // now fill the board
        int[][] boardToAdd = new int[8][8];
        for (int i = 1; i < 9; i++) {
            String[] boardRow = turnAsString[i].split(",");
            if (boardRow.length > 8) {
                throw new IllegalArgumentException("Bad format of Game Board lines");
            }

            // check for valid strings within each row
            for (int j = 0; j < 8; j++) {
                if (!checkValidRead(boardRow[j], false)) {
                    throw new IllegalArgumentException("Illegal In board: " + boardRow[j]);
                }
                boardToAdd[i - 1][j] = Integer.parseInt(boardRow[j]);
            }
        }
        int[][] boardCopy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardCopy[i][j] = boardToAdd[i][j];
            }
        }

        // generate the StorageOthello
        StorageOthello turn = new StorageOthello(
                boardCopy, numTurnsToAdd,
                isP1TurnToAdd, passOccurredToAdd
        );
        return turn;
    }

    /*
     * Checks if a string is valid to be converted for use as in a StorageOthello
     */
    private boolean checkValidRead(String test, boolean isFirstLine) {
        // check if the first line is int, boolean, boolean
        // check if the next 8 lines are all 0's, 1's, or 2's
        boolean isValid = false;
        if (isFirstLine) {
            if (test.equals("false") || test.equals("true")) {
                isValid = true;
            } else {
                try {
                    Integer.parseInt(test);
                    isValid = true;
                } catch (NumberFormatException e) {
                    isValid = false;
                }
            }
        } else {
            try {
                int testInt = Integer.parseInt(test);

                if (testInt == 0 || testInt == 1 || testInt == 2) {
                    isValid = true;
                } else {
                    isValid = false;
                }

            } catch (NumberFormatException e) {
                isValid = false;
            }
        }
        return isValid;
    }

    // utility getters
    /**
     * returns the string containing the winner
     */
    public String getWinner() {
        return winner;
    }

    /**
     * returns the boolean value indicating if the game is over
     * 
     * @return
     */
    public boolean getGameOver() {
        return gameOver;
    }

    // return the boolean the says if it is black's turns
    public boolean getCurrentPlayer() {
        return isP1Turn;
    }

    /**
     * Returns the score of the black player
     * 
     * @return integer representing the score of the black player
     */
    public int getBlackScore() {
        return blackScore;
    }

    /**
     * Returns the score of the white player
     * 
     * @return integer representing the score of the white player
     */
    public int getWhiteScore() {
        return whiteScore;
    }

    /*
     * returns the value of the input game space
     */
    public int getCell(int r, int c) {
        return board[r][c];
    }

    /*
     * returns a set of valid moves for the currentPlayer
     */
    public Set<Pair> getValidMoves() {
        Set<Pair> copiedSet = new TreeSet<Pair>();
        for (Pair p : validMoves) {
            Pair copyToAdd = new Pair(p.getFirst(), p.getSecond());
            copiedSet.add(copyToAdd);
        }
        return copiedSet;
    }

    /**
     * Generates an copied array of the current game board
     * 
     * @return
     */
    public int[][] getGameBoard() {
        int[][] boardCopy = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    /**
     * returns the number of turns
     * 
     * @return
     */
    public int getNumTurns() {
        return numTurns;
    }

    /**
     * returns the boolean representing if a passOccurred in the previous turn
     * 
     * @return
     */
    public boolean getPassOccurred() {
        return passOccurred;
    }

    /**
     * Generate a copied list of the current gameHistory
     * 
     * @return
     */
    public LinkedList<StorageOthello> getGameHistory() {
        LinkedList<StorageOthello> copy = new LinkedList<StorageOthello>();
        for (StorageOthello o : gameHistory) {
            StorageOthello copyToAdd = new StorageOthello(
                    o.getBoard(), o.getNumTurns(),
                    o.getIsP1Turn(), o.getPassOccurred()
            );
            copy.add(copyToAdd);
        }
        return copy;
    }

    // setters for only for testing purposes
    // private methods in public release
    public void setGameSpace(int r, int c, int val) {
        board[r][c] = val;
    }

    public void changeTurn() {
        isP1Turn = !isP1Turn;
    }

    public void setBoard(int[][] inputBoard, boolean turnDesired, boolean passOccurred) {
        for (int i = 0; i < inputBoard.length; i++) {
            for (int j = 0; j < inputBoard[i].length; j++) {
                board[i][j] = inputBoard[i][j];
            }
        }
        isP1Turn = turnDesired;
        numTurns++;
        this.passOccurred = passOccurred;
        StorageOthello newTurn = new StorageOthello(
                this.getGameBoard(), this.getNumTurns(),
                this.getCurrentPlayer(), this.getPassOccurred()
        );
        gameHistory.addFirst(newTurn);

        findValidMoves();
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nTurn " + numTurns + ":\n");
        System.out.println("Black: " + blackScore + " White: " + whiteScore);
        System.out.println("passOccurred: " + passOccurred + " gameOver: " + gameOver);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 7) {
                    System.out.print(" | ");
                }
            }
            if (i < 7) {
                System.out.println("\n-----------------------------");
            }
        }
        System.out.println();
        System.out.println("Winner: " + winner);
    }

    /**
     * the set of valid moves prints the current game state
     * for debugging.
     */
    public void printValidMoves() {
        Set<Pair> test = this.getValidMoves();
        this.findValidMoves();
        for (Pair p : test) {
            System.out.println(p);
        }
    }

    /**
     * Allows the comparison of the internally stored board with an external board
     * Good for testing
     * 
     * @param inputBoard
     * @return boolean representing if the input board is equal to the internal
     *         board
     */
    public boolean compareBoard(int[][] inputBoard) {
        for (int i = 0; i < inputBoard.length; i++) {
            for (int j = 0; j < inputBoard[i].length; j++) {
                if (inputBoard[i][j] != board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
    }

}