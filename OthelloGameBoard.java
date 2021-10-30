package org.cis120.othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * This class will serve as the View-Controller for the Othello game
 * Thus, it will handle user interaction and update the an instance
 * of the Othello class that models the game state.
 *
 *
 */
@SuppressWarnings("serial")
public class OthelloGameBoard extends JPanel {

    private Othello o;
    // these are labels that need to access the game state
    // indicates where in the setupProcess and the runningState
    // will display winner and whose turn it is and general state of the game
    private JLabel setupStatus;
    private JLabel blackScore;
    private JLabel whiteScore;

    // Game constants 8x8 grid
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    // constructor for OthelloGameBoard
    // we pass in references for labels we want to update using the game's internal
    // state
    public OthelloGameBoard(JLabel setupStatus, JLabel blackScore, JLabel whiteScore) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);

        o = new Othello();
        this.setupStatus = setupStatus;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // will vary from 0 to 7
                o.playTurn(p.y / 100, p.x / 100);
                // checks if turn needs to be passed, flips turn if so
                o.checkPass();

                // if a turn did pass, run again and see if we need to gameOver
                if (o.getPassOccurred()) {
                    checkGame();
                    return;
                }
                updateStatus();
                repaint();
            }
        });
    }

    /*
     * Handle when a turn had been passed
     */
    public void checkGame() {
        // check for a gameOver
        o.checkPass();
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    /*
     * Resets the internal game state
     */
    public void reset() {
        o.reset();
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    /*
     * Undoes the previous move
     */
    public void undo() {
        o.undoMove();
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    /*
     * Saves the previous 4 turns or less
     */
    public void save() {
        o.saveToGameFile();
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    /*
     * Loads the saved game history from files/saved_othello.csv
     */
    public void load() {
        o.loadFromGameFile();
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    /**
     * Will handle updating the state of the game
     * Needs to update the setup/turn status
     * Check for a winner
     * Update the scores
     */
    public void updateStatus() {

        blackScore.setText("Black Score: " + o.getBlackScore());
        whiteScore.setText("White Score: " + o.getWhiteScore());

        // gameOver happened
        if (o.getGameOver()) {
            setupStatus.setText("Game Over: " + o.getWinner() + " wins!");
            return;
        }

        // if a gameOver didn't occur, but a passed turn occurred
        if (o.getPassOccurred()) {
            if (o.getCurrentPlayer()) {
                setupStatus.setText("White's turn passed. Black's Turn");
            } else {
                setupStatus.setText("Black's turn passed. White's Turn");
            }
            return;
        }

        if (o.getCurrentPlayer()) {
            setupStatus.setText("Black's Turn");
        } else {
            setupStatus.setText("White's Turn");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.GREEN);
        // draw the board grid here
        // do this based on the cell values
        // Draws board grid
        int gridSideLength = 100;
        int pos = 0;
        for (int i = 0; i < 9; i++) {
            // nine lines vertical
            g.drawLine(pos, 0, pos, BOARD_HEIGHT);
            // nine lines horizontal
            g.drawLine(0, pos, BOARD_WIDTH, pos);
            pos += gridSideLength;
        }

        // draw the current pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int val = o.getCell(i, j);
                // draw black piece
                if (val == 1) {
                    g.setColor(Color.black);
                    g.fillOval(10 + 100 * j, 10 + 100 * i, 80, 80);
                }
                // draw white piece
                if (val == 2) {
                    g.setColor(Color.white);
                    g.fillOval(10 + 100 * j, 10 + 100 * i, 80, 80);
                }

            }
        }

        // draw the valid moves
        // also draw the valid circle spaces as a different color - based on whose turn
        // it is
        Set<Pair> validMoves = o.getValidMoves();
        for (Pair p : validMoves) {
            if (o.getCurrentPlayer()) {
                g.setColor(Color.black);
            } else {
                g.setColor(Color.white);
            }
            g.drawOval(10 + 100 * p.getSecond(), 10 + 100 * p.getFirst(), 80, 80);
        }
        // set the color back??
        g.setColor(Color.black);
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
