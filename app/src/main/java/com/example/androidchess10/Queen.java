package com.example.androidchess10;

/**
 * Queen is the class that contains the Queen object which handles queen specific interactions on the board.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 * @see Piece
 */
public class Queen extends Piece {
	
	/**
	 * Initializes the queen with a given name, a color, and a position on the board.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "Q" for queen
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 */
	public Queen(String name, String color, int[] coords) {
		super(name, color, coords);
	}

	/**
	 * Checks to see whether this queen is able to move from its stored position to a given position.
	 * 
	 * @param chessboard		<code>Board</code> on which this queen is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this queen can move to position indicated by <code>dest</code>;
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
		
		int kr = 0;
		int kc = 0;
		
		boolean inDiagonal = false;
		for (int k = -7; k <= 7; k++) {
			if ((r2 == r1+k) && (c2 == c1+k)) {
				kr = k;
				kc = k;
				inDiagonal = true;
				break;
			}
			if ((r2 == r1-k) && (c2 == c1+k)) {
				kr = -k;
				kc = k;
				inDiagonal = true;
				break;
			}
		}
		
		boolean inLine = false;
		if (((r2 == r1) && (c2 != c1)) || ((r2 != r1) && (c2 == c1))) {
			inLine = true;
		}
		
		if (!(inDiagonal || inLine)) {
			return false;
		}
		
		if (inDiagonal) {
			int rsta,rfin,csta;
			
			if (kr * kc > 0) {
				
				rsta = Math.min(r1, r2);
				rfin = Math.max(r1, r2);
				csta = Math.min(c1, c2);
				for (int k = 1; k < rfin - rsta; k++) {
					if (!(chessboard.positions[rsta+k][csta+k].getName().equals("na"))) {
						return false;
					}
				}
	 
			} else {
				
				rsta = Math.max(r1, r2);
				rfin = Math.min(r1, r2);
				csta = Math.min(c1, c2);
				for (int k = 1; k < rsta - rfin; k++) {
					if (!(chessboard.positions[rsta-k][csta+k].getName().equals("na"))) {
						return false;
					}
				}
			}
		} else {
			
			int start,finish;
			
			if (r2 == r1) {
				
				start = Math.min(c1, c2);
				finish = Math.max(c1, c2);
				for (int c = start+1; c < finish; c++) {
					if (!(chessboard.positions[r2][c].getName().equals("na"))) {
						return false;
					}
				}
	 
			} else {
				
				start = Math.min(r1, r2);
				finish = Math.max(r1, r2);
				for (int r = start+1; r < finish; r++) {
					if (!(chessboard.positions[r][c2].getName().equals("na"))) {
						return false;
					}
				}
			}
		}
		
		
		return true;
	}

}
