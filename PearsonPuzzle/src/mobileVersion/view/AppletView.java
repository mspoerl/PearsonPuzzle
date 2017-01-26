package mobileVersion.view;

import java.awt.BorderLayout;

import java.util.Observer;

import javax.swing.JPanel;

import model.Model;
import view.Allert;

import controller.Controller;

public abstract class AppletView extends JPanel implements Observer{
	
	private static final long serialVersionUID = -5146860469857534054L;
	public AppletView(Model model) {
		super(new BorderLayout());
	}

	public abstract Object get(String valueToReturn);
	public abstract void addController(Controller controller);
	
	public void draw(){
		this.setOpaque(true);
		this.setVisible(true);
	}
	
	public void showDialog(Allert allert) {
		allert.allert(null);
	}

	public abstract void removeController(Controller Controller);
}
