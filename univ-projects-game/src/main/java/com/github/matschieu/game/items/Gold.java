
package com.github.matschieu.game.items;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Gold extends Item {

	/** The value of this gold item */
	private int value;

	/**
	 * Constructs a new gold item
	 * @param name : the name of this item
	 * @param value : the value of this gold item
	 */
	public Gold(String name, int value) {
		super(name);
		this.value = value;
	}

	/**
	 * Returns the value of this gold item
	 * @return int : the value of this gold item
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Applies the effects of this item on the character that use it<br />
	 * Add some monney to character
	 * @param character : the character that uses this item
	 */
	public void use(GameCharacter character) {
		character.setGold(character.getGold() + this.value);
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
		String ret = Localization.SINGLETON.getElement("GOLD_GETDESCRIPTION");
		ret = ret.replace("[[GOLD_NAME]]", this.name);
		ret = ret.replace("[[GOLD_VALUE]]", "" + this.value);
		return ret;
	}

}
