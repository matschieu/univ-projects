package com.github.matschieu.maths;

/**
*@author Matschieu
*/
public class Operations {

	/* Méthode qui trouve la solution de Ax=B dans le cas ou A est une matrice diagonale
	de taille n (n=réel) du type :
	    | a 0 0 |            | 0 0 a |
	A = | 0 b 0 |   ou   A = | 0 b 0 |
	    | 0 0 c |            | c 0 0 |
	La fonction retourne NULL si il n'y a aucune solution possible */
	public static Vector diagonalSolution(SquaredMatrix mat, Vector vect) {
		int size = mat.getSize();
		Vector res;
		if (!mat.isDiagonal() || size != vect.getSize()) return null;
		else res = new Vector(size);
		if (size == 1) res.set(0, vect.get(0) / mat.get(0, 0));
		else if (mat.get(0, size - 1) == 0) {
			for(int i = 0 ; i < size; i++) {
				double a = mat.get(i, i);
				double b = vect.get(i);
				double tmp1;
				if (a == 0  && b == 0) tmp1 = Matrix.INF;
				else if (a != 0) tmp1 = b / a;
				else return null;
				res.set(i, tmp1);
			}
		}
		else if (mat.get(0, 0) == 0) {
			for(int i = 0 ; i < size; i++) {
				double a = mat.get(i, size - 1 - i);
				double b = vect.get(i);
				double tmp1;
				if (a == 0  && b == 0) tmp1 = Matrix.INF;
				else if (a != 0) tmp1 = b / a;
				else return null;
				res.set(i, tmp1);
			}
		}
		else return null;
		return res;
	}

	/* Méthode qui trouve la solution de Ax=B dans le cas ou A est une matrice triangulaire
	supérieure ou inférieure de taille n (n=réel) du type :
	    | a b c |            | a 0 0 |
	A = | 0 d e |   ou   A = | b c 0 |
	    | 0 0 f |            | d e f |
	La fonction retourne NULL si il n'y a aucune solution possible
	Si il y a une infinité de solution, on prend 0 comme valeur arbitraire */
	public static Vector trigularSolution(SquaredMatrix mat, Vector vect) {
		int size = mat.getSize();
		Vector res;
		if (size != vect.getSize()) return null;
		else if (mat.isDiagonal()) return diagonalSolution(mat, vect);
		else res = new Vector(size);
		if (size == 1) res.set(0, vect.get(0) / mat.get(0, 0));
		else if (mat.isSupTriangular()) {
			for(int i = size - 1 ; i >= 0; i--) {
				double tmp = 0;
				for(int j = i + 1 ; j < size; j++) tmp += mat.get(i, j) * (res.get(j) == Matrix.INF ? 0 : res.get(j));
				if (mat.get(i, i) == 0) {
					if (vect.get(i) == tmp) res.set(i, Matrix.INF);
					else return null;
				}
				else	{
					double tmp1 = (vect.get(i) - tmp) / mat.get(i, i);
					res.set(i, tmp1);
				}
			}
		}
		else if (mat.isInfTriangular()) {
			for(int i = 0 ; i < size; i++) {
				double tmp = 0;
				for(int j = 0 ; j < i; j++) tmp += mat.get(i, j) * (res.get(j) == Matrix.INF ? 0 : res.get(j));
				if (mat.get(i, i) == 0) {
					if (vect.get(i) == tmp) res.set(i, Matrix.INF);
					else return null;
				}
				else	{
					double tmp1 = (vect.get(i) - tmp) / mat.get(i, i);
					res.set(i, tmp1);
				}
			}
		}
		else return null;
		return res;
	}

	/* Méthode qui applique la méthode de Gauss à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice triangulaire supérieure */
	public static Vector gauss(SquaredMatrix m, Vector v) {
		SquaredMatrix mat = m.copy();
		Vector vect = v.copy();
		int size = mat.getSize();
		for(int i = 0; i < size; i++) {
			double p = mat.get(i, i);
			if (p == 0 && i + 1 < size) {
				for(int n = i + 1; n < size; n++)
					if (mat.get(n, i) != 0) {
						mat.permutLine(i, i + 1);
						vect.permutLine(i, i+ 1);
						i--;
						break;
					}
			}
			else if (p != 0) {
				for(int j = i + 1; j < size; j++) {
					double c;
					c = mat.get(j, i) / p;
					if (c == 0 && j + 1 < size) {
						for(int n = j + 1; n < size; n++)
							if (mat.get(n, i) != 0) {
								mat.permutLine(j, j + 1);
								vect.permutLine(j, j+ 1);
								j--;
								break;
							}
					}
					else if (c != 0) {for(int k = i; k < size; k++) {
						double v1 = mat.get(j, k) - c * mat.get(i, k);
						mat.set(j, k, v1);
					}
					double v2 = vect.get(j) - c * vect.get(i);
					vect.set(j, v2);
					}
				}
			}
		}
		return trigularSolution(mat, vect);
	}

	/* Méthode qui applique la méthode de Jordan à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice diagonale */
	public static Vector jordan(SquaredMatrix m, Vector v) {
		SquaredMatrix mat = m.copy();
		Vector vect = v.copy();
		int size = mat.getSize();
		for(int i = 0; i < size; i++) {
			double p = mat.get(i, i);
			if (p == 0 && i + 1 < size) {
				for(int n = i + 1; n < size; n++)
					if (mat.get(n, i) != 0) {
						mat.permutLine(i, i + 1);
						vect.permutLine(i, i+ 1);
						i--;
						break;
					}
			}
			else if (p != 0) {
				for(int j = i + 1; j < size; j++) {
					double c;
					c = mat.get(j, i) / p;
					if (c == 0 && j + 1 < size) {
						for(int n = j + 1; n < size; n++)
							if (mat.get(n, i) != 0) {
								mat.permutLine(j, j + 1);
								vect.permutLine(j, j+ 1);
								j--;
								break;
							}
					}
					else if (c != 0) {for(int k = i; k < size; k++) {
						double v1 = mat.get(j, k) - c * mat.get(i, k);
						mat.set(j, k, v1);
					}
					double v2 = vect.get(j) - c * vect.get(i);
					vect.set(j, v2);
					}
				}
			}
		}
		for(int i = size - 1; i >= 0; i--) {
			double p = mat.get(i, i);
			double c;
			for(int j = i - 1; j >= 0; j--) {
				double v1;
				if (p != 0) c = mat.get(j, i) / p;
				else c = 0;
				for(int k = size - 1; k >= i; k--) {
					if (p !=0 ) v1 = mat.get(j, k) - c * mat.get(i, k);
					else v1 = mat.get(j, k) - mat.get(i - 1, k);
					mat.set(j, k, v1);
				}
				double v2 = vect.get(j) - c * vect.get(i);
				vect.set(j, v2);
			}
		}
		return diagonalSolution(mat, vect);
	}

	/* Méthode calcul l'erreur E du sytème A.x = B (E = A.x - B) */
	public static Vector error(SquaredMatrix mat, Vector vect, Vector res) {
		Vector err;
		int size = mat.getSize();
		if (size != vect.getSize() && size != res.getSize()) return null;
		else err = new Vector(size);
		for(int i = 0; i < size; i++) {
			double tmp = 0;
			for(int j = 0; j < size; j++) {
				double a = mat.get(i, j);
				double b = res.get(j);
				if (a == Matrix.INF) a = 0;
				if (b == Matrix.INF) b = 0;
				tmp += a * b;
			}
			tmp -= vect.get(i);
			err.set(i, tmp);
		}
		return err;
	}

}