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
 *
 */
public class PupilView extends JView{
	private JList<String> dragDropList;
	private JList<String> saveDropList;
	private JTextArea description;
	private JList <String> projectList;
	private JButton enter;
	private JMenuItem enterProject;
	private JMenuItem logout;
	private JButton saveButton;
	public PupilView(Model model){
		super(model);
		this.dragDropList=new JList<String>(model.getCodeModel());
		this.saveDropList=new JList<String>(model.getSaveModel());
		description = new JTextArea("Wähle ein Projekt aus");
		enter = new JButton("Projekt öffnen");
		enterProject = new JMenuItem("Projekte anzeigen");
		logout = new JMenuItem("Logout");
		saveButton = new JButton("Übernehmen");
		openProjectList();
	}
	
	public void openProjectList(){
		DefaultListModel <String> projectListModel = makeDefaultListModel(model.getProjects());
		projectList = new JList<String>(projectListModel);
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
	public void openProject(){
		setupCodeLists();
		setupMenu();
		setupButtons();
		// TODO: Arbeitsanweisungen für Schüler definieren und einfügen
		mainPanel.add(new JTextField("Hier erfolgt eine möglichst präzise Arbeitsanweisung für den Schüler"),BorderLayout.PAGE_END);
		draw();
		// TODO: projekt öffnen
	}
	
	private void setupCodeLists(){
		dragDropList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		dragDropList.setDragEnabled(true);
		dragDropList.setDropMode(DropMode.ON);
		saveDropList.setDropMode(DropMode.ON);
		saveDropList.setFixedCellHeight(20);
		dragDropList.setFixedCellHeight(20);
		saveDropList.setFixedCellWidth(300);
		dragDropList.setFixedCellWidth(300);
		JScrollPane sp = new JScrollPane(saveDropList);
		mainPanel.add(sp, BorderLayout.LINE_START);
		sp = new JScrollPane(dragDropList);
		mainPanel.add(sp, BorderLayout.LINE_END);
	}
	private void setupMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu ("Datei");		
		menuBar.add(menu);
		menu.add(enterProject);
		menu.add(logout);
		frame.setJMenuBar(menuBar);
	}
	private void setupButtons(){
		JPanel topPanel=new JPanel(new BorderLayout());
		JButton compileButton=new JButton("Compile");
		topPanel.add(compileButton,BorderLayout.LINE_START);
		topPanel.add(saveButton, BorderLayout.LINE_END);
		mainPanel.add(topPanel,BorderLayout.PAGE_START);
	}
	public void addController(Controller controller){
		dragDropList.setTransferHandler(new FromTransferHandler(model.getCodeModel(), dragDropList));
		
		saveDropList.setTransferHandler(new ToSaveTransferHandler(TransferHandler.COPY));
		
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
		dragDropList=new JList<String>(model.getCodeModel());
		saveDropList=new JList<String>(model.getSaveModel());
		
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void submitChangeToController() {
		// TODO Auto-generated method stub
		
	}
}
