package com.github.matschieu.maths;

import java.util.Stack;

/**
*@author Matschieu
*/
public class LinearSystem {

	private SquaredMatrix mat;
	private Vector vect;
	private Vector res;

	public LinearSystem() { this(null, null); }
	public LinearSystem(SquaredMatrix m, Vector v) {
		mat = m;
		vect = v;
		res = null;
	}

	public int getSize() { return mat.getSize(); }

	public boolean isNull() { return mat == null && vect == null; }
	public boolean resIsNull() { return res == null; }

	public void setMatrix(SquaredMatrix m) { mat = m; }
	public void setVector(Vector v) { vect = v; }
	public void set(SquaredMatrix m, Vector v) {
		mat = m;
		vect = v;
	}

	public SquaredMatrix getMatrix() { return mat; }
	public Vector getVector() { return vect; }
	public Vector getRes() { return res; }

	public void readInFile(String filePath) throws InvalidSizeException, NullPointerException, FileNotFoundException, InputMismatchException{
		try {
			IOFile file = new IOFile();
			file.open(filePath);
			mat = new SquaredMatrix(filePath);
			vect = new Vector(filePath);
			if (mat.getSize() > Project.MATRIX_MAX_SIZE) {
				mat = null;
				vect = null;
				throw new InvalidSizeException();
			}
		}
		catch(NullPointerException e) { throw e; }
		catch(FileNotFoundException e) { throw new FileNotFoundException(filePath); }
		catch(InputMismatchException e) { throw new InputMismatchException(filePath); }
	}

	public void saveInFile(String filePath) throws NoSystemException, FileNotFoundException, WriteFileException {
		if (isNull()) throw new NoSystemException();
		try {
			IOFile file = new IOFile();
			if (!file.exists(filePath)) file.create(filePath);
			file.open(filePath);
			StringBuffer strbuf = new StringBuffer("");
			strbuf.append(mat.getSize() + "\n");
			for(int i = 0; i < mat.getSize(); i++) {
				for(int j = 0; j < mat.getSize(); j++) strbuf.append(mat.get(j, i) + "\t");
				strbuf.append("\t" + vect.get(i) + "\n");
			}
			file.write((strbuf.toString()).replace('.', ','));
		}
		catch(FileNotFoundException e) { throw new FileNotFoundException(filePath); }
		catch(WriteFileException e) { throw new WriteFileException(filePath); }
	}

	/* Méthode qui trouve la solution de Ax=B dans le cas ou A est une matrice diagonale
	de taille n (n=réel) du type :
	    | a 0 0 |            | 0 0 a |
	A = | 0 b 0 |   ou   A = | 0 b 0 |
	    | 0 0 c |            | c 0 0 |
	La fonction retourne NULL si il n'y a aucune solution possible */
	private void diagonal(SquaredMatrix m, Vector v) {
		int size = m.getSize();
		if (!m.isDiagonal() || size != v.getSize()) res = null;
		else res = new Vector(size);
		if (size == 1 && res != null)
			if (m.get(0, 0) != 0) res.set(0, v.get(0) / m.get(0, 0));
			else if (v.get(0) == 0) res.set(0, Matrix.INF);
			else res = null;
		else if (m.get(0, size - 1) == 0 && res != null) {
			for(int i = 0 ; i < size; i++) {
				double a = m.get(i, i);
				double b = v.get(i);
				double tmp1;
				if (a == 0  && b == 0) tmp1 = Matrix.INF;
				else if (a != 0) tmp1 = b / a;
				else {
					res = null;
					break;
				}
				res.set(i, tmp1);
			}
		}
		else res = null;
	}

	/* Méthode qui trouve la solution de Ax=B dans le cas ou A est une matrice triangulaire
	supérieure ou inférieure de taille n (n=réel) du type :
	    | a b c |            | a 0 0 |
	A = | 0 d e |   ou   A = | b c 0 |
	    | 0 0 f |            | d e f |
	La fonction retourne NULL si il n'y a aucune solution possible
	Si il y a une infinitée de solution, on prend 0 comme valeur arbitraire */
	private void triangular(SquaredMatrix m, Vector v) {
		int size = m.getSize();
		if (size != v.getSize()) res = null;
		else res = new Vector(size);
		if (size == 1 && res != null)
			if (m.get(0, 0) != 0) res.set(0, v.get(0) / m.get(0, 0));
			else if (v.get(0) == 0) res.set(0, Matrix.INF);
			else res = null;
		else if (m.isSupTriangular() && res != null) {
			for(int i = size - 1 ; i >= 0; i--) {
				double tmp = 0;
				for(int j = i + 1 ; j < size; j++) tmp += m.get(i, j) * (res.get(j) == Matrix.INF ? 0 : res.get(j));
				if (m.get(i, i) == 0) {
					if (v.get(i) == tmp) res.set(i, Matrix.INF);
					else {
						res = null;
						break;
					}
				}
				else {
					double tmp1 = (v.get(i) - tmp) / m.get(i, i);
					res.set(i, tmp1);
				}
			}
		}
		else res = null;
	}

