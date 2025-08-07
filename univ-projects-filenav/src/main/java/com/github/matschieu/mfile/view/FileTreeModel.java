package com.github.matschieu.mfile.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author Matschieu
 */
public class FileTreeModel implements TreeModel {

	private final File rootFile;
	private final boolean showHiddenFiles;

	public FileTreeModel(String rootFileName, boolean showHiddenFiles) throws FileNotFoundException {
		this.rootFile = new File(rootFileName);
		this.showHiddenFiles = showHiddenFiles;
		if (!this.rootFile.exists())
			throw new FileNotFoundException();
	}

	@Override
	public Object getChild(Object parent, int index) {
		final File parentFile = ((File)parent);
		final String[] filesList = parentFile.list();

		final List<File> directories = new LinkedList<>();
		final List<File> files = new LinkedList<>();

		for(int i = 0; i < filesList.length; i++) {
			if (!showHiddenFiles && filesList[i].charAt(0) == '.')
				continue;
			final File f = new File(parentFile.getAbsolutePath() + File.separator + filesList[i]);
			if (f.isDirectory())
				directories.add(f);
			else
				files.add(f);
		}

		Collections.sort(directories);
		Collections.sort(files);

		directories.addAll(files);
		if (index > directories.size() - 1)
			return null;

		return directories.get(index);
	}

	@Override
	public int getChildCount(Object parent) {
		final String[] filesList = ((File)parent).list();
		int n = 0;

		for(int i = 0; i < filesList.length; i++) {
			if (!showHiddenFiles && filesList[i].charAt(0) == '.')
				continue;
			n++;
		}

		return n;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		final File parentFile = ((File)parent);
		final String[] filesList = parentFile.list();

		final List<File> directories = new LinkedList<>();
		final List<File> files = new LinkedList<>();

		for(int i = 0; i < filesList.length; i++) {
			if (!showHiddenFiles && filesList[i].charAt(0) == '.')
				continue;
			final File f = new File(parentFile.getAbsolutePath() + File.separator + filesList[i]);
			if (f.isDirectory())
				directories.add(f);
			else
				files.add(f);
		}

		Collections.sort(directories);
		Collections.sort(files);

		directories.addAll(files);

		int idx = 0;
		for(final File f : directories) {
			if (f.equals(child))
				return idx;
			idx++;
		}

		return 0;
	}

	@Override
	public Object getRoot() {
		return this.rootFile;
	}

	@Override
	public boolean isLeaf(Object node) {
		return !((File)node).isDirectory();
	}

	@Override
	public void valueForPathChanged(TreePath arg0, Object arg1) { }

	@Override
	public void removeTreeModelListener(TreeModelListener arg0) { }

	@Override
	public void addTreeModelListener(TreeModelListener arg0) { }

}
