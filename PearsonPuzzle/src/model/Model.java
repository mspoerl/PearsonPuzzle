package model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;


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
	//private char[] password;
	private Code code;
	private ArrayList<String> codeList;
	private ArrayList<String> saveList;
	private List <String> projectList;
	private int SelectedProject;
	//private String projectDescription;
	private String projectCode;
	private int tabSize;
	private boolean randomMode;
	private int grade;
	public Model(){
		code = new Code();
		this.codeList=code.getCodeList();
		this.saveList=code.getSaveList();
		this.projectList= fetchProjects();
		// Default Werte werden gesetzt
		this.tabSize=3;
		this.randomMode=true;
		this.grade=0;
		projectCode= new String(" Zeile 1: Dies ist ein erstes Testprojekt \n Zeile 2: um zu sehen,\n \t Zeile 3: wie Java dies und die Zeilenumbrüche \n \t Zeile 4: darstellt");
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		if(grade<14 && grade >5){
			this.grade = grade;
		}
		else{
			// TODO: Fehlerausgabe: Diese Jahrgangsstufe ist nicht klassifiziert
		}
	}
	public void setTabSize(int tabWidth) {
		this.tabSize=tabWidth;
	}
	public int getTabSize(){
		return tabSize;
	}
	public void setRandomMode(boolean random){
		this.randomMode=random;
	}
	public boolean getRandomMode(){
		return randomMode;
	}
	
	public void setSaveList(ArrayList<String> listModelToSave){
		saveList=listModelToSave;
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
		else if(username.equals("TUM") || username.equals("Lehrer")){
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
	public ArrayList<String> getCodeModel(){
		return codeList;
	}
	public ArrayList<String> getSaveModel(){
		return saveList;
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
	public String getProjectCode() {
		return new String(projectCode);
	}
	public String[] getProjectCodeArray(){
		String[] parts = projectCode.split("\n");
		if(randomMode){
			String buffer;
			for(int i=parts.length-1; i>0;i--){
				int randomInt = new java.util.Random().nextInt(i);
				buffer=parts[randomInt];
				parts[randomInt]=parts[i];
				parts[i]=buffer;
			}
		}
		return parts;
	}
	public void setProjectCode(String codeString) {
		// TODO: In Datenbank speichern
		projectCode=new String(codeString);	
	}
}

	
