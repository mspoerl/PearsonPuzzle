package view;

import java.awt.FlowLayout;
import javax.swing.*;
import controller.Controller;

/**
 * Design Klasse, um Erscheinungsbild der grafischen Oberfläche zu definieren
 * und auf jeweiligen Nutzer einzustellen.
 * 
 * @author wildcut
 *
 */

public class View{
		protected JFrame frame;
		private Controller controller;
		private JPanel mainPanel;
		View(){
			frame = new JFrame("PearsonPuzzle");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new FlowLayout());
			mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			frame.add(mainPanel);
			frame.setSize(800,500);
		}
			
		public void draw(){
			if(frame.isVisible()==false){
				frame.setVisible(true);
			}
			// TODO: wenn frame bereits sichtbar ist, updaten
		}
		public void setDesignPupil(JList<String> dragDropList, JList<String> saveDropList, String username){
			// TODO: Name und Passwortprüfung
						
			//codeButtons.setDragEnabled(true);
			this.quitView();
			dragDropList.setDragEnabled(true);
			mainPanel.add(saveDropList);
			mainPanel.add(dragDropList);
			frame.pack();
			frame.setSize(800,500);
			this.draw();
		}
		public void addController(Controller controller){
			this.controller=controller;
		}
		public void quitView(){
		}
		//		javax.swing.Timer t = new javax.swing.Timer( 1000, new ActionListener() {
		//	  public void actionPerformed( ActionEvent e ) {
		//	    codeList.repaint();
		//	  }
		//	});
		//	t.start();   // t.stop() beendet.
		
		// Um das Passwort schelchter abgreifbar zu machen
		// bin aber nicht sicher, ob das so auch sicherer ist, 
		// aber theoretisch schon
}
