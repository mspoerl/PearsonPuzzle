package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
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
		protected static JFrame frame;
		protected static JPanel mainPanel;
		protected Model model; 
		private Controller controller;
		
		public JView(Model model){
			this.model=model;
			model.addObserver(this);
		}
		protected DefaultListModel<String> makeDefaultListModel(List<String> stringList){
			DefaultListModel<String> listModel = new DefaultListModel<String>();
			for(String listElement : stringList){
				listModel.add(listModel.size(),  listElement);
			}
			return listModel;
		}

		public void addController(Controller controller){
			this.controller=controller;
		}
		/**
		 * Methode, um das Fenster aufzusetzen ung grundlegende Fenstereinstellungen festzulegen. 
		 * Wird nur zu Beginn aufgerufen, wenn ein neues Fesnter erstellt werden soll, dies ist 
		 * in der Regel nur zu Beginn des Programms der Fall.
		 */
		public void setupFrame(){
			frame = new JFrame("PearsonPuzzle");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new FlowLayout());
			System.out.println("Neuer Frame erstelle");
			// BorderLayout mit 5 horizontalem und 1 vertikalem Versatz zwischen den Komponenten
			mainPanel = new JPanel(new BorderLayout(5,1));
			frame.add(mainPanel);
			frame.setSize(800,500);
			frame.setLocationRelativeTo(null);
		}
		public void draw(){
			if(frame.isVisible()==false){
				frame.setVisible(true);
			}
			frame.pack();
			frame.setSize(800,500);
			// TODO: wenn frame bereits sichtbar ist, updaten
		}
		public Controller getController(){
			// !!! Return kann nulle sein
			return controller;
		}
		public abstract void submitChangeToController();
		abstract public void quitView();
		abstract public void update();
		public void openProject() {
			System.out.println("open");
			// TODO Auto-generated method stub
		}
		public void openProjectList() {
			// TODO Auto-generated method stub
			
		}
		public void allert(String message) {
			JOptionPane.showMessageDialog(frame, message);
			// TODO Auto-generated method stub
			
		}
}
