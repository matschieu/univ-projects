package com.github.matschieu.tictactoe;

import java.util.Scanner;

public class InputTerm {

	private Scanner scan;

	public InputTerm() { this.scan = new Scanner(System.in); }

	public String inputString() throws InvalidInputException {
		try { return scan.next(); }
		catch(Exception e) { throw new InvalidInputException("Erreur : Saisie incorrect"); }
	}

	public int inputInt() throws InvalidInputException {
		try { return scan.nextInt(); }
		catch(Exception e) { throw new InvalidInputException("\nErreur : Saisie d'entier incorrect"); }
	}

}


/*

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InputTerm {

	public InputTerm() { }

	public String inputString() throws InvalidInputException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			return br.readLine();
		}
		catch(Exception e) { throw new InvalidInputException("Erreur : Saisie incorrect"); }
	}

	public int inputInt() throws InvalidInputException {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			return Integer.parseInt(br.readLine());
		}
		catch(Exception e) { throw new InvalidInputException("\nErreur : Saisie d'entier incorrect"); }
	}

}

*/
