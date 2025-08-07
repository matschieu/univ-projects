package com.github.matschieu.color.selector.model;

import java.awt.Color;

/**
 * @author Matschieu
 */
public class MColor extends Color {

	public MColor(int v) {
		super(v, v, v);
	}

	public MColor(int r, int g, int b) {
		super(r, g, b);
	}

	public MColor(Color c) {
		super(c.getRed(), c.getGreen(), c.getBlue());
	}
	
	public int getGrayLevel() {
		return (int)(0.3 * this.getRed() + 0.59 * this.getGreen() + 0.11 * this.getBlue());
	}
	
	public String getHexaString(boolean diese) {
		try {
			String hexCode = "";
			hexCode += (this.getRed() < 16 ? "0" : "") + Integer.toHexString(this.getRed());
			hexCode += (this.getGreen() < 16 ? "0" : "") + Integer.toHexString(this.getGreen());
			hexCode += (this.getBlue() < 16 ? "0" : "") + Integer.toHexString(this.getBlue());
			return (diese ? "#" : "") + hexCode;
		}
		catch(NumberFormatException e) {
			return null;
		}
	}
	
	public boolean equals_(MColor c) {
		int seuil = 30;
		return this.getRed() - seuil < c.getRed() && this.getRed() + seuil > c.getRed() &&
			   this.getGreen() - seuil < c.getGreen() && this.getGreen() + seuil > c.getGreen() &&	
			   this.getBlue() - seuil < c.getBlue() && this.getBlue() + seuil > c.getBlue();
	}

	public boolean equals(Object o) {
		if (!(o instanceof MColor))
			return false;
		Color c = (Color)o;
		return this.getRed() == c.getRed() && this.getGreen() == c.getGreen() && this.getBlue() == c.getBlue();
	}

	public String toString() {
		return "[" + this.getRed() + " " + this.getGreen() + " " + this.getBlue() + "]";
	}

}
