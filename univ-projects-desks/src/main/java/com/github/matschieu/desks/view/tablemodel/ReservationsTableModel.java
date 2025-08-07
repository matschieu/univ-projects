package com.github.matschieu.desks.view.tablemodel;

import java.util.List;

import com.github.matschieu.desks.model.util.Reservation;

/**
 * A JTable model for Desk
 * @author Matschieu
 */
public class ReservationsTableModel extends TableModelAdapter<Reservation> {

	private final String[] COLUMNS_NAME = { "Début", "Fin", "Bureau", "Batiment", "Salle", "Manager","Stagiaire" };
	private List<Reservation> list;
	
	public ReservationsTableModel(List<Reservation> list) {
		this.list = list;
	}

	public int getColumnCount() {
		return COLUMNS_NAME.length;
	}

	public String getColumnName(int columnIndex) {
		return COLUMNS_NAME[columnIndex];
	}

	public int getRowCount() {
		return list.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Reservation r = list.get(rowIndex);
		switch(columnIndex) {
			case 0 : return r.getStart().toDateFirst();
			case 1 : return r.getEnd().toDateLast();
			case 2 : return r.getDesk().getNumber();
			case 3 : return r.getDesk().getBuilding();
			case 4 : return r.getDesk().getRoom();
			case 5 : return r.getManager().getFirstName() + " " + r.getManager().getName();
			case 6 : return r.getTrainee().getFirstName() + " " + r.getTrainee().getName();
		}
		return null;
	}
	
	public Reservation getDataAtRow(int row) {
		return list.get(row);
	}
	
}