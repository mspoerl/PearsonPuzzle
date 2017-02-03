package compiler_Test;

import static org.junit.Assert.*;

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
	Boolean expectedResult_Solo;
	Boolean expectedResult_Import;
	String testString;
	TestCompiler testCompiler;
	
	@Before
	   public void initialize() {
			testCompiler = new TestCompiler(testString, null, null, null);
	   }
		
	   public TestCompiler_Test(Boolean expectedResult_Import, Boolean expectedResult,String testString) {
	      this.testString = testString;
	      this.expectedResult_Solo = expectedResult;
	      this.expectedResult_Import = expectedResult_Import;
	   }

	   @Parameterized.Parameters
	   public static Collection<Object[]> sourceCode() {
	      return Arrays.asList(new Object[][] {
	    		// EmptyString
	 	     {true, ""},
	    		// normale Klasse
	         {true, "" +
	         		//"package gen_src.testCode;" +
	         		"\npublic class first{ \npublic static void main(String args[]){\nSystem.out.println(\"alles klar\");}}"},	// 	
	         	// Berechtiguns-Test
	         {false, "public class first{ public static void main(String args[]){add(2,2);}}\n"+
	         		"public class second{ private int add(int a, int b){return a+b;}}"}, 
	         	// Anweisung außerhalb einer Methode (im Klassenrumpf)
	         {false, "public class first{ int a=0; int b=3; int c=a+b;\n System.out.println(\"Wer braucht schon Methoden\");}"},
	         	// ohne Klasse und main
	         {true, "int a=0; int b=3; int c=a+b;"},
	         	// selbst definierte Methode ohne Klasse
	         {true, "private void run(){int a=0; int b=3; int c=a+b;}"},
	         
	         	// zwei Methoden ohne Klasse 
	         {true, "public static void main(String args[]){add(2,2);}"+	
     		 		"private static int add(int a, int b){return a+b;}"},
	         	// zwei Methoden ohne Klasse + statische Methode greift auf nicht statische Methode zu 
	         {false,"public static void main(String args[]){add(2,2);}"+	
	         		"private int add(int a, int b){return a+b;}"},
	         	
	         	// zwei Klassen
	         {true, "public class first{ \npublic first(){}}\n"+
	        		"public class second{ \npublic second(){}}"},
        		// zwei main Klassen,	
   	         {true, "public class first{ \n" +
   	         		"public static void main(String args[]){System.out.println(\"first\");}}\n"+
   	        		"public class second{ \n" +
   	        		"public static void main(String args[])\n" +
   	        		"{System.out.println(\"second\");}}"},
   	        		
	        	// import Test
	         {true, "import java.util.Vector; \n" +
	        		"public class first{ \n" +
	        		"public void main(String args[]){\n" +
	        		"getOrdervektor();}\n" +
	        		"public void getOrdervektor(){\n" +
	        		"Vector<Integer> ordervector = new Vector<Integer>();\n" +
	        		"for(int ordernumber=0;ordernumber<4;ordernumber++){\n" +
	        		"ordervector.add(ordernumber);		}\n" +
	        		"System.out.println(ordervector.toString());}" +
	        		"}"},
	        	// ohne import
    		 {false,"public class first{ \n" +
 	        		"public void main(String args[]){\n" +
 	        		"getOrdervektor();}\n" +
 	        		"public void getOrdervektor(){\n" +
 	        		"Vector<Integer> ordervector = new Vector<Integer>();\n" +
 	        		"for(int ordernumber=0;ordernumber<4;ordernumber++){\n" +
 	        		"ordervector.add(ordernumber);		}\n" +
 	        		"System.out.println(ordervector.toString());}" +
 	        		"}"},
 	        		
	        	// Nonsense 
	        {false, "asd"},
	        	// XXX: Klasse in Klasse ermöglichen.
	        	// Klasse in Klasse wird nicht erkannt. (Wird aufgrund der Art der Zerlegung nicht unterstützt, eigentlich valide)
	        {false, "public class hihi{int a;int b;int c;public static int run(){\n" +
	        		"//Kommentar\n" +
	        		"System.out.println(\"asd\");\n" +
	        		"return 1;}public class ohno{}}\n"},
	        	// Zwei aufeinanderfolgende Klassen können kompiliert werden.
	        {true, "public class hihi{int a;int b;int c;public static int run(){\n" +
	        		"//Kommentar\n" +
	        		"System.out.println(\"asd\");\n" +
	        		"return 1;}}\n" +
	        		"public class ohno{}"},
	        {true, " "}
	      });
	   }

	@Test
	public void Klassen_Solo() {	
		testCompiler.compile();
		Boolean noCompilationFailures = testCompiler.getFailures().size()==0;
		assertEquals(expectedResult_Solo, noCompilationFailures);
	}
	
	@Test
	public void Klassen_mit_Import(){
		TestCompiler testCompiler = new TestCompiler(testString, "import java.util.Vector;", null, null);
	}
	
	@After
	public void print(){
		if(false != expectedResult_Solo != testCompiler.getFailures().isEmpty()){
			 System.out.println(testCompiler.getFailures().get(0).get("Nachricht"));
			 System.out.println(testCompiler.getFailures().get(0).get("Class"));
		 }
		testCompiler=null;
	}
}
