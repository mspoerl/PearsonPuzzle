package Listener;


import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSourceEvent;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

import model.Model;

/**
 * 
 * 
 * @author workspace
 *
 */
public class FromTransferHandler extends TransferHandler {
	private Model model;
	private boolean internDnD=false;
	private boolean canInvertAction=true;
	private static final long serialVersionUID = 1L;
	private JList<String> dragJList;
	private DefaultListModel<String> dragDList;
	private final static int action = TransferHandler.MOVE;
	
	//DefaultListModel from;
	public FromTransferHandler(DefaultListModel<String> dragDList, JList<String> dragJList, Model model){
		this.dragDList=dragDList;
		this.dragJList=dragJList;
		this.model=model;
	}
	
	public void setCanRevertAction(boolean canRevert){
		canInvertAction=canRevert;
	}
	// --------------- Import Methoden
    public boolean canImport(TransferHandler.TransferSupport support) {
    	if(internDnD)
    		return false;
    	if(!canInvertAction)
    		return false;
        if (!support.isDrop())
        	return false;
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor))
            return false;
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
        Integer dropIndex = dl.getIndex();
        
        String data;
        try {
            data = (String)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            return false;
        } catch (java.io.IOException e) {
            return false;
        }

        JList<String> list = (JList<String>)support.getComponent();
        DefaultListModel<String> listModel = (DefaultListModel<String>)list.getModel();
        // Daten von Extern werden nicht erkannt
        if(!model.getCodeVector().contains(data)){
        	return false;
        }
        listModel.insertElementAt(data, dropIndex);
        Rectangle rect = list.getCellBounds(dropIndex, dropIndex);
        list.scrollRectToVisible(rect);
        list.setSelectedIndex(dropIndex);
        list.requestFocusInWindow();
      	return true;
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
        internDnD=true;
        return new StringSelection((String)dragJList.getSelectedValue());
    }
    
    public void exportDone(JComponent comp, Transferable trans, int action) {
    	internDnD = false;
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
