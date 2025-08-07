
package com.github.matschieu.gol.config;

/**
 * @author Matschieu
 */
public class Config extends PropertiesLoader {
	
	public static final Config SINGLETON = new Config();
	
	private Config() {
		this("config/configs.xml");
	}

	private Config(String filename) {
		super(filename);
	}
	
}