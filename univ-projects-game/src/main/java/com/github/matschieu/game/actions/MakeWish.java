
package com.github.matschieu.game.actions;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class MakeWish implements Action, HeroAction {

	/** The energy to add */
	private static final int ENERGY = 25;

	/** The character who executes the action */
	private GameCharacter character;
	/** The number of times a character can call this action (with effect) */
	private int call;

	/**
	 * Creates a new make a wish action
	 * @param character : the character who executes the action
	 */
	public MakeWish(GameCharacter character) {
		this.character = character;
		this.call = 3;
	}

	/**
	 * Executes the action<br />
	 * Make a wish (gives 25 of energy 3 times)
	 * @return true
	 */
	@Override
	public boolean execute() {
		if (this.call > 0) {
			int charNewEnergy = this.character.getEnergy() + ENERGY;
			int charMaxEnergy = this.character.getMaxEnergy();
			if (charNewEnergy > charMaxEnergy) this.character.setEnergy(charMaxEnergy);
			else this.character.setEnergy(charNewEnergy);
			String display = Localization.SINGLETON.getElement("MAKEWISH_EXECUTE_1");
			display = display.replace("[[CHAR_NAME]]", this.character.getName());
			display = display.replace("[[ENERGY]]", "" + ENERGY);
			display = display.replace("[[CHAR_ENERGY]]", "" + this.character.getEnergy());
			System.out.println(display);
			this.call--;
		}
		else System.out.println(Localization.SINGLETON.getElement("MAKEWISH_EXECUTE_2"));
		return true;
	}

	/**
	 * Checks if this action is available for a character
	 * @return true
	 */
	@Override
	public boolean isAvailable() {
		return true;
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("MAKEWISH_GETDESCRIPTION");
	}

}
