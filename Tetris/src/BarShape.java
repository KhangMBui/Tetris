import java.awt.Color;

public class BarShape extends AbstractPiece{
	/**
	 * Creates an L-Shape piece. See class description for actual location of r
	 * and c
	 * 
	 * @param r
	 *            row location for this piece
	 * @param c
	 *            column location for this piece
	 * @param g
	 *            the grid for this game piece
	 * 
	 */
	public BarShape(int r, int c, Grid g) {
		super(g);
		// Create the squares
		square[0] = new Square(g, r , c - 1, Color.CYAN, true);
		square[1] = new Square(g, r , c , Color.CYAN, true);
		square[2] = new Square(g, r , c + 1, Color.CYAN, true);
		square[3] = new Square(g, r , c + 2, Color.CYAN, true);
	}
	
	public void rotate() {
		super.rotate();
		Square.barRotationCount++;
	}
}
