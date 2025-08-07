
package com.github.matschieu.ftp.client.web;

/**
 * @author Matschieu
 */
public class CircularStringList extends CircularList<String> {

	/**
	 * Creates a new String circular list
	 * @param maxElements the maximum elements possible to add
	 */
	public CircularStringList(int maxElements) {
		super(maxElements);
	}

	/**
	 * Creates a new String circular list
	 */
	public CircularStringList() {
		super();
	}

}
