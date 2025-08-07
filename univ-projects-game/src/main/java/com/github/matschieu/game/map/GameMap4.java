
package com.github.matschieu.game.map;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.characters.Hero;
import com.github.matschieu.game.characters.Monster;
import com.github.matschieu.game.items.FullBagException;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.items.Potion;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class GameMap4 extends GameMap {

	/** The default size (height and width) of a squared map */
	public static final int DEFAULT_SIZE = 15;

	/** The width of the map */
	protected int width;
	/** The height of the map */
	protected int height;

	/**
	 * Constructs a new squared map
	 */
	public GameMap4() {
		this(DEFAULT_SIZE, DEFAULT_SIZE);
	}

	/**
	 * Constructs a new squared map
	 * @param width : the width of the map
	 * @param height : the height of the map
	 */
	public GameMap4(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Initializes the map with some parameters<br />
	 * It must be 2 number parameters to give a custom width/height, else this method does nothing
	 * @param args : parameters to initialize the map<br />
	 *		args[0]=width
	 *		args[1]=height
	 *		args[x>1]=ignored
	 */
	@Override
	public void init(String[] args) {
		if (args.length == 0) return;
		try {
			final int w = Integer.parseInt(args[0]);
			final int h = Integer.parseInt(args[1]);
			this.width = w;
			this.height = h;
		}
		catch(final NumberFormatException e) { }
	}

	/**
	 * Returns the width of the map
	 * @return int : the width of the map
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns the height of the map
	 * @return int : the height of the map
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Updates the width of the map
	 * @param width : the width of the map
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Updates the height of the map
	 * @param height : the height of the map
	 */
	public void setHeight(int height) {
		this.height = height;
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
	public void build(int nbChar) {
		this.buildMap();
		this.buildChars(nbChar);
		this.buildItems();
	}

	/**
	 * Builds the map
	 */
	protected void buildMap() {
		final String[] classFiles = (new File(Thread.currentThread().getContextClassLoader().getResource("com/github/matschieu/game/map").getFile())).list();
		final String classPackage = "com.github.matschieu.game.map";
		final String ext = ".class";
		final Cell4[][] array = new Cell4[this.height][this.width];
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
					array[i][j] = new Cell4((CellType) classCells.get(r).newInstance());
				}
				catch(final Exception e) {
					array[i][j] = new Cell4(new Field());
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
	protected List<Cell<? extends Direction>> buildCellsNeighbours(Cell4[][] array) {
		final List<Cell<? extends Direction>> list = new LinkedList<>();
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				int x, y;
				x = j;
				y = i - 1 < 0 ? this.height - 1 : i - 1;
				array[i][j].addNeighbour(array[y][x], Direction4.NORTH);
				y = (i + 1) % this.height;
				array[i][j].addNeighbour(array[y][x], Direction4.SOUTH);
				y = i;
				x = j - 1 < 0 ? this.width - 1 : j - 1;
				array[i][j].addNeighbour(array[y][x], Direction4.WEST);
				x = (j + 1) % this.width;
				array[i][j].addNeighbour(array[y][x], Direction4.EAST);
				list.add(array[i][j]);
			}
		}
		return list;
	}

	/**
	 * Builds the characters list
	 * @param nbChar : the number of characters on the map
	 */
	protected void buildChars(int nbChar) {
		final String[] classFiles = (new File(Thread.currentThread().getContextClassLoader().getResource("com/github/matschieu/game/characters").getFile())).list();
		final String classPackage = "com.github.matschieu.game.characters";
		final String ext = ".class";
		final List<GameCharacter> list = new LinkedList<>();
		final GameCharacter heroChar = new Hero();
		final List<Class<?>> classList = new LinkedList<>();
		Class<?> charClass = null;
		final Class<?> heroClass = heroChar.getClass();
		try {
			charClass = Class.forName(classPackage + ".GameCharacter");
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
					if (charClass.isAssignableFrom(cls) && cls != charClass && cls != heroClass) classList.add(cls);
				}
			}
			catch(final ClassNotFoundException e) { }
		}
		if (classList.size() == 0) System.exit(1);
		for(int i = 0; i < nbChar; i++) {
			GameCharacter gc;
			Cell<? extends Direction> c = this.cells.get((int)(Math.random() * cells.size()));
			while(!c.isReachable()) c = cells.get((int)(Math.random() * cells.size()));
			if (i == nbChar - 1) {
				System.out.print(Localization.SINGLETON.getElement("GAMESQUAREDMAP_BUILD") + " : ");
				System.out.flush();
				final Scanner sc = new Scanner(System.in);
				String heroName = null;
				if (sc.hasNextLine()) heroName = sc.nextLine();
				System.out.println();
				heroChar.setName(heroName);
				gc = heroChar;
			}
			else {
				try {
					final int r = (int)(Math.random() * classList.size());
					gc = (GameCharacter)classList.get(r).newInstance();
				}
				catch(final Exception e) {
					gc = new Monster();
				}
			}
			gc.setCell(c);
			gc.setGameMap(this);
			c.getCharacters().add(gc);
			list.add(gc);
		}
		Collections.shuffle(list);
		this.characters = list;
		this.hero = heroChar;
	}

	/**
	 * Builds the items list
	 */
	protected void buildItems() {
		final String[] classFiles = (new File(Thread.currentThread().getContextClassLoader().getResource("com/github/matschieu/game/items").getFile())).list();
		final String classPackage = "com.github.matschieu.game.items";
		final String ext = ".class";
		final List<Class<?>> classList = new LinkedList<>();
		Class<?> itemClass = null;
		try {
			itemClass = Class.forName(classPackage + ".Item");
		}
		catch(final ClassNotFoundException e) {
			return;
		}
		for(int i = 0; i < classFiles.length; i++) {
			try {
				if (classFiles[i].matches("[a-zA-Z0-9]*" + ext)) {
					classFiles[i] = classFiles[i].substring(0, classFiles[i].length() - ext.length());
					final Class<?> cls = Class.forName(classPackage + "." + classFiles[i]);
					if (itemClass.isAssignableFrom(cls) && cls != itemClass) classList.add(cls);
				}
			}
			catch(final ClassNotFoundException e) { }
		}
		final int nItem = (int)(Math.random() * (this.width * this.height));
		for(int i = 0; i < nItem + (nItem * (int)(Math.random() * 5)) ; i++) {
			Cell<? extends Direction> c = this.cells.get((int)(Math.random() * cells.size()));
			while(!c.isReachable()) c = cells.get((int)(Math.random() * cells.size()));
			final List<GameCharacter> chars = c.getCharacters();
			Item item;
			try {
				final int r = (int)(Math.random() * classList.size());
				item = (Item)classList.get(r).newInstance();
			}
			catch(final Exception e) {
				item = new Potion();
			}
			if (chars.size() > 0 && (int)(Math.random() * 2) == 0) {
				try {
					chars.get((int)(Math.random() * chars.size())).getBag().add(item);
				}
				catch(final FullBagException e) {
					c.getItems().add(item);
				}
			}
			else c.getItems().add(item);
		}
	}

	/**
	 * Returns a string view of the map
	 * @return String : a string view of the map
	 */
	@Override
	public String getStringMap() {
		final StringBuffer strBuf = new StringBuffer();
		final Iterator<Cell<? extends Direction>> it1 = this.cells.iterator();
		int idx = 0;
		strBuf.append("|");
		for(int i = 0; i < this.width + (this.width - 1) * 3 + 2; i++)
			strBuf.append("-");
		strBuf.append("|\n");
		while(it1.hasNext()) {
			final Cell<? extends Direction> c = it1.next();
			final List<GameCharacter> chars = c.getCharacters();
			strBuf.append("| ");
			if (chars.isEmpty())
				strBuf.append(c.getSymbole());
			else {
				final Iterator<GameCharacter> it2 = chars.iterator();
				GameCharacter gc = null;
				boolean hasHero = false;
				while(it2.hasNext()) {
					gc = it2.next();
					if (gc instanceof Hero) {
						hasHero = true;
						break;
					}
				}
				if (hasHero) strBuf.append((gc.getSymbole()));
				else strBuf.append(chars.get(0).getSymbole());
			}
			strBuf.append(" ");
			if (++idx % this.width == 0) {
				strBuf.append("|\n|");
				for(int i = 0; i < this.width + (this.width - 1) * 3 + 2; i++)
					strBuf.append("-");
				strBuf.append("|\n");
			}

		}
		strBuf.append(Localization.SINGLETON.getElement("GAMESQUAREDMAP_GETSTRINGMAP") + "\n");
		return strBuf.toString();
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
	protected void buildMap(String[][] cells) {
		if (cells == null) System.exit(1);
		final Cell4[][] array = new Cell4[this.height][this.width];
		final List<Cell4> list = new LinkedList<>();
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
					list.add(new Cell4((CellType)cls.newInstance()));
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
		final Iterator<Cell4> it = list.iterator();
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				if (it.hasNext()) array[i][j] = it.next();
				if (array[i][j] == null) array[i][j] = new Cell4(new Field());
			}
		}
		this.cells = this.buildCellsNeighbours(array);
	}

	/**
	 * Build the characters list
	 * @param nbChars : the number of characters on the map (without the hero)
	 * @param chars : chars parameters
	 */
	protected void buildChars(int nbChars, String[][] chars) {
		if (chars == null) return;
		this.characters = new LinkedList<>();
		Cell<? extends Direction> cTmp;
		try {
			this.hero = (GameCharacter)Class.forName(chars[0][0]).newInstance();
			this.hero.init(chars[0]);
		}
		catch(final Exception e) {
			this.hero = new Hero(chars[0][1]);
		}
		cTmp = this.cells.get((int)(Math.random() * this.cells.size()));
		while(!cTmp.isReachable()) cTmp = this.cells.get((int)(Math.random() * this.cells.size()));
		this.hero.setCell(cTmp);
		this.hero.setGameMap(this);
		cTmp.getCharacters().add(this.hero);
		this.characters.add(this.hero);
		for(int i = 1; i < chars.length; i++) {
			try {
				final Class<?> cls = Class.forName(chars[i][0]);
				final int n = nbChars * Integer.parseInt(chars[i][1]) / 100;
				for(int j = 0; j < n; j++) {
					final GameCharacter gc = (GameCharacter)cls.newInstance();
					gc.init(chars[i]);
					cTmp = this.cells.get((int)(Math.random() * this.cells.size()));
					while(!cTmp.isReachable()) cTmp = this.cells.get((int)(Math.random() * this.cells.size()));
					gc.setCell(cTmp);
					gc.setGameMap(this);
					cTmp.getCharacters().add(gc);
					this.characters.add(gc);
				}
			}
			catch(final Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

	/**
	 * Build the items list
	 * @param nbItems : the number of items on the map/characters
	 * @param items : items parameters
	 */
	protected void buildItems(int nbItems, String[][] items) {
		if (nbItems == 0 || items == null) return;
		for(int i = 0; i < items.length; i++) {
			try {
				final Class<?> cls = Class.forName(items[i][0]);
				final int n = nbItems * Integer.parseInt(items[i][1]) / 100;
				for(int j = 0; j < n; j++) {
					final Item item = (Item)cls.newInstance();
					final Cell<? extends Direction> c = this.cells.get((int)(Math.random() * this.cells.size()));
					if (this.characters.size() > 0 && (int)(Math.random() * 2) == 0) {
						try {
							final int r = (int)(Math.random() * this.characters.size());
							this.characters.get(r).getBag().add(item);
						}
						catch(final FullBagException e) {
							c.getItems().add(item);
						}
					}
					else c.getItems().add(item);
				}
			}
			catch(final Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
