package com.github.matschieu.color.selector.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import com.github.matschieu.color.selector.model.MColor;

/**
 * @author Matschieu
 */
public class ComboBoxColorRenderer extends JLabel implements ListCellRenderer {
	
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			MColor color = (MColor)value;
			if (color == null)
				return null;
			this.setOpaque(true);
			this.setText(color.getHexaString(true));
			this.setBackground(color);
			if (isSelected)
				this.setForeground(MColor.WHITE);
			else
				this.setForeground(color);
			this.setPreferredSize(new Dimension(80, 20));
			return this;
		}

}
