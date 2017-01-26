package visitor;

import java.awt.FlowLayout;
import java.util.Properties;

import jUnitUmgebung.UnitRunner;

import javax.swing.JFrame;
import javax.swing.UIManager;


import org.syntax.jedit.JEditTextArea;
import org.syntax.jedit.tokenmarker.JavaTokenMarker;


import controller.Controller;
import controller.DefaultController;
import controller.ExceptionController;
import model.Model;
import view.JView;
import view.LoginView;
import view.PPException;
import view.dialog.InitializeAccess;

/**
 * Klasse dient dazu, die GUI zu erzeugen.<br>
 * Soll später zur Factory werden, um Controller, View und Model zu verknüpfen.
 * 
 * @author workspace
 */
public class user {
	private static Controller controller;
	public static void main (String args[]){
//		try {
//			  UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
//		} catch( Exception e ) { e.printStackTrace(); }
		setupGUI();
		//runJEdit();
	}
	public static void setupGUI(){
		Model model = new Model();
		LoginView startView = new LoginView(model);
		controller = new DefaultController(model, startView);
		startView.drawFrame();
	}
	public static void setupExceptionGUI(){
		Model model = new Model();
		JView view = new InitializeAccess(model);
		controller = new ExceptionController(model, view);
		view.drawFrame();
	}
	public static void runUnitTest(){
		
		final String unitText = "import org.junit.Test; \nimport static org.junit.Assert.*; \n\npublic class testcase_Test{\n\t@Test\n\t"+"public void testMethode1(){ \n"+"\t\tassertTrue( true);\n\t}}\n";
		final String code = "public class test{public static boolean runt(){int a=0; int b=3; int c=a+b; return true;}}";
		UnitRunner jUnitRunner;
		try {
			jUnitRunner = new UnitRunner(unitText, code, null);
			jUnitRunner.run(unitText);
		} catch (PPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void runJEdit(){
//		final Properties props = new Properties();
//		StandaloneTextArea jeditText = new StandaloneTextArea(new IPropertyManager(){
//		@Override
//		public String getProperty(String name) {
//			// TODO Auto-generated method stub
//			return "";
//		}});

		
//		static final Properties props = new Properties();
//		 *     static IPropertyManager propertyManager;
//		 *
//		 *     static
//		 *     {
//		 *        props = new Properties();
//		 *        load(props);
//		 *        propertyManager = new IPropertyManager() {
//		 *        	public String getProperty() {
//		 *        		return props.getProperty();
//		 *        	}
//		 *        }
//		 *     }
//		 *
//		 *     public MyTextArea()
//		 *     {
//		 *         super(propertyManager);
//		 *     }
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new FlowLayout());
		
		frame.setSize(800,500);
		frame.setLocationRelativeTo(null);
		JEditTextArea jeditArea = new JEditTextArea();
		jeditArea.setElectricScroll(10);
		jeditArea.setText("public class juhu{\n" +
				"}\n\n\n\n\nasd");
		jeditArea.setTokenMarker(new JavaTokenMarker());		
		frame.add(jeditArea);
		frame.setVisible(true);
		frame.pack();
		frame.validate();
	}
}
