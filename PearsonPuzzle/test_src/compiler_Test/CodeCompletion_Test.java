package compiler_Test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import compiler.CodeCompletion;

@RunWith(Parameterized.class)
public class CodeCompletion_Test {
	
	@Parameterized.Parameters
	   public static Collection<Object[]> primeNumbers() {
	      return Arrays.asList(new Object[][] {
	    		  {"classt",  	"public class classt ;"}, 
	    		  {"classt", 	"public class classt\n\t ;asdd d\t "},
	    		  {"classt",  	"public class classt {"}, 
	    		  {"classt", 	"public class classt ;\n"},
	    		  {null, 	"public class"},
	    		  //{"classt", "class"}
	      });
	   }

	private String testString;
	private String expectedResult_className;
	   public CodeCompletion_Test(String expectedResult_className, String testString) {
		      this.testString = testString;
		      this.expectedResult_className = expectedResult_className;
		   }

	@Test
	public void extractClassName_Test() {
		assertEquals(expectedResult_className,CodeCompletion.extractClassName(testString));
	}
	@Test
	public void extractDeclarationName_Test() {
		if(expectedResult_className==null)
			expectedResult_className = "asd";
		assertEquals(expectedResult_className,CodeCompletion.extractDeclarationName(testString, expectedResult_className+";", "public class"));
	}
	
	

}
