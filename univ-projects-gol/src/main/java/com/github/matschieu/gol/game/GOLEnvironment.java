
package com.github.matschieu.gol.game;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.jdom.*;

import com.github.matschieu.gol.config.*;
import com.github.matschieu.gol.tools.*;

/**
 * @author Matschieu
 */
public class GOLEnvironment extends Grid {
	
	private int rate;
	
	public GOLEnvironment(int height, int width, int rate) throws NumberFormatException {
		super(true);
		this.reset(height, width, rate);
	}

	public GOLEnvironment(Document doc) throws Exception {
		super(true);
		this.reset(doc);
	}
	
	public void reset(int height, int width, int rate) throws NumberFormatException {
		if (height < 0) throw new NumberFormatException("" + height);
		if (width < 0) throw new NumberFormatException("" + width);
		if (rate < 0 || rate > 100) throw new NumberFormatException("" + rate);
		this.cells = new GOLCell[height][width];
		this.generation = 0;
		this.rate = rate;
		for(int i = 0; i < this.getHeight(); i++) {
			for(int j = 0; j < this.getWidth(); j++) {
				Position p = new Position(j, i);
				int rdm = (int)(Math.random() * 101);
				if (rdm <= rate)
					this.cells[i][j] = GOLCell.ALIVE;
				else
					this.cells[i][j] = GOLCell.DEAD;
			}
		}
		this.notifyChangesObservers();
	}
	
	public void reset(Document doc) throws Exception {
		Element root = doc.getRootElement();
		Element config = root.getChild("config");
		int width = Integer.parseInt(config.getChild("width").getText());
		int height = Integer.parseInt(config.getChild("height").getText());
		this.cells = new GOLCell[height][width];
		for(int i = 0; i < this.getHeight(); i++) {
			for(int j = 0; j < this.getWidth(); j++) {
				this.cells[i][j] = GOLCell.DEAD;
			}
		}
		this.rate = Integer.parseInt(config.getChild("rate").getText());
		this.generation = Integer.parseInt(config.getChild("generation").getText());			
		String iconsStr = config.getChild("icons").getText();
		if (iconsStr.toUpperCase().equals("TRUE"))
			this.setDisplayIconOption(true);
		else if (iconsStr.toUpperCase().equals("FALSE"))
			this.setDisplayIconOption(false);
		else
			throw new Exception(iconsStr);
		java.util.List<?> cellsList = root.getChild("aliveCells").getChildren("cell");
		Iterator<?> it = cellsList.iterator();
		while(it.hasNext()) {
			Element tmp = (Element)it.next();
			int x = Integer.parseInt(tmp.getChild("x").getText());
			int y = Integer.parseInt(tmp.getChild("y").getText());
			this.cells[y][x] = GOLCell.ALIVE;
		}
		this.notifyChangesObservers();
	}
	
	public Document toXMLDocument() {
		Element root = new Element("gol");
		Element config = new Element("config");
		Element width = new Element("width");
		Element height = new Element("height");
		Element rate = new Element("rate");
		Element generation = new Element("generation");
		Element icons = new Element("icons");
		width.setText("" + this.getWidth());
		height.setText("" + this.getHeight());
		rate.setText("" + this.rate);
		generation.setText("" + this.generation);
		icons.setText(this.getDisplayIconOption() ? "true" : "false");
		config.addContent(width);
		config.addContent(height);
		config.addContent(rate);
		config.addContent(generation);
		config.addContent(icons);
		root.addContent(config);
		Element aliveGOLCells = new Element("aliveCells");
		for(int i = 0; i < this.getHeight(); i++) {
			for(int j = 0; j < this.getWidth(); j++) {
				if (((GOLCell)this.cells[i][j]).isAlive()) {
					Element cell = new Element("cell");
					Element x = new Element("x");
					Element y = new Element("y");
					x.setText("" + j);
					y.setText("" + i);
					cell.addContent(x);
					cell.addContent(y);
					aliveGOLCells.addContent(cell);
				}
			}
		}
		root.addContent(aliveGOLCells);
		Document doc = new Document(root);
		doc.setDocType(new DocType("gol","config/dtd/gol_dtd.dtd"));
		return doc;
	}

	public int getRate() {
		return this.rate;
	}
	
	private int countAliveNeighbours(Position p) {
		int nbAlive = 0;
		for(int i = p.getY() - 1; i < p.getY() + 2; i++) {
			for(int j = p.getX() - 1; j < p.getX() + 2; j++) {
				int y = (i < 0 ? this.cells.length - 1 : i); 
				y = y > this.cells.length - 1 ? 0 : y;
				int x = (j < 0 ? this.cells[0].length - 1 : j); 
				x = x > this.cells[0].length - 1 ? 0 : x;
				if (i == p.getY() && j == p.getX()) continue;
				if (((GOLCell)this.cells[y][x]).isAlive()) nbAlive++;
			}
		}
		return nbAlive;
	}
	
	public void nextGeneration() {
		GOLCell[][] tmp = new GOLCell[this.getHeight()][this.getWidth()];
		for(int i = 0; i < this.getHeight(); i++) {
			for(int j = 0; j < this.getWidth(); j++) {
				Position p = new Position(j, i);
				int alive = this.countAliveNeighbours(p);
				tmp[i][j] = GOLCell.DEAD;
				GOLCell c = (GOLCell)this.getPositionContent(p);
				if (!c.isAlive() && alive == 3) {
					tmp[i][j] = GOLCell.ALIVE;
				}
				else if (c.isAlive() && (alive == 2 || alive == 3)) {
					tmp[i][j] = GOLCell.ALIVE;
				}
			}
		}
		this.cells = tmp;
		this.generation++;
		this.notifyChangesObservers();
	}
	
	private void notifyChangesObservers() {
		String notify = Language.SINGLETON.getElement("GOL_ENVIRONMENT_NOTIF");
		notify = notify.replace("[[GENERATION]]", "" + this.generation);
		this.setChanged();
		this.notifyObservers(notify);
	}
	
}