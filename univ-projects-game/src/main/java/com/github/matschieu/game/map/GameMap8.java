
package com.github.matschieu.game.map;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *@author Matschieu
 */
public class GameMap8 extends GameMap4 {

	/**
	 * Constructs a new squared map
	 */
	public GameMap8() {
		this(DEFAULT_SIZE, DEFAULT_SIZE);
	}

	/**
	 * Constructs a new squared map
	 * @param width : the width of the map
	 * @param height : the height of the map
	 */
	public GameMap8(int width, int height) {
		super(width, height);
	}

	/**
	 * Builds a new map (default = 10 characters)
	 */
	@Override
	public void build() {
		this.build(10);
	}

	/**
	 * Builds a new map
	 * @param nbChar : the number of characters on the map (one of them is the hero)
	 */
	@Override
	public void build(int nbChar) {
		this.buildMap();
		this.buildChars(nbChar);
		this.buildItems();
	}

	/**
	 * Builds the map
	 */
	@Override
	protected void buildMap() {
		final String[] classFiles = (new File(Thread.currentThread().getContextClassLoader().getResource("com/github/matschieu/game/map").getFile())).list();
		final String classPackage = "com.github.matschieu.game.map";
		final String ext = ".class";
		final Cell8[][] array = new Cell8[this.height][this.width];
		final List<Class<?>> classCells = new LinkedList<>();
		Class<?> typeClass = null;
		try {
			typeClass = Class.forName(classPackage + ".CellType");
		}
		catch(final ClassNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		for(int i = 0; i < classFiles.length; i++) {
			try {
				if (classFiles[i].matches("[a-zA-Z0-9]*" + ext)) {
					classFiles[i] = classFiles[i].substring(0, classFiles[i].length() - ext.length());
					final Class<?> cls = Class.forName(classPackage + "." + classFiles[i]);
					if (typeClass.isAssignableFrom(cls) && cls != typeClass) classCells.add(cls);
				}
			}
			catch(final ClassNotFoundException e) { }
		}
		if (classCells.size() == 0) System.exit(1);
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				try {
					final int r = (int)(Math.random() * classCells.size());
					array[i][j] = new Cell8((CellType) classCells.get(r).newInstance());
				}
				catch(final Exception e) {
					array[i][j] = new Cell8(new Field());
				}
			}
		}
		this.cells = this.buildCellsNeighbours(array);
	}

	/**
	 * Add neighbours for each cells
	 * @param array : an array that represents the squared map
	 * @return List<Cell<? extends Direction>> : the list of complete cells (the map)
	 */
	protected List<Cell<? extends Direction>> buildCellsNeighbours(Cell8[][] array) {
		final List<Cell<? extends Direction>> list = new LinkedList<>();
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				int x, y;
				x = j;
				y = i - 1 < 0 ? this.height - 1 : i - 1;
				array[i][j].addNeighbour(array[y][x], Direction8.NORTH);
				y = (i + 1) % this.height;
				array[i][j].addNeighbour(array[y][x], Direction8.SOUTH);
				y = i;
				x = j - 1 < 0 ? this.width - 1 : j - 1;
				array[i][j].addNeighbour(array[y][x], Direction8.WEST);
				x = (j + 1) % this.width;
				array[i][j].addNeighbour(array[y][x], Direction8.EAST);
				x = j - 1 < 0 ? this.width - 1 : j - 1;
				y = i - 1 < 0 ? this.height - 1 : i - 1;
				array[i][j].addNeighbour(array[y][x], Direction8.NORTH_WEST);
				x = (j + 1) % this.width;
				y = i - 1 < 0 ? this.height - 1 : i - 1;
				array[i][j].addNeighbour(array[y][x], Direction8.NORTH_EAST);
				x = j - 1 < 0 ? this.width - 1 : j - 1;
				y = (i + 1) % this.height;
				array[i][j].addNeighbour(array[y][x], Direction8.SOUTH_WEST);
				x = (j + 1) % this.width;
				y = (i + 1) % this.height;
				array[i][j].addNeighbour(array[y][x], Direction8.SOUTH_EAST);
				list.add(array[i][j]);
			}
		}
		return list;
	}

	/**
	 * Builds a new map
	 * @param cells : parameters about cells<br />
	 *		cells[x][0]=class name<br />
	 *		cells[x][1]=rate
	 * @param nbChars : the number of chars on the map (without the hero)
	 * @param chars : parameters about characters, chars[0] defines the hero and chars[x>0] defines the others characters<br />
	 *		chars[0][0]=class name<br />
	 *		chars[0][1]=hero's name<br />
	 *		chars[0][2]=hero's energy<br />
	 *		chars[0][3]=hero's strength<br />
	 *		chars[0][4]=hero's strategy<br />
	 *		chars[0][x>4]=hero's actions<br />
	 *		chars[x>0][0]=class name<br />
	 *		chars[x>0][1]=rate<br />
	 *		chars[x>0][2]=char's energy<br />
	 *		chars[x>0][3]=char's strength<br />
	 *		chars[x>0][4]=char's strategy<br />
	 *		chars[x>0][y>4]=char's actions
	 * @param nbItems : the number of items on the map/characters
	 * @param items : parameters about items<br />
	 *		items[x][0]=class name<br />
	 *		cells[x][1]=rate
	 */
	@Override
	public void build(String[][] cells, int nbChars, String[][] chars, int nbItems, String[][] items) {
		this.buildMap(cells);
		this.buildChars(nbChars, chars);
		this.buildItems(nbItems, items);
	}

	/**
	 * Build the map list
	 * @param cells : cells parameters
	 */
	@Override
	protected void buildMap(String[][] cells) {
		if (cells == null) System.exit(1);
		final Cell8[][] array = new Cell8[this.height][this.width];
		final List<Cell8> list = new LinkedList<>();
		int totalCells = 0;
		for(int i = 0; i < cells.length; i++) {
			int tx = 0;
			Class<?> cls = null;
			try {
				cls = Class.forName(cells[i][0]);
				tx = this.width * this.height * Integer.parseInt(cells[i][1]) / 100;
			}
			catch(final ClassNotFoundException e) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			catch(final NumberFormatException e) { }
			for(int j = 0; j < tx; j++) {
				try {
					list.add(new Cell8((CellType)cls.newInstance()));
					totalCells++;
				}
				catch(final Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		if (list.size() < totalCells) {
			System.err.println("");
			System.exit(1);
		}
		Collections.shuffle(list);
		final Iterator<Cell8> it = list.iterator();
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				if (it.hasNext()) array[i][j] = it.next();
				if (array[i][j] == null) array[i][j] = new Cell8(new Field());
			}
		}
		this.cells = this.buildCellsNeighbours(array);
	}

}
