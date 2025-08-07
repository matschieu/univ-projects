package com.github.matschieu.desks.controller;

import javax.swing.JOptionPane;

import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Person;

/**
 * @author Matschieu
 */
public class PersonManagementController implements DataController<Person> {

	private DataBase db;
	
	/**
	 * Creates a new person management controller
	 * @param db the model
	 */
	public PersonManagementController(DataBase db) {
		this.db = db;
	}
	
	/**
	 * Adds a new person
	 * @param p the person to add
	 */
	public void addPerson(Person p) {
		if (p == null)
			return;
		if (!this.db.containsHomonyme(p))
			this.db.addPerson(p);
		else {
			String msg = "La personne " + p.getFirstName() + " " + p.getName() + " existe déjà";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Removes a person
	 * @param p the person to remove
	 */
	public void removePerson(Person p) {
		if (p == null)
			return;
		if (this.db.personExists(p))
			this.db.removePerson(p);
		else {
			String msg = "La personne " + p.getFirstName() + " " + p.getName() + " n'existe pas";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Updates a person
	 * @param p the person to update
	 */
	public void updatePerson(Person p) {
		if (p == null)
			return;
		if (this.db.personExists(p))
			this.db.updatePerson(p);
		else {
			String msg = "La personne " + p.getFirstName() + " " + p.getName() + " n'existe pas";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	public void add(Person p) {
		this.addPerson(p);
	}

	public void remove(Person p) {
		this.removePerson(p);
	}

	public void update(Person p) {
		this.updatePerson(p);
	}

}
