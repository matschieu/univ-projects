
package com.github.matschieu.game.strategy;

import java.util.List;
import java.util.Map;

import com.github.matschieu.game.actions.Action;
import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.map.Cell;
import com.github.matschieu.game.map.Direction;

/**
 *@author Matschieu
 */
public interface Strategy {

	/**
	 * Strategy to choose a string in a list
	 * @param values : a list of strings
	 * @return String : the string selected
	 */
	public String choiceString(List<String> values);

	/**
	 * Strategy to choose an action in a list
	 * @param actions : a list of actions available
	 * @return Action : the action selected
	 */
	public Action choiceAction(List<Action> actions);

	/**
	 * Strategy to choose a character in a list
	 * @param chars : a list of characters available
	 * @return GameCharacter : the character selected
	 */
	public GameCharacter choiceGameCharacter(List<GameCharacter> chars);

	/**
	 * Strategy to choose an item in a list
	 * @param items : a list of items available
	 * @return Item : the item selected
	 */
	public Item choiceItem(List<Item> items);

	/**
	 * Strategy to choose a cell in a list
	 * @param cells : a list of cells available
	 * @return Cell<? extends Direction> : the cell selected
	 */
	public Cell<? extends Direction> choice(Map<? extends Direction, Cell<? extends Direction>> cells);

}
