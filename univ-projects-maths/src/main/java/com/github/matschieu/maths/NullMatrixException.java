package com.github.matschieu.maths;

/**
*@author Matschieu
*/
public class NullMatrixException extends Exception {

	String message;

	public NullMatrixException() { message = "Erreur : parametre Matrice null"; }
	public NullMatrixException(String msg) { message = msg; }

	@Override
	public String toString() { return message; }

}