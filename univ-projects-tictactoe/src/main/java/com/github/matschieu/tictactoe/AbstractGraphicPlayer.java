package com.github.matschieu.tictactoe;

import javax.swing.*;

public abstract class AbstractGraphicPlayer extends AbstractPlayer {

	public AbstractGraphicPlayer(String login, String passwd) {
		super("JOUEUR", login, passwd);
		try { this.name = JOptionPane.showInputDialog(this.name + " - veuillez saisir votre nom : "); }
		catch(Exception e) { JOptionPane.showMessageDialog(null, e.toString()); }
	}
	public AbstractGraphicPlayer() { this("", ""); }

	public abstract Coordinate play();

}
