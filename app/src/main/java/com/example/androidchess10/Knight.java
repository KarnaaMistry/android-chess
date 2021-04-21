package com.example.androidchess10;

/**
 * Knight is the class that contains the Knight object which handles knight specific interactions on the board.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 * @see Piece
 */
public class Knight extends Piece {
	
	/**
	 * Initializes the knight with a given name, a color, and a position on the board.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "N" for knight
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 */
	public Knight(String name, String color, int[] coords) {
		super(name, color, coords);
	}

	/**
	 * Checks to see whether a knight is able to move from its stored position to a given position.
	 * 
	 * @param chessboard		<code>Board</code> on which this knight is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this knight can move to position indicated by <code>dest</code>;
	 * 							<code>false</code> otherwise
	 * @see Board
	 */
	@Override
	public boolean legalMove(Board chessboard, int[] dest) {
		
		int r1 = this.getCoords()[0];
		int c1 = this.getCoords()[1];
		int r2 = dest[0];
		int c2 = dest[1];
		
		if (chessboard.positions[r2][c2].getColor().equals(this.getColor())) { return false; } 
		
		if (!(((r2 == r1+2) && (c2 == c1+1)) || ((r2 == r1+1) && (c2 == c1+2)) || ((r2 == r1-1) && (c2 == c1+2)) || ((r2 == r1-2) && (c2 == c1+1))
			|| ((r2 == r1-2) && (c2 == c1-1)) || ((r2 == r1-1) && (c2 == c1-2)) || ((r2 == r1+1) && (c2 == c1-2)) || ((r2 == r1+2) && (c2 == c1-1)))) {
			return false;
		}
		
		return true;
		
	}

}
