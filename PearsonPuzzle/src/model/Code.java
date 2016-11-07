package model;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;


public class Code {
	private static String[] codeLines = {"Line1","Line2","Line3","Line4","Line5"};
	private static JList<String> codeList;
	private static JList<String> saveList;
	private static DefaultListModel code=new DefaultListModel();
	private static DefaultListModel saveModel=new DefaultListModel();
	private JList<JTextField> codeTextFields;
	/*
	 * soll noch erweitert werden, Daten aus datenbank, List, ...
	 */
	Code(){
		codeList = new JList<String> (codeLines);
		codeTextFields=new JList<JTextField>();
		for(String line: codeLines){
			codeTextFields.add(new JTextField(line));
			code.add(0, line);
			saveModel.add(0, new String());
		}
		saveList=new JList<String>(new String[codeLines.length]);
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
	public DefaultListModel getCode(){
		return code;
	}
	public DefaultListModel getSaveModel(){
		return saveModel;
	}

}
