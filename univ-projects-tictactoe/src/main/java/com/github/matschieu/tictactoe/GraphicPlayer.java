package com.github.matschieu.tictactoe;


public class GraphicPlayer extends AbstractGraphicPlayer  {

	private Coordinate position;

	public GraphicPlayer(String login, String passwd) {
		super(login, passwd);
		position = null;
	}
	public GraphicPlayer() { this("", ""); }

	@Override
	public Coordinate play() {
		while(position == null)
			try { this.wait(100); }
			catch(Exception e) { };
		Coordinate pos = position;
		position = null;
		return pos;
	}

	public void setPosition(Coordinate c) { position = c; }

}
