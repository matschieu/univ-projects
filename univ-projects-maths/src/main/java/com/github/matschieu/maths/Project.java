package com.github.matschieu.maths;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
*@author Matschieu
*/
public class Project extends WindowAdapter {

	/* POUR LE PROGRAMMEUR ***********/
	private static final int DVP_REPET = 100;
	private static final boolean DVP_DISPLAY = true;
	/* ******************* ***********/

	public static final String APPLICATION_NAME = "MSysLin";
	public static final int MATRIX_MAX_SIZE = 1000;
	public static final int GA = 1;
	public static final int GB = 2;
	public static final int GC = 3;
	public static final int JA = 1;
	public static final int JB = 2;
	public static final int JC = 3;

	private JFrame frame;
	private MenuBar menuBar;
	private MatrixPanel matPanel;
	private Report report;
	private LinearSystem system;

	public Project() {
		frame = new JFrame(APPLICATION_NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		menuBar = new MenuBar(this);
		report = new Report();
		frame.setJMenuBar(menuBar);
		frame.add(report, BorderLayout.SOUTH);
		frame.pack();
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int frameHeight = (int)frame.getPreferredSize().getHeight();
		int frameWidth = (int)frame.getPreferredSize().getWidth();
		frame.setVisible(true);
		frame.setLocation((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2);
		//frame.setLocation((int)(Math.random() * 100), (int)(Math.random() * 100));
		system = new LinearSystem();
	}

	private void refresh() {
		try {
			if (matPanel != null) frame.remove(matPanel);
			matPanel = new MatrixPanel(system);
			frame.add(matPanel, BorderLayout.CENTER);
			frame.pack();
		}
		catch(NullMatrixException e) { report.append(e.toString()); }
		catch(NullVectorException e) { report.append(e.toString()); }
	}

	/* Lecture d'un fichier */
	public void readFile() {
		String file;
		JFileChooser fileChooser = new JFileChooser();
		if(fileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getCurrentDirectory() + File.separator + fileChooser.getSelectedFile().getName();
			try {
				system.readInFile(file);
				refresh();
				report.append("Système récupéré par lecture du fichier " + file);
			}
			catch(FileNotFoundException e) { report.append(e.toString()); }
			catch(InputMismatchException e) { report.append(e.toString()); }
			catch(InvalidSizeException e) { report.append(e.toString()); }
			catch(NullPointerException e) { }
		}
	}

	/* Lecture de la taille de la matrice et du vecteur */
	private int readSize() {
		try {
			String inputValue = "";
			while(true) {
				try {
					int size = MATRIX_MAX_SIZE + 1;
					while (inputValue.equals("") || size > MATRIX_MAX_SIZE) {
						String msga = "Saisir la taille de la matrice/ du vecteur :";
						inputValue = JOptionPane.showInputDialog(null, msga, "Saisie", JOptionPane.QUESTION_MESSAGE);
						size = Integer.parseInt(inputValue);
						if (size > MATRIX_MAX_SIZE) {
							String msgb = "Erreur : taille trop grande (>" + MATRIX_MAX_SIZE + ")";
							report.append(msgb);
							JOptionPane.showMessageDialog(null, msgb, "Erreur", JOptionPane.ERROR_MESSAGE);
						}
					}
					return size;
				}
				catch(NumberFormatException e) {
					if (inputValue != null) report.append("Erreur : " + inputValue + " n'est pas une valeur correcte");
				}
			}
		}
		catch(NullPointerException e) { }
		return 0;
	}

	/* Affichage de la taille de la matrice */
	public void displaySize() {
		String msg = "";
		if (system.isNull()) msg = "Pas de matrice/vecteur";
		else msg = "Taille de la matrice : " + system.getSize() + " * " + system.getSize() + "\nTaille du vecteur : " + system.getSize();
		JOptionPane.showMessageDialog(null, msg, "Taille du système", JOptionPane.INFORMATION_MESSAGE);
	}

	/* Saisie des valeurs de la matrice */
	public void readValues() {
		int size = readSize();
		if (size > 0) {
			try {
				String inputValue = "";
				SquaredMatrix mat = new SquaredMatrix(size);
				for(int i = 0; i < mat.getSize(); i++) {
					for(int j = 0; j < mat.getSize(); j++) {
						inputValue = "";
						String msg = "Saisir la valeur de M(" + (i + 1) + "," + (j + 1) + ") :";
						while (inputValue.equals(""))
							inputValue = JOptionPane.showInputDialog(null, msg, "Saisie", JOptionPane.QUESTION_MESSAGE);
						try { mat.set(i, j, Double.parseDouble(inputValue)); }
						catch (NumberFormatException e) {
							report.append("Erreur : " + inputValue + " n'est pas une valeur correcte");
							i--;
						}
					}
				}
				inputValue = "";
				Vector vect = new Vector(size);
				for(int i = 0; i < vect.getSize(); i++) {
					String msg = "Saisir la valeur de V(" + (i + 1) + ") :";
					inputValue = "";
					while (inputValue.equals(""))
						inputValue = JOptionPane.showInputDialog(null, msg, "Saisie", JOptionPane.QUESTION_MESSAGE);
					try { vect.set(i, Double.parseDouble(inputValue)); }
					catch (NumberFormatException e) {
						report.append("Erreur : " + inputValue + " n'est pas une valeur correcte");
						i--;
					}
				}
				system.set(mat, vect);
				refresh();
				report.append("Système récupérés par saisie des valeurs");
			}
			catch(NullPointerException e) { }
		}
	}

	/* Saisie des valeurs de la formule de remplissage de la matrice */
	public void readFormulaValues() {
		int size = readSize();
		if (size > 0) {
			try {
				int[] tab1 = new int[6];
				String inputValue = "";
				StringBuffer strbuf = new StringBuffer("");
				strbuf.append("Saisie de la matrice avec la formule\n");
				strbuf.append("          (a * i + b * j + c)\n");
				strbuf.append("          --------------------- = mat(i, j)\n");
				strbuf.append("          (d * i + e * j + f)\n\n");
				strbuf.append("Saisie de ");
				for(int i = 0; i < tab1.length; i++) {
					String msg = strbuf.toString() + (char)(97 + i) + " :";
					inputValue = "";
					while (inputValue.equals(""))
						inputValue = JOptionPane.showInputDialog(null, msg, "Saisie", JOptionPane.QUESTION_MESSAGE);
					try { tab1[i] = Integer.parseInt(inputValue); }
					catch (NumberFormatException e) {
						report.append("Erreur : " + inputValue + " n'est pas une valeur correcte");
						i--;
					}
				}
				SquaredMatrix mat = new SquaredMatrix(size, tab1[0], tab1[1], tab1[2], tab1[3], tab1[4], tab1[5]);
				int[] tab2 = new int[4];
				inputValue = "";
				strbuf = new StringBuffer("");
				strbuf.append("Saisie du vecteur avec la formule\n");
				strbuf.append("          (a * i + b)\n");
				strbuf.append("          ------------- = vect(i)\n");
				strbuf.append("          (c * i + d)\n\n");
				strbuf.append("Saisie de ");
				for(int i = 0; i < tab2.length; i++) {
					String msg = strbuf.toString() + (char)(97 + i) + " :";
					inputValue = "";
					while (inputValue.equals(""))
						inputValue = JOptionPane.showInputDialog(null, msg, "Saisie", JOptionPane.QUESTION_MESSAGE);
					try { tab2[i] = Integer.parseInt(inputValue); }
					catch (NumberFormatException e) {
						report.append("Erreur : " + inputValue + " n'est pas une valeur correcte");
						i--;
					}
				}
				Vector vect = new Vector(size, tab2[0], tab2[1], tab2[2], tab2[3]);
				system.set(mat, vect);
				refresh();
				report.append("Système généré par saisie de formule");
			}
			catch(NullPointerException e) { }
		}
	}

	/* Saisie du taux de remplissage de la matrice */
	public void randomSystem() {
		int size = readSize();
		if (size > 0) {
			String inputValue = "";
			try {
				while (inputValue.equals("")) {
					String msg = "Saisir le taux de remplissage (en %) :";
					inputValue = JOptionPane.showInputDialog(null, msg, "Saisie", JOptionPane.QUESTION_MESSAGE);
				}
				system.set(new SquaredMatrix(size, Integer.parseInt(inputValue)), new Vector(size, Integer.parseInt(inputValue)));
				refresh();
				report.append("Système généré aléatoirement");
			}
			catch(NumberFormatException e) {
				report.append("Erreur : " + inputValue + " n'est pas une valeur correcte");
				system.set(new SquaredMatrix(size, 100), new Vector(size, 100));
				refresh();
				report.append("Système généré aléatoirement");
			}
			catch(NullPointerException e) { }
		}
	}

	/* Résolution de système avec la méthode de Gauss */
	public void gaussResolution(int type) {
		if (!system.isNull()) {
			long time = 0;
			long average = 0;
			int repet = 1;
			for(int i = 0; i < DVP_REPET; i++) {
				if (type == GA) time = system.gaussA();
				else if (type == GB) time = system.gaussB();
				else if (type == GC) time = system.gaussC();
				average += time;
				if (DVP_DISPLAY) System.out.println("Gauss execution : " + time);
			}
			average /= repet;
			if (DVP_DISPLAY) System.out.println("Gauss average execution : " + average);
			if (!system.resIsNull()) {
				int nbs = matPanel.displayResult(system);
				if (nbs == 0)
					report.append("Solution du système A.x = B avec la méthode de Gauss " + type +" correctement générée en " + time + " nano secondes");
				else {
					String msg = "Il existe une infinité de solutions (ici on a pris 0 comme valeur arbitraire pour résoudre le système)";
					report.append("Solution du système A.x = B avec la méthode de Gauss " + type +" correctement générée en " + time + " nano secondes : " + msg);
					JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else  { report.append("Pas de solution au système A.x = B avec la méthode de Gauss " + type); }
		}
		else report.append("Erreur : aucun système à résoudre");
	}

	/* Résolution de système avec la méthode de Jordan */
	public void jordanResolution(int type) {
		if (!system.isNull()) {
			long time = 0;
			long average = 0;
			int repet = 1;
			for(int i = 0; i < DVP_REPET; i++) {
				if (type == JA) time = system.jordanA();
				else if (type == JB) time = system.jordanB();
				else if (type == JC) time = system.jordanC();
				average += time;
				if (DVP_DISPLAY) System.out.println("Jordan execution : " + time);
			}
			average /= repet;
			if (DVP_DISPLAY) System.out.println("Jordan average execution : " + average);
			if (!system.resIsNull()) {
				int nbs = matPanel.displayResult(system);
				if (nbs == 0)
					report.append("Solution du système A.x = B avec la méthode de Jordan " + type +" correctement générée en " + time + " nano secondes");
				else {
					String msg = "Il existe une infinité de solution (ici on a pris 0 comme valeur arbitraire pour résoudre le système)";
					report.append("Solution du système A.x = B avec la méthode de Jordan " + type +" correctement générée en " + time + " nano secondes : " + msg);
					JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			else  { report.append("Pas de solution au système A.x = B avec la méthode de Jordan " + type); }
		}
		else report.append("Erreur : aucun système à résoudre");
	}

	/* Calcul de l'erreur du système */
	public void calculateError() {
		String msg = "";
		if (!system.isNull() && !system.resIsNull()) {
			double error = system.error();
			msg = "Norme de l'erreur :\n||E|| = " + error;
			report.append("Calcul de l'erreur E = A.x - B");
		}
		else msg = "Pas de système/solution";
		JOptionPane.showMessageDialog(null, msg, "Calcul de l'erreur", JOptionPane.INFORMATION_MESSAGE);
	}

	/* Nouveau document */
	public void newDocument() {
		if (matPanel != null) {
			frame.remove(matPanel);
			frame.pack();
		}
		report.append("Nouveau document");
	}

	/* Sauvegarde */
	public void saveFile() {
		if (system.isNull()) report.append("Erreur : aucun système à sauvegarder");
		else {
			JFileChooser fileChooser = new JFileChooser();
			String file = "";
			if(fileChooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getCurrentDirectory() + File.separator + fileChooser.getSelectedFile().getName();
				try {
					system.saveInFile(file);
					report.append("Système sauvegardé dans le fichier " + file);
				}
				catch(NoSystemException e) { report.append(e.toString()); }
				catch(WriteFileException e) { report.append(e.toString()); }
				catch(FileNotFoundException e) { report.append(e.toString()); }
			}
		}
	}

	/* Méthodes de l'application */
	@Override
	public void windowClosing(WindowEvent e) { exit(); }
	public void exit() { System.exit(0); }
	public void displayInfo() {
		StringBuffer strbuf = new StringBuffer("");
		strbuf.append("<html><center><b>" + APPLICATION_NAME + "</b><br><br>");
		strbuf.append("Projet d'algèbre linéaire<br>");
		strbuf.append("Résolution de systèmes Ax=B<br>");
		strbuf.append("DUT informatique, 2007<br><br>");
		strbuf.append("Matschieu<br></center></html>");
		JOptionPane.showMessageDialog(null, strbuf.toString(), APPLICATION_NAME, JOptionPane.INFORMATION_MESSAGE);
	}

}
