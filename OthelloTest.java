package org.cis120.othello;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

// Tests for the game model
public class OthelloTest {
    private Othello o;
    // fresh board after reset
    private final int[][] resetBoard = { { 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 2, 1, 0, 0, 0 },
        { 0, 0, 0, 1, 2, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0 } };
    // board that is finished
    private final int[][] staleMateBoard = { { 2, 2, 2, 2, 2, 2, 2, 2 },
        { 2, 2, 2, 2, 2, 2, 2, 2 },
        { 2, 2, 2, 2, 2, 2, 2, 2 },
        { 2, 2, 2, 2, 2, 2, 2, 0 },
        { 2, 2, 2, 2, 2, 2, 0, 0 },
        { 2, 2, 2, 2, 2, 2, 0, 1 },
        { 2, 2, 2, 2, 2, 2, 2, 0 },
        { 2, 2, 2, 2, 2, 2, 2, 2 } };

    @BeforeEach
    public void setUp() {
        // We initialize a fresh Othello game for each test
        o = new Othello();
    }

    @Test
    public void testResetAndConstructor() {
        assertTrue(o.compareBoard(resetBoard));
        assertEquals(o.gameHistory.size(), 1);
        assertFalse(o.getGameOver());
        assertTrue(o.getCurrentPlayer());
        assertEquals(o.getNumTurns(), 0);
        assertFalse(o.getPassOccurred());
        assertEquals(o.getBlackScore(), 2);
        assertEquals(o.getWhiteScore(), 2);
    }

    @Test
    public void testPlayTurnValid() {
        assertTrue(o.playTurn(3, 2));
        // should be player2's turn
        assertFalse(o.getCurrentPlayer());
        int[][] expectedBoard = { { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 1, 1, 0, 0, 0 },
            { 0, 0, 0, 1, 2, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 } };
        assertTrue(o.compareBoard(expectedBoard));
    }

    @Test
    public void testPlayTurnFilledSpace() {
        assertFalse(o.playTurn(4, 4));
        assertTrue(o.getCurrentPlayer());
        assertTrue(o.compareBoard(resetBoard));
    }

    @Test
    public void testPlayTurnInvalidSpace() {
        assertFalse(o.playTurn(2, 2));
        assertTrue(o.getCurrentPlayer());
        assertTrue(o.compareBoard(resetBoard));
    }

    @Test
    public void testPlayMultipleTurns() {
        assertTrue(o.playTurn(3, 2));
        assertTrue(o.playTurn(2, 2));
        assertTrue(o.getCurrentPlayer());
        int[][] expectedBoard = { { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 2, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 2, 1, 0, 0, 0 },
            { 0, 0, 0, 1, 2, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 } };
        assertTrue(o.compareBoard(expectedBoard));
    }

    // test undo move functionality
    @Test
    public void testUndoNoMoves() {
        assertFalse(o.undoMove());
        assertTrue(o.getCurrentPlayer());
        assertTrue(o.compareBoard(resetBoard));
    }

    @Test
    public void testUndoOnce() {
        o.playTurn(3, 2);
        assertTrue(o.undoMove());
        assertTrue(o.getCurrentPlayer());
        assertTrue(o.compareBoard(resetBoard));
    }

    @Test
    public void testUndoBothPlayers() {
        o.playTurn(3, 2);
        o.playTurn(2, 2);
        assertTrue(o.undoMove());
        assertTrue(o.undoMove());
        assertTrue(o.getCurrentPlayer());
        assertTrue(o.compareBoard(resetBoard));
    }

    @Test
    public void testUndoSamePlayerMultipleTimes() {
        o.playTurn(3, 2);
        assertTrue(o.undoMove());
        o.playTurn(2, 3);
        assertTrue(o.undoMove());
        assertTrue(o.getCurrentPlayer());
        assertTrue(o.compareBoard(resetBoard));
    }

    // valid turn checking
    // one case for black
    // one case for white
    // check the valid turns are being calculated properly
    @Test
    public void testBlackNoMoves() {
        o.setBoard(staleMateBoard, true, false);
        assertTrue(o.getValidMoves().isEmpty());
    }

    @Test
    public void testResetBoardBeginningValidMoves() {
        Set<Pair> expectedValidMoves = new TreeSet<Pair>();
        expectedValidMoves.add(new Pair(2, 3));
        expectedValidMoves.add(new Pair(3, 2));
        expectedValidMoves.add(new Pair(4, 5));
        expectedValidMoves.add(new Pair(5, 4));
        assertEquals(expectedValidMoves, o.getValidMoves());
    }

    @Test
    public void testOneTurnWhiteValidMoves() {
        assertTrue(o.playTurn(2, 3));
        // now check white's valid moves here
        Set<Pair> expectedValidMoves = new TreeSet<Pair>();
        expectedValidMoves.add(new Pair(2, 2));
        expectedValidMoves.add(new Pair(2, 4));
        expectedValidMoves.add(new Pair(4, 2));
        assertEquals(expectedValidMoves, o.getValidMoves());
    }

    // check for a situation where a player must pass

    // check for game overs
    // check if winner is calculated properly
    @Test
    public void testWhiteAsWinner() {
        o.setBoard(staleMateBoard, true, false);
        // should pass to white's turn
        o.checkPass();
        assertTrue(o.getPassOccurred());
        assertFalse(o.getCurrentPlayer());
        assertTrue(o.getPassOccurred());
        // then white should pass
        o.checkPass();
        // gameOver here
        assertTrue(o.getWinner().equals("White"));
        assertTrue(o.getGameOver());
    }

    // check for playing a turn when gameOver is true
    @Test
    public void playTurnWhenGameOver() {
        o.setBoard(staleMateBoard, true, false);
        // black attempts to play
        assertFalse(o.playTurn(1, 2));
        o.checkPass();
        // white attempts to play
        assertFalse(o.playTurn(1, 2));
        o.checkPass();
        // should be equivalent (these are deep copies)
        assertTrue(o.compareBoard(staleMateBoard));
    }

    @Test
    public void undoMoveWhenGameOver() {
        // white's turn, trigger a gameOver
        o.setBoard(staleMateBoard, false, true);
        o.checkPass();
        assertFalse(o.undoMove());
    }

    // test scoring here
    @Test
    public void testScoringEndGame() {
        o.setBoard(staleMateBoard, true, false);
        o.score();
        assertEquals(58, o.getWhiteScore());
        assertEquals(1, o.getBlackScore());
    }

    // test loading a game state to the game
    @Test
    public void testSaveAndLoad() {
        assertTrue(o.playTurn(3, 2));
        o.saveToGameFile();
        o.reset();
        assertTrue(o.compareBoard(resetBoard));
        o.loadFromGameFile();
        int[][] expectedBoard = { { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 1, 1, 0, 0, 0 },
            { 0, 0, 0, 1, 2, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0 } };
        assertTrue(o.compareBoard(expectedBoard));
    }

}
