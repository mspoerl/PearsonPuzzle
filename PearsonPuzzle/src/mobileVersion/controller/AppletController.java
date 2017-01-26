package mobileVersion.controller;

import jUnitUmgebung.UnitRunner;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JRootPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import org.junit.runner.Result;

import controller.Controller;
import controller.DCCommand;

import mobileVersion.view.AppletView;
import mobileVersion.view.CodeSortAView;
import mobileVersion.view.ProjectListAView;
import model.Model;
import model.access.AccessGroup;

import view.Allert;
import view.PPException;

public class AppletController implements Controller{
	
	private Model model;
	private AppletView view;

	public AppletController(Model model, AppletView view){
		this.model = model;
		this.view = view;
		view.addController(this);
	}

	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		DCCommand cmd = DCCommand.valueOf(e.getActionCommand());
		switch(cmd){
		case Login:
			model.login((String)view.get("username"), (char[])view.get("password"));
			if(model.getAccessGroup()==AccessGroup.UNAUTHORIZED) 
				return;
		case ProjectList:
				setView( new ProjectListAView(model));
			break;
		case OpenProject:
			if(model.getProjectListID()==null)
				view.showDialog(Allert.noProjectSelected);
			else{
				view.removeController(this);
				CodeSortAView view = new CodeSortAView(model);
				setView(view);
			}
			break;
		case TestCode:
			Result result;
			model.testOrderOfSollution();
				if(model.getJUnitCode()!=null){ // FIXME: diese if-Abfrage gehört in den UnitRunner
					UnitRunner unitRunner;
					try {
						unitRunner = new UnitRunner(model.getJUnitCode(), model.getProjectCode(), model.getImport("methods"));
						unitRunner.addOnlineImport(model.getImport("online"));
						unitRunner.addClasses(model.getImport("classes"));
						result = unitRunner.run();
						System.out.println(result.getFailures());
						System.out.println("Anzahl der Fehler im Junit Testlauf:"+result.getFailureCount());;
						model.setJunitFailures(result);
					} catch (PPException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			break;
		default:
			return;
		}
	}

	private void setView(AppletView view) {
		if(view!=null){
		JRootPane frame = this.view.getRootPane();
		frame.setContentPane(view);
		view.draw();
		view.addController(this);
		this.view=view;
		frame.validate();
		}
	}

	public AppletView getView() {
		return null;
	}

	/**
	 * Legt fest, was beim Ändern der Selektion eines Listenelemts passiert.
	 * @param <ListSelectionModel>
	 */
	public void valueChanged(ListSelectionEvent e) {
		System.out.println("asd"+view.getClass());
		ListSelectionModel lsm = (ListSelectionModel)e.getSource();
        if (((ListSelectionModel) e.getSource()).isSelectionEmpty()) {
        } 
        else {
            // Findet heraus, welcher Index selektiert ist
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                	
                		model.selectProject(i);
                }
             }
        }
	}
}
