
package com.github.matschieu.game;

import java.util.Iterator;
import java.util.List;

import com.github.matschieu.game.characters.GameCharacter;
import com.github.matschieu.game.map.GameMap;
import com.github.matschieu.game.map.GameMap4;
import com.github.matschieu.game.map.GameMap8;
import com.github.matschieu.game.xml.GameConfiguration;
import com.github.matschieu.game.xml.Localization;

/**
 *@author Matschieu
 */
public class OnePlayerGame {

	/**
	 * Play the game (main loop)
	 */
	public void play() {
		this.play4();
	}

	/**
	 * Play the game (main loop)<br />
	 * Use a GameMap4
	 */
	public void play4() {
		Localization.SINGLETON.selectLanguage();
		System.out.println();
		GameMap4 map = new GameMap4(10, 5);
		map.build();
		this.commonMethodPlay(map);
	}

	/**
	 * Play the game (main loop)<br />
	 * Use a GameMap8
	 */
	public void play8() {
		Localization.SINGLETON.selectLanguage();
		System.out.println();
		GameMap8 map = new GameMap8(10, 5);
		map.build();
		this.commonMethodPlay(map);
	}

	/**
	 * Play the game (main loop)<br />
	 * The game uses the configuration defines in the config file
	 * @param configFileName : the config file name
	 */
	public void play(String configFileName) {
		String display = Localization.SINGLETON.getElement("ONEPLAYERGAME_PLAY_1");
		display = display.replace("[[FILE_NAME]]", configFileName);
		System.out.print(display + "... ");
		GameMap map = (new GameConfiguration()).loadConfigFile(configFileName);
		System.out.println(Localization.SINGLETON.getElement("ONEPLAYERGAME_PLAY_2"));
		System.out.println();
		this.commonMethodPlay(map);
	}

	/**
	 * Main loop used by all play() methods
	 * @param map : the map (fully built) used in the game
	 */
	private void commonMethodPlay(GameMap map) {
		int idx = 1;
		List<GameCharacter> characters = map.getCharacters();
		GameCharacter hero = map.getHero();
		System.out.println(Localization.SINGLETON.getElement("ONEPLAYERGAME_COMMONMETHODPLAY_1"));
		while(hero.getEnergy() > 0) {
			System.out.println("\n** " + Localization.SINGLETON.getElement("ONEPLAYERGAME_COMMONMETHODPLAY_2") + " " + idx++ + "\n");
			Iterator<GameCharacter> it = characters.iterator();
			while(it.hasNext()) {
				GameCharacter gc = it.next();
				if (gc.getEnergy() > 0) gc.action();
			}
		}
		System.out.println("\n" + Localization.SINGLETON.getElement("ONEPLAYERGAME_COMMONMETHODPLAY_3"));
	}

	/**
	 * Display help & authors
	 */
	public void help() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(Localization.SINGLETON.getElement("GAME_NAME") + " :\n");
		strBuf.append("\t" + Localization.SINGLETON.getElement("ONEPLAYERGAME_HELP_1") + "\n\n");
		strBuf.append(Localization.SINGLETON.getElement("ONEPLAYERGAME_HELP_2") + " :\n");
		strBuf.append("\t" + Localization.SINGLETON.getElement("AUTHOR_1") + "\n\n");
		strBuf.append(Localization.SINGLETON.getElement("ONEPLAYERGAME_HELP_3") + " :\n");
		strBuf.append("\t" + Localization.SINGLETON.getElement("ONEPLAYERGAME_HELP_4") + "\n");
		strBuf.append("\t" + Localization.SINGLETON.getElement("ONEPLAYERGAME_HELP_5") + "\n");
		System.out.print(strBuf.toString());
	}

}
