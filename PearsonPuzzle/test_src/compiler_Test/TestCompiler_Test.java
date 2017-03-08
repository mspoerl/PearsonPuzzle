package compiler_Test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import compiler.TestCompiler;

@RunWith(Parameterized.class)
/**
 * Test dient dazu, die Zerlegung der Strings in Klassen zu Testen, die vor dem Kompilieren geschieht. 
 * @author workspace
 *
 */
public class TestCompiler_Test {
    private Boolean expectedResult_Solo;
    private Boolean expectedResult_OnlineImport;
    private Boolean expectedResult_MethodeImport;
    private Boolean expectedResult_ClassImport;

    Boolean testResult;
    TestCompiler testCompiler;
    String testString;

    @Before
    public void initialize() {
	testCompiler = null;
    }

    public TestCompiler_Test(Boolean expRes_ClassImport,
	    Boolean expRes_MethodeImp, Boolean expRes_OnlineImp,
	    Boolean expRes, String testString) {
	this.testString = testString;
	this.expectedResult_ClassImport = expRes_ClassImport;
	this.expectedResult_MethodeImport = expRes_MethodeImp;
	this.expectedResult_OnlineImport = expRes_OnlineImp;
	this.expectedResult_Solo = expRes;

    }

    @Parameterized.Parameters
    public static Collection<Object[]> sourceCode() {
	return Arrays
		.asList(new Object[][] {

			// 0: EmptyString
			{ true, true, true, true, "" },
			// 1: normale Klasse
			{
				true,
				true,
				true,
				true,
				"" +
				// "package gen_src.testCode;" +
					"\npublic class first{ \npublic static void main(String args[]){\nSystem.out.println(\"alles klar\");}}" },
			// 2: Methode nicht definiert
			{ false, true, false, false,
				"public class first{ int a; public first(){ a=add(2,2);}}" },
			// 3: Berechtiguns-Test
			{
				false,
				false,
				false,
				false,
				"public class first{ public static void main(String args[]){int a=second.add(2,2);}}\n"
					+ "public class second{ private int add(int a, int b){return a+b;}}" },
			// 4: Anweisung außerhalb einer Methode (im
			// Klassenrumpf)
			{
				false,
				false,
				false,
				false,
				"public class first{ int a=0; int b=3; int c=a+b;\n System.out.println(\"Wer braucht schon Methoden\");}" },
			// 5: ohne Klasse und main
			{ true, true, true, true,
				"int a=0; int b=3; int c=a+b;" },
			// 6: selbst definierte Methode ohne Klasse
			{ true, true, true, true,
				"private void run(){int a=0; int b=3; int c=a+b;}" },

			// 7: zwei Methoden ohne Klasse
			{
				true,
				false,
				true,
				true,
				"public static void main(String args[]){add(2,2);}"
					+ "private static int add(int a, int b){return a+b;}" },
			// 8: zwei Methoden ohne Klasse + statische Methode
			// greift auf nicht statische Methode zu
			{
				false,
				false,
				false,
				false,
				"public static void main(String args[]){add(2,2);}"
					+ "private int add(int a, int b){return a+b;}" },

			// 9: Klassen import
			{
				true,
				false,
				false,
				false,
				"public class first{ \npublic first(){}}\n"
					+ "public class second{ \npublic second(){} private void test(){testClass tClass  = new testClass(); assert tClass.add(2,2)==4;}}" },

			// 10: zwei main Klassen,
			{
				true,
				true,
				true,
				true,
				"public class first{ \n"
					+ "public static void main(String args[]){System.out.println(\"first\");}}\n"
					+ "public class second{ \n"
					+ "public static void main(String args[])\n"
					+ "{System.out.println(\"second\");}}" },

			// 11: Import
			{
				true,
				true,
				true,
				true,
				"import java.util.Vector; \n"
					+ "public class first{ \n"
					+ "public void main(String args[]){\n"
					+ "getOrdervektor();}\n"
					+ "public void getOrdervektor(){\n"
					+ "Vector<Integer> ordervector = new Vector<Integer>();\n"
					+ "for(int ordernumber=0;ordernumber<4;ordernumber++){\n"
					+ "ordervector.add(ordernumber);		}\n"
					+ "System.out.println(ordervector.toString());}"
					+ "}" },
			// 12: Import test
			{
				false,
				false,
				true,
				false,
				"public class first{ \n"
					+ "public void main(String args[]){\n"
					+ "getOrdervektor();}\n"
					+ "public void getOrdervektor(){\n"
					+ "Vector<Integer> ordervector = new Vector<Integer>();\n"
					+ "for(int ordernumber=0;ordernumber<4;ordernumber++){\n"
					+ "ordervector.add(ordernumber);		}\n"
					+ "System.out.println(ordervector.toString());}"
					+ "}" },
			// 13: Nonsense
			{ false, false, false, false, "asd" },

			// XXX: Klasse in Klasse ermöglichen & Zwei Klassen in
			// einer Zeile ermöglichen
			// 14: Klasse in Klasse wird nicht erkannt. (Wird
			// aufgrund der Art der Zerlegung nicht unterstützt,
			// eigentlich valide)
			{
				false,
				null,
				null,
				false,
				"public class hihi{int a;int b;int c;public static int run(){\n"
					+ "//Kommentar\n"
					+ "System.out.println(\"asd\");\n"
					+ "return 1;}public class ohno{}}\n"
					+ "public class ohno2{} public class ohno3{}"
					+ "" },
			// 15: Mehrere aufeinanderfolgende Klassen können
			// kompiliert werden.
			{
				true,
				true,
				true,
				true,
				"public class hihi{\n"
					+ "int a;int b;int c;\n"
					+ "public static int run(){\n"
					+ "//Kommentar\n"
					+ "System.out.println(\"asd\");\n"
					+ "return 1;}\n}\n"
					+ "public class Ohno{ public int getInt(){ return 2;} }\n"
					+ "public class TeClass{ private Ohno ohno = new Ohno(); int a=ohno.getInt(); }\n" } });
    }

