
package com.github.matschieu.game.items;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Food extends Item {

	/** The energy given by eating this food */
	private int energy;

	/**
	 * Constructs a new food item
	 * @param name : the name of this item
	 * @param energy : the energy given by this food item
	 */
	public Food(String name, int energy) {
		super(name);
		this.energy = energy;
	}

	/**
	 * Returns the energy of this food item
	 * @return int : the energy given by this food item
	 */
	public int getEnergy() {
		return this.energy;
	}

	/**
	 * Applies the effects of this item on the character that use it<br />
	 * Add some energy to character
	 * @param character : the character that uses this item
	 */
	public void use(GameCharacter character) {
		character.setEnergy(character.getEnergy() + this.energy);
	}

	/**
	 * Checks if this item can be picked up by the character
	 * @param character : the character that wants to pick up this item
	 * @return true if the character can pick up this item else false
	 */
	public boolean canBePickedUp(GameCharacter character) {
		return !character.getBag().isFull();
	}

	/**
	 * Returns a string representation of this item
	 * @return String : a string representation of this item
	 */
	public String getDescription() {
		String ret = Localization.SINGLETON.getElement("FOOD_GETDESCRIPTION");
		ret = ret.replace("[[FOOD_NAME]]", this.name);
		ret = ret.replace("[[FOOD_ENERGY]]", "" + this.energy);
		return ret;
	}

}
