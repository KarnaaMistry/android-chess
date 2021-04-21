package com.example.androidchess10;

import java.util.Scanner;

/**
 * Chess is the class which handles
 * <ul>
 * <li> player inputs
 * <li> input errors
 * <li> translation of input to coordinates
 * <li> whether a player is in check/checkmate
 * </ul>
 * 
 * @author Fin Herbig
 * @author Karnaa Mistry
 */
public class Chess {
	
	/**
	 * A <code>boolean</code> that is <code>true</code> if it is white's turn; 
	 * <code>false</code> if it is black's turn.
	 */
	public static boolean white_turn = true;
	
	/**
	 * An <code>int</code> that equals 2 when the current player can perform enpassant.
	 * It is incremented after every move to help signify either performing enpassant, or
	 * forfeiting the opportunity to do so.
	 */
	public static int enpassant = 2;
	
	/**
	 * An <code>int</code> that copies the value of <code>enpassant</code> when 
	 * creating a backup of the current state of the game.
	 */
	public static int enpassant_b = 2;
	
	/**
	 * An <code>int</code> array that stores the positions of up to two pawns that may
	 * perform enpassant that turn, in index pairs 0,1, and 4,5; index pair 2,3 stores
	 * the position where a pawn performing enpassant would move to.
	 */
	public static int[] enpassloc = new int[] {-1,-1,-1,-1,-1,-1};
	
	/**
	 * An <code>int</code> array that copies the values of <code>enpassloc</code> when
	 * creating a backup of the current state of the game.
	 */
	public static int[] enpassloc_b = new int[] {-1,-1,-1,-1,-1,-1};
	
	/**
	 * A <code>boolean</code> that is <code>true</code> if enpassant is successfully
	 * performed, so that a pawn may be captured in passing; <code>false</code> otherwise.
	 */
	public static boolean enpasscap = false;
	
	/**
	 * An <code>int</code> array that stores the position of the pawn to be captured
	 * during a successful move of enpassant.
	 */
	public static int[] enpasstarg = new int[] {-1, -1};
	
	/**
	 * The main method that handles playing the game of Chess.
	 * @param args		command line arguments
	 */
	public static void main(String[] args) {
		
		boolean gameover = false;
		boolean draw_prompt = false;

		Scanner scan = new Scanner(System.in);
		Board chessboard = new Board();
		
		chessboard.printBoard();
		
		while (!gameover) {
			
			String prompt = "";
			
			if (white_turn) { prompt = "White's turn: "; }
			else { prompt = "Black's turn: "; }
			
			System.out.print(prompt);
			String input = scan.nextLine();
			input = input.trim();
			
			if (!draw_prompt && input.length() < 5) {
				System.out.println("Illegal move, try again");
				continue;
			}
			
			if (input.compareToIgnoreCase("resign") == 0) {
				gameover = true;
				if (white_turn) {
					System.out.println("\nBlack wins");
				}
				else {
					System.out.println("\nWhite wins");
				}
				break;
			}
			
			if (draw_prompt) {
				if (input.compareToIgnoreCase("draw") == 0) {
					gameover = true;
					System.out.println("\ndraw");
					break;
				}
				else {
					draw_prompt = false;
				}
			}
			
			
			int ini[] = translate(input.substring(0,2));
			int end[] = translate(input.substring(3,5));
			if (ini[0] == -1 || ini[1] == -1 || end[0] == -1 || end[1] == -1) {
				System.out.println("Illegal move, try again");
				continue;
			}
			Piece pc = chessboard.positions[ini[0]][ini[1]];  //moving Piece
			Piece targ = chessboard.positions[end[0]][end[1]];  //target Piece
			
			if ((pc.getName().equals("na") || (white_turn && pc.getColor().equals("black")) || (!white_turn && pc.getColor().equals("white")))) {
				System.out.println("Illegal move, try again");
				continue;
			}
			if (((white_turn && targ.getColor().equals("white")) || (!white_turn && targ.getColor().equals("black")))) {
				System.out.println("Illegal move, try again");
				continue;
			}
			if (pc.legalMove(chessboard, end)) {
				
				Board backup_board = backupBoard(chessboard);
				enpassant_b = enpassant;
				for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }
				
				
				pc.move(chessboard, end);
				if (enpasscap) {
					Pawn.enpassCapture(chessboard, enpasstarg[0], enpasstarg[1]);
					enpasstarg[0] = -1;
					enpasstarg[1] = -1;
					enpasscap = false;
				}
				
				
				if (isInCheck(chessboard, white_turn)) {  //revert changes brought by the move
					chessboard = backup_board;
					enpassant = enpassant_b;
					for (int h = 0; h < 6; h++) { enpassloc[h] = enpassloc_b[h]; }
					System.out.println("Illegal move, try again");
					continue;
				}
				
				
			} else {
				System.out.println("Illegal move, try again");
				continue;
			}
			if (pc.getName().charAt(1) == 'p') {  //check for promotion
				if ((white_turn && pc.getCoords()[0] == 7) || (!white_turn && pc.getCoords()[0] == 0)) {
					if (input.length() < 7) {
						Pawn p = (Pawn)pc;
						p.promote(chessboard, 'Q');
					}
					else {
						Pawn p = (Pawn)pc;
						p.promote(chessboard, input.charAt(6));
					}
				}
			}
			enpassant++;
			
			
			if (input.substring(input.length()-5,input.length()).compareToIgnoreCase("draw?") == 0) {
				draw_prompt = true;
			}
			

			
			System.out.println();
			chessboard.printBoard();
			white_turn = !white_turn;
			
			if (isInCheck(chessboard, white_turn)) {
				if (!canEscapeCheck(chessboard, white_turn)) {
					if (white_turn) {
						System.out.println("Checkmate\n\nBlack wins");
					} else {
						System.out.println("Checkmate\n\nWhite wins");
					}
					gameover = true;
					break;
				}
				System.out.println("Check\n");
			}
			
			
		}
		
