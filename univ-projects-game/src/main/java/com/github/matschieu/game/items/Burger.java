
package com.github.matschieu.game.items;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Burger extends Food {

	/**
	 * Constructs a new burger
	 */
	public Burger() {
		this(10);
	}
	
	/**
	 * Constructs a new burger
	 * @param energy : the energy gived by using this burger
	 */
	public Burger(int energy) {
		super(Localization.SINGLETON.getElement("BURGER_NAME"), energy);
	}

}

