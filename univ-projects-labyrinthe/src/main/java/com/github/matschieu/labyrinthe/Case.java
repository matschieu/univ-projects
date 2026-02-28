package com.github.matschieu.labyrinthe;

/**
 * @author  Matschieu
 * @version Mars 2007
 */
class Case {

	private int x;
	private int y;

	// Constructeur avec des coordonnées
	public Case(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Constructeur sans coordonnées
     public Case() {
		this.x = -1;
		this.y = -1;
     }

	// Retourne l'affichage des Coordonnée de la case
	@Override
	public String toString() {
          if ((this.x != -1) && (this.y != -1)) return "(" + this.x + "," + this.y +")";
          return "erreur";
	}

	// Retourne l'abscisse de la case
	public int getX() {
		return this.x;
	}

	// Retourne l'ordonnée de la case
	public int getY() {
		return this.y;
	}

	// Modifie les coordonnées de la case
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// Détermine si deux cases sont égales
	public boolean equals(Case c) {
		return (this.x == c.getX()) && (this.y == c.getY());
	}

}