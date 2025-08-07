package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.github.matschieu.desks.controller.ReservationController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.model.util.Week;
import com.github.matschieu.desks.view.tablemodel.ReservationsTableModel;

/**
 * @author Matschieu
 */
public class WeeklyView extends JDialog implements Observer {

	private Week week;
	private DataBase db;
	private ReservationController controller;

	private JLabel totalDesk;
	private JLabel freeDesk;
	private JTable reservationTable;
	private JScrollPane scrollPane;	
	private JButton back;
	private JButton reservation;
	private JButton modification;
	private JButton remove;
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_GAP = 50;


	public WeeklyView(DataBase db, ReservationController controller, Week week){
		this.db = db;
		this.controller = controller;
		this.week = week;

		this.db.addObserver(this);

		init();
	}

	private void init(){
		//week informations
		totalDesk = new JLabel("Nombre total de bureaux   :  " + this.db.getTotalDeskCount());
		freeDesk = new JLabel("Nombre de bureaux libres  :  " + this.db.getFreeDeskCount(week));
		totalDesk.setPreferredSize(new Dimension(WINDOW_WIDTH - WINDOW_GAP*2, 20));
		freeDesk.setPreferredSize(new Dimension(WINDOW_WIDTH - WINDOW_GAP*2, 20));

		//Reservation list
		this.reservationTable = new JTable(new ReservationsTableModel(db.getAllReservation(week)));
		reservationTable.setGridColor(Color.GRAY);
		reservationTable.setShowVerticalLines(false);
		reservationTable.setAutoscrolls(true);
		reservationTable.getSelectionModel().addSelectionInterval(0,0);
		reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		reservationTable.getTableHeader().setReorderingAllowed(false);
		reservationTable.setAutoscrolls(true);	

		scrollPane = new JScrollPane(reservationTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Liste des Bureaux existants"));
		scrollPane.setBackground(UIManager.getColor("Panel.background"));

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
		top.add(new WindowTitle(this.week.toString()), BorderLayout.NORTH);

		top.add(Box.createVerticalStrut(10));
		top.add(totalDesk);
		top.add(Box.createVerticalStrut(5));
		top.add(freeDesk);
		top.add(Box.createVerticalStrut(15));

		//bottom panel
		JPanel bottom = new JPanel();
		back = new JButton("Retour");
		reservation = new JButton("Réserver un bureau");
		modification = new JButton("Modifier");
		remove = new JButton("Supprimer");
		this.back.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		this.reservation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ReservationForm(db, controller, week).setVisible(true);
			}
		});
		this.modification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showErrorMessage())
					return;
				Reservation r = ((ReservationsTableModel)reservationTable.getModel()).getDataAtRow((reservationTable.getSelectedRow()));
				String[] choix = {"Modifier uniquement cette semaine", "Modifer la réservation entière", "Annuler"};
				int reponse = JOptionPane.showOptionDialog(getThis(), 
						"Vous allez modifier une réservation,\nSouhaitez-vous la modifier uniquement pour cette semaine ou pour toutes les semaines de la réservation?", 
						"Modification d'une réservation", 
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						choix,
						choix[0]);
				switch(reponse){	
				case 0 :
					new ReservationForm(db, controller, r, week).setVisible(true);	
					break;
				case 1 : 
					new ReservationForm(db, controller, r).setVisible(true);	
					break;
				case 2 :
					return;
				}
			}
		});
		this.remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (showErrorMessage())
					return;
				Reservation r = ((ReservationsTableModel)reservationTable.getModel()).getDataAtRow((reservationTable.getSelectedRow()));
				String[] choix = {"Supprimer uniquement cette semaine", "Supprimer la réservation entière", "Annuler"};
				int reponse = JOptionPane.showOptionDialog(getThis(), 
						"Vous allez supprimer une réservation,\nSouhaitez-vous la supprimer uniquement pour cette semaine ou pour toutes les semaines de la réservation?", 
						"Suppression d'une réservation", 
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						choix,
						choix[0]);
				switch(reponse){	
				case 0 :
					controller.removeReservation(r,week);
					break;
				case 1 : 
					controller.removeReservation(r);
					break;
				case 2 :
					return;
				}
			}
		});
		reservationTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Reservation r = ((ReservationsTableModel)reservationTable.getModel()).getDataAtRow((reservationTable.getSelectedRow()));
					new ReservationForm(db, controller, r).setVisible(true);
				}
			}
		});
		
		if (this.reservationTable.getRowCount() > 0) this.reservationTable.setRowSelectionInterval(0, 0);


		bottom.add(back);
		bottom.add(reservation);
		bottom.add(modification);
		bottom.add(remove);

		//panel building
		//setTitle(this.week.toString());
		setSize(new Dimension(WINDOW_WIDTH, 400));
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new BorderLayout(WINDOW_GAP,0));

		add(top, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);		
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

	public WeeklyView getThis(){
		return this;
	}

	/**
	 * Update the GUI when an observable notify this
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		reservationTable.setModel(new ReservationsTableModel(this.db.getAllReservation(this.week)));

		if (this.reservationTable.getModel().getRowCount() == 0) {
			this.remove.setEnabled(false);
			this.modification.setEnabled(false);
		}
		else {
			this.remove.setEnabled(true);
			this.modification.setEnabled(true);
		}

		if (this.reservationTable.getRowCount() > 0) this.reservationTable.setRowSelectionInterval(0, 0);

		totalDesk.setText("Nombre total de bureaux   :  " + this.db.getTotalDeskCount());
		freeDesk.setText("Nombre de bureaux libres  :  " + this.db.getFreeDeskCount(week));

		this.repaint();
	}
}
