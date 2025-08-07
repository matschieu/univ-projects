
package com.github.matschieu.scxml.ihm;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.github.matschieu.scxml.log.StaticTextAreaLog;

/**
 *
 * @author mathieu
 */
public class ConsolePanel extends JPanel {

	private JTextArea textArea;

	/**
	 * Constructor
	 * @param rows
	 * @param columns
	 */
	public ConsolePanel(int rows, int columns) {
		this.textArea = StaticTextAreaLog.textArea;
		this.textArea.setRows(rows);
		this.textArea.setColumns(columns);

		this.textArea.setEditable(false);
		this.textArea.setLineWrap(false);
		this.textArea.setForeground(new Color(255, 0, 0));

		JScrollPane scrollPane = new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setAutoscrolls(true);

		this.setLayout(new GridLayout(1, 1));
		this.add(scrollPane);
	}

}
