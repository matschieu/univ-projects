
package com.github.matschieu.game.items;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Potion extends Food {

	/** Energy to add or remove */
	private static final int ENERGY = 10;
	/** Strength to add */
	private static final int STRENGTH = 1;

	/**
	 * Constructs a new potion
	 */
	public Potion() {
		super(Localization.SINGLETON.getElement("POTION_NAME"), 0);
	}

	/**
	 * Applies the effects of this item on the character that use it<br />
	 * Add or remove some energy to character or increase his strength
	 * @param character : the character that uses this item
	 */
	@Override
	public void use(GameCharacter character) {
		character.setEnergy(character.getEnergy() - 50);
		switch((int)(Math.random() * 3)) {
			case 0 :
				character.setEnergy(character.getEnergy() + ENERGY);
				break;
			case 1 :
				character.setEnergy(character.getEnergy() - ENERGY);
				break;
			case 2 :
				character.setStrength(character.getStrength() + STRENGTH);
				character.setMaxStrength(character.getMaxStrength() + STRENGTH);
		}
	}

	/**
	 * Returns a string representation of this item
	 * @return String : a string representation of this item
	 */
	@Override
	public String getDescription() {
		String ret = Localization.SINGLETON.getElement("POTION_GETDESCRIPTION");
		ret = ret.replace("[[POTION_NAME]]", this.name);
		ret = ret.replace("[[POTION_ENERGY]]", "" + ENERGY);
		ret = ret.replace("[[POTION_STRENGTH]]", "" + STRENGTH);
		return ret;
	}

}
