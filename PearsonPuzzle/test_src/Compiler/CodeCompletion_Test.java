package Compiler;

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
	    		  {"classt", "public class classt"}, 
	    		  {"classt", "public class classt\n\t asdd d\t "},
	    		  {"classt", "public class classt {"}
	      });
	   }

	private String testString;
	private String expectedResult;
	   public CodeCompletion_Test(String expectedResult,String testString) {
		      this.testString = testString;
		      this.expectedResult = expectedResult;
		   }

	@Test
	public void test() {
		assertEquals(expectedResult,CodeCompletion.extractClassName(testString));
	}
	
	

}
