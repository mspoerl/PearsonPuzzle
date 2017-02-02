package view;

import java.util.Observable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import controller.Controller;
import controller.DCCommand;
import controller.DefaultController;
import model.Model;

/**
 * Definiert die Login Ansicht, die zugleich als start Screen fungiert.
 * 
 * @author workspace
 *
 */
public class LoginView extends JView{
	private JPanel loginPanel;
	private JButton enter;
	private JButton startApplet;
	private JButton importDatabase;
	private JTextField username;
	private JPasswordField password;
	public LoginView(Model model){
		super(model);
		setupFrame();
		setupLoginPanel();
		this.addMenuToFrame(new JMenuBar());
		mainPanel.revalidate();
	}
	
	private void setupLoginPanel(){
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
		
		importDatabase = new JButton("Daten Laden");
		importDatabase.setActionCommand(DCCommand.DB_Import.toString());
		loginPanel.add(importDatabase);
		loginPanel.add(Box.createVerticalStrut(10));
		
		startApplet = new JButton("Los gehts");
		startApplet.setActionCommand(DCCommand.Applet.toString());
		startApplet.setName("Applet");
		loginPanel.add(startApplet);
		loginPanel.add(Box.createVerticalStrut(10));
		
		username = new JTextField("TUM");
		username.setActionCommand(DCCommand.Login.toString());
		JLabel label = new JLabel("Login");
		label.setLabelFor(username);
		loginPanel.add(label);
		loginPanel.add(username);
		
		loginPanel.add(new JLabel("Password"));
		password = new JPasswordField("TUM");
		password.setName("pwd");
		password.setActionCommand(DCCommand.Login.toString());
		loginPanel.add(password);
		
		enter = new JButton("Login");
		enter.setActionCommand(DCCommand.Login.toString());
		loginPanel.add(enter);
		
		mainPanel.add(loginPanel);
		
		// Für Erstbenutzung prüfen, ob Datenbank existiert
		if(model.getException()!=null)
			showDialog(model.getException(), true);
	}
	
	/**
	 * Controller mit Action Listener Implementierung @param controller
	 */
	public void addController(Controller controller) {
		importDatabase.addActionListener(controller);
		startApplet.addActionListener(controller);
		enter.addActionListener(controller);
		username.addActionListener(controller);
		password.addActionListener(controller);
	}
	
	/**
	 * TODO: Sollte auf eine Login Methode des Models zugreifen
	 * Login Methode wird ausgeführt
	 */
	public void submitChangeToController(){
		if(this.getController().getClass().equals(DefaultController.class)){
			((DefaultController)(this.getController())).login(username.getText(), password.getPassword());
		}
	}
	@Override
	public void update() {
	}

	@Override
	public void update(Observable o, Object arg) {
		if(arg!=null && arg.getClass().equals(PPException.class)){
			this.showDialog((PPException) arg, true);
		}
		// TODO Auto-generated method stub
		
	}
}
