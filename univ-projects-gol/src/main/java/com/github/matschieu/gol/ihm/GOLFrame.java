
package com.github.matschieu.gol.ihm;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import com.github.matschieu.gol.config.*;
import com.github.matschieu.gol.game.*;

/**
 * @author Matschieu
 */
public class GOLFrame extends JFrame implements Observer {
	
	private JLabel infoBar;
	
	public GOLFrame(Grid grid, String title) {
		super(title);
		try { 
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
		}
		catch (Exception e) { }
		this.setSize(700, 550);
		this.setLocationRelativeTo(null);
		String gen = Language.SINGLETON.getElement("GOL_ENVIRONMENT_NOTIF");
		this.infoBar = new JLabel(gen.replace("[[GENERATION]]", "" + grid.getGeneration()));
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(new GridPanel(grid)), BorderLayout.CENTER);
		Console console = new Console(6, 10);
		console.append(Language.SINGLETON.getElement("STARTING_APPLICATION"));
		GOLConfigPanel configPanel = new GOLConfigPanel(grid);
		configPanel.addObserver(console);
		this.add(new JScrollPane(configPanel.getPanel()), BorderLayout.WEST);
		GOLToolBar toolBar = new GOLToolBar(grid);
		toolBar.addObserver(console);
		this.add(toolBar.getPanel(), BorderLayout.NORTH);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(this.infoBar, BorderLayout.NORTH);
		p.add(console, BorderLayout.SOUTH);
		grid.addObserver(this);
		grid.addObserver(configPanel);
		this.add(p, BorderLayout.SOUTH);
		this.addWindowListener(new JFrameWindowAdapter(console));
		this.setVisible(true);
	}
	
	public void update(Observable o, Object arg) {
		this.infoBar.setText((String)arg);
	}
	
	class JFrameWindowAdapter extends WindowAdapter {
		
		private Console console;
		
		public JFrameWindowAdapter(Console console) {
			this.console = console;
		}
		
		public void windowClosing(WindowEvent e) {
			this.console.append(Language.SINGLETON.getElement("CLOSING_APPLICATION"));
			System.exit(0);
		}
		
	}
	
}
