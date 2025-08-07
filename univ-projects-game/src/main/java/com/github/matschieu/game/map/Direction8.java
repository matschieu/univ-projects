
package com.github.matschieu.game.map;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public enum Direction8 implements Direction {

	EAST, NORTH, WEST, SOUTH, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST;
	
	/**
	 * Returns the number of direction (cell's side number)
	 * @return int : the number of direction
	 */
	public int getSideNumber() {
		return 8;
	}
	
	/**
	 * Returns a string representation of this direction (equals to toString())
	 * @return String : a string representation of this direction
	 */
	public String getDescription() {
		return this.toString();
	}
	
	/**
	 * Returns a string representation of this direction (equals to getDescription())
	 * @return String : a string representation of this direction
	 */
	public String toString() {
		switch(this.ordinal()) {
			case 0 : return Localization.SINGLETON.getElement("DIRECTION8_EAST");
			case 1 : return Localization.SINGLETON.getElement("DIRECTION8_NORTH");
			case 2 : return Localization.SINGLETON.getElement("DIRECTION8_WEST");
			case 3 : return Localization.SINGLETON.getElement("DIRECTION8_SOUTH");
			case 4 : return Localization.SINGLETON.getElement("DIRECTION8_NORTH_EAST");
			case 5 : return Localization.SINGLETON.getElement("DIRECTION8_NORTH_WEST");
			case 6 : return Localization.SINGLETON.getElement("DIRECTION8_SOUTH_EAST");
			case 7 : return Localization.SINGLETON.getElement("DIRECTION8_SOUTH_WEST");

		}
		return null;
	}

}
