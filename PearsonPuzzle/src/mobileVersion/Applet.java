package mobileVersion;

import java.awt.Dimension;

import javax.swing.JFrame;

import controller.Controller;

import mobileVersion.controller.AppletController;
import mobileVersion.view.AppletView;
import mobileVersion.view.LoginAView;
import mobileVersion.view.ProjectListAView;
import model.Model;


public class Applet extends JFrame{
	private static final long serialVersionUID = -6156615178571385705L;

	public static void main(String[] args) {
		Applet app = new Applet();
		app.createGUI();
	}

	private void createGUI() {
		Model model = new Model();
		AppletView startPanel = new ProjectListAView(model);		
		startPanel.setOpaque(true);
		setContentPane(startPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		Controller controller = new AppletController(model, startPanel);
	}
	
	public void createGUI(Model model) {
		AppletView startPanel = new ProjectListAView(model);		
		startPanel.setOpaque(true);
		setContentPane(startPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		startPanel.setPreferredSize(new Dimension(400,800));
		this.setSize(new Dimension(400,800));
		pack();
		setVisible(true);
		Controller controller = new AppletController(model, startPanel);
	}
}
