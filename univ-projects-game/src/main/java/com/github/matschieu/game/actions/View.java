
package com.github.matschieu.game.actions;

import java.util.Iterator;
import java.util.Map;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.items.Item;
import com.github.matschieu.game.map.Cell;
import com.github.matschieu.game.map.Direction;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class View implements Action, HeroAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new view action
	 * @param character : the character who executes the action
	 */
	public View(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * View all neighbours cell of the character's current cell (characters, items and type
	 * @return false
	 */
	@Override
	public boolean execute() {
		StringBuffer strBuf = new StringBuffer();
		Map<? extends Direction, Cell<? extends Direction>> cells = this.character.getCell().getNeighboursMap();
		Iterator<? extends Direction> it = cells.keySet().iterator();
		String display = Localization.SINGLETON.getElement("VIEW_EXECUTE_1");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		strBuf.append(display + "\n");
		while(it.hasNext()) {
			Iterator<?> itr;
			Direction d = it.next();
			Cell<? extends Direction> cell = cells.get(d);
			display = Localization.SINGLETON.getElement("VIEW_EXECUTE_2");
			display = display.replace("[[CELL_DESC]]", cell.getDescription());
			display = display.replace("[[DIRECTION]]", "" + d);
			strBuf.append(display);
			if (!cell.isReachable()) strBuf.append(Localization.SINGLETON.getElement("VIEW_EXECUTE_3"));
			if (cell.isReachable()) {
				itr = cell.getItems().iterator();
				if (itr.hasNext()) {
					strBuf.append(Localization.SINGLETON.getElement("VIEW_EXECUTE_4") + " : ");
					while(itr.hasNext()) {
						strBuf.append(((Item)itr.next()).getDescription());
						if (itr.hasNext()) strBuf.append(", ");
					}
				}
				else strBuf.append(Localization.SINGLETON.getElement("VIEW_EXECUTE_5"));
				strBuf.append(" | ");
				itr = cell.getCharacters().iterator();
				if (itr.hasNext()) {
					strBuf.append(Localization.SINGLETON.getElement("VIEW_EXECUTE_6") + " : ");
					while(itr.hasNext()) {
						strBuf.append(((GameCharacter)itr.next()).getDescription());
						if (itr.hasNext()) strBuf.append(", ");
					}
				}
				else strBuf.append(Localization.SINGLETON.getElement("VIEW_EXECUTE_7"));
			}
			strBuf.append("\n");
		}
		System.out.print(strBuf.toString());
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
		return Localization.SINGLETON.getElement("VIEW_GETDESCRIPTION");
	}

}

