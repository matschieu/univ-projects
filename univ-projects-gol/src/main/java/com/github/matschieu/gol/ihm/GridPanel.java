
package com.github.matschieu.gol.ihm;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import com.github.matschieu.gol.game.*;
import com.github.matschieu.gol.tools.*;

/**
 * @author Matschieu
 */
public class GridPanel extends JPanel implements Observer {
	
	private Grid grid;
	
	public GridPanel(Grid grid) {
		this.grid = grid;
		this.grid.addObserver(this);
		this.setPreferredSize(new Dimension(this.grid.getPixelWidth(), this.grid.getPixelHeight()));
	}
	
	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D)g;
		this.revalidate();
		gr.setColor(new Color(0, 128, 255));
		gr.fillRect(0, 0, grid.getPixelWidth(), grid.getPixelHeight());
		for(int i = 0; i < this.grid.getHeight(); i++) {
			for(int j = 0; j < this.grid.getWidth(); j++) {
				GridCell c = this.grid.getPositionContent(new Position(j, i));
				if (c.getIcon() == null || !this.grid.getDisplayIconOption()) {
					gr.setColor(c.getColor());
					gr.fillRect(j * GridCell.BORDER_SIDE, i * GridCell.BORDER_SIDE, GridCell.BORDER_SIDE, GridCell.BORDER_SIDE);
				}
				else
					gr.drawImage(c.getIcon().getImage(), j * c.getIcon().getIconWidth(), i * c.getIcon().getIconHeight(), null);
	
			}
		}
	}
	
	public void update(Observable o, Object arg) {
		this.setPreferredSize(new Dimension(this.grid.getPixelWidth(), this.grid.getPixelHeight()));
		this.repaint();
	}
	
}