package com.github.matschieu.color.selector.view;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

/**
 * @author Matschieu
 */
public class ColorTextFieldTransferHandler extends TransferHandler {

	public int getSourceActions(JComponent c) {
		return COPY;
	}

	protected Transferable createTransferable(JComponent c) {
		return new StringSelection(((JTextField)c).getText());
	}

	protected void exportDone(JComponent source, Transferable data, int action) {
		if (action == MOVE) {
			((JTextField)source).setText("");
		}
	}

	public boolean importData(JComponent comp, TransferHandler.TransferSupport support) {
		return true;
	}

}
