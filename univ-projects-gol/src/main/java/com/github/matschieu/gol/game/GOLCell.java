
package com.github.matschieu.gol.game;
 
import java.awt.Color;
import javax.swing.ImageIcon;

import com.github.matschieu.gol.config.*;
import com.github.matschieu.gol.loader.*;
import com.github.matschieu.gol.tools.*;

/**
 * @author Matschieu
 */
public class GOLCell implements GridCell {

	public static final char  ALIVE_SYMBOLE  = '*';
	public static final char  DEAD_SYMBOLE   = ' ';
	public static final Color ALIVE_COLOR    = Color.ORANGE;
	public static final Color DEAD_COLOR     = Color.BLACK;
	public static final ImageIcon ALIVE_ICON = ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("ALIVE_CELL_ICON"));
	public static final ImageIcon DEAD_ICON  = ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("DEAD_CELL_ICON"));
	
	private boolean isAlive;
	private Position position;

	public static final GOLCell ALIVE = new GOLCell(true, null);
	public static final GOLCell DEAD  = new GOLCell(false, null);
	
	private GOLCell(boolean isAlive, Position p) {
		this.isAlive = isAlive;
		this.position = position;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public Position getPosition() {
		return this.position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public char getSymbole() {
		if (isAlive) return ALIVE_SYMBOLE;
		return DEAD_SYMBOLE;
	}

	public Color getColor() {
		if (isAlive) return ALIVE_COLOR;
		return DEAD_COLOR;
	}
	
	public ImageIcon getIcon() {
		if (isAlive) return ALIVE_ICON;
		return DEAD_ICON;
	}

	public String toString() {
		return "" + this.getSymbole();
	}

}
