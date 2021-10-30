=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: fdustin
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
     I use an 2D int array to represent the pieces on the Othello game board.
     0's represent empty space, 1's represent black spaces, and 2's represent
     white spaces.

  2. Collections and Maps
     I use a LinkedList to store a history of turns in order to allow players to
     undo previous moves. I also use a TreeSet to store the validMoves for each
     player based on the current turn and board state. This allows me to 
     track, display, and validate the moves that a player can make given a board
     state.

  3. File I/O
     Using the CSV file saved_othello.csv, I have implemented persistent game state.
     Players are able to save up to the last 4 turns and load this state while playing
     the game. Undo's can still be done; however, a maximum of 3 undo commands can be made
     as at most 4 turns are stored.

  4. JUnit Testing
     Since internal game state was modeled with GUI elements and the game is played
     in turns, the entire game can be played within JUnit tests. 

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Othello.java - This class serves as the model of the game. It maintains and updates
                 internal state using defined methods. 
  Pair.java - This is a utility class that store pairs of two ints. This is primarily used
              to represent spaces on the game board to easily store and track valid moves.
  StorageOthello.java - This is a utlity class, where each instance abstracts a turn made
                        in the Othello game. This class is used to store previous turn states
                        for undo functionality and allows games to be saved and loaded.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

    I would refactor my GameOver checking a bit. I check for turns to be passed
    and gameOvers with the same function. I would seperate these in the future.
    
    In terms of encapsulation, I made sure to generate copies when using retrieving
    fields or data in reference classes. However, there are methods defined in Othello.java
    that allow modification of the internal state, but these methods are primarily used for
    JUnit testing. These methods would not be found in a public release.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
