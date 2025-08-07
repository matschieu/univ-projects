package com.github.matschieu.tictactoe;

public class OutputTerm {

	public final String RED;
	public final String GREEN;
	public final String YELLOW;
	public final String BLUE;
	public final String DEFAULT_COLOR;

	public OutputTerm() {
		if (!System.getProperty( "os.name" ).substring(0, 3).equals("Win")) {
			RED = "\033[0;31m";
			GREEN = "\033[0;32m";
			YELLOW = "\033[0;33m";
			BLUE = "\033[0;34m";
			DEFAULT_COLOR = "\033[0;49m";
		}
		else {
			RED = "";
			GREEN = "";
			YELLOW = "";
			BLUE = "";
			DEFAULT_COLOR = "";
		}
	}

	public void print(String str) { System.out.print(str); }
	public void println(String str) { System.out.println(); }
	
	public void setStyle(int style) { if (style < 50) System.out.print("\033[0;" + style + "m"); }
	public void defaultStyle() { System.out.print(DEFAULT_COLOR); }
}
