package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

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
	
	private ArrayList<String> codeList;
	private ArrayList<String> saveList;
	private Vector<String> codeVector;
	private Vector<Integer> sequenceVector;
	private String projectDescription;
	private String projectCode;
	
	private List<String> projectList;
	private Vector<String> projectVector;
	private Integer projectID;
	private int tabSize;
	private boolean randomMode;
	private boolean resetDB;
	private int grade;
	private UserDBaccess userDBaccess;
	private AccessGroup accessGroup;
	

	public Model() {
		// Datenbankverbindung wird aufgebaut
		try {
			userDBaccess = new UserDBaccess();
		} catch (SQLException e) {
			if(e.getSQLState().equals("XJ040"))
				// TODO Ausgabe, dass bereits eine Programminstanz gestartet wurde
				System.out.println("Nichts wie raus hier!!!!\n");
			else
				e.printStackTrace();
			}
		
		// Holt Daten aus der Datenbank
		this.fetchAll();
		
		// Default Werte werden gesetzt
		this.tabSize = 3;
		this.randomMode = true;
		this.grade = 0;
	}

	// ------------------------------------ Getter und Setter ------------------------------
	// --- Klassenstufe
	public void setGrade(int grade) {
		if (grade < 14 && grade > 5) {
			this.grade = grade;
		} else {
			// TODO: Fehlerausgabe: Diese Jahrgangsstufe ist nicht klassifiziert
		}
	}
	public int getGrade() {
		return grade;
	}

	
	// --- Tabbreite
	public void setTabSize(int tabWidth) {
		this.tabSize = tabWidth;
	}
	public int getTabSize() {
		return tabSize;
	}

	// --- Zufallsmodus
	public void setRandomMode(boolean random) {
		this.randomMode = random;
	}
	public boolean getRandomMode() {
		return randomMode;
	}

	// --- Zugriffsgruppe
	// TODO: in accessGroup auslagern
	public void setAccessGroup(AccessGroup accessGroup) {
		this.accessGroup=accessGroup;		
	}
	public AccessGroup getAccessGroup(String username, char[] password) {
		if (userDBaccess.lookUpstudent(username, password)) {
			return AccessGroup.PUPIL;
		} else if (userDBaccess.lookUpteacher(username, password)) {
			return AccessGroup.TEACHER;
		} else
			return AccessGroup.UNKNOWN;
	}
	public AccessGroup getAccessGroup(){
		return accessGroup;
	}
	
	// --- Nutzername
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUsername() {
		return username;
	}
	
	// --- Code
	public ArrayList<String> getCodeList() {
		return codeList;
	}

	// --- Projekte
	public Vector<String> getProjectVector(){
		return projectVector;
	}
		// -- Gewähltes Projekt
	public void selectProject(Integer projectID) {
		this.projectID = projectID;
		this.fetchProjectCode();
		this.fetchProjectDescription();
	}
	public Integer getProjectListID() {
		return projectID;
	}
			// - Projektname
	public String getProjectName(){
		if(projectID!=null){
			return projectList.get(projectID);
		}
		return new String();
	}
			// - Projektbeschreibung
	public void setProjectDescription(String descriptionString) {
		this.projectDescription=descriptionString;
		setChanged();
	}
	public String getProjectDescription() {
		if(projectID!=null){
		return projectDescription;
		}
		else return null;
	}
			// - Projektcode
	public String getProjectCode() {
		if(projectID!=null){
			return projectCode;
		}
		else return new String();
	}
	public void setProjectCode(String codeString){
		projectCode=codeString;
		setChanged();
	}
				// Projektvektor
	public Vector<String> getCodeVector() {
		return codeVector;
	}
	public void setCodeVector(Vector<String> codeVector) {
		this.codeVector = codeVector;
	}
				// Projekt Sequenz Vektor
	public Vector<Integer> getSequenceVector() {
		return sequenceVector;
	}
	public void setSequenceVector(Vector<Integer> sequenceVector) {
		this.sequenceVector = sequenceVector;
	}

	// Code zum puzzeln
	private String[] getRandomCode() {
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
	
	// --- Vom Schüler zusammengepuzzelter Code
	public ArrayList<String> getSaveList() {
		return saveList;
	}
	public void setSaveList(ArrayList<String> listModelToSave) {
		saveList = listModelToSave;
	}
	
	// --- Datenbank zurücksetzen
	public boolean isResetDB() {
		return resetDB;
	}
	public void setResetDB(boolean resetDB) {
		this.resetDB = resetDB;
		setChanged();
		notifyObservers();
	}

	// ------------------------------------ Datenbankinteraktionen----------------------------------------
	/**
	 * Model wird mit in Datenbank vorhandenen Werten gefüllt. <br>
	 * Ist Abhängig vom unter <b>projectID</b> gespeicherten Listeneintag. 
	 */
	public void fetchAll(){
		this.fetchProjects();
		this.fetchProjectCode();
		this.fetchProjectDescription();
		notifyObservers();
		clearChanged();
	}	
	
	/**
	 * Speichert das Projekt. Für den Fall, dass das Projekt umbenannt wurde, <br>
	 * wird projectName und der gewählte Eintrag ind projectList auf Ungleichheit geprüft.
	 * Neuer Codeinhalt @param codeString
	 * Neuer Projektname @param projectName
	 * @param linelength
	 */
	public boolean saveProject(String codeString, String projectName, String projectDescription,int linelength) {
		projectCode = new String(codeString);
		
		// ---- Prüfen, ob bereits ein gleichnamiges Projekt existiert 
		if(projectID==null 
				|| !projectName.equals(projectList.get(projectID))){
			if( userDBaccess.projectExists(projectName)){
				return false;
			}
		}
		
		// ---- Wenn der Projektname geändert wurde, Projektnamen updaten
		if(!projectName.equals(projectList.get(projectID))){
			userDBaccess.renameProject(projectList.get(projectID), projectName);
		}
		
		// ----- Projekt speichern
		userDBaccess.saveProject(projectName, codeString, 0, 0);
		userDBaccess.updateDescription(projectName, projectDescription);
		
		// TODO: Test, ob erfolgreich gespeichert wurde
		
		this.fetchProjects();
		this.fetchProjectCode();
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
		
		return true;
	}

	/**
	 * Löscht das mit <b>projectID</b> selektierte Projekt <br>und löscht es aus der Datenbank <br>
	 * Löschen_erfolgreich@return
	 */
	public boolean removeProject() {
		if(userDBaccess.delete(projectList.get(projectID))){
			this.selectProject(null);
			this.fetchAll();
			return true;
		}
		return false;
	}
	
	/**
	 * Speichert die neue Konfiguration
	 */
	public void updateConfig() {
		if(isResetDB()){
			try {
				userDBaccess.resetAll();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// ----- private Methoden zur Datenbankinteraktion
	/**
	 * Holt Liste mit Projekten aus Datenbank
	 */
	private void fetchProjects() {
		List <String> projects;
		projects = userDBaccess.getProjects(grade);
		List<String> projectList = new ArrayList<String>();
		projectVector = new Vector<String>();
		for (String line : projects) {
			projectList.add(projectList.size(), line);
			projectVector.add(line);
		}		
		this.projectList=projectList;
	}
	
	/**
	 * Holt Projektbeschreibung <br><u>des aktuell in der Liste selektieren Projekts </u> <br> aus der Datenbank
	 */
	private void fetchProjectDescription(){
		if(projectID!=null){
			try{
				this.projectDescription = userDBaccess.getProjectDescription(projectList.get(projectID));
				}
			catch(SQLException e){
				this.projectDescription="Noch keine Beschreibung vorhanden";
				}
		}
		else{
			this.projectDescription = new String();
		}
	}
	
	/**
	 * Holt Projekt-Code <br> <u>des aktuell in der Liste selektierten Projekts </u> <br> aus der Datenbank
	 */
	private void fetchProjectCode(){
		if(projectID!=null){
			if(projectID!=null){
			try {
				this.projectCode = userDBaccess.getCode(projectList.get(projectID));
				String[] stringField = this.getRandomCode();
				this.codeList = new ArrayList<String>();
				this.saveList = new ArrayList<String>();
				this.codeVector = new Vector<String>();
				this.sequenceVector = new Vector<Integer>();
				for(String line: stringField){
					codeList.add(line);
					saveList.add(new String());
					codeVector.add(line);
					sequenceVector.add(0);
				}		
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			else{
			}
		}
	}
}


/*
public class Model extends Observable {
	private String username;
	private ArrayList<String> codeList;
	private ArrayList<String> saveList;
	private List<String> projectList;
	private Integer projectListID;
	// private String projectDescription;
	private String projectCode;
	private int tabSize;
	private boolean randomMode;
	private int grade;
	private UserDBaccess userDBaccess;
	private AccessGroup accessGroup;

	public Model() {
		try {
			userDBaccess = new UserDBaccess();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setup();		
		// Default Werte werden gesetzt
		this.tabSize = 3;
		this.randomMode = false;
		this.grade = 0;
			}
	private void setup(){
		this.codeList= fetchProject();
		this.saveList= new ArrayList<String>();
		for (Iterator<String> iterator = codeList.iterator(); iterator.hasNext();) {
			saveList.add(new String());
		}
		this.projectList = fetchProjects();
		
		this.projectCode = new String(
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

	private ArrayList<String> fetchProjects() {
		String[] projects = { "Project1", "Project2", "Project3" };
		ArrayList<String> projectList = new ArrayList<String>();
		for (String line : projects) {
			projectList.add(projectList.size(), line);
		}
		return projectList;
	}
	
	private ArrayList<String> fetchProject(){
		//try {
		//	return userDBaccess.getProject("DefaultProjekt");
		//} catch (SQLException e) {
			// TODO Auto-generated catch block
			ArrayList<String> failure= new ArrayList<String>();
			failure.add("Probleme beim Laden des Projekts.");
			failure.add("Bitte nehmen Sie kontakt zum Systemadministrator auf.");
			return failure;
		//}
	}

	public void setPassword(char[] password) {
		// if (isPasswordCorrect(password)) {
		// }
		// else{}
		// //Zero out the possible password, for security.
		// Arrays.fill(password, '0');
	}

	// TODO: in accessGroup auslagern
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

	public Integer getProjectListID() {
		return projectListID;
	}
	public String getProjectName(){
		if(projectListID!=null){
			return projectList.get(projectListID);
		}
		return "";
	}

	public void setProject(Integer projectID) {
		this.projectListID = projectID;
	}

	public String getProjectDescription() {
		// TODO: Projektbeschreibung ergänzen
		if(projectListID!=null)
		{
			return projectList.get(projectListID);
		}
		return "Leider keine Projektbeschreibung voranden";
	}

	public String getProjectCode() {
		if(projectListID!=null){
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
			userDBaccess.saveProject(projectArray, projectname, linelength, 0);
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
*/
