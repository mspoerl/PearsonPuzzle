package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Model;
/**
 * Oberklasse der verschiedenen Controller, die alle dafür zuständig sind, 
 * Benutzerinteraktionen zu erfassen, zu klassifizieren, um weiterzuverarbeiten.
 * 
 * @author workspace
 *
 */
public interface Controller extends java.awt.event.ActionListener, ListSelectionListener, ItemListener {
	
	/**
	 * ActionEvents, wie zum Beispiel das Drücken von Buttons wird verarbeitet.
	 */
	void actionPerformed(ActionEvent arg0);
	
	/**
	 * List-Selektionen können verarbeitet werden.
	 */
	void valueChanged(ListSelectionEvent arg0);
	
	/**
	 * Nutzung ist im Normalfall nicht notwendig, zu Testzwecken aber sinnvoll.
	 * @return Die View des Controllers
	 */
	public Object getView();
	
	/**
	 * Nutztung ist im Normalfall nciht notwendig, zu Testzwecken aber sinnvoll.
	 * @return Das Model des Controllers
	 */
	public Model getModel();

}