    @Test
    public void Klassen_Solo() {
	testCompiler = new TestCompiler(testString, null, null, null);
	testCompiler.compile();
	testResult = testCompiler.getFailures().size() == 0;
	assertEquals(expectedResult_Solo, testResult);
    }

    @Test
    public void Klassen_mit_OnlineImport() {
	if (expectedResult_OnlineImport != null) {
	    testCompiler = new TestCompiler(testString, null,
		    "import java.util.Vector;", null);
	    testCompiler.compile();
	    testResult = testCompiler.getFailures().size() == 0;
	    assertEquals(expectedResult_OnlineImport, testResult);
	}
    }

    @Test
    public void Klassen_mit_MethodenImport() {
	if (expectedResult_MethodeImport != null) {
	    testCompiler = new TestCompiler(testString,
		    "\n public static int add(int a, int b){return a+b;}\n",
		    null, null);
	    testCompiler.compile();
	    testResult = testCompiler.getFailures().isEmpty();
	    assertEquals(expectedResult_MethodeImport, testResult);
	}
    }

    @Test
    public void Klassen_mit_KlassenImport() {
	testCompiler = new TestCompiler(
		testString,
		null,
		null,
		"public class testClass{ public testClass(){} public int add(int a, int b){return a+b;}}");
	testCompiler.compile();
	testResult = testCompiler.getFailures().isEmpty();
	assertEquals(expectedResult_ClassImport, testResult);
    }

    @After
    public void print() {
	if (testCompiler != null
		&& (false != testResult != testCompiler.getFailures().isEmpty())) {
	    System.out.println(testCompiler.getFailures().get(0).get("Art")
		    + testCompiler.getFailures().get(0).get("Nachricht"));
	    System.out.println(testCompiler.getFailures().get(0).get("Class"));
	}
	testCompiler = null;
    }
}
