package view.teacher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import view.JView;

import controller.Controller;
import controller.DCCommand;

import model.Model;

/**
 * Definiert die Lehrer-Sicht der grafischen Oberfläche
 * 
 * @author workspace
 *
 */
public class TeacherView extends JView{
	private ListSelectionModel listSelectionModel;
	//private DefaultListModel <String> projectListModel;
	private JList <String> projectList;
	
	private JButton enter;
	private JButton newProject;
	private JButton delete;
	private JTextArea projectDescription;	
	public TeacherView(Model model) {
		super(model);
		
		enter = new JButton("Projekt editieren");
		newProject = new JButton("Neues Projekt");
		delete = new JButton("Projekt löschen");
		projectDescription = new JTextArea("Wählen Sie ein Projekt aus");
		model.selectProject(null);
		
		menu = new MenuTeacher();
		this.addMenuToFrame(menu);
		
		setupProjektList();
		draw();
	}
	
	/**
	 * Die Projekt-Listenansicht wird definiert 
	 */
	private void setupProjektList(){
		projectList = new JList<String>();
		projectList.setListData(model.getProjectVector());
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		new JScrollPane(projectList);
		listSelectionModel = projectList.getSelectionModel();
		projectList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		projectList.setPreferredSize(new Dimension(250,250));
		projectList.setFixedCellHeight(25);
		JScrollPane scrollPanel_pL = new JScrollPane(projectList);
		scrollPanel_pL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_pL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(scrollPanel_pL, BorderLayout.LINE_START);
		
		// Zeilen werden umgebrochen und Wortgrenzen beachtet
		projectDescription.setLineWrap(true);
		projectDescription.setWrapStyleWord(true);
		projectDescription.setEditable(false);
		projectDescription.setSize(new Dimension(300,300));		
		JScrollPane scrollPanel_pD = new JScrollPane(projectDescription);
		scrollPanel_pD.setPreferredSize(new Dimension(300,300));
		scrollPanel_pD.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);	
		mainPanel.add(scrollPanel_pD, BorderLayout.EAST);
		
		// Buttons hinufügen
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		enter.setActionCommand(DCCommand.editProject.toString());
		newProject.setActionCommand(DCCommand.newProject.toString());
		delete.setActionCommand(DCCommand.deleteProject.toString());
		buttonPanel.add(newProject);
		buttonPanel.add(enter);
		buttonPanel.add(delete);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);		
	}
	public void addController(Controller controller){
		enter.addActionListener(controller);
		newProject.addActionListener(controller);
		delete.addActionListener(controller);
		//projectList.addListSelectionListener(controller);
		listSelectionModel.addListSelectionListener(controller);
		menu.addActionListener(controller);
	}
	@Override
	public void update(Observable model, Object List){
	}

	@Override
	public void update() {
		//projectList.setListData(model.getProjects().toArray(new String[0]));
		projectList.setListData(model.getProjectVector());
		projectDescription.setText(model.getProjectDescription());
	}
}
