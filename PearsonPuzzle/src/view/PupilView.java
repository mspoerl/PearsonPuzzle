package view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import Listener.FromTransferHandler;
import Listener.ToTransferHandler;

public class PupilView extends View{
	JList<String> dragDropList;
	JList<String> saveDropList;
	public PupilView(DefaultListModel<String> codeList, DefaultListModel<String> emptyList, String username){
		// Konfiguration der Listen für Drag&Drop
		this.dragDropList=new JList<String>(codeList);
		this.saveDropList=new JList<String>(emptyList);
		dragDropList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dragDropList.setDragEnabled(true);
		dragDropList.setTransferHandler(new FromTransferHandler(codeList, dragDropList));
		saveDropList.setTransferHandler(new ToTransferHandler(TransferHandler.COPY));
		saveDropList.setDropMode(DropMode.ON);
		saveDropList.setFixedCellHeight(20);
		dragDropList.setFixedCellHeight(20);
		saveDropList.setFixedCellWidth(300);
		dragDropList.setFixedCellWidth(300);
		// Swing Elemete werden integriert und dargestellt
		JPanel topPanel=new JPanel(new BorderLayout());
		JButton compileButton=new JButton("Compile");
		JButton takeButton = new JButton("Übernehmen");
		topPanel.add(compileButton,BorderLayout.LINE_START);
		topPanel.add(takeButton, BorderLayout.LINE_END);
		mainPanel.add(topPanel,BorderLayout.PAGE_START);
		JScrollPane sp = new JScrollPane(saveDropList);
		mainPanel.add(sp, BorderLayout.LINE_START);
		sp = new JScrollPane(dragDropList);
		mainPanel.add(sp, BorderLayout.LINE_END);
		frame.pack();
		frame.setSize(800,500);
		this.draw();
	}
	
	
}
