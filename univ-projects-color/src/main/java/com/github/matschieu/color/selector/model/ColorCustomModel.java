package com.github.matschieu.color.selector.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Matschieu
 */
public class ColorCustomModel extends ColorAutoModel {

	public static final int MAX_INIT_COLOR = 25;
	
	private List<MColor> initialColors;

	public ColorCustomModel() {
		super();
		this.initialColors = new LinkedList<MColor>();
	}

	public List<MColor> getInitialColors() {
		return initialColors;
	}

	public void generate() {
		int partSize = 256 / (this.initialColors.size() + 1);
		this.colorsList.clear();
		for(int i = 0; i < this.initialColors.size(); i++) {
			this.colorsList.add(getColor((i + 1) * partSize));
		}
	}
	
}
