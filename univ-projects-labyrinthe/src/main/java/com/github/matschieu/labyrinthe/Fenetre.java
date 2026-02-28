package com.github.matschieu.labyrinthe;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author  Matschieu
 * @version Mars 2007
 */
public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel panel;
	private JLabel[][] label;

	public Fenetre(String contenu, String titre, int largeur, int hauteur) {
		super(titre);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocation((int)(Math.random() * 200), (int)(Math.random() * 200));
		setVisible(true);
		panel = new JPanel();
		label = new JLabel[hauteur][largeur];
		panel.setLayout(new GridLayout(hauteur, largeur, 0, 0));
		affichage(contenu, largeur, hauteur);
		for(int i = 0; i < label.length; i++)
			for(int j = 0; j < label[0].length; j++)
				panel.add(label[i][j]);
		add(panel);
		pack();
	}

	private void affichage(String contenu, int largeur, int hauteur) {
		int idxChaine = 0;
		for(int i = 0; i < label.length; i++) {
			for(int j = 0; j < label[0].length; j++) {
				label[i][j] = new JLabel(contenu.substring(idxChaine, idxChaine + 1));
				idxChaine++;
			}
			idxChaine++;
		}
	}

}
