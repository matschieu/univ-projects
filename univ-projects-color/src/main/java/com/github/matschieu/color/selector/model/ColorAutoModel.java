package com.github.matschieu.color.selector.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Matschieu
 */
public class ColorAutoModel {
	
	protected List<ColorList> colorsList;
	protected int nbColor;

	public ColorAutoModel() { 
		this.colorsList = new LinkedList<ColorList>();
		this.nbColor = 0;
	}
	
	public List<ColorList> getColorsList() {
		return this.colorsList;
	}

	public int getNbColor() {
		return nbColor;
	}

	public void setNbColor(int nbColor) {
		this.nbColor = nbColor;
		this.colorsList.clear();
	}

	public void generate() {
		int partSize = 256 / (this.nbColor + 1);
		this.colorsList.clear();
		for(int i = 0; i < this.nbColor; i++) {
			this.colorsList.add(getColor((i + 1) * partSize));
		}
	}

	public static ColorList getColor(int grayLevel) {
		ColorList list = new ColorList();
		for(int i = 0; i < 256; i++) {
			for(int j = 0; j < 256; j++) {
				for(int k = 0; k < 256; k++) {
					MColor c = new MColor(i, j, k);
					if (c.getGrayLevel() == grayLevel && !list.contains_(c)) {
						list.add(c);
					}
				}						
			}
		}
		return list;
	}
	
}
