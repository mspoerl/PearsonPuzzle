/**
 * 
 */
package model_Test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import model.Model;
import model.access.AccessGroup;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Export und Import Funktion wird getestet. Vor dem Test werden Werte in die Datenbank geschrieben, die Datenbank exportiert und wieder importiert.
 * Die Tests prüfen nun, ob die beiden Model in den enstpechenden Werten übereinstimmen, sowie, ob der im Model gespeicherte Wert dem ursprünglich übergebenen entspricht.
 * 
 * Wird wie in initiailze_Test(); kommentiert die Zeile *exportImportData();* durch *secondModel = model;* ersetzt, kann der Test genutzt werden, um zu prüfen, 
 * ob im Model die richtigen Werte gesetzt wurden. <br>Dabei ist allerdings zu beachten:
 * <ul><li> Dass der sollution-Vecotr nicht geprüft wird. (Da er bei jedem model.fetchAll(); neu initialisiert wird.</li>
 * <li>Vectoren in der Regel nicht auf Korrektheit, sonder nur auf "nicht Null" und "nicht Leer" geprüft werden. (Einzige Ausnahme stellt im Moment der User-Vector dar.)</li>
 * </ul> 
 * 
 * 
 * @author workspace
 *
 */
public class dbTransaction_ExportTest {

	private final static String location = "gen_rsc/database";
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
		if(!model.saveProject(projectCode, projectName, projectDescription, 150))
			fail("es Existiert bereits ein Projekt mit Name \"projektName\"");
		
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
			fail("no Data");
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
	}
	
	@Test
	public void testMainParameters(){
		assertEquals(model.getCodeVector(true),secondModel.getCodeVector(true));
		assertEquals(model.getCodeVector(false),secondModel.getCodeVector(false));
		assertNotNull(secondModel.getCodeVector(true));
		assertNotNull(secondModel.getCodeVector(false));
		assertNotEquals(secondModel.getCodeVector(true), new Vector<String>());
		assertNotEquals(secondModel.getCodeVector(false), new Vector<String>());
		
		assertEquals(model.getProjectName(), secondModel.getProjectName());
		assertEquals(projectName, secondModel.getProjectName());
		assertEquals(model.getProjectCode(), secondModel.getProjectCode());
		assertEquals(projectCode, secondModel.getProjectCode().trim());
	}
	
	@Test 
	public void testSettings(){	
		//assertEquals(model.getGrade(), secondModel.getGrade()); // nicht vollständig implementiert
		
		assertEquals(model.getProjectDescription(), secondModel.getProjectDescription());
		assertEquals(projectDescription, secondModel.getProjectDescription());
		
		assertEquals(model.getTabSize(), secondModel.getTabSize());		
		assertEquals(Integer.parseInt(tabSize_String), secondModel.getTabSize());
		
		assertEquals(model.getProjectVector(), secondModel.getProjectVector());
		assertNotNull(secondModel.getProjectVector());
		
		// Die Sollution vecotren sind in der Regel beim Laden aus der Datenbank leer, 
		// da noch kein DnD stattgefunden hat.
		assertEquals(model.getSollution(), secondModel.getSollution());
		assertNotNull(secondModel.getSollution());
		assertEquals(model.getSollutionOrder(), secondModel.getSollutionOrder());
		assertEquals(model.getSolutionStrings(), secondModel.getSolutionStrings());
		assertNotNull(secondModel.getSolutionStrings());
		
		assertEquals(model.getTestExpressionsVector(), secondModel.getTestExpressionsVector());
		assertNotNull(secondModel.getTestExpressionsVector());
		
		assertEquals(puzzlemode, secondModel.getPuzzlemode());
	}
	
	@Test
	public void testUnitCode(){
		assertEquals(model.getJUnitCode(), secondModel.getJUnitCode());
		assertEquals(jUnitCode, secondModel.getJUnitCode());
	}
	@Test
	public void testImports(){
		assertEquals(model.getImport("online"), secondModel.getImport("online"));
		assertEquals(onlineImport, secondModel.getImport("online"));
		assertEquals(model.getImport("classes"), secondModel.getImport("classes"));
		assertEquals(classImport, secondModel.getImport("classes"));
		assertEquals(model.getImport("methods"), secondModel.getImport("methods"));
		assertEquals(methodImport, secondModel.getImport("methods"));
	}
	
	@Test
	public void testOrderSettings(){
		assertEquals(model.getGroupMatrix(), secondModel.getGroupMatrix());
	}
	
	@Test
	public void testOrderFailures(){
		assertEquals(model.getOrderFailureText(0), secondModel.getOrderFailureText(0));
		assertEquals(failureText, secondModel.getOrderFailureText(0));
	}
	
	@Test
	public void testUserGroups(){
		Boolean allNull = true;
		for(AccessGroup acGroup : AccessGroup.values()){
			assertEquals(userGroup.get(acGroup), secondModel.getUsers(acGroup));
			if(userGroup.get(acGroup)!=null || secondModel.getUsers(acGroup)!=null)
				allNull = false;
		}
		assertFalse(allNull);
	}
	
	@After
	public void delelteProject(){
		secondModel.removeProject();
		secondModel.setDatabase(null);
	}

}
