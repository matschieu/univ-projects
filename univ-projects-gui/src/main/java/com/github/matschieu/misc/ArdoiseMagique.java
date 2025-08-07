package com.github.matschieu.misc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JPanel;

class Point {
	public Integer x,y;

	Point() {
		x = 0;
		y = 0;
	}

	Point(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}
}

class Curve {
	public ArrayList<Point> points;

	Curve() {
		points = new ArrayList<Point>();
	}

	public void addPoint(Point P) {
		points.add(P);
	}

	public void clear() {
		points.clear();
	}
}

public class ArdoiseMagique extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Curve> curves;

	public ArdoiseMagique(){
		curves = new ArrayList<Curve>();
		curves.add(new Curve());
		setBackground(Color.white);

		// Gérer les évènements liés aux boutons de la souris
		this.addMouseListener(
			new MouseAdapter() {
				// Si on lache un bouton de la souris
				@Override
				public void mouseReleased(MouseEvent e) { newCurve(); }
				// Si on presse un bouton de la souris
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) // Si c'est le bouton gauche
						addPoint(e.getX(), e.getY());
					else // si c'est le bouton droit
						clear();
				}
			}
		);

		// Gérer l'évènement "la souris bouge"
		this.addMouseMotionListener(
			new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) { addPoint(e.getX(), e.getY()); }
			}
		);
	}

	public void addPoint(Integer x, Integer y) {
		curves.get(curves.size()-1).addPoint(new Point(x,y));
		repaint();
	}

	public void newCurve() {
		curves.add(new Curve());
	}

	public void clear() {
		curves.clear();
		curves.add(new Curve());
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		Point Pprev, Pcurrent;
		super.paintComponent(g);

		Iterator<Curve> itcurve = curves.iterator();

		Pprev = new Point();

		// Pour chaque courbe
		while (itcurve.hasNext()) {
			Iterator<Point> it = itcurve.next().points.iterator();

			if (it.hasNext()) {
				Pprev = it.next();
			}

			// Dessine les points d'une courbe
			while (it.hasNext()) {
				Pcurrent = it.next();
				g.drawLine(Pprev.x,Pprev.y, Pcurrent.x, Pcurrent.y);
				Pprev = Pcurrent;
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("ArdoiseMagique");
		ArdoiseMagique ardoise = new ArdoiseMagique();
		frame.getContentPane().add(ardoise);
		frame.setPreferredSize(new Dimension(900, 500));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}