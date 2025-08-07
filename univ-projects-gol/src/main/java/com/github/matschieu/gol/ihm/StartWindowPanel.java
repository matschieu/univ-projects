
package com.github.matschieu.gol.ihm;

import java.awt.*;
import javax.swing.*;

/**
 * @author Matschieu
 */
public class StartWindowPanel extends JPanel {

	private ImageIcon image;
	
	public StartWindowPanel(ImageIcon image) {
		this.image = image;
		this.setPreferredSize(new Dimension(this.image.getIconWidth(), this.image.getIconHeight()));
	}
	
	public ImageIcon getImage() {
		return this.image;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(this.image.getImage(), 0, 0, null);
	}
	
}