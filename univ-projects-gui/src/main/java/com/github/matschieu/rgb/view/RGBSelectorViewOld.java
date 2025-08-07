package com.github.matschieu.rgb.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.matschieu.rgb.controller.RGBController;
import com.github.matschieu.rgb.model.RGBModel;

/**
 * RGB Selector class
 * @author Matschieu
 */
public class RGBSelectorViewOld implements Observer, RGBView {
	
	private RGBModel model; // The model
	private RGBController ctlr; // The controller

	private JSlider rSlider; // Red slider
	private JSlider gSlider; // Green slider
	private JSlider bSlider; // Blue slider

	private JTextField rTextField; // Red text field
	private JTextField gTextField; // Green text field
	private JTextField bTextField; // Blue text field

	private JTextField colorValueTextField; // Color hexa code text field

	private JPanel colorPanel; // Color view panel

	private JFrame rgbSelectorWindow; // The main window

	public RGBSelectorViewOld(RGBModel model, RGBController ctlr) {
		// Select the GUI look and feel
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); }
		catch (Exception e) { }
		// Initialize the model and controller
		this.model = model;
		this.model.addObserver(this);
		this.ctlr = ctlr;
		// Initialize the sliders
		this.rSlider = new JSlider(0, 255);
		this.gSlider = new JSlider(0, 255);
		this.bSlider = new JSlider(0, 255);
		// Create a panel for the sliders
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(3, 1));
		sliderPanel.add(this.rSlider);
		sliderPanel.add(this.gSlider);
		sliderPanel.add(this.bSlider);
		// Initialize the text fields		
		this.rTextField = new JTextField();
		this.gTextField = new JTextField();
		this.bTextField = new JTextField();
		this.rTextField.setColumns(3);
		this.gTextField.setColumns(3);
		this.bTextField.setColumns(3);
		// Create a panel for the text fields		
		JPanel textFieldPanel = new JPanel();
		textFieldPanel.setLayout(new GridLayout(3, 1));
		textFieldPanel.add(this.rTextField);
		textFieldPanel.add(this.gTextField);
		textFieldPanel.add(this.bTextField);
		// Initialize the color view and code
		this.colorValueTextField = new JTextField();
		this.colorValueTextField.setColumns(6);
		this.colorPanel = new JPanel();
		this.colorPanel.setPreferredSize(new Dimension(64, 64));
		// Create a panel for the color view and code		
		JPanel colorRenderPanel = new JPanel();
		colorRenderPanel.setLayout(new BorderLayout());
		colorRenderPanel.add(colorValueTextField, BorderLayout.NORTH);
		colorRenderPanel.add(colorPanel, BorderLayout.SOUTH);
		// Add listeners to components
		this.initListener();
		// Initialize and display the main window
		this.rgbSelectorWindow = new JFrame("RGBSelector");
		this.rgbSelectorWindow.getContentPane().setLayout(new FlowLayout());
		this.rgbSelectorWindow.getContentPane().add(sliderPanel);
		this.rgbSelectorWindow.getContentPane().add(textFieldPanel);
		this.rgbSelectorWindow.getContentPane().add(colorRenderPanel);		
		this.rgbSelectorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.rgbSelectorWindow.setResizable(false);
		this.rgbSelectorWindow.pack();
		this.rgbSelectorWindow.setLocationRelativeTo(null);
		this.rgbSelectorWindow.setVisible(true);
		this.update();
	}

	/**
	 * Add listeners to this view components
	 */
	private void initListener() {
		// Add listeners to sliders
		this.rSlider.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						ctlr.setRValue(rSlider.getValue());
					}
				}
		);
		this.gSlider.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						ctlr.setGValue(gSlider.getValue());
					}
				}
		);
		this.bSlider.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent e) {
						ctlr.setBValue(bSlider.getValue());
					}
				}
		);
		// Add listeners to text fields
		this.rTextField.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try { ctlr.setRValue(Integer.parseInt(rTextField.getText())); }
						catch(NumberFormatException e) { }
					}
				}
		);
		this.gTextField.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try { ctlr.setGValue(Integer.parseInt(gTextField.getText())); }
						catch(NumberFormatException e) { }
					}
				}
		);
		this.bTextField.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try { ctlr.setBValue(Integer.parseInt(bTextField.getText())); }
						catch(NumberFormatException e) { }
					}
				}
		);
		this.colorValueTextField.addActionListener(
				new ActionListener() {
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
				}
		);
	}

	/**
	 * Updates the view with the correct values r, v and b
	 * @param comp the component that update the view
	 */
	private void update() {
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
		// Update of text fields
		this.rTextField.setText("" + this.model.getR());
		this.gTextField.setText("" + this.model.getG());
		this.bTextField.setText("" + this.model.getB());
		this.colorValueTextField.setText(
				(this.model.getR() < 16 ? "0" : "") + Integer.toHexString(this.model.getR()) + 
				(this.model.getG() < 16 ? "0" : "") + Integer.toHexString(this.model.getG()) +
				(this.model.getB() < 16 ? "0" : "") + Integer.toHexString(this.model.getB()));
		// Update of the color view
		this.colorPanel.setBackground(new Color(this.model.getR(), this.model.getG(), this.model.getB()));
	}

	public void update(Observable arg0, Object arg1) {
		this.update();
	}

}
