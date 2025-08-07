
package com.github.matschieu.game.actions;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Status implements Action, HeroAction {

	/** The character who executes the action */
	private GameCharacter character;
	
	/**
	 * Creates a new status action
	 * @param character : the character who executes the action
	 */	
	public Status(GameCharacter character) {
		this.character = character;
	}
	
	/**
	 * Executes the action<br />
	 * View the status of the character
	 * @return false
	 */
	public boolean execute() {
		StringBuffer strBuf = new StringBuffer();
		String display = Localization.SINGLETON.getElement("STATUS_EXECUTE_1");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		strBuf.append(display + "\n");
		display = Localization.SINGLETON.getElement("STATUS_EXECUTE_2");
		display = display.replace("[[CHAR_ENERGY]]", "" + this.character.getEnergy());
		strBuf.append(display + "\n");
		display = Localization.SINGLETON.getElement("STATUS_EXECUTE_3");
		display = display.replace("[[CHAR_STRENGTH]]", "" + this.character.getStrength());
		strBuf.append(display + "\n");		
		display = Localization.SINGLETON.getElement("STATUS_EXECUTE_4");
		display = display.replace("[[CHAR_GOLD]]", "" + this.character.getGold());
		strBuf.append(display + "\n");
		System.out.print(strBuf.toString());
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
		return Localization.SINGLETON.getElement("STATUS_GETDESCRIPTION");

	}

}
