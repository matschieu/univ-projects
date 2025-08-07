package com.github.matschieu.desks.view.tablemodel;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public abstract class TableModelAdapter<D extends Data> implements TableModel {

	public void addTableModelListener(TableModelListener arg0) { }

	public Class<?> getColumnClass(int columnIndex) {
		return (new String()).getClass();
	}

	public int getColumnCount() {
		return 0;
	}

	public String getColumnName(int arg0) {
		return null;
	}

	public int getRowCount() {
		return 0;
	}

	public Object getValueAt(int arg0, int arg1) {
		return null;
	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	public void removeTableModelListener(TableModelListener arg0) { }

	public void setValueAt(Object arg0, int arg1, int arg2) { }

	public abstract D getDataAtRow(int row);
	
}
