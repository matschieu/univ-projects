
package com.github.matschieu.game.actions;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Quit implements Action, HeroAction {

	/** The character who executes the action */
	private GameCharacter character;
	
	/**
	 * Creates a new quit action
	 * @param character : the character who executes the action
	 */	
	public Quit(GameCharacter character) {
		this.character = character;
	}
	
	/**
	 * Executes the action<br />
	 * Quit the game
	 * @return true
	 */
	public boolean execute() {
		this.character.setEnergy(0);
		String display = Localization.SINGLETON.getElement("QUIT_EXECUTE");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		System.out.println(display);
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
		return Localization.SINGLETON.getElement("QUIT_GETDESCRIPTION");
	}

}
