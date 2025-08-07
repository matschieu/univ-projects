package com.github.matschieu.mfile.view;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Matschieu
 */
public class MFileNavigatorFrame extends JFrame implements Observer {

	private static final long serialVersionUID = -2311561667843056344L;

	private final FileTree tree;

	public MFileNavigatorFrame(String rootPathname) {
		this.tree = new FileTree(rootPathname);
		tree.setSelectionInterval(2, 2);

		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(this.tree), BorderLayout.WEST);

		final JTextArea ta = new JTextArea();
		final File f = (File)this.tree.getSelectionPath().getLastPathComponent();
		ta.setText("" + f.getParentFile().getPath() + "\n" +
				f.getName() + "\n" + f.getTotalSpace() + "\n\n");
		for(int i = 0; i < f.list().length; i++)
			ta.setText(ta.getText() + f.list()[i] + "\n");
		this.add(ta, BorderLayout.CENTER);
		this.add(new JLabel("" + this.tree.getSelectionPath().getLastPathComponent()), BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
