package com.github.matschieu.desks.model.util;

/**
 * @author Matschieu
 */
public class Person implements Comparable<Person>, Data {
	
	private int id;
	private String name;
	private String firstName;
	private String post;
	private boolean trainee;
	
	public Person() { }
	
	public Person(int id, String name, String firstName, String post, boolean trainee) {
		this.id = id;
		this.name = name;
		this.firstName = firstName;
		this.post = post;
		this.trainee = trainee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public boolean isTrainee() {
		return trainee;
	}

	public void setTrainee(boolean trainee) {
		this.trainee = trainee;
	}

	public int compareTo(Person p) {
		String thisFullName = this.name + " " + this.firstName;
		String pFullName = p.getName() + " " + p.getFirstName();
		return thisFullName.compareTo(pFullName);
	}
	
	public String toString() {
		return this.firstName + " " + this.name;
	}
	
	public Person duplicate() {
		return new Person(this.id, this.name, this.firstName, this.post, this.trainee);
	}
	
	public boolean equals(Person p){
		return id == p.getId();
	}
	
}
