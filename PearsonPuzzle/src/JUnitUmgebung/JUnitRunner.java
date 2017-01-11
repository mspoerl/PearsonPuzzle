package JUnitUmgebung;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import compiler.StringJavaFileObject;

public class JUnitRunner {
	private final static String DEFAULT_CLASS_NAME="testCode";
	private HashMap<String, String> sourceCodeMap;
	private HashMap<String, MemClassLoader> classLoaders;

	public JUnitRunner(String sourceCode_ToBeTested, String imports){
		sourceCodeMap = new HashMap<String, String>();
		fillClasses(sourceCode_ToBeTested);
		loadClasses();
	}
		
	private void fillClasses(String src){
		 // Konflikte bezüglich des Klassennamens werden ausgeschlossen
		 String className=new String(DEFAULT_CLASS_NAME);
		 
		 // Falls der Code keine Klasse enthält, wird hier eine generiert 
		 if(!src.contains("class")){
			 while(src.contains(className)){
				 className=className+"_";
			 }
			 src = "class "+className+" { "+src+" }";
			 sourceCodeMap.put(className, src);
		 }
		 else{
			 Vector<String> srcVector= new Vector<String>();
			 for(String line: src.split("\n")){
				 srcVector.add(line);
			 }
			 fillClasses(srcVector);
		 }
	}
	
	private void fillClasses(Vector<String> solutionArray){
//		HashMap<String, String> classes = new LinkedHashMap<String, String>();
		String className=new String();
		String solutionString = new String();
			for(String line: solutionArray){
				if(line.contains("class")){
					if(className.isEmpty()){
						className=line.substring(line.indexOf("class")+5, line.indexOf("{"));
						className=className.trim();
						solutionString=solutionString+"\n"+line;
					}
					else{
//						if(classes.containsKey(className))
//							return false;
//						classes.put(className, solutionString);
						
						sourceCodeMap.put(className, solutionString);
						className = new String();
						solutionString=new String(line);
					}
				}
				else{
					solutionString=solutionString+"\n"+line;
				}
			}
//		for(String classN : classes.keySet()){
//			ret = ret & compileCode(classN, classes.get(classN));
//		}
//		return ret;
		if(className.isEmpty())
			
			fillClasses(solutionString);
		else 
			sourceCodeMap.put(className, solutionString);
	}
	
	private void loadClasses(){
		for(String className : sourceCodeMap.keySet()){
			String sourceCode = sourceCodeMap.get(className);
			final String[] imports = {"",""};
			for(String importString: imports){
				if(!sourceCode.contains(importString))
					sourceCode=importString+sourceCode;
			}
			System.out.println(className);
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			MemClassLoader classLoader = new MemClassLoader();
			JavaFileManager fileManager = new MemJavaFileManager( compiler, classLoader );
			JavaFileObject javaFile = new StringJavaFileObject( className, sourceCode);
			Collection<JavaFileObject> units = Collections.singleton( javaFile );
			Set<String> options = new HashSet<String>();		
			// Hier kann man den Compiler verfolgen:
				//options.add( "-verbose");
				//options.add("-deprecation");
			CompilationTask task = compiler.getTask( null, fileManager,null, options, null, units );
			//System.out.println(task.call());
			task.call();
			try {
				fileManager.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("class "+className+"\n");
			
			//this.classLoaders.put(className, classLoader);
//						
//			try {
//				
//				Class.forName(className,true, classLoader).newInstance();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return;
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	
	public static Result run(String unitText){
		if(!unitText.contains(" class "))
			return new Result();
		String className = new String();
		final String[] imports = {"\n import org.junit.Test; \n", "import static org.junit.Assert.*"};
		
		className=unitText.substring(unitText.indexOf("class")+5, unitText.indexOf("{")).trim();
		for(String importString: imports){
			if(!unitText.contains(importString))
				unitText=importString+unitText;
		}
		//System.out.println(unitText);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		MemClassLoader_JUnit classLoader = new MemClassLoader_JUnit();
		JavaFileManager fileManager = new MemJavaFileManager_JUnit( compiler, classLoader );
		JavaFileObject javaFile = new StringJavaFileObject( className, unitText );
		Collection<JavaFileObject> units = Collections.singleton( javaFile );
		Set<String> options = new HashSet<String>();		
		// Hier kann man den Compiler verfolgen:
			//options.add( "-verbose");
			//options.add("-deprecation");
		CompilationTask task = compiler.getTask( null, fileManager,null, options, null, units );
		//System.out.println(task.call());
		task.call();
		try {
			fileManager.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			return JUnitCore.runClasses(classLoader.findClass(className));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
//		
//		try {
//			System.out.println(unitText);
//			System.out.println(Class.forName(className,true, classLoader).newInstance().getClass());
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//
//		try {
//			return JUnitCore.runClasses(Class.forName(className, false, classLoader).getClass());
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
