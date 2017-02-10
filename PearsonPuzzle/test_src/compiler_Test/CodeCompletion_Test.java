package compiler_Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import compiler.CodeCompletion;

/**
 * Parametrisierter Test, der die Klasse Code Completion prüft.
 * @author workspace
 *
 */

@RunWith(Parameterized.class)
public class CodeCompletion_Test {
	
	@Parameterized.Parameters
	   public static Collection<Object[]> primeNumbers() {
	      return Arrays.asList(new Object[][] {
	    		  {"classt", "classt;",  	"//ass \npublic /**dnnd\ndn*/class /**d\n\ndndn*/classt;"},
	    		  {"classt", "classt",  	"public class\n classt ;"},
	    		  {null, "classt",  	"public\n class classt {"},
	    		  {"classt", "classt", 	"public class classt\n\t ;asdd d\t "},
	    		  {"classt", "classt",  	"public class classt {"}, 
	    		  {"classt", "classt", 	"public class classt ;\n"},
	    		  {"classt", null, 	"public class  "},
	    		  {null, null, "class"}, 
	    		  {null, null, null}
	      });
	   }
	private String testString;
	private String expectedResult_className;
	private String expectedResult_Decalaration;
	
	public CodeCompletion_Test(String expectedResult_Declaration, String expectedResult_className, String testString) {
		      this.testString = testString;
		      this.expectedResult_Decalaration = expectedResult_Declaration;
		      this.expectedResult_className = expectedResult_className;
		   }

	@Test
	public void extractClassName_Test() {
			assertEquals(expectedResult_className,CodeCompletion.extractClassName(testString));
	}
	@Test
	public void extractDeclarationName_Test() {
		String withoutComments = CodeCompletion.removeComment(testString);
		assertEquals(expectedResult_Decalaration,CodeCompletion.extractDeclarationName(withoutComments, "classt;", "public class"));
	}
	
	

}
