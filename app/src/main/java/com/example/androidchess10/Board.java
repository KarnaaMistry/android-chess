package com.example.androidchess10;

/**
 * Board is the class that holds the Board object and all of its interactions which include:
 * <ul>
 * <li>Initialization
 * <li>Printing the board
 * <li>Comparison to other boards
 * </ul>
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 */
public class Board {
	
	/**
	 * The <code>Piece</code> double array that stores references to the <code>Piece</code>
	 * objects on the board.
	 */
	public Piece[][] positions = new Piece[8][8];
	
	/**
	 * Initializes the board and fills it with pieces in their starting positions.
	 */
	public Board() {
		this.positions[0][0] = new Rook("wR","white", new int[] {0,0});
		this.positions[0][1] = new Knight("wN","white", new int[] {0,1});
		this.positions[0][2] = new Bishop("wB","white", new int[] {0,2});
		this.positions[0][3] = new Queen("wQ","white", new int[] {0,3});
		this.positions[0][4] = new King("wK","white", new int[] {0,4});
		this.positions[0][5] = new Bishop("wB","white", new int[] {0,5});
		this.positions[0][6] = new Knight("wN","white", new int[] {0,6});
		this.positions[0][7] = new Rook("wR","white", new int[] {0,7});
		for (int k = 0; k < 8; k++) { this.positions[1][k] = new Pawn("wp","white",new int[] {1,k}); }
		for (int i = 2; i < 6; i++) {
			for (int j = 0; j < 8; j++) {
				this.positions[i][j] = new Blank(new int[] {i,j});
			}
		}
		for (int k = 0; k < 8; k++) { this.positions[6][k] = new Pawn("bp","black",new int[] {6,k}); }
		this.positions[7][0] = new Rook("bR","black", new int[] {7,0});
		this.positions[7][1] = new Knight("bN","black", new int[] {7,1});
		this.positions[7][2] = new Bishop("bB","black", new int[] {7,2});
		this.positions[7][3] = new Queen("bQ","black", new int[] {7,3});
		this.positions[7][4] = new King("bK","black", new int[] {7,4});
		this.positions[7][5] = new Bishop("bB","black", new int[] {7,5});
		this.positions[7][6] = new Knight("bN","black", new int[] {7,6});
		this.positions[7][7] = new Rook("bR","black", new int[] {7,7});
	}
	
	/**
	 * Initializes the board with an empty positions array.
	 * 
	 * @param k		the length of the square positions array
	 */
	public Board(int k) {
		this.positions = new Piece[k][k];
	}

	/**
	 * Creates an array of strings representing the positions of pieces on the board,
	 * as well as black, and white squares.
	 * 
	 * @param positions		<code>Piece</code> double array that stores information on every square 
	 * 						of the board
	 * @return				<String> array representing the current board to be printed
	 */
	private String[][] addHashes(Piece[][] positions) {
		
		String[][] boardMap = new String[8][8];
		for (int i = 7; i >= 0; i--) {
			for (int j = 0; j < 8; j++) {
				
				if (positions[i][j].getName().compareTo("na") == 0) {
					
					if (i % 2 == 1) {
						if (j % 2 == 0) { boardMap[7-i][j] = "  "; }
						else { boardMap[7-i][j] = "##"; }
					} else {
						if (j % 2 == 1) { boardMap[7-i][j] = "  "; }
						else { boardMap[7-i][j] = "##"; }
					}
				
				} else {
					
					boardMap[7-i][j] = positions[i][j].getName();
					
				}
				
			}
		}
		
		return boardMap;
		
		
	}
	
	/**
	 * Compares the current board with another given board. Returns <code>true</code> 
	 * if they are the same, <code>false</code> otherwise.
	 * 
	 * @param b		The board being compared to this current board
	 * @return		<code>true</code> if all pieces are in the same positions; <code>false</code> otherwise.
	 */
	public boolean equals(Board b) {
		
		Piece[][] pos = this.positions;
		Piece[][] q = b.positions;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				Piece A = pos[i][j];
				Piece B = q[i][j];
				if (!A.getName().equals(B.getName())) { return false; } 
				if (!A.getColor().equals(B.getColor())) { return false; } 
				if (!(A.getCoords()[0] == B.getCoords()[0])) { return false; } 
				if (!(A.getCoords()[1] == B.getCoords()[1])) { return false; } 
			}
		}
		
		return true;
		
	}
	
	/**
	 * Prints the current state of this board.
	 */
	public void printBoard() {
		
		String[][] boardStr = addHashes(this.positions);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				System.out.print(boardStr[i][j] + " ");
			}
			System.out.print(8-i);
			System.out.println();
		}
		System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println("");
		
		
	}

}
