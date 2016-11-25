package controller;

import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import view.JView;
import view.PupilView;
import view.TeacherView;
import model.Model;
import model.accessGroup;
/**
 * Oberklasse der verschiedenen Controller, die alle dafür zuständig sind, <br>
 * Daten vom Model an den jeweiligen darstellenden View weiterzureichen und <br>
 * bei Interaktion des Benutzers die von den Listenern erfassten Veränderungen <br>
 * an das Model weiterzureichen.
 * 
 * @author workspace
 *
 */
public abstract class Controller implements java.awt.event.ActionListener,  ListSelectionListener{
	protected Model model;
	protected JView view;
	public Controller(Model model, JView view){
		this.model=model;
		this.view=view;
		view.addController(this);
	}
	
	/**
	 * Soll eventuell ins Modell ausgelagert werden
	 * Nutzername @param username
	 * Passwort @param password
	 */
	public void login(String username, char[] password){
		model.setUsername(username);
		if(username.isEmpty() || password.length==0){
			view.allert("Bitte Nutzernamen und Passwort eingeben");
		}
		else if(model.getAccessGroup(username, password)==accessGroup.TEACHER){
			view.quitView();
			this.view=new TeacherView(model);
			view.addController(this);
		}
		else if(model.getAccessGroup(username, password)==accessGroup.PUPIL){
			view.quitView();
			this.view=new PupilView(model);
			view.addController(this);
		}
		else{
			view.allert("Zugang verweigert");
		}
	}
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public abstract void valueChanged(ListSelectionEvent arg0);
	
}
