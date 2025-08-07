package com.github.matschieu.tictactoe;

public abstract class AbstractHumanPlayer extends AbstractPlayer {

	public AbstractHumanPlayer(String login, String passwd) {
		super("JOUEUR", login, passwd);
		try {
			System.out.print("\n");
			InputTerm input = new InputTerm();
			System.out.print(this.name + " - veuillez saisir votre nom : ");
			this.name = input.inputString();
			/*
			System.out.print(this.name + " - veuillez saisir votre prenom : ");
			this.firstName = input.inputString();
			*/
			System.out.print("\n");
		}
		catch(Exception e) { System.err.println(e.toString() + "\n"); }
	}
	public AbstractHumanPlayer() { this("", ""); }

	public abstract Coordinate play();

}
