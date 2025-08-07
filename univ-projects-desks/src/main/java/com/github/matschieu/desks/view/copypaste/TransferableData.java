package com.github.matschieu.desks.view.copypaste;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public class TransferableData<D extends Data> implements Transferable {

	public static DataFlavor dataFlavor;
	private D data;

	public TransferableData(D data) {
		String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=" + data.getClass().getName();
		try {
			dataFlavor = new DataFlavor(mimeType);
			this.data = data;
		} catch (ClassNotFoundException e) { }
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor==null)
			throw new IOException();
		if (this.isDataFlavorSupported(flavor)) {
			if (flavor.equals(dataFlavor))
				return this.data;
			if (flavor.equals(DataFlavor.stringFlavor))
				return this.data.toString();
		}
		else
			throw new UnsupportedFlavorException(flavor);
		return false;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { dataFlavor, DataFlavor.stringFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(dataFlavor) || flavor.equals(DataFlavor.stringFlavor);
	}

}
