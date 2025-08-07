package com.github.matschieu.desks.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

import com.github.matschieu.desks.model.util.Desk;
import com.github.matschieu.desks.model.util.Person;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.model.util.Week;

/**
 * @author Matschieu
 */
public abstract class DataBase extends Observable {

	/**
	 * Creates the database (first use)
	 * @return true if the database is correctly created
	 */
	public abstract boolean createDataBase();
	
	/**
	 * Adds the desk d to the dataBase
	 * @param d desk to add
	 */
	public abstract void addDesk(Desk d);

	/**
	 * Updates the desk d in the dataBase
	 * @param d desk to update
	 */
	public abstract void updateDesk(Desk d);

	/**
	 * Removes the desk d of the dataBase
	 * @param d desk to remove
	 */
	public abstract void removeDesk(Desk d);

	/**
	 * Checks if a desk already exists
	 * @return true if exist 
	 */
	public abstract boolean deskExists(Desk d);

	/**
	 * Returns all desks existing
	 * @return List<Desk> a list of all desks
	 */
	public abstract List<Desk> getAllDesk();

	/**
	 * Returns the desk with the desk id deskId
	 * @param deskId the desk id desk to get
	 * @return the desk with the id deskId
	 */
	public abstract Desk getDesk(String deskId);

	/**
	 * Add the person p to the dataBase
	 * @param d person to add
	 */
	public abstract void addPerson(Person p);

	/**
	 * Updates the person p in the dataBase
	 * @param p person to update
	 */
	public abstract void updatePerson(Person p);

	/**
	 * Remove the person d of the dataBase
	 * @param p person to remove
	 */
	public abstract void removePerson(Person p);

	/**
	 * Returns the person with the person id personId
	 * @param personId the person id desk to get
	 * @return Person the person with the id personId
	 */
	public abstract Person getPerson(String personId);

	/**
	 * Checks if a person already exists
	 * @return true if exist
	 */
	public abstract boolean personExists(Person p);

	/**
	 * Returns all persons existing
	 * @return List<Person> a list of all persons
	 */
	public abstract List<Person> getAllPerson();

	/**
	 * Returns all persons existing who are trainee or not
	 * @param trainee select only trainees if true, else other persons
	 * @return List<Person> a list of persons
	 */
	public abstract List<Person> getAllPerson(boolean trainee);

	/**
	 * Checks if there's an homonyme in the database
	 * @return true if the person p has an homonyme
	 */
	public abstract boolean containsHomonyme(Person p);
	
	/**
	 * Checks if a reservation already exists
	 * @return true if exist 
	 */
	public abstract boolean reservationExists(Reservation r);
	
	/**
	 * Returns the reservation with the id resId
	 * @param resId the id reservation to get
	 * @return the reservation with the id resId
	 */
	public abstract Reservation getReservation(String resId);

	/**
	 * Adds the reservation r to the dataBase
	 * @param r reservation to add
	 */
	public abstract void addReservation(Reservation r);

	/**
	 * Updates the reservation r in the dataBase
	 * @param r reservation to update
	 */
	public abstract void updateReservation(Reservation r);

	/**
	 * Remove the reservation of the dataBase
	 * @param r reservation to remove
	 */
	public abstract void removeReservation(Reservation r);

	/**
	 * Returns all reservations existing
	 * @return List<Reservation> a list of all reservations
	 */
	public abstract List<Reservation> getAllReservation();
	
	/**
	 * Returns all reservations existing at a specified week
	 * @return List<Reservation> a list of all reservations
	 */
	public abstract List<Reservation> getAllReservation(Week week);
	
	/**
	 * Returns all reservations where a person appears
	 * @param p the person (trainee or manager)
	 * @return List<Reservation> 
	 */
	public abstract List<Reservation> getReservations(Person p);
	
	/**
	 * Returns all reservations for a desk
	 * @param d the desk
	 * @return List<Reservation> 
	 */
	public abstract List<Reservation> getReservations(Desk d);
	
	/**
	 * Updates the reservation r in the dataBase a a specified week
	 * @param oldRes the old reservation
	 * @param newRes the new reservation
	 */
	public void updateReservation(Reservation oldRes, Reservation newRes){
		
		Week week = newRes.getStart();
		
		removeReservation(oldRes);

		addReservation(newRes);
		
		if (oldRes.getStart().compareTo(week)<0) addReservation(new Reservation("", oldRes.getDesk(), oldRes.getManager(), oldRes.getTrainee(), oldRes.getStart(), week.previousWeek()));
		if (oldRes.getEnd().compareTo(week)>0) addReservation(new Reservation("", oldRes.getDesk(), oldRes.getManager(), oldRes.getTrainee(), week.nextWeek(), oldRes.getEnd()));
	}
	
	/**
	 * Removes the reservation r in the dataBase a a specified week
	 * @param oldRes the old reservation
	 * @param week the week when remove the reservation
	 */
	public void removeReservation(Reservation oldRes, Week week){
		
		removeReservation(oldRes);
		
		if (oldRes.getStart().compareTo(week)<0) addReservation(new Reservation("", oldRes.getDesk(), oldRes.getManager(), oldRes.getTrainee(), oldRes.getStart(), week.previousWeek()));
		if (oldRes.getEnd().compareTo(week)>0) addReservation(new Reservation("", oldRes.getDesk(), oldRes.getManager(), oldRes.getTrainee(), week.nextWeek(), oldRes.getEnd()));
	}
	
	/**
	 * Get the number of free desk for the week w
	 * @param w the week
	 * @return the number of free desk for the week w
	 */
	public int getFreeDeskCount(Week w) {
		int usedDesks = 0;
		for (Reservation res : getAllReservation()) {
			if (res.getStart().compareTo(w) <= 0 && res.getEnd().compareTo(w) >= 0)
				usedDesks ++;
		}
		return getTotalDeskCount() - usedDesks;

	}

	/**
	 * Get the number of all desks
	 * @return the number of all desks
	 */
	public int getTotalDeskCount() {
		return getAllDesk().size();
	}

	/**
	 * returns the list of free desks at a specified week
	 * @param week 
	 * @return the list of free desks at a specified week
	 */
	public List<Desk> getFreeDesks(Week week){
		
		List<Desk> reservationDesks = new LinkedList<Desk>(); 
		List<Desk> result = new LinkedList<Desk>(); 
		
		for (Reservation res : getAllReservation(week))
			reservationDesks.add(res.getDesk());
		
		for (Desk desk : getAllDesk()) {
			if (!reservationDesks.contains(desk) && !result.contains(desk)){
				result.add(desk);
			}
		}
		return result;
	}
	
	/**
	 * returns the list of free desks between the start and the end week 
	 * @param start the start week
	 * @param end the end week
	 * @return the list of free desks between the start and the end week 
	 */
	public List<Desk> getFreeDesks(Week start, Week end){
		
		Week tmp = start;
		
		List<Desk> result = getAllDesk();

		while (tmp.compareTo(end)<=0){

			for (Desk desk : getAllDesk()) {
				if (!getFreeDesks(tmp).contains(desk) && result.contains(desk))
					result.remove(desk);
			}
			tmp = tmp.nextWeek();
		}
		return result;
	}

}
