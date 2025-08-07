
package com.github.matschieu.game.items;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *@author Matschieu
 */
public class Bag {

	/** The list of the items contained by the bag */
	private List<Item> itemsList;
	/** The maximum number of items that can be contained */
	private int maxItems;

	/**
	 * Constructs a new bag encapsule a list of Item
	 * @param maxItems : the number of items the bag can contain
	 */
	public Bag(int maxItems) {
		this.itemsList = new LinkedList<Item>();
		this.maxItems = maxItems;
	}

	/**
	 * Constructs a new bag encapsule a list of Item
	 */
	public Bag() { this(100); }

	/**
	 * Updates the maximum of items the bag can contain
	 * @param maxItems : the maximum of item the bag can contain
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	/**
	 * Adds an item to this bag
	 * @param item : an item to add to the bag
	 * @throws FullBagException if the bag is full
	 */
	public void add(Item item) throws FullBagException {
		if (this.isFull()) throw new FullBagException();
		this.itemsList.add(item);
	}

	/**
	 * Removes an item from this bag
	 * @param idx : the index of the item to remove from the bag
	 * @return Item : the item which has just removed from the bag
	 */
	public Item remove(int idx) {
		return this.itemsList.remove(idx);
	}

	/**
	 * Removes an item from this bag
	 * @param item : the item to remove from the bag
	 * @return true if the item belongs to the bag and is successfully removed else false
	 */
	public boolean remove(Item item) {
		return this.itemsList.remove(item);
	}

	/**
	 * Returns an item from this bag
	 * @param idx : the index of the item expected in the bag
	 * @return Item : the item at the idx position in the bag
	 */
	public Item get(int idx) {
		return this.itemsList.get(idx);
	}

	/**
	 * Checks if the bag is full
	 * @return true if the bag is full else false
	 */
	public boolean isFull() {
		return this.itemsList.size() == maxItems;
	}

	/**
	 * Checks if the bag is empty
	 * @return true if the bag is empty else false
	 */
	public boolean isEmpty() {
		return this.itemsList.isEmpty();
	}

	/**
	 * Checks if item belongs to the bag
	 * @return true if item belongs to the bag else false
	 */
	public boolean contains(Item item) {
		return this.itemsList.contains(item);
	}

	/**
	 * Returns an iterator for the bag
	 * @return Iterator : an iterator for the bag
	 */
	public Iterator<Item> iterator() {
		return this.itemsList.iterator();
	}

	/**
	 * Returns a list of items which are in the bag
	 * @return List<Item> : a list of items which are in the bag
	 */
	public List<Item> toList() {
		return new LinkedList<Item>(this.itemsList);
	}

	/**
	 * Returns a string representation of this bag
	 * @return String : a string representation of this bag
	 */
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("[");
		Iterator<Item> it = this.itemsList.iterator();
		while(it.hasNext()) {
			Item i = it.next();
			str.append(i);
			str.append(", ");
		}
		str.append("]\n");
		return str.toString();
	}

}
