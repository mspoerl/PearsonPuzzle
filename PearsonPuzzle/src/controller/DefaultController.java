package controller;

import java.awt.event.ActionEvent;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import model.Model;
import view.LoginView;
import view.JView;
/**
 * Klasse dient dazu, die standardmäßige Benutzeroberfläche aufzurufen und 
 * mit dem Controller zu verknüpfen.
 * 
 * 
 * @author workspace
 *
 */
public class DefaultController extends Controller {
	public DefaultController(Model model, LoginView view) {
		super(model, view);
		view.addActionListener(this);
	}
	public DefaultController(Model model, JView view) {
		super(model, view);
	}
	/*public void submitPassword(JPasswordField password){
		if(password!=null)
		{
		model.setPassword(password.getPassword());
		}
	}
	public void submitUsername(JTextField username){
		try{
			model.setUsername(username.getText(0, 20));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	private JTextField jTextField;
	public void textFieldListener(JTextField jTextField){
		this.jTextField=jTextField;
	}*/

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("submitPassword")){
			view.submitChangeToController();
		}
		else if(e.getActionCommand().equals("openProject") ){
			view.quitView();
			view.openProject();
		}
		else if(e.getActionCommand().equals("openProjectList")){
			view.quitView();
			view.openProjectList();
		}
		else if(e.getActionCommand().equals("saveChanges")){
			model.setSaveList(model.getCodeModel());
			view.update();
		}
		else if(e.getActionCommand().equals("logout")){
			view.quitView();
			this.model=new Model();
			this.view=new LoginView(model);
			view.update();
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getValueIsAdjusting()){
			System.out.println(e.getLastIndex() + " " + e.getFirstIndex() +" "+ model.getSelectedProject());
			if(model.getSelectedProject()==e.getFirstIndex()){
				model.setSelectedProject(e.getLastIndex());
			}
			else{
				model.setSelectedProject(e.getFirstIndex());
			}
			System.out.println(e.getLastIndex() + " " + e.getFirstIndex() +" "+ model.getSelectedProject());
			view.update();
		}
	}
}
