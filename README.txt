=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: agrav
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2-D arrays: I use a 2-D array of Pieces in my Board class in order to create the grid for the
  game board. Since each Piece needs to be accessible (often by its row and column positions) in
  many different scenarios, this was the best way to display this. I also use a 2-D array of
  boolean values when randomly assigning the bombs to the Pieces in the Board.

  2. Recursion: I use recursion in my Board class (specifically in the getNumbers method) in order
  to determine the number of bombs adjacent to a given Piece. The function recurses in the event
  that a piece has no adjacent bombs, because the game then needs to show the adjacent bomb counts
  of all eight pieces adjacent to that piece. (If any of those do not have adjacent bombs, then it
  needs to recurse through that piece's surrounding pieces, and so on and so forth.)
  
  3. I/O: I use I/O in order to handle high scores. The game starts with an initial text file
  (highscores.txt) that contains some basic headings. It uses this to read in the high scores when
  the user views the high scores in the game. When the user wins a game, if they set a high score
  (i.e. win in a time faster than one of the other high scores), they will be prompted to submit
  their name, and their name and their score will be written to highscores.txt. Only five high
  scores are saved per difficulty, and they are displayed based upon which difficulty the scores
  are associated with. 
  
  4. Testable component: I use JUnit to test my getters and setters for Board and Piece, especially
  for some methods like getNumAdjacent() in Piece, which should return -1 if it has not yet been
  set. I also made sure to test my setLevel method from Board to make sure that it changes that
  board's number of pieces and bombs. Similarly, I wanted to make sure that my reset() function
  maintains the number of pieces and bombs for a board, rather than switching back to the initial
  size (which is numPieces = 9 and numBombs = 10).

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
    The Game class builds the GUI for everything surround the board, including the reset button at
the top, the instructions button at the bottom, and the settings button at the bottom.
    The Board class builds the game board and handles MouseEvents within the board. It also creates
the bombs left tracker, status, and timer.
    The Piece class builds the individual pieces that make up the game board. It also handles
drawing all involved shapes, including bombs and flags. 

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
    Nope!

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
    I planned out most of my design before I began coding, and I think it definitely made the
process easier. If given the chance, I think I would like to find out a slightly more efficient way
of handling MouseEvents for the individual Pieces. There are also a couple methods that are
lengthy (such as my highscores method in Board) that could benefit from helper functions.
    I believe my private states are encapsulated well, as I wrote plenty of getters and setters.
The few variables I left public (such as SIZE in Piece) are all final variables, and are public out
of necessity (SIZE must be accessed by Board when calculating positions for Pieces). Similarly, my
public methods in Piece are necessary because they need to be accessed in Board (such as when the
user left-clicks on a Piece, and that Piece's leftClicked property must be changed to true.

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
    I looked at some sources that were linked by TAs on Piazza, namely this one on MouseEvents
(http://www.java2s.com/Code/Java/Event/Handlemousebuttonclickevent.htm), this one on dialogs
(http://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html), and this one on tabbed
panes (http://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html).

===============================
=: To run on Linux computers :=
===============================

javac -cp junit.jar *.java && java Game
