package visitor;

import controller.Controller;
import controller.DefaultController;
import model.Model;
import view.StartView;
import view.View;

public class user {
	public static void main (String args[]){
		Model model = new Model();
		StartView startView = new StartView();
		DefaultController controller= new DefaultController(model, startView);
		startView.draw();
	}

}
