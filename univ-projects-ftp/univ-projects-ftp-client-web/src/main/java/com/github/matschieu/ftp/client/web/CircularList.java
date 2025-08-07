
package com.github.matschieu.ftp.client.web;

import java.util.ArrayList;

/**
 * @author Matschieu
 */
public class CircularList<T> {

	public static final int MAX_ELEMENT = 10;

	private ArrayList<T> array;
	private int currentIdx;
	private int maxElements;

	/**
	 * Creates a new circular list
	 * @param maxElements the maximum elements possible to add
	 */
	public CircularList(int maxElements) {
		this.array = new ArrayList<T>(maxElements);
		this.currentIdx = 0;
		this.maxElements = maxElements;
	}

	/**
	 * Creates a new circular list
	 */
	public CircularList() {
		this(MAX_ELEMENT);
	}

	/**
	 * Adds an element to the list (at the end)
	 * Delete the first element of the list if the list is full
	 * @param element the element to add
	 */
	public void add(T element) {
		if (this.currentIdx < this.maxElements) {
			this.array.add(element);
			this.currentIdx++;
			return;
		}
		for(int i = 0; i < this.currentIdx; i++) {
			if (i + 1 < this.currentIdx)
				this.array.set(i, this.array.get(i + 1));
			else 
				this.array.set(i, element);
		}
	}

	/**
	 * Returns the element at the idx index in the list
	 *  @param idx the index of the element to get
	 *  @return T the element at the index idx in the list (null if the index is invalid)
	 */
	public T get(int idx) {
		if (idx < 0 || idx >= this.array.size())
			return null;
		return this.array.get(idx);
	}

	/**
	 * Returns the number of elements in the list
	 * @return int the number of elements in the list
	 */
	public int getNbElement() {
		return this.currentIdx;
	}

	/**
	 * Returns a String representation of this list
	 * @return String the string representation of the list
	 */
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(int i = 0; i < this.currentIdx; i++)
			str.append(this.array.get(i) + "\n");
		return str.toString();
	}

}

