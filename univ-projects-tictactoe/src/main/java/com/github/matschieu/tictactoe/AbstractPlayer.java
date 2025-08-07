package com.github.matschieu.tictactoe;

public abstract class AbstractPlayer implements Player {

	protected String name;
	protected String login;
	protected String passwd;
	protected Pawn pawn;
	protected int tttScore;
	protected int nbPart;

	public AbstractPlayer(String name, String login, String passwd) {
		this.name = name;
		this.login = login;
		this.passwd = passwd;
		this.pawn = null;
		this.tttScore = 0;
		this.nbPart = 0;
	}
	public AbstractPlayer(String name) {
		this(name, "", "");
	}

	public String getName() { return name; }
	public String getLogin() { return login; }
	public void setPawn(Pawn pawn) { this.pawn = pawn; }

	public Pawn getPawn() { return pawn; }
	public int getTTTClassement() { return tttScore; }
	public int getNbPart() { return nbPart; }

	public void setTTTClassement() { tttScore++; }
	public void setNbPart() { nbPart++; }

	public boolean checkLoginFor(String login, String passwd) {
		return login.equals(this.login) && passwd.equals(this.passwd);
	}

	public String toString() { return "Pseudo : " + name + "\t\tlogin : " + login; }

	public abstract Coordinate play();

}
