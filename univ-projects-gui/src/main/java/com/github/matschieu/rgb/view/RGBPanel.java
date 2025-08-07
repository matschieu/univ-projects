package com.github.matschieu.rgb.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.matschieu.rgb.controller.RGBController;
import com.github.matschieu.rgb.model.RGBModel;

/**
 * RGB slider panel to add to a frame
 * @author Matschieu
 */
public class RGBPanel extends JPanel implements Observer, RGBView {

	private RGBModel model; // The model
	private RGBController ctlr; // The controller

	private JTextField colorValueTextField; // Color hexa code text field
	private JPanel colorPanel; // Color view panel

	public RGBPanel(RGBModel model, RGBController ctlr_) {
		// Initialize the model and controller
		this.model = model;
		this.model.addObserver(this);
		this.ctlr = ctlr_;
		// Initialize the color view and code
		this.colorValueTextField = new JTextField(
				(this.model.getR() < 16 ? "0" : "") + Integer.toHexString(this.model.getR()) +
				(this.model.getG() < 16 ? "0" : "") + Integer.toHexString(this.model.getG()) +
				(this.model.getB() < 16 ? "0" : "") + Integer.toHexString(this.model.getB()));
		this.colorValueTextField.setColumns(6);
		this.colorPanel = new JPanel();
		this.colorPanel.setPreferredSize(new Dimension(64, 64));
		this.colorPanel.setBackground(new Color(this.model.getR(), this.model.getG(), this.model.getB()));
		// Add the panel and the text field to this panel
		this.setLayout(new BorderLayout());
		this.add(colorValueTextField, BorderLayout.NORTH);
		this.add(colorPanel, BorderLayout.SOUTH);
		// Add listener to text field
		this.colorValueTextField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int r = Integer.parseInt(colorValueTextField.getText().substring(0, 2), 16);
					int g = Integer.parseInt(colorValueTextField.getText().substring(2, 4), 16);
					int b = Integer.parseInt(colorValueTextField.getText().substring(4, 6), 16);
					ctlr.setRValue(r);
					ctlr.setGValue(g);
					ctlr.setBValue(b);
				}
				catch(NumberFormatException e) { }
			}
		});
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.colorValueTextField.setText(
				(this.model.getR() < 16 ? "0" : "") + Integer.toHexString(this.model.getR()) +
				(this.model.getG() < 16 ? "0" : "") + Integer.toHexString(this.model.getG()) +
				(this.model.getB() < 16 ? "0" : "") + Integer.toHexString(this.model.getB()));
		// Update of the color view
		this.colorPanel.setBackground(new Color(this.model.getR(), this.model.getG(), this.model.getB()));
	}

}
