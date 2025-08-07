
package com.github.matschieu.game.actions;

import java.util.Scanner;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class ChangeHeroName implements Action {

	/** The character who executes the action */
	private final GameCharacter character;

	/**
	 * Creates a new change name action
	 * @param character : the character who executes the action
	 */
	public ChangeHeroName(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Change hero's name
	 * @return false
	 */
	@Override
	public boolean execute() {
		final Scanner sc = new Scanner(System.in);
		System.out.print(Localization.SINGLETON.getElement("GAMESQUAREDMAP_BUILD") + " : ");
		System.out.flush();
		String heroName = null;
		if (sc.hasNextLine()) heroName = sc.nextLine();
		this.character.setName(heroName);
		String display = Localization.SINGLETON.getElement("CHANGEHERONAME_EXECUTE");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		System.out.println(display);
		return false;
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
		return Localization.SINGLETON.getElement("CHANGEHERONAME_GETDESCRIPTION");
	}

}
