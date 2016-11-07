package visitor;

import controller.Controller;
import controller.DefaultController;
import model.Model;
import view.LoginView;
import view.View;

public class user {
	public static void main (String args[]){
		Model model = new Model();
		LoginView startView = new LoginView();
		DefaultController controller= new DefaultController(model, startView);
		startView.draw();
	}

}
