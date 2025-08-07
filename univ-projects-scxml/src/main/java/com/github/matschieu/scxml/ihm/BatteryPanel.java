
package com.github.matschieu.scxml.ihm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import com.github.matschieu.scxml.Robot;

/**
 *
 * @author mathieu
 */
public class BatteryPanel extends JPanel {

	private Robot stateMachine;

	/**
	 * Constructor
	 * @param stateMachine
	 */
	public BatteryPanel(Robot stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D gr = (Graphics2D)g;
		Color chargedColor = new Color(0, 255, 0);
		Color unchargedColor = new Color(0, 0, 0);
		Color warningColor = new Color(255, 0, 0);
		int m = 5;
		int tmp = (int)(this.stateMachine.getBatteryLevel() * (this.getWidth() - m) / 100);

		if (this.stateMachine.getBatteryLevel() < 16)
			gr.setColor(warningColor);
		else
			gr.setColor(chargedColor);
		gr.fillRect(0, 0, tmp, this.getHeight());
		gr.setColor(unchargedColor);
		gr.fillRect(tmp, 0, (this.getWidth() - m) - tmp, this.getHeight());

		if (this.stateMachine.getBatteryLevel() == 100)
			gr.setColor(chargedColor);
		else
			gr.setColor(unchargedColor);
		gr.fillRect(this.getWidth() - m, m, m, this.getHeight() - 2 * m);

	}

}
