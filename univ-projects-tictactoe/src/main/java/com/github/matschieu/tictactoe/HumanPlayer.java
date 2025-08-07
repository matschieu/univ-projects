package com.github.matschieu.tictactoe;


public class HumanPlayer extends AbstractHumanPlayer  {

	public HumanPlayer(String login, String passwd) { super(login, passwd); }
	public HumanPlayer() { super(); }

	@Override
	public Coordinate play() {
		try {
			InputTerm input = new InputTerm();
			int x, y;
			System.out.print(name + " - veuillez saisir X : ");
			x = input.inputInt() - 1;
			System.out.print(name + " - veuillez saisir Y : ");
			y = input.inputInt() - 1;
			return new Coordinate(x, y);
		}
		catch(Exception e) { System.err.println(e.toString() + "\n"); }
		return play();
	}

}
