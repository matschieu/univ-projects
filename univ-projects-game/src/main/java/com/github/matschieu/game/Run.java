package com.github.matschieu.game;

import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Run {

	/**
	 * Main method to run the game
	 */
	public static void main(String args[]) {
		OnePlayerGame game = new OnePlayerGame();
		System.out.println(Localization.SINGLETON.getElement("GAME_NAME"));
		System.out.println();
		if (args.length == 0) {
			game.play();
			System.exit(0);
		}
		if (args.length > 0) {
			for(int i = 0; i < args.length; i++)
				if (args[i].equals("-h")) {
					game.help();
					System.exit(0);
				}
			for(int i = 0; i < args.length; i++) {
				if (args[i].equals("-map4")) {
					game.play4();
					System.exit(0);
				}
				if (args[i].equals("-map8")) {
					game.play8();
					System.exit(0);
				}
			}
		}
		if (args.length == 1) {
			game.play(args[0]);
			System.exit(0);
		}
		System.err.println(Localization.SINGLETON.getElement("RUN_ERROR_1"));
		System.err.println(Localization.SINGLETON.getElement("RUN_ERROR_2"));
	}

}
