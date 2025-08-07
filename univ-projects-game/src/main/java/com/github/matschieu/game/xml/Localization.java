
package com.github.matschieu.game.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.github.matschieu.game.strategy.HumanStrategy;

/**
 * @author Matschieu
 */
public class Localization {

	public static final String FR = "Francais";
	public static final String EN = "English";
	public static final String DIRECTORY= "lang/";
	public static final String EXTENSION = ".xml";

	public static final Localization SINGLETON = new Localization();

	private final Properties languageProperties;
	private final String language;

	/**
	 * Constructs a new localization<br />
	 * Language loaded by default is English
	 */
	private Localization() {
		this(DIRECTORY + EN + EXTENSION);
	}

	/**
	 * Constructs a new localization
	 * @param language : Language loaded
	 */
	private Localization(String language) {
		this.languageProperties = new Properties();
		this.loadLanguageFile(language);
		this.language = language;
	}

	/**
	 * Returns the language used in the game
	 * @return  String : the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * Loads a language's file (XML) in the game
	 * @param language : Language loaded
	 */
	public void loadLanguageFile(String language){
		try {
			languageProperties.loadFromXML(Thread.currentThread().getContextClassLoader().getResourceAsStream(language));
		}
		catch (final InvalidPropertiesFormatException e) {
			System.err.println(e.getMessage());
		}
		catch (final IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Records the language used in a file (XML)
	 * @param language
	 */
	public void SaveLanguageFile(String language) {
		final File file = new File(language);
		try {
			file.createNewFile();
			final FileOutputStream outputStream = new FileOutputStream(file);
			languageProperties.storeToXML(outputStream, null);
			outputStream.close();
		}
		catch (final IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Chooses the language to be used in the game
	 */
	public void selectLanguage() {
		final String[] langFiles = (new File(Thread.currentThread().getContextClassLoader().getResource(DIRECTORY).getFile())).list();
		final String ext = ".xml";
		final List<String> xmlFiles = new LinkedList<>();
		System.out.println(Localization.SINGLETON.getElement("LOCALISATION_SELECTLANGUAGE") + " : ");
		for(int i = 0; i < langFiles.length; i++) {
			if (langFiles[i].matches("[a-zA-Z0-9]*" + ext)) {
				final String tmp = langFiles[i].substring(0, langFiles[i].length() - ext.length());
				xmlFiles.add(tmp);
			}
		}
		this.loadLanguageFile(DIRECTORY + (new HumanStrategy()).choiceString(xmlFiles) + EXTENSION);
	}

	/**
	 * Returns the word corresponding to the key according to the language used in the game<br />
	 * If there is no word corresponding to the key return a string empty
	 * @param word : the key
	 * @return String : the string corresponding to the key
	 */
	public String getElement(String word) {
		final String str = this.languageProperties.getProperty(word);
		if (str == null) return "";
		return str;
	}

	/**
	 * If the key exists, it's swapped, else it's created
	 * @param key
	 * @param value
	 */
	public void setElement(String key, String value) {
		this.languageProperties.setProperty(key, value);
	}

}
