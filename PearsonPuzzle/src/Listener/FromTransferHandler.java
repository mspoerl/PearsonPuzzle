package Listener;


import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * 
 * 
 * @author workspace
 *
 */
public class FromTransferHandler extends TransferHandler {
	JList dragFrom;
	DefaultListModel dragList;
	//DefaultListModel from;
	public FromTransferHandler(DefaultListModel dragList, JList dragFrom){
		this.dragList=dragList;
		this.dragFrom=dragFrom;
	}
	
    public int getSourceActions(JComponent comp) {
        return COPY_OR_MOVE;
    }
    
    private int index = 0;
    public Transferable createTransferable(JComponent comp) {
        index = dragFrom.getSelectedIndex();        
        if (index < 0 || index >= dragList.getSize()) {
            return null;
        }
        return new StringSelection((String)dragFrom.getSelectedValue());
    }
    
    public void exportDone(JComponent comp, Transferable trans, int action) {
        if (action != MOVE) {
        	return;
        }
        dragList.removeElementAt(index);
    }
}
