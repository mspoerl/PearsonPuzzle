package model_Test;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import model.Model;
import model.access.AccessGroup;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/**
 * Export und Import Funktion wird getestet. Vor dem Test werden Werte in die Datenbank geschrieben, die Datenbank exportiert und wieder importiert.
 * Die Tests prüfen nun, ob die beiden Model in den enstpechenden Werten übereinstimmen, sowie, ob der im Model gespeicherte Wert dem ursprünglich übergebenen entspricht.
 * 
 * Wird wie in initiailze_Test(); kommentiert die Zeile *exportImportData();* durch *secondModel = model;* ersetzt, kann der Test genutzt werden, um zu prüfen, 
 * ob im Model die richtigen Werte gesetzt wurden. <br>Dabei ist allerdings zu beachten:
 * <ul><li> Dass der sollution-Vector nicht geprüft wird. (Da er bei jedem model.fetchAll(); neu initialisiert wird.</li>
 * <li>Vectoren in der Regel nicht auf Korrektheit, sonder nur auf "nicht Null" und "nicht Leer" geprüft werden. (Einzige Ausnahme stellt im Moment der User-Vector dar.)</li>
 * </ul> 
 * 
 * @author workspace
 *
 */
public class DbTransaction_Export_Test {

	private final static String location = "gen_rsc"+File.separator+"database";
	private Model model;
	private Model secondModel;
	private Integer projectID;
	
	// main Parameter
	private String projectCode = "projectCode";
	private String projectName = "projectName";
	private String projectDescription = "projectDescription";
	
	// !!! nicht implementiert
	private int grade = 6;
	
	// !!! Wird im Moment nicht geprüft werden (da bei model.fetchAll() wieder neu initialisiert wird)
	private LinkedList<Integer> sollution = new LinkedList<Integer>();
	
	// Unit Code und Imports
	private String jUnitCode = "jUnitCode";
	private String methodImport = "methodImport";
	private String classImport = "classImport";
	private String onlineImport = "onlineImport";
	
	// Project Settings & Puzzle Mode
	private String tabSize_String = "3";
	private Integer puzzlemode = 0;
	
	// Order Settings und Order Failures
	private Object groupMatrixValue = "1";
	private String failureText = "failureText";
	
	// User Group
	private HashMap<AccessGroup, Vector<String>> userGroup;

	@Before
	public void initiailze_Test(){
		model = new Model();
		model.fetchAll();
		
		// Test werden bei Bedarf abgebrochen
		Assume.assumeFalse("Datensatz nicht initialisiert (Es ist kein Teacher in der Datenbank hinterlegt.) Bitte Legen Sie erst einen TEACHER an.", model.getUsers(AccessGroup.TEACHER).isEmpty());
		Assume.assumeTrue("Es Existiert bereits ein Projekt mit Name \"projektName\"",model.saveProject(projectCode, projectName, projectDescription, 150));
		
		model.setGrade(grade);
		model.setJUnitCode(jUnitCode );
		model.setImports("methods", methodImport );
		model.setImports("classes", classImport);
		model.setImports("online", onlineImport);
		model.setTabSize(tabSize_String);
		model.saveProjectSettings();
		
		model.savePuzzlemode(puzzlemode);
		
		sollution.add(0);
		model.setSollutionVector(sollution);
		model.saveRandomisation();	
		
		model.addTestGroup();
		model.setGroupMatrixEntry(0, 0, groupMatrixValue);
		model.saveGroupMatrix();
		
		model.setOrderFailures(0, failureText);
		model.saveOrderFailures();
		
		projectID = model.getProjectListID();
		
		userGroup = new HashMap<AccessGroup, Vector<String>>();
		for(AccessGroup acGroup : AccessGroup.values()){
			userGroup.put(acGroup, model.getUsers(acGroup));
		}
		
		if(model.getProjectVector()== null || model.getProjectVector().isEmpty()){
			fail("Es wurden keine Projekte in der Datenbank gefunden.");
		}
		
		// Falls der Test nur prüfen soll, dass die Werte im Model richtig gesetzt wurden,
		// kann man die folgende Zeile auskommentieren und stattdessen *secondModel = model;* ausführen. 
		exportAndImportData();
		// secondModel = model;
	}
	private void exportAndImportData() {
		model.exportDatabase(location);
		model.setDatabase(null);
		File file = new File(location);
		secondModel = new Model();
		secondModel.fetchAll();
		secondModel.replaceDatabase("exportdatei.zip", file.getAbsolutePath());
		secondModel.selectProject(projectID);
		file.delete();
	}
	
