package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Klasse dient dazu, alle für die Benutzer notwendigen Daten einzulesen, zu
 * halten und zu speichern. Die Extension Observable implementiert die Methoden:
 * - add Observer (Observer o) - setChanged() - notifyObservers()
 * 
 * @author workspace
 * 
 */

public class Model extends Observable {
	private String username;
	private Code code;
	private ArrayList<String> codeList;
	private ArrayList<String> saveList;
	private List<String> projectList;
	private Integer projectID;
	// private String projectDescription;
	private String projectCode;
	private int tabSize;
	private boolean randomMode;
	private int grade;
	private UserDBaccess userDBaccess;
	private AccessGroup accessGroup;

	public Model() {
		code = new Code();
		try {
			userDBaccess = new UserDBaccess();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.codeList = code.getCodeList();
		this.saveList = code.getSaveList();
		this.projectList = fetchProjects();
		
		// Default Werte werden gesetzt
		this.tabSize = 3;
		this.randomMode = false;
		this.grade = 0;
		projectCode = new String(
				" Zeile 1: Dies ist ein erstes Testprojekt \n \t \t Zeile 2: um zu sehen,\n \t Zeile 3: wie Java dies und die Zeilenumbrüche \n \t Zeile 4: darstellt");
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		if (grade < 14 && grade > 5) {
			this.grade = grade;
		} else {
			// TODO: Fehlerausgabe: Diese Jahrgangsstufe ist nicht klassifiziert
		}
	}

	public void setTabSize(int tabWidth) {
		this.tabSize = tabWidth;
	}

	public int getTabSize() {
		return tabSize;
	}

	public void setRandomMode(boolean random) {
		this.randomMode = random;
	}

	public boolean getRandomMode() {
		return randomMode;
	}

	public void setSaveList(ArrayList<String> listModelToSave) {
		saveList = listModelToSave;
	}

	private List<String> fetchProjects() {
		String[] projects = { "Project1", "Project2", "Project3" };
		List<String> projectList = new ArrayList<String>();
		for (String line : projects) {
			projectList.add(projectList.size(), line);
		}
		return projectList;
	}

	public void setPassword(char[] password) {
		// if (isPasswordCorrect(password)) {
		// }
		// else{}
		// //Zero out the possible password, for security.
		// Arrays.fill(password, '0');
	}

	/*
	 * TODO: in accessGroup auslagern
	 */
	public AccessGroup getAccessGroup(String username, char[] password) {

		if (userDBaccess.lookUpstudent(username, password)) {
			return AccessGroup.PUPIL;
		} else if (userDBaccess.lookUpteacher(username, password)) {
			return AccessGroup.TEACHER;
		} else
			return AccessGroup.UNKNOWN;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<String> getCodeModel() {
		return codeList;
	}

	public ArrayList<String> getSaveModel() {
		return saveList;
	}

	public List<String> getProjects() {
		return projectList;
	}

	public Integer getProjectID() {
		return projectID;
	}
	public String getProjectName(){
		if(projectID!=null){
			return projectList.get(projectID);
		}
		return "";
	}

	public void setProject(Integer projectID) {
		this.projectID = projectID;
	}

	public String getProjectDescription() {
		return projectList.get(projectID);
	}

	public String getProjectCode() {
		if(projectID!=null){
			return new String(projectCode);
		}
		return "";
	}

	public String[] getProjectCodeArray() {
		String[] parts = projectCode.split("\n");
		if (randomMode) {
			String buffer;
			for (int i = parts.length - 1; i > 0; i--) {
				int randomInt = new java.util.Random().nextInt(i);
				buffer = parts[randomInt];
				parts[randomInt] = parts[i];
				parts[i] = buffer;
			}
		}
		return parts;
	}

	// Speicherung eines Projekts
	public void setProjectCode(String codeString, String projectname,
			int linelength) {
		// TODO linelength maximum an puzzlestücke anpassen
		projectCode = new String(codeString);
		String[] projectArray = projectCode.split("\n");
		try {
			userDBaccess.saveProject(projectArray, projectname, linelength);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void setAccessGroup(AccessGroup accessGroup) {
		this.accessGroup=accessGroup;		
	}
	public AccessGroup getAccessGroup(){
		return accessGroup;
	}

}

