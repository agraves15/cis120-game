import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.*;

/**
 * Board
 * 
 * A game board made up of Pieces that the users interact with in order to play the game.
 */
@SuppressWarnings("serial")
public class Board extends JPanel {
    private int numPieces;
    private int numBombs;
    private Piece[][] pieces;
    
    private char level = 'B';
    private String status = "";
    private boolean gameOver = false;
    
    // dimensions of surrounding board area (including around pieces)
    public static final int BOARD_WIDTH = 400;
    public static final int BOARD_HEIGHT = 400;
    
    // interval for timer
    public static final int INTERVAL = 1000;
    private int seconds = 0;
    private boolean timeStarted = false;
    
    /**
     * constructor sets Board's number of pieces (the value indicates the number of pieces on one
     * side, meaning that for a 9 x 9 Board, numPieces is 9) and the number of bombs using given
     * values. It also handles MouseEvents and starts the timer.
     */
    public Board(int numPieces, int numBombs) {
        this.numPieces = numPieces;
        this.numBombs = numBombs;
        pieces = new Piece[numPieces][numPieces];
        assignBombs();
        
        // creates border around the game board
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        // timer to count the seconds since game was started
        final Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        
        // mouseAdapter checks if the user has clicked in any of the Pieces
        // if so, the Piece's rightClicked or leftClicked is updated and the board is repainted 
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // start timer on first click
                if (!timeStarted) {
                    timer.start();
                    timeStarted = true;
                }
                if (!gameOver) {
                    // check for right click (either through right click or left click + control)
                    if ((e.getButton() == MouseEvent.BUTTON1 && e.isControlDown()) ||
                         e.getButton() == MouseEvent.BUTTON3) {
                        for (int i = 0; i < getNumPieces(); i++) {
                            for (int j = 0; j < getNumPieces(); j++) {
                                if (e.getX() > pieces[i][j].getX() &&
                                    e.getX() < pieces[i][j].getX() + Piece.SIZE &&
                                    e.getY() > pieces[i][j].getY() &&
                                    e.getY() < pieces[i][j].getY() + Piece.SIZE &&
                                    !pieces[i][j].getLeft()) {
                                    if (pieces[i][j].getRight()) {
                                        pieces[i][j].setRight(false);
                                    } else {
                                        pieces[i][j].setRight(true);
                                    }
                                    if (numClicked() == getNumBombs()) {
                                        checkWin();
                                    }
                                    repaint();
                                    if (gameOver) {
                                        highscores();
                                    }
                                }
                            }
                        }
                    } else if (e.getButton() == MouseEvent.BUTTON1) {
                        // check for left click
                        for (int i = 0; i < getNumPieces(); i++) {
                            for (int j = 0; j < getNumPieces(); j++) {
                                if (e.getX() > pieces[i][j].getX() &&
                                    e.getX() < pieces[i][j].getX() + Piece.SIZE &&
                                    e.getY() > pieces[i][j].getY() &&
                                    e.getY() < pieces[i][j].getY() + Piece.SIZE &&
                                    !pieces[i][j].getRight()) {
                                    pieces[i][j].setLeft(true);
                                    if (pieces[i][j].getBomb()) {
                                        setGameOver();
                                        setStatus("You lose!");
                                    } else {
                                        getNumbers(pieces[i][j], i, j);
                                    }
                                    repaint();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
    
    /**
     * tick() advances the seconds count and repaints the timer every 1000 milliseconds, as long as
     * the gameOver property is false
     */
    void tick() {
        if (!gameOver) {
            seconds++;
            repaint();
        }
    }
    
    /**
     * assignBombs() randomly assigns the given number of bombs to random pieces
     */
    public void assignBombs() {
        boolean[][] bools = new boolean[numPieces][numPieces];
        int counter = 0;
        
        // generates a random x and y position in the array
        // checks if that location does not yet have a bomb and if so advances counter
        while (counter < numBombs) {
            int x = (int) (numPieces * Math.random());
            int y = (int) (numPieces * Math.random());
            
            if (bools[x][y] == false) {
                bools[x][y] = true;
                counter++;
            }
        }
        
        // uses getPositions method to find the x and y positions for every piece
        // fills pieces array with new Pieces with the bomb assignments
        int[] positions = getPositions();
        for (int i = 0; i < numPieces; i++) {
            for (int j = 0; j < numPieces; j++) {
                pieces[i][j] = new Piece (bools[i][j], false, false, positions[i], positions[j]);
            }
        }
    }
    
    /**
     * getPositions() finds the x and y positions for the given pieces
     * 
     * @return int[] positions: a array of the range of positions for the pieces
     */
    public int[] getPositions() {
        int pieceSize = Piece.SIZE;
        int boardSize = numPieces * pieceSize;
        int min = (BOARD_WIDTH / 2) - (boardSize / 2);
        
        // finds positions for the pieces starting from the minimum value
        int[] positions = new int[numPieces];
        positions[0] = min;
        for (int i = 1; i < numPieces; i++) {
            positions[i] = positions[i - 1] + pieceSize;
        }
        
        return positions;
    }
    
    /**
     * getNumbers(Piece piece, int pieceI, int pieceJ) finds the number of adjacent bombs to a
     * given Piece and then updates that Piece's numAdjacent property; this method recurses when
     * necessary to check the number of bombs for Pieces adjacent to the original Piece
     * 
     * @param Piece piece: the piece for which to check the number of adjacent bombs
     */
    public void getNumbers(Piece piece, int pieceI, int pieceJ) {
        // if the given Piece has a bomb, return
        if (piece.getBomb()) {
            return;
        }
        
        // if the number of adjacent bombs has already been found for this Piece, return
        if (piece.getNumAdjacent() != -1) {
            return;
        }
        
        // check all surrounding Pieces to see if they have bombs
        int num = 0;
        for (int i = Math.max(0, pieceI - 1); i < Math.min(numPieces, pieceI + 2); i++) {
            for (int j = Math.max(0, pieceJ - 1); j < Math.min(numPieces, pieceJ + 2); j++) {
                if (pieces[i][j].getBomb()) {
                    num++;
                }
            }
        }
        
        // update the Piece's numAdjacent property
        piece.setNumAdjacent(num);
        
        // if none of the adjacent Pieces have bombs
        // recurse over all of those Pieces to see if any of them have adjacent bombs
        if (num == 0) {
            for (int i = Math.max(0, pieceI - 1); i < Math.min(numPieces, pieceI + 2); i++) {
                for (int j = Math.max(0, pieceJ - 1); j < Math.min(numPieces, pieceJ + 2); j++) {
                    getNumbers(pieces[i][j], i, j);
                }
            }
        }
    }
    
    /**
     * numClicked() returns the number of Pieces currently right-clicked
     * 
     * @return int count: number of Pieces that are currently flagged (right-clicked)
     */
    public int numClicked() {
        int count = 0;
        
        for (int i = 0; i < numPieces; i++) {
            for (int j = 0; j < numPieces; j++) {
                if (pieces[i][j].getRight()) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * checkWin() checks to see if all of the Pieces the user has right-clicked have bombs; if so,
     * the user wins, and gameOver and status are updated accordingly
     */
    public void checkWin() {
        int count = 0;
        
        for (int i = 0; i < numPieces; i++) {
            for (int j = 0; j < numPieces; j++) {
                if (pieces[i][j].getRight() && pieces[i][j].getBomb()) {
                    count++;
                }
            }
        }
        
        if (count == numBombs) {
            gameOver = true;
            status = "You win!";
        }
    }
    
    public void highscores() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("highscores.txt"));
            
            // read first line from file and add it to toOut (this will always be "High Scores")
            String toOut = in.readLine();
            
            // if high scores file is empty (which it should never be), notify the user
            if (toOut == null) {
                System.out.println("Looks like your highscores.txt file isn't correct. Make sure "
                        + "it follows this format:\nHigh Scores\nBeginner\n1.\n2.\n3.\n4.\n5.\n"
                        + "Intermediate\n1.\n2.\n3.\n4.\n5.\nAdvanced\n1.\n2.\n3.\n4.\n5.");
                in.close();
                return;
            }
            
            // find correct level scores
            String line = in.readLine();
            char c = line.charAt(0);
            while (c != level) {
                toOut = toOut + "\n" + line;
                line = in.readLine();
                c = line.charAt(0);
            }
            toOut = toOut + "\n" + line;
            
            // find saved scores (starting from second line) and feed them into an array
            int[] scores = new int[5];
            String[] names = new String[5];
            for (int i = 0; i < 5; i++) {
                line = in.readLine();
                int start = line.indexOf(':');
                if (start == -1) {
                    scores[i] = -1;
                    names[i] = "N/A";
                } else {
                    int end = line.indexOf(' ', start + 2);
                    int score = Integer.parseInt(line.substring(start + 1, end).trim());
                    String name = line.substring(2, start).trim();
                    scores[i] = score;
                    names[i] = name;
                }
            }
            
            // find if user's score is a high score, and if so update scores and names arrays
            boolean highScore = false;
            int compare = seconds;
            int rank = -1;
            int saveScore = -2;
            String saveName = "";
            for (int i = 0; i < 5; i++) {
                if (scores[i] == -1 && saveScore == -2) {
                    scores[i] = compare;
                    names[i] = "";
                    rank = i;
                    highScore = true;
                    break;
                } else if (saveScore != -2) {
                    int saveScore2 = scores[i];
                    scores[i] = saveScore;
                    saveScore = saveScore2;
                    String saveName2 = names[i];
                    names[i] = saveName;
                    saveName = saveName2;
                } else if (compare < scores[i]) {
                    rank = i;
                    highScore = true;
                    saveScore = scores[i];
                    saveName = names[i];
                    scores[i] = compare;
                    names[i] = "";
                }
            }
            
            // if user got a high score, prompt them to enter their name
            String name = "";
            if (highScore) {
                final JFrame winner = new JFrame("You won!");
                winner.setSize(400, 200);
                winner.setLocation(200, 200);
                String message = "Congrats! You just set a new high score!\n"
                        + "Please enter your name to record your score.";
                name = (String)JOptionPane.showInputDialog(winner, message, "You won!",
                        JOptionPane.PLAIN_MESSAGE, null, null, "Name");
                // notify user if they use an invalid character (=)
                if (name.contains(":")) {
                    message += "\n" + "\":\" is an invalid character. Please try again.";
                    name = (String)JOptionPane.showInputDialog(winner, message, "You won!",
                            JOptionPane.PLAIN_MESSAGE, null, null, "Name");
                }
                names[rank] = name;
            }
            
            // write scores to file
            // also write scores and names to an array that will be accessible from Game
            for (int i = 0; i < 5; i++) {
                if (scores[i] != -1) {
                    toOut = toOut + "\n" + (i + 1) + ". " + names[i] + ": " + scores[i]
                            + " seconds";
                } else {
                    toOut = toOut + "\n" + (i + 1) + ". ";
                }
            }
            line = in.readLine();
            while (line != null) {
                toOut = toOut + "\n" + line;
                line = in.readLine();
            }
            Writer out = new BufferedWriter(new FileWriter("highscores.txt"));
            out.write(toOut);
            
            in.close();
            out.flush();
            out.close();
          } catch (IOException e) {
            System.out.println("error while checking document: " + e.getMessage());
          }
    }
    
    /**
     * getNumBombs() returns the numBombs property of the board
     * 
     * @return int numBombs: the Board's numBombs property
     */
    public int getNumBombs() {
        return numBombs;
    }
    
    /**
     * getNumPieces() returns the numPieces property of the board
     * 
     * @return int numPieces: the Board's numPieces property
     */
    public int getNumPieces() {
        return numPieces;
    }
    
    /**
     * setStatus(String status) updates the Board's status property to the given string
     * 
     * @param String status: the new status to update the Board's status property to
     */
    private void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * setGameOver() updates the Board's gameOver property to true, ending the game
     */
    private void setGameOver() {
        gameOver = true;
    }
    
    /**
     * reset() sets the board back to its original state with all blank Pieces; to be called when
     * user presses the "new game" button from Game
     */
    public void reset() {
        gameOver = false;
        timeStarted = false;
        status = "";
        seconds = 0;
        pieces = new Piece[numPieces][numPieces];
        assignBombs();
        
        repaint();
    }
    
    /**
     * setLevel(char level) resets the board to a set size from the given level; to be called when
     * selecting difficulty from Game
     * 
     * @param char level: the first character of the given level (either B, I, or E)
     */
    public void setLevel(char level) {
        gameOver = false;
        timeStarted = false;
        status = "";
        seconds = 0;
        this.level = level;
        if (level == 'B') {
            numPieces = 9;
            numBombs = 10;
        } else if (level == 'I') {
            numPieces = 16;
            numBombs = 40;
        } else if (level == 'E') {
            numPieces = 22;
            numBombs = 100;
        }
        pieces = new Piece[numPieces][numPieces];
        assignBombs();
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // draw number of bombs left for user to identify
        String bombsLeft = "Bombs Left: " + Integer.toString(numBombs - numClicked());
        int width = bombsLeft.length() * 7 + 5;
        g.drawRect(0, 0, width, 20);
        g.drawString(bombsLeft, 5, 15);
        
        // draw game status
        String status = this.status;
        int center = (BOARD_WIDTH / 2) - (status.length() * 7 / 2);
        g.drawString(status, center, 15);
        
        // draw timer
        String time = "Time: " + seconds;
        int timeWidth = time.length() * 7 + 8;
        g.drawRect(BOARD_WIDTH - timeWidth, 0, timeWidth, 20);
        g.drawString(time, BOARD_WIDTH - timeWidth + 5, 15);
        
        // draw Pieces
        for (int i = 0; i < numPieces; i++) {
            for (int j = 0; j < numPieces; j++) {
                if (gameOver) {
                    pieces[i][j].setGameOver();
                }
                pieces[i][j].draw(g);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
    
}