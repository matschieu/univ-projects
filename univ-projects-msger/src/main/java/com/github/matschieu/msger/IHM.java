package com.github.matschieu.msger;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class IHM extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextArea messages;
	private JTextArea input;

	public IHM() {
		messages = new JTextArea(10, 10);
		messages.setEditable(false);

		input = new JTextArea(10, 10);

		setLayout(new BorderLayout());
		add(new JScrollPane(messages), BorderLayout.CENTER);
		add(new JScrollPane(input), BorderLayout.SOUTH);
	}

	public void setMessage(String str) {
		messages.setText(str);
	}

	public String getMessages() {
		return messages.getText();
	}

	public void appendMessage(String str) {
		messages.setText(messages.getText() + "\n" + str);
	}

	public static void main(String arg[]) {
		IHM ihm = new IHM();
		JFrame frame = new JFrame("TOTO");
		frame.add(ihm);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}

}
