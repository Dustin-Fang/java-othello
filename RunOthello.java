package org.cis120.othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * This is the top level interface definition that handles the game
 * 
 * @author fdustin
 *
 */
public class RunOthello implements Runnable {
    private final String instructionsText = "These are the instructions for Othello: \n"
            + "Othello is played by placing black or white squares that "
            + "'outflank' the opponent's pieces. "
            + "Pieces are played such that they are adjacent to an opponent piece "
            + "and a piece of the same color can be found in the direction of the adjacent "
            + "opponent piece. "
            + "After playing a piece, the opposing color's pieces that have been "
            + "'outflanked' flip and become the opposite color. "
            + "I've developed a system to highlight the availible moves to each player. "
            + "The color of the open circles as well as the text at the top indicate "
            + "which player the valid moves are for. "
            + "Click on those spaces to play a turn. "
            + "Players keep playing to capture the opponent's pieces. "
            + "If a player has no availible moves, their turn is passed. "
            + "When neither player can make a move or when the board is full, the game ends. "
            + "At the end, whichever player has the most pieces faceup wins. "
            + "Scores are tracked at the top. "
            + "Black always goes first. \n\n "
            + "Button functions: \n "
            + "Use 'reset' to reset the game to the beginning. \n "
            + "Use 'undo' to undo the previous turn. "
            + "Note you cannot undo if your turn was passed or the game ends. \n "
            + "Use 'save' to save the current game to a default csv file. "
            + "Only the previous 4 (or less) turns will be saved. \n "
            + "Use 'load' to load a saved game. This will reset the game if the file isn't "
            + "formatted properly or a game isn't saved.";

    public void run() {

        // Instructions Frame
        final JFrame instructionFrame = new JFrame("Instructions for Othello");
        instructionFrame.setLocation(50, 100);
        instructionFrame.setMinimumSize(new Dimension(500, 500));

        // add a JTextArea for instructions
        final JTextArea instructionsText = new JTextArea();
        instructionsText.setLineWrap(true);
        instructionsText.setWrapStyleWord(true);
        instructionsText.setEditable(false);
        instructionsText.setText(this.instructionsText);

        instructionFrame.add(instructionsText, BorderLayout.NORTH);
        // add a button to this frame to hide it
        final JButton okInstructions = new JButton("Ok");
        okInstructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                instructionFrame.setVisible(false);
            }
        });
        instructionFrame.add(okInstructions, BorderLayout.SOUTH);
        instructionFrame.pack();
        instructionFrame.setVisible(false);

        // Top-level frame
        final JFrame topLevelFrame = new JFrame("Othello");
        topLevelFrame.setLocation(50, 100);

        // Status panel
        final JPanel status_panel = new JPanel();
        topLevelFrame.add(status_panel, BorderLayout.NORTH);
        final JLabel setUpStatus = new JLabel("Setting up...");

        final JLabel blackScore = new JLabel("");
        final JLabel whiteScore = new JLabel("");

        status_panel.add(setUpStatus);
        status_panel.add(blackScore);
        status_panel.add(whiteScore);

        // Game board creation with the labels
        final OthelloGameBoard board = new OthelloGameBoard(setUpStatus, blackScore, whiteScore);
        topLevelFrame.add(board, BorderLayout.CENTER);

        /// Control_panel
        final JPanel control_panel = new JPanel();
        control_panel.setLayout(new GridLayout(5, 1));
        topLevelFrame.add(control_panel, BorderLayout.EAST);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.reset();
            }
        });
        control_panel.add(reset);

        final JButton undo = new JButton("Undo");
        undo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.undo();
            }
        });
        control_panel.add(undo);

        final JButton save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add the file saving functionality
                board.save();
                JOptionPane.showMessageDialog(topLevelFrame, "Game Saved");
            }
        });
        control_panel.add(save);

        final JButton load = new JButton("Load");
        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add the file loading functionality
                board.load();
                JOptionPane.showMessageDialog(topLevelFrame, "Game Loaded");
            }
        });
        control_panel.add(load);

        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // make the instruction visible here
                instructionFrame.setVisible(true);
            }
        });
        control_panel.add(instructions);

        topLevelFrame.setMinimumSize(new Dimension(930, 875));

        // Put the frame on the screen
        topLevelFrame.pack();
        topLevelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        topLevelFrame.setVisible(true);

        // Start the game
        board.reset();

    }

}