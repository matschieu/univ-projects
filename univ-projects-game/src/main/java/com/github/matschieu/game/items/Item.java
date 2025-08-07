
package com.github.matschieu.game.items;

import com.github.matschieu.game.characters.GameCharacter;

/**
 *@author Matschieu
 */
public abstract class Item {

	/** The name of this item */
	protected String name;

	/**
	 * Constructs a new Item
	 * @param name : the name of this item
	 */
	public Item(String name) {
		this.name = name;
	}

	/**
	 * Applies the effects of this item on the character that use it
	 * @param character : the character that uses this item
	 */
	public abstract void use(GameCharacter character);

	/**
	 * Checks if this item can be picked up by the character
	 * @param character : the character that wants to pick up this item
	 * @return true if the character can pick up this item else false
	 */
	public abstract boolean canBePickedUp(GameCharacter character);
	
	/**
	 * Returns a string representation of this item
	 * @return String : a string representation of this item
	 */
	public abstract String getDescription();

	/**
	 * Returns a string representation of this item
	 * @return String : a string representation of this item
	 */
	public String toString() {
		return this.name;
	}

}
