
package com.github.matschieu.game.map;

import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;

/**
 *@author Matschieu
 */
public abstract class GameMap {

	/** List of Cells that represent the map */
	protected List<Cell<? extends Direction>> cells;
	/** List of all characters who are on the map */
	protected List<GameCharacter> characters;
	/** Hero of the game who is on the map */
	protected GameCharacter hero;

	/**
	 * Constructs a new game map
	 */
	public GameMap() { }

	/**
	 * Returns all cells of the map
	 * @return List<Cell<? extends Direction>> : the list of all cell of the map
	 */
	public List<Cell<? extends Direction>> getCells() {
		return this.cells;
	}
	
	/**
	 * Returns all characters who are on the map
	 * @return List<GameCharacter> : the list of all characters who are on the map
	 */
	public List<GameCharacter> getCharacters() {
		return this.characters;
	}
	
	/**
	 * Returns the hero of the game who is on the map
	 * @return List<GameCharacter> : the hero of the game who is on the map
	 */
	public GameCharacter getHero() {
		return this.hero;
	}
	
	/**
	 * Builds a new map
	 */
	public abstract void build();

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
	 *		chars[x>0][y>4]=char's actions	 * @param nbItems : the number of items on the map/characters
	 * @param items : parameters about items<br />
	 *		items[x][0]=class name<br />
	 *		cells[x][1]=rate
	 */
	public abstract void build(String[][] cells, int nbChars, String[][] chars, int nbItems, String[][] items);
	
	/**
	 * Initializes the map with some parameters
	 * @param args : parameters to initialize the map
	 */
	public abstract void init(String[] args);
	
	/**
	 * Returns a string view of the map
	 * @return String : a string view of the map
	 */
	public abstract String getStringMap();
	
}
