
package com.github.matschieu.gol.game;

import java.util.*;

import com.github.matschieu.gol.tools.*;

/**
 * @author Matschieu
 */
public abstract class Grid extends Observable {
	
	protected GridCell[][] cells;
	protected int generation;
	protected boolean displayIcon;
	
	public Grid(boolean displayIcon) {
		this.displayIcon = displayIcon;
	}
	
	public void setDisplayIconOption(boolean displayIcon) {
		this.displayIcon = displayIcon;
	}
	
	public boolean getDisplayIconOption() {
		return this.displayIcon;
	}
	
	public int getHeight() {
		return this.cells.length;
	}
	
	public int getWidth() {
		return this.cells[0].length;
	}
	
	public int getPixelHeight() {
		if (!displayIcon) return this.cells.length * GridCell.BORDER_SIDE;
		return this.cells.length * this.cells[0][0].getIcon().getIconHeight();
	}
	
	public int getPixelWidth() {
		if (!displayIcon) return this.cells[0].length * GridCell.BORDER_SIDE;
		return this.cells[0].length * this.cells[0][0].getIcon().getIconWidth();
	}
	
	public int getGeneration() {
		return this.generation;
	}
	
	public GridCell getPositionContent(Position position) {
		return (GridCell)this.cells[position.getY()][position.getX()];
	}
	
	public void setPositionContent(Position position, GridCell content) {
		this.cells[position.getY()][position.getX()] = content;
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer("");
		for(int j = 0; j < this.getWidth() * 2 + 1; j++)
			str.append("-");
		str.append("\n");
		for(int i = 0; i < this.getHeight(); i++) {
			for(int j = 0; j < this.getWidth(); j++) {
				str.append("|" + this.cells[i][j].getSymbole());
				if (j == this.getWidth() - 1)
					str.append("|");
			}
			str.append("\n");
		}
		for(int j = 0; j < this.getWidth() * 2 + 1; j++)
			str.append("-");
		return str.toString();
	}
	
	public abstract void nextGeneration();
	
}