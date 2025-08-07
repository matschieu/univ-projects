
package com.github.matschieu.game.actions;

import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Options implements Action, HeroAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new options action
	 * @param character : the character who executes the action
	 */
	public Options(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Select an option
	 * @return false
	 */
	@Override
	public boolean execute() {
		List<Action> acts = new LinkedList<Action>();
		acts.add(new NoOption());
		acts.add(new ChangeHeroName(this.character));
		acts.add(new SelectLanguage());
		acts.add(new NewGame());
		acts.add(new Load(this.character));
		Action a = this.character.getStrategy().choiceAction(acts);
		if (a != null) {
			System.out.println(a.getDescription());
			a.execute();
		}
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
		return Localization.SINGLETON.getElement("OPTIONS_GETDESCRIPTION");
	}

}
