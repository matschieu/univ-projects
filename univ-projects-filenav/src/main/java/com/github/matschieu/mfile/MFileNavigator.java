package com.github.matschieu.mfile;

import javax.swing.SwingUtilities;

import com.github.matschieu.mfile.view.MFileNavigatorFrame;


/**
 * @author Matschieu
 */
public class MFileNavigator implements Runnable {

	MFileNavigatorFrame frame;

	public MFileNavigator(String rootPathname) {
		this.frame = new MFileNavigatorFrame(rootPathname);
	}

	@Override
	public void run() {
		this.frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new MFileNavigator("/home/mathieu"));
	}

}
