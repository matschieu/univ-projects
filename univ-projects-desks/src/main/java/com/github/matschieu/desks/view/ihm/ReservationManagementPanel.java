package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

import com.github.matschieu.desks.controller.ReservationController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.view.tablemodel.ReservationsTableModel;
import com.github.matschieu.desks.view.undo.AddUndoable;
import com.github.matschieu.desks.view.undo.DellUndoable;
import com.github.matschieu.desks.view.undo.InnerObservable;
import com.github.matschieu.desks.view.undo.UndoObservable;
import com.github.matschieu.desks.view.undo.UpdateUndoable;

/**
 * @author Matschieu
 */
public class ReservationManagementPanel extends JPanel implements Observer, UndoObservable {

	private static final String HELP_MSG = "<html>Après avoir sélectionné une ligne dans la table, " +
											"vous pouvez utiliser les touches suivantes :<br>" +
											"&nbsp;&nbsp;* '+' pour ajouter une réservation<br>" +
											"&nbsp;&nbsp;* 'suppr' pour supprimer une réservation<br>" +
											"&nbsp;&nbsp;* 'entrée' pour modifier une réservation<br>" +
											"</html>";

	private DataBase db;
	private ReservationController ctr;
	
	private JTable reservationTable;
	private JButton addReservationButton;
	private JButton delReservationButton;
	private JButton updateReservationButton;
	private JButton helpButton;
	private JScrollPane scrollPane;	

	private UndoManager undoManager;
	private InnerObservable observable;

	/**
	 * Creates a new reservations management panel
	 * @param db the model
	 * @param ctr the controller
	 */
	public ReservationManagementPanel(DataBase db, ReservationController ctr, UndoManager undoManager) {
		this.db = db;
		this.db.addObserver(this);
		this.ctr = ctr;
		this.undoManager = undoManager;
		this.observable = new InnerObservable();
		this.initGUI();
		this.initActionListener();
	}
	
	/**
	 * Initializes the GUI
	 */
	private void initGUI() {
		this.addReservationButton = new JButton("Nouvelle réservation");
		this.delReservationButton = new JButton("Supprimer");
		this.updateReservationButton = new JButton("Modifier");
		this.helpButton = new JButton("Aide");
		
		this.reservationTable = new JTable(new ReservationsTableModel(db.getAllReservation()));
		this.reservationTable.getTableHeader().setReorderingAllowed(false);
		this.reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.reservationTable.setAutoscrolls(true);
		
		if (this.reservationTable.getRowCount() > 0)
			this.reservationTable.setRowSelectionInterval(0, 0);
		
		if (this.reservationTable.getModel().getRowCount() == 0) {
			this.delReservationButton.setEnabled(false);
			this.updateReservationButton.setEnabled(false);
		}
		
		JPanel p = new JPanel();
		p.add(this.addReservationButton);
		p.add(this.updateReservationButton);
		p.add(this.delReservationButton);
		p.add(this.helpButton);
		
		this.scrollPane = new JScrollPane(this.reservationTable);
		this.scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Liste du personnel connu"));
		this.scrollPane.setBackground(UIManager.getColor("Panel.background"));

		
		this.setLayout(new BorderLayout(10, 10));
		this.add(new WindowTitle("Gestion des réservations"), BorderLayout.NORTH);
		this.add(this.scrollPane, BorderLayout.CENTER);
		this.add(p, BorderLayout.SOUTH);
		this.add(new JPanel(), BorderLayout.EAST);
		this.add(new JPanel(), BorderLayout.WEST);
	}
	
	/**
	 * Initializes all action listener
	 */
	public void initActionListener() {
		this.addReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addReservation();
			}
		});
		this.delReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delReservation();
			}
		});
		this.updateReservationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateReservation();
			}
		});
		this.helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				help();
			}
		});
		
		this.reservationTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					updateReservation();
				}
			}
		});
		this.reservationTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {				
				switch(e.getKeyCode()) {
					case KeyEvent.VK_PLUS :
						addReservation();
						break;					
					case KeyEvent.VK_ENTER :
						updateReservation();
						break;					
					case KeyEvent.VK_DELETE :
						delReservation();
						break;
				}
			}
		});
	}
	
	private void addReservation() {
		Reservation r = (new ReservationForm(this.db, this.ctr)).showDialog();
		
		this.undoManager.addEdit(new AddUndoable<Reservation>(this.ctr, r));		
		this.observable.setChanged();
		this.observable.notifyObservers();
	}
	
	private void delReservation() {
		if (showErrorMessage())
			return;
		
		int idx = this.reservationTable.getSelectedRow();
		Reservation r = ((ReservationsTableModel)this.reservationTable.getModel()).getDataAtRow(idx);
		this.ctr.removeReservation(r);

		this.undoManager.addEdit(new DellUndoable<Reservation>(this.ctr, r));
		this.observable.setChanged();
		this.observable.notifyObservers();
		
		idx -= 1;
		if (idx > - 1)
			this.reservationTable.setRowSelectionInterval(idx, idx);
	}
	
	private void updateReservation() {
		if (showErrorMessage())
			return;
		Reservation r = ((ReservationsTableModel)this.reservationTable.getModel()).getDataAtRow((reservationTable.getSelectedRow()));
		Reservation r_ = r.duplicate();
		r = new ReservationForm(this.db, this.ctr, r).showDialog();
		
		this.undoManager.addEdit(new UpdateUndoable<Reservation>(this.ctr, r_, r));
		this.observable.setChanged();
		this.observable.notifyObservers();
	}

	private void help() {
		JOptionPane.showMessageDialog(null, HELP_MSG, "Aide", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	/**
	 * Displays an error message if the selected value is invalid
	 * @return true if an error occurred
	 */
	private boolean showErrorMessage() {
		int idx = this.reservationTable.getSelectedRow();
		int listSize = this.reservationTable.getModel().getRowCount();
		if (listSize <= 0) {
			JOptionPane.showMessageDialog(null, "Aucune réservation n'existe", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if (idx < 0 || idx >= listSize) {
			JOptionPane.showMessageDialog(null, "Veuillez sélectionner une réservation", "Erreur", JOptionPane.ERROR_MESSAGE);
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
		this.reservationTable.setModel(new ReservationsTableModel(db.getAllReservation()));
		if (this.reservationTable.getModel().getRowCount() == 0) {
			this.delReservationButton.setEnabled(false);
			this.updateReservationButton.setEnabled(false);
		}
		else {
			this.delReservationButton.setEnabled(true);
			this.updateReservationButton.setEnabled(true);
		}
		if (this.reservationTable.getRowCount() > 0)
			this.reservationTable.setRowSelectionInterval(0, 0);
		this.repaint();
	}

	public Observable getObservable() {
		return this.observable;
	}

}

