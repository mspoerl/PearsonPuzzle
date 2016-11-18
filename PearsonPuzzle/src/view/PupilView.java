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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import model.Model;

import controller.Controller;

import Listener.FromTransferHandler;
import Listener.ToSaveTransferHandler;

/**
 * Definiert die Schüler Perspektive der grafischen Oberfläche
 * 
 * @author workspace
 */
public class PupilView extends JView{
	private JList<String> dragDropList;
	private JList<String> saveDropList;
	private DefaultListModel dragDropModel;
	private DefaultListModel saveDropModel;
	private JTextArea description;
	private JList <String> projectList;
	private JButton enter;
	private JMenuItem enterProject;
	private JMenuItem logout;
	private JButton saveButton;
	private DefaultListModel <String> projectListModel;
	public PupilView(Model model){
		// Instanzierung der Variablen
		super(model);
		dragDropModel=makeDefaultListModel(model.getCodeModel());
		saveDropModel=makeDefaultListModel(model.getSaveModel());
		dragDropList=new JList<String>(dragDropModel);
		saveDropList=new JList<String>(saveDropModel);
		description = new JTextArea("Wähle ein Projekt aus");
		enter = new JButton("Projekt öffnen");
		enterProject = new JMenuItem("Projekte anzeigen");
		logout = new JMenuItem("Logout");
		saveButton = new JButton("Übernehmen");
		projectListModel = makeDefaultListModel(model.getProjects());
		projectList = new JList<String>(projectListModel);		
		// Bei Konstruktion wird Ansicht "Projektliste" aufgerufen
		selectView(0);
	}
	
	/**
	 * Die Möglichen Schüleranscihten <br>
	 * TODO: eventuell mittels Enum auswählen, welcher View gewählt wird
	 */
	public void selectView(int i){
	    if(i==0){
		setupCodeLists();
		setupMenu();
		setupButtons();
		// TODO: Arbeitsanweisungen für Schüler definieren und einfügen
		mainPanel.add(new JTextField("Hier erfolgt eine möglichst präzise Arbeitsanweisung für den Schüler"),BorderLayout.PAGE_END);
		draw();
	    }
	    if(i==1){
		setupMenu();
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		new JScrollPane(projectList);
		ListSelectionModel listSelectionModel = projectList.getSelectionModel();
		projectList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		projectList.setFixedCellHeight(20);
		projectList.setFixedCellWidth(200);
		//projectList.setPreferredSize(new Dimension(250,250));		
		mainPanel.add(projectList, BorderLayout.LINE_START);
		// Zeilen werden umgebrichen und Wortgrenzen beachtet
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		description.setSize(200, 200);
		mainPanel.add(description, BorderLayout.EAST);
		mainPanel.add(enter, BorderLayout.SOUTH);
		draw();
	    }    
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
	
	// private Methode, um das Menü zu definieren
	private void setupMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu ("Datei");		
		menuBar.add(menu);
		menu.add(enterProject);
		menu.add(logout);
		frame.setJMenuBar(menuBar);
	}
	
	// private Methode, um die Buttons zu definieren
	private void setupButtons(){
		JPanel topPanel=new JPanel(new BorderLayout());
		JButton compileButton=new JButton("Compile");
		topPanel.add(compileButton,BorderLayout.LINE_START);
		topPanel.add(saveButton, BorderLayout.LINE_END);
		mainPanel.add(topPanel,BorderLayout.PAGE_START);
	}
	
	/**
	 * Wird vom Controller asugeführt, um Listener, Handler und <br>
	 * Controller hinzuzufügen
	 */
	public void addController(Controller controller){		
		projectList.addListSelectionListener(controller);
		
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
	public void quitView() {
		mainPanel.removeAll();
	}
	
	@Override
	public void update() {
		description.setText(model.getProjectDescription());
		dragDropModel=makeDefaultListModel(model.getCodeModel());
		saveDropModel=makeDefaultListModel(model.getSaveModel());
		dragDropList=new JList<String>(dragDropModel);
		saveDropList=new JList<String>(saveDropModel);
		// TODO: in den offiziellen Controller auslagern
		dragDropList.setTransferHandler(new FromTransferHandler(dragDropModel, dragDropList));
		saveDropList.setTransferHandler(new ToSaveTransferHandler(TransferHandler.COPY));
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