	@Test
	public void testMainParameters(){
		assertEquals("getCodeVector(true) liefert unterschiedliche Ergebnisse", model.getCodeVector(true),secondModel.getCodeVector(true));
		assertEquals("getCodeVector(false) liefert unterschiedliche Ergebnisse", model.getCodeVector(false),secondModel.getCodeVector(false));
		assertNotNull("getCodeVector(true) ist NULL", secondModel.getCodeVector(true));
		assertNotNull("getCodeVector(false) ist NULL", secondModel.getCodeVector(false));
		assertNotEquals("getCodeVEctor(true) ist leer",secondModel.getCodeVector(true), new Vector<String>());
		assertNotEquals("getCodeVector(false) ist leer",secondModel.getCodeVector(false), new Vector<String>());
		
		assertEquals("Problem bei Export&Import", model.getProjectName(), secondModel.getProjectName());
		assertEquals("Problem beim Speichern der Werte", projectName, secondModel.getProjectName());
		assertEquals("Problem bei Export&Import", model.getProjectCode(), secondModel.getProjectCode());
		assertEquals("Problem beim Speichern der Werte", projectCode, secondModel.getProjectCode().trim());
	}
	
	@Test 
	public void testSettings(){	
		//assertEquals(model.getGrade(), secondModel.getGrade()); // nicht vollständig implementiert
		
		assertEquals("Problem bei Export&Import",model.getProjectDescription(), secondModel.getProjectDescription());
		assertEquals("Problem beim Speichern der Werte", projectDescription, secondModel.getProjectDescription());
		
		assertEquals("Problem bei Export&Import",model.getTabSize(), secondModel.getTabSize());		
		assertEquals("Problem beim Speichern der Werte", Integer.parseInt(tabSize_String), secondModel.getTabSize());
		
		assertEquals("Problem bei Export&Import",model.getProjectVector(), secondModel.getProjectVector());
		assertNotNull("Problem beim Speichern der Werte: getProjectVector() = NULL", secondModel.getProjectVector());
		
		// Die Sollution vecotren sind in der Regel beim Laden aus der Datenbank leer, 
		// da noch kein DnD stattgefunden hat.
		assertEquals("Problem bei Export&Import",model.getSollution(), secondModel.getSollution());
		assertNotNull("Problem beim Speichern der Werte: getSollution() = NULL", secondModel.getSollution());
		assertEquals("Problem bei Export&Import",model.getSollutionOrder(), secondModel.getSollutionOrder());
		assertEquals("Problem bei Export&Import",model.getSolutionStrings(), secondModel.getSolutionStrings());
		assertNotNull("Problem beim Speichern der Werte: getSolutionStrings() = NULL", secondModel.getSolutionStrings());
		
		assertEquals("Problem bei Export&Import",model.getTestExpressionsVector(), secondModel.getTestExpressionsVector());
		assertNotNull("Problem beim Speichern der Werte: getTestExpressionVector() = NULL", secondModel.getTestExpressionsVector());
		
		// Puzzlemode wird direkt aus der Datenbank gezogen (Es existiert keine Instanz im Moedel)
		assertEquals("Problem beim Export&Import", puzzlemode, secondModel.getPuzzlemode());
	}
	
	@Test
	public void testUnitCode(){
		assertEquals("Problem bei Export&Import: getJUnitCode() liefert unterschiedliche Ergebnisse", model.getJUnitCode(), secondModel.getJUnitCode());
		assertEquals("Problem beim Speichern: getJUnitCode() liefert nicht den gesetzten Wert", jUnitCode, secondModel.getJUnitCode());
	}
	@Test
	public void testImports(){
		assertEquals("Problem bei Export&Import", model.getImport("online"), secondModel.getImport("online"));
		assertEquals("Problem beim Speichern der Werte", onlineImport, secondModel.getImport("online"));
		assertEquals("Problem bei Export&Import",model.getImport("classes"), secondModel.getImport("classes"));
		assertEquals("Problem beim Speichern der Werte", classImport, secondModel.getImport("classes"));
		assertEquals("Problem bei Export&Import",model.getImport("methods"), secondModel.getImport("methods"));
		assertEquals("Problem beim Speichern der Werte", methodImport, secondModel.getImport("methods"));
	}
	
	@Test
	public void testOrderSettings(){
		assertEquals("Problem bei Export&Import oder Speichervorgang", model.getGroupMatrix(), secondModel.getGroupMatrix());
	}
	
	@Test
	public void testOrderFailures(){
		assertEquals("Problem bei Export&Import",model.getOrderFailureText(0), secondModel.getOrderFailureText(0));
		assertEquals("Problem beim Speichern der Werte", failureText, secondModel.getOrderFailureText(0));
	}
	
	@Test
	public void testUserGroups(){
		Boolean allNull = true;
		for(AccessGroup acGroup : AccessGroup.values()){
			assertEquals("Problem bei Export&Import", userGroup.get(acGroup), secondModel.getUsers(acGroup));
			if(userGroup.get(acGroup)!=null || secondModel.getUsers(acGroup)!=null)
				allNull = false;
		}
		assertFalse("Es konnte kein Nutzer in der Datenbank gefunden werden", allNull);
	}
	
	@After
	public void delelteProject(){
		secondModel.removeProject();
		secondModel.setDatabase(null);
	}
}
