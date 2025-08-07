
package com.github.matschieu.game.characters;

import java.util.List;

import com.github.matschieu.game.actions.Action;
import com.github.matschieu.game.items.Bag;
import com.github.matschieu.game.items.FullBagException;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.items.Purse;
import com.github.matschieu.game.map.Cell;
import com.github.matschieu.game.map.Direction;
import com.github.matschieu.game.map.GameMap;
import com.github.matschieu.game.strategy.Strategy;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public abstract class GameCharacter {

	/** The name of this character */
	protected String name;
	/** The total energy points of this character */
	protected int energy;
	/** The maximum energy points of this character */
	protected int maxEnergy;
	/** The strength of this character */
	protected int strength;
	/** The maximum strength of this character */
	protected int maxStrength;
	/** The amount of monney of this character */
	protected int gold;
	/** The bag that contains all the items of this character */
	protected Bag bag;
	/** The cell where this character is */
	protected Cell<? extends Direction> cell;
	/** The map on which the character is */
	protected GameMap map;
	/** The strategy used by this character to play */
	protected Strategy strategy;
	/** List of actions this character cans do */
	protected List<Action> actionsList;
	
	/**
	 * Constructs a new GameCharacter for the game
	 * @param name : the name of the character
	 * @param energy : the initial energy of this character
	 * @param maxEnergy : the maximum energy of this character
	 * @param strength : the initial strength of this character
	 * @param maxStrength : the maximum strength of this character
	 * @param strategy : the strategy used by this character to play
	 */
	public GameCharacter(String name, int energy, int maxEnergy, int strength, int maxStrength, Strategy strategy) {
		this.name = name;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.strength = strength;
		this.maxStrength = maxStrength;
		this.gold = 0;
		this.bag = new Bag();
		this.cell = null;
		this.map = null;
		this.strategy = strategy;
		this.actionsList = null;
	}
	
	/**
	 * Returns the current amount of monney of this character
	 * @return int : character's amount of monney
	 */
	public int getGold() {
		return this.gold;
	}

	/**
	 * Returns the total energy of this character
	 * @return int : character's energy 
	 */
	public int getEnergy() {
		return this.energy;
	}

	/**
	 * Returns the maximum energy of this character
	 * @return int : character's maximum energy 
	 */
	public int getMaxEnergy() {
		return this.maxEnergy;
	}

	/**
	 * Returns the current strength of this character
	 * @return int : character's strength
	 */
	public int getStrength() {
		return this.strength;
	}

	/**
	 * Returns the maximum strength of this character
	 * @return int : character's maximum strength
	 */
	public int getMaxStrength() {
		return this.maxStrength;
	}

	/**
	 * Returns the name of this character
	 * @return String : character's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns the bag of this character
	 * @return Bag : charcter's bag
	 */
	public Bag getBag() {
		return this.bag;
	}
	
	/**
	 * Returns a list of all actions this character can do
	 * @return List<Action> : a list of all actions this character can do
	 */
	public List<Action> getActionsList() {
		return this.actionsList;
	}
	
	/**
	 * Returns the cell where this character is
	 * @return Cell<? extends Direction> : the cell where the character is
	 */
	public Cell<? extends Direction> getCell() {
		return this.cell;
	}
	
	/**
	 * Returns the map where this character is
	 * @return GameMap : the map where the character is
	 */
	public GameMap getGameMap() {
		return this.map;
	}
	
	/**
	 * Returns the strategy used by this character to play
	 * @return Strategy : the strategy used by this character to play
	 */
	public Strategy getStrategy() {
		return this.strategy;
	}
	
	/**
	 * Returns the actions this character cans do
	 * @return List<Action> : a list of actions this character cans do
	*/
	public List<Action> getActions() {
		return this.actionsList;
	}
	
	/**
	 * Updates the name of this character
	 * @param name : character's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Updates the amount of monney of this character
	 * @param gold : the new amount of monney of this character's
	 */
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	/**
	 * Updates the energy of this character
	 * @param energy : the new energy of this character
	 */
	public void setEnergy(int energy) {
		if (energy > this.maxEnergy) this.energy = maxEnergy;
		else this.energy = energy;
	}
	
	/**
	 * Updates the strength of this character
	 * @param strength : the new strength of this character
	 */
	public void setStrength(int strength) {
		if (strength > this.maxStrength) this.strength = maxStrength;
		else this.strength = strength;
	}
	
	/**
	 * Updates the maximum energy of this character
	 * @param maxEnergy : the new maximum energy of this character
	 */
	public void setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	/**
	 * Updates the maximum strength of this character
	 * @param maxStrength : the new maximum strength of this character
	 */
	public void setMaxStrength(int maxStrength) {
		this.maxStrength = maxStrength;
	}
	
	/**
	 * Changes the cell where this character is
	 * @param cell : the cell where the character is now
	 */
	public void setCell(Cell<? extends Direction> cell) {
		this.cell = cell;
	}
	
	/**
	 * Changes the map where this character is
	 * @param map : the map where the character is now
	 */
	public void setGameMap(GameMap map) {
		this.map = map;
	}
	
	/**
	 * Updates the strategy used by this character to play
	 * @param strategy : the new strategy used by this character to play
	 */
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	/**
	 * Uses an item on this character
	 * @param item : the item to use on this character
	 */
	public void use(Item item) {
		if (this.bag.contains(item)) {
			item.use(this);
			this.bag.remove(item);
		}
	}
	
	/**
	 * Checks if this character is alive (life > 0)
	 * @return true if this character is alive else false
	 */
	public boolean isAlive() {
		return this.energy > 0;
	}
	
	/**
	 * Picks up an item and add it in this character's bag
	 * @param item : the item to pick up
	 */
	public void pickUp(Item item) {
		try { if (item.canBePickedUp(this) && !bag.isFull()) bag.add(item); }
	       	catch(FullBagException e) { }
	}
	
	/**
	 * Method called when this character dies
	 */	
	public void die() {
		if (this.energy > 0) return;
		this.cell.getItems().add(new Purse(this.gold));
		this.cell.getItems().addAll(this.bag.toList());
		this.cell.getCharacters().remove(this);
		this.cell = null;
		String display = Localization.SINGLETON.getElement("GAMECHARACTER_DIE");
		display = display.replace("[[CHAR_NAME]]", this.name);
		System.out.println(display);
	}
	
	/**
	 * Returns a string representation of this character
	 * @return String : a string representation of this character 
	 */
	public String getDescription() {
		return this.name;
	}
	
	/**
	 * Returns a string representation of this character
	 * @return String : a string representation of this character 
	 */
	public String toString() {
		String ret = Localization.SINGLETON.getElement("GAMECHARACTER_TOSTRING");
		ret = ret.replace("[[CHAR_NAME]]", this.name);
		ret = ret.replace("[[CHAR_ENERGY]]", "" + this.energy);
		ret = ret.replace("[[CHAR_STRENGTH]]", "" + this.strength);
		ret = ret.replace("[[CHAR_GOLD]]", "" + this.gold);
		return  ret;
	}
	
	/**
	 * Checks if this character can be attacked
	 * @return true if this character can be attacked else false
	 */
	public abstract boolean canBeAttacked();
	
	/**
	 * Actions this character can do on a tour
	 */
	public abstract void action();
	
	/**
	 * Returns a string symbole of this character
	 * @return String : the symbole of this character
	 */
	public abstract String getSymbole();
	
	/**
	 * Initializes the character with some values
	 * @param args : an array of parameters
	 */
	public abstract void init(String[] args);

}
