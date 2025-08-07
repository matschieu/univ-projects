
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
import com.github.matschieu.gol.tools.*;

/**
 * @author Matschieu
 */
public class GOLToolBar extends Observable implements ActionListener {
	
	private Grid grid;
	
	private JPanel panel;
	
	private JButton next;
	private JButton start;
	private JButton stop;
	private JButton load;
	private JButton save;
	private JButton info;

	private Thread thread;

	public GOLToolBar(Grid grid) {
		this.grid = grid;
		this.panel = new JPanel();
		this.panel.setLayout(new GridLayout(1, 1));
		this.panel.add(this.initToolBar(new JToolBar()));
	}
	
	public JPanel getPanel() {
		return this.panel;
	}
	
	private JToolBar initToolBar(JToolBar b) {
		b.setFloatable(false);
		this.next = new JButton(ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("NEXT_ICON")));
		this.next.setToolTipText(Language.SINGLETON.getElement("TOOL_BAR_NEXT_BUTTON"));
		this.next.addActionListener(this);
		this.start = new JButton(ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("START_ICON")));
		this.start.setToolTipText(Language.SINGLETON.getElement("TOOL_BAR_START_BUTTON"));
		this.start.addActionListener(this);
		this.stop = new JButton(ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("STOP_ICON")));
		this.stop.setToolTipText(Language.SINGLETON.getElement("TOOL_BAR_STOP_BUTTON"));
		this.stop.addActionListener(this);
		this.stop.setEnabled(false);
		this.load = new JButton(ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("LOAD_ICON")));
		this.load.setToolTipText(Language.SINGLETON.getElement("TOOL_BAR_LOAD_BUTTON"));
		this.load.addActionListener(this);
		this.save = new JButton(ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("SAVE_ICON")));
		this.save.setToolTipText(Language.SINGLETON.getElement("TOOL_BAR_SAVE_BUTTON"));
		this.save.addActionListener(this);
		this.info = new JButton(ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("INFO_ICON")));
		this.info.setToolTipText(Language.SINGLETON.getElement("TOOL_BAR_INFO_BUTTON"));
		this.info.addActionListener(this);
		b.add(this.next);
		b.add(this.start);
		b.add(this.stop);
		b.addSeparator();
		b.add(this.load);
		b.add(this.save);
		b.addSeparator();
		b.add(this.info);
		return b;
	}
	
	public void actionPerformed(ActionEvent e) {
		String notify = null;
		if (e.getSource() == this.next) {
			this.grid.nextGeneration();
			notify = Language.SINGLETON.getElement("TOOL_BAR_NEXT_NOTIF");
			notify = notify.replace("[[GENERATION]]", "" + this.grid.getGeneration());
		}
		else if (e.getSource() == this.start) {
			final Grid g = this.grid;
			this.next.setEnabled(false);
			this.start.setEnabled(false);
			this.stop.setEnabled(true);
			thread = new Thread() {
				private boolean interrupt = false;
				public void interrupt() { 
					interrupt = true;
				}
				public void run() {
					interrupt = false;
					while(!interrupt) {
						g.nextGeneration();
						try { Thread.sleep(250); }
						catch(Exception expt) { }
					}
				}
			};
			thread.start();
			notify = Language.SINGLETON.getElement("TOOL_BAR_START_NOTIF");
			notify = notify.replace("[[GENERATION]]", "" + this.grid.getGeneration());
		}
		else if (e.getSource() == this.stop) {
			thread.interrupt();
			this.stop.setEnabled(false);
			this.start.setEnabled(true);
			this.next.setEnabled(true);
			notify = Language.SINGLETON.getElement("TOOL_BAR_STOP_NOTIF");
			notify = notify.replace("[[GENERATION]]", "" + this.grid.getGeneration());
		}
		else if (e.getSource() == this.load) {
			String path = "";
			try {
				JFileChooser fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new XMLFileFilter());
				if(fileChooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
					path = fileChooser.getCurrentDirectory() + java.io.File.separator + fileChooser.getSelectedFile().getName();
					File file = new File(path);
					if (!file.exists()) 
						throw new Exception(Language.SINGLETON.getElement("TOOL_BAR_ERROR_1_NOTIF"));
					((GOLEnvironment)this.grid).reset(XMLLoader.SINGLETON.load(path));
					notify = Language.SINGLETON.getElement("TOOL_BAR_LOAD_NOTIF");
					notify = notify.replace("[[FILE]]", "" + path);
				}
			}
			catch(Exception expt) { 
				notify = Language.SINGLETON.getElement("TOOL_BAR_ERROR_2_NOTIF");
				notify = notify.replace("[[FILE]]", "" + path);
				JOptionPane.showMessageDialog(null, notify + "\n" + expt.getMessage(), Language.SINGLETON.getElement("CONFIG_PANEL_ERROR_TITLE"), 
							       JOptionPane.ERROR_MESSAGE, ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("ERROR_ICON")));
				notify +=  " : " + expt.getMessage();
			}
		}
		else if (e.getSource() == this.save) {
			String path = "";
			try {
				JFileChooser fileChooser = new JFileChooser(".");
				fileChooser.setFileFilter(new XMLFileFilter());
				if(fileChooser.showSaveDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
					path = fileChooser.getCurrentDirectory() + java.io.File.separator + fileChooser.getSelectedFile().getName();
					XMLLoader.SINGLETON.save(((GOLEnvironment)this.grid).toXMLDocument(), path);
					notify = Language.SINGLETON.getElement("TOOL_BAR_SAVE_NOTIF");
					notify = notify.replace("[[FILE]]", "" + path);
				}
			}
			catch(Exception expt) { 
				notify = Language.SINGLETON.getElement("TOOL_BAR_ERROR_2_NOTIF");
				notify = notify.replace("[[FILE]]", "" + path);
				JOptionPane.showMessageDialog(null, notify + "\n" + expt.getMessage(), Language.SINGLETON.getElement("CONFIG_PANEL_ERROR_TITLE"), 
							       JOptionPane.ERROR_MESSAGE, ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("ERROR_ICON")));
				notify +=  " : " + expt.getMessage();
			}
		}
		else if (e.getSource() == this.info) {
			StringBuffer strBuf = new StringBuffer();
			strBuf.append(Language.SINGLETON.getElement("APPLICATION_NAME") + " ");
			strBuf.append(Language.SINGLETON.getElement("APPLICATION_VERSION") + "\n");
			strBuf.append(Language.SINGLETON.getElement("APPLICATION_DESC") + "\n");
			strBuf.append(Language.SINGLETON.getElement("AUTHOR_NAME") + "\n");
			strBuf.append(Language.SINGLETON.getElement("AUTHOR_MAIL") + "\n");
			strBuf.append(Language.SINGLETON.getElement("AUTHOR_WEBSITE"));
			JOptionPane.showMessageDialog(null, strBuf.toString(), Language.SINGLETON.getElement("APPLICATION_NAME"), 
						       JOptionPane.INFORMATION_MESSAGE, ImageLoader.SINGLETON.load(Icons.SINGLETON.getElement("INFO_IMAGE")));
		}
		if (notify != null) {
			this.setChanged();
			this.notifyObservers(notify);
		}
	}
	
}