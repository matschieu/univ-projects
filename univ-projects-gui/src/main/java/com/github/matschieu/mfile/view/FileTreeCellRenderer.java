package com.github.matschieu.mfile.view;

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Matschieu
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -7543934120883335175L;

	public FileTreeCellRenderer(JTree tree) {
		final Icon openIcon = new ImageIcon(this.getClass().getResource("/img/minus.png"));
		final Icon closedIcon = new ImageIcon(this.getClass().getResource("/img/plus.png"));
		((javax.swing.plaf.basic.BasicTreeUI)tree.getUI()).setExpandedIcon(openIcon);
		((javax.swing.plaf.basic.BasicTreeUI)tree.getUI()).setCollapsedIcon(closedIcon);
	}

	@Override
	public Icon getClosedIcon() {
		return new ImageIcon(this.getClass().getResource("/img/folder.png"));
	}

	@Override
	public Icon getLeafIcon() {
		return new ImageIcon(this.getClass().getResource("/img/file.png"));
	}

	@Override
	public Icon getOpenIcon() {
		return new ImageIcon(this.getClass().getResource("/img/open_folder.png"));
	}

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		final JLabel l = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value instanceof File)
			l.setText(((File)value).getName());
		return l;
	}

}
