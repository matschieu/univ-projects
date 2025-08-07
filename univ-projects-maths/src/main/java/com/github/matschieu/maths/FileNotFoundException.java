package com.github.matschieu.maths;

/**
*@author Matschieu
*/
public class FileNotFoundException extends Exception {

	String message;

	public FileNotFoundException() { message = "Erreur : impossible d'ouvrir le fichier"; }
	public FileNotFoundException(String file) { message = "Erreur : impossible d'ouvrir le fichier " + file; }

	@Override
	public String toString() { return message; }

}