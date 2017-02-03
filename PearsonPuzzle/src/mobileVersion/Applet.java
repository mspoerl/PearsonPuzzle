package mobileVersion;

import java.awt.Dimension;

import javax.swing.JFrame;

import controller.Controller;

import mobileVersion.controller.AppletController;
import mobileVersion.view.AppletMenu;
import mobileVersion.view.AppletView;
import mobileVersion.view.ProjectListAView;
import model.Model;

/**
 * Diese Klasse ist als Java Weg Start Application designed, um aus dem Internet (per Webstart) geladen werden zu können. 
 * Da Lese- und Schreibrechte, sowie die Nutzung eines Class Loaders notwendig sind, kannd die Applikation allerdings nicht im Sandbox-Modus ausgeführt werden.
 * Vergleiche hierzu: https://docs.oracle.com/javase/tutorial/deployment/webstart/index.html
 * 
 * @author workspace
 */
public class Applet extends JFrame{
	private static final long serialVersionUID = -6156615178571385705L;

	public static void main(String[] args) {
		Model model = new Model();
		Applet app = new Applet();
		app.createGUI(model);
	}
	public Applet(){
	}
	public Applet(Model model){
	}

	public void createGUI() {
		Model model = new Model();
		AppletView startPanel = new ProjectListAView(model);		
		startPanel.setOpaque(true);
		setContentPane(startPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		Controller controller = new AppletController(model, startPanel);
		startPanel.addController(controller);
	}
	
	public void createGUI(Model model) {
		AppletView startPanel = new ProjectListAView(model);		
		startPanel.setOpaque(true);
		setContentPane(startPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startPanel.setPreferredSize(new Dimension(400,600));
		setSize(new Dimension(400,600));
		setLocationRelativeTo(null);
		AppletMenu menu = new AppletMenu();
		setJMenuBar(menu);
		//pack();
		setVisible(true);
		Controller controller = new AppletController(model, startPanel);
		menu.addController(controller);
	}
}
