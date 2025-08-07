package com.github.matschieu.rgb.view;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.matschieu.rgb.controller.RGBController;
import com.github.matschieu.rgb.model.RGBModel;

/**
 * RGB slider panel to add to a frame
 * @author Matschieu
 */
public class RGBSliderPanel extends JPanel implements Observer, RGBView {
	
	private RGBModel model; // The model
	private RGBController ctlr; // The controller

	private JSlider rSlider; // Red slider
	private JSlider gSlider; // Green slider
	private JSlider bSlider; // Blue slider

	public RGBSliderPanel(RGBModel model, RGBController ctlr_) {
		// Initialize the model and controller
		this.model = model;
		this.model.addObserver(this);
		this.ctlr = ctlr_;
		// Initialize the sliders
		this.rSlider = new JSlider(0, 255, this.model.getR());
		this.gSlider = new JSlider(0, 255, this.model.getG());
		this.bSlider = new JSlider(0, 255, this.model.getB());
		this.setLayout(new GridLayout(3, 1));
		// Add sliders to this panel
		this.add(this.rSlider);
		this.add(this.gSlider);
		this.add(this.bSlider);
		// Add listeners to sliders
		this.rSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ctlr.setRValue(rSlider.getValue());
			}
		});
		this.gSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ctlr.setGValue(gSlider.getValue());
			}
		});
		this.bSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ctlr.setBValue(bSlider.getValue());
			}
		});
	}

	public void update(Observable arg0, Object arg1) {
		// Update of sliders
		ChangeListener listener = this.rSlider.getChangeListeners()[0];
		this.rSlider.removeChangeListener(listener);
		this.rSlider.setValue(this.model.getR());
		this.rSlider.addChangeListener(listener);
		listener = this.gSlider.getChangeListeners()[0];
		this.gSlider.removeChangeListener(listener);
		this.gSlider.setValue(this.model.getG());
		this.gSlider.addChangeListener(listener);
		listener = this.bSlider.getChangeListeners()[0];
		this.bSlider.removeChangeListener(listener);
		this.bSlider.setValue(this.model.getB());
		this.bSlider.addChangeListener(listener);
	}

}
