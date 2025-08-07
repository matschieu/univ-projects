
package com.github.matschieu.game.strategy;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.matschieu.game.actions.Action;
import com.github.matschieu.game.actions.Wait;
import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.map.Cell;
import com.github.matschieu.game.map.Direction;

/**
 *@author Matschieu
 */
public class RandomStrategy implements Strategy {

	/**
	 * Constructs a new Random strategy (random choice) used by monsters
	 */
	public RandomStrategy() { }

	/**
	 * Returns a random choic
	 * @param limit : the maximum number to choose (the minimum is 0)
	 * @return int : a random choice (between 0 and limit included)
	 */
	private int randomChoice(int limit) {
		return (int)(Math.random() * limit);
	}

	/**
	 * Strategy to choose a string in a list
	 * @param values : a list of strings
	 * @return String : the string selected
	 */
	@Override
	public String choiceString(List<String> values) {
		if (values == null || values.isEmpty()) return null;
		return values.get(randomChoice(values.size()));
	}

	/**
	 * Strategy to choose an action in a list
	 * @param actions : a list of actions available
	 * @return Action : the action selected
	 */
	@Override
	public Action choiceAction(List<Action> actions) {
		if (actions == null || actions.isEmpty()) return new Wait();
		List<Action> tmp = new LinkedList<Action>();
		Iterator<Action> it = actions.iterator();
		while(it.hasNext()) {
			Action a = it.next();
			if (a.isAvailable()) tmp.add(a);
		}
		return tmp.get(randomChoice(tmp.size()));
	}

	/**
	 * Strategy to choose a character in a list
	 * @param chars : a list of characters available
	 * @return GameCharacter : the character selected
	 */
	@Override
	public GameCharacter choiceGameCharacter(List<GameCharacter> chars) {
		if (chars == null || chars.isEmpty()) return null;
		List<GameCharacter> tmp = new LinkedList<GameCharacter>();
		Iterator<GameCharacter> it = chars.iterator();
		while(it.hasNext()) {
			GameCharacter c = it.next();
			tmp.add(c);
		}
		return tmp.get(randomChoice(tmp.size()));
	}

	/**
	 * Strategy to choose an item in a list
	 * @param items : a list of items available
	 * @return Item : the item selected
	 */
	@Override
	public Item choiceItem(List<Item> items) {
		if (items == null || items.isEmpty()) return null;
		return items.get(randomChoice(items.size()));
	}

	/**
	 * Strategy to choose a cell in a list
	 * @param cells : a list of cells available
	 * @return Cell<? extends Direction> : the cell selected
	 */
	@Override
	public Cell<? extends Direction> choice(Map<? extends Direction, Cell<? extends Direction>> cells) {
		if (cells == null || cells.isEmpty()) return null;
		List<Cell<? extends Direction>> tmp = new LinkedList<Cell<? extends Direction>>();
		Iterator<? extends Direction> it = cells.keySet().iterator();
		while(it.hasNext()) {
			Direction d = it.next();
			Cell<? extends Direction> c = cells.get(d);
			if (c.isReachable()) tmp.add(c);
		}
		return tmp.get(randomChoice(tmp.size()));
	}

}
