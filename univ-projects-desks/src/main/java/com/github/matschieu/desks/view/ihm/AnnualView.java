package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Week;

/**
 * @author Matschieu
 */
public class AnnualView extends JPanel{
	
	private int nbWeekOnLine;
	private JPanel pnlTop;
	private JPanel pnlWeeks;
	private JLabel lblDescription;
	private JComboBox comboYear;
	private int year;
	private DataBase db;
	
	public AnnualView(DataBase db, int nbWeekOnLine){
		this.db = db;
		this.nbWeekOnLine = nbWeekOnLine;
		
		pnlTop = new JPanel();
		pnlWeeks = new JPanel();
		setLayout(new BorderLayout());

		/* Top panel */
		pnlTop.setLayout(new BorderLayout(10,10));

		// title
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new WindowTitle("Calendrier annuel"), BorderLayout.NORTH);
		//description
		lblDescription = new JLabel("Cliquez sur une semaine pour obtenir plus d'informations ou gérer ses réservations.");

		//year choice
		Integer [] years = new Integer[3];
		this.year = (new GregorianCalendar()).get(Calendar.YEAR);
		for (int i = 0; i < years.length; i++) {
			years[i] = this.year + i;
		}
		comboYear = new JComboBox(years);
		comboYear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				year =  (Integer) (((JComboBox) e.getSource()).getSelectedItem());
				pnlWeeks.removeAll();
				pnlWeeks.add(getWeeksPanels(year));
				updateUI();
			}
		});
		JPanel yearChoice = new JPanel();
		yearChoice.add(new JLabel("Année"));
		yearChoice.add(comboYear);

		
		pnlTop.add(p, BorderLayout.NORTH);
		pnlTop.add(yearChoice);

		/* Weeks panel */
		pnlWeeks.add(getWeeksPanels(this.year));

		/* Frame */
		add(pnlTop, BorderLayout.NORTH);
		add(new JScrollPane(pnlWeeks), BorderLayout.CENTER);
		add(lblDescription, BorderLayout.SOUTH);
	}
	
	/**
	 * Return a panel of week for the specified year
	 * @param year of weeks add
	 * @return panel of week for the specified year
	 */
	private JPanel getWeeksPanels(int year){
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
		
		int nbLine = Math.round(52 / this.nbWeekOnLine) + 1;
		panel.setPreferredSize(new Dimension(
				WeekPanel.PANEL_SIZE.width * this.nbWeekOnLine + (this.nbWeekOnLine + 1) * fl.getHgap(),
				WeekPanel.PANEL_SIZE.height * nbLine + (nbLine + 1) * fl.getVgap()));
				
		panel.setLayout(fl);
		for (int i = 1; i <= 52; i++) {
			panel.add(new WeekPanel(db, new Week(i, year)));	
		}
		return panel;
	}
	
}
