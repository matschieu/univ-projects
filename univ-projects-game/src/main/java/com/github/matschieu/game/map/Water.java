
package com.github.matschieu.game.map;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Water implements CellType {
		
	/** String description of this type */
	public static final String DESCRIPTION = Localization.SINGLETON.getElement("WATER_DESCRIPTION");
	
	/**
	 * Constructs a new water type 
	 */	
	public Water() { }

	/**
	 * Checks if this type of cell is reachable
	 * @return false 
	 */
	public boolean isReachable() {
		return false;
	}
	
	/**
	 * Returns the energy lost by the character who cross over a field of this type
	 * @return int : the energy lost by the character who cross over a field of this type
	 */
	public int energyLost() {
		return 0;
	}

	/**
	 * Returns a string description of this cell
	 * @return String : the description of this cell
	 */
	public String getDescription() {
		return DESCRIPTION;
	}

	/**
	 * Returns a string symbole of this cell
	 * @return String : the symbole of this cell
	 */
	public String getSymbole() {
		return Localization.SINGLETON.getElement("WATER_SYMBOLE");
	}

}
