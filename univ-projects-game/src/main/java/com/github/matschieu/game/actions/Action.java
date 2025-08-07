
package com.github.matschieu.game.actions;

/**
 *@author Matschieu
 */
public interface Action {

	/**
	 * Executes the action
	 * @return true if the action consume a tour else false
	 */
	public boolean execute();

	/**
	 * Checks if this action is available for a character
	 * @return true if the action is available else false
	 */
	public boolean isAvailable();

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	public String getDescription();
}

