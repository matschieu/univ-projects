package com.github.matschieu.desks.view.tablemodel;

import java.util.List;

import com.github.matschieu.desks.model.util.Desk;

/**
 * A JTable model for Desk
 * @author Matschieu
 */
public class DesksTableModel extends TableModelAdapter<Desk> {

	private final String[] COLUMNS_NAME = { "Bureau", "Bâtiment", "Salle", "Description" };
	private List<Desk> list;
	
	public DesksTableModel(List<Desk> list) {
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
		Desk d = list.get(rowIndex);
		switch(columnIndex) {
			case 0 : return d.getNumber();
			case 1 : return d.getBuilding();
			case 2 : return d.getRoom();
			case 3 : return d.getDescription();
		}
		return null;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Desk d = list.get(rowIndex);
		switch(columnIndex) {
			case 0 : d.setNumber((String)aValue);
			case 1 : d.setBuilding((String)aValue);
			case 2 : d.setRoom((String)aValue);
			case 3 : d.setDescription((String)aValue);
		}
	}
	
	public Desk getDataAtRow(int row) {
		return list.get(row);
	}
	
}
