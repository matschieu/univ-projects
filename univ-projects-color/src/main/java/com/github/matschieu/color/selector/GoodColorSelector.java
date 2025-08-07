package com.github.matschieu.color.selector;

import com.github.matschieu.color.selector.model.ColorAutoModel;
import com.github.matschieu.color.selector.model.ColorCustomModel;
import com.github.matschieu.color.selector.view.GoodColorSelectorFrame;

/**
 * @author Matschieu
 */
public class GoodColorSelector {

	public static void main(String[] args) {	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ColorAutoModel autoModel = new ColorAutoModel();
				ColorCustomModel colorModel = new ColorCustomModel(); 
				new GoodColorSelectorFrame(autoModel, colorModel);
			}
		});
	}
	
}
