package com.github.matschieu.mfile.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JTree;

/**
 * @author Matschieu
 */
public class FileTree extends JTree {

	private static final long serialVersionUID = -8581874817183872472L;

	public FileTree(String rootPathname) {
		this.setCellRenderer(new FileTreeCellRenderer(this));
		this.putClientProperty("JTree.lineStyle", "None");
		try {
			this.setModel(new FileTreeModel(rootPathname, false));
		}
		catch(final IOException e) { }

		this.addMouseListener(new FileTreeMouseListener(this));
	}

}

class FileTreeMouseListener implements MouseListener {

	private final FileTree tree;

	public FileTreeMouseListener(FileTree tree) {
		this.tree = tree;
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("sur: " + this.tree.getLastSelectedPathComponent());

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
