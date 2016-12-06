package model;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.*;

public class DBAccess {

	UserDBaccess db;
	// TODO: null testen
	private final static String normalString ="r2d2r2d2r2d2r2d";
	private final static String emptyString="";
	private final static String noString=null;
	private final static String tabString= "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
	private final static String newLines = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
	private final static String fancyString =getFancyString();
	private ArrayList <String> stringList;
		
	@Before
	public void initModel(){
		stringList = new ArrayList<String>();
		stringList.add(normalString);
		stringList.add(emptyString);
		stringList.add(tabString);
		stringList.add(newLines);
		stringList.add(fancyString);
		stringList.add(null);
		try {
			db = new UserDBaccess();
		} catch (SQLException e) {
			fail("Problem beim Erstellen oder Öffnen der Datenbank");
		}
	}
	
	@After
	public void clear(){
		for(String projectName : stringList){
			db.delete(projectName);
		}
		db.delete(fancyString.substring(0, UserDBaccess.getLength_ProjectName()-1));
	}
	
	private void saveProject(final String projectName, final String codeString, final int linelength){
		// TODO: Testfehler sauber definieren
		db.saveProject(projectName, codeString, linelength, 0);
		try {
			assertEquals(db.getCode(projectName),codeString);
		} catch (SQLException e) {
			fail("Problem nicht spezifiziert");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveProject_Test() {
		for(String projectName : stringList){
			for(String codeString : stringList){
				db.saveProject(projectName, codeString, 0, 0);
				System.out.println(projectName);
				try{
				if(projectName!= null && projectName.length()>UserDBaccess.getLength_ProjectName())
					if(codeString==null)
						assertNotEquals(null, db.getCode(fancyString.substring(0, UserDBaccess.getLength_ProjectName()-1)));
					else
						assertEquals(codeString, db.getCode(fancyString.substring(0, UserDBaccess.getLength_ProjectName()-1)));
				else if(codeString==null)
					assertNotEquals(null, db.getCode(projectName));
				else
					assertEquals(codeString, db.getCode(projectName));
					}
				catch(SQLException e){
					if(projectName==null)
						continue;
					if(projectName.equals(""))
						continue;
					fail("Problem beim vergleichen von "+codeString+"... in "+projectName+"...");
					e.printStackTrace();
					}
			}
		}	
	}
	/*
	@Test 
	public void multiThread(){
		try {
			UserDBaccess database = new UserDBaccess();
			database.connectionToDerby();
		} catch (SQLException e) {
			fail("Keine zwei Verbingunen möglich");
			e.printStackTrace();
		}
	}
	@Test
	public void test_Normal() {
		assertTrue(isEqual(normalString, "2", normalString,150));	
	}
	@Test
	public void test_Normal_noSplit() {
		assertTrue(isEqual(normalString, "", normalString,150));	
	}
	@Test
	public void test_Normal_Empty() {
		assertTrue(isEqual(normalString, "2", emptyString,150));
	}
	@Test
	public void test_Empty_Normal() {
		assertTrue(isEqual(emptyString,normalString,"2",150));
	}
	
	@Test//(expected=IllegalArgumentException.class)
	public void test_MIN() {
		assertTrue(isEqual(normalString,"2",normalString,Integer.MIN_VALUE));	
	}
	
	@Test//(expected=IllegalArgumentException.class)
	public void test_Empty_Empty_MIN() {
		assertTrue(isEqual(emptyString,"",emptyString,Integer.MIN_VALUE));	
	}
	
	@Test//(expected=IllegalArgumentException.class)
	public void test_MAX() {
		assertTrue(isEqual(normalString, "", normalString, Integer.MAX_VALUE));			
	}
	
	@Test//(expected=IllegalArgumentException.class)
	public void test_Tab_Tab() {
		assertTrue(isEqual(tabString, "", tabString, 150));
	}
	@Test//(expected=IllegalArgumentException.class)
	public void test_NewLine_NewLine() {
		assertTrue(isEqual(newLines, "", newLines, 150));
	}
	
	@Test
	public void test_Fancy() {
		assertTrue(isEqual(fancyString, "2", fancyString, 150));
	}
	@Test
	public void test_Normal_Fancy() {
		assertTrue(isEqual(normalString, "2", fancyString, 150));
	}
	@Test
	public void test_Fancy_Normal() {
		assertTrue(isEqual(fancyString, "", normalString, 150));
	}
	*/
	/**
	 * Methode, um Eingabe und Ausgabe der Datenbank auf Gleichheit zu prüfen.
	 * @param code
	 * @param split_String
	 * @param projectname
	 * @param linelength
	 * @return
	 */
	private boolean isEqual(final String code, final String split_String, final String projectname, final int linelength){
		try {
			db.saveProject(code.split(split_String),projectname , linelength, 0);
		} catch (SQLException e) {
			fail("Problem beim Speichern");
			e.printStackTrace();
			}
		try {
			String[] split=code.split(split_String);
			// FIXME muss geändert werden (getCodeList is depricated)
			ArrayList<String> dbResult = db.getCodeList(projectname);
			assertEquals(split.length, dbResult.size());
			for(int i=0; i<split.length; i++){
				assertEquals(split[i],dbResult.get(i));
			}
		} catch (SQLException e) {

			fail("Problem beim Holen der Daten");
			e.printStackTrace();
			}
		return true;
	}
	
	
	
	/**
	 * String mit allen möglichen ASCI Zeichen @return
	 */
	private static String getFancyString(){
		StringBuffer buf = new StringBuffer();
		for(int i=0; i<256;i++){
			buf.append(i);
		}
		return buf.toString();		
	}
	
	
	
	
}
