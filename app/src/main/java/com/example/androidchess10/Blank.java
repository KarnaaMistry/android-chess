package com.example.androidchess10;

/**
 * A subclass of piece representing a blank piece with no information stored besides coordinates.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 * @see Piece
 */
public class Blank extends Piece {
	
	/**
	 * Creates a blank piece at the given coordinates.
	 * 
	 * @param coords		<code>int</code> array storing the row of the blank piece at index 0 and
	 *  					the column at index 1
	 */
	public Blank(int[] coords) {
		super("na","",coords);
	}

	/**
	 * A blank piece cannot be moved and thus returns false when any move is input
	 * 
	 * @param chessboard	<code>Board</code> on which this blank piece is attempting to move
	 * @param dest			<code>int</code> array storing the the attempted destination of this blank piece where
	 * 						the row is stored at index 0 and the column at index 1.
	 * @see Board
	 */
	@Override
	public boolean legalMove(Board chessboard, int[] dest) {
		return false;
	}

	public int getDrawable(){
		return R.drawable.ic_blank;
	}
	
}
