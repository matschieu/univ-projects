package com.github.matschieu.maths;

/**
*@author Matschieu
*/
public class NullVectorException extends Exception {

	String message;

	public NullVectorException() { message = "Erreur : paramètre Vecteur null"; }
	public NullVectorException(String msg) { message = msg; }

	@Override
	public String toString() { return message; }

}