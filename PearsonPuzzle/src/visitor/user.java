package visitor;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import controller.DefaultController;
import model.Model;
import view.LoginView;

/**
 * Klasse dient dazu, die GUI zu erzeugen.
 * Soll später zur Factory werden, um Controller, View und Model zu verknüpfen.
 * 
 * 
 * @author workspace
 *
 */
public class user {
	public static void main (String args[]){
		Model model = new Model();
		LoginView startView = new LoginView(model);
		@SuppressWarnings("unused")
		DefaultController controller= new DefaultController(model, startView);
		startView.draw();
	}
}
