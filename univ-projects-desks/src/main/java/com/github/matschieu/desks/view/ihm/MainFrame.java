package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;

import com.github.matschieu.desks.controller.DeskManagementController;
import com.github.matschieu.desks.controller.PersonManagementController;
import com.github.matschieu.desks.controller.ReservationController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.view.undo.UndoObserver;

/**
 * @author Matschieu
 */
public class MainFrame extends JFrame implements UndoObserver {

	public static final String APPLICATION_NAME = "DesksManager";

	private DataBase db;
	private DeskManagementController deskController;
	private PersonManagementController personController;
	private ReservationController resController;

	private JPanel middleComp;
	private JMenuItem undoMenuItem;
	private JMenuItem redoMenuItem;
	private JButton undoButton;
	private JButton redoButton;

	private UndoManager undoManager;

	/**
	 * Display a new window
	 * @param db the database model
	 */
	public MainFrame(DataBase db) {
		super();
		this.db = db;
		this.deskController = new DeskManagementController(this.db);
		this.personController = new PersonManagementController(this.db);
		this.resController = new ReservationController(this.db);
		this.undoManager = new UndoManager();
		this.initGUI();
	}

	/**
	 * Initializes the GUI
	 */
	private void initGUI() {
		this.initMenu();

		this.middleComp = new AnnualView(this.db, 10);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.middleComp, BorderLayout.CENTER);

		this.setTitle(APPLICATION_NAME);
		this.setMinimumSize(new Dimension(700, 500));
		this.setPreferredSize(new Dimension(900, 620));
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.initToolsBar();

