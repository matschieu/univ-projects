
package com.github.matschieu.game.map;

/**
 *@author Matschieu
 */
public class Cell8 extends Cell<Direction8> {

	/**
	 * Constructs a new cell with 8 neighbours for the game map
	 * @param type : the type of this cell
	 */
	public Cell8(CellType type) {
		super(type);
	}

	/**
	 * Adds a neighbour of this cell situated in the direction dir
	 * @param cell : the neighbour in the direction dir
	 * @param dir : the direction of the neighbour
	 */
	public void addNeighbour(Cell<? extends Direction> cell, Direction8 dir) {
		if (this.neighbours.containsKey(dir)) this.neighbours.remove(dir);
		if (this.neighbours.keySet().size() < dir.getSideNumber()) this.neighbours.put(dir, cell);
	}

}
