
package com.github.matschieu.game.actions;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class NoOption implements Action {
	
	/**
	 * Creates a new save game action
	 */
	public NoOption() { }
	
	/**
	 * Executes the action
	 * @return false
	 */
	public boolean execute() {
		return false;
	}
	
	/**
	 * Checks if this action is available for a character
	 * @return true
	 */
	public boolean isAvailable() {
		return true;
	}
	
	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	public String getDescription() {
		return Localization.SINGLETON.getElement("HUMANSTRATEGY_PREVIOUS");
	}

}
