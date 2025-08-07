package com.github.matschieu.desks.view.copypaste;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public class TransferData<D extends Data> extends TransferHandler {

	public int getSourceActions(JComponent c){
		return COPY;
	}

	public Transferable createtransferable(D data){
		return new TransferableData<D>(data);
	}

	public boolean canImport(TransferSupport transfer){
		DataFlavor flavor[] = transfer.getDataFlavors();
		for (int i = 0; i < flavor.length; i++) {
			if(flavor[i].equals(TransferableData.dataFlavor))
				return true;
			if(flavor[i].equals(DataFlavor.stringFlavor))
				return true;
		}
		return false;
	}

	public boolean importData(TransferSupport transfer){
		if (!transfer.isDrop())
			return false;

		if (transfer.isDataFlavorSupported(TransferableData.dataFlavor)){
			try {
				Transferable t = transfer.getTransferable();
				t.getTransferData((TransferableData.dataFlavor));
			} catch (Exception e) { }
			return true;
		}
		if (transfer.isDataFlavorSupported(DataFlavor.stringFlavor)){
			return true;
		}
		return false;
	}
	
}
