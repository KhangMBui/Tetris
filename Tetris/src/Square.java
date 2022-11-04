import java.awt.Color;
import java.awt.Graphics;

/**
 * One Square on our Tetris Grid or one square in our Tetris game piece
 * 
 * @author Khang Minh Bui & Anh Minh Tran
 */
public class Square {
	private Grid grid; // the environment where this Square is

	private int row, col; // the grid location of this Square

	protected boolean ableToMove; // true if this Square can move

	private Color color; // the color of this Square

	// possible move directions are defined by the Game class

	// dimensions of a Square
	public static final int WIDTH = 20;

	public static final int HEIGHT = 20;
	
	public static int barRotationCount = 0;

	/**
	 * Creates a square
	 * 
	 * @param g
	 *            the Grid for this Square
	 * @param row
	 *            the row of this Square in the Grid
	 * @param col
	 *            the column of this Square in the Grid
	 * @param c
	 *            the Color of this Square
	 * @param mobile
	 *            true if this Square can move
	 * 
	 * @throws IllegalArgumentException
	 *             if row and col not within the Grid
	 */
	public Square(Grid g, int row, int col, Color c, boolean mobile) {
		if (row < 0 || row > Grid.HEIGHT - 1)
			throw new IllegalArgumentException("Invalid row =" + row);
		if (col < 0 || col > Grid.WIDTH - 1)
			throw new IllegalArgumentException("Invalid column  = " + col);

		// initialize instance variables
		grid = g;
		this.row = row;
		this.col = col;
		color = c;
		ableToMove = mobile;
	}

	/**
	 * Returns the row for this Square
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column for this Square
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Returns true if this Square can move 1 spot in direction d
	 * 
	 * @param direction
	 *            the direction to test for possible move
	 */
	public boolean canMove(Direction direction) {
		if (!ableToMove)
			return false;

		boolean move = true;
		// if the given direction is blocked, we can't move
		// remember to check the edges of the grid
		switch (direction) {
		case DOWN:
			if (row == (Grid.HEIGHT - 1) || grid.isSet(row + 1, col))
				move = false;
			break;

		// currently doesn't support checking LEFT or RIGHT
		// MODIFY so that it correctly returns if it can move left or right
		case LEFT:
			//If the square is next to the left wall
			//or is next to another filled square in the left, move = false:
			if (col == 0 || grid.isSet(row, col - 1))
				move = false;
			break;
		case RIGHT:
			//If the square is next to the right wall
			//or is next to another filled square in the right, move = false:
			if (col == Grid.WIDTH-1 || grid.isSet(row, col + 1))
				move = false;
			break;
		}
		return move;
	}

	/**
	 * moves this square in the given direction if possible.
	 * 
	 * The square will not move if the direction is blocked, or if the square is
	 * unable to move.
	 * 
	 * If it attempts to move DOWN and it can't, the square is frozen and cannot
	 * move anymore
	 * 
	 * @param direction
	 *            the direction to move
	 */
	public void move(Direction direction) {
		if (canMove(direction)) {
			switch (direction) {
			case DOWN:
				row++;;
				break;	
			// currently doesn't support moving LEFT or RIGHT
			// MODIFY so that the Square moves appropriately	
			case LEFT:
				col--;
				break;
			case RIGHT:
				col++;
				break;
			}
		}
	}
	
	public boolean checkInsideGridAndNotSet(int row, int col) {
		//Check if square's inside grid
		if (row < 0 || row > 19 || col <0 || col > 9 )
			return false;
		//Check if square's empty
		else
			return !(grid.isSet(row, col));
	}
	
