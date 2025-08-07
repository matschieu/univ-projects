package com.github.matschieu.scxml.pingpong;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author mathieu
 */
public class PingPongButtonsFrame extends JFrame {

	private PingPong pp;

	public PingPongButtonsFrame(PingPong pp_) {
		this.pp = pp_;

		this.setLayout(new GridLayout(2, 1));

		JButton pongButton = new JButton("Pong");
		JButton pingButton = new JButton("Ping");

		this.add(pongButton);
		this.add(pingButton);

		pongButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pp.fireEvent(PingPong.EVENT_PONG);
			}
		});
		pingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pp.fireEvent(PingPong.EVENT_PING);
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	public static void main(String [] args) {
		new PingPongButtonsFrame(new PingPong());
	}

}
