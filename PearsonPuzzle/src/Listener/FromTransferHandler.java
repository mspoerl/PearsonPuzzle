package Listener;


import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
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
	private JList dragJList;
	private DefaultListModel dragDList;
	
	//DefaultListModel from;
	public FromTransferHandler(DefaultListModel dragDList, JList dragJList){
		this.dragDList=dragDList;
		this.dragJList=dragJList;
	}
	// ----- ExportMethoden
    public int getSourceActions(JComponent comp) {
        return COPY_OR_MOVE;
    }
    
    private int index = 0;
    public Transferable createTransferable(JComponent comp) {
        index = dragJList.getSelectedIndex();        
        if (index < 0 || index >= dragDList.getSize())
            return null;
        return new StringSelection((String)dragJList.getSelectedValue());
    }
    
    public void exportDone(JComponent comp, Transferable trans, int action) {
        System.out.println(action+this.toString());
    	if (action != MOVE) {
        	dragJList.clearSelection();
    		return;
        }
        if(action == MOVE){
        	dragDList.removeElementAt(index);
        	dragJList.clearSelection();
        	return;
        }
    }
}
