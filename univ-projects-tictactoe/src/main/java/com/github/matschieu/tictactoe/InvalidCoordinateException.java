package com.github.matschieu.tictactoe;

class InvalidCoordinateException extends Exception {

	private String message;
	
	public InvalidCoordinateException(String message) { this.message = message; }
	public InvalidCoordinateException() { this(""); }

	public String toString() { return message; }

}
