
package com.github.matschieu.game.map;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public enum Direction4 implements Direction {

	EAST, NORTH, WEST, SOUTH;
	
	/**
	 * Returns the number of direction (cell's side number)
	 * @return int : the number of direction
	 */
	public int getSideNumber() {
		return 4;
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
			case 0 : return Localization.SINGLETON.getElement("DIRECTION4_EAST");
			case 1 : return Localization.SINGLETON.getElement("DIRECTION4_NORTH");
			case 2 : return Localization.SINGLETON.getElement("DIRECTION4_WEST");
			case 3 : return Localization.SINGLETON.getElement("DIRECTION4_SOUTH");
		}
		return null;
	}

}
