
package com.github.matschieu.game.actions;

import com.github.matschieu.game.OnePlayerGame;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class NewGame implements Action {
	
	/**
	 * Creates a new save game action
	 */
	public NewGame() { }
	
	/**
	 * Executes the action<br />
	 * Save the game
	 * @return true
	 */
	public boolean execute() {
		System.out.println("\n" + Localization.SINGLETON.getElement("NEWGAME_EXECUTE") + "\n");
		(new OnePlayerGame()).play();
		System.exit(0);
		return true;
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
		return Localization.SINGLETON.getElement("NEWGAME_GETDESCRIPTION");
	}

}
