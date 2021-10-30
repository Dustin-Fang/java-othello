package org.cis120.othello;

/**
 * This is a utility class to help model the Othello game's history
 * 
 * @author fdustin
 *
 */
final class StorageOthello {
    private final int[][] board;
    private final int numTurns;
    private final boolean isP1Turn;
    private final boolean passOccurred;

    public StorageOthello(
            int[][] board, int numTurns, boolean isP1Turn,
            boolean passOccurred
    ) {
        this.board = board;
        this.numTurns = numTurns;
        this.isP1Turn = isP1Turn;
        this.passOccurred = passOccurred;
    }

    /**
     * Generates a shallow copy of the board
     * 
     * @return
     */
    public int[][] getBoard() {
        int[][] boardCopy = new int[8][8];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public int getNumTurns() {
        return numTurns;
    }

    public boolean getIsP1Turn() {
        return isP1Turn;
    }

    public boolean getPassOccurred() {
        return passOccurred;
    }

    @Override
    public String toString() {
        String toReturn = "";
        toReturn = toReturn + "Turn " + numTurns + ":\n";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                toReturn = toReturn + board[i][j];
                if (j < 7) {
                    toReturn = toReturn + " | ";
                }
            }
            if (i < 7) {
                toReturn = toReturn + "\n-----------------------------\n";
            }
        }
        toReturn = toReturn + "\n Whose Turn: " + isP1Turn + "\n";
        return toReturn;
    }

}