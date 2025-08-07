package com.github.matschieu.desks.view.ihm;

/**
 * @author Matschieu
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.matschieu.desks.controller.ReservationController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Desk;
import com.github.matschieu.desks.model.util.Person;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.model.util.Week;

public class ReservationForm extends JDialog{
	
	private boolean update;
	private Reservation oldRes;
	private DataBase db;
	private ReservationController controller;
	private Week week;
	private Reservation newRes;
	private DefaultComboBoxModel comboDeskModel;
	
	private JPanel formPanel;
	private JPanel buttonsPanel;
	
	private JComboBox comboDesk;
	private JComboBox comboManager;
	private JComboBox comboTrainee;
	private JComboBox comboDateBegin;
	private JComboBox comboYearBegin;
	private JComboBox comboDateEnd;
	private JComboBox comboYearEnd;

	private JButton okButton;
	private JButton cancelButton;
	
	/**
	 * Create a panel to reserve a desk 
	 * @param db database
	 * @param controller reservation controller
	 */
	public ReservationForm(DataBase db, ReservationController controller) {
		this.db = db;
		this.controller = controller;
		this.update = false;
		this.oldRes = new Reservation();
		init();
	}
	
	/**
	 * Create a panel to reserve a desk at a specified week
	 * @param db database
	 * @param controller reservation controller
	 * @param week the week
	 */
	public ReservationForm(DataBase db, ReservationController controller, Week week) {
		this.db = db;
		this.controller = controller;
		this.update = false;
		this.oldRes = new Reservation();
		this.week = week;
		
		init();
		
		comboDateBegin.setSelectedItem(week.getWeekNumber());
		comboYearBegin.setSelectedItem(week.getYear());
		comboDateEnd.setSelectedItem(week.getWeekNumber());
		comboYearEnd.setSelectedItem(week.getYear());

	}

	/**
	 * Create a panel to modify a desk reservation
	 * @param db database
	 * @param controller reservation controller
	 * @param oldRes old reservation
	 */
	public ReservationForm(DataBase db, ReservationController controller, Reservation oldRes){
		this.db = db;
		this.controller = controller;
		this.update = true;
		this.oldRes = oldRes;

		init();
				
		comboDesk.setSelectedItem(this.oldRes.getDesk());
		comboManager.setSelectedItem(this.oldRes.getManager());
		comboTrainee.setSelectedItem(this.oldRes.getTrainee());
		
		comboDateBegin.setSelectedItem(this.oldRes.getStart().getWeekNumber());
		comboYearBegin.setSelectedItem(this.oldRes.getStart().getYear());
		comboDateEnd.setSelectedItem(this.oldRes.getEnd().getWeekNumber());
		comboYearEnd.setSelectedItem(this.oldRes.getEnd().getYear());
				
	}	
	
	/**
	 * Create a panel to modify a desk reservation at a specified week
	 * @param db database
	 * @param controller reservation controller
	 * @param oldRes old reservation
	 * @param week the week
	 */
	public ReservationForm(DataBase db, ReservationController controller, Reservation oldRes, Week week){
		this(db, controller, oldRes);
		this.week = week;
		
		comboDateBegin.setSelectedItem(week.getWeekNumber());
		comboYearBegin.setSelectedItem(week.getYear());
		comboDateEnd.setSelectedItem(week.getWeekNumber());
		comboYearEnd.setSelectedItem(week.getYear());
		
		comboDateBegin.setEnabled(false);
		comboYearBegin.setEnabled(false);
		comboDateEnd.setEnabled(false);
		comboYearEnd.setEnabled(false);	
	}
	
	private void init(){
		formPanel = new JPanel();
		buttonsPanel = new JPanel();
		
		formPanel.setLayout(new GridLayout(5, 2));
		
		JPanel p = new JPanel();
		
		Integer[] weeks = new Integer[52];
		for (int i = 0; i < weeks.length; i++) {
			weeks[i] = i+1;
		}
		
		Integer [] years = new Integer[3];
		int year = (new GregorianCalendar()).get(Calendar.YEAR);
		for (int i = 0; i < years.length; i++) {
			years[i] = year + i;
		}
		
		this.comboDeskModel = new DefaultComboBoxModel(this.db.getAllDesk().toArray());
		this.comboDesk = new JComboBox(comboDeskModel);
		this.comboManager = new JComboBox(this.db.getAllPerson(false).toArray());
		this.comboTrainee = new JComboBox(this.db.getAllPerson(true).toArray());
		this.comboDateBegin = new JComboBox(weeks);
		this.comboYearBegin = new JComboBox(years);
		this.comboDateEnd = new JComboBox(weeks);
		this.comboYearEnd = new JComboBox(years);
		
		this.comboDesk.setPreferredSize(new Dimension(350, 25));
		this.comboManager.setPreferredSize(new Dimension(350, 25));
		this.comboTrainee.setPreferredSize(new Dimension(350, 25));
		this.comboDateBegin.setPreferredSize(new Dimension(100, 25));
		this.comboYearBegin.setPreferredSize(new Dimension(100, 25));
		this.comboDateEnd.setPreferredSize(new Dimension(100, 25));
		this.comboYearEnd.setPreferredSize(new Dimension(100, 25));
		
		this.comboDateBegin.addActionListener(new ActionListenerFreeDesks());
		this.comboYearBegin.addActionListener(new ActionListenerFreeDesks());
		this.comboDateEnd.addActionListener(new ActionListenerFreeDesks());
		this.comboYearEnd.addActionListener(new ActionListenerFreeDesks());
		
		p.add(this.comboDesk);
		formPanel.add(new JLabel("      Bureau : "));
		formPanel.add(p);

		p= new JPanel();
		p.add(this.comboManager);
		formPanel.add(new JLabel("      Manager :"));
		formPanel.add(p);

		p= new JPanel();
		p.add(this.comboTrainee);
		formPanel.add(new JLabel("      Stagiaire :"));
		formPanel.add(p);
		
		formPanel.add(new JLabel("      Semaine de début :"));

		p = new JPanel();
		p.add(new JLabel("Numéro :"));
		p.add(this.comboDateBegin);
		p.add(new JLabel("Année :"));
		p.add(this.comboYearBegin);
		formPanel.add(p);
		
		formPanel.add(new JLabel("      Semaine de fin :"));
		p = new JPanel();
		p.add(new JLabel("Numéro :"));
		p.add(this.comboDateEnd);
		p.add(new JLabel("Année :"));
		p.add(this.comboYearEnd);
		formPanel.add(p);
		
		
		this.okButton = new JButton("Enregistrer");
		this.cancelButton = new JButton("Annuler");
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
	
		this.setLayout(new BorderLayout(10, 10));
		this.add(new WindowTitle(this.update ? "Modification d'une réservation" : "Création d'une réservation"), BorderLayout.NORTH);
		this.add(formPanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validation();
			}
		});
		
		this.formPanel.addKeyListener(new ReservationKeyListener());
		this.buttonsPanel.addKeyListener(new ReservationKeyListener());
		this.comboDesk.addKeyListener(new ReservationKeyListener());
		this.comboManager.addKeyListener(new ReservationKeyListener());
		this.comboTrainee.addKeyListener(new ReservationKeyListener());
		this.comboDateBegin.addKeyListener(new ReservationKeyListener());
		this.comboYearBegin.addKeyListener(new ReservationKeyListener());
		this.comboDateEnd.addKeyListener(new ReservationKeyListener());
		this.comboYearEnd.addKeyListener(new ReservationKeyListener());
		this.okButton.addKeyListener(new ReservationKeyListener());
		this.cancelButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		
		int newStart = (Integer) comboDateBegin.getSelectedItem();
		int newStartYear = (Integer) comboYearBegin.getSelectedItem();
		int newEnd = (Integer) comboDateEnd.getSelectedItem();
		int newEndYear = (Integer) comboYearEnd.getSelectedItem();
		
		List<Desk> availableDesk = db.getFreeDesks(new Week(newStart, newStartYear), new Week(newEnd, newEndYear));
		if (update) availableDesk.add(oldRes.getDesk());
		comboDesk.setModel(new DefaultComboBoxModel(availableDesk.toArray()));
		if (update) comboDesk.setSelectedItem(this.oldRes.getDesk());
		
		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}

	public Reservation showDialog() {
		this.setVisible(true);
		while(this.isVisible());
		return newRes;
	}
	
	public void validation() {
		Desk newDesk = (Desk) this.comboDesk.getSelectedItem();
		Person newManager = (Person) this.comboManager.getSelectedItem();
		Person newTrainee = (Person) this.comboTrainee.getSelectedItem();
		int newStart = (Integer) this.comboDateBegin.getSelectedItem();
		int newStartYear = (Integer) this.comboYearBegin.getSelectedItem();
		int newEnd = (Integer) this.comboDateEnd.getSelectedItem();
		int newEndYear = (Integer) this.comboYearEnd.getSelectedItem();
		
		if (this.update)	{
			this.newRes = new Reservation(this.oldRes.getId(), newDesk, newManager, newTrainee, new Week(newStart, newStartYear), new Week(newEnd, newEndYear));
			if (week != null ) 
				this.controller.updateReservation(this.oldRes, this.newRes);
			else 
				this.controller.updateReservation(this.newRes);
		}
		else {
			this.newRes = new Reservation("", newDesk, newManager, newTrainee, new Week(newStart, newStartYear), new Week(newEnd, newEndYear));
			this.controller.addReservation(this.newRes);
		}
		dispose();
	}
	
	public void cancel() {
		this.dispose();
	}
	
	/**
	 * Updates free desks according to start and end weeks
	 */
	class ActionListenerFreeDesks implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			int newStart = (Integer) comboDateBegin.getSelectedItem();
			int newStartYear = (Integer) comboYearBegin.getSelectedItem();
			int newEnd = (Integer) comboDateEnd.getSelectedItem();
			int newEndYear = (Integer) comboYearEnd.getSelectedItem();
			
			List<Desk> avalableDesk = db.getFreeDesks(new Week(newStart, newStartYear), new Week(newEnd, newEndYear));
			if (update) avalableDesk.add(oldRes.getDesk());
			comboDesk.setModel(new DefaultComboBoxModel(avalableDesk.toArray()));
			if (update) comboDesk.setSelectedItem(oldRes.getDesk());
			
			repaint();
		}
	}
	
	class ReservationKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				validation();
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				cancel();
		}
	}
	
}
