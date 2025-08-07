package com.github.matschieu.test;

import javax.swing.*;
import java.awt.*;


// NullLayout
class NullLayoutPanel extends JPanel {	
	public NullLayoutPanel() {
		JLabel label = new JLabel("Ceci_est_un_long_texte");
		JButton button = new JButton("...");
		setLayout(null);
		label.setBounds(0,0,200,20);
		button.setBounds(30,40,50,20);
		add(label);
		add(button);
	}
}


// FlowLayout
class FlowLayoutPanel extends JPanel {
	public FlowLayoutPanel() {
		add(new JButton("Hello"));
		add(new JLabel("World"));
		add(new JButton("What's"));
		add(new JLabel("new"));
		add(new JButton("Today"));
	}
}


// BorderLayout
class BorderLayoutPanel extends JPanel {
	public BorderLayoutPanel() {
		setLayout(new BorderLayout());
		add(new JButton("Le NORTH"), BorderLayout.NORTH);
		add(new JButton("On dirait le SOUTH"), BorderLayout.SOUTH);
		add(new JButton("East is here"), BorderLayout.EAST);
		add(new JButton("A l'WEST"), BorderLayout.WEST);
		add(new JLabel("le CENTER"), BorderLayout.CENTER);
	}
}


// BoxLayout
class BoxLayoutPanel extends JPanel {
	public BoxLayoutPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JButton("Petit bouton"));
		add(new JButton("?"));
		add(new JButton("Foo"));
		add(new JButton("Bouton un peu grand"));
		add(new JButton("Bar"));
	}
}


// GridLayout
class GridLayoutPanel extends JPanel {
	public GridLayoutPanel() {
		setLayout(new GridLayout(3, 2));
		add(new JButton("Je ne sais"));
		add(new JButton("plus"));
		add(new JButton("quoi"));
		add(new JButton("�crire pour illustrer"));
		add(new JButton("cet exemple..."));
	}
}


// GridBagLayout
class GridBagLayoutPanel extends JPanel {
	public GridBagLayoutPanel() {
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridBag);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		addButton("bouton 1", gridBag, c);
		addButton("bouton 2", gridBag, c);
		addButton("bouton 3", gridBag, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addButton("bouton 4", gridBag, c);
		addButton("bouton 5", gridBag, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.gridwidth = 3;
		c.gridheight = 1;
		addButton("bouton 6", gridBag, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		addButton("bouton 7", gridBag, c);
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 1;
		c.gridheight = 2;
		addButton("bouton 8", gridBag, c);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight =  1;
		c.weighty = 0.0;
		addButton("bouton 9", gridBag, c);
		addButton("bouton 10", gridBag, c);
	}
	protected void addButton(String name, GridBagLayout gridBag, GridBagConstraints c) {
		JButton button = new JButton(name);
		gridBag.setConstraints(button, c);
		add(button);
	}
}

/**
 * @author Matschieu
 */
public class LayoutExercise {
	public static void main(String[] args) {
		// Exercice 1 : NullLayout
		JFrame nullWindow = new JFrame("NullLayout");
		nullWindow.setVisible(true);
		nullWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nullWindow.add(new NullLayoutPanel());
		nullWindow.setSize(200,100);
		nullWindow.setLocation(0, 50); 
		// Exercice 2 : FlowLayout
		JFrame flowWindow = new JFrame("FlowLayout");
		flowWindow.setVisible(true);
		flowWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		flowWindow.add(new FlowLayoutPanel());
		flowWindow.pack();
		flowWindow.setLocation(300, 50); 
		// Exercice 3 : BorderLayout
		JFrame borderWindow = new JFrame("BorderLayout");
		borderWindow.setVisible(true);
		borderWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		borderWindow.add(new BorderLayoutPanel());
		borderWindow.pack();
		borderWindow.setLocation(700, 50);
		// Exercice 4 : BoxLayout
		JFrame boxWindow = new JFrame("BoxLayout");
		boxWindow.setVisible(true);
		boxWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boxWindow.add(new BoxLayoutPanel());
		boxWindow.pack();
		boxWindow.setLocation(1000, 50);
		// Exercice 5 : GridLayout
		JFrame gridWindow = new JFrame("GridLayout");
		gridWindow.setVisible(true);
		gridWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gridWindow.add(new GridLayoutPanel());
		gridWindow.pack();
		gridWindow.setLocation(0, 200);
		// Exercice 7 : GridBagLayout
		JFrame bagWindow = new JFrame("GridBagLayout");
		bagWindow.setVisible(true);
		bagWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bagWindow.add(new GridBagLayoutPanel());
		bagWindow.pack();
		bagWindow.setLocation(400, 200);
	}
}