		scan.close();

		
	}
	
	
	/**
	 * Returns whether or not the indicated player is able to escape check.
	 * 
	 * @param cboard 		current board storing all piece information
	 * @param white_turn 	<code>true</code> if it is white's turn; <code>false</code> if it is black's turn.
	 * @return 				<code>false</code> if the player indicated by <code>white_turn</code>
	 * 						is in checkmate; <code>true</code> otherwise.
	 * @see Board
	 */
	public static boolean canEscapeCheck(Board cboard, boolean white_turn) {
		
		String color;
		if (white_turn) { color = "white"; }
		else { color = "black"; }
		
		Board chessboard = backupBoard(cboard);
		enpassant_b = enpassant;
		for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }
		
		
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				
				if (!chessboard.positions[r][c].getColor().equals(color)) { continue; }
				
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						
						Piece hero = chessboard.positions[r][c];  //Candidate piece that may get the player out of check
						if (hero.legalMove(chessboard, new int[] {i,j})) {
							
							
							Board backup_board = backupBoard(chessboard);

							enpassant_b = enpassant;
							for (int h = 0; h < 6; h++) { enpassloc_b[h] = enpassloc[h]; }

							hero.move(chessboard, new int[] {i,j});
							if (enpasscap) {
								Pawn.enpassCapture(chessboard, enpasstarg[0], enpasstarg[1]);
								enpasstarg[0] = -1;
								enpasstarg[1] = -1;
								enpasscap = false;
							}
				
							
							if (!isInCheck(chessboard, white_turn)) {
								
								chessboard = backup_board;
								enpassant = enpassant_b;
								for (int h = 0; h < 6; h++) { enpassloc[h] = enpassloc_b[h]; }
								
								return true;
							}
							chessboard = backup_board;
							enpassant = enpassant_b;
							for (int h = 0; h < 6; h++) { enpassloc[h] = enpassloc_b[h]; }
						}
						
					}
				}
				
			}
		}
		
		
		
		return false;
		
	}
	
	/**
	 * Returns whether or not the indicated player is currently in check by finding the king's position 
	 * on the board and seeing if that coordinate is being threatened by an enemy piece.
	 * 
	 * @param chessboard 	current board storing all piece information
	 * @param white_turn 	<code>true</code> if it is white's turn; <code>false</code> if it is black's turn.
	 * @return 				<code>false</code> if the player indicated by <code>white_turn</code>
	 *  					is in check; <code>true</code> otherwise.
	 * @see Board
	 */
	public static boolean isInCheck(Board chessboard, boolean white_turn) {
		
		String kingname = "";
		String opponent = "";
		Piece[][] board = chessboard.positions;
		if (white_turn) {
			kingname = "wK";
			opponent = "black";
		}
		else {
			kingname = "bK";
			opponent = "white";
		}
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (board[r][c].getName().equals(kingname)) { 
					if (board[r][c].inDanger(chessboard, opponent)) {
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		
		return false;
		
	}
	
	/**
	 * Saves the current state of the given board and returns it, creating a duplicate.
	 * 
	 * @param boardA		board to be duplicated
	 * @return				<code>Board</code> instance with the same pieces and positions as the given board
	 * @see Board
	 */
	public static Board backupBoard(Board boardA) {
		
		Board boardB = new Board(8);
		Piece[][] posA = boardA.positions;
		Piece[][] posB = boardB.positions;
		
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				Piece p = posA[r][c];
				if (p instanceof Pawn) { posB[r][c] = new Pawn(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}, ((Pawn)p).getFirstMoveFlag()); }
				else if (p instanceof Rook) { posB[r][c] = new Rook(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}, ((Rook)p).getHasMoved()); }
				else if (p instanceof Knight) { posB[r][c] = new Knight(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}); }
				else if (p instanceof Bishop) { posB[r][c] = new Bishop(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}); }
				else if (p instanceof Queen) { posB[r][c] = new Queen(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}); }
				else if (p instanceof King) { posB[r][c] = new King(p.getName(), p.getColor(), new int[] {p.getCoords()[0], p.getCoords()[1]}, ((King)p).getHasMoved());  }
				else if (p instanceof Blank) { posB[r][c] = new Blank(new int[] {p.getCoords()[0], p.getCoords()[1]}); }
				else { }
			}
		}
		
		return boardB;
		
	}
	
	
	
	/**
	 * Takes an input of a string representing a rank and file position on a board and turns it into 
	 * a coordinate which can be used to access piece information on a board. The string input is 
	 * compared to a regex expression and if it matches it is converted to two separate integers, 
	 * one for the file and the rank.
	 * 
	 * @param input		single coordinate passed as a <code>String</code>
	 * @return			<code>int</code> array of size 2 where index 0 stores the row and index 1
	 * 					stores the column
	 */
	public static int[] translate(String input) {
		int output[] = new int[2];
		if(!input.matches(".*[a-h].*.*[1-8].*")) {
			output[0] = -1;
			output[1] = -1;
			return output;
		}
		output[0] = Character.getNumericValue(input.charAt(1)) - 1;
		output[1] = Character.getNumericValue(input.charAt(0)) - 10;
		return output;
	}

}
