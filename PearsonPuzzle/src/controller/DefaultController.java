package controller;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import model.AccessGroup;
import model.Model;
import view.LoginView;
import view.JView;
import view.pupil.CodeSortView;
import view.pupil.PupilView;
import view.teacher.TeacherView;
import view.teacher.TextEditor;
/**
 * Klasse dient dazu, die standardmäßige Benutzeroberfläche aufzurufen und 
 * mit dem Controller zu verknüpfen.
 * 
 * @author workspace
 */
public class DefaultController extends Controller {
	
	public DefaultController(Model model, JView view) {
		super(model, view);
		view.setController(this);
	}
	
	/**
	 * Legt fest, was bei einem Action Event (z.B. Button drücken) passiert
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(DCCommand.submitPassword.toString())){
			// FIXME: Ab und zu wird die Aktion 2x ausgeführt
			if(view.getClass().equals(LoginView.class)){
				((LoginView)view).submitChangeToController();
			}
		}
		else if(e.getActionCommand().equals(DCCommand.editProject.toString())){
			if(model.getProjectListID()==null){
				view.allert("Bitte Projekt auswählen!");
			}
			else{
				view.quitView();
				this.view=new TextEditor(model);
				view.addController(this);
			}
		}
		else if(e.getActionCommand().equals(DCCommand.newProject.toString())){
			model.setProject(null);
			view.quitView();
			this.view=new TextEditor(model);
			view.addController(this);
		}
		else if(e.getActionCommand().equals(DCCommand.openProject.toString()) ){
			if(model.getProjectListID()==null){
				view.allert("Bitte Projekt auswählen!");
			}
			else{
				view.quitView();
				this.view=new CodeSortView(model);
				view.addController(this);
			}
		}
		else if(e.getActionCommand().equals(DCCommand.projectList.toString())){
			view.quitView();
			if(model.getAccessGroup().equals(AccessGroup.PUPIL)){
				this.view= new PupilView(model);
			}
			else{
				this.view= new TeacherView(model);
			}
			view.addController(this);
			
		}
		else if(e.getActionCommand().equals(DCCommand.saveChanges.toString())){
			model.setSaveList(model.getCodeModel());
			view.update();
		} 
		else if(e.getActionCommand().equals(DCCommand.logout.toString())){
			view.quitView();
			this.model=new Model();
			LoginView startView= new LoginView(model);
			startView.setController(this);
			startView.addController(this);
			this.view=startView;
			view.update();
		}
		else if(e.getActionCommand().equals(DCCommand.saveProject.toString())){
			if(view.getClass().equals(TextEditor.class) && model.getAccessGroup().equals(AccessGroup.TEACHER)){
				if((((TextEditor) view).getCode()).equals("")
						|| (((TextEditor) view).getProjectName()).equals("")){
					view.allert("Bitte Titel und Inhalt des Projekts angeben");
				}
				else{
					// TODO: Fall behandeln, dass Projekt mit gleichem Namen existiert!
					model.setProjectCode(((TextEditor)view).getCode(), ((TextEditor)view).getProjectName(),150);
					ArrayList <JTextField> inputFields = (((TextEditor)view).getInputComponents()); 
					model.setTabSize(Integer.parseInt(inputFields.get(0).getText()) % 10);
					model.setGrade(Integer.parseInt(inputFields.get(1).getText()));
					view.allert("Projekt wurde gespeichert");
					view.update();
				}
			}
		}
	}
	
	/**
	 * XXX: Soll !eventuell! ins Modell ausgelagert werden
	 * Nutzername @param username
	 * Passwort @param password
	 */
	public void login(String username, char[] password){
		model.setUsername(username);
		if(username.isEmpty() || password.length==0){
			view.allert("Bitte Nutzernamen und Passwort eingeben");
		}
		else if(model.getAccessGroup(username, password)==AccessGroup.TEACHER){
			model.setAccessGroup(AccessGroup.TEACHER);
			view.quitView();
			this.view=new TeacherView(model);
			view.addController(this);
		}
		else if(model.getAccessGroup(username, password)==AccessGroup.PUPIL){
			model.setAccessGroup(AccessGroup.PUPIL);
			view.quitView();
			this.view=new PupilView(model);
			view.addController(this);
		}
		else{
			view.allert("Zugang verweigert");
		}
	}
	
	/**
	 * Legt fest, was beim Ändern der Selektion eines Listenelemts passiert
	 * @param <ListSelectionModel>
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
        } 
        else {
            // Find out which indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    model.setProject(i);
                    view.update();
                }
             }
        }
	}
}
