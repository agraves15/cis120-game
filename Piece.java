import java.awt.*;

/**
 * Piece
 * 
 * A game piece that makes up the game board. Users interact with the piece through right and left
 * clicks to play the game.
 */
public class Piece {
    public static final int SIZE = 15;
    private boolean hasBomb;
    private int numAdjacent = -1;
    private int x;
    private int y;
    
    private boolean leftClicked;
    private boolean rightClicked;
    
    private boolean gameOver;
    
    /**
     * constructor sets Piece's properties of hasBomb and its position using given values.
     */
    public Piece(boolean hasBomb, boolean leftClicked, boolean rightClicked, int x, int y) {
        this.hasBomb = hasBomb;
        this.x = x;
        this.y = y;
        
        this.leftClicked = leftClicked;
        this.rightClicked = rightClicked;
    }
    
    /**
     * draw(Graphics g) draws the square as a grey square with a black border at the given position
     * 
     * @param graphics context g
     */
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, SIZE, SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, SIZE, SIZE);
        
        if (numAdjacent != -1) {
            drawNumber(g, numAdjacent);
        }
        
        if (gameOver && hasBomb) {
            drawBomb(g);
        }
        
        if (leftClicked && hasBomb) {
            drawFail(g);
        }
        
        if (leftClicked && !hasBomb) {
            drawNumber(g, numAdjacent);
        }
        
        if (rightClicked) {
            drawFlag(g);
        }
        
        if (rightClicked && gameOver && !hasBomb) {
            drawWrongFlag(g);
        }
    }
    
    /**
     * drawBomb(Graphics g) draws a bomb within the piece; to be called when the user clicks on a
     * Piece for which hasBomb is true (this method is specifically used to draw all of the bombs
     * at the very end; these black bombs indicate where bombs were, but are not the ones the user
     * clicked on)
     * 
     * @param graphics context g
     */
    public void drawBomb(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x + 2, y + 2, SIZE - 4, SIZE - 4);
    }
    
    /**
     * drawFail(Graphics g) draws makes the current Piece's bomb red to indicate to the player
     * which bomb they clicked on to make them fail; to be called when a players left-clicks on a
     * Piece with a bomb
     * 
     * @param graphics context g
     */
    public void drawFail(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x + 2, y + 2, SIZE - 4, SIZE - 4);
    }
    
    /**
     * drawFlag(Graphics g) draws a flag within the piece; to be called when the user right-clicks
     * on a Piece
     * 
     * @param graphics context g
     */
    public void drawFlag(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x + 2, y + 2, SIZE - 4, SIZE - 4);
    }
    
    /**
     * drawWrongFlag(Graphics g) draws a crossed-out bomb within the piece; to be called when the
     * player loses and has right-clicked on a Piece that does not actually have a bomb
     * 
     * @param graphics context g
     */
    public void drawWrongFlag(Graphics g) {
        g.setColor(Color.RED);
        String s = "X";
        g.drawString(s, x + 4, y + SIZE - 2);
    }
    
    /**
     * drawNumber(Graphics g, int num) draws the number of adjacent bombs to a Piece within the
     * piece (if there are none, it changes the look of the Piece); to be called when the user
     * clicks on a Piece for which hasBomb is false
     * 
     * @param Graphics g
     * @param int num; indicates the number of bombs adjacent to the Piece
     */
    public void drawNumber(Graphics g, int num) {
        if (num == 0) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(x + 2, y + 2, SIZE - 4, SIZE - 4);
        } else {
            g.setColor(Color.RED);
            String s = Integer.toString(num);
            g.drawString(s, x + 4, y + SIZE - 2);
        }
    }
    
    /**
     * getBomb() returns the Piece's hasBomb property (true if it has a bomb, false otherwise)
     * 
     * @return boolean hasBomb: if the Piece has a bomb or not
     */
    public boolean getBomb() {
        return hasBomb;
    }
    
    /**
     * setNumAdjacent() sets the numAdjacent property of the Piece; to be called in the getNumbers
     * method in Board
     * 
     * @param int num: the number of bombs found to be adjacent to the Piece
     */
    public void setNumAdjacent(int num) {
        numAdjacent = num;
    }
    
    /**
     * getNumAdjacent() returns the number of bombs adjacent to the Piece; if this has not yet been
     * set, numAdjacent will be -1
     * 
     * @return int numAdjacent: the number of bombs adjacent to the Piece
     */
    public int getNumAdjacent() {
        return numAdjacent;
    }
    
    /**
     * getX() returns the Piece's x position
     * 
     * @return int x: the Piece's x position
     */
    public int getX() {
        return x;
    }
    
    /**
     * getY() returns the Piece's y position
     * 
     * @return int y: the Piece's y position
     */
    public int getY() {
        return y;
    }
    
    /**
     * setLeft(boolean clicked) sets the Piece's leftClicked property to the given boolean value
     * 
     * @param boolean clicked: if the piece has been clicked or not
     */
    public void setLeft(boolean clicked) {
        leftClicked = clicked;
    }
    
    /**
     * setRight(boolean clicked) sets the Piece's rightClicked property to the given boolean value
     * 
     * @param boolean clicked: if the piece has been clicked or not
     */
    public void setRight(boolean clicked) {
        rightClicked = clicked;
    }
    
    /**
     * getLeft() returns the Piece's leftClicked property
     * 
     * @return boolean leftClicked: if the piece has been left-clicked or not
     */
    public boolean getLeft() {
        return leftClicked;
    }
    
    /**
     * getRight() returns the Piece's rightClicked property
     * 
     * @return boolean rightClicked: if the piece is currently flagged or not
     */
    public boolean getRight() {
        return rightClicked;
    }
    
    /**
     * setGameOver() sets the gameOver property to true, forcing all of the bombs to be drawn
     */
    public void setGameOver() {
        gameOver = true;
    }
    
}
