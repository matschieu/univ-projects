package com.github.matschieu.tictactoe;

public class GridTerm implements Grid {

	private static final char VERTICAL_WALL = '|';
	private static final char HORIZONTAL_WALL = '-';
	private Pawn[][] grid;

	public GridTerm(int rows, int cols) {
		grid = new Pawn[cols][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[j][i] = Pawn.EMPTY_PAWN;
	}
	public GridTerm() { this(ROWS, COLS); }

	public int getCols() { return grid[0].length; }
	public int getRows() { return grid.length; }

	public void setCase(Coordinate c, Pawn p) throws InvalidCoordinateException {
		try {
			coordinateVerify(c);
			grid[c.getX()][c.getY()] = p;
		}
		catch(InvalidCoordinateException obe) { throw obe; }
	}

	public Pawn getPawn(Coordinate c) throws InvalidCoordinateException {
		try {
			coordinateVerify(c);
			return grid[c.getX()][c.getY()];
		}
		catch(InvalidCoordinateException obe) { throw obe; }
	}

	public boolean isEmpty(Coordinate c) throws InvalidCoordinateException {
		try {
			coordinateVerify(c);
			return grid[c.getX()][c.getY()] == Pawn.EMPTY_PAWN;
		}
		catch(InvalidCoordinateException obe) { throw obe; }
	}

	public void coordinateVerify(Coordinate c) throws InvalidCoordinateException {
		int x = c.getX();
		int y = c.getY();
		if (x > grid[0].length - 1 || x < 0 || y > grid.length - 1 || y < 0)
			throw new InvalidCoordinateException("Erreur : coordonnees incorrectes\n");
	}

	public String toString() {
		StringBuffer str = new StringBuffer("");
		int idx = 1;
		str.append(" ");
		for (int i = 0; i < grid.length * 2 + 1; i++)
			if (i %2 == 1) str.append(idx++);
			else str.append(" ");
		str.append("\n ");
		for (int i = 0; i < grid.length * 2 + 1; i++)
			str.append("" + HORIZONTAL_WALL);
		str.append("\n");
		idx = 1;
		for (int i = 0; i < grid.length; i++) {
			str.append(idx++);
			for (int j = 0; j < grid[0].length; j++)
				str.append("" + VERTICAL_WALL + grid[j][i].toString());
			str.append("" + VERTICAL_WALL + "\n ");
			for (int j = 0; j < grid.length * 2 + 1; j++)
				str.append("" + HORIZONTAL_WALL);
			str.append("\n");
		}
		return str.toString();
	}

}
