package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.view.tablemodel.ReservationsTableModel;

/**
 * @author Matschieu
 */
public class ReservationsView extends JDialog {
	
	public ReservationsView(List<Reservation> list, String title) {
		JTable reservationTable = new JTable(new ReservationsTableModel(list));
		
		reservationTable.setEnabled(false);
		
		JButton okButton = new JButton("Ok");
		JPanel panel = new JPanel();
		panel.add(okButton);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		okButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE)
					dispose();
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(new WindowTitle(title), BorderLayout.NORTH);
		this.add(new JScrollPane(reservationTable), BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);

		this.setMinimumSize(new Dimension(700, 500));
		this.setModal(true);
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);		
	}

}
