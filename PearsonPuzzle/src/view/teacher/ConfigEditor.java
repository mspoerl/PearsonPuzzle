package view.teacher;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.*;

import controller.Controller;
import controller.DCCommand;

import model.AccessGroup;
import model.Model;
import view.JView;
/**
 * View, der den Lehrer grundsätzliche Einstellungen treffen lässt.
 * @author workspace
 */
public class ConfigEditor extends JView{

	JCheckBox dbReset;
	JButton submit;
	JPasswordField passwordField;
	JComboBox accessGroupSelection;
	JComboBox gradeSelection;
	JComboBox<String> userSelection;
	public ConfigEditor(Model model) {
		super(model);
		setupConfigPanel();
		draw();
	}
	
	private void setupConfigPanel(){
		//JPanel configPanel=new JPanel(new GridLayout(/*6*/ 0,2, 6,3));
		JPanel configPanel = new JPanel();	
		configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
		
		
		
		
		JPanel groupSelection = new JPanel();
		String[] grades = {"5","6","7","8","9","10","11","12","13","0"};
		gradeSelection = new JComboBox();
		gradeSelection.setEnabled(false);
		accessGroupSelection = new JComboBox<String>();
		gradeSelection.setActionCommand(DCCommand.SetGrade.toString());
		for(String string:grades){
			gradeSelection.addItem(string);
		}
		for(AccessGroup aG: AccessGroup.values()){
			accessGroupSelection.addItem(aG.toString());
		}
		groupSelection.add(accessGroupSelection);
		groupSelection.add(gradeSelection);
		
		JPanel addUser = new JPanel();
		addUser.add(new JLabel("Nutzer hinzufügen: "));
		addUser.add(new JTextField(10));
		addUser.add(new JPasswordField(10));
		
		JPanel editUsers = new JPanel();
		editUsers.add(new JLabel("Nutzer bearbeiten: "));
		passwordField= new JPasswordField(10);
		userSelection = new JComboBox<String>();
		//for(String user:model.getUsers(accessGroupSelection.getSelectedItem().toString(), gradeSelection.getSelectedItem().toString())){
		//	userSelection.addItem(user);
		//}
		editUsers.add(userSelection);
		editUsers.add(passwordField);
		
		
		
		//configPanel.add(new JLabel("Datenbank auf Werkseinstellungen"));
		dbReset = new JCheckBox("Datenbank auf Werkseinstellungen zurücksetzten");
		dbReset.setActionCommand("resetDB");
		
		configPanel.add(groupSelection);
		configPanel.add(addUser);
		configPanel.add(editUsers);
		configPanel.add(dbReset);
		mainPanel.add(configPanel, BorderLayout.CENTER);
		
		// - Damit Button im Border Layout (mainPanel) richtige Größe erhät, wird er in eigenes Panel gekapselt
		submit=new JButton("Übernehmen");
		submit.setActionCommand(DCCommand.ResetDB.toString());
		JPanel submitPanel = new JPanel();
		submitPanel.add(submit);
		
		mainPanel.add(submitPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void addController(Controller controller){
		dbReset.addItemListener(controller);
		gradeSelection.addActionListener(controller);
	}
	
	@Override
	public void update() {
		dbReset.setSelected(model.isResetDB());
	}

	@Override
	public void update(Observable o, Object arg) {
		update();	
	}
}
