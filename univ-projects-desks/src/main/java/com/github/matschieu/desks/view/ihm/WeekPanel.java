package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import com.github.matschieu.desks.controller.ReservationController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Week;

/**
 * @author Matschieu
 */
public class WeekPanel extends JPanel implements Observer{
	
	public static final Dimension PANEL_SIZE = new Dimension(80, 60);
	
	private Week week;
	private DataBase db;
	private JPanel infoPanel;
	
	public WeekPanel(DataBase db, Week week) {
		this.db = db;
		this.week = week;
		
		this.db.addObserver(this);

		init();
	}
	
	/**
	 * initialize components of the panel
	 */
	private void init(){
		setLayout(new BorderLayout());
		setPreferredSize(PANEL_SIZE);
		setBorder(new LineBorder(Color.black));
		setBackground(Color.white);
		addMouseListener(new MouseAdapter() {
			
			public void mouseExited(MouseEvent e) {
				setBackground(WindowTitle.TITLE_BACKGROUND);
				infoPanel.setBackground(Color.white);
			}
			public void mouseEntered(MouseEvent e) {
				setBackground(WindowTitle.TITLE_BACKGROUND);
				infoPanel.setBackground(WindowTitle.TITLE_BACKGROUND);
			}
			public void mouseClicked(MouseEvent e) {
				new WeeklyView(db, new ReservationController(db), new Week(week.getWeekNumber(), week.getYear())).setVisible(true);
			}
		});
		
		/* Informations building */
		String freeDeskText = db.getFreeDeskCount(week) > 1 ? " bureaux libres" : " bureau libre";
		
		int freeDesk = db.getFreeDeskCount(week);
		JLabel info = new JLabel("" + freeDesk);
			
		info.setHorizontalAlignment(JLabel.CENTER);
		info.setFont(new Font("Default", Font.BOLD , 16));
		
		JLabel info2 = new JLabel(freeDeskText);
		info2.setHorizontalAlignment(JLabel.CENTER);
		info2.setFont(new Font("Default", Font.PLAIN , 10));
		
		infoPanel = new JPanel();
		infoPanel.add(info);
		infoPanel.add(info2);
		
		String toolTipText = "<html>" + this.week.toDate() + "<br>";

		if (freeDesk == 0) {
			info.setForeground(Color.RED);
			toolTipText += "<font color=#ff0000>" + db.getFreeDeskCount(week) + freeDeskText + " sur " + this.db.getTotalDeskCount() + "<html>";
		}
		else {
			info.setForeground(new Color(42, 122, 15));
			toolTipText += db.getFreeDeskCount(week) + freeDeskText + " sur " + this.db.getTotalDeskCount() + "<html>";
		}
		
		setBackground(WindowTitle.TITLE_BACKGROUND);
		infoPanel.setBackground(Color.WHITE);
		setToolTipText(toolTipText);
		add(new JLabel("" + week.getWeekNumber()) , BorderLayout.NORTH);
		add(infoPanel, BorderLayout.CENTER);	
	}

	public void update(Observable o, Object arg) {
		removeAll();
		init();
		updateUI();
	}
}
