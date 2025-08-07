package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import com.github.matschieu.desks.controller.DeskManagementController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Desk;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.view.copypaste.CopyPaste;
import com.github.matschieu.desks.view.tablemodel.DesksTableModel;
import com.github.matschieu.desks.view.undo.AddUndoable;
import com.github.matschieu.desks.view.undo.DellUndoable;
import com.github.matschieu.desks.view.undo.InnerObservable;
import com.github.matschieu.desks.view.undo.UndoObservable;
import com.github.matschieu.desks.view.undo.UpdateUndoable;

/**
 * @author Matschieu
 */
public class DeskManagementPanel extends JPanel implements Observer, UndoObservable {
	
	private static final String HELP_MSG = "<html>Après avoir sélectionné une ligne dans la table, " +
											"vous pouvez utiliser les touches suivantes :<br>" +
											"&nbsp;&nbsp;* '+' pour ajouter un bureau<br>" +
											"&nbsp;&nbsp;* 'suppr' pour supprimer un bureau<br>" +
											"&nbsp;&nbsp;* 'entrée' pour modifier un bureau<br>" +
											"&nbsp;&nbsp;* 'c' pour copier les données d'un bureau<br>" +
											"&nbsp;&nbsp;* 'p' pour coller les données d'un bureau<br>" +
											"</html>";

	private DataBase db;
	private DeskManagementController ctr;
	
	private JTable desksTable;
	private JButton addDeskButton;
	private JButton delDeskButton;
	private JButton updateDeskButton;
	private JButton listReservationsButton;
	private JButton helpButton;
	private JScrollPane scrollPane;	

	private UndoManager undoManager;
	private InnerObservable observable;

	private CopyPaste<Desk> cp;

	/**
	 * Creates a new desk management panel
	 * @param db the model
	 * @param ctr the controller
	 */
	public DeskManagementPanel(DataBase db, DeskManagementController ctr, UndoManager undoManager) {
		this.db = db;
		this.db.addObserver(this);
		this.ctr = ctr;
		this.undoManager = undoManager;
		this.observable = new InnerObservable();
		this.cp = new CopyPaste<Desk>();
		this.initGUI();
		this.initActionListener();
	}
	
	/**
	 * Initializes the GUI
	 */
	private void initGUI() {
		this.addDeskButton = new JButton("Nouveau bureau");
		this.delDeskButton = new JButton("Supprimer");
		this.updateDeskButton = new JButton("Modifier");
		this.listReservationsButton = new JButton("Réservations");
		this.helpButton = new JButton("Aide");
		
		this.desksTable = new JTable(new DesksTableModel(db.getAllDesk()));
		this.desksTable.getTableHeader().setReorderingAllowed(false);
		this.desksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.desksTable.setAutoscrolls(true);
		
		if (this.desksTable.getModel().getRowCount() > 0)
			this.desksTable.setRowSelectionInterval(0, 0);
		
		if (this.desksTable.getRowCount() == 0) {
			this.delDeskButton.setEnabled(false);
			this.updateDeskButton.setEnabled(false);
			this.listReservationsButton.setEnabled(false);
		}
		
		JPanel p = new JPanel();
		p.add(this.addDeskButton);
		p.add(this.updateDeskButton);
		p.add(this.delDeskButton);
		p.add(this.listReservationsButton);
		p.add(this.helpButton);
		
		this.scrollPane = new JScrollPane(this.desksTable);
		
		this.scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Liste des Bureaux existants"));
		this.scrollPane.setBackground(UIManager.getColor("Panel.background"));
		
		this.setLayout(new BorderLayout(10, 10));
		this.add(new WindowTitle("Gestion des bureaux"), BorderLayout.NORTH);
		this.add(this.scrollPane, BorderLayout.CENTER);
		this.add(p, BorderLayout.SOUTH);
		this.add(new JPanel(), BorderLayout.EAST);
		this.add(new JPanel(), BorderLayout.WEST);
	}
	
