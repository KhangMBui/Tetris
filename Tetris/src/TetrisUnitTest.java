import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import org.junit.jupiter.api.Test;


public class TetrisUnitTest {
	//The given unit testing case on the class calendar:
	@Test
	public void testRotate() {
		Grid g = new Grid();

		int row = 5, col = 5;
		Piece[] pieces = { new BarShape(row, col, g), new LShape(row, col, g), new ZShape(row, col, g),
				new TShape(row, col, g), new JShape(row, col, g), new SShape(row, col, g) };

		for (Piece p : pieces) {
			System.out.println(p.getClass());

			// The piece should be able to rotate
			assertTrue(p.canRotate());

			// Place a square next to the piece to prevent it from rotating
			int setRow, setCol;
			if (p.getClass() == BarShape.class) {
				setRow = row + 1;
				setCol = col;
			} else if (p.getClass() == LShape.class) {
				setRow = row;
				setCol = col - 1;
			} else if (p.getClass() == ZShape.class) {
				setRow = row + 1;
				setCol = col - 1;
			} else if (p.getClass() == TShape.class) {
				setRow = row + 1;
				setCol = col - 1;
			} else if (p.getClass() == JShape.class) {
				setRow = row;
				setCol = col - 1;
			} else { // SShape
				setRow = row;
				setCol = col - 1;
			}
			g.set(setRow, setCol, Color.GREEN);
			assertFalse(p.canRotate());

			// clear the square for the next piece
			g.set(setRow, setCol, Grid.EMPTY);
		}
	}
	
	@Test
	void testRotate2() {
		Grid g = new Grid();
		//Create an object of LShape in the middle of the grid, and check if it can rotate,
		//which it should be able to.
		AbstractPiece lSh = new LShape(Grid.HEIGHT/2-1, Grid.WIDTH/2-1, g);
		assertTrue(lSh.canRotate());
		
		//Create an object of LShape in the middle of the grid, and check if it can rotate,
		//which it should be able to.
		AbstractPiece bSh = new BarShape(Grid.HEIGHT/2-1, Grid.WIDTH/2-1, g);
		assertTrue(bSh.canRotate());
		
		//LShape can't rotate when put 1 unit next to the left wall:
		AbstractPiece L = new LShape(3, 0, g);
		assertFalse(L.canRotate());
		
		//After moved 1 unit next to the right wall,
		//rotated twice, and moved 1 unit to the right (as the two rotations put
		//the LShape backward 1 unit), the LShape is put in a position where
		//it can't be rotated.
		for (int i = 0; i<Grid.WIDTH; i++) 
			L.move(Direction.RIGHT);
		L.rotate();
		L.rotate();
		L.move(Direction.RIGHT);
		assertFalse(L.canRotate());
		
		//Bar shape can be rotated when it's 1 unit next to the left wall:
		AbstractPiece B2 = new BarShape(Grid.HEIGHT/2-1, 1, g);
		assertTrue(B2.canRotate());
		//After being moved all the way to the right, rotated once, 
		//and move to the right 2 more units (as the rotation put the
		//BarShape B2 backward 2 units), the BarShape B2 is put in a position where
		//it can't be rotated.
		for (int i = 0; i<Grid.WIDTH; i++) 
			B2.move(Direction.RIGHT);
		B2.rotate();
		B2.move(Direction.RIGHT);
		B2.move(Direction.RIGHT);
		assertFalse(B2.canRotate());
	}
	

