package com.github.matschieu.desks.model.util;

/**
 * @author Matschieu
 */
public class Reservation implements Comparable<Reservation>, Data {
	
	private String resId;
	private Desk desk;
	private Person manager;
	private Person trainee;
	private Week start;
	private Week end;
	
	public Reservation() { }
	
	public Reservation(String resId, Desk desk, Person manager, Person trainee, Week start, Week end) {
		this.resId = resId;
		this.desk = desk;
		this.manager = manager;
		this.trainee = trainee;
		this.start = start;
		this.end = end;
	}
	
	public String getId() {
		return resId;
	}
	
	public void setId(String id) {
		this.resId = id;
	}

	public Desk getDesk() {
		return desk;
	}
	
	public void setDesk(Desk desk) {
		this.desk = desk;
	}
	
	public Person getManager() {
		return manager;
	}
	
	public void setManager(Person manager) {
		this.manager = manager;
	}
	
	public Person getTrainee() {
		return trainee;
	}
	
	public void setTrainee(Person trainee) {
		this.trainee = trainee;
	}
	
	public Week getStart() {
		return start;
	}
	
	public void setStart(Week start) {
		this.start = start;
	}
	
	public Week getEnd() {
		return end;
	}
	
	public void setEnd(Week end) {
		this.end = end;
	}

	public int compareTo(Reservation o) {
		Week start_ = o.getStart();
		Week end_ = o.getEnd();		
		if (this.start.getYear() < start_.getYear())
			return -1;
		if (this.start.getYear() > start_.getYear())
			return 1;
		else {
			if (this.start.getWeekNumber() < start_.getWeekNumber())
				return -1;
			if (this.start.getWeekNumber() > start_.getWeekNumber())
				return 1;
			else {
				if (this.end.getYear() < end_.getYear())
					return -1;
				if (this.end.getYear() > end_.getYear())
					return 1;
				else {
					if (this.end.getWeekNumber() < end_.getWeekNumber())
						return -1;
					if (this.end.getWeekNumber() > end_.getWeekNumber())
						return 1;
					else
						return 0;
				}
			}
		}
	}
	
	public boolean equals(Reservation r) {
		return this.resId.equals(r.getId()) && this.desk.equals(r.getDesk()) && this.manager.equals(r.getManager()) && this.trainee.equals(r.getTrainee()) && this.start.equals(r.getStart()) && this.end.equals(r.getEnd());
	}
	
	public Reservation duplicate() {
		return new Reservation(this.resId, this.desk, this.manager, this.trainee, this.start, this.end);
	}
}
