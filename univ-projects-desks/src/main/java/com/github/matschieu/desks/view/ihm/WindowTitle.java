package com.github.matschieu.desks.view.ihm;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Matschieu
 */
public class WindowTitle extends JPanel {
	
	public static final Color TITLE_BACKGROUND = new Color(151, 191, 230);
	public static final Color TITLE_FOREGROUND = Color.black;
	
	public WindowTitle(String title) {
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		this.setBackground(TITLE_BACKGROUND);
		JLabel label = new JLabel(title);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setAlignmentY(CENTER_ALIGNMENT);
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 15));
		label.setForeground(TITLE_FOREGROUND);
		this.add(label);
	}
	
}
