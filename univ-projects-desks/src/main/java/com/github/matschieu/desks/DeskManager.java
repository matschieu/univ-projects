package com.github.matschieu.desks;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.jdom.JDOMException;

import com.github.matschieu.desks.model.XMLDataBase;
import com.github.matschieu.desks.view.ihm.MainFrame;

/**
 * @author Matschieu
 */
public class DeskManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XMLDataBase db;
		try {
			String dir = "";
			if (DeskManager.class.getClassLoader().getResource(".") != null) {
				dir = DeskManager.class.getClassLoader().getResource(".").getPath();
			}
			db = new XMLDataBase(dir + XMLDataBase.DEFAULT_XML_DESK_FILE, dir + XMLDataBase.DEFAULT_XML_PERS_FILE, dir + XMLDataBase.DEFAULT_XML_RESV_FILE);
		}
		catch(JDOMException e) {
			JOptionPane.showMessageDialog(null, "Fichier base de données mal formé\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			db = new XMLDataBase();
			db.createDataBase();
			JOptionPane.showMessageDialog(null, "Nouvelle base créée :\n" + db.getXmlDeskFile() + "\n" + db.getXmlPersFile() + "\n" + db.getXmlResvFile(), "", JOptionPane.INFORMATION_MESSAGE);
		}
		catch(IOException e) {
			JOptionPane.showMessageDialog(null, "Fichier base de données introuvable\n" + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			db = new XMLDataBase();
			db.createDataBase();
			JOptionPane.showMessageDialog(null, "Nouvelle base créée :\n" + db.getXmlDeskFile() + "\n" + db.getXmlPersFile() + "\n" + db.getXmlResvFile(), "", JOptionPane.INFORMATION_MESSAGE);
		}
		new MainFrame(db);
	}
}
