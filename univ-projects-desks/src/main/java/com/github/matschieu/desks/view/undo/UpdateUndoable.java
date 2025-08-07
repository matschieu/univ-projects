package com.github.matschieu.desks.view.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import com.github.matschieu.desks.controller.DataController;
import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public class UpdateUndoable<D extends Data> extends AbstractUndoableEdit {

	private DataController<D> ctr;
	private D oldData;
	private D newData;
	
	public UpdateUndoable(DataController<D> ctr, D oldData, D newData) {
		this.ctr = ctr;
		this.oldData = oldData;
		this.newData = newData;
	}

	public void redo() throws CannotRedoException {
		super.redo();
		this.ctr.update(this.newData);
	}

	public void undo() throws CannotUndoException {
		super.undo();
		this.ctr.update(this.oldData);
	}
		
}
