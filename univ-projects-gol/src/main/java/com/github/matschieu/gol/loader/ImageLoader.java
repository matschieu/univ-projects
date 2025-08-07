
package com.github.matschieu.gol.loader;

import java.net.*;
import javax.swing.*;

/**
 * @author Matschieu
 */
public class ImageLoader {
	
	public static final ImageLoader SINGLETON = new ImageLoader();
	
	public ImageIcon load(String filename) {
		URL fileLoc = this.getClass().getClassLoader().getResource(filename);
		if (fileLoc == null) 
			return new ImageIcon(filename);
		return new ImageIcon(fileLoc);
	}
	
}