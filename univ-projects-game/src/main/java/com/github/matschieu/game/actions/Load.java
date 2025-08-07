
package com.github.matschieu.game.actions;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.game.OnePlayerGame;
import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Load implements Action {

	/** The character who executes the action */
	private final GameCharacter character;

	/**
	 * Creates a new load game action
	 * @param character : the character who executes the action
	 */
	public Load(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Load a game
	 * @return true
	 */
	@Override
	public boolean execute() {
		final String ext = ".xml";
		final String[] configFiles = (new File(Thread.currentThread().getContextClassLoader().getResource("./config").getFile())).list();
		final List<String> files = new LinkedList<>();
		for(int i = 0; i < configFiles.length; i++)
			if (configFiles[i].matches("[a-zA-Z0-9]*" + ext)) files.add(configFiles[i]);
		final String ans = this.character.getStrategy().choiceString(files);
		System.out.println();
		(new OnePlayerGame()).play("./config/" + ans);
		System.exit(0);
		return true;
	}

	/**
	 * Checks if this action is available for a character
	 * @return true
	 */
	@Override
	public boolean isAvailable() {
		return true;
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("LOAD_GETDESCRIPTION");
	}

}
