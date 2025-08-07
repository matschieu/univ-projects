
package com.github.matschieu.gol.loader;

import java.io.*;
import java.net.*;
import javax.swing.*;

import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

/**
 * @author Matschieu
 */
public class XMLLoader {
	
	public static final XMLLoader SINGLETON = new XMLLoader();
	
	public Document load(String filename) throws Exception {
		URL url = ((URLClassLoader)this.getClass().getClassLoader()).findResource(filename);
		SAXBuilder builder = new SAXBuilder(true);
		if (url == null) 
			return builder.build(filename);
		return builder.build(url);
	}
	
	public void save(Document doc, String filename) throws IOException {
		XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc, new FileOutputStream(new File(filename)));
	}
	
}