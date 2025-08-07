
package com.github.matschieu.gol.ihm;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * @author Matschieu
 */
public class Console extends JPanel implements Observer {
	
	public static final String EOL = System.getProperty("line.separator");
	
	private JTextArea textArea;
	
	public Console(int rows, int cols) {
		this.setLayout(new GridLayout());
		this.textArea = new JTextArea(rows, cols);
		this.textArea.setForeground(new Color(200, 0, 0));
		this.textArea.setBackground(new Color(238, 238, 238));
		this.textArea.setLineWrap(true);
		this.textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(this.textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setAutoscrolls(true);
		this.add(scrollPane);
	}

	public void append(String str) {
		StringBuffer tmp = new StringBuffer("");
		tmp.append((new Date()).toString());
		tmp.append(" : ");
		tmp.append(str);
		this.textArea.append(tmp.toString() + EOL);
	}

	public void update(Observable o, Object arg) {
		this.append((String)arg);
	}
	
}
