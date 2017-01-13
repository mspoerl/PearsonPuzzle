package JUnitUmgebung;

public class run {
	public static void main (String args[]){
		runUnitTest();
	}
	
	public static void runUnitTest(){
		
		final String unitText = "import org.junit.Test; \nimport static org.junit.Assert.*; \n\npublic class testcase_Test{\n\t@Test\n\t"+"public void testMethode1(){ \n"+"\t\tassertTrue( true);\n\t}}\n";
		final String code = "public class test{public static boolean runt(){int a=0; int b=3; int c=a+b; return true;}}";
		JUnitRunner jUnitRunner = new JUnitRunner(code, null);
		jUnitRunner.run(unitText);
	}

}
