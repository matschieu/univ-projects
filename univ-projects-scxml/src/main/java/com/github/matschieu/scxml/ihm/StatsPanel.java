
package com.github.matschieu.scxml.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.matschieu.scxml.Robot;
import com.github.matschieu.scxml.RobotStateMachine;

/**
 *
 * @author mathieu
 */
public class StatsPanel extends JPanel implements Observer {

	private Robot stateMachine;
	private JLabel batteryLabel;
	private JLabel pointLabel;

	/**
	 * Constructor
	 * @param stateMachine
	 */
	public StatsPanel(Robot stateMachine) {
		this.stateMachine = stateMachine;

		BatteryPanel batteryPanel = new BatteryPanel(stateMachine);
		batteryPanel.setPreferredSize(new Dimension(50, 25));

		this.batteryLabel = new JLabel();
		this.pointLabel = new JLabel();

		this.add(this.batteryLabel);
		this.add(batteryPanel);
		this.add(this.pointLabel);

		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setPreferredSize(new Dimension(170, 100));
		this.repaint();
	}

	@Override
	public void repaint() {
		super.repaint();
		
		if(this.stateMachine != null) {
			if (this.stateMachine.getBatteryLevel() == 0)
				this.batteryLabel.setForeground(new Color(255, 0, 0));
			else
				this.batteryLabel.setForeground(new Color(0, 0, 0));
			this.batteryLabel.setText("Batterie : " + this.stateMachine.getBatteryLevel() + "%");
			if (this.stateMachine.getRobotState() == RobotStateMachine.ROBOT_STATE.ON)
				this.pointLabel.setText("Position : (x:" + (int)this.stateMachine.getPosition().getX() + " ; y:" + (int)this.stateMachine.getPosition().getY() + ")");
			else
				this.pointLabel.setText("Robot éteint");
		}
	}

	/**
	 * 
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		this.repaint();
	}

}