		this.updateUndoRedo();
	}

	/**
	 * Initializes the menu
	 */
	private void initMenu() {
		JMenuItem calendarMenuItem = new JMenuItem("Calendrier");
		JMenuItem resMenuItem = new JMenuItem("Gestion des réservations");
		JMenuItem deskMenuItem = new JMenuItem("Gestion des bureaux");
		JMenuItem persMenuItem = new JMenuItem("Gestion du personnel");
		JMenuItem quitMenuItem = new JMenuItem("Quitter");
		JMenuItem infoMenuItem = new JMenuItem("A propos");
		this.undoMenuItem = new JMenuItem("Annuler");
		this.redoMenuItem = new JMenuItem("Refaire");

		calendarMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		resMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
		deskMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
		persMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		infoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
		this.undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		this.redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));

		JMenu fileMenu = new JMenu("Fichier");
		fileMenu.setMnemonic('F');
		fileMenu.add(calendarMenuItem);
		fileMenu.add(resMenuItem);
		fileMenu.add(deskMenuItem);
		fileMenu.add(persMenuItem);
		fileMenu.add(quitMenuItem);

		JMenu editMenu = new JMenu("Edition");
		editMenu.setMnemonic('E');
		editMenu.add(this.undoMenuItem);
		editMenu.add(this.redoMenuItem);

		JMenu helpMenu = new JMenu("?");
		helpMenu.setMnemonic('?');
		helpMenu.add(infoMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(helpMenu);

		this.setJMenuBar(menuBar);

		calendarMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFrameContent(new AnnualView(db, (getSize().width - 50) / (WeekPanel.PANEL_SIZE.width + 5)));
			}
		});
		resMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reservationsManagement();
			}
		});
		deskMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				desksManagement();
			}
		});
		persMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				personsManagement();
			}
		});
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		undoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		redoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
		infoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		});
	}

	/**
	 * Initializes the toolbar
	 */
	private void initToolsBar() {
		JButton calButton = new JButton(new ImageIcon(this.getClass().getResource("/img/calendar.png")));
		JButton resButton = new JButton(new ImageIcon(this.getClass().getResource("/img/reservations.png")));
		JButton desksButton = new JButton(new ImageIcon(this.getClass().getResource("/img/desks.png")));
		JButton persoButton = new JButton(new ImageIcon(this.getClass().getResource("/img/users.png")));
		JButton infoButton = new JButton(new ImageIcon(this.getClass().getResource("/img/info.png")));
		JButton exitButton = new JButton(new ImageIcon(this.getClass().getResource("/img/exit.png")));
		this.undoButton = new JButton(new ImageIcon(this.getClass().getResource("/img/undo.png")));
		this.redoButton = new JButton(new ImageIcon(this.getClass().getResource("/img/redo.png")));

		calButton.setToolTipText("Calendrier");
		resButton.setToolTipText("Gestion des réservations");
		desksButton.setToolTipText("Gestion des bureaux");
		persoButton.setToolTipText("Gestion du personnel");
		infoButton.setToolTipText("A propos");
		exitButton.setToolTipText("Quitter");
		this.undoButton.setToolTipText("Annuler");
		this.redoButton.setToolTipText("Refaire");

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(calButton);
		toolBar.add(resButton);
		toolBar.add(desksButton);
		toolBar.add(persoButton);
		toolBar.addSeparator();
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		toolBar.addSeparator();
		toolBar.add(infoButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);

		this.getContentPane().add(toolBar, BorderLayout.NORTH);

		calButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setFrameContent(new AnnualView(db, (getSize().width - 50) / (WeekPanel.PANEL_SIZE.width + 5)));
			}
		});
		resButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				reservationsManagement();
			}
		});
		desksButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				desksManagement();
			}
		});
		persoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				personsManagement();
			}
		});
		infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		});
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				undo();
			}
		});
		this.redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				redo();
			}
		});
	}

	/**
	 * Display the persons management
	 */
	private void personsManagement() {
		PersonManagementPanel pmp = new PersonManagementPanel(this.db, this.personController, this.undoManager);
		pmp.getObservable().addObserver(this);
		setFrameContent(pmp);
	}

	/**
	 * Display the desks management
	 */
	private void desksManagement() {
		DeskManagementPanel dmp = new DeskManagementPanel(this.db, this.deskController, this.undoManager);
		dmp.getObservable().addObserver(this);
		setFrameContent(dmp);
	}

	/**
	 * Display the reservations management
	 */
	private void reservationsManagement() {
		ReservationManagementPanel rmp = new ReservationManagementPanel(this.db, this.resController, this.undoManager);
		rmp.getObservable().addObserver(this);
		setFrameContent(rmp);
	}

	/**
	 * Set the middle panel of the frame
	 * @param p the panel to display
	 */
	public void setFrameContent(JPanel p) {
		this.getContentPane().remove(middleComp);
		this.middleComp = p;
		this.getContentPane().add(middleComp, BorderLayout.CENTER);
		this.setPreferredSize(this.getSize());
		this.pack();
	}

	/**
	 * Display a pop up information window about this program
	 */
	public void showAbout() {
		String msg = "Matschieu\nTP d'IHM\nM1S2 informatique\nUniversité Lille1";
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/img/info.png"));
		JOptionPane.showMessageDialog(null, msg, "A propos de " + APPLICATION_NAME, JOptionPane.PLAIN_MESSAGE, icon);
	}

	/**
	 * Sets undo/redo buttons or menus enabled or not
	 */
	private void updateUndoRedo() {
		if (this.undoManager.canUndo()) {
			this.undoMenuItem.setEnabled(true);
			this.undoButton.setEnabled(true);
		}
		else {
			this.undoMenuItem.setEnabled(false);
			this.undoButton.setEnabled(false);
		}
		if (this.undoManager.canRedo()) {
			this.redoMenuItem.setEnabled(true);
			this.redoButton.setEnabled(true);
		}
		else {
			this.redoMenuItem.setEnabled(false);
			this.redoButton.setEnabled(false);
		}
	}

	private void undo() {
		this.undoManager.undo();
		this.updateUndoRedo();
	}

	private void redo() {
		this.undoManager.redo();
		this.updateUndoRedo();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.updateUndoRedo();
	}

}
