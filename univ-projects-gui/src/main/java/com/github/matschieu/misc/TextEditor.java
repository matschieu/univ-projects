package com.github.matschieu.misc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * @author Matschieu
 */
public class TextEditor {

	public TextEditor() {
		JFrame frame = new JFrame("TextEditor");

		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem("New"));
		fileMenu.add(new JMenuItem("Open"));
		fileMenu.add(new JMenuItem("Save"));
		fileMenu.add(new JMenuItem("Save as"));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem("Print"));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem("Quit"));

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(new JMenu("Edit"));
		menuBar.add(new JMenu("Format"));

		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		searchPanel.add(new JLabel("Find : "));
		searchPanel.add(new JTextField(10));
		searchPanel.add(new JButton("<="));
		searchPanel.add(new JButton("=>"));
		searchPanel.add(new JButton("Highlight all"));

		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(new JTextArea(), BorderLayout.CENTER);
		frame.add(searchPanel, BorderLayout.SOUTH);
		frame.setPreferredSize(new Dimension(800, 500));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); }
		catch (Exception e) { }
		new TextEditor();
	}

}
