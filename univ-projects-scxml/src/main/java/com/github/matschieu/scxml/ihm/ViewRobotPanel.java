
package com.github.matschieu.scxml.ihm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;

import com.github.matschieu.scxml.Robot;
import com.github.matschieu.scxml.RobotStateMachine;

/**
 *
 * @author mathieu
 */
public class ViewRobotPanel extends JPanel implements Observer {

	private static final int ZOOM = 5;

	private Robot stateMachine;

	/**
	 * Constructor
	 * @param stateMachine
	 */
	public ViewRobotPanel(Robot stateMachine) {
		this.stateMachine = stateMachine;

		Dimension d = this.stateMachine.getSpaceDimension();
		Dimension d_ = new Dimension((int)d.getWidth() * ZOOM, (int)d.getHeight() * ZOOM);

		this.setMaximumSize(d_);
		this.setPreferredSize(d_);
		this.setMinimumSize(d_);
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D)g;

		gr.setColor(new Color(0, 0, 0));
		gr.fillRect(0, 0, (int)this.stateMachine.getSpaceDimension().getWidth() * ZOOM, (int)this.stateMachine.getSpaceDimension().getHeight() * ZOOM);

		if (this.stateMachine.getRobotState() == RobotStateMachine.ROBOT_STATE.ON) {
			gr.setColor(new Color(0, 255, 0));
			gr.fillRect(
					(int)this.stateMachine.getPosition().getX() * ZOOM,
					(int)this.getPreferredSize().getHeight() - (((int)this.stateMachine.getPosition().getY() + 1 ) * ZOOM),
					ZOOM, ZOOM);
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
