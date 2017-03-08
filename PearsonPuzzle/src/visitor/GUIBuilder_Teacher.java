package visitor;

import javax.swing.UIManager;

import model.Model;
import view.JView;
import view.LoginView;
import controller.Controller;
import controller.DefaultController;

public class GUIBuilder_Teacher extends GUIBuilder {
    private Controller controller;

    public static void main(String[] args) {
	try {
	    // UIManager.setLookAndFeel(
	    // UIManager.getSystemLookAndFeelClassName() );
	    UIManager.setLookAndFeel(UIManager
		    .getCrossPlatformLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace();
	}
	GUIBuilder usr = new GUIBuilder();
	usr.setupGUI();
    }

    public GUIBuilder_Teacher() {
    }

    public void setupGUI() {
	if (controller == null) {
	    Model model = new Model();
	    LoginView startView = new LoginView(model);
	    controller = new DefaultController(model, startView);
	    startView.addController(controller);
	    startView.setController(controller);
	    startView.drawFrame();
	} else
	    ((JView) controller.getView()).drawFrame();
    }

}
