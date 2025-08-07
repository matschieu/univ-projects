package com.github.matschieu.desks.model.util;

/**
 * @author Matschieu
 */
public class Desk implements Comparable<Desk>, Data {

	private int id;
	private String number;
	private String building;
	private String room;
	private String description;
	
	public Desk() { }
	
	public Desk(int id, String number, String building, String room, String description) {
		this.id = id;
		this.number = number;
		this.building = building;
		this.room = room;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getBuilding() {
		return building;
	}
	
	public void setBuilding(String building) {
		this.building = building;
	}
	
	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		str.append("Bureau ");
		str.append(this.number);
		str.append(" [Bâtiment ");
		str.append(this.building);
		str.append(" / Salle ");
		str.append(this.room);
		str.append(" / ");
		str.append(this.description);
		str.append("]");
		return str.toString();
	}

	public int compareTo(Desk d) {
		return this.getNumber().compareTo(d.getNumber());
	}
	
	public boolean equals(Object obj) {
		return this.id == ((Desk)obj).getId();
	}
	
	public Desk duplicate() {
		return new Desk(this.id, this.number, this.building, this.room, this.description);
	}
	
}
