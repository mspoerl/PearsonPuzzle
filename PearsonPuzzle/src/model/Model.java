package model;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import org.junit.runner.notification.Failure;

/**
 * Klasse dient dazu, alle für die Benutzer notwendigen Daten einzulesen, zu
 * halten und zu speichern. Die Extension Observable implementiert die Methoden:
 * - add Observer (Observer o) - setChanged() - notifyObservers()
 * 
 * @author workspace
 * 
 */

public class Model extends Observable implements PropertyChangeListener{
	private String username;
	
	private CodeModel codeModel;
	
	private int grade;
	private Integer projectID;
	
	// private Vector<String> testExpressionsVector;
	// private Vector<Vector<Integer>> codeLine_GroupMatrix;
	private Vector<String> projectsVector;
	
	private boolean randomMode;
	private boolean resetDB;
	private UserDBaccess userDBaccess;
	private AccessGroup accessGroup;
	
	private LinkedList<Failure> jUnitFailures;

	public Model() {
		// Datenbankverbindung wird aufgebaut
		try {
			userDBaccess = new UserDBaccess();
			//userDBaccess.resetAll();
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
		jUnitFailures=new LinkedList<Failure>();
		randomMode = true;
		grade =0;
	}

	// ------------------------------------ Getter und Setter ------------------------------
	// --- Klassenstufe
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		if (grade < 14 && grade > 5) {
			this.grade = grade;
		} else {
			// TODO: Fehlerausgabe: Diese Jahrgangsstufe ist nicht klassifiziert
		}
		clearChanged();
	}
	
