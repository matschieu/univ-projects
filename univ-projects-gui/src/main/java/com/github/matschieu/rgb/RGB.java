package com.github.matschieu.rgb;

import com.github.matschieu.rgb.controller.RGBController;
import com.github.matschieu.rgb.model.RGBModel;
import com.github.matschieu.rgb.view.RGBSelectorView;

/**
 * @author Matschieu
 */
public class RGB {

	public static void main(String[] args) {
		RGBModel model = new RGBModel(223, 116, 23);
		RGBController ctlr = new RGBController(model);
		RGBSelectorView view = new RGBSelectorView(model, ctlr);
		ctlr.setView(view);
	}
}
