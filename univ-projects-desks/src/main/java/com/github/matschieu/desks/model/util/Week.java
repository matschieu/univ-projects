package com.github.matschieu.desks.model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * @author Matschieu
 */
public class Week implements Comparator<Week>, Comparable<Week>{

	private int week;
	private int year;
	
	public Week() {
		Calendar c = new GregorianCalendar();
		this.week = c.get(Calendar.WEEK_OF_YEAR);
		this.year = c.get(Calendar.YEAR);
	}
	
	public Week(int weekNumber, int year) {
		this.week = weekNumber;
		this.year = year;
	}

	public int getWeekNumber() {
		return week;
	}

	public void setWeekNumber(int weekNumber) {
		this.week = weekNumber;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public String toString() {
		return "Semaine " + this.week + " - année " + this.year;
	}

	/**
	 * @return this - comp
	 */
	public int compareTo(Week comp) {
		if (year < comp.getYear()) {
			return -1;
		}
		else{
			if (year > comp.getYear()) {
				return 1;
			}
			else {
				return this.week - comp.getWeekNumber();
			}
		}
	}

	public int compare(Week w1, Week w2) {
		if (w1.getYear() < w2.getYear()) {
			return -1;
		}
		else{
			if (w1.getYear() > w2.getYear()) {
				return 1;
			}
			else {
				return w1.getWeekNumber() - w2.getWeekNumber();
			}
		}
	}

	/**
	 * Returns the first day of the week
	 * @return the first day of the week
	 */
	public String toDateFirst() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.WEEK_OF_YEAR, week -1);
		calendar.set(Calendar.YEAR, year);
		
		SimpleDateFormat dateStandart = new SimpleDateFormat("dd/MM/yyyy");				
		return dateStandart.format(calendar.getTime());
	}
	
	/**
	 * Returns the last day of the week
	 * @return the last day of the week
	 */
	public String toDateLast() {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.WEEK_OF_YEAR, week - 1);
		calendar.set(Calendar.YEAR, year);
		
		calendar.add(Calendar.DAY_OF_YEAR, 6);
		
		SimpleDateFormat dateStandart = new SimpleDateFormat("dd/MM/yyyy");				
		return dateStandart.format(calendar.getTime());
	}
	
	/**
	 * Returns start and end dates of the week
	 * @return start and end dates of the week
	 */
	public String toDate() {
		
		return "Du " + toDateFirst() + " au " + toDateLast();
	}
	
	/**
	 * Returns the next week
	 * @return the next week
	 */
	public Week nextWeek(){
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.YEAR, year);
		
		calendar.add(Calendar.WEEK_OF_YEAR, 1);

		return new Week(calendar.get(Calendar.WEEK_OF_YEAR),calendar.get(Calendar.YEAR));
	}
	
	/**
	 * Returns the previous week
	 * @return the previous week
	 */
	public Week previousWeek(){
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.WEEK_OF_YEAR, week);
		calendar.set(Calendar.YEAR, year);
		
		calendar.add(Calendar.WEEK_OF_YEAR, -1);

		return new Week(calendar.get(Calendar.WEEK_OF_YEAR),calendar.get(Calendar.YEAR));
	}
}
