package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import controller.Controller;

public class LoginListener implements ActionListener {
	String username; 
	JPasswordField password;
	Controller controller;

    public LoginListener(JTextField username, JPasswordField password, Controller controller) {
        this.username = username.getText();
        this.password = password;
        this.controller = controller;
    }
	public void actionPerformed(ActionEvent e) {
        //myTextField.append(e.getActionCommand());
		controller.login( username, password.getPassword());
		System.out.println(username);
		System.out.println(password.getPassword());
        //myTextField.setCaretPosition(myTextField.getDocument().getLength());
    }
}