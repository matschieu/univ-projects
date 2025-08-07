package com.github.matschieu.rgb.view;

import java.awt.GridLayout;
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
public class RGBTextFieldPanel extends JPanel implements Observer, RGBView {
	
	private RGBModel model; // The model
	private RGBController ctlr; // The controller

	private JTextField rTextField; // Red text field
	private JTextField gTextField; // Green text field
	private JTextField bTextField; // Blue text field

	public RGBTextFieldPanel(RGBModel model, RGBController ctlr_) {
		// Initialize the model and controller
		this.model = model;
		this.model.addObserver(this);
		this.ctlr = ctlr_;
		// Initialize the text fields
		this.rTextField = new JTextField("" + this.model.getR());
		this.gTextField = new JTextField("" + this.model.getG());
		this.bTextField = new JTextField("" + this.model.getB());
		this.rTextField.setColumns(3);
		this.gTextField.setColumns(3);
		this.bTextField.setColumns(3);
		// Add text fields to this panel
		this.setLayout(new GridLayout(3, 1));
		this.add(this.rTextField);
		this.add(this.gTextField);
		this.add(this.bTextField);
		// Add listeners to text fields
		this.rTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { ctlr.setRValue(Integer.parseInt(rTextField.getText())); }
				catch(NumberFormatException e) { }
			}
		});
		this.gTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { ctlr.setGValue(Integer.parseInt(gTextField.getText())); }
				catch(NumberFormatException e) { }
			}
		});
		this.bTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try { ctlr.setBValue(Integer.parseInt(bTextField.getText())); }
				catch(NumberFormatException e) { }
			}
		});
	}

	public void update(Observable arg0, Object arg1) {
		// Update of text fields
		this.rTextField.setText("" + this.model.getR());
		this.gTextField.setText("" + this.model.getG());
		this.bTextField.setText("" + this.model.getB());
	}

}
