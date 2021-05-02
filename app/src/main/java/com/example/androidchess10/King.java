package com.example.androidchess10;

import java.io.Serializable;

/**
 * King is the class that contains the King object which handles king specific interactions on the board.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 * @see Piece
 */
public class King extends Piece implements Serializable {

	private static final long serialVersionUID = 3838888163315521525L;
	
	/**
	 * A <code>boolean</code> that is <code>true</code> if this king has ever moved during the game;
	 * <code>false</code> otherwise.
	 */
	private boolean hasMoved = false;
	
	/**
	 * Initializes the king piece with a given name, a color, and a position on the board.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "K" for king
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 */
	public King(String name, String color, int[] coords) {
		super(name, color, coords);
		this.hasMoved = false;
	}
	
	/**
	 * Initializes the king with a given name, a color, a position on the board, and whether it has moved.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "K" for king.
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 * @param moved 	<code>boolean</code> indicating whether this king has moved
	 */
	public King(String name, String color, int[] coords, boolean moved) {
		super(name, color, coords);
		this.hasMoved = moved;
	}
	
	/**
	 * Returns the value of <code>hasMoved</code> from this king instance
	 * 
	 * @return			<code>boolean</code> indicating if the king has moved
	 */
	public boolean getHasMoved() {
		return this.hasMoved;
	}

	/**
	 * Checks to see whether this king is able to move from its stored position to a given position.
	 * 
	 * @param chessboard		<code>Board</code> on which this king is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this king can move to position indicated by <code>dest</code>;
	 * 							<code>false</code> otherwise.
	 * @see Board
	 */
	@Override
	public boolean legalMove(Board chessboard, int[] dest) {
		
		int r1 = this.getCoords()[0];
		int c1 = this.getCoords()[1];
		int r2 = dest[0];
		int c2 = dest[1];
		
		if (chessboard.positions[r2][c2].getColor().equals(this.getColor())) { return false; } 
		
		if (r1 == r2 && c1 == 4 && (c1 == c2-2 || c1 == c2+2)) { 
			return this.canCastle(chessboard, dest);
		}
		
		if (!(((r2 == r1+1) && (c2 == c1)) || ((r2 == r1+1) && (c2 == c1+1)) || ((r2 == r1) && (c2 == c1+1)) || ((r2 == r1-1) && (c2 == c1+1))
			|| ((r2 == r1-1) && (c2 == c1)) || ((r2 == r1-1) && (c2 == c1-1)) || ((r2 == r1) && (c2 == c1-1)) || ((r2 == r1+1) && (c2 == c1-1)))) {
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * Moves the king from one position on the board by storing the current instance at the given 
	 * destination on the board, setting the instances coordinates to the new position, and creating
	 * a blank piece at the location from which this instance moved, updating necessary fields.
	 * 
	 * @param chessboard	<code>Board</code> on which this king will be moved
	 * @param dest			coordinates to which this king will be moved
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
		if (c1 == 4) {
			if (c2 == 6) {
				Piece kside_rook = board[r1][7];
				board[r1][5] = kside_rook;
				kside_rook.setCoords(r1, 5);
				board[r1][7] = new Blank(new int[] {r1,7});
			}
			if (c2 == 2) {
				Piece kside_rook = board[r1][0];
				board[r1][3] = kside_rook;
				kside_rook.setCoords(r1, 3);
				board[r1][0] = new Blank(new int[] {r1,0});
			}
		}
		this.hasMoved = true;
	}
	
	/**
	 * Checks to see whether this king is able to castle by checking if it or the piece it is attempting to castle
	 * with has been moved; castling will not be allowed if this king would be put into check.
	 * 
	 * @param chessboard		<code>Board</code> on which this king is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this king is able to castle; <code>false</code> otherwise
	 * @see Board
	 */
	public boolean canCastle(Board chessboard, int[] dest) {
		int rk = this.getCoords()[0];
		int ck = this.getCoords()[1];
		int c2 = dest[1];
		int[] corner = new int[2];
		corner[0] = rk;
		if (c2 < ck) {
			corner[1] = dest[1]-2;
		}
		if (c2 > ck) {
			corner[1] = dest[1]+1;
		}
		
		int rr = corner[0];
		int cr = corner[1];
		
		if (Chess.isInCheck(chessboard, MainActivity.white_turn)) {
			return false;
		}
		if (this.hasMoved) { 
			return false;
		}
		if (dest[0] != rk) { 
			return false;
		}
		if (!(chessboard.positions[rr][cr] instanceof Rook)) { 
			return false;
		} else {
			if (((Rook)chessboard.positions[rr][cr]).getHasMoved()) {
				return false;
			}
		}
		int start = Math.min(ck, cr);
		int finish = Math.max(ck, cr);
		for (int x = start+1; x < finish; x++) {
			if (!chessboard.positions[rk][x].getName().equals("na")) { 
				return false;
			}
		}
		if (ck < cr) {
			for (int x = ck; x < ck+2; x++) {
				if (rk == 0) {
					if (chessboard.positions[rk][x].inDanger(chessboard,"black")) { return false; }
				}
				if (rk == 7) {
					if (chessboard.positions[rk][x].inDanger(chessboard, "white")) { return false; }
				}
			}
		} else {
			for (int x = ck; x > ck-2; x--) {
				if (rk == 0) {
					if (chessboard.positions[rk][x].inDanger(chessboard,"black")) { return false; }
				}
				if (rk == 7) {
					if (chessboard.positions[rk][x].inDanger(chessboard, "white")) { return false; }
				}
			}
		}
		
		return true;
	}

	public int getDrawable(){
		if(this.getColor().equals("white")) {
			return R.drawable.ic_king_white;
		}else{
			return R.drawable.ic_king_black;
		}
	}

}
