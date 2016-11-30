package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import model.Model;
import controller.Controller;

/**
 * Design Klasse, um Erscheinungsbild der grafischen Oberfläche zu definieren
 * und auf jeweiligen Nutzer einzustellen.
 * 
 * Die Implementierung von Observer 
 * @author workspace
 *
 */

public abstract class JView implements Observer {
	protected static JFrame frame = new JFrame("PearsonPuzzle");
	protected static JPanel mainPanel = new JPanel(new BorderLayout(5,1));
	protected Menu menu;
	protected Model model;
	private Controller controller;
	public JView(Model model){
			this.model=model;
			model.addObserver(this);
		}
	
		/**
		 * Soll noch in einen Presenter ausgelagert werden <br>
		 * ermöglicht, dass die Swing Komponenten ein Listen Model erhalten
		 * Liste aus dem Model @param stringList
		 * Liste für (swing) View @return
		 */
		protected DefaultListModel<String> makeDefaultListModel(List<String> stringList){
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			for(String listElement : stringList){
				listModel.add(listModel.size(),  listElement);
			}
			return listModel;
		}
		protected DefaultListModel<String> makeDefaultListModel(String[] strings){
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			
			// Dies ist nötig, um bei JList Elementen die Tabbreite berücksichtigen zu können
			for(String string : strings){
				String tab=" ";
				for(int i=0;i<model.getTabSize();i++){
					tab=tab+" ";
				}
				String bString = string.replaceAll("\t", tab);
				listModel.add(listModel.size(),  bString);
			}
			return listModel;
		}
		
		/**
		 * Methode soll vom Controller ausgeführt werden, um sich selbst <br>
		 * als konkreter Controller für einen konkreten View hinzuzufügen.
		 * Action Controller @param controller
		 */
		public void setController(Controller controller){
			this.controller=controller;
		}
		public void addController(Controller controller){
		}
		/**
		 * Methode, um das Fenster aufzusetzen ung grundlegende Fenstereinstellungen festzulegen. <br>
		 * Wird nur zu Beginn aufgerufen, wenn ein neues Fesnter erstellt werden soll, dies ist <br>
		 * in der Regel nur zu Beginn des Programms der Fall.
		 */
		public void setupFrame(){
			//frame = new JFrame("PearsonPuzzle");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new FlowLayout());
			System.out.println("Neuer Frame wurde erstell");
			// BorderLayout mit 5 horizontalem und 1 vertikalem Versatz zwischen den Komponenten
			//mainPanel = new JPanel(new BorderLayout(5,1));
			frame.add(mainPanel);
			frame.setSize(800,500);
			frame.setLocationRelativeTo(null);
		}
		
		/**
		 * Frame wird auf sichtbar gesetzt und Größe festgelegt.
		 */
		public void draw(){
		    frame.pack();
			frame.setSize(800,500);
			if(frame.isVisible()==false){
				frame.setVisible(true);
			}
			
			// TODO: wenn frame bereits sichtbar ist, updaten
		}
		
		/**
		 * Controller dieses View @return
		 */
		public Controller getController(){
			// !!! Return kann nulle sein
			return controller;
		}
		
		/**
		 * Der View soll geschlossen werden (in erster Linie das mainPanel
		 */
		public void quitView(){
			mainPanel.removeAll();
		}
		
		/**
		 * Hier müssen alle Elemente notiert werden, die aktualisiert <br>
		 * die aktualiseirt werden sollen können. <br>
		 * (Methodenaufruf erfolgt durch den Controller) 
		 */
		abstract public void update();
		abstract public void update(Observable o, Object arg);
		public void selectView(int i){};
		
		/**
		 * Nachricht wird als Allert ausgegeben
		 * Nachricht @param message
		 */
		public void allert(String message) {
			JOptionPane.showMessageDialog(frame, message);	
		}
		
		/**
		 * Frame soll geschlossen werden.
		 */
		public void exit() {
		    frame.dispose();
		    // TODO Auto-generated method stub
		}		
}
