package com.github.matschieu.desks.view.copypaste;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public final class CopyPaste<D extends Data> implements ClipboardOwner {
	
	private boolean canPaste;
	
	public CopyPaste() {
		this.canPaste = false;
	}
	
	public boolean canPaste() {
		return this.canPaste;
	}
	
	public void lostOwnership(Clipboard cb, Transferable t) {
		this.canPaste = false;
	}

	public void setClipboardContents(D d) {
		TransferableData<D> tp = new TransferableData<D>(d);
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		cb.setContents(tp, this);
		this.canPaste = true;
	}

	public Object getClipboardContents() {
		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try {
			if (t != null && t.isDataFlavorSupported(TransferableData.dataFlavor)) {
				return t.getTransferData(TransferableData.dataFlavor);
			}
		}
		catch (Exception e) { } 
		return null;
	}
}