package com.github.matschieu.tictactoe;

public abstract class AbstractRobotPlayer extends AbstractPlayer {

	public static final String ROBOT_NAME = "ROBOT";

	public AbstractRobotPlayer() { super(ROBOT_NAME); }

	abstract public Coordinate play();

}
