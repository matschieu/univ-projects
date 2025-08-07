package com.github.matschieu.maths;

/**
*@author Matschieu
*/
public class InputMismatchException extends Exception {

	String message;

	public InputMismatchException() { message = "Erreur : structure du fichier incorrecte"; }
	public InputMismatchException(String file) { message = "Erreur : structure du fichier " + file + " incorrecte"; }

	@Override
	public String toString() { return message; }

}