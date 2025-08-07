
package com.github.matschieu.game;

/**
 *@author Matschieu
 */
public interface Game {

	/**
	 * Play the game (main loop)
	 */
	public void play();
	
	/**
	 * Play the game (main loop)<br />
	 * The game uses the configuration defines in the config file
	 * @param configFileName : the config file name
	 */
	public void play(String configFileName);

}
