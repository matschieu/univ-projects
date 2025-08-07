package com.github.matschieu.maths;

/**
*@author Matschieu
*/
public class WriteFileException extends Exception {

	private String message;

	public WriteFileException() { message = "Erreur : impossible d'écrire dans le fichier"; }
	public WriteFileException(String file) { message = "Erreur : impossible d'écrire dans le fichier" + file; }

	@Override
	public String toString() { return message; }

}
