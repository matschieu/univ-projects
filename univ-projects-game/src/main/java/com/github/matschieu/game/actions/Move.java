
package com.github.matschieu.game.actions;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.map.Cell;
import com.github.matschieu.game.map.Direction;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class Move implements Action, HeroAction, MonsterAction {

	/** The character who executes the action */
	private GameCharacter character;

	/**
	 * Creates a new move action
	 * @param character : the character who executes the action
	 */
	public Move(GameCharacter character) {
		this.character = character;
	}

	/**
	 * Executes the action<br />
	 * Move to a neighbours cell of the character's current cell
	 * @return true if the action consume a tour else false
	 */
	@Override
	public boolean execute() {
		Cell<? extends Direction> currentCell = this.character.getCell();
		Map<? extends Direction, Cell<? extends Direction>> cells = this.character.getCell().getNeighboursMap();
		Cell<? extends Direction> newCell = this.character.getStrategy().choice(cells);
		if (newCell == null) return false;
		currentCell.getCharacters().remove(this.character);
		newCell.getCharacters().add(this.character);
		this.character.setCell(newCell);
		this.character.setEnergy(this.character.getEnergy() - newCell.energyLost());
		String display = Localization.SINGLETON.getElement("MOVE_EXECUTE");
		display = display.replace("[[CHAR_NAME]]", this.character.getName());
		display = display.replace("[[CELL_DESC]]", newCell.getDescription());
		display = display.replace("[[CELL_ENERGY]]", "" + newCell.energyLost());
		display = display.replace("[[CHAR_ENERGY]]", "" + this.character.getEnergy());
		System.out.println(display);
		if (this.character.getEnergy() < 1) this.character.die();
		return true;
	}

	/**
	 * Checks if this action is available for a character (if there is a reachable field around the character)
	 * @return true if the action is available else false
	 */
	@Override
	public boolean isAvailable() {
		List<Cell<? extends Direction>> cells = this.character.getCell().getAllNeighbours();
		Iterator<Cell<?>> it = cells.iterator();
		while(it.hasNext()) {
			Cell<? extends Direction> c = it.next();
			if (c.isReachable()) return true;
		}
		return false;
	}

	/**
	 * Returns a string representation of this action
	 * @return String : a string representation of this action
	 */
	@Override
	public String getDescription() {
		return Localization.SINGLETON.getElement("MOVE_GETDESCRIPTION");
	}

}
