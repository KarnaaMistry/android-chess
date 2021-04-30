package com.example.androidchess10;

/**
 * Pawn is the class that contains the Pawn object which handles pawn specific interactions on the board.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 * @see Piece
 */
public class Pawn extends Piece {
	
	/**
	 * A <code>boolean</code> that is <code>true</code> when this pawn has not yet had its first move;
	 * <code>false</code> otherwise.
	 */
	private boolean firstMove = true;

	/**
	 * Initializes the pawn with a given name, a color, and a position on the board.
	 * 
	 * @param name		<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 					character of the color followed by "p" for pawn
	 * @param color		<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords	<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 */
	public Pawn(String name, String color, int[] coords) {
		super(name, color, coords);
		this.firstMove = true;
	}
	
	/**
	 * Initializes the pawn with a given name, a color, a position on the board, and whether it has moved.
	 * 
	 * @param name			<code>String</code> holding the text to be displayed on the board consisting of the starting 
	 * 						character of the color followed by "p" for pawn
	 * @param color			<code>String</code> holding the color of this piece: "black", "white"
	 * @param coords		<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 * @param firstMov	 	<code>boolean</code> indicating whether the pawn has moved
	 */
	public Pawn(String name, String color, int[] coords, boolean firstMov) {
		super(name, color, coords);
		this.firstMove = firstMov;
	}
	
	/**
	 * Returns the value of <code>firstMove</code> from this pawn instance.
	 * 
	 * @return			<code>true</code> if this pawn has moved; <code>false</code> otherwise
	 */
	public boolean getFirstMoveFlag() {
		return this.firstMove;
	}
	
