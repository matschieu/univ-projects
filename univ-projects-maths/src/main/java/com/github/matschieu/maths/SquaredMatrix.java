package com.github.matschieu.maths;

import java.io.File;
import java.util.Scanner;

/**
*@author Matschieu
*/
public class SquaredMatrix extends Matrix {

	/* CONSTRUCTEURS */

	/* Initialise à 0 la matrice dont les dimensions sont passées en paramètre */
	public SquaredMatrix(int size) { super(size, size); }
	/* Initialise la matrice dont les dimensions sont passées en paramètre avec
	la formule Mat(i, j)=(a * i + b * j + c) / (d * i + e * j + f) */
	public SquaredMatrix(int x, int a, int b, int c, int d, int e, int f) { super(x, x, a, b, c, d, e, f); }
	/* Initialise la matrice dont les dimensions sont passées en paramètre de façon
	aléatoire en fonction du taux passé en paramètre */
	public SquaredMatrix(int x, int rate) { super(x, x, rate); }
	/* Initialise la matrice carrée A à partir des valeurs contenues dans un fichier dont le chemin est passé en paramètre
	Le fichier doit être de la forme :
	N
	A(1,1)	A(1,2)	...	A(1,N)		B(1)
	A(2,1)	A(2,2)	...	A(2,N)		B(2)
	...
	A(N,1)	A(N,2)	...	A(N,N)		B(N)
	où N est la taille de la matrice et B un vecteur */
	public SquaredMatrix(String file) throws FileNotFoundException, InputMismatchException {
		try {
			Scanner sc = new Scanner(new File(file));
			int x = sc.nextInt();
			mat = new double[x][x];
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

	/* Méthode qui teste si la matrice carrée est une matrice diagonale du type :
		| a 0 0 |            | 0 0 a |
	A =	| 0 b 0 |   ou   A = | 0 b 0 |
		| 0 0 c |            | c 0 0 |
	La méthode retourne true si la matrice est diagonale et false si elle ne l'est pas */
	public boolean isDiagonal() {
		int size = mat.length;
		int nbz = 0;
		int diag = 0;
		int diagz = 0;
		if (size == 1) return true;
		if (mat[0][size - 1] == 0) {
			for(int i = 0; i < mat.length; i++)
				for(int j = 0; j < mat[0].length; j++) {
					if(i == j && mat[i][j] != 0) diag++;
					else if(i == j && mat[i][j] == 0) diagz++;
					else if (mat[i][j] == 0) nbz++;
				}
			return (diag + diagz == size && nbz == size * size - size);
		}
		else if (mat[size - 1][0] == 0) {
			for(int i = 0; i < size; i++)
				for(int j = 0; j < size; j++) {
					if(i == size - 1 - j && mat[i][j] != 0) diag++;
					else if(i == size - 1 - j && mat[i][j] == 0) diagz++;
					else if (mat[i][j] == 0) nbz++;
				}
			return (diag + diagz == size && nbz == size * size - size);
		}
		return false;
	}

	/* Méthode qui teste si la matrice carrée est une matrice triangulaire supérieure ou
	inférieure du type :
		| a 0 0 |            | a b c |
	A =	| b c 0 |   ou   A = | 0 d e |
		| d e f |            | 0 0 f |
	La méthode retourne true si la matrice est diagonale et false si elle ne l'est pas */
	public boolean isSupTriangular() {
		int size = mat.length;
		int nbz = 0;
		int tri = 0;
		int triz = 0;
		if (size == 1) return true;
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++) {
				if(j >= i && mat[i][j] != 0) tri++;
				else if(j >= i && mat[i][j] == 0) triz++;
				else if (j < i && mat[i][j] == 0) nbz++;
			}
		return (nbz + tri + triz == size * size);
	}
	public boolean isInfTriangular() {
		int size = mat.length;
		int nbz = 0;
		int tri = 0;
		int triz = 0;
		if (size == 1) return true;
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++) {
				if(j <= i && mat[i][j] != 0) tri++;
				else if(j <= i && mat[i][j] == 0) triz++;
				else if (j > i && mat[i][j] == 0) nbz++;
			}
		return (nbz + tri + triz == size * size);
	}

	/* Méthode qui renvoient les dimensions de la matrice carrée */
	public int getSize() { return mat.length; }

	/* Méthode renvoyant une copie du vecteur */
	public SquaredMatrix copy() {
		SquaredMatrix vect = new SquaredMatrix(mat.length);
		for(int i = 0; i < mat.length; i++)
			for(int j = 0; j < mat[0].length; j++) vect.set(i, j, mat[i][j]);
		return vect;
	}

}