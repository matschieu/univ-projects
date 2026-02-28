package com.github.matschieu.labyrinthe;

/**
 * @author  Matschieu
 * @version Mars 2007
 */
class LabyrintheException extends Exception {

	private static final long serialVersionUID = -8883558420359063006L;

	private String message;

	LabyrintheException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}