	/**
	 * Sets the value of <code>firstMove</code> for this pawn instance.
	 * 
	 * @param flag		<code>boolean</code> indicating if this pawn has moved
	 */
	public void setFirstMoveFlag(boolean flag) {
		this.firstMove = flag;
	}
	
	
	/**
	 * Checks to see whether a pawn is able to take another piece, specifically checking for en passant.
	 * 
	 * @param chessboard		<code>Board</code> on which the pawn is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this pawn can capture the piece at <code>dest</code>;
	 * 							<code>false</code> otherwise
	 * @see Board
	 */
	public boolean legalPawnCapture(Board chessboard, int[] dest) {
		
		int r1 = this.getCoords()[0];
		int c1 = this.getCoords()[1];
		int r2 = dest[0];
		int c2 = dest[1];
		
		if (chessboard.positions[r2][c2].getColor().equals(this.getColor())) { return false; } 
		
		Piece target = chessboard.positions[r2][c2];
		
		if (this.getColor().equals("white") ) {
			
			if (MainActivity.enpassant == 1 && ((r1 == MainActivity.enpassloc[0] && c1 == MainActivity.enpassloc[1]) || (r1 == MainActivity.enpassloc[4] && c1 == MainActivity.enpassloc[5]))) {
				if (target instanceof Pawn) { 
					if (r2+1 == MainActivity.enpassloc[2] && c2 == MainActivity.enpassloc[3]) {
						return true;
					}
				}
			} else if (r2 == r1+1 && c2 > 0 && c1 == c2-1 && chessboard.positions[r2][c2].getColor().equals("black")) {
				return true;
			} else if (r2 == r1+1 && c2 < 7 && c1 == c2+1 && chessboard.positions[r2][c2].getColor().equals("black")) {
				return true;
			}
			
		} else {
			
			if (MainActivity.enpassant == 1 && ((r1 == MainActivity.enpassloc[0] && c1 == MainActivity.enpassloc[1]) || (r1 == MainActivity.enpassloc[4] && c1 == MainActivity.enpassloc[5]))) {
				if (target instanceof Pawn) {
					if (r2-1 == MainActivity.enpassloc[2] && c2 == MainActivity.enpassloc[3]) {
						return true;
					}
				}
			} else if (r2 == r1-1 && c2 > 0 && c1 == c2-1 && chessboard.positions[r2][c2].getColor().equals("white")) {
				return true;
			}
			else if (r2 == r1-1 && c2 < 7 && c1 == c2+1 && chessboard.positions[r2][c2].getColor().equals("white")) {
				return true;
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * Checks to see whether a pawn is able to move from its stored position to a given position.
	 * 
	 * @param chessboard		<code>Board</code> on which this pawn is moving
	 * @param dest				destination as given by a pair of row column coordinates in an <code>int</code> array
	 * @return					<code>true</code> if this pawn can move to position indicated by <code>dest</code>;
	 * 							<code>false</code> otherwise
	 * @see Board
	 */
	public boolean legalMove(Board chessboard, int[] dest) {
		
		int r1 = this.getCoords()[0];
		int c1 = this.getCoords()[1];
		int r2 = dest[0];
		int c2 = dest[1];
		
		if (chessboard.positions[r2][c2].getColor().equals(this.getColor())) { return false; } 
		
		if (this.getColor().compareTo("white") == 0) {
			if (r2 <= r1 || r2 > r1+2) { 
				return false;
			}
			else if (r2 == 3 && c1 == c2 && this.firstMove && chessboard.positions[r2][c2].getName().equals("na") && chessboard.positions[r2-1][c2].getName().equals("na")) {
				if (c2 > 0 && c2 < 7 && (chessboard.positions[r2][c2-1].getName().equals("bp")) && (chessboard.positions[r2][c2+1].getName().equals("bp"))) {

					return true;
				}
				if (c2 > 0 && (chessboard.positions[r2][c2-1].getName().equals("bp"))) {

					return true;
				}
				if (c2 < 7 && (chessboard.positions[r2][c2+1].getName().equals("bp"))) {

					return true;
				}
			}
			else if (r2 == r1+1 && c1 == c2 && chessboard.positions[r2][c2].getName().equals("na")) {

			}
			else if (MainActivity.enpassant == 1 && ((r1 == MainActivity.enpassloc[0] && c1 == MainActivity.enpassloc[1]) || (r1 == MainActivity.enpassloc[4] && c1 == MainActivity.enpassloc[5]))
					&& r2 == MainActivity.enpassloc[2] && c2 == MainActivity.enpassloc[3]) {

			}
			else if (r2 == r1+1 && c2 > 0 && c1 == c2-1 && chessboard.positions[r2][c2].getColor().equals("black")) {

			}
			else if (r2 == r1+1 && c2 < 7 && c1 == c2+1 && chessboard.positions[r2][c2].getColor().equals("black")) {

			}
			else {
				 return false;
			}
			
			return true;
			
			
		} else {
			
			if (r1 <= r2 || r1 > r2+2) {
				return false;
			}
			else if (r2 == 4 && c1 == c2 && this.firstMove && chessboard.positions[r2][c2].getName().equals("na") && chessboard.positions[r2+1][c2].getName().equals("na")) {
				if (c2 > 0 && c2 < 7 && (chessboard.positions[r2][c2-1].getName().equals("wp")) && (chessboard.positions[r2][c2+1].getName().equals("wp"))) {
					return true;
				}
				if (c2 > 0 && (chessboard.positions[r2][c2-1].getName().equals("wp"))) {

					return true;
				}
				if (c2 < 7 && (chessboard.positions[r2][c2+1].getName().equals("wp"))) {

					return true;
				}
			}
			else if (r2 == r1-1 && c1 == c2 && chessboard.positions[r2][c2].getName().equals("na")) {
				
			}
			else if (MainActivity.enpassant == 1 && ((r1 == MainActivity.enpassloc[0] && c1 == MainActivity.enpassloc[1]) || (r1 == MainActivity.enpassloc[4] && c1 == MainActivity.enpassloc[5]))
					&& r2 == MainActivity.enpassloc[2] && c2 == MainActivity.enpassloc[3]) {

				return true;
			}
			else if (r2 == r1-1 && c2 > 0 && c1 == c2-1 && chessboard.positions[r2][c2].getColor().equals("white")) {
				
			}
			else if (r2 == r1-1 && c2 < 7 && c1 == c2+1 && chessboard.positions[r2][c2].getColor().equals("white")) {

			}
			else {
				return false;
			}

			return true;
		}
			
		
	}
	
	/**
	 * Moves the pawn from one position on the board to a given destination,
	 * updating all necessary fields.
	 * 
	 * @param chessboard	<code>Board</code> on which the pawn will be moved
	 * @param dest			coordinates to which the pawn will be moved
	 * @see Board
	 * @see Blank
	 */
	public void move(Board chessboard, int[] dest) {
		
		int r1 = this.getCoords()[0];
		int c1 = this.getCoords()[1];
		int r2 = dest[0];
		int c2 = dest[1];
		
		if (this.getColor().compareTo("white") == 0) {
			if (r2 == 3 && c1 == c2 && this.firstMove && chessboard.positions[r2][c2].getName().equals("na") && chessboard.positions[r2-1][c2].getName().equals("na")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
				if (c2 > 0 && c2 < 7 && (chessboard.positions[r2][c2-1].getName().equals("bp")) && (chessboard.positions[r2][c2+1].getName().equals("bp"))) {
					MainActivity.enpassant = 0;
					MainActivity.enpassloc[0] = r2;
					MainActivity.enpassloc[1] = c2-1;
					MainActivity.enpassloc[2] = r2-1;
					MainActivity.enpassloc[3] = c2;
					MainActivity.enpassloc[4] = r2;
					MainActivity.enpassloc[5] = c2+1;
					this.firstMove = false;
					return;
				}
				if (c2 > 0 && (chessboard.positions[r2][c2-1].getName().equals("bp"))) {
					MainActivity.enpassant = 0;
					MainActivity.enpassloc[0] = r2;
					MainActivity.enpassloc[1] = c2-1;
					MainActivity.enpassloc[2] = r2-1;
					MainActivity.enpassloc[3] = c2;
					this.firstMove = false;
					return;
				}
				if (c2 < 7 && (chessboard.positions[r2][c2+1].getName().equals("bp"))) {
					MainActivity.enpassant = 0;
					MainActivity.enpassloc[0] = r2;
					MainActivity.enpassloc[1] = c2+1;
					MainActivity.enpassloc[2] = r2-1;
					MainActivity.enpassloc[3] = c2;
					this.firstMove = false;
					return;
				}
			}
			else if (r2 == r1+1 && c1 == c2 && chessboard.positions[r2][c2].getName().equals("na")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
			}
			else if (MainActivity.enpassant == 1 && ((r1 == MainActivity.enpassloc[0] && c1 == MainActivity.enpassloc[1]) || (r1 == MainActivity.enpassloc[4] && c1 == MainActivity.enpassloc[5]))
					&& r2 == MainActivity.enpassloc[2] && c2 == MainActivity.enpassloc[3]) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
				MainActivity.enpasscap = true;
				MainActivity.enpasstarg[0] = r2-1;
				MainActivity.enpasstarg[1] = c2;
			}
			else if (r2 == r1+1 && c2 > 0 && c1 == c2-1 && chessboard.positions[r2][c2].getColor().equals("black")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
			}
			else if (r2 == r1+1 && c2 < 7 && c1 == c2+1 && chessboard.positions[r2][c2].getColor().equals("black")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
			}
			else {
				
			}
			
			
			this.firstMove = false;
			return;
			
			
		} else {
			
			if (r2 == 4 && c1 == c2 && this.firstMove && chessboard.positions[r2][c2].getName().equals("na") && chessboard.positions[r2+1][c2].getName().equals("na")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
				if (c2 > 0 && c2 < 7 && (chessboard.positions[r2][c2-1].getName().equals("wp")) && (chessboard.positions[r2][c2+1].getName().equals("wp"))) {
					MainActivity.enpassant = 0;
					MainActivity.enpassloc[0] = r2;
					MainActivity.enpassloc[1] = c2-1;
					MainActivity.enpassloc[2] = r2+1;
					MainActivity.enpassloc[3] = c2;
					MainActivity.enpassloc[4] = r2;
					MainActivity.enpassloc[5] = c2+1;
					this.firstMove = false;
					return;
				}
				if (c2 > 0 && (chessboard.positions[r2][c2-1].getName().equals("wp"))) {
					MainActivity.enpassant = 0;
					MainActivity.enpassloc[0] = r2;
					MainActivity.enpassloc[1] = c2-1;
					MainActivity.enpassloc[2] = r2+1;
					MainActivity.enpassloc[3] = c2;
					this.firstMove = false;
					return;
				}
				if (c2 < 7 && (chessboard.positions[r2][c2+1].getName().equals("wp"))) {
					MainActivity.enpassant = 0;
					MainActivity.enpassloc[0] = r2;
					MainActivity.enpassloc[1] = c2+1;
					MainActivity.enpassloc[2] = r2+1;
					MainActivity.enpassloc[3] = c2;
					this.firstMove = false;
					return;
				}
			}
			else if (r2 == r1-1 && c1 == c2 && chessboard.positions[r2][c2].getName().equals("na")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
			}
			else if (MainActivity.enpassant == 1 && ((r1 == MainActivity.enpassloc[0] && c1 == MainActivity.enpassloc[1]) || (r1 == MainActivity.enpassloc[4] && c1 == MainActivity.enpassloc[5]))
					&& r2 == MainActivity.enpassloc[2] && c2 == MainActivity.enpassloc[3]) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
				MainActivity.enpasscap = true;
				MainActivity.enpasstarg[0] = r2+1;
				MainActivity.enpasstarg[1] = c2;
			}
			else if (r2 == r1-1 && c2 > 0 && c1 == c2-1 && chessboard.positions[r2][c2].getColor().equals("white")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
			}
			else if (r2 == r1-1 && c2 < 7 && c1 == c2+1 && chessboard.positions[r2][c2].getColor().equals("white")) {
				chessboard.positions[r2][c2] = this;
				this.setCoords(r2, c2);
				chessboard.positions[r1][c1] = new Blank(new int[] {r1,c1});
			}
			else {
				
			}
			
			this.firstMove = false;
			return;
		}
			
		
	}
	
	/**
	 * Called if a pawn used enpassant to capture an opponent's piece and places a blank piece at
	 * that pawn's previous position.
	 * 
	 * @param chessboard		<code>Board</code> on which the pawn is moving
	 * @param r					<code>int</code> representing the row
	 * @param c					<code>int</code> representing the column
	 * @see Board
	 */
	public static void enpassCapture(Board chessboard, int r, int c) {
		
		chessboard.positions[r][c] = new Blank(new int[] {r,c});
		
	}
	
	/**
	 * When a pawn is promoted, the pawn stored on the board is replaced with an indicated piece with the
	 * color determined by the current players turn.
	 * 
	 * @param chessboard		<code>Board</code> on which the pawn is moving
	 * @param P					<code>char</code> representing the piece to which the pawn will be promoted
	 */
	public void promote(Board chessboard, char P) {
		int r = this.getCoords()[0];
		int c = this.getCoords()[1];

		if (MainActivity.white_turn) {
			switch (P) {
			case 'B':
				chessboard.positions[r][c] = new Bishop("wB","white",new int[] {r,c});
				break;
			case 'N':
				chessboard.positions[r][c] = new Knight("wN","white",new int[] {r,c});
				break;
			case 'R':
				chessboard.positions[r][c] = new Rook("wR","white",new int[] {r,c});
				break;
			default:
				chessboard.positions[r][c] = new Queen("wQ","white",new int[] {r,c});
				break;
			}
		} else {
			switch (P) {
			case 'B':
				chessboard.positions[r][c] = new Bishop("bB","black",new int[] {r,c});
				break;
			case 'N':
				chessboard.positions[r][c] = new Knight("bN","black",new int[] {r,c});
				break;
			case 'R':
				chessboard.positions[r][c] = new Rook("bR","black",new int[] {r,c});
				break;
			default:
				chessboard.positions[r][c] = new Queen("bQ","black",new int[] {r,c});
				break;
			}
		}
		
	}

	public int getDrawable(){
		if(this.getColor() == "white"){
			return R.drawable.ic_pawn_white;
		}else{
			return R.drawable.ic_pawn_black;
		}
	}
	
}
