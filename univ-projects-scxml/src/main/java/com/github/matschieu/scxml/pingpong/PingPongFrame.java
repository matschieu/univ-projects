package com.github.matschieu.scxml.pingpong;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.github.matschieu.scxml.log.StaticTextAreaLog;

/**
 *
 * @author mathieu
 */
public class PingPongFrame extends JFrame {

	private PingPong pp;

	public PingPongFrame(PingPong pp_) {
		this.pp = pp_;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		JButton pongButton = new JButton("Pong");
		pongButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				pp.fireEvent(PingPong.EVENT_PONG);
			}

		});
		JButton pingButton = new JButton("Ping");
		pingButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				pp.fireEvent(PingPong.EVENT_PING);
			}
		});

		JTextArea ta = StaticTextAreaLog.textArea;
		ta.setPreferredSize(new Dimension(900, 500));
		ta.setEditable(false);

		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1, 2));
		p.add(pongButton);
		p.add(pingButton);

		this.add(new JScrollPane(ta), BorderLayout.CENTER);
		this.add(p, BorderLayout.SOUTH);

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String [] args) {
		new PingPongFrame(new PingPong());
	}

}
