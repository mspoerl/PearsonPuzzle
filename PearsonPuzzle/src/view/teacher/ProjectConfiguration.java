package view.teacher;

import java.util.Observable;
import javax.swing.*;

import controller.Controller;
import view.JView;
import model.Model;

public class ProjectConfiguration extends JView{
	
	private JList <String> codeList;
	private JList <Integer> sequenceList;
	private JButton newGroup;

	public ProjectConfiguration(Model model) {
		super(model);
		setupConfigPanel();
		draw();
	}
	
	private void setupConfigPanel(){
		JPanel configPanel=new JPanel();
		codeList = new JList <String>(model.getCodeVector());
		codeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		sequenceList = new JList<Integer> (model.getSequenceVector());
		sequenceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		configPanel.add(codeList);
		configPanel.add(sequenceList);
		mainPanel.add(configPanel);
	}

	@Override
	public void addController(Controller controller) {
		sequenceList.getSelectionModel().addListSelectionListener(controller);
	}

	@Override
	public void update() {
	}

	@Override
	public void update(Observable o, Object arg) {
	}

}
