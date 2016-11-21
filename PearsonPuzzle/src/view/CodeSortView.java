package view;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import controller.Controller;

import Listener.FromTransferHandler;
import Listener.ToSaveTransferHandler;

import model.Model;

public class CodeSortView extends JView {
	private JList<String> dragDropList;
	private JList<String> saveDropList;
	private DefaultListModel dragDropModel;
	private DefaultListModel saveDropModel;
	private JButton enter;
	private JMenuItem enterProject;
	private JMenuItem logout;
	private JButton saveButton;
	public CodeSortView(Model model) {
		super(model);
		// Instanzierung der Variablen
				dragDropModel=makeDefaultListModel(model.getProjectCodeArray());
				saveDropModel=makeDefaultListModel(model.getSaveModel());
				dragDropList=new JList<String>(dragDropModel);
				saveDropList=new JList<String>(saveDropModel);
				enter = new JButton("Projekt öffnen");
				enterProject = new JMenuItem("Projekte anzeigen");
				logout = new JMenuItem("Logout");
				saveButton = new JButton("Übernehmen");
				setupCodeLists();
				setupMenu();
				setupButtons();
				// TODO: Arbeitsanweisungen für Schüler definieren und einfügen
				mainPanel.add(new JTextField("Hier erfolgt eine möglichst präzise Arbeitsanweisung für den Schüler"),BorderLayout.PAGE_END);
				draw();
		// TODO Auto-generated constructor stub
	}
	
	// private Methode, um die Drag and Drop Liste zu konstruiren
		private void setupCodeLists(){
			dragDropList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			dragDropList.setDragEnabled(true);
			dragDropList.setDropMode(DropMode.ON);
			saveDropList.setDropMode(DropMode.ON);
			saveDropList.setFixedCellHeight(20);
			dragDropList.setFixedCellHeight(20);
			saveDropList.setFixedCellWidth(300);
			dragDropList.setFixedCellWidth(300);
			// TODO: In den offiziellen Controller auslagern
			dragDropList.setTransferHandler(new FromTransferHandler(dragDropModel, dragDropList));
			saveDropList.setTransferHandler(new ToSaveTransferHandler(TransferHandler.COPY));
			JScrollPane sp = new JScrollPane(saveDropList);
			mainPanel.add(sp, BorderLayout.LINE_START);
			sp = new JScrollPane(dragDropList);
			mainPanel.add(sp, BorderLayout.LINE_END);
		}
		
		// private Methode, um die Buttons zu definieren
		private void setupButtons(){
			JPanel topPanel=new JPanel(new BorderLayout());
			JButton compileButton=new JButton("Compile");
			topPanel.add(compileButton,BorderLayout.LINE_START);
			topPanel.add(saveButton, BorderLayout.LINE_END);
			mainPanel.add(topPanel,BorderLayout.PAGE_START);
		}
		
		// private Methode, um das Menü zu definieren
		private void setupMenu(){
			menuBar = new JMenuBar();
			JMenu menu = new JMenu ("Datei");
			menuBar.add(menu);
			menu.add(enterProject);
			menu.add(logout);
			frame.setJMenuBar(menuBar);
		}
		
		
		/**
		 * Wird vom Controller asugeführt, um Listener, Handler und <br>
		 * Controller hinzuzufügen
		 */
		public void addController(Controller controller){				
			enter.addActionListener(controller);
			enter.setActionCommand("openProject");
			
			saveButton.addActionListener(controller);
			saveButton.setActionCommand("saveChanges");
			
			enterProject.setActionCommand("openProjectList");
			enterProject.addActionListener(controller);
			
			logout.setActionCommand("logout");
			logout.addActionListener(controller);
		}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quitView() {
		mainPanel.removeAll();		
	}

	@Override
	public void update() {
		dragDropModel=makeDefaultListModel(model.getProjectCodeArray());
		saveDropModel=makeDefaultListModel(model.getSaveModel());
		dragDropList=new JList<String>(dragDropModel);
		saveDropList=new JList<String>(saveDropModel);
		// TODO: in den offiziellen Controller auslagern
		dragDropList.setTransferHandler(new FromTransferHandler(dragDropModel, dragDropList));
		saveDropList.setTransferHandler(new ToSaveTransferHandler(TransferHandler.COPY));	
	}

}
