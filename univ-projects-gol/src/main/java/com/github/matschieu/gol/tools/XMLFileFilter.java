
package com.github.matschieu.gol.tools;

import java.io.*;

import com.github.matschieu.gol.config.*;

/**
 * @author Matschieu
 */
public class XMLFileFilter extends javax.swing.filechooser.FileFilter {
	
	public boolean accept(File f) {
		String filename = f.getName();
		int l = filename.length();
		if (l > 4 && filename.substring(l - 4, l).toUpperCase().equals(".XML") || f.isDirectory())
			return true;
		return false;
	}

	public String getDescription() {
		return Language.SINGLETON.getElement("XML_FILE_FILTER_DESC");
	}

}