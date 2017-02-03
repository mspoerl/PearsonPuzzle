package mobileVersion.controller;

import jUnitUmgebung.UnitRunner;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.JRootPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import org.junit.runner.Result;

import compiler.TestCompiler;

import controller.Controller;
import controller.DCCommand;

import mobileVersion.view.AppletMenu;
import mobileVersion.view.AppletView;
import mobileVersion.view.CodeSortAView;
import mobileVersion.view.ProjectListAView;
import model.Model;
import model.access.AccessGroup;

import view.Allert;
import view.PPException;
import view.teacher.UnitEditor;

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
				model.deleteObserver(view);
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
		case Compile:		
				TestCompiler testCompiler = new TestCompiler(model.getSollution(), model.getImport("methods"), model.getImport("online"), model.getImport("classes"));
				testCompiler.compile();
				model.setCompilerFailures(testCompiler.getFailures());
			break;
		case Test:
				model.testOrderOfSollution();
			break;
		case TestCode:
				Result result;
				if(model.getJUnitCode()!=null && !model.getJUnitCode().isEmpty() && !model.getJUnitCode().equals(UnitEditor.DEFAULT_UNIT_CODE)){ //Junit Test soll nur erfolgen, wenn auch einer definerit ist
					UnitRunner unitRunner;
					try {
						unitRunner = new UnitRunner(model.getJUnitCode(), model.getProjectCode(), model.getImport("methods"),model.getImport("online"), model.getImport("classes"));
						result = unitRunner.run();
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
			model.addObserver(view);
			JRootPane frame = this.view.getRootPane();
			frame.setContentPane(view);
			view.addController(this);
			this.view=view;
			((AppletMenu) (frame).getJMenuBar()).setView(view);
			frame.revalidate();
		}
	}

	public AppletView getView() {
		return view;
	}

	/**
	 * Legt fest, was beim Ändern der Selektion eines Listenelemts passiert.
	 * @param <ListSelectionModel>
	 */
	public void valueChanged(ListSelectionEvent e) {
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