	// --- Tabbreite
	public int getTabSize() {
		if(projectID!=null)
			return codeModel.getTabSize();
		return 0;
	}
	public void setTabSize(int tabWidth) {
		codeModel.setTabSize(tabWidth);
		setChanged();
		notifyObservers();
		clearChanged();
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
	
	// -------- Code
	
	// --- Projekte
	public Vector<String> getProjectVector(){
		return projectsVector;
	}
		// -- Gewähltes Projekt
	public void selectProject(Integer projectID) {
		this.projectID = projectID;
		if(projectID!=null){
			this.fetchProjectCode();
			this.fetchProjectSettings();
		}
	}
		
	public Integer getProjectListID() {
		return projectID;
	}
			// - Projektname
	public String getProjectName(){
		if(projectID!=null)
			return projectsVector.get(projectID);
		return null;
	}
			// - Projektbeschreibung
	public void setProjectDescription(String descriptionString) {
		codeModel.setProjectDescription(descriptionString);
		setChanged();
	}
	public String getProjectDescription() {
		if(projectID!=null)
			return codeModel.getProjectDescription();
		return null;
	}
			// - Projektcode
	public String getProjectCode() {
		if(projectID!=null && accessGroup==AccessGroup.TEACHER)
			return codeModel.getProjectCode();
		return null;
	}
	public void setProjectCode(String codeString){
		codeModel.setProjectCode(codeString);
		setChanged();
	}
				// Projektvektor
	public Vector<String> getCodeVector() {
		return codeModel.getCodeVector();
	}
	
	// Projekt Sequenz Vektor
	public Vector<Vector<Integer>> getGroupMatrix() {
		return codeModel.getGroupMatrix();
	}
	public void setGroupMatrixEntry(int yPosition, int xPosition, Object value){
		String string = (String)value;
		string=string.trim();
		try{
		codeModel.getGroupMatrix().get(xPosition).set(yPosition, Integer.parseInt(string));
			}
			catch(NumberFormatException e){}
		setChanged();
		notifyObservers();
	}
	// Testrelevante Daten
	public void addTestGroup(){
		Vector<Integer> codeGroup=new Vector<Integer>();
		for (Iterator<String> iterator = codeModel.getCodeVector().iterator(); iterator.hasNext();) {
			iterator.next();
			codeGroup.add(new Integer(0));
		}
		codeModel.getGroupMatrix().add(codeGroup);
		setChanged();
		notifyObservers();
	}
	public void removeTestGroup(int index){
		if(index < codeModel.getGroupMatrix().size())
			codeModel.getGroupMatrix().remove(index);	
		setChanged();
		notifyObservers();
	}
	public Vector<String> getTestExpressionsVector() {
		return codeModel.getTestExpressionsVector();
	}
	public void setTestExpressionsVector(Vector<String> testVector) {
		System.out.println(testVector+"asd");
		codeModel.setTestExpressionsVector(testVector);
	}

	// --- Vom Schüler zusammengepuzzelter Code
	public Vector<String> getSolution() {
		return codeModel.getSolutionStrings();
	}
	public boolean isExactSollution() {
		return codeModel.isExactOrder();
	}
	
	public LinkedList<Failure> getjUnitFailures() {
		return jUnitFailures;
	}
	public void addjUnitFailure(Failure failure) {
		this.jUnitFailures.add(failure);
	}
	/**
     * Nutzt Reflection, um eine gewünschte Änderung des LösungsVectors an das CodeModel weiter zu leiten.
     */
	public void changeSolution(String propertyName, Integer changeIndex , String newValue){
		Method method;
		try {
			method = codeModel.getClass().getMethod(propertyName, new Class[]{ Integer.class,String.class});
			method.invoke(codeModel,changeIndex, newValue);
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();		
			} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

	// ------------------------------------ Datenbankinteraktionen----------------------------------------
	/**
	 * Model wird mit in Datenbank vorhandenen Werten gefüllt. <br>
	 * Ist Abhängig vom unter <b>projectID</b> gespeicherten Listeneintag. 
	 */
	public void fetchAll(){
		this.fetchProjects();
		this.fetchProjectCode();
		this.fetchProjectSettings();
		notifyObservers();
		clearChanged();
	}	
	
	/**
	 * Speichert das Projekt. Für den Fall, dass das Projekt umbenannt wurde, <br>
	 * wird projectName und der gewählte Eintrag ind projectList auf Ungleichheit geprüft.
	 * @param codeString Neuer Codeinhalt 
	 * @param projectName Neuer Projektname 
	 * @param linelength
	 */
	public boolean saveProject(String codeString, String projectName, String projectDescription,int linelength) {
		codeModel.setProjectCode(codeString);
		
		// ---- Prüfen, ob bereits ein gleichnamiges Projekt existiert 
		if(projectID==null 
				|| !projectName.equals(projectsVector.get(projectID))){
			if( userDBaccess.projectExists(projectName)){
				return false;
			}
		}
		
		// ---- Wenn der Projektname geändert wurde, Projektnamen updaten
		else if(!projectName.equals(projectsVector.get(projectID))){
			userDBaccess.renameProject(projectsVector.get(projectID), projectName);
		}
		
		// ----- Projekt speichern
		userDBaccess.saveProject(projectName, codeString, 0, 0);
		userDBaccess.updateDescription(projectName, projectDescription);
		
		// TODO: Test, ob erfolgreich gespeichert wurde
		this.fetchProjects();
		this.selectProject(projectsVector.indexOf(projectName));
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
		
		return true;
	}
	
	public void saveProjectSettings(){
		if(projectID!=null)
			userDBaccess.saveProjectSettings(projectsVector.get(projectID), codeModel.getTabSize(), grade);
	}

	/**
	 * Löscht das mit <b>projectID</b> selektierte Projekt <br>und löscht es aus der Datenbank <br>
	 * Löschen_erfolgreich@return
	 */
	public boolean removeProject() {
		if(userDBaccess.delete(projectsVector.get(projectID))){
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
		projectsVector = new Vector<String>();
		for (String line : projects) {
			projectsVector.add(line);
		}		
	}
	
	/**
	 * Holt Projektbeschreibung <br><u>des aktuell in der Liste selektieren Projekts </u> <br> aus der Datenbank
	 */
	private void fetchProjectSettings(){
		if(projectID!=null){
			try{
				codeModel.setProjectDescription(userDBaccess.getProjectDescription(projectsVector.get(projectID)));
				codeModel.setTabSize(userDBaccess.getTabSize(projectsVector.get(projectID)));
				}
			catch(SQLException e){
				codeModel.setProjectDescription("Noch keine Beschreibung vorhanden");
				}
		}
	}
	
	/**
	 * Holt Projekt-Code <br> <u>des aktuell in der Liste selektierten Projekts </u> <br> aus der Datenbank
	 */
	private void fetchProjectCode(){
		if(projectID!=null){
			try {
				codeModel = new CodeModel(projectsVector.get(projectID));
				codeModel.setProjectCode(userDBaccess.getCode(projectsVector.get(projectID)));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			this.setChanged();
			notifyObservers();
		}
}