	/**
	 * Initializes all action listener
	 */
	public void initActionListener() {
		this.addDeskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addDesk();
			}
		});
		this.delDeskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delDesk();
			}
		});
		this.updateDeskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateDesk();
			}
		});
		this.listReservationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayReservations();
			}
		});
		this.helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				help();
			}
		});

		this.desksTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					updateDesk();
				}
			}
		});
		this.desksTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {				
				switch(e.getKeyCode()) {
					case KeyEvent.VK_PLUS :
						addDesk();
						break;					
					case KeyEvent.VK_ENTER :
						updateDesk();
						break;					
					case KeyEvent.VK_DELETE :
						delDesk();
						break;
					case KeyEvent.VK_C :
						copyDesk();
						break;
					case KeyEvent.VK_P :
						pasteDesk();
						break;
				}
			}
		});
	}
	
	private void addDesk() {
		Desk d = (new DeskForm()).showDialog();
		this.ctr.addDesk(d);
		
		this.undoManager.addEdit(new AddUndoable<Desk>(this.ctr, d));		
		this.observable.setChanged();
		this.observable.notifyObservers();
	}
	
	private void delDesk() {
		if (showErrorMessage())
			return;
		
		int idx = this.desksTable.getSelectedRow();
		Desk d = ((DesksTableModel)this.desksTable.getModel()).getDataAtRow(idx);
		this.ctr.removeDesk(d);
		
		this.undoManager.addEdit(new DellUndoable<Desk>(this.ctr, d));
		this.observable.setChanged();
		this.observable.notifyObservers();
		
		idx -= 1;
		if (idx > - 1)
			this.desksTable.setRowSelectionInterval(idx, idx);
	}
	
	private void updateDesk() {
		if (showErrorMessage())
			return;
		Desk d = ((DesksTableModel)this.desksTable.getModel()).getDataAtRow(this.desksTable.getSelectedRow());
		Desk d_ = d.duplicate();
		d = (new DeskForm(d)).showDialog();
		this.ctr.updateDesk(d);
		
		this.undoManager.addEdit(new UpdateUndoable<Desk>(this.ctr, d_, d));
		this.observable.setChanged();
		this.observable.notifyObservers();
	}
	
	private void copyDesk() {
		Desk d = ((DesksTableModel)this.desksTable.getModel()).getDataAtRow(this.desksTable.getSelectedRow());
		this.cp.setClipboardContents(d);
	}
	
	private void pasteDesk() {
		try {
			Desk d = (Desk)this.cp.getClipboardContents();
			if (d == null)
				return;

			d.setNumber(d.getNumber() + " (2)");
			Desk d_ = d.duplicate();
			d_.setId(-1);
			this.ctr.add(d_);
			
			this.undoManager.addEdit(new AddUndoable<Desk>(this.ctr, d_));		
			this.observable.setChanged();
			this.observable.notifyObservers();
		}
		catch(ClassCastException e) { }
	}
	
	private void displayReservations() {
		Desk d = ((DesksTableModel)this.desksTable.getModel()).getDataAtRow(this.desksTable.getSelectedRow());
		List<Reservation> list = this.db.getReservations(d);
		if (list.size() == 0) {
			JOptionPane.showMessageDialog(null, "Aucune réservation pour le bureau " + d.getNumber(), "Erreur", JOptionPane.WARNING_MESSAGE);
			return;
		}
		new ReservationsView(list, "Réservations du bureau " + d.getNumber() + " bâtiment " + d.getBuilding());		
	}

	private void help() {
		JOptionPane.showMessageDialog(null, HELP_MSG, "Aide", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	/**
	 * Displays an error message if the selected value is invalid
	 * @return true if an error occurred
	 */
	private boolean showErrorMessage() {
		int idx = this.desksTable.getSelectedRow();
		int listSize = this.desksTable.getModel().getRowCount();
		if (listSize <= 0) {
			JOptionPane.showMessageDialog(null, "Aucun bureau n'existe", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if (idx < 0 || idx >= listSize) {
			JOptionPane.showMessageDialog(null, "Veuillez sélectionner un bureau", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}
	
	/**
	 * Update the GUI when an observable notify this
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		this.desksTable.setModel(new DesksTableModel(db.getAllDesk()));
		if (this.desksTable.getModel().getRowCount() == 0) {
			this.delDeskButton.setEnabled(false);
			this.updateDeskButton.setEnabled(false);
			this.listReservationsButton.setEnabled(false);
		}
		else {
			this.delDeskButton.setEnabled(true);
			this.updateDeskButton.setEnabled(true);
			this.listReservationsButton.setEnabled(true);
		}
		if (this.desksTable.getRowCount() > 0)
			this.desksTable.setRowSelectionInterval(0, 0);
		this.repaint();
	}

	public Observable getObservable() {
		return this.observable;
	}


	
}
