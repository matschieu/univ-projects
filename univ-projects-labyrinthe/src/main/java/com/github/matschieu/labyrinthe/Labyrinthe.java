
package com.github.matschieu.labyrinthe;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author  Matschieu
 * @version Mars 2007
 */
public class Labyrinthe {

	// Attributs du labyrinthe
	private int largeur;
	private int hauteur;
	private char[][] laby;
	private Case depart;
	private Case arrivee;
	// Carcatères qui seront affichés dans le labyrinthe
	private final char CARMUR = '#';
	private final char CARDEPART = 'D';
	private final char CARARRIVEE = '@';
	private final char CARBLOQUE = 'x';
	private final char CARCHEMIN = '.';
	private final char CARVIDE = ' ';

	// Constructeur du labyrinthe
	public Labyrinthe(int largeur, int hauteur, int tauxMurs) {
		this.largeur = largeur + 2;
		this.hauteur = hauteur + 2;
		this.laby = new char[this.hauteur][this.largeur];
		initialisation(tauxMurs);
	}

	private void initialisation(int tauxMurs) {
		int[][] temp = new int[2][largeur * hauteur];
		int nbCasesVides = 0;
		int idx, perm;
		// Création du labyrinthe à partir d'un pourcentage de mur placés aléatoirement
		for (int i = 0; i < this.laby.length; i++)
			for (int j = 0; j < this.laby[0].length; j++) {
				if ((i == 0) || (i == this.hauteur - 1) || (j == 0) || (j == this.largeur - 1))
					this.laby[i][j] = this.CARMUR;
				else
					if ((int)(Math.random() * 100) <= tauxMurs)
						this.laby[i][j] = this.CARMUR;
					else {
						this.laby[i][j] = this.CARVIDE;
						temp[0][nbCasesVides] = j;
						temp[1][nbCasesVides] = i;
						nbCasesVides++;
					}
			}
		// Création d'un point de départ aléatoire sur une case vide
		idx = (int)(Math.random() * nbCasesVides);
		this.depart = new Case(temp[0][idx], temp[1][idx]);
		this.setCase(this.depart.getX(), this.depart.getY(), this.CARDEPART);
		perm = temp[0][idx];
		temp[0][idx] = temp[0][nbCasesVides - 1];
		temp[0][nbCasesVides - 1] = perm;
		perm = temp[1][idx];
		temp[1][idx] = temp[1][nbCasesVides - 1];
		temp[1][nbCasesVides - 1] = perm;
		// Création d'un point d'arrivée aléatoire
		idx = (int)(Math.random() * (nbCasesVides - 1));
		this.arrivee = new Case(temp[0][idx], temp[1][idx]);
		this.setCase(this.arrivee.getX(), this.arrivee.getY(), this.CARARRIVEE);
	}

	// Récupère la hauteur du labyrinthe
	public int getHauteur() {
		return this.hauteur;
	}

	// Récupèrela largeur du labyrinthe
	public int getLargeur() {
		return this.largeur;
	}

	// Récupère une case du labyrinthe avec ses coordonnéés
	public char getCase(int x, int y) {
		return this.laby[y][x];
	}

	// Modifie une case du labyrinthe à partir de ses coordonnées
	public void setCase(int x, int y, char c) {
		if ((laby[y][x] != this.CARDEPART) && (laby[y][x] != this.CARARRIVEE)&& (laby[y][x] != this.CARMUR))
			this.laby[y][x] = c;
	}

	// Récupère la case de départ
	public Case getDepart() {
		return this.depart;
	}

	// Récupère la case d'arrivée
	public Case getArrivee() {
		return this.arrivee;
	}

	// Teste si la case est vide
	public boolean estVide(Case c) {
		return this.laby[c.getY()][c.getX()] == this.CARVIDE;
	}

	// Retourne l'affichage du labyrinthe
	@Override
	public String toString() {
		String str = "";
   		for (int i = 0; i < this.laby.length; i++) {
			for (int j = 0; j < this.laby[0].length; j++) str += laby[i][j];
			str += "\n";
		}
		return str;
	}

	// Retourne l'affichage du labyrinthe pour console
	public String toStringConsole(boolean resoudre) {
		String str = "";
		str += titreToString();
		if (resoudre) str += resolution();
		str += toString();
		return str;
	}

