
package com.github.matschieu.game.characters;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Healer extends GameCharacter {

	/** To generate a name for a healer */
	private static int cpt = 0;

	/** the default energy of a monster */
	private static final int DEFAULT_ENERGY = 100;
	/** the default strength of a monster */
	private static final int DEFAULT_STRENGTH = 0;
	
	/**
	 * Constructs a new healer
	 */
	public Healer() {
		this(Localization.SINGLETON.getElement("HEALER_NAME") + cpt++);
	}
	
	/**
	 * Constructs a new healer
	 * @param name : the name of this healer
	 */
	public Healer(String name) {
		super(name, DEFAULT_ENERGY, DEFAULT_ENERGY, DEFAULT_STRENGTH, DEFAULT_STRENGTH, null);
	}

	/**
	 * Initializes the healer with some values
	 * @param args : an array of parameters<br />
	 *		args[0]=class name<br />
	 *		args[1]=rate<br />
	 *		args[2]=healer's energy<br />
	 *		args[3]=healer's strength
	 */
	public void init(String[] args) {
		this.name = Localization.SINGLETON.getElement("HEALER_NAME") + cpt++;
		try {
			this.maxEnergy = this.energy = Integer.parseInt(args[2]);
			this.maxStrength = this.strength = Integer.parseInt(args[3]);
		}
		catch(NumberFormatException e) { }
	}
	
	/**
	 * Checks if this character can be attacked
	 * @return false
	 */
	public boolean canBeAttacked() {
		return false;
	}

	/**
	 * Actions this character can do on a tour<br />
	 * For a healer, this method doesn't do anything
	 */
	public void action() { 
		String display = Localization.SINGLETON.getElement("HEALER_ACTION");
		display = display.replace("[[CHAR_NAME]]", this.name);
		System.out.println("* " + display);
	}
	
	/**
	 * Returns a string symbole of this character
	 * @return String : the symbole of this character
	 */
	public String getSymbole() {
		return Localization.SINGLETON.getElement("HEALER_SYMBOLE");
	}
	
}
