
package com.github.matschieu.game.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.items.Item;

/**
 *@author Matschieu
 */
public abstract class Cell<D extends Direction> {

	/** Map of all neighbours of this cell which are identified by the direction */
	protected Map<D, Cell<? extends Direction>> neighbours;
	/** List of all items on this cell */
	protected List<Item> items;
	/** List of all characters on this cell */
	protected List<GameCharacter> characters;
	/** The type of this Cell */
	protected CellType type;

	/**
	 * Constructs a new cell for the game map
	 * @param type : the type of this cell
	 */
	public Cell(CellType type) {
		this.type = type;
		this.neighbours = new HashMap<D, Cell<? extends Direction>>();
		this.items = new LinkedList<Item>();
		this.characters = new LinkedList<GameCharacter>();
	}

	/**
	 * Checks if this cell is reachable
	 * @return true if this cell is reachable else false
	 */
	public boolean isReachable() {
		return this.type.isReachable();
	}

	/**
	 * Returns the type of this cell
	 * @return CellType : the type of this cell
	 */
	public CellType getType() {
		return this.type;
	}

	/**
	 * Returns the energy lost by the character who cross over this field
	 * @return int : the energy lost by the character who cross over this field
	 */
	public int energyLost() {
		return this.type.energyLost();
	}

	/**
	 * Returns a map which contains neighbours
	 * @return Map<D, Cell<? extends Direction>> : a map  which contains neighbours
	 */
	public Map<D, Cell<? extends Direction>> getNeighboursMap() {
		return this.neighbours;
	}

	/**
	 * Returns the neighbour of this cell situated in the direction dir
	 * @param dir : the direction of the neighbour
	 * @return Cell<? extends Direction> : the neighbour in the direction dir
	 */
	public Cell<? extends Direction> getNeighbour(D dir) {
		return this.neighbours.get(dir);
	}

	/**
	 * Returns all neighbours of this cell
	 * @return List<Cell<? extends Direction>> : list of all neighbours of this cell
	 */
	public List<Cell<? extends Direction>> getAllNeighbours() {
		List<Cell<? extends Direction>> list = new LinkedList<Cell<? extends Direction>>();
		Collection<Cell<? extends Direction>> c = this.neighbours.values();
		Iterator<Cell<? extends Direction>> it = c.iterator();
		while(it.hasNext()) list.add(it.next());
		return list;
	}

	/**
	 * Returns a list of items of this cell
	 * @return List<Items> : the list of items of this cell
	 */
	public List<Item> getItems() {
		return this.items;
	}

	/**
	 * Returns a list of characters of this cell
	 * @return List<GameCharacter> : the list of characters of this cell
	 */
	public List<GameCharacter> getCharacters() {
		return this.characters;
	}

	/**
	 * Returns a string description of this cell
	 * @return String : the description of this cell
	 */
	public String getDescription() {
		return this.type.getDescription();
	}

	/**
	 * Returns a string symbole of this cell
	 * @return String : the symbole of this cell
	 */
	public String getSymbole() {
		return this.type.getSymbole();
	}

	/**
	 * Adds a neighbour of this cell situated in the direction dir
	 * @param cell : the neighbour in the direction dir
	 * @param dir : the direction of the neighbour
	 */
	public abstract void addNeighbour(Cell<? extends Direction> cell, D dir);

}
