
package com.github.matschieu.gol.config;

/**
 * @author Matschieu
 */
public class Icons extends PropertiesLoader {

	public static final Icons SINGLETON = new Icons();

	private Icons() {
		this("config/icons.xml");
	}

	private Icons(String filename) {
		super(filename);
	}

}
