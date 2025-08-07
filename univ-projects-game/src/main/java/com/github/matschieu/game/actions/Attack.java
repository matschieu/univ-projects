
package com.github.matschieu.game.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Attack implements Action, HeroAction, MonsterAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new attack action
	 * @param character : the character who executes the action
	 */
	public Attack(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Attack another character
	 * @return true if the action consume a tour else false
	 */
	@Override
	public boolean execute() {
		StringBuffer strBuf = new StringBuffer();
		List<GameCharacter> chars = this.character.getCell().getCharacters();
		List<GameCharacter> tmp = new LinkedList<GameCharacter>();
		Iterator<GameCharacter> it = chars.iterator();
		while(it.hasNext()) {
			GameCharacter c = it.next();
			if (c.isAlive() && c.canBeAttacked() && c != this.character) tmp.add(c);
		}
		GameCharacter ennemy = this.character.getStrategy().choiceGameCharacter(tmp);
		if (ennemy == null) return false;
		String display = Localization.SINGLETON.getElement("ATTACK_EXECUTE_2");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		display = display.replace("[[ENNEMY_NAME]]", ennemy.getName());
		strBuf.append(display + "\n");
		ennemy.setEnergy(ennemy.getEnergy() - this.character.getStrength());
		display = Localization.SINGLETON.getElement("ATTACK_EXECUTE_1");
		display = display.replace("[[ENNEMY_NAME]]", ennemy.getName());
		display = display.replace("[[CHAR_STRENGTH]]", "" + this.character.getStrength());
		display = display.replace("[[ENNEMY_ENERGY]]", ennemy.getEnergy() > 0 ? "" + ennemy.getEnergy() : "0");
		strBuf.append(display);
		System.out.println(strBuf.toString());
		if (ennemy.getEnergy() < 1) ennemy.die();
		return true;
	}

	/**
	 * Checks if this action is available for a character (if there is another attackable character on the same sale of the character who attacks)
	 * @return true if the action is available else false
	 */
	@Override
	public boolean isAvailable() {
		List<GameCharacter> chars = this.character.getCell().getCharacters();
		Iterator<GameCharacter> it = chars.iterator();
		while(it.hasNext()) {
			GameCharacter c = it.next();
			if (c.isAlive() && c.canBeAttacked() && c != this.character) return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("ATTACK_GETDESCRIPTION");
	}

}
