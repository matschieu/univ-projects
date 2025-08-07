package com.github.matschieu.maths;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
*@author Matschieu
*/
public class Report extends JPanel {

	private static final String EOL = System.getProperty("line.separator");

	private JTextArea textArea;

	public Report() { this(8, 50); }
	public Report (int rows, int cols) {
		setLayout(new GridLayout(1, 1));
		textArea = new JTextArea(rows, cols);
		textArea.setForeground(new Color(200, 0, 0));
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setAutoscrolls(true);
		add(scrollPane);
	}

	public void append(String str) {
		StringBuffer tmp = new StringBuffer("");
		tmp.append((new Date()).toString());
		tmp.append(" : ");
		tmp.append(str);
		textArea.append(tmp.toString() + EOL);
	}

}