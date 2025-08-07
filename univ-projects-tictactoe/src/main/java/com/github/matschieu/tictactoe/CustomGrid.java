package com.github.matschieu.tictactoe;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

import com.github.matschieu.tictactoe.*;

public class CustomGrid extends JPanel {

	private GridTerm grid;
	private ClickListener listener;

	public CustomGrid(GridTerm grid, String title) {
		this.grid = grid;
		this.listener = new ClickListener(this);
		addMouseListener(listener);
		JFrame jf = new JFrame(title);
		jf.add(this);
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		GradientPaint gradient = new GradientPaint(0, 0, Color.yellow, 175, 175, Color.orange, false);
		Rectangle2D.Double square = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
		g2d.setPaint(gradient);
		g2d.fill(square);
		Line2D.Double vline1 = new Line2D.Double(getWidth() / 3, 0, getWidth() / 3, getHeight());
		Line2D.Double vline2 = new Line2D.Double(2 * getWidth() / 3, 0, 2 * getWidth() / 3, getHeight());
		Line2D.Double hline1 = new Line2D.Double(0, getHeight() / 3, getWidth(), getHeight() / 3);
		Line2D.Double hline2 = new Line2D.Double(0, 2 * getHeight() / 3, getWidth(), 2 * getHeight() / 3);
		gradient = new GradientPaint(0, 0, Color.black, 300, 300, Color.red, false);
		g2d.setPaint(gradient);
		g2d.draw(vline1);
		g2d.draw(vline2);
		g2d.draw(hline1);
		g2d.draw(hline2);
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if (j != 0) g2d.translate(getWidth() / 3, 0);
				try {
					if (grid.getPawn(new Coordinate(j, i)) == Pawn.CROSS) drawCross(g2d);
					else if (grid.getPawn(new Coordinate(j, i)) == Pawn.CIRCLE) drawCircle(g2d);
				}
				catch(Exception e) { }
			}
			if (i < 2) g2d.translate(-2 * getWidth() / 3,getHeight() / 3);
			else g2d.translate(-2 * getWidth() / 3, -2 * getHeight() / 3);
		}
	} 
	
	public void drawCross(Graphics2D g2d) {
		GradientPaint gradient = new GradientPaint(0, 0, Color.green, 175, 175, Color.green, false);
		g2d.setPaint(gradient);
		Line2D.Double line1 = new Line2D.Double(0, 0, getWidth() / 3, getHeight() / 3);
		Line2D.Double line2 = new Line2D.Double(getWidth() / 3, 0, 0, getHeight() / 3);
		g2d.draw(line1);
		g2d.draw(line2);
	}

	public void drawCircle(Graphics2D g2d) {
		GradientPaint gradient = new GradientPaint(0, 0, Color.blue, 175, 175, Color.blue, false);
		g2d.setPaint(gradient);
		Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, getWidth() / 3, getHeight() / 3);
		g2d.draw(circle);
	}

	public Dimension getPreferredSize() {
		Dimension dim = new Dimension(300, 300);
		setPreferredSize(dim);
		return dim;
	}
	
	public void setGridTerm(GridTerm grid) { this.grid = grid; }
	
	public void setCurrentPlayer(GraphicPlayer p) { listener.setCurrentPlayer(p); }

}
