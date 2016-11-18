package model;

import java.awt.List;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextField;
/**
 * Klasse, um den Code aus der Datenbank zu holen, zu speichern 
 * und für andere Klassen bereit zu stellen.
 * 
 * @author workspace
 *
 */
public class Code {
	private String[] codeLines = {"Line1","Line2","Line3","Line4","Line5"};
	private ArrayList<String> codeList = new ArrayList<String>();
	private ArrayList<String>  saveList = new ArrayList<String> ();
	private JList<JTextField> codeTextFields;
	/*
	 * soll noch erweitert werden, Daten aus datenbank, List, ...
	 */
	Code(){
		codeTextFields=new JList<JTextField>();
		for(String line: codeLines){
			codeTextFields.add(new JTextField(line));
			codeList.add(codeList.size(), line);
			saveList.add(saveList.size(), new String());
		}
	}
	public void setCodeTextFields(){
		codeTextFields=new JList<JTextField>();
		for(String line: codeLines){
			codeTextFields.add(new JButton(line));
			System.out.println(JButton.getDefaultLocale());
		}	
	}
	public ArrayList<String> getCodeList(){
		return codeList;
	}
	public ArrayList<String> getSaveList(){
		return saveList;
	}

}
