package view.pupil;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import controller.Controller;
import view.Menu;

public class MenuPupil extends Menu{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem enterProject;
	JMenuItem logout;
	MenuPupil(){
		setupMenu();
	}
	MenuPupil(JFrame frame){
		setupMenu();
		frame.setJMenuBar(this);
	}

	@Override
	protected void setupMenu() {		
		JMenu menu = new JMenu ("Datei");			
		enterProject = new JMenuItem("Projekte anzeigen");
		enterProject.setActionCommand("projectList");
		logout = new JMenuItem("Logout");
		logout.setActionCommand("logout");

		this.add(menu);
		menu.add(enterProject);
		menu.add(logout);
	}

	@Override
	public void addActionListener(Controller controller) {
		enterProject.addActionListener(controller);
		logout.addActionListener(controller);
	}
}
