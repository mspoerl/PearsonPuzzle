package Listener;

import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * Klasse definiert, was beim "droppen" mit dem Element passiert, auf das
 * "gedropped" wurde.
 * 
 * @author workspace
 *
 */
public class ToSaveTransferHandler extends TransferHandler {
	private static final long serialVersionUID = 1L;
	int action;
	
	private DefaultListModel<String> dropDList;
	private static DropMode defaultDropmode;
	private JList<String> dropJList;
	private int dragIndex = 0;
	private int dropIndex = 0;
	private boolean internDnD = false;
	
	public ToSaveTransferHandler(int action, DefaultListModel<String> dropDList, JList<String> dropJList) {
        this.action = action;
        defaultDropmode=dropJList.getDropMode();
        this.dropDList=dropDList;
        this.dropJList=dropJList;
    }
    
	// --------------- Import Methoden
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            return false;
        }
        boolean actionSupported = (action & support.getSourceDropActions()) == action;
        if (actionSupported) {
            support.setDropAction(action);
            return true;
        }
        return false;
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }
        JList.DropLocation dl = (JList.DropLocation)support.getDropLocation();
        dropIndex = dl.getIndex();
        
        String data;
        try {
            data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }

        JList list = (JList)support.getComponent();
        DefaultListModel listModel = (DefaultListModel)list.getModel();
        
        // FIXME: if ist problematisch, muss abgeändert werden
        if(listModel.contains(data)){
        	// Wenn Drag and Drop Liste gleich ist
        	if(internDnD){
        		listModel.insertElementAt(data, dropIndex);     	
            	
        		Rectangle rect = list.getCellBounds(dropIndex, dropIndex);
                list.scrollRectToVisible(rect);
                list.setSelectedIndex(dropIndex);
                list.requestFocusInWindow();
            	return true;
        	}
        	else{
        		// Element kann nicht 2x aus der rechten Liste gezogen werden.
        		return false;
        	}
        }
        else{
        	listModel.insertElementAt(data, dropIndex);
        	if(list.getDropLocation().isInsert()){
        	}
        	else{
        		listModel.remove(dropIndex+1);
        	}
            Rectangle rect = list.getCellBounds(dropIndex, dropIndex);
            list.scrollRectToVisible(rect);
            list.setSelectedIndex(dropIndex);
            list.requestFocusInWindow();
            return true;
        }
    }
    
    
    // --------- Export Methoden -----------------
    public int getSourceActions(JComponent comp) {
        return COPY_OR_MOVE;
    }
    
    public Transferable createTransferable(JComponent comp) {
    	dropJList.setDropMode(DropMode.INSERT);
        dragIndex = dropJList.getSelectedIndex();
        internDnD=true;
        if (dragIndex < 0 || dragIndex >= dropDList.getSize()) {
            return null;
        }
        return new StringSelection((String)dropJList.getSelectedValue());
    }
    
    public void exportDone(JComponent comp, Transferable trans, int action) {
    	
    	
    	// DnD von Extern
        if(action==0){
        	// FIXME: Insert muss besser gehandelt werden
        	dropDList.removeElementAt(dragIndex);
        	if(defaultDropmode==DropMode.ON){
        		dropDList.addElement("");
        	}
        }
        
        // Internes DnD
        if(action==1 && internDnD){
        	if(dragIndex<=dropIndex)
        		dropDList.removeElementAt(dragIndex);
        	else if(dragIndex>dropIndex)
        		dropDList.removeElementAt(dragIndex+1);
        }
        // Damit erkannt wird, wenn Drag and Drop von Extern kommt
        dropJList.clearSelection();
        internDnD=false;
        dropJList.setDropMode(defaultDropmode);
    }
} 
