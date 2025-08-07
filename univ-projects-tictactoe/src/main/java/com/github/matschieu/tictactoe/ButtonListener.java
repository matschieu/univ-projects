package com.github.matschieu.tictactoe;

import java.awt.event.*;
import javax.swing.*;

import com.github.matschieu.tictactoe.*;

public class ButtonListener implements ActionListener {

	private JButton[][] buttons;
	private GraphicPlayer player;
	
	public ButtonListener(JButton[][] buttons) { 
		this.buttons = buttons;
		this.player = null;
	}

	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < buttons.length; i++)
			for(int j = 0; j < buttons[0].length; j++)
				if (e.getSource() == buttons[j][i]) setPlayerPosition(new Coordinate(j, i));
	}
	
	public void setPlayerPosition(Coordinate c) { player.setPosition(c); }
	
	public void setCurrentPlayer(GraphicPlayer p) { player = p; }
	
}