	public boolean ableToRotate(Square center) {
		//Current square's position:
		int r1 = this.getRow();
		int c1 = this.getCol();
		//New square's position:
		int r2 = center.getRow() + (c1 - center.getCol());
		int c2 = center.getCol() + (center.getRow( ) - r1);		
		
		int rDiff = r2 - r1;
		int cDiff = c2 - c1;
		boolean flag = true;
		
		//Specific case for barShape where the square shifts two units upward or downward, and two units to the left or to the right
		if (Math.abs(rDiff) == 2 && Math.abs(cDiff) ==2) {
			//Bar placed horizontally
			if (barRotationCount%2==0) {
				if (rDiff>0) {
					flag = checkInsideGridAndNotSet(r1 + rDiff, c1) && checkInsideGridAndNotSet(r1 + rDiff - 1, c1);
				}
				else {
					flag = checkInsideGridAndNotSet(r1 + rDiff, c1) && checkInsideGridAndNotSet(r1 + rDiff + 1, c1);
				}
			
			    if (cDiff>0) {
				   flag = (flag && checkInsideGridAndNotSet(r1 + rDiff, c1 + cDiff - 1));
			    }
			    else {
				   flag = (flag && checkInsideGridAndNotSet(r1 + rDiff, c1 + cDiff + 1));
			    }
			}
			//Bar placed vertically
			if (barRotationCount%2!=0) {
				if (rDiff<0) {
					flag = checkInsideGridAndNotSet(r1 + rDiff + 1, c1 + cDiff);
				}
				else {
					flag = checkInsideGridAndNotSet(r1 + rDiff - 1, c1 + cDiff);
				}
				if (cDiff<0) {
					flag = (flag && checkInsideGridAndNotSet(r1, c1+ cDiff) && checkInsideGridAndNotSet(r1, c1+ cDiff + 1));
				}
				else {
					flag = (flag && checkInsideGridAndNotSet(r1, c1+ cDiff) && checkInsideGridAndNotSet(r1, c1+ cDiff - 1));
				}
			}
		}
		else if (Math.abs(rDiff) ==1) {
			//Square lies in the same row with the center square
			if (center.getRow() == r1) 
				flag = (checkInsideGridAndNotSet(r1 + rDiff, c1));
			//Square lies above or below
			else  
				flag = (checkInsideGridAndNotSet(r1, c1 + cDiff));
		}

		
		//Case where square only shifts two units downward or upward
		else if (Math.abs(rDiff) == 2) {
			if (rDiff>0) {
				flag = (checkInsideGridAndNotSet(r1 + rDiff - 1, c1));
			}
			else {
				flag = (checkInsideGridAndNotSet(r1 + rDiff + 1, c1));
			}	
		}
		
		//Case where square only shifts two units to the right or left
		else if (Math.abs(rDiff) == 0 && Math.abs(cDiff) == 2) {
			if (cDiff>0)
				flag = (checkInsideGridAndNotSet(r1, c1 + cDiff - 1)); 
			else
				flag = (checkInsideGridAndNotSet(r1, c1 + cDiff + 1)); 
		}
		flag = (flag && checkInsideGridAndNotSet(r2, c2));
		
		return flag;

	}
	public void rotate(Square center) {
		//Current square's position:
		int r1 = this.getRow();
		int c1 = this.getCol();
		//New square's position:
		this.row = center.getRow() + (c1 - center.getCol());
		this.col = center.getCol() + (center.getRow( ) - r1);		
	}

	/**
	 * Changes the color of this square
	 * 
	 * @param c
	 *            the new color
	 */
	public void setColor(Color c) {
		color = c;
	}

	/**
	 * Gets the color of this square
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Draws this square on the given graphics context
	 */
	public void draw(Graphics g) {

		// calculate the upper left (x,y) coordinate of this square
		int actualX = Grid.LEFT + col * WIDTH;
		int actualY = Grid.TOP + row * HEIGHT;
		g.setColor(color);
		g.fillRect(actualX, actualY, WIDTH, HEIGHT);
		// black border (if not empty)
		if (!color.equals(Grid.EMPTY)) {
			g.setColor(Color.BLACK);
			g.drawRect(actualX, actualY, WIDTH, HEIGHT);
		}
	}
}
