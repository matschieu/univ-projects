package com.github.matschieu.tictactoe;

import java.util.List;

public interface Player {

	public String getName();
	public String getLogin();
	public void setPawn(Pawn pawn);
	public Pawn getPawn();
	public int getTTTClassement();
	public int getNbPart();
	public void setTTTClassement();
	public void setNbPart();
	public boolean checkLoginFor(String login, String passwd);
	public Coordinate play();

}
