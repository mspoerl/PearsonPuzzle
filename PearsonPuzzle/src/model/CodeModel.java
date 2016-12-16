package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;

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
public class CodeModel {
	public final static String INSERT = "insertInSolution";
	public final static String REPLACE = "replaceInSolution";
	public final static String REMOVE = "removeInSolution";

	private final String projectname;
	
	private Vector<String> codeVector;
	private LinkedHashMap<String, Integer> codeMap;
	private LinkedList<Integer> sortedCode;
	private Vector<String> testExpressionsVector;
	private Vector<Vector<Integer>> codeLine_GroupMatrix;
	
	

	private int tabSize;
	private String projectDescription;
	private String projectCode;
	
	CodeModel(String projectname){
		this.projectname = projectname;
		codeVector= new Vector<String>();
		testExpressionsVector= new Vector<String>();
		codeMap = new LinkedHashMap<String, Integer>();
		sortedCode= new LinkedList<Integer>();
		codeLine_GroupMatrix = new Vector<Vector<Integer>>();
		projectDescription = new String();
		
		tabSize = 0;
	}

	public LinkedList<Integer> getSollution(){
		return sortedCode;
	}
	// Wird so gelöst, damit codeMap nicht öffentlich wird (diskutabel)
	public void insertInSolution(Integer index, String value){
		sortedCode.add(index, codeMap.get(value.trim()));
	}
	public void replaceInSolution(Integer index, String value){
		sortedCode.remove(index);
		sortedCode.add(index, codeMap.get(value.trim()));
	}
	public void removeInSolution(Integer index, String value){
		sortedCode.remove(index);
	}
	public boolean isExactOrder(){
		String sollutionString = new String();
		for (String string : getSolutionStrings()){
			sollutionString=sollutionString+string+"\n";
		}
		if(sollutionString.equals(projectCode))
		 	return true;
		return false;
	}
	public Vector<String> getSolutionStrings(){
		Vector<String> solution = new Vector<String>(codeMap.size());
		for(Integer index: sortedCode){
			solution.add(codeVector.get(index));
		}
		return solution;
	}
	
	
	public void setProjectCode(String projectCode_string) {
		this.projectCode=projectCode_string;
		codeVector.setSize(0);		
		String[] stringField = this.getRandomCode();
		Vector<Integer> codeLine_Group= new Vector<Integer>();
		
		for(String line: stringField){
			codeVector.add(line);
			testExpressionsVector.add(new String());
			codeMap.put(line.trim(), codeVector.size()-1);
			codeLine_Group.add(new Integer(0));
		}
		codeLine_GroupMatrix.add(codeLine_Group);
	}
	private String[] getRandomCode() {
		String[] parts = projectCode.split("\n");
		String buffer;
		for (int i = parts.length - 1; i > 0; i--) {
			int randomInt = new java.util.Random().nextInt(i);
			buffer = parts[randomInt];
			parts[randomInt] = parts[i];
			parts[i] = buffer;
		}
		return parts;
	}

	public Vector<String> getCodeVector(){
		return codeVector;
	}
	public void setCodeVector(Vector<String> codeVector) {
		this.codeVector=codeVector;
	}
	
	public int getTabSize() {
		return tabSize;
	}
	public void setTabSize(int tabSize) {
		this.tabSize = tabSize;
	}

	/**
	 * @return the projectCode
	 */
	public String getProjectCode() {
		return projectCode;
	}

	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	/**
	 * @return the codeLine_GroupMatrix
	 */
	public Vector<Vector<Integer>> getGroupMatrix() {
		return codeLine_GroupMatrix;
	}

	/**
	 * @return the testExpressionsVector
	 */
	public Vector<String> getTestExpressionsVector() {
		return testExpressionsVector;
	}

	/**
	 * @param testExpressionsVector the testExpressionsVector to set
	 */
	public void setTestExpressionsVector(Vector<String> testExpressionsVector) {
		this.testExpressionsVector = testExpressionsVector;
	}
}
