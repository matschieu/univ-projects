
package com.github.matschieu.gol.ihm;

import java.awt.*;
import java.net.*;
import javax.swing.*;

import com.github.matschieu.gol.loader.*;

/**
 * @author Matschieu
 */
public class StartWindow extends JWindow {

	private int pause;
	
	public StartWindow(String filename, String message, int pause) {
		ImageIcon image = ImageLoader.SINGLETON.load(filename);
		StartWindowPanel panel = new StartWindowPanel(image);
		JLabel label = new JLabel(message);
		label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		this.pause = pause;
		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		this.add(label, BorderLayout.SOUTH);
		this.setSize(image.getIconWidth(), image.getIconHeight());
		this.setLocationRelativeTo(null);
	}
	
	public void display() {
		this.setVisible(true);
		try { Thread.sleep(this.pause); }
		catch(Exception e) { }
		this.setVisible(false);
	}
	
}