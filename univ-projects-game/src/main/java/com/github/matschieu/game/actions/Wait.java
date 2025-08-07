
package com.github.matschieu.game.actions;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Wait implements Action, HeroAction, MonsterAction {

	/**
	 * Creates a new wait action
	 */	
	public Wait() {	}
	
	/**
	 * Executes the action<br />
	 * Consume a tour without doing nothing
	 * @return true
	 */
	public boolean execute() {
		System.out.println();
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
		return Localization.SINGLETON.getElement("WAIT_GETDESCRIPTION");
	}

}

