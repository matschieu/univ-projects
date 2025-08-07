package com.github.matschieu.desks.view.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.github.matschieu.desks.controller.DataController;
import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public class AddUndoable<D extends Data> extends AbstractUndoableEdit {

	private DataController<D> ctr;
	private D data;
	
	public AddUndoable(DataController<D> ctr, D data) {
		this.ctr = ctr;
		this.data = data;
	}

	public void redo() throws CannotRedoException {
		super.redo();
		this.ctr.add(this.data);
	}

	public void undo() throws CannotUndoException {
		super.undo();
		this.ctr.remove(this.data);
	}
	
}
