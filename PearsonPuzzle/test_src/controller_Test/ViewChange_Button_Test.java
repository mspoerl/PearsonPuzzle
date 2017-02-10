package controller_Test;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import model.Model;
import model.access.AccessGroup;
import view.*;
import view.teacher.*;
import view.pupil.*;
import controller.Controller;
import controller.DCCommand;
import controller.DefaultController;

@RunWith(Parameterized.class)
public class ViewChange_Button_Test {

	private Model model;
	private DCCommand command;
	private Class<View> viewToBe;
	private Class<View>[] startViews;
	private AccessGroup acGroup;
	
	@SuppressWarnings("unchecked")
	public ViewChange_Button_Test(AccessGroup acGroup, DCCommand command, Class<View> viewToBe){
		this.command = command;
		this.viewToBe = viewToBe;
		this.acGroup = acGroup;
		startViews = new Class[] { PupilView.class, CodeSortView.class,
				TeacherView.class, ConfigEditor.class, OptionConfiguration.class, 
				PreViewEditor.class, UnitEditor.class, TextEditor.class, UserEditor.class};
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> commandAndView(){
		return Arrays.asList(new Object[][]{

			{AccessGroup.STUDENT, DCCommand.Login, null}, 

			{AccessGroup.STUDENT, DCCommand.Logout, LoginView.class}, 
			{AccessGroup.TEACHER, DCCommand.Logout, LoginView.class}, 
			
			{AccessGroup.TEACHER, DCCommand.EditConfig, ConfigEditor.class},
			{AccessGroup.STUDENT, DCCommand.EditConfig, null},
			{AccessGroup.UNAUTHORIZED, DCCommand.EditConfig, null},
			
			{AccessGroup.TEACHER, DCCommand.EditJUnit , UnitEditor.class},
			{AccessGroup.STUDENT, DCCommand.EditJUnit , null},
			{AccessGroup.UNAUTHORIZED, DCCommand.EditJUnit , null}, 
			
			{AccessGroup.STUDENT, DCCommand.EditUsers, UserEditor.class}, 
			{AccessGroup.STUDENT, DCCommand.EditPreview, PreViewEditor.class}, 
			{AccessGroup.STUDENT, DCCommand.NewProject, TextEditor.class}, 
			
			{AccessGroup.TEACHER, DCCommand.EditProject, TextEditor.class},
			{AccessGroup.STUDENT, DCCommand.ProjectList, PupilView.class},
			{AccessGroup.UNAUTHORIZED, DCCommand.ProjectList, PupilView.class},
			
			{AccessGroup.STUDENT, DCCommand.OpenProject, CodeSortView.class},
			{AccessGroup.UNAUTHORIZED, DCCommand.OpenProject, CodeSortView.class},
			
			{AccessGroup.TEACHER, DCCommand.Admin, OptionConfiguration.class}
		});
	}
	
	@Before
	public void create(){
		model = new Model();
		Assume.assumeTrue("Es sind leider keine Projekte vorhanden", selectFirstProject());
	}
	
	private void sleep(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	@Test
	public void viewChange_buttonTest() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		for(Class<View> view_class : startViews){
			View startView = (View) view_class.getConstructor(Model.class).newInstance(model);
				
			if(!view_class.equals(startView.getClass())){
				fail("Start View wurde nicht korrekt instanziert");
			}
			Controller controller=null;
			if(view_class.getSuperclass().equals(JView.class))
				controller = new DefaultController(model, (JView) startView);

			Assume.assumeNotNull(controller);
			selectFirstProject();
			model.setAccessGroup(acGroup);
			JButton testButton = new JButton();
			sleep(10);
			ActionEvent e = new ActionEvent(testButton, 1, command.toString());
			controller.actionPerformed(e);
			sleep(10);
			if(viewToBe == null)
				assertEquals("View hätte sich durch Kommando nicht ändern dürfen", startView.getClass(), controller.getView().getClass());
			else
				assertEquals("Kommando "+command+" führte nicht zu "+view_class, viewToBe,controller.getView().getClass());
			model.deleteObservers();
			controller = null;
			startView = null;
		}
	}
	
	private boolean selectFirstProject(){
		if(model.getProjectVector()==null 
				|| model.getProjectVector().size()<1)
			return false;
		else
			model.selectProject(0);
		return true;
	}
	
	@After
	public void sleep(){
		sleep(10);
	}
}
