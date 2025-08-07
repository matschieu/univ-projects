package com.github.matschieu.tictactoe;

import java.awt.event.*;
import javax.swing.*;

import com.github.matschieu.tictactoe.*;

public class ClickListener extends MouseAdapter {

	private GraphicPlayer player;
	private CustomGrid cg;
	
	public ClickListener(CustomGrid cg) { 	
		this.cg = cg;
		this.player = null;
	}

	public void mouseClicked(MouseEvent e) {
		int x = 0;
		int y = 0;
		if (e.getX() < cg.getWidth() / 3) x = 0;
		else if (e.getX() < 2 * cg.getWidth() / 3) x = 1;
		else if (e.getX() > 2 *cg.getWidth() / 3) x = 2;
		if (e.getY() < cg.getHeight() / 3) y = 0;
		else if (e.getY() < 2 * cg.getHeight() / 3) y = 1;
		else if (e.getY() > 2 *cg.getHeight() / 3) y = 2;
		setPlayerPosition(new Coordinate(x, y));
	}
	
	public void setPlayerPosition(Coordinate c) { player.setPosition(c); }
	
	public void setCurrentPlayer(GraphicPlayer p) { player = p; }
	
}