	/* Méthode qui applique la méthode de Gauss à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice triangulaire supérieure.
	Si le pivot est nul, on recherche un nombre non nul dans la colonne et on échange les lignes */
	public long gaussA() {
		long startTime = System.nanoTime();;
		SquaredMatrix m = mat.copy();
		Vector v = vect.copy();
		int size = m.getSize();
		for(int i = 0; i < size; i++) {
			double p = m.get(i, i);
			if (p == 0)
				for(int j = i + 1; j < size; j++)
					if (m.get(j, i) != 0) {
						m.permutLine(i, j);
						v.permutLine(i, j);
						p = m.get(i, i);
						break;
					}
			for(int j = i + 1; j < size; j++) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
		}
		triangular(m, v);
		return System.nanoTime() - startTime;
	}

	/* Méthode qui applique la méthode de Gauss à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice triangulaire supérieure.
	Recherche le plus grand nombre de la colonne pour le prendre comme pivot */
	public long gaussB() {
		long startTime = System.nanoTime();;
		SquaredMatrix m = mat.copy();
		Vector v = vect.copy();
		int size = m.getSize();
		for(int i = 0; i < size; i++) {
			double p = m.get(i, i);
			double max = p;
			int idxMax = i;
			for(int j = i + 1; j < size; j++)
				if (m.get(j, i) > max) {
					max = m.get(j, i);
					idxMax = j;
				}
			if (idxMax != i) {
				m.permutLine(i, idxMax);
				v.permutLine(i, idxMax);
				p = m.get(i, i);
			}
			for(int j = i + 1; j < size; j++) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
		}
		triangular(m, v);
		return System.nanoTime() - startTime;
	}

	/* Méthode qui applique la méthode de Gauss à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice triangulaire supérieure.
	Recherche le plus grand nombre de la sous matrice pour le prendre comme pivot */
	public long gaussC() {
		Stack<int[]> stack = new Stack<int[]>();
		long startTime = System.nanoTime();;
		SquaredMatrix m = mat.copy();
		Vector v = vect.copy();
		int size = m.getSize();
		for(int i = 0; i < size; i++) {
			double p = m.get(i, i);
			double max = p;
			int idxXMax = i;
			int idxYMax = i;
			for(int j = i; j < size; j++)
				for(int k = i; k < size; k++)
					if (m.get(j, k) > max) {
						max = m.get(j, k);
						idxXMax = k;
						idxYMax = j;
					}
			if (i != idxXMax) {
				m.permutColumn(i, idxXMax);
				int[] t = new int[2];
				t[0] = i;
				t[1] = idxXMax;
				stack.push(t);
			}
			if (i != idxYMax) {
				m.permutLine(i, idxYMax);
				v.permutLine(i, idxYMax);
			}
			p = m.get(i, i);
			for(int j = i + 1; j < size; j++) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
		}
		confirmTriangular(m, v);
		triangular(m, v);
		if (res != null)
			while(!stack.empty()) {
				int[] t = stack.pop();
				res.permutLine(t[0], t[1]);
			}
		return System.nanoTime() - startTime;
	}

	/* Méthode qui applique la méthode de Jordan à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice diagonale
	Si le pivot est nul, on recherche un nombre non nul dans la colonne et on échange les lignes */
	public long jordanA() {
		long startTime = System.nanoTime();;
		SquaredMatrix m = mat.copy();
		Vector v = vect.copy();
		int size = m.getSize();
		for(int i = 0; i < size; i++) {
			double p = m.get(i, i);
			if (p == 0)
				for(int j = i + 1; j < size; j++)
					if (m.get(j, i) != 0) {
						m.permutLine(i, j);
						v.permutLine(i, j);
						p = m.get(i, i);
						break;
					}
			if (p == 0)
				for(int j = i - 1; j >= 0; j--)
					if (m.get(j, i) != 0) {
						m.permutLine(i, j);
						v.permutLine(i, j);
						p = m.get(i, i);
						break;
					}
			for(int j = i + 1; j < size; j++) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
			for(int j = i - 1; j >= 0; j--) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
		}
		diagonal(m, v);
		return System.nanoTime() - startTime;
	}

