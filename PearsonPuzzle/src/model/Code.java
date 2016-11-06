package model;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;


public class Code extends Element{
	private static String[] codeLines = {"Line1","Line2","Line3","Line4","Line5"};
	private static JList<String> codeList;
	private static JList<String> saveList;
	private JList<JTextField> codeTextFields;
	/*
	 * soll noch erweitert werden, Daten aus datenbank, List, ...
	 */
	Code(){
		codeList = new JList<String> (codeLines);
		codeTextFields=new JList<JTextField>();
		for(String line: codeLines){
			codeTextFields.add(new JTextField(line));
		}
		saveList=new JList<String>(codeLines);
	}
	public static JList<String> getCodeList(){
		return codeList;
	}
	public JList<JTextField> getCodeTextfields(){
		return codeTextFields;	
	}
	public void setCodeTextFields(){
		codeTextFields=new JList<JTextField>();
		for(String line: codeLines){
			codeTextFields.add(new JButton(line));
			System.out.println(JButton.getDefaultLocale());
		}	
	}
	public JList<String> getSaveList() {
		// TODO Auto-generated method stub
		return saveList;
	}

}
