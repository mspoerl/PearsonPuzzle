package Compiler;

import static org.junit.Assert.*;

import org.junit.Test;

import compiler.TestCompiler;

public class TestCompiler_Test {
	
	public final static String[] testStrings ={
		"", 		// TODO: fill
		"",			// TODO: fill
		" ", 		// Space String
		"",			// EmptyString
	};
	public final static String[] testStrings_fail = {
		"asd",		// TODO: fill
		"asd",		// TODO: fill
		"asd"		// Nonsense 
	};

	@Test
	public void test() {
		for(String testString : testStrings){
			assertTrue(TestCompiler.compileCode(testString));
		}
		for(String testString : testStrings_fail){
			assertFalse(TestCompiler.compileCode(testString));
		}
	}

}