	// Retourne l'affichage du labyrinthe pour fenêtre
	public String toStringWindow(boolean resoudre) {
		String str = "";
		if (resoudre) resolution();
		str += toString();
		return str;
	}

	// Retourne l'affichage du titre
	public String titreToString() {
		String str = "";
		str += "\n\n   ******************   \n";
		str += "   *   LABYRINTHE   *   \n";
		str += "   ******************   \n\n\n";
		return str;
	}

	// Retourne l'affichage de la légende
	public String legendeToString() {
		String str = "";
		str += "\n'" + this.CARDEPART + "' = Depart\t'" + this.CARARRIVEE + "' = Arrivee\t'" + this.CARCHEMIN + "' = Chemin parcouru\n";
		return str;
	}

	// Détermine si il existe une solution au labyrinthe
	public String resolution() {
        String str = "";
		Case position = new Case();
		try {
			position = resolutionLaby(this);
		}
		catch(LabyrintheException e) {
			str += e.getMessage() + "\n\n";
		}
		finally {
			if (position.equals(this.getArrivee())) {
				str += "Le chemin a ete trouve !\n";
				str += "Position au debut : " + this.getDepart().toString() + "\n";
				str += "Position a la fin : " + position.toString() + "\n\n";
			}
		}
		return str;
	}

	// Résolution du labyrinthe
	public Case resolutionLaby(Labyrinthe lab) throws LabyrintheException {
		// Variables et objets
		Stack <Case> pile = new Stack <Case> ();
		Case position;
		ArrayList <Case> caseVoisine;
		int x, y;
		int variationY, variationX;
		int nbChemin;
		boolean estProcheArrivee;
		// On se place sur le départ
		position = lab.getDepart();
		pile.push(position);
		// Regarde les actions possibles et les effectue
		while (!pile.isEmpty() && !position.equals(lab.getArrivee())) {
			x = position.getX();
			y = position.getY();
			variationX = lab.getArrivee().getX() - x;
			variationY = lab.getArrivee().getY() - y;
			nbChemin = 0;
			estProcheArrivee = false;
			int signeX = (int)(Math.signum(variationX));
			int signeY = (int)(Math.signum(variationY));
			if (signeX == 0) signeX = 1;
			if (signeY == 0) signeY = 1;
			caseVoisine = new ArrayList <Case> ();
			if (Math.abs(variationY) < Math.abs(variationX)) {
				caseVoisine.add(new Case(x + signeX, y));
				caseVoisine.add(new Case(x, y + signeY));
				caseVoisine.add(new Case(x, y - signeY));
				caseVoisine.add(new Case(x - signeX, y));
			}
			else {
				caseVoisine.add(new Case(x, y + signeY));
				caseVoisine.add(new Case(x + signeX, y));
				caseVoisine.add(new Case(x - signeX, y));
				caseVoisine.add(new Case(x, y - signeY));
			}
			for (int i = 0; i < caseVoisine.size(); i++) {
				if (caseVoisine.get(i).equals(lab.getArrivee()))
					estProcheArrivee = true;
				else if (lab.estVide(caseVoisine.get(i)))
					nbChemin++;
			}
			if (estProcheArrivee) {
				for (int i = 0; i < caseVoisine.size(); i++)
					if (caseVoisine.get(i).equals(lab.getArrivee())) {
						pile.push(position);
						lab.setCase(x, y, this.CARCHEMIN);
						position = (caseVoisine.get(i));
						break;
					}
			}
			else if (nbChemin > 0) {
				for (int i = 0; i < caseVoisine.size(); i++) {
					if (lab.estVide(caseVoisine.get(i))) {
						pile.push(position);
						lab.setCase(x, y, this.CARCHEMIN);
						position = (caseVoisine.get(i));
						break;
					}
				}
			}
			else if ((nbChemin == 0)&& !pile.isEmpty()) {
				lab.setCase(x, y, this.CARBLOQUE);
				position = (pile.pop());
			}
		}
		if (position.equals(getArrivee())) return position;
		else throw new LabyrintheException("Aucun chemin...");
	}

	// Exécution du programme
	public static void main(String[] args) {
		Labyrinthe lab = new Labyrinthe(70, 20, 25);
		System.out.println("\n" + lab.toStringConsole(true));
		new Fenetre(lab.toStringWindow(false), "Labyrinthe", lab.getLargeur(), lab.getHauteur());
	}

}
