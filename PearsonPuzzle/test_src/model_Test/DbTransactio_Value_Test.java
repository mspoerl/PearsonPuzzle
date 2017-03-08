package model_Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import model.Model;
import model.access.AccessGroup;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DbTransactio_Value_Test {
  
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
		
		model.setDatabase(null);
		
		secondModel = new Model();
		secondModel.fetchAll();
		secondModel.selectProject(projectID);
	}
    	public DbTransactio_Value_Test(Object string, Object integer){
    	    grade=(Integer) integer;
    	    jUnitCode=(String)string;
    	    methodImport=(String)string;
    	    classImport=(String)string;
    	    onlineImport=(String)string;
    	    tabSize_String=integer.toString();
    	    puzzlemode=(Integer)integer;
    	    sollution.add((Integer)integer);
    	    groupMatrixValue=(String)string;
    	    failureText=(String)string;
    	}
    @Parameterized.Parameters
	   public static Collection<Object[]> primeNumbers() {
	      return Arrays.asList(new Object[][]{
		      {"r2d2r2d2r2d2r2d", 99999999},
		      {"", 1},
		      {"     ", -5},
		      {"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t", 2},
		      {"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", 18},
		      {"äöü!(){}", 0}, 
		     {"0",0}
	      });
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
		if(Integer.parseInt(tabSize_String)<=10){
		    assertEquals("Problem beim Speichern der Werte", Integer.parseInt(tabSize_String), secondModel.getTabSize());
		}
		
		assertEquals("Problem bei Export&Import",model.getProjectVector(), secondModel.getProjectVector());
		assertNotNull("Problem beim Speichern der Werte: getProjectVector() = NULL", secondModel.getProjectVector());
		
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
