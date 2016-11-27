package view.teacher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import view.JView;

import controller.Controller;

import model.Model;

/**
 * Definiert die Lehrer-Sicht der grafischen Oberfläche
 * 
 * @author workspace
 *
 */
public class TeacherView extends JView{
	private ListSelectionModel listSelectionModel;
	private JButton enter;
	private JButton newProject;
	private JTextArea projectDescription;
	private ArrayList <JMenuItem> menuItems;	
	public TeacherView(Model model) {
		super(model);
		enter = new JButton("Projekt öffnen");
		newProject = new JButton("Neues Projekt anlgen");
		projectDescription = new JTextArea("Wähle ein Projekt aus");
		menuItems= new ArrayList<JMenuItem>();
		setupMenu();
		setupProjektList();
		draw();
	}
	
	/**
	 * Menü wird definiert
	 */
	private void setupMenu(){
		this.menuBar = new JMenuBar();
		JMenu menu = new JMenu("Datei");
		menuItems.add(new JMenuItem("Neues Projekt"));
		menuItems.get(menuItems.size()-1).setActionCommand("newProject");
		menuItems.add(new JMenuItem("Projekte anzeigen"));
		menuItems.get(menuItems.size()-1).setActionCommand("projectList");
		menuItems.add(new JMenuItem("Klassen verwalten"));
		menuItems.get(menuItems.size()-1).setActionCommand("editClass");
		menuItems.add(new JMenuItem("Logout"));
		menuItems.get(menuItems.size()-1).setActionCommand("logout");
		menuBar.add(menu);
		for(JMenuItem menuItem: menuItems){
			menu.add(menuItem);
		}
		frame.setJMenuBar(menuBar);
	}
	
	/**
	 * Die Projekt-Listenansicht wird definiert 
	 */
	private void setupProjektList(){
		DefaultListModel <String> projectListModel = makeDefaultListModel(model.getProjects());
		JList <String> projectList = new JList<String>(projectListModel);
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		new JScrollPane(projectList);
		listSelectionModel = projectList.getSelectionModel();
		projectList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		projectList.setFixedCellHeight(20);
		projectList.setFixedCellWidth(200);
		//projectList.setPreferredSize(new Dimension(250,250));		
		mainPanel.add(projectList, BorderLayout.LINE_START);
		// Zeilen werden umgebrichen und Wortgrenzen beachtet
		projectDescription.setLineWrap(true);
		projectDescription.setWrapStyleWord(true);
		projectDescription.setEditable(false);
		projectDescription.setSize(200, 200);
		mainPanel.add(projectDescription, BorderLayout.EAST);
		
		// Buttons hinufügen
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		enter.setActionCommand("editProject");
		newProject.setActionCommand("newProject");
		buttonPanel.add(enter);
		buttonPanel.add(newProject);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);		
	}
	public void addController(Controller controller){
		enter.addActionListener(controller);
		newProject.addActionListener(controller);
		listSelectionModel.addListSelectionListener(controller);
	    for(JMenuItem menuItem : menuItems){
	    	menuItem.addActionListener(controller);
	    }  
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update() {
		projectDescription.setText(model.getProjectDescription());			
	}
}
