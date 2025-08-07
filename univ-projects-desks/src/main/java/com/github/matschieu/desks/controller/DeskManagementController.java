package com.github.matschieu.desks.controller;

import javax.swing.JOptionPane;

import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Desk;

/**
 * @author Matschieu
 */
public class DeskManagementController implements DataController<Desk> {
	
	private DataBase db;
	
	/**
	 * Creates a new desk management controller
	 * @param db the model
	 */
	public DeskManagementController(DataBase db) {
		this.db = db;
	}
	
	/**
	 * Adds a new Desk
	 * @param d the desk to add
	 */
	public void addDesk(Desk d) {
		if (d == null)
			return;
		if (!this.db.deskExists(d))
			this.db.addDesk(d);
		else {
			String msg = "Le bureau " + d.getNumber() + " existe déjà";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Removes a Desk
	 * @param d the desk to remove
	 */
	public void removeDesk(Desk d) {
		if (d == null)
			return;
		if (this.db.deskExists(d))
			this.db.removeDesk(d);
		else {
			String msg = "Le bureau " + d.getNumber() + " n'existe pas";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Updates a Desk
	 * @param d the desk to update
	 */
	public void updateDesk(Desk d) {
		if (d == null)
			return;
		if (this.db.deskExists(d))
			this.db.updateDesk(d);
		else {
			String msg = "Le bureau " + d.getNumber() + " n'existe pas";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void add(Desk d) {
		this.addDesk(d);
	}

	public void remove(Desk d) {
		this.removeDesk(d);
	}

	public void update(Desk d) {
		this.updateDesk(d);
	}

}
