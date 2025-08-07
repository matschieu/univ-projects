package com.github.matschieu.tictactoe;

public class Game {

	public static void main(String[] args) {
		try {
			if (args[0].equals("-1")) (new TicTacToe(true)).start();
			else if (args[0].equals("-2")) (new TicTacToe(false)).start();
			else Game.displayTTTStartError("Erreur : argument incorrect");
		}
		catch(ArrayIndexOutOfBoundsException obe) { 
			Game.displayTTTStartError("Erreur : Aucun argument");
			System.exit(1);
		}
		System.exit(0);
	}

	public static void displayTTTStartError(String erreur) {
		StringBuffer sb = new StringBuffer("");
		sb.append("\n" + erreur + "\n\n");
		sb.append("\tArguments possibles :\n");
		sb.append("\t\t-1 : jeu a un joueur\n");
		sb.append("\t\t-2 : jeu a deux joueurs\n\n");
		sb.append("\tRegles du Tic Tac Toe:\n");
		sb.append("\t\tAligner 3 pions horizontalement, verticalement ou en diagonale");
		System.out.println(sb.toString());
	}

}
