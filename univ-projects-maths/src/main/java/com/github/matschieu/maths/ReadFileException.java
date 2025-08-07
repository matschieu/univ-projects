package com.github.matschieu.maths;
/**
*@author Matschieu 
*/

public class ReadFileException extends Exception {

	private String message;

	public ReadFileException() { message = "Erreur : impossible de lire le fichier"; }
	public ReadFileException(String file) { message = "Erreur : impossible de lire le fichier " + file; }

	public String toString() { return message; }

}
