package com.github.matschieu.desks.controller;

import javax.swing.JOptionPane;

import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.model.util.Week;

/**
 * @author Matschieu
 */

public class ReservationController implements DataController<Reservation> {

	private DataBase db;

	/**
	 * Creates a new desk management controller
	 * @param db the model
	 */
	public ReservationController(DataBase db) {
		this.db = db;
	}

	/**
	 * Adds the reservation r to the dataBase
	 * @param r reservation to add
	 */
	public void addReservation(Reservation r) {
		if (!showErrorMessage(r) && isValidDesk(r))
			this.db.addReservation(r);
	}

	/**
	 * Updates the reservation r in the dataBase
	 * @param r reservation to update
	 */
	public void updateReservation(Reservation r) {
		if (!showErrorMessage(r))
			this.db.updateReservation(r);
	}
	
	/**
	 * Updates the reservation r in the dataBase a a specified week
	 * @param oldRes the old reservation
	 * @param newRes the new reservation
	 */
	public void updateReservation(Reservation oldRes, Reservation newRes) {
		if (!showErrorMessage(newRes) && !oldRes.equals(newRes))
			this.db.updateReservation(oldRes, newRes);
	}

	/**
	 * Removes the reservation of the dataBase
	 * @param r reservation to remove
	 */
	public void removeReservation(Reservation r) {
		this.db.removeReservation(r);
	}
	
	/**
	 * Removes the reservation r in the dataBase a a specified week
	 * @param oldRes the old reservation
	 * @param week the week when remove the reservation
	 */
	public void removeReservation(Reservation r, Week week) {
		this.db.removeReservation(r, week);
	}

	/**
	 * Returns if the start and end dates are valid
	 * @param r the reservation to check
	 * @return true if the date is valid
	 */
	private boolean isValidDate(Reservation r){
		return r.getStart().compareTo(r.getEnd()) <= 0;
	}

	/**
	 * Returns if all weeks haves a free desk
	 * @param r the reservation to check
	 * @return true if it's valid
	 */
	private boolean isValidFreeDesk(Reservation r){

		Week tmp = r.getStart();
		while (tmp.compareTo(r.getEnd())<=0){
			boolean deskIdExists = false;
			// if the desk id already exists at this week, it's ok
			for (Reservation res : db.getAllReservation(tmp)) {
				if (res.getDesk().getId() == r.getDesk().getId()){
					deskIdExists = true;
				}
			} 
			if (!deskIdExists && db.getFreeDeskCount(tmp) < 1){
				return false;
			}
			tmp = tmp.nextWeek();
		}

		return true;
	}

	/**
	 * Returns if a desk id already exists for the weeks of the reservation r
	 * @param r reservation to check
	 * @return true if it's valid
	 */
	private boolean isValidDesk(Reservation r) {
		Week tmp = r.getStart();
		while (tmp.compareTo(r.getEnd())<=0){
			for (Reservation res : db.getAllReservation(tmp)) {
				if (res.getDesk().getId() == r.getDesk().getId()){
					JOptionPane.showMessageDialog(null, "Le bureau sélectionné est déjà utilisé", "Erreur", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} 
			tmp = tmp.nextWeek();
		}
		return true;
	}

	/**
	 * Verify informations and display error messages 
	 * @param r the reservation to check
	 * @return false if an error is occurred
	 */
	private boolean showErrorMessage(Reservation r) {

		if (!isValidDate(r)){
			JOptionPane.showMessageDialog(null, "Les dates de début et de fin ne sont pas valides", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(!isValidFreeDesk(r)){
			JOptionPane.showMessageDialog(null, "Il n'y a pas assez de bureau disponibles pour une des semaines choisies", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}

	public void add(Reservation r) {
		this.addReservation(r);
	}

	public void remove(Reservation r) {
		this.removeReservation(r);
	}

	public void update(Reservation r) {
		this.updateReservation(r);
	}
}
