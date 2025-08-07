package com.github.matschieu.rgb.controller;

import com.github.matschieu.rgb.model.RGBModel;
import com.github.matschieu.rgb.view.RGBView;

/**
 * @author Matschieu
 */
public class RGBController {
	
	private RGBModel model;
	private RGBView view;
	
	public RGBController(RGBModel model) {
		this.model = model;
	}
	
	public void setRValue(int r) {
		if (r >= 0 && r < 256)
			this.model.setR(r);
		else
			this.model.setR(this.model.getR());
	}

	public void setGValue(int g) {
		if (g >= 0 && g < 256)
			this.model.setG(g);
		else
			this.model.setG(this.model.getG());
	}

	public void setBValue(int b) {
		if (b >= 0 && b < 256)
			this.model.setB(b);
		else
			this.model.setB(this.model.getB());
	}

	public void setView(RGBView view) {
		this.view = view;
	}

}
