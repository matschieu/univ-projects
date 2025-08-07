package com.github.matschieu.tictactoe;


public class RobotPlayer extends AbstractRobotPlayer {

	public RobotPlayer() { super(); }

	@Override
	public Coordinate play() {
		int x = (int)(Math.random() * 3);
		int y = (int)(Math.random() * 3);
		//System.out.println("Le robot a joue la case (" + (x + 1) + "," + (y + 1) + ")");
		return new Coordinate(x, y);
	}

}
