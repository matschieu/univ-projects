
package com.github.matschieu.gol.ihm;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

import org.jdom.*;

import com.github.matschieu.gol.config.*;
import com.github.matschieu.gol.game.*;
import com.github.matschieu.gol.loader.*;

/**
 * @author Matschieu
 */
public class GOLConfigPanel extends Observable implements ActionListener, Observer {
	
	private Grid grid;
	
	private JPanel panel;
	
	private JTextField height;
	private JTextField width;
	private JTextField rate;
	
	private JComboBox icons;
	
	private JButton apply;
	private JButton next;
	private JButton start;
	private JButton stop;
	private JButton load;
	private JButton save;

	private Thread thread;

	public GOLConfigPanel(Grid grid) {
		this.grid = grid;
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		this.panel.add(this.initPanel(new JPanel()), BorderLayout.NORTH);
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	
	private JPanel initPanel(JPanel p) {
		JPanel tmp = new JPanel();
		tmp.setLayout(new BorderLayout());
		this.width = new JTextField();
		this.width.setColumns(3);
		this.width.setText("" + this.grid.getWidth());
		this.height = new JTextField();
		this.height.setColumns(3);
		this.height.setText("" + this.grid.getHeight());
		this.rate = new JTextField();
		this.rate.setColumns(3);
		this.rate.setText("" + ((GOLEnvironment)this.grid).getRate());
		this.icons = new JComboBox(new String[] { Language.SINGLETON.getElement("CONFIG_PANEL_NO"), Language.SINGLETON.getElement("CONFIG_PANEL_YES") });
		p.setLayout(new GridLayout(6, 3));
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JPanel());
		p.add(new JLabel(Language.SINGLETON.getElement("CONFIG_PANEL_HEIGHT") + " "));
		p.add(this.height);
		p.add(new JLabel(" " + Language.SINGLETON.getElement("CONFIG_PANEL_DIM_UNIT")));
		p.add(new JLabel(Language.SINGLETON.getElement("CONFIG_PANEL_WIDTH") + " "));
		p.add(this.width);
		p.add(new JLabel(" " + Language.SINGLETON.getElement("CONFIG_PANEL_DIM_UNIT")));
		p.add(new JLabel(Language.SINGLETON.getElement("CONFIG_PANEL_RATE") + " "));
		p.add(this.rate);
		p.add(new JLabel(" " + Language.SINGLETON.getElement("CONFIG_PANEL_RATE_UNIT")));
		p.add(new JLabel(Language.SINGLETON.getElement("CONFIG_PANEL_ICONS") + " "));
		p.add(this.icons);
		p.add(new JPanel());
		this.apply = new JButton(Language.SINGLETON.getElement("CONFIG_PANEL_APPLY_BUTTON"), 
					  ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("APPLY_ICON")));
		this.apply.addActionListener(this);
		tmp.add(p, BorderLayout.NORTH);
		tmp.add(this.apply, BorderLayout.SOUTH);
		return tmp;
	}

	public void actionPerformed(ActionEvent e) {
		String notify = null;
		if (e.getSource() == this.apply) {
			int w, h, r;
			try {
				w = Integer.parseInt(this.width.getText());
				h = Integer.parseInt(this.height.getText());
				r = Integer.parseInt(this.rate.getText());
				((GOLEnvironment)this.grid).reset(h, w, r);
				if (this.icons.getSelectedItem() == this.icons.getItemAt(1))
					this.grid.setDisplayIconOption(true);
				else
					this.grid.setDisplayIconOption(false);
				notify = Language.SINGLETON.getElement("CONFIG_PANEL_APPLY_NOTIF");
				notify = notify.replace("[[WIDTH]]", "" + w);
				notify = notify.replace("[[HEIGHT]]", "" + h);
				notify = notify.replace("[[RATE]]", "" + r);
				notify = notify.replace("[[ICONS]]", "" + this.icons.getSelectedItem());
			}
			catch(NumberFormatException nfe) { 
				notify = Language.SINGLETON.getElement("CONFIG_PANEL_ERROR_1_NOTIF");
				JOptionPane.showMessageDialog(null, notify + "\n" + nfe.getMessage(), Language.SINGLETON.getElement("CONFIG_PANEL_ERROR_TITLE"), 
							       JOptionPane.ERROR_MESSAGE, ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("ERROR_ICON")));
				notify +=  " : " + nfe.getMessage();
			}
		}
		if (notify != null) {
			this.setChanged();
			this.notifyObservers(notify);
		}
	}

	public void update(Observable o, Object arg) {
		this.width.setText("" + this.grid.getWidth());
		this.height.setText("" + this.grid.getHeight());
		this.rate.setText("" + ((GOLEnvironment)this.grid).getRate());
//		this.icons = new JComboBox(new String[] { Language.SINGLETON.getElement("CONFIG_PANEL_NO"), Language.SINGLETON.getElement("CONFIG_PANEL_YES") });
	}
	
}