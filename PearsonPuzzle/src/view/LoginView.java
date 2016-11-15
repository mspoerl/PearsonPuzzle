package view;

import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import controller.Controller;

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
	private JTextField username;
	private JPasswordField password;
	public LoginView(Model model){
		super(model);
		setupFrame();
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(loginPanel);
		username = new JTextField("Name");
		username.setActionCommand("Username");
		JLabel label = new JLabel("Login");
		label.setLabelFor(username);
		loginPanel.add(label);
		loginPanel.add(username);
		loginPanel.add(new JLabel("Password"));
		password = new JPasswordField(10);
		password.setName("pwd");
		password.setActionCommand("pwd");
		loginPanel.add(password);
		enter = new JButton("Los gehts");
		enter.setActionCommand("submitPassword");
		//enter.setActionCommand();
		loginPanel.add(enter);
	}	
	// Controller ist dafür zuständig
	//public void addActionListener(ActionListener listener){
		//enter.addActionListener(listener);
		//enter.addActionListener(controller);
		//username.addActionListener(controller);
		//password.addActionListener(controller);
	//}
	public void addActionListener(Controller controller) {
		enter.addActionListener(controller);		
		//enter.addActionListener(new LoginListener(username, password, controller));
		//username.addActionListener(controller);
		//password.addActionListener(controller);
	}
	public void quitView(){
		loginPanel.removeAll();
	}
	public void submitChangeToController(){
		this.getController().login(username.getText(), password.getPassword());
	}
	@Override
	public void update() {
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
