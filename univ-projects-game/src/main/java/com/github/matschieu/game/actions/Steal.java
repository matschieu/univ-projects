
package com.github.matschieu.game.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Steal implements Action {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new attack action
	 * @param character : the character who executes the action
	 */
	public Steal(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Attack another character
	 * @return true if the action consume a tour else false
	 */
	@Override
	public boolean execute() {
		List<GameCharacter> chars = this.character.getCell().getCharacters();
		List<GameCharacter> tmp = new LinkedList<GameCharacter>();
		Iterator<GameCharacter> it = chars.iterator();
		while(it.hasNext()) {
			GameCharacter c = it.next();
			if (c.isAlive() && c.canBeAttacked() && c != this.character) tmp.add(c);
		}
		GameCharacter ennemy = this.character.getStrategy().choiceGameCharacter(tmp);
		if (ennemy == null) return false;
		int monney = 25 / 100 * ennemy.getGold();
		ennemy.setGold(ennemy.getGold() - monney);
		this.character.setGold(this.character.getGold() + monney);
		String display = Localization.SINGLETON.getElement("STEAL_EXECUTE");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		display = display.replace("[[MONNEY]]", "" + monney);
		display = display.replace("[[ENNEMY_NAME]]", ennemy.getName());
		System.out.println(display);
		return true;
	}

	/**
	 * Checks if this action is available for a character (if there is another character on the same sale of the character who steals)
	 * @return true if the action is available else false
	 */
	@Override
	public boolean isAvailable() {
		List<GameCharacter> chars = this.character.getCell().getCharacters();
		return chars.size() > 1;
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("STEAL_GETDESCRIPTION");
	}

}