	/* Méthode qui applique la méthode de Jordan à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice diagonale
	Recherche le plus grand nombre de la colonne pour le prendre comme pivot */
	public long jordanB() {
		long startTime = System.nanoTime();;
		SquaredMatrix m = mat.copy();
		Vector v = vect.copy();
		int size = m.getSize();
		for(int i = 0; i < size; i++) {
			double p = m.get(i, i);
			double max = p;
			int idxMax = i;
			for(int j = i + 1; j < size; j++)
				if (m.get(j, i) > max) {
					max = m.get(j, i);
					idxMax = j;
				}
			if (idxMax != i) {
				m.permutLine(i, idxMax);
				v.permutLine(i, idxMax);
				p = m.get(i, i);
			}
			for(int j = i + 1; j < size; j++) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
			for(int j = i - 1; j >= 0; j--) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
		}
		diagonal(m, v);
		return System.nanoTime() - startTime;
	}

	/* Méthode qui applique la méthode de Jordan à une matrice carrée A et un vecteur B
	pour pouvoir appliquer la résolution du système A.x = B avec la méthode de résolution
	du système avec une matrice diagonale
	Recherche le plus grand nombre de la sous matrice pour le prendre comme pivot */
	public long jordanC() {
		Stack<int[]> stack = new Stack<int[]>();
		long startTime = System.nanoTime();;
		SquaredMatrix m = mat.copy();
		Vector v = vect.copy();
		int size = m.getSize();
		for(int i = 0; i < size; i++) {
			double p = m.get(i, i);
			double max = p;
			int idxXMax = i;
			int idxYMax = i;
			for(int j = i; j < size; j++)
				for(int k = i; k < size; k++)
					if (m.get(j, k) > max) {
						max = m.get(j, k);
						idxXMax = k;
						idxYMax = j;
					}
			if (i != idxXMax) {
				m.permutColumn(i, idxXMax);
				int[] t = new int[2];
				t[0] = i;
				t[1] = idxXMax;
				stack.push(t);
			}
			if (i != idxYMax) {
				m.permutLine(i, idxYMax);
				v.permutLine(i, idxYMax);
			}
			p = m.get(i, i);
			for(int j = i + 1; j < size; j++) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
			for(int j = i - 1; j >= 0; j--) {
				double c;
				if (p != 0) c = m.get(j, i) / p;
				else break;
				for(int k = i; k < size; k++) {
					double v1 = m.get(j, k) - c * m.get(i, k);
					m.set(j, k, v1);
				}
				double v2 = v.get(j) - c * v.get(i);
				v.set(j, v2);
			}
		}
		confirmDiagonal(m, v);
		diagonal(m, v);
		if (res != null)
			while(!stack.empty()) {
				int[] t = stack.pop();
				res.permutLine(t[0], t[1]);
			}
		return System.nanoTime() - startTime;
	}

	/* Méthode qui remplace les erreurs d'imprécision en 0 afin de bien avoir une matrice
	diagonale */
	private void confirmDiagonal(SquaredMatrix m, Vector v) {
		for(int i = 0; i < m.getSize(); i++)
			for(int j = 0; j < m.getSize(); j++) if (j != i) m.set(i, j, 0);
	}

	/* Méthode qui remplace les erreurs d'imprécision en 0 afin de bien avoir une matrice
	triangulaire */
	private void confirmTriangular(SquaredMatrix m, Vector v) {
		for(int i = 0; i < m.getSize(); i++)
			for(int j = 0; j < i; j++) m.set(i, j, 0);
	}

	/* Méthode calcul l'erreur E du sytème A.x = B (E = A.x - B) */
	public double error() {
		Vector err = new Vector(mat.getSize());
		for(int i = 0; i < mat.getSize(); i++) {
			double tmp = 0;
			for(int j = 0; j < mat.getSize(); j++) {
				double a = mat.get(i, j);
				double b = res.get(j);
				if (a == Matrix.INF) a = 0;
				if (b == Matrix.INF) b = 0;
				tmp += a * b;
			}
			tmp -= vect.get(i);
			err.set(i, tmp);
		}
		double max = err.getMaxValue();
		if (max != 0) return max;
		return err.getMinValue();
	}

}