	//Test check row:
		@Test
		void testCheckRow() {
			Grid g = new Grid();
			//Fill even lines with yellow:
			for (int row = 0; row<Grid.HEIGHT; row+=2) {
				for (int col = 0; col<Grid.WIDTH; col++) {
					g.set(row, col, Color.YELLOW);
				}
			}
			//Test if the even lines are filled:
			for (int row = 0; row<Grid.HEIGHT; row+=2) {
				for (int col = 0; col<Grid.WIDTH; col++) {
					assertTrue(g.isSet(row, col));
				}
			}
			//Test if the odd lines are not filled:
			for (int row = 1; row<Grid.HEIGHT; row+=2) {
				for (int col = 0; col<Grid.WIDTH; col++) {
					assertFalse(g.isSet(row, col));
				}
			}
			//Check rows and get rid of all filled rows:
			g.checkRows();
			//Test if the every lines are not filled:
			for (int row = 0; row<Grid.HEIGHT; row++) {
				for (int col = 0; col<Grid.WIDTH; col++) {
					assertFalse(g.isSet(row, col));
				}
			}
		}
		
		@Test
		public void testCheckRow2() {
			Grid g = new Grid();
			// Fill all lines with yellow except row 9
			for (int row = 0; row < g.HEIGHT; row++) {
				if (row == 9) {
					g.set(row, 5, Color.YELLOW);
					g.set(row, 6, Color.YELLOW);
				} else {
					for (int col = 0; col < g.WIDTH; col++) {
						g.set(row, col, Color.YELLOW);
					}
				}
			}
			// Check rows and get rid of all filled rows:
			g.checkRows();
			// There should be only 2 squares on the bottom row
			for (int row = 0; row < g.HEIGHT; row++) {
				for (int col = 0; col < g.WIDTH; col++) {
					if (row != g.HEIGHT - 1 || col != 5 && col != 6) {
						assertFalse(g.isSet(row, col));
					} else {
						assertTrue(g.isSet(row, col));
					}
				}
			}
		}
		
		//Test motion of square:
		@Test
		void testSquareMotion() {
			Grid g = new Grid();
			//Test can move
			Square sq = new Square(g, 3, 5, Color.pink, true);
		    assertTrue(sq.canMove(Direction.LEFT));
		    
		    //Can't move right:
		    Square sq2 = new Square(g, 3, Grid.WIDTH-1, Color.black, true);
		    assertFalse(sq2.canMove(Direction.RIGHT));
		    
		    //Can't move left:
		    Square sq3 = new Square(g, 3, Grid.WIDTH - Grid.WIDTH, Color.black, true);
		    assertFalse(sq3.canMove(Direction.LEFT));
		    
		    //Can't move down:
		    Square sq4 = new Square(g, Grid.HEIGHT - 1, 9, Color.RED, true);
		    assertFalse(sq4.canMove(Direction.DOWN));
		}
		
		//Test motion of LShape:
		@Test
		void testLMotion() {
			Grid g = new Grid();
			//Test can move
			LShape piece = new LShape(1, Grid.WIDTH/2 - 1, g);
			assertTrue(piece.canMove(Direction.RIGHT));
			
			//Can't move down:
			LShape piece2 = new LShape(Grid.HEIGHT-2, Grid.WIDTH/2 - 1, g);
			assertFalse(piece2.canMove(Direction.DOWN));
			
			//Can't move left:
			LShape piece3 = new LShape(1, Grid.WIDTH-Grid.WIDTH, g);
			assertFalse(piece3.canMove(Direction.LEFT));
			
			//Can't move right:
			LShape piece4 = new LShape(1, Grid.WIDTH-2, g);
			assertFalse(piece4.canMove(Direction.RIGHT));
			
			//Starting from the left, does it move all the way to the right 
			//with the right number of moves (8 moves)?
			LShape piece5 = new LShape(1, Grid.WIDTH - Grid.WIDTH, g);
			for (int i = 0; i<8; i++)
				piece5.move(Direction.RIGHT);
			assertFalse(piece5.canMove(Direction.RIGHT));
			
			//Starting at the top, can it be dropped down anymore after 17 down moves??
			LShape piece6 = new LShape(1, Grid.WIDTH/2, g);
			for (int i = 0; i<17; i++)
				piece6.move(Direction.DOWN);
			assertFalse(piece6.canMove(Direction.DOWN));
		}

}
