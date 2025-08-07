package com.github.matschieu.tictactoe;

class InvalidInputException extends Exception {

	private String message;
	
	public InvalidInputException(String message) { this.message = message; }
	public InvalidInputException() { this(""); }

	public String toString() { return message; }

}
