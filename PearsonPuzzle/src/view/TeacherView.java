package view;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import controller.Controller;

import model.Model;

/**
 * Definiert die Lehrer-Sicht der grafischen Oberfläche
 * 
 * @author workspace
 *
 */
public class TeacherView extends JView{
	JMenu menu;
	ListSelectionModel listSelectionModel;
	JTextArea projectDescription;
	public TeacherView(Model model) {
		super(model);
		menu = new JMenu("Datei");
		projectDescription = new JTextArea("Wähle ein Projekt aus");
		setupMenu();
		setupProjektList();
		draw();
		// TODO Auto-generated constructor stub
	}
	private void setupMenu(){
		JMenuBar menuBar = new JMenuBar();
		JMenuItem showProjects;
		JMenuItem addProject;
		JMenuItem classAdmin;
		menuBar.add(menu);
		JMenuItem enterProject = new JMenuItem("Projekte anzeigen");
		JMenuItem newProject = new JMenuItem("Projekt anlegen");
		JMenuItem enterClass = new JMenuItem("Klassen verwalten");
		menu.add(enterProject);
		menu.add(newProject);
		menu.add(enterClass);
		frame.setJMenuBar(menuBar);
	}
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
	}
	public void addController(Controller controller){
		menu.addActionListener(controller);
		listSelectionModel.addListSelectionListener(controller);
	}
	/* TODO: Lehreransicht definieren und interaktionen festlegen */

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void submitChangeToController() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quitView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		projectDescription.setText(model.getProjectDescription());		
		// TODO Auto-generated method stub
		
	}
}
