package com.github.matschieu.maths;

import java.io.File;
import java.util.Scanner;

/**
*@author Matschieu
*/
public class Matrix {

	/* ATTRIBUTS */

	public static final double INF = 40379050287.140339;
	protected String name = "Mat";
	protected double[][] mat;

	/* CONSTRUCTEURS */

	protected Matrix() { }
	/* Initialise à 0 la matrice dont les dimensions sont passées en paramètre */
	public Matrix(int x, int y) { mat = new double[y][x]; }
	/* Initialise la matrice dont les dimensions sont passées en paramètre avec
	la formule Mat(i, j)=(a * i + b * j + c) / (d * i + e * j + f) */
	public Matrix(int x, int y, int a, int b, int c, int d, int e, int f) {
		mat = new double[y][x];
		for (int i = 0; i < mat.length; i++)
			for(int j = 0; j < mat[0].length; j++) mat[i][j] = (double)(a * i + b * j + c) / (double)(d * i + e * j + f);
	}
	/* Initialise la matrice dont les dimensions sont passées en paramètre de façon
	aléatoire en fonction du taux passé en paramètre */
	public Matrix(int x, int y, int rate) {
		mat = new double[y][x];
		for (int i = 0; i < mat.length; i++)
			for(int j = 0; j < mat[0].length; j++)
				if ((int)(Math.random() * 101) <= rate) {
					int rdm = (int)(Math.random() * 100);
					mat[i][j] = rdm;
				}
				else mat[i][j] = 0;
	}
	/* Initialise la matrice A à partir des valeurs contenues dans un fichier dont le chemin est passé en paramètre
	Le fichier doit être de la forme :
	N P
	A(1,1)	A(1,2)	...	A(1,P)		B(1)
	A(2,1)	A(2,2)	...	A(2,P)		B(2)
	...
	A(N,1)	A(N,2)	...	A(N,P)		B(P)
	où N est le nombre de colonne de la matrice A et P le nombre de ligne de la matrice A et du vecteur B */
	public Matrix(String file) throws FileNotFoundException, InputMismatchException {
		try {
			Scanner sc = new Scanner(new File(file));
			int x = sc.nextInt();
			int y = sc.nextInt();
			mat = new double[y][x];
			for (int i = 0; i < mat.length; i++) {
				for(int j = 0; j < mat[0].length; j++) mat[i][j] = sc.nextDouble();
				sc.nextDouble();
			}
			sc.close();
		}
		catch(java.io.FileNotFoundException e) { throw new FileNotFoundException(file); }
		catch(java.util.InputMismatchException e) { throw new InputMismatchException(file); }
		catch(java.util.NoSuchElementException e) { throw new InputMismatchException(file); }
	}

	/* METHODES */

	/* Méthode qui vérifie si toutes les valeurs de la matrices sont nulles,
	renvoie true si la matrice est nulle et false si elle ne l'est pas */
	public boolean is_null() {
		int nbz = 0;
		for (int i = 0; i < mat.length; i++)
			for(int j = 0; j < mat[0].length; j++) if (mat[i][j] == 0) nbz++;
		return nbz == 0;
	}

	/* Méthodes qui permettent de manipuler les valeurs de la matrice (modifier/obtenir) */
	public double get(int y, int x) { return mat[y][x]; }
	public void set(int y, int x, double val) {
		try { mat[y][x] = val; }
		catch(IndexOutOfBoundsException e) { }
	}

	/* Méthodes qui renvoient les dimensions de la matrice */
	public int getXSize() { return mat[0].length; }
	public int getYSize() { return mat.length; }

	public void setName(String str) { name = str; }
	public String getName() { return name; }

	/* Méthode qui effectue la permutation de deux lignes */
	public void permutLine(int a, int b) {
		try {
			for(int i = 0; i < mat[0].length; i++) {
				double tmp = mat[a][i];
				mat[a][i] = mat[b][i];
				mat[b][i] = tmp;
			}
		}
		catch(IndexOutOfBoundsException e) { }
	}

	/* Méthode qui effectue la permutation de deux colonnes */
	public void permutColumn(int a, int b) {
		try {
			for(int i = 0; i < mat[0].length; i++) {
				double tmp = mat[i][a];
				mat[i][a] = mat[i][b];
				mat[i][b] = tmp;
			}
		}
		catch(IndexOutOfBoundsException e) { }
	}

	/* Méthode qui renvoie une chaine de caractères représentant la matrice */
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("");
		int cpt = 0;
		str.append("\n");
		for(int i = 0; i < mat.length; i++) {
			if (i == mat.length / 2) str.append("\t" + name + " =\t| ");
			else str.append("\t\t| ");
			for(int j = 0; j < mat[0].length; j++) {
				if (mat[i][j] == INF) {
					char c = (char)(97 + cpt++);
					str.append(c + "\t");
				}
				else str.append(mat[i][j] + "\t");
			}
			str.append("|\n");
		}
		if (cpt != 0) {
			str.append("\tou ");
			for(int i = 0; i < cpt; i++) {
				char c = (char)(97 + i);
				str.append(c + (i == cpt - 1 ? " " : ", "));
			}
			str.append(cpt == 1 ? "appartient a |R\n" : "appartiennent a |R\n");
			str.append("\t(pour la resolution, on a pris ");
			for(int i = 0; i < cpt; i++) {
				char c = (char)(97 + i);
				str.append(c + " = ");
			}
			str.append("0)");
		}
		str.append("\n");
		return str.toString();
	}

}