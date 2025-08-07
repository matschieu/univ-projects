
package com.github.matschieu.game.items;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Treasure extends Gold {

	/**
	 * Constructs a new treasure
	 */
	public Treasure() {
		this((int)(Math.random() * 1000 + 50));
	}

	/**
	 * Constructs a new treasure
	 * @param value : the value of the treasure
	 */
	public Treasure(int value) {
		super(Localization.SINGLETON.getElement("TREASURE_NAME"), value);
	}

}

