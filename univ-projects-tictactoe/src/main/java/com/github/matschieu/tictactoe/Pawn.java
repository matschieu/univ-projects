package com.github.matschieu.tictactoe;

public class Pawn {

	public static final Pawn EMPTY_PAWN = new Pawn('.');
	private static final char CHAR1 = 'x';
	private static final char CHAR2 = 'o';
	public static final Pawn CROSS= new Pawn(CHAR1);
	public static final Pawn CIRCLE = new Pawn(CHAR2);
	private OutputTerm term = new OutputTerm();
	protected char symbole;
	
	protected Pawn(char symbole) { this.symbole = symbole; }

	public char getPawnChar() { return symbole; }

	public String toString() {
		return "" + symbole;
		//if (this == CROSS) return  term.BLUE + symbole + term.DEFAULT_COLOR;
		//else return  term.GREEN + symbole + term.DEFAULT_COLOR;
	}

}
