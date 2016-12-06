package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import model.AccessGroup;
import model.Model;
import view.Allert;
import view.LoginView;
import view.JView;
import view.pupil.CodeSortView;
import view.pupil.PupilView;
import view.teacher.ConfigEditor;
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
		act(DCCommand.valueOf(e.getActionCommand()));
	}
	private void act(DCCommand cmd){
		// Es erfolgt Warnung, wenn Projekt noch nicht gespeicher wurde
		if(view.getClass().equals(TextEditor.class)  
				&& cmd!=DCCommand.saveProject){
			// FIXME: Nur, wenn Nutzer Projekt geändert hat, soll Allert ausgegeben werden
			Integer allert=view.showMessage(Allert.notSaved);
			if(allert==JOptionPane.YES_OPTION){
				this.act(DCCommand.saveProject);
			}
			else if(allert==JOptionPane.CANCEL_OPTION){
				return;
			}
		}
		switch(cmd){
			case submitPassword:
				if(view.getClass().equals(LoginView.class)){
					((LoginView)view).submitChangeToController();
				}
				break;
			case editProject:
				if(model.getProjectListID()==null){
					view.showMessage(Allert.noProjectSelected);
				}
				else{
					view.quitView();
					this.view=new TextEditor(model);
					view.addController(this);
				}
				break;
			case newProject:
				model.selectProject(null);
				view.quitView();
				this.view=new TextEditor(model);
				view.addController(this);
				break;
			case openProject:
				if(model.getProjectListID()==null){
					view.showMessage(Allert.noProjectSelected);
				}
				else{
					view.quitView();
					this.view=new CodeSortView(model);
					view.addController(this);
				}
				break;
			case projectList:
				view.quitView();
				if(model.getAccessGroup().equals(AccessGroup.PUPIL)){
					this.view= new PupilView(model);
				}
				else{
					this.view= new TeacherView(model);
				}
				view.addController(this);
				break;
			case admin:
				if(model.getAccessGroup().equals(AccessGroup.TEACHER)){
					view.quitView();
					this.view=new ConfigEditor(model);
					view.addController(this);
				}
				break;
			case saveChanges:
				model.setSaveList(model.getCodeModel());
				view.update();
				break;
			case logout:
				view.quitView();
				this.model=new Model();
				LoginView startView= new LoginView(model);
				startView.setController(this);
				startView.addController(this);
				this.view=startView;
				view.update();
				break;
			case saveProject:
				// Berechtigung wird geprüft
				if(view.getClass().equals(TextEditor.class) 
						&& model.getAccessGroup().equals(AccessGroup.TEACHER)){
					
					if(((TextEditor) view).getCode()==null 
							|| ((TextEditor)view).getProjectName()==null
							|| (((TextEditor) view).getCode()).equals("")
							|| (((TextEditor) view).getProjectName()).equals("")){
					
						view.showMessage(Allert.noContentInput);
					}
					
					else{
						
						// ---- Es wird versucht das Projekt zu speichern, schlägt dies fehl, so existiert bereits ein Projekt mit gleichem Namen
						if(model.saveProject(((TextEditor)view).getCode(), ((TextEditor)view).getProjectName(),150))
						{
							ArrayList <JTextField> inputFields = (((TextEditor)view).getInputComponents()); 
							model.setTabSize(Integer.parseInt(inputFields.get(0).getText()) % 10);
							model.setGrade(Integer.parseInt(inputFields.get(1).getText()));
							// TODO: Test, ob erfolgreich gespeichert wurde
							view.showMessage(Allert.projectSaved);
						}
						else{
							view.showMessage(Allert.projectExists);
						}
					}
				}
				break;
			case deleteProject:
				if(view.getClass().equals(TeacherView.class) 
						&& model.getAccessGroup().equals(AccessGroup.TEACHER)){
					if(model.getProjectListID()==null){
						view.showMessage(Allert.noProjectSelected);
					}
					else{
						view.allert("Sind Sie sicher, dass Sie das Projekt löschen wollen?");
						// TODO: Auswahlmöglichkeit zu Allert hinzufügen
						if(model.removeProject())
						{
							view.showMessage(Allert.projectDeleted);
							view.update();
						}
					}
				}
				break;
			case setConfig:
				if(model.getAccessGroup()==AccessGroup.TEACHER){
					model.updateConfig();
				}
			default:
				break;				
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
                    model.selectProject(i);
                    view.update();
                }
             }
        }
	}

	public void itemStateChanged(ItemEvent e) {
		if(((AbstractButton) e.getItem()).getActionCommand()==DCCommand.resetDB.toString()){
			if(e.getStateChange() == ItemEvent.SELECTED
				&& !model.isResetDB()){
					if(view.showMessage(Allert.reset)==JOptionPane.YES_OPTION)
					model.setResetDB(true);
					else
					model.setResetDB(false);
			}
			else
			model.setResetDB(e.getStateChange()==ItemEvent.SELECTED);			
		}
	}
}
