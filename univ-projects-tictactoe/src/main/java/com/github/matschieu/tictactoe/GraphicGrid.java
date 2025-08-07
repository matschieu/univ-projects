package com.github.matschieu.tictactoe;

import java.awt.*;
import javax.swing.*;

import com.github.matschieu.tictactoe.*;

public class GraphicGrid implements Grid {

	private static final String EMPTY_CASE = "   ";
	private JButton[][] buttons;
	private ButtonListener listener;
	
	public GraphicGrid(String title, int cols, int rows) {
		this.buttons = new JButton[cols][rows];
		this.listener = new ButtonListener(buttons);
		JFrame frame = new JFrame(title);
		frame.setLayout(new GridLayout(rows, cols));
		for(int i = 0; i < buttons.length; i++)
			for(int j = 0; j < buttons[0].length; j++) {
				buttons[j][i] = new JButton(EMPTY_CASE);
				buttons[j][i].addActionListener(listener);
				frame.add(buttons[j][i]);
			}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 200);
		frame.setVisible(true);
		frame.pack();
	}
	public GraphicGrid(String title) { this(title, COLS, ROWS); }
	
	public int getCols() { return buttons[0].length; }
	public int getRows() { return buttons.length; }

	public boolean isEmpty(Coordinate c) { return buttons[c.getX()][c.getY()].equals(EMPTY_CASE); }
	
	public void setCase(Coordinate c, Pawn p) { buttons[c.getX()][c.getY()].setText(p.toString()); }
	
	public void refresh(GridTerm g) {
		for(int i = 0; i < buttons.length; i++)
			for(int j = 0; j < buttons[0].length; j++) {
				Coordinate c = new Coordinate(j, i);
				try { if (!g.isEmpty(c)) setCase(c, g.getPawn(c)); }
				catch(Exception e) { }
			}
	}
	
	public void reset() {
		for(int i = 0; i < buttons.length; i++)
			for(int j = 0; j < buttons[0].length; j++)
				buttons[j][i].setText(EMPTY_CASE);	
	}
	
	public void setCurrentPlayer(GraphicPlayer p) { listener.setCurrentPlayer(p); }

}
