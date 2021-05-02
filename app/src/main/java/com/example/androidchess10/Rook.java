package com.example.androidchess10;

import java.io.Serializable;

/**
 * Rook is the class that contains the Rook object which handles rook specific interactions on the board.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 * @see Piece
 */
public class Rook extends Piece implements Serializable {

	private static final long serialVersionUID = 1602647007666843103L;
	
	/**
	 * A <code>boolean</code> that is <code>true</code> if this rook has ever moved during the game;
	 * <code>false</code> otherwise.
	 */
	private boolean hasMoved = false;
	
	/**
	 * Initializes the rook piece with a given name, a color, and a position on the board.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "R" for rook
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 */
	public Rook(String name, String color, int[] coords) {
		super(name, color, coords);
	}
	
	/**
	 * Initializes the rook with a given name, a color, a position on the board, and whether it has moved.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "R" for rook
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 * @param moved 	<code>boolean</code> indicating whether this rook has moved
	 */
	public Rook(String name, String color, int[] coords, boolean moved) {
		super(name, color, coords);
		this.hasMoved = moved;
	}
	
	/**
	 * Returns the value of hasMoved from the current rook instance.
	 * 
	 * @return			<code>boolean</code> indicating if this rook has moved
	 */
	public boolean getHasMoved() {
		return this.hasMoved;
	}

	/**
	 * Checks to see whether this rook is able to move from its stored position to a given position.
	 * 
	 * @param chessboard		<code>Board</code> on which the rook is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this rook can move to position indicated by <code>dest</code>;
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
		
		if (!(((r2 == r1) && (c2 != c1)) || ((r2 != r1) && (c2 == c1)))) {
			return false;
		}
		
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
		
		return true;
		
	}
	
	/**
	 * Moves the rook from one position on the board by storing the current instance at the given 
	 * destination on the board, setting the instances coordinates to the new position, and creating
	 * a blank piece at the location from which this instance moved, updating necessary fields.
	 * 
	 * @param chessboard	<code>Board</code> on which the rook will be moved
	 * @param dest			coordinates to which the rook will be moved
	 * @see Board
	 * @see Blank
	 */
	public void move(Board chessboard, int[] dest) {
		Piece[][] board = chessboard.positions;
		int[] ini = this.getCoords();
		int r1 = ini[0]; int c1 = ini[1];
		int r2 = dest[0]; int c2 = dest[1];
		board[r2][c2] = this;
		this.setCoords(r2, c2);
		board[r1][c1] = new Blank(new int[] {r1,c1});
		this.hasMoved = true;
	}

	public int getDrawable(){
		if(this.getColor().equals("white")) {
			return R.drawable.ic_rook_white;
		}else{
			return R.drawable.ic_rook_black;
		}
	}

}
