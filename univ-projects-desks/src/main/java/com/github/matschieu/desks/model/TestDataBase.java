package com.github.matschieu.desks.model;

import java.util.LinkedList;
import java.util.List;

import com.github.matschieu.desks.model.util.Desk;
import com.github.matschieu.desks.model.util.Person;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.model.util.Week;

/**
 * @author Matschieu
 */
public class TestDataBase extends DataBase {

	private List<Desk> deskList;
	private List<Reservation> resList;
	private List<Person> personList;

	public TestDataBase() {
		deskList = new LinkedList<Desk>();
		resList = new LinkedList<Reservation>();
		personList = new LinkedList<Person>();

		for(int i = 0; i < 10; i++)
			deskList.add(new Desk(i, "" + i, "Bat. " + i, "Salle " + i, "descr"));

		personList.add(new Person(1, "Terieur", "Alain", "", true));
		personList.add(new Person(2, "Terieur", "Alex", "", false));
		personList.add(new Person(3, "Toto", "Titi", "", true));

		//resList.add(new Reservation("1", deskList.get(0), personList.get(0), personList.get(1), 3, 5, 2010));
		//resList.add(new Reservation("2", deskList.get(1), personList.get(0), personList.get(2), 8, 9, 2011));

	}

	/**
	 * Add the desk d to the dataBase
	 * @param d desk to add
	 */
	public void addDesk(Desk d) {
		deskList.add(d);
		setChanged();
		notifyObservers();
	}

	/**
	 * Updates the desk d in the dataBase
	 * @param d desk to update
	 */
	public void updateDesk(Desk d) {
		int i = 0;
		if (!deskList.contains(d))
			return;
		for(Desk d_ : deskList) {
			if (d_.equals(d))
				break;
			i++;
		}
		deskList.set(i, d);
		setChanged();
		notifyObservers();		
	}

	/**
	 * Remove the desk d of the dataBase
	 * @param d desk to remove
	 */
	public void removeDesk(Desk d) {
		if (!deskList.contains(d))
			return;
		deskList.remove(d);
		setChanged();
		notifyObservers();
	}

	/**
	 * Checks if a desk already exists
	 * @return true if a desk with the same id exists
	 */
	public boolean deskExists(Desk d) {
		for(Desk d_ : deskList) {
			if (d.getNumber() == d_.getNumber())
				return true;
		}
		return false;
	}	

	/**
	 * Returns all desks existing
	 * @return List<Desk> a list of all desks
	 */
	public List<Desk> getAllDesk() {
		return deskList;
	}

	/**
	 * Get the desk with the desk id deskId
	 * @param deskId the desk id desk to get
	 * @return the desk with the desk id deskId
	 */
	public Desk getDesk(String deskId) {
		for (Desk d : getAllDesk()) {
			if (d.getNumber().equals(deskId)){
				return d;
			}
		}
		return null;
	}

	/**
	 * Add the person p to the dataBase
	 * @param d person to add
	 */
	public void addPerson(Person p) {
	}

	/**
	 * Remove the person d of the dataBase
	 * @param p person to remove
	 */
	public void removePerson(Person p) {
	}
	
	public Reservation getReservation(String resId){
		return null;
	}

	/**
	 * Remove the reservation of the desk with deskId at the week week
	 * @param deskId desk id of the desk to remove
	 * @param week week of the reservation to remove
	 */
	public void removeReservation(String deskId, Week week){
		/*
		for (Reservation res : getAllReservation()) {
			if (res.getDesk().getDeskId().equals(deskId) && res.getStart() <= week && week < res.getEnd() && week.getYear() == res.getYear()){
				resList.remove(res);
			}
		}*/

		setChanged();
		notifyObservers();
	}
	
	/**
	 * Remove the reservation of the dataBase
	 * @param r reservation to remove
	 */
	public void removeReservation(Reservation r){
		
	}

	public List<Person> getAllPerson() {
		return personList;
	}

	public List<Reservation> getAllReservation() {
		return resList;
	}

	@Override
	public void addReservation(Reservation r) {
		
	}

	@Override
	public void updateReservation(Reservation r) {
		
	}

	@Override
	public Person getPerson(String personId) {
		return null;
	}

	@Override
	public void updatePerson(Person p) {
		
	}

	@Override
	public boolean reservationExists(Reservation r) {
		return false;
	}

	@Override
	public boolean personExists(Person p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsHomonyme(Person p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Person> getAllPerson(boolean trainee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getAllReservation(Week week) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getReservations(Person p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reservation> getReservations(Desk d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createDataBase() {
		// TODO Auto-generated method stub
		return false;
	}
}
