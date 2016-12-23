import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class MinesweeperTest {
    Board board;
    
    @Before
    public void setUp() {
        board = new Board (9, 10);
    }
    
    // BOARD TESTS
    @Test
    public void testGetNumBombs() {
        int bombs = board.getNumBombs();
        assertEquals(10, bombs);
    }
    
    @Test
    public void testGetNumPieces() {
        int pieces = board.getNumPieces();
        assertEquals(9, pieces);
    }
    
    @Test
    public void testSetLevelBeginner() {
        board.setLevel('I');
        board.setLevel('B');
        int pieces = board.getNumPieces();
        int bombs = board.getNumBombs();
        assertEquals(9, pieces);
        assertEquals(10, bombs);
    }
    
    @Test
    public void testSetLevelIntermediate() {
        board.setLevel('I');
        int pieces = board.getNumPieces();
        int bombs = board.getNumBombs();
        assertEquals(16, pieces);
        assertEquals(40, bombs);
    }
    
    @Test
    public void testSetLevelExpert() {
        board.setLevel('E');
        int pieces = board.getNumPieces();
        int bombs = board.getNumBombs();
        assertEquals(22, pieces);
        assertEquals(100, bombs);
    }
    
    @Test
    public void testResetMaintainsSizeBombs() {
        board.setLevel('I');
        board.reset();
        int pieces = board.getNumPieces();
        int bombs = board.getNumBombs();
        assertEquals(16, pieces);
        assertEquals(40, bombs);
    }
    
    // PIECE TESTS
    @Test
    public void testSIZEisPublic() {
        int size = Piece.SIZE;
        assertEquals(15, size);
    }
    
    @Test
    public void testGetBomb() {
        Piece piece = new Piece(true, false, false, 0, 0);
        assertEquals(true, piece.getBomb());
    }
    
    @Test
    public void testGetNumAdjacentNotSet() {
        Piece piece = new Piece(false, false, false, 0, 0);
        assertEquals(-1, piece.getNumAdjacent());
    }
    
    @Test
    public void testGetNumAdjacentSet() {
        Piece piece = new Piece(false, false, false, 0, 0);
        piece.setNumAdjacent(3);
        assertEquals(3, piece.getNumAdjacent());
    }
    
    @Test
    public void testGetX() {
        Piece piece = new Piece(true, false, false, 20, 40);
        assertEquals(20, piece.getX());
    }
    
    @Test
    public void testGetY() {
        Piece piece = new Piece(true, false, false, 20, 40);
        assertEquals(40, piece.getY());
    }
    
    @Test
    public void testGetLeft() {
        Piece piece = new Piece(true, true, false, 20, 40);
        assertEquals(true, piece.getLeft());
    }
    
    @Test
    public void testGetRight() {
        Piece piece = new Piece(true, false, true, 20, 40);
        assertEquals(true, piece.getRight());
    }
}
    