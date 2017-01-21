package mobileVersion;

import java.awt.Dimension;

import javax.swing.JFrame;

import controller.Controller;

import mobileVersion.controller.AppletController;
import mobileVersion.view.AppletView;
import mobileVersion.view.ProjectListAView;
import mobileVersion.view.LoginAView;
import model.Model;


public class Applet extends JFrame{
	public static void main(String[] args) {
		Applet app = new Applet();
		app.createGUI();
	}

	private void createGUI() {
		Model model = new Model();
		AppletView startPanel = new LoginAView(model);		
		startPanel.setOpaque(true);
		setContentPane(startPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		Controller controller = new AppletController(model, startPanel);
	}
}
