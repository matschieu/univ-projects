package com.github.matschieu.rgb.view;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.github.matschieu.rgb.controller.RGBController;
import com.github.matschieu.rgb.model.RGBModel;
import com.github.matschieu.rgb.view.RGBSliderPanel;
import com.github.matschieu.rgb.view.RGBTextFieldPanel;
import com.github.matschieu.rgb.view.RGBPanel;

/**
 * RGB Selector class
 * @author Matschieu
 */
public class RGBSelectorView implements RGBView {
	
	public RGBSelectorView(RGBModel model, RGBController ctlr) {
		// Select the GUI look and feel
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); }
		catch (Exception e) { }
		// Initialize and display the main window
		JFrame rgbSelectorWindow = new JFrame("RGBSelector");
		// Add the 3 types of panel used in this view
		rgbSelectorWindow.getContentPane().setLayout(new FlowLayout());
		rgbSelectorWindow.getContentPane().add(new RGBSliderPanel(model, ctlr));
		rgbSelectorWindow.getContentPane().add(new RGBTextFieldPanel(model, ctlr));
		rgbSelectorWindow.getContentPane().add(new RGBPanel(model, ctlr));		
		rgbSelectorWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rgbSelectorWindow.setResizable(false);
		rgbSelectorWindow.pack();
		rgbSelectorWindow.setLocationRelativeTo(null);
		rgbSelectorWindow.setVisible(true);
	}

}
