package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import javax.swing.DefaultListModel;


/**
 * Klasse dient dazu, alle für die Benutzer notwendigen Daten einzulesen, zu halten
 * und zu speichern.
 * Die Extension Observable implementiert die Methoden: 
 * 	- add Observer (Observer o) 
 * 	- setChanged()
 *  - notifyObservers()
 * 
 * @author workspace
 *
 */

public class Model extends Observable {
	private String username;
	private char[] password;
	private Code code;
	private DefaultListModel codeModel;
	private DefaultListModel saveModel;
	private List <String> projectList;
	private int SelectedProject;
	private String projectDescription;
	public Model(){
		code = new Code();
		this.codeModel=code.getCode();
		this.saveModel=code.getSaveModel();
		this.projectList= fetchProjects();	
	}
	
	public void setSaveList(DefaultListModel <String> listModelToSave){
		saveModel=listModelToSave;
	}
	
	private List <String> fetchProjects(){
		String[] projects = {"Project1", "Project2", "Project3"};
		List<String> projectList = new ArrayList<String> ();
		for(String line:projects){
			projectList.add(projectList.size(),line);
		}
		return projectList;
	}
	public void setPassword(char[] password) {
		if (isPasswordCorrect(password)) {
            
        } 
		else{}
        //Zero out the possible password, for security.
        Arrays.fill(password, '0');
	}
	private static boolean isPasswordCorrect(char[] input) {
	    boolean isCorrect = true;
	    char[] correctPassword = { 'b', 'u', 'g', 'a', 'b', 'o', 'o' };

	    if (input.length != correctPassword.length) {
	        isCorrect = false;
	    } else {
	        isCorrect = Arrays.equals (input, correctPassword);
	    }

	    //Zero out the password.
	    Arrays.fill(correctPassword,'0');

	    return isCorrect;
	}
	
	/*
	 * TODO: in accessGroup auslagern
	 */
	public accessGroup getAccessGroup(){
		if(username.equals("Name")){
			return accessGroup.PUPIL;
		}
		else if(username.equals("TUM")){
			return accessGroup.TEACHER;
		}
		else
			return accessGroup.UNKNOWN;
	}
	public String getUsername(){
		return username;
	}
	public void setUsername(String username){
		System.out.println(username);
		this.username=username;
	}
	public DefaultListModel getCodeModel(){
		return codeModel;
	}
	public DefaultListModel getSaveModel(){
		return saveModel;
	}
	public List<String> getProjects() {
		return projectList;
	}

	public int getSelectedProject() {
		return SelectedProject;
	}
	public void setSelectedProject(int selectedProject) {
		SelectedProject = selectedProject;
	}
	public String getProjectDescription() {
		return projectList.get(getSelectedProject());
	}
}
