
package com.github.matschieu.gol.config;

import java.io.*;
import java.util.*;

/**
 * @author Matschieu
 */
public class PropertiesLoader extends Observable {

	protected Properties properties;
	protected String filename;

	public PropertiesLoader(String filename) {
		this.properties = new Properties();
		this.filename = filename;
		this.loadFile(filename);
	}
	
	public String getFilename() {
		return this.filename;
	}

	public void loadFile(String filename){
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(filename);
			if (inputStream == null)
				inputStream = new FileInputStream(filename);
			this.properties.loadFromXML(inputStream);
			inputStream.close();
		}
		catch (Exception e) {
			System.err.println("Error, invalid properties file (" + filename + ")");
			System.exit(1);
		}
	}

	public String getElement(String word) {
		String str = this.properties.getProperty(word);
		if (str == null) return "";
		return str;
	}

}
