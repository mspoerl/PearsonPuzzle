package visitor;

import jUnitUmgebung.UnitRunner;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.UIManager;

import model.Model;

import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;

import view.JView;
import view.LoginView;
import view.PPException;
import view.applet.AppletView;
import view.dialog.InitializeAccess;
import controller.AppletController;
import controller.Controller;
import controller.DefaultController;

/**
 * Klasse dient dazu, die GUI zu erzeugen.<br>
 * Soll später zur Factory werden, um Controller, View und Model zu verknüpfen.
 * 
 * @author workspace
 */
public class GUIBuilder {
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
	// runJEdit();
    }

    public GUIBuilder() {
    }

    public GUIBuilder(Model model, AppletView view) {
	controller = new AppletController(model, view);
	view.addController(controller);
    }

    public GUIBuilder(Model model, JView view) {
	controller = new DefaultController(model, view);
	view.addController(controller);
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

    @SuppressWarnings("deprecation")
    public void setupExceptionGUI() {
	Model model = new Model();
	JView view = new InitializeAccess(model);
	controller = new controller.ExceptionController(model, view);
	view.drawFrame();
    }

    public Controller getController() {
	return controller;
    }

    public static void runUnitTest() {

	final String unitText = "import org.junit.Test; \nimport static org.junit.Assert.*; \n\npublic class testcase_Test{\n\t@Test\n\t"
		+ "public void testMethode1(){ \n"
		+ "\t\tassertTrue( true);\n\t}}\n";
	final String code = "public class test{public static boolean runt(){int a=0; int b=3; int c=a+b; return true;}}";
	UnitRunner jUnitRunner;
	try {
	    jUnitRunner = new UnitRunner(unitText, code, null, null, null);
	    jUnitRunner.run(unitText);
	} catch (PPException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void runJEdit() {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setLayout(new FlowLayout());

	frame.setSize(800, 500);
	frame.setLocationRelativeTo(null);
	JEditTextArea jeditArea = new JEditTextArea();
	jeditArea.setElectricScroll(10);
	jeditArea.setText("public class juhu{\n" + "}\n\n\n\n\nasd");
	jeditArea.setTokenMarker(new JavaTokenMarker());
	frame.add(jeditArea);
	frame.setVisible(true);
	frame.pack();
	frame.validate();
    }
}
