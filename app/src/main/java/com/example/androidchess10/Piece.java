package com.example.androidchess10;

/**
 * Piece is the superclass class from which all other piece types are subclasses of.
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 */
public abstract class Piece {
	
	/**
	 * A <code>String</code> holding the text to be displayed on the board, usually the
	 * starting character of the color followed by the first character of the type of piece.
	 */
	private String name;
	
	/**
	 * A <code>String</code> holding the color of this piece: "black", "white", or empty for a blank piece.
	 */
	private String color;
	
	/**
	 * An <code>int</code> array storing the row of this piece at index 0 and the column at index 1.
	 */
	private int[] coords;
	
	/**
	 * Initializes the piece with a given name, color, and its position on the board.
	 * 
	 * @param name		<code>String</code> that is this piece's display text
	 * @param color		<code>String</code> holding the color of this piece
	 * @param coords	<code>int</code> array storing the position of this piece on the board
	 */
	public Piece(String name, String color, int[] coords) {
		this.name = name;
		this.color = color;
		this.coords = coords;
	}
	
	/**
	 * Returns the stored name of the piece.
	 * 
	 * @return			<code>String</code> corresponding to the type of this piece
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the stored color of the piece.
	 * 
	 * @return			<code>String</code> corresponding to the color of this piece
	 */
	public String getColor() {
		return color;
	}
	
	/**
	 * Returns the current position of a piece.
	 * 
	 * @return			<code>int</code> array storing the row of this piece at index 0 and the column at index 1
	 */
	public int[] getCoords() {
		return coords;
	}
	
	/**
	 * Sets the stored position of this piece to the given coordinates.
	 * @param r			new row of the piece given as an <code>int</code>
	 * @param c			new column of the piece given as an <code>int</code>
	 */
	public void setCoords(int r, int c) {
		this.coords[0] = r;
		this.coords[1] = c;
	}
	
	/**
	 * Moves the piece from one position on the board by storing the current instance at the given 
	 * destination on the board, setting the instances coordinates to the new position, and creating
	 * a blank piece at the location from which this instance moved.
	 * 
	 * @param chessboard	<code>Board</code> on which the piece will be moved
	 * @param dest			coordinates to which the piece will be moved
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
	}
	
	/**
	 * Indicates whether or not the current piece can be taken by a piece of the opposite color
	 * 
	 * @param chessboard		<code>Board</code> on which the piece will be moved
	 * @param offense_color		the color of the opponent as a <code>String</code>
	 * @return					<code>true</code> if the current piece is threatened; <code>false</code> otherwise
	 */
	public boolean inDanger(Board chessboard, String offense_color) {
		
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece p = chessboard.positions[r][c];
				if (!p.getColor().equals(offense_color)) {
					continue;
				}
				if (p instanceof Pawn) {
					if (((Pawn) p).legalPawnCapture(chessboard, this.getCoords())) {
						return true;
					}
				} else {
					if (p.legalMove(chessboard, this.getCoords())) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}

	
	/**
	 * An <code>abstract</code> method which will return a <code>boolean</code> indicating whether an attempted move will be legal.
	 * 
	 * @param chessboard	<code>Board</code> on which the current instance of piece is attempting to move on
	 * @param dest			coordinates to which the piece is checking a legal move
	 * @return				<code>true</code> if a piece will make a legal move; <code>false</code> otherwise
	 * @see Board
	 */
	public abstract boolean legalMove(Board chessboard, int[] dest);
	

}
