package JUnitUmgebung;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import compiler.StringJavaFileObject;

public class JUnitRunner {
	private final static String DEFAULT_CLASS_NAME="testCode";
	private HashMap<String, String> srcCodeMap;
	private HashMap<String, MemClassLoader> classLoaders;

	/**
	 * Klasse dient dazu, in Textform vorliegenden Code <br>
	 * auf unterschiedliche Art und Weise zu testen. 
	 * @param sourceCode_ToBeTested zu testende Klassen, Methoden, ...
	 * @param imports evtl. notwendige imports
	 */
	public JUnitRunner(String sourceCode_ToBeTested, String imports){
		srcCodeMap = new HashMap<String, String>();
		fillSrcCodeMap(sourceCode_ToBeTested);
	}
	
	/**
	 * HashMap sourceCodeMap wird mit Inhalt gefüllt.<br>
	 * Falls in src keine Klasse defineirt wird, wird klasse "gewrapt".
	 * 
	 * @param src sourceCode
	 */
	private void fillSrcCodeMap(String src){
		 // Konflikte bezüglich des Klassennamens werden ausgeschlossen
		 String className=new String(DEFAULT_CLASS_NAME);
		 
		 // Falls der Code keine Klasse enthält, wird hier eine generiert 
		 if(!src.contains(" class ")){
			 while(src.contains(className)){
				 className=className+"_";
			 }
			 src = "class "+className+" { "+src+" }";
			 srcCodeMap.put(className, src);
		 }
		 else{
			 Vector<String> srcVector= new Vector<String>();
			 for(String line: src.split("\n")){
				 srcVector.add(line);
			 }
			 fillSrcCodeMap(srcVector);
		 }
	}
	
	/**
	 * HashMap sourceCodeMap wird mit Inhalt gefüllt.<br>
	 * Kann nicht agieren, wenn keine codeLine mit " class " enthalten ist,<br>
	 * spirch in codeLines keine Klasse definiert ist.
	 * 
	 * @param codeLines
	 */
	private void fillSrcCodeMap(Vector<String> codeLines){
//		HashMap<String, String> classes = new LinkedHashMap<String, String>();
		String className=new String();
		String solutionString = new String();
			for(String line: codeLines){
				if(line.contains(" class ")){
					if(className.isEmpty()){
						className=line.substring(line.indexOf("class")+5, line.indexOf("{"));
						className=className.trim();
						solutionString=solutionString+"\n"+line;
					}
					else{
//						if(classes.containsKey(className))
//							return false;
//						classes.put(className, solutionString);
						
						srcCodeMap.put(className, solutionString);
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
			
			fillSrcCodeMap(solutionString);
		else
			srcCodeMap.put(className, solutionString);
	}
	
	/**
	 * Klassen in srcCodeMap werden 
	 * @return
	 */
	public void compileClasses(){
		for(String className:srcCodeMap.keySet()){
			String src = srcCodeMap.get(className);
			// TODO: Imports hinzufügen
			StringJavaFileObject javaFile = new StringJavaFileObject( "gen_src//"+className, src );
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			StandardJavaFileManager fileManager = compiler.getStandardFileManager( diagnostics, null, null );
			Iterable<? extends JavaFileObject> units = Arrays.asList( javaFile );
			CompilationTask task = compiler.getTask( null, fileManager, diagnostics, null, null, units );
			task.call();
			
			 // Diagnose (bei aufgetretenem Fehler)
			for ( Diagnostic<?> diagnostic : diagnostics.getDiagnostics() )
			{
				 HashMap <String, String> compileFailure = new HashMap<String, String>(9);
				 compileFailure.put("Class", ""+className);
				 compileFailure.put("Kind", ""+diagnostic.getKind());
				 compileFailure.put("Quelle", ""+diagnostic.getSource());
				 compileFailure.put("Code", ""+src);
				 // TODO: Fehlerbericht anpassen (Nachricht)
				 compileFailure.put("Nachricht",""+diagnostic.getMessage( null ) );
				 compileFailure.put("Zeile", ""+diagnostic.getLineNumber() );
				 compileFailure.put("Position", ""+diagnostic.getPosition());
				 compileFailure.put("Spalte", ""+diagnostic.getColumnNumber() );
				 compileFailure.put("Startpostion", ""+diagnostic.getStartPosition());
				 compileFailure.put("Endposition", ""+diagnostic.getEndPosition() );				 
			 }			 
		
			 try {
				 fileManager.close();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }

			 URLClassLoader classLoader = null;
			 try {
				 classLoader = new URLClassLoader( new URL[] { new File(".").getAbsoluteFile().toURI().toURL() } );
				 //System.out.println(classLoader.getURLs());
			 } catch (MalformedURLException e) {
				 e.printStackTrace();
			 }
			 try {
				 System.out.println(className);
				 //className = "sourceCode_toTest/"+className;
				 Class.forName( className, true, classLoader );
				 
			 } catch (ClassNotFoundException e) {
				 e.printStackTrace();
			 }    
		 }
	}
	
	/**
	  * Klassendateien werden gelöscht, falls sie existiert.
	  * @param className Name der Klasse (Dateiname = className.class)
	  */
	 private void deleteTestClasses(){
		 for(String className : srcCodeMap.keySet()){
			 File file = new File(className+".class");
			 if(file.exists()){
				 file.delete();
			 } 
		 }
	 }
	 
	/**
	 * Die in srcCodeMap (in textform) enthaltenen Klassen werden kompiliert.
	 */
	public void loadClassesToMemory(){
		for(String className : srcCodeMap.keySet()){
			String sourceCode = srcCodeMap.get(className);
			final String[] imports = {"",""};
			for(String importString: imports){
				if(!sourceCode.contains(importString))
					sourceCode=importString+sourceCode;
			}
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
	
	/**
	 * Unit Test wird kompiliert und gestartet.
	 * Anschließend werden vorhandene Klassendateien gelöscht.
	 * @param unitText JUnit Klasse in Textform
	 * @return Result
	 */
	public Result run(String unitText){
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
		MemClassLoader_JUnit unitClassLoader = new MemClassLoader_JUnit();
				
		JavaFileManager unitFileManager = new MemJavaFileManager_JUnit( compiler, unitClassLoader );
		
	// ---------------------------- Zugriff auf die zu Testenden Klassen (erst noch zu kompilieren)		
		
		
		
		
		
		JavaFileObject javaFile = new StringJavaFileObject( className, unitText );
		Collection<JavaFileObject> units = Collections.singleton( javaFile );
		Set<String> options = new HashSet<String>();		
		// Hier kann man den Compiler verfolgen:
			//options.add( "-verbose");
			//options.add("-deprecation");
		CompilationTask task = compiler.getTask( null, unitFileManager,null, options, null, units );
		task.call();
		
		try {
			unitFileManager.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		deleteTestClasses();
		
		try {
			Result result = JUnitCore.runClasses(unitClassLoader.findClass(className));
			System.out.println("Fehler beim Test: "+result.getFailures());
			return result;
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
