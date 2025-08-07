
package com.github.matschieu.gol;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.github.matschieu.gol.config.*;
import com.github.matschieu.gol.game.*;
import com.github.matschieu.gol.ihm.*;
import com.github.matschieu.gol.tools.*;

/**
 * @author Matschieu
 */
public class GameOfLife {

	public static void main(String[] args) {
		try {
			Icons.SINGLETON.loadFile(Config.SINGLETON.getElement("ICONS_FILE"));
			Language.SINGLETON.loadFile(Config.SINGLETON.getElement("LANGUAGE_FILE"));
			int w = Integer.parseInt(Config.SINGLETON.getElement("DEFAULT_WIDTH"));
			int h = Integer.parseInt(Config.SINGLETON.getElement("DEFAULT_HEIGHT"));
			int r = Integer.parseInt(Config.SINGLETON.getElement("DEFAULT_RATE"));
			(new StartWindow(Icons.SINGLETON.getElement("START_IMAGE"), null, 1000)).display();
			new GOLFrame(new GOLEnvironment(h, w, r), Language.SINGLETON.getElement("APPLICATION_NAME"));
		}catch(Exception e) { 
			System.err.println("Error...");
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

}
