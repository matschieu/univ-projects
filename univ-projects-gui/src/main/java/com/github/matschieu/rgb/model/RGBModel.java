package com.github.matschieu.rgb.model;

import java.util.Observable;

/**
 * @author Matschieu
 */
public class RGBModel extends Observable {
	
	private int r; // Red
	private int g; // Green
	private int b; // Blue
	
	public RGBModel(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public void setR(int r) {
		this.r = r;
		setChanged();
		notifyObservers();
	}
		
	public void setG(int g) {
		this.g = g;
		setChanged();
		notifyObservers();
	}

	public void setB(int b) {
		this.b = b;
		setChanged();
		notifyObservers();
	}

	public int getR() {
		return this.r;
	}
	
	public int getG() {
		return this.g;
	}
	
	public int getB() {
		return this.b;
	}

}
