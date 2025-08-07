package com.github.matschieu.maths;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
*@author Matschieu
*/
public class MenuBar extends JMenuBar implements ActionListener {

	private JMenu[] menu;
	private JMenuItem[] file;
	private JMenuItem[] system;
	private JMenuItem[] resolution;
	private JMenuItem[] help;
	private Project project;

	public MenuBar(Project p) {
		project = p;
		menu = new JMenu[4];
		menu[0] = new JMenu("Fichier");
		menu[1] = new JMenu("Système");
		menu[2] = new JMenu("Résolution");
		menu[3] = new JMenu("?");
		file = new JMenuItem[4];
		file[0] = new JMenuItem("Nouveau");
		file[1] = new JMenuItem("Ouvrir");
		file[2] = new JMenuItem("Enregistrer");
		file[3] = new JMenuItem("Quitter");
		system = new JMenuItem[4];
		system[0] = new JMenuItem("Saisir");
		system[1] = new JMenuItem("A partir des formules");
		system[2] = new JMenuItem("Aléatoire");
		system[3] = new JMenuItem("Afficher la taille de la matrice");
		resolution = new JMenuItem[7];
		resolution[0] = new JMenuItem("Méthode de Gauss 1");
		resolution[1] = new JMenuItem("Méthode de Gauss 2");
		resolution[2] = new JMenuItem("Méthode de Gauss 3");
		resolution[3] = new JMenuItem("Méthode de Jordan 1");
		resolution[4] = new JMenuItem("Méthode de Jordan 2");
		resolution[5] = new JMenuItem("Méthode de Jordan 3");
		resolution[6] = new JMenuItem("Calculer la norme de l'erreur");
		help = new JMenuItem[1];
		help[0] = new JMenuItem("A propos de...");
		for(int i = 0; i < file.length; i++) {
			file[i].addActionListener(this);
			menu[0].add(file[i]);
		}
		for(int i = 0; i < system.length; i++) {
			system[i].addActionListener(this);
			menu[1].add(system[i]);
		}
		for(int i = 0; i < resolution.length; i++) {
			resolution[i].addActionListener(this);
			if (i == 3 || i == resolution.length - 1) menu[2].addSeparator();
			menu[2].add(resolution[i]);
		}
		for(int i = 0; i < help.length; i++) {
			help[i].addActionListener(this);
			menu[3].add(help[i]);
		}
		for(int i = 0; i < menu.length; i++) add(menu[i]);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == file[0]) project.newDocument();
		else if (e.getSource() == file[1]) project.readFile();
		else if (e.getSource() == file[2]) project.saveFile();
		else if (e.getSource() == file[3]) project.exit();
		if (e.getSource() == system[0]) {	project.readValues(); }
		else if (e.getSource() == system[1]) { project.readFormulaValues(); }
		else if (e.getSource() == system[2]) { project.randomSystem(); }
		else if (e.getSource() == system[3]) { project.displaySize(); }
		if (e.getSource() == resolution[0]) project.gaussResolution(Project.GA);
		else if (e.getSource() == resolution[1]) project.gaussResolution(Project.GB);
		else if (e.getSource() == resolution[2]) project.gaussResolution(Project.GC);
		else if (e.getSource() == resolution[3]) project.jordanResolution(Project.JA);
		else if (e.getSource() == resolution[4]) project.jordanResolution(Project.JB);
		else if (e.getSource() == resolution[5]) project.jordanResolution(Project.JC);
		else if (e.getSource() == resolution[6]) project.calculateError();
		if (e.getSource() == help[0]) project.displayInfo();
	}

}
