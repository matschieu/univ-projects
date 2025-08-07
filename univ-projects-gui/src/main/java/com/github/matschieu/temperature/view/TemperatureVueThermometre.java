package com.github.matschieu.temperature.view;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.matschieu.temperature.controller.TemperatureController;
import com.github.matschieu.temperature.model.TemperatureModel;

/**
 * @author Matschieu
 */
public class TemperatureVueThermometre implements TemperatureVue, Observer {

	private TemperatureModel model;
	private TemperatureController controller;

	private JFrame temperatureJFrame;
	private JSlider temperatureSlider;

	public TemperatureVueThermometre(TemperatureModel model,
			TemperatureController controller, int posX, int posY) {
		this.model = model;
		this.controller = controller;
		this.temperatureJFrame = new JFrame("Thermomètre Celsius");
		this.temperatureSlider = new JSlider(JSlider.VERTICAL, -20, 100, (int)model.getC());
		this.temperatureSlider.setMajorTickSpacing(10);
		this.temperatureSlider.setMinorTickSpacing(1);
		this.temperatureSlider.setPaintTicks(true);
		this.temperatureSlider.setPaintLabels(true);
		this.temperatureJFrame.add(this.temperatureSlider);
		this.model.addObserver(this);
		this.temperatureJFrame.pack();
		temperatureJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.temperatureJFrame.setLocation(posX, posY);
		this.temperatureJFrame.setResizable(false);
		this.temperatureJFrame.setVisible(true);
		this.temperatureSlider.addChangeListener(
				new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) { notifyController(); }
				}
		);
	}

	private void notifyController() {
		this.controller.fixeDegresC(this.temperatureSlider.getValue());
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.temperatureSlider.setValue((int)this.model.getC());
	}

	@Override
	public void disableWarningColor() {
		this.temperatureSlider.setBackground(new Color(238, 238, 238));
	}

	@Override
	public void enableWarningColor() {
		this.temperatureSlider.setBackground(Color.RED);
	}

	@Override
	public double getDisplay() {
		return this.temperatureSlider.getValue();
	}

}
