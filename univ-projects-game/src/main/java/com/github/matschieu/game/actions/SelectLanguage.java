
package com.github.matschieu.game.actions;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class SelectLanguage implements Action {
	
	/**
	 * Creates a new select language action
	 */	
	public SelectLanguage() { }
	
	/**
	 * Executes the action<br />
	 * Ask to select a language
	 * @return false
	 */
	public boolean execute() {
		Localization.SINGLETON.selectLanguage();
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
		return Localization.SINGLETON.getElement("SELECTLANGUAGE_GETDESCRIPTION");
	}

}
