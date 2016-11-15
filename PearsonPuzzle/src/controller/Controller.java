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
 * Oberklasse der verschiedenen Controller, die alle dafür zuständig sind,
 * Daten vom Model an den jeweiligen darstellenden View weiterzureichen und 
 * bei Interaktion des Benutzers die von den Listenern erfassten Veränderungen 
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
	public void login(String username, char[] password){
		model.setPassword(password);
		model.setUsername(username);
		if(username.isEmpty() || password.length==0){
			view.allert("Bitte Nutzernamen und Passwort eingeben");
		}
		else if(model.getAccessGroup()==accessGroup.TEACHER){
			view.quitView();
			this.view=new TeacherView(model);
			view.addController(this);
		}
		else if(model.getAccessGroup()==accessGroup.PUPIL){
			view.quitView();
			this.view=new PupilView(model);
			view.addController(this);
		}
		else{
			view.allert("Zugang verweigert");
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	/*
	List<Model> models;
	List <View> views;
	public Controller(Model model, View view) {
		this.models.add(model);
		this.views.add(view);
		view.addController(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	public void addView(View view){
		this.views.add(view);
	}
	public void removeView(View view){
		this.views.remove(view);
	}
	public void addModel(Model model){
		this.models.add(model);
	}
	public void removeModel(Model model){
		this.models.remove(model);
	}
	
	//noch unvollsändig es soll eine änderung übergeben werden (runde klammern sind noch zu füllen)
	public void updateModels(){
		for(View view: views){
			//view.update(Event e);
		}
	}
	public void setModel(){
		
	}
	*/
	@Override
	public abstract void valueChanged(ListSelectionEvent arg0);
	
}
