package com.github.matschieu.desks.view.tablemodel;

import java.util.List;

import com.github.matschieu.desks.model.util.Person;

/**
 * @author Matschieu
 */
public class PersonsTableModel extends TableModelAdapter<Person> {

	private final String[] COLUMNS_NAME = { "Nom", "Prenom", "Poste", "Stagiaire" };
	private List<Person> list;
	
	public PersonsTableModel(List<Person> list) {
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
		Person p = list.get(rowIndex);
		switch(columnIndex) {
			case 0 : return p.getName();
			case 1 : return p.getFirstName();
			case 2 : return p.getPost();
			case 3 : return (p.isTrainee() ? "oui" : "non"); 
		}
		return null;
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Person p = list.get(rowIndex);
		switch(columnIndex) {
			case 0 : p.setName((String)aValue);
			case 1 : p.setFirstName((String)aValue);
			case 2 : p.setPost((String)aValue);
			case 3 : p.setTrainee((Boolean)aValue);
		}
	}
	
	public Person getDataAtRow(int row) {
		return list.get(row);
	}
	
}

