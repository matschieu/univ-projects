
package com.github.matschieu.game.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Interact implements Action, HeroAction, MonsterAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new interact action
	 * @param character : the character who executes the action
	 */
	public Interact(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Interaction with another character
	 * @return true if the action consume a tour else false
	 */
	@Override
	public boolean execute() {
		StringBuffer strBuf = new StringBuffer();
		String display = "";
		List<GameCharacter> chars = this.character.getCell().getCharacters();
		List<GameCharacter> tmp = new LinkedList<GameCharacter>();
		Iterator<GameCharacter> it = chars.iterator();
		while(it.hasNext()) {
			GameCharacter c = it.next();
			if (!c.canBeAttacked() && c != this.character) tmp.add(c);
		}
		GameCharacter contact = this.character.getStrategy().choiceGameCharacter(tmp);
		if (contact == null) return false;
		if (this.character.getGold() == 0) {
			display = Localization.SINGLETON.getElement("INTERACT_EXECUTE_1");
			display = display.replace("[[CHAR_NAME]]", this.character.getName());
			display = display.replace("[[CONTACT_NAME]]", contact.getName());
			System.out.println(display);
			return false;
		}
		display = Localization.SINGLETON.getElement("INTERACT_EXECUTE_2");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		display = display.replace("[[CONTACT_NAME]]", contact.getName());
		strBuf.append(display + "\n");
		int lifeMissing = character.getMaxEnergy() - character.getEnergy();
		if (lifeMissing <= this.character.getGold()) {
			this.character.setEnergy(this.character.getMaxEnergy());
			this.character.setGold(this.character.getGold() - lifeMissing);
			display = Localization.SINGLETON.getElement("INTERACT_EXECUTE_3");
			display = display.replace("[[CHAR_NAME]]", this.character.getName());
			display = display.replace("[[CONTACT_NAME]]", contact.getName());
			strBuf.append(display);
		}
		else {
			this.character.setEnergy(this.character.getEnergy() + this.character.getGold());
			display = Localization.SINGLETON.getElement("INTERACT_EXECUTE_4");
			display = display.replace("[[CHAR_NAME]]", this.character.getName());
			display = display.replace("[[CONTACT_NAME]]", contact.getName());
			display = display.replace("[[ENERGY]]", "" + this.character.getGold());
			strBuf.append(display);
			this.character.setGold(0);
		}
		System.out.println(strBuf.toString());
		return true;
	}

	/**
	 * Checks if this action is available for a character (if there is another character who can't be attacked on the same sale of the character who interacts)
	 * @return true if the action is available else false
	 */
	@Override
	public boolean isAvailable() {
		List<GameCharacter> chars = this.character.getCell().getCharacters();
		Iterator<GameCharacter> it = chars.iterator();
		while(it.hasNext()) {
			GameCharacter c = it.next();
			if (!c.canBeAttacked() && c != this.character) return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("INTERACT_GETDESCRIPTION");
	}

}

