
package com.github.matschieu.game.map;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Field implements CellType {
	
	/** String description of this type */
	public static final String DESCRIPTION = Localization.SINGLETON.getElement("FIELD_DESCRIPTION");
	
	/**
	 * Constructs a new field type 
	 */	
	public Field() { }

	/**
	 * Checks if this type of cell is reachable
	 * @return true
	 */
	public boolean isReachable() {
		return true;
	}
	
	/**
	 * Returns the energy lost by the character who cross over a field of this type
	 * @return int : the energy lost by the character who cross over a field of this type
	 */
	public int energyLost() {
		return 1;
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
		return Localization.SINGLETON.getElement("FIELD_SYMBOLE");
	}

}
