package com.github.matschieu.maths;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/**
*@author Matschieu
*/
public class MatrixOptionPane extends JOptionPane {

	private Color defaultBGOP;
	private Color defaultBGP;
	private Color defaultBGB;
	private Color defaultFGB;
	private final String title = ".:Enter the Matrix:.";
	private final String msg = "Suivez le lapin blanc,\net entrez dans la matrice...";
	private final ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("lapin.png"));

	public MatrixOptionPane (Color bg, Color fg) {
		JTextArea ta = new JTextArea(msg);
		ta.setBackground(bg);
		ta.setForeground(fg);
		changeColor(bg, fg);
		showMessageDialog(null, ta, title, JOptionPane.INFORMATION_MESSAGE, img);
		restore();
	}

	private void changeColor(Color bg, Color fg) {
		defaultBGOP = UIManager.getColor("OptionPane.background");
		defaultBGP = UIManager.getColor("Panel.background");
		defaultBGB = UIManager.getColor("Button.background");
		defaultFGB = UIManager.getColor("Button.foreground");
		UIManager.put("OptionPane.background", bg);
		UIManager.put("Panel.background", bg);
		UIManager.put("Button.background", bg);
		UIManager.put("Button.foreground", fg);
	}

	private void restore() {
		UIManager.put("OptionPane.background", defaultBGOP);
		UIManager.put("Panel.background", defaultBGP);
		UIManager.put("Button.background", defaultBGB);
		UIManager.put("Button.foreground", defaultFGB);
	}

}