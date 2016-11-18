package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.MenuEvent;

import model.Model;
import view.JView;

public class LoginController extends Controller implements ActionListener{
	public LoginController(Model model, JView view) {
		super(model, view);
	}
	JTextField username;
	JPasswordField password;
	
    public void LoginListener(JTextField username, JPasswordField password) {
        this.username = username;
        this.password = password;
    }
	public void actionPerformed(ActionEvent e) {
		model.setPassword(password.getPassword());
		model.setUsername(username.getText());
		view.update();
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
	/*
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