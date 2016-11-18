package controller;

import java.awt.event.ActionEvent;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import model.Model;
import view.LoginView;
import view.JView;
import view.TextEditor;
/**
 * Klasse dient dazu, die standardmäßige Benutzeroberfläche aufzurufen und 
 * mit dem Controller zu verknüpfen.
 * 
 * @author workspace
 */
public class DefaultController extends Controller {
	public DefaultController(Model model, LoginView view) {
		super(model, view);
		view.addActionListener(this);
	}
	public DefaultController(Model model, JView view) {
		super(model, view);
	}
	/**
	 * Legt fest, was bei einem Action Event (z.B. Button drücken) passiert
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("submitPassword")){
			((LoginView)view).submitChangeToController();
		}
		else if(e.getActionCommand().equals("editProject")){
			view.quitView();
			this.view=new TextEditor(model);
			view.addController(this);
			view.update();
		}
		else if(e.getActionCommand().equals("openProject") ){
			view.quitView();
			view.selectView(0);
		}
		else if(e.getActionCommand().equals("openProjectList")){
			view.quitView();
			view.selectView(1);
		}
		else if(e.getActionCommand().equals("saveChanges")){	
			model.setSaveList(model.getCodeModel());
			view.update();
		}
		else if(e.getActionCommand().equals("logout")){
			view.quitView();
			this.model=new Model();
			LoginView startView= new LoginView(model);
			startView.addActionListener(this);
			startView.addController(this);
			this.view=startView;
			view.update();
		}
		else if(e.getActionCommand().equals("saveProject")){
			model.setProjectHtml(((TextEditor)view).getCode());
			view.allert("Projekt wurde gespeichert");
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
                    model.setSelectedProject(i);
                    System.out.println(i);
                    view.update();
                }
             }
        }
	}
}
