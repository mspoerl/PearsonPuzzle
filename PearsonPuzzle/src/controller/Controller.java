package controller;

import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import view.JView;
import model.Model;
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
	
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	public abstract void valueChanged(ListSelectionEvent arg0);
	
	public JView getView(){
		return view;
	}
}
