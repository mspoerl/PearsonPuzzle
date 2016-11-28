package Controller;
import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;

import model.AccessGroup;
import model.Model;

import org.junit.*;

import view.JView;
import view.pupil.PupilView;
import controller.Controller;
import controller.DCCommand;
import controller.DefaultController;


public class randomKlickTest {
	
	
	@Test
	public void randomKlicktest() {
		ArrayList <String> commandList = new ArrayList<String>();
		for(DCCommand command : DCCommand.values()){
			commandList.add(command.toString());
		}
		for(int i=0; i<20;i++){
			int randomInt = new java.util.Random().nextInt(commandList.size());
			Model model = new Model();
			model.setAccessGroup(AccessGroup.PUPIL);
			JView view = new PupilView(model);
			Controller controller = new DefaultController(model, view);
			JButton testButton = new JButton();
			ActionEvent e = new ActionEvent(testButton, 1, commandList.get(randomInt));
			try{
				controller.actionPerformed(e);
			}
			catch(Exception ex){
				ex.printStackTrace();
				fail("Fehler beim Ausführen von: "+ commandList.get(randomInt));
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//view.exit();
		}
	}

}
