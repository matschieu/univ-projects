
package com.github.matschieu.game.actions;

import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.items.FullBagException;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class PickUp implements Action, HeroAction, MonsterAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new pick up action
	 * @param character : the character who executes the action
	 */
	public PickUp(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Pick up an object on the character's current cell
	 * @return true if the action consume a tour else false
	 */
	@Override
	public boolean execute() {
		List<Item> items = this.character.getCell().getItems();
		Item item2PickUp = this.character.getStrategy().choiceItem(items);
		if (item2PickUp == null) return false;
		try {
			this.character.getBag().add(item2PickUp);
			items.remove(item2PickUp);
			String display = Localization.SINGLETON.getElement("PICKUP_EXECUTE");
			display = display.replace("[[CHAR_NAME]]", this.character.getName());
			display = display.replace("[[ITEM_DESC]]", item2PickUp.getDescription());
			System.out.println(display);
		}
		catch(FullBagException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Checks if this action is available for a character (if there is an item on the cell where the character is)
	 * @return true if the action is available else false
	 */
	@Override
	public boolean isAvailable() {
		return !this.character.getCell().getItems().isEmpty() && !this.character.getBag().isFull();
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("PICKUP_GETDESCRIPTION");
	}

}
