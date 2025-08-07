
package com.github.matschieu.game.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Use implements Action, HeroAction, MonsterAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new use action
	 * @param character : the character who executes the action
	 */
	public Use(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Use an item contained by character's bag
	 * @return true if the action consume a tour else false
	 */
	@Override
	public boolean execute() {
		List<Item> items = new LinkedList<Item>();
		Iterator<Item> it = this.character.getBag().iterator();
		while(it.hasNext()) {
			Item i = it.next();
			items.add(i);
		}
		Item item2Use = this.character.getStrategy().choiceItem(items);
		if (item2Use == null) return false;
		this.character.use(item2Use);
		String display = Localization.SINGLETON.getElement("USE_EXECUTE");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		display = display.replace("[[ITEM_DESC]]", item2Use.getDescription());
		System.out.println(display);

		return true;
	}

	/**
	 * Checks if this action is available for a character (if the character's bag isn't empty)
	 * @return true if the action is available else false
	 */
	@Override
	public boolean isAvailable() {
		return !this.character.getBag().isEmpty();
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("USE_GETDESCRIPTION");
	}

}
