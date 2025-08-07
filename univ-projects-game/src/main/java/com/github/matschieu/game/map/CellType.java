
package com.github.matschieu.game.map;

/**
 *@author Matschieu
 */
public interface CellType {
	
	/**
	 * Checks if this type of cell is reachable
	 * @return true if this type of cell is reachable else false
	 */
	public boolean isReachable();
	
	/**
	 * Returns the energy lost by the character who cross over a field of this type
	 * @return int : the energy lost by the character who cross over a field of this type
	 */
	public int energyLost();

	/**
	 * Returns a string description of this cell
	 * @return String : the description of this cell
	 */
	public String getDescription();

	/**
	 * Returns a string symbole of this cell
	 * @return String : the symbole of this cell
	 */
	public String getSymbole();
	
}

