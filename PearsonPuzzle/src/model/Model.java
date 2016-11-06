package model;

import javax.swing.JList;
import javax.swing.JTextField;

import view.View;


public class Model {
	private String username; 
	private char[] password;
	private static Code code;
	public Model(){
		code = new Code();
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		this.username=username;
	}
	public static JList<String> getCodeList(){
		return Code.getCodeList();
	}
	public static JList<JTextField> getTextFields(){
		return code.getCodeTextfields();
	}
	public void addObserver(View design){	
	}
	public JList<String> getSaveList() {
		return code.getSaveList();
	}
	
}
