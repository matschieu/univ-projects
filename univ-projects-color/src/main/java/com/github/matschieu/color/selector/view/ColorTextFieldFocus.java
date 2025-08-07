package com.github.matschieu.color.selector.view;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;

/**
 * @author Matschieu
 */
public class ColorTextFieldFocus extends FocusAdapter {

	private JTextField textField;
	
	public ColorTextFieldFocus(JTextField textField) {
		this.textField = textField;
	}
	
	public void focusGained(FocusEvent e) {
		this.textField.selectAll();
	}

}
