package mobileVersion.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import model.Model;

import view.beans.ProjectList;
import view.pupil.MenuPupil;

import controller.Controller;
import controller.DCCommand;

public class ProjectListAView extends AppletView{

	private static final long serialVersionUID = 9165370696356294174L;
	private Model model;
	private ListSelectionModel listSelectionModel;
	private JTextArea projectDescription;
	private JButton enter;
	private MenuPupil menu;
	private ProjectList projectList;
	
	public ProjectListAView(Model model) {
		super(model);
		this.model = model;
		setLayout(new BorderLayout());
		enter = new JButton("Projekt öffnen");
		// Bei Konstruktion wird Ansicht "Projektliste" aufgerufen
		menu = new MenuPupil();
		this.setPreferredSize(new Dimension(200,200));
		//this.addMenuToFrame(menu);
		setupProjectList();
	}
	
	
	/**
	 * Projekt Liste wird gezeichnet.
	 */
	private void setupProjectList(){
		JList <String> projectList = new JList<String>(model.getProjectVector());
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		new JScrollPane(projectList);
		listSelectionModel = projectList.getSelectionModel();
		// Liste wird nicht umgebrochen, sondern vertikal weitergeführt
		projectList.setLayoutOrientation(JList.VERTICAL);
		projectList.setFixedCellHeight(20);
		projectList.setFixedCellWidth(200);
		JScrollPane scrollPanel_pL = new JScrollPane(projectList);
		scrollPanel_pL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_pL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(projectList, BorderLayout.CENTER);
		
		// Zeilen werden umgebrichen und Wortgrenzen beachtet
		projectDescription = new JTextArea("Wähle ein Projekt aus");
		projectDescription.setLineWrap(true);
		projectDescription.setWrapStyleWord(true);
		projectDescription.setEditable(false);
		JScrollPane scrollPanel_pD = new JScrollPane(projectDescription);
		scrollPanel_pD.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	
		scrollPanel_pD.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//scrollPanel_pD.setMaximumSize(new Dimension(100,100));
		scrollPanel_pD.setMinimumSize(new Dimension(100,100));
		this.add(scrollPanel_pD, BorderLayout.NORTH);
		this.add(enter, BorderLayout.SOUTH);
	}
	
	/**
	 * Wird vom Controller asugeführt, um Listener, Handler und <br>
	 * Controller hinzuzufügen
	 */
	public void addController(Controller controller){
		listSelectionModel.addListSelectionListener(controller);
		//listSelectionModel.addListSelectionListener(controller);
		
		enter.addActionListener(controller);
		enter.setActionCommand(DCCommand.OpenProject.toString());
			
		//menu.addActionListener(controller);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		projectDescription.setText(model.getProjectDescription());
	}

	@Override
	public Object get(String valueToReturn) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void removeController(Controller controller) {
		listSelectionModel.removeListSelectionListener(controller);
		//listSelectionModel.addListSelectionListener(controller);
		enter.removeActionListener(controller);	
		//menu.removeActionListener(controller);
	}
}