package Model_Test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import model.UserDBaccess;

public class DBAccess {

	UserDBaccess db;
	private final static String normalString ="r2d2r2d2r2d2r2d";
	private final static String emptyString="";
	private final static String tabString= "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
	private final static String newLines = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n";
	private final static String fancyString =getFancyString();
		
	@Before
	public void initModel(){
		try {
			db = new UserDBaccess();
		} catch (SQLException e) {
			fail("Problem beim erstellen oder öffnen der Datenbank");
		}
	}
	
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
		assertTrue(isEqual(tabString, "\t", tabString, 150));
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
			db.saveProject(code.split(split_String),projectname , linelength);
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Problem beim Speichern");
			}
		
		try {
			String[] split=code.split(split_String);
			ArrayList<String> dbResult = db.getProject(projectname);
			assertEquals(split.length, dbResult.size());
			for(int i=0; i<split.length; i++){
				assertEquals(split[i],dbResult.get(i));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Problem beim Holen der Daten");
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
