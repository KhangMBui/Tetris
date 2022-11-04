import java.awt.Color;
import java.awt.Graphics;

/**
 * This is the Tetris board represented by a (HEIGHT - by - WIDTH) matrix of
 * Squares.
 * 
 * The upper left Square is at (0,0). The lower right Square is at (HEIGHT -1,
 * WIDTH -1).
 * 
 * Given a Square at (x,y) the square to the left is at (x-1, y) the square
 * below is at (x, y+1)
 * 
 * Each Square has a color. A white Square is EMPTY; any other color means that
 * spot is occupied (i.e. a piece cannot move over/to an occupied square). A
 * grid will also remove completely full rows.
 * 
 * @author Khang Minh Bui & Anh Minh Tran
 */
public class Grid {
	private Square[][] board;

	// Width and Height of Grid in number of squares
	public static final int HEIGHT = 20;

	public static final int WIDTH = 10;

	private static final int BORDER = 5;

	public static final int LEFT = 100; // pixel position of left of grid

	public static final int TOP = 50; // pixel position of top of grid

	public static final Color EMPTY = Color.WHITE;

	/**
	 * Creates the grid
	 */
	public Grid() {
		board = new Square[HEIGHT][WIDTH];

		// put squares in the board
		for (int row = 0; row < HEIGHT; row++) {
			for (int col = 0; col < WIDTH; col++) {
				board[row][col] = new Square(this, row, col, EMPTY, false);
			}
		}
	}
	public Color getColor(int row, int col) {
		return board[row][col].getColor();
	}

	/**
	 * Returns true if the location (row, col) on the grid is occupied
	 * 
	 * @param row
	 *            the row in the grid
	 * @param col
	 *            the column in the grid
	 */
	public boolean isSet(int row, int col) {
		return !board[row][col].getColor().equals(EMPTY);
	}
	
//	public boolean checkInsideGridAndNotSet(int row, int col) {
//		boolean flag1 = !(row < 0 || row > 19 || col <0 || col > 9); 
//		boolean flag2 = !isSet(row, col);
//		return (flag1 && flag2);
//	}

	/**
	 * Changes the color of the Square at the given location
	 * 
	 * @param row
	 *            the row of the Square in the Grid
	 * @param col
	 *            the column of the Square in the Grid
	 * @param c
	 *            the color to set the Square
	 * @throws IndexOutOfBoundsException
	 *             if row < 0 || row>= HEIGHT || col < 0 || col >= WIDTH
	 */
	public void set(int row, int col, Color c) {
		board[row][col].setColor(c);
	}

	/**
	 * Checks for and remove all solid rows of squares.
	 * 
	 * If a solid row is found and removed, all rows above it are moved down and
	 * the top row set to empty
	 */
	public void checkRows() {
		//Iterate the game board from bottom to top:
		int r;
		for (r = HEIGHT - 1; r>0; r--) {
			//Create a flag to keep track if every column in a row is filled with color.
			//If not, flag will be set to false.
			boolean flag = true;
			//Iterate through every column of each row:
			for (int c = 0; c<WIDTH; c++) {
				//If a square is not filled with color, set flag to false:
				if (!isSet(r,c))
					flag = false;
			}
			//After every row, if flag == true => the whole row is filled.
			if (flag) {
				//Use the variable track to iterate from the filled row upward:
				for (int track = r; track>=0; track--) {
					//If the filled row is the first row, empty it (filled it with white)
					if (track == 0) {
						for (int c = 0; c<WIDTH; c++)
							board[track][c].setColor(EMPTY);
					}
					//Else, set the color of each square in the filled row to the color of 
					//each square above it:
					else {
						for (int c = 0; c<WIDTH; c++) {
							board[track][c].setColor(board[track-1][c].getColor());
						}	
					}
			   }
			   r++;
		    }
	    }
	}

	/**
	 * Draws the grid on the given Graphics context
	 */
	public void draw(Graphics g) {

		// draw the edges as rectangles: left, right in blue then bottom in red
		g.setColor(Color.BLUE);
		g.fillRect(LEFT - BORDER, TOP, BORDER, HEIGHT * Square.HEIGHT);
		g.fillRect(LEFT + WIDTH * Square.WIDTH, TOP, BORDER, HEIGHT
				* Square.HEIGHT);
		g.setColor(Color.RED);
		g.fillRect(LEFT - BORDER, TOP + HEIGHT * Square.HEIGHT, WIDTH
				* Square.WIDTH + 2 * BORDER, BORDER);

		// draw all the squares in the grid
		// empty ones first (to avoid masking the black lines of the pieces that
		// have already fallen)
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (board[r][c].getColor().equals(EMPTY)) {
					board[r][c].draw(g);
				}
			}
		}
		for (int r = 0; r < HEIGHT; r++) {
			for (int c = 0; c < WIDTH; c++) {
				if (!board[r][c].getColor().equals(EMPTY)) {
					board[r][c].draw(g);
				}
			}
		}
	}
}
