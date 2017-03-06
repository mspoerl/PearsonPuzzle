package view.pupil;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import view.Menu;
import controller.Controller;
import controller.DCCommand;

public class MenuPupil extends Menu {

    /**
     * Definiert das Menü aus Schülerperspektive.
     */
    private static final long serialVersionUID = 1L;
    JMenuItem enterProject;
    JMenuItem logout;

    public MenuPupil() {
	setupMenu();
    }

    MenuPupil(JFrame frame) {
	setupMenu();
	frame.setJMenuBar(this);
    }

    @Override
    protected void setupMenu() {
	JMenu menu = new JMenu("Menü");
	enterProject = new JMenuItem("Projekte anzeigen");
	enterProject.setActionCommand(DCCommand.ProjectList.toString());
	logout = new JMenuItem("Logout");
	logout.setActionCommand(DCCommand.Logout.toString());

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
