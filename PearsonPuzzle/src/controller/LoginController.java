/*package controller;

import java.awt.event.ActionEvent;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;

import model.Model;
import view.PupilView;
import view.View;

public class LoginController extends Controller{
	String username; 
	char[] password;

	public LoginController(Model model, View view, JTextField username, JPasswordField password) {
		super(model, view);
		try {
			this.username=username.getText(0, 20);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.addController(this);	
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		//model.setUsername(view.getUsername());
		System.out.println(username);
		this.view=new PupilView();
		// TODO Auto-generated method stub
		
	}

}
*/