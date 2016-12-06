package view.teacher;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.*;

import controller.Controller;
import controller.DCCommand;

import model.Model;
import view.JView;
/**
 * View, der den Lehrer grundsätzliche Einstellungen treffen lässt.
 * @author workspace
 */
public class ConfigEditor extends JView{

	JCheckBox dbReset;
	JButton submit;
	public ConfigEditor(Model model) {
		super(model);
		setupConfigPanel();
		model.addObserver(this);
	}
	
	private void setupConfigPanel(){
		JPanel configPanel=new JPanel(new GridLayout(/*6*/ 0,2, 6,3));
		configPanel.add(new JLabel("Datenbank auf Werkseinstellungen"));
		dbReset = new JCheckBox("Datenbank auf Werkseinstellungen zurücksetzten");
		dbReset.setActionCommand("resetDB");
		configPanel.add(dbReset);
		mainPanel.add(configPanel, BorderLayout.CENTER);
		
		// - Damit Button im Border Layout (mainPanel) richtige Größe erhät, wird er in eigenes Panel gekapselt
		submit=new JButton("Übernehmen");
		submit.setActionCommand(DCCommand.resetDB.toString());
		JPanel submitPanel = new JPanel();
		submitPanel.add(submit);
		mainPanel.add(submitPanel, BorderLayout.SOUTH);
		draw();
	}
	
	@Override
	public void addController(Controller controller){
		dbReset.addItemListener(controller);
	}

	public void update() {
		dbReset.setSelected(model.isResetDB());
	}

	public void update(Observable o, Object arg) {
		update();	
	}
}
