
package com.github.matschieu.scxml.ihm;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.github.matschieu.scxml.Robot;
import com.github.matschieu.scxml.RobotStateMachine;

/**
 *
 * @author mathieu
 */
public class ControlPanel extends JPanel implements Observer {

	private Robot stateMachine;

	private JButton moveNorthButton;
	private JButton moveSouthButton;
	private JButton moveEastButton;
	private JButton moveWestButton;
	private JButton turnOnButton;
	private JButton turnOffButton;
	private JButton reloadButton;

	/**
	 * Constructor
	 * @param stateMachine
	 */
	public ControlPanel(Robot stateMachine) {
		this.stateMachine = stateMachine;
		
		this.moveNorthButton = new JButton("Nord");
		this.moveSouthButton = new JButton("Sud");
		this.moveEastButton = new JButton("Est");
		this.moveWestButton = new JButton("Ouest");
		this.turnOnButton = new JButton("Allumer");
		this.turnOffButton = new JButton("Eteindre");
		this.reloadButton = new JButton("Recharger");

		this.moveNorthButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_MOVE_NORTH));
		this.moveSouthButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_MOVE_SOUTH));
		this.moveEastButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_MOVE_EAST));
		this.moveWestButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_MOVE_WEST));
		this.turnOnButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_TURN_ON));
		this.turnOffButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_TURN_OFF));
		this.reloadButton.addActionListener(new ButtonListener(RobotStateMachine.EVENT_RELOAD));

		this.setPreferredSize(new Dimension(110, 100));
		this.repaint();
	}

	@Override
	public void repaint() {

		if (this.stateMachine == null) {
			super.repaint();
			return;
		}

		this.removeAll();

		if (this.stateMachine.getRobotState() == RobotStateMachine.ROBOT_STATE.ON) {
			this.setLayout(new GridLayout(5, 1, 10, 10));
			this.add(this.moveNorthButton);
			this.add(this.moveSouthButton);
			this.add(this.moveEastButton);
			this.add(this.moveWestButton);
			this.add(this.turnOffButton);
		}
		else {
			this.setLayout(new GridLayout(2, 1, 10, 10));
			if (this.stateMachine.getBatteryState() == RobotStateMachine.BATTERY_STATE.EMPTY) {
				this.turnOnButton.setEnabled(false);
				this.reloadButton.setEnabled(true);
			}
			else {
				this.turnOnButton.setEnabled(true);
				this.reloadButton.setEnabled(false);
			}
			this.add(this.turnOnButton);
			this.add(this.reloadButton);
		}

		//this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.revalidate();
		super.repaint();
	}

	/**
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		this.repaint();
	}

	class ButtonListener implements ActionListener {

		private String event;

		public ButtonListener(String event) {
			this.event = event;
		}

		public void actionPerformed(ActionEvent e) {
			stateMachine.fireEvent(event);
		}
		
	}

}
