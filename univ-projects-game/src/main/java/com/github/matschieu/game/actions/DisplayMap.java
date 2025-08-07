
package com.github.matschieu.game.actions;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class DisplayMap implements Action, HeroAction {

	/** The character who executes the action */
	private GameCharacter character;
	
	/**
	 * Creates a new display map action
	 * @param character : the character who executes the action
	 */
	public DisplayMap(GameCharacter character) {
		this.character = character;
	}
	
	/**
	 * Executes the action<br />
	 * Dispkay the map
	 * @return false
	 */
	public boolean execute() {
		System.out.print(this.character.getGameMap().getStringMap());
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
		return Localization.SINGLETON.getElement("DISPLAYMAP_GETDESCRIPTION");
	}

}
