
package com.github.matschieu.game.items;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Purse extends Gold {

	/**
	 * Constructs a new purse
	 */
	public Purse() {
		this((int)(Math.random() * 500 + 50));
	}

	/**
	 * Constructs a new purse
	 * @param value : the monney in the purse
	 */
	public Purse(int value) {
		super(Localization.SINGLETON.getElement("PURSE_NAME"), value);
	}

}

