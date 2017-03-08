package visitor;

import javax.swing.UIManager;

import model.Model;
import view.JView;
import view.LoginView;
import controller.Controller;
import controller.DefaultController;

public class GUIBuilder_Student {
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
	GUIBuilder_Student usr = new GUIBuilder_Student();
	usr.setupGUI();
    }

    public GUIBuilder_Student() {
    }

    public void setupGUI() {
	if (controller == null) {
	    Model model = new Model();
	    LoginView startView = new LoginView(model);

	    // Verhindert, dass ein Schüler einen Account anlegen kann, wenn
	    // noch keine Datenbank existiert.

	    startView.disableAddUserDialog();
	    controller = new DefaultController(model, startView);
	    startView.addController(controller);
	    startView.setController(controller);
	    startView.drawFrame();
	} else
	    ((JView) controller.getView()).drawFrame();
    }

}