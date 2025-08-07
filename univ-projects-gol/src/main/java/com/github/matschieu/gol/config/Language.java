
package com.github.matschieu.gol.config;

/**
 * @author Matschieu
 */
public class Language extends PropertiesLoader {

	public static final Language SINGLETON = new Language();

	private Language() {
		this("config/lang/francais.xml");
	}

	private Language(String filename) {
		super(filename);
	}

}
