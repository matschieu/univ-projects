package com.github.matschieu.rgb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * RGB Selector class
 * @author Matschieu
 */
public class RGBSelectorView {

	private int r; // Red
	private int g; // Green
	private int b; // Blue

	private JSlider rSlider; // Red slider
	private JSlider gSlider; // Green slider
	private JSlider bSlider; // Blue slider

	private JTextField rTextField; // Red text field
	private JTextField gTextField; // Green text field
	private JTextField bTextField; // Blue text field

	private JTextField colorValueTextField; // Color hexa code text field

	private JPanel colorPanel; // Color view panel

	private JFrame rgbSelectorWindow; // The main window

	public RGBSelectorView(int r, int g, int b) {
		// Select the GUI look and feel
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); }
		catch (Exception e) { }
		// Initialize the initial color
		this.r = r;
		this.g = g;
		this.b = b;
		// Initialize the sliders
		this.rSlider = new JSlider(0, 255, r);
		this.gSlider = new JSlider(0, 255, g);
		this.bSlider = new JSlider(0, 255, b);
		// Create a panel for the sliders
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new GridLayout(3, 1));
		sliderPanel.add(this.rSlider);
		sliderPanel.add(this.gSlider);
		sliderPanel.add(this.bSlider);
		// Initialize the text fields
		this.rTextField = new JTextField("" + r);
		this.gTextField = new JTextField("" + g);
		this.bTextField = new JTextField("" + b);
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
		this.colorValueTextField = new JTextField(
				(this.r < 16 ? "0" : "") + Integer.toHexString(this.r) +
				(this.g < 16 ? "0" : "") + Integer.toHexString(this.g) +
				(this.b < 16 ? "0" : "") + Integer.toHexString(this.b));
		this.colorValueTextField.setColumns(6);
		this.colorPanel = new JPanel();
		this.colorPanel.setPreferredSize(new Dimension(64, 64));
		this.colorPanel.setBackground(new Color(r, g, b));
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
	}

	/**
	 * Add listeners to this view components
	 */
	private void initListener() {
		// Add listeners to sliders
		this.rSlider.addChangeListener(
				new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						r = rSlider.getValue();
						update(rSlider);
					}
				}
		);
		this.gSlider.addChangeListener(
				new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						g = gSlider.getValue();
						update(gSlider);
					}
				}
		);
		this.bSlider.addChangeListener(
				new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						b = bSlider.getValue();
						update(bSlider);
					}
				}
		);
		// Add listeners to text fields
		this.rTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							int tmp = Integer.parseInt(rTextField.getText());
							if (tmp >= 0 && tmp < 256)
								r = tmp;
							update(rTextField);
						} catch(NumberFormatException e) {
							update(rTextField);
						}
					}
				}
		);
		this.gTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							int tmp = Integer.parseInt(gTextField.getText());
							if (tmp >= 0 && tmp < 256)
								g = tmp;
							update(gTextField);
						} catch(NumberFormatException e) {
							update(gTextField);
						}
					}
				}
		);
		this.bTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							int tmp = Integer.parseInt(bTextField.getText());
							if (tmp >= 0 && tmp < 256)
								b = tmp;
							update(bTextField);
						} catch(NumberFormatException e) {
							update(bTextField);
						}
					}
				}
		);
		this.colorValueTextField.addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						try {
							int rTmp = Integer.parseInt(colorValueTextField.getText().substring(0, 2), 16);
							int gTmp = Integer.parseInt(colorValueTextField.getText().substring(2, 4), 16);
							int bTmp = Integer.parseInt(colorValueTextField.getText().substring(4, 6), 16);
							r = rTmp;
							g = gTmp;
							b = bTmp;
							update(colorValueTextField);
						} catch(Exception e) {
							update(colorValueTextField);
						}
					}
				}
		);
	}

	/**
	 * Updates the view with the correct values r, v and b
	 * @param comp the component that update the view
	 */
	private void update(Component comp) {
		// Update of sliders
		ChangeListener listener = this.rSlider.getChangeListeners()[0];
		this.rSlider.removeChangeListener(listener);
		this.rSlider.setValue(this.r);
		this.rSlider.addChangeListener(listener);
		listener = this.gSlider.getChangeListeners()[0];
		this.gSlider.removeChangeListener(listener);
		this.gSlider.setValue(this.g);
		this.gSlider.addChangeListener(listener);
		listener = this.bSlider.getChangeListeners()[0];
		this.bSlider.removeChangeListener(listener);
		this.bSlider.setValue(this.b);
		this.bSlider.addChangeListener(listener);
		// Update of text fields
		this.rTextField.setText("" + this.r);
		this.gTextField.setText("" + this.g);
		this.bTextField.setText("" + this.b);
		this.colorValueTextField.setText((this.r < 16 ? "0" : "") + Integer.toHexString(this.r) +
										 (this.g < 16 ? "0" : "") + Integer.toHexString(this.g) +
										 (this.b < 16 ? "0" : "") + Integer.toHexString(this.b));
		// Update of the color view
		this.colorPanel.setBackground(new Color(this.r, this.g, this.b));
	}

	public static void main(String[] args) {
		new RGBSelectorView(223, 116, 3);
	}

}