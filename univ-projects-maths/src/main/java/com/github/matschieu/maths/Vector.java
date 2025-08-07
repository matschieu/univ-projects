package com.github.matschieu.maths;

import java.io.File;
import java.util.Scanner;

/**
*@author Matschieu
*/
public class Vector extends Matrix {

	/* CONSTRUCTEURS */

	/* Initialise à 0 un vecteur dont les dimensions sont passées en paramètre */
	public Vector(int size) {
		super(1, size);
		setName("Vect");
	}
	/* Initialise le vecteur dont les dimensions sont passées en paramètre avec
	la formule Vect(i)=(a * i + b) / (c * i + d) */
	public Vector(int size, int a, int b, int c, int d) {
		super(1, size);
		setName("Vect");
		for (int i = 0; i < mat.length; i++) mat[i][0] = (double)(a * i + b) / (double)(c * i + d);
	}
	/* Initialise le vecteur dont les dimensions sont passées en paramètre de façon
	aléatoire en fonction du taux passé en paramètre */
	public Vector(int size, int rate) {
		super(1, size, rate);
		setName("Vect");
	}
	/* Initialise le vecteur B à partir des valeurs contenues dans un fichier dont le chemin est passé en paramètre
	Le fichier doit être de la forme :
	N
	A(1,1)	A(1,2)	...	A(1,N)		B(1)
	A(2,1)	A(2,2)	...	A(2,N)		B(2)
	...
	A(N,1)	A(N,2)	...	A(N,N)		B(N)
	où N est la taille du vecteur et A une matrice */
	public Vector(String file) throws FileNotFoundException, InputMismatchException {
		try {
			Scanner sc = new Scanner(new File(file));
			int y = sc.nextInt();
			mat = new double[y][1];
			setName("Vect");
			for (int i = 0; i < mat.length; i++) {
				for(int j = 0; j < mat.length; j++) sc.nextDouble();
				mat[i][0] = sc.nextDouble();
			}
			sc.close();
		}
		catch(java.io.FileNotFoundException e) { throw new FileNotFoundException(file); }
		catch(java.util.InputMismatchException e) { throw new InputMismatchException(file); }
		catch(java.util.NoSuchElementException e) { throw new InputMismatchException(file); }
	}

	/* METHODES */

	/* Méthode qui renvoient les dimensions du vecteur */
	public int getSize() { return mat.length; }

	/* Méthodes qui permettent de manipuler les valeurs du vecteur (modifier/obtenir) */
	public double get(int y) { return mat[y][0]; }
	public void set(int y, double val) { mat[y][0] = val; }

	/* Méthode renvoyant une copie du vecteur */
	public Vector copy() {
		Vector vect = new Vector(mat.length);
		for(int i = 0; i < mat.length; i++) vect.set(i, 0, mat[i][0]);
		return vect;
	}

	/* Méthode qui renvoit la plus grande valeur du vecteur */
	public double getMaxValue() {
		double max = -9999999;
		for(int i = 0; i < mat.length; i++) if (mat[i][0] > max) max = mat[i][0];
		return max;
	}

	/* Méthode qui renvoit la plus petite valeur du vecteur */
	public double getMinValue() {
		double min = 9999999;
		for(int i = 0; i < mat.length; i++) if (mat[i][0] < min) min = mat[i][0];
		return min;
	}

}