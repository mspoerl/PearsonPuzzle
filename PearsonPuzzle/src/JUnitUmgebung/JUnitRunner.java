package JUnitUmgebung;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import compiler.StringJavaFileObject;

public class JUnitRunner {
	public static Result run(String unitText){
		if(!unitText.contains(" class "))
			return new Result();
		String className = new String();
		className=unitText.substring(unitText.indexOf("class")+5, unitText.indexOf("{")).trim();
		System.out.println(className);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		MemClassLoader classLoader = new MemClassLoader();
		JavaFileManager fileManager = new MemJavaFileManager( compiler, classLoader );
		JavaFileObject javaFile = new StringJavaFileObject( className, unitText );
		Collection<JavaFileObject> units = Collections.singleton( javaFile );
		Set<String> options = new HashSet<String>();
		System.out.println(compiler.isSupportedOption("-cp \"\\usr\\share\\eclipse\\dropins\\jdt\\plugins\\org.junit_4.8.2.dist\\junit.jar\""));
		System.out.println(compiler.isSupportedOption("-cp \"lib\\junit.rar\""));
		System.out.println(compiler.isSupportedOption("-cp \".;..junit.rar\""));
		System.out.println(compiler.isSupportedOption("-cp \".;..\\hamcrest-1.3\\hamcrest-core-1.3.0.jar\""));
		//options.add( "-cp \"lib/junit.rar\"");
		CompilationTask task = compiler.getTask( null, fileManager,null, options, null, units );
		System.out.println(task.call());
		try {
			fileManager.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			System.out.println(unitText);
			System.out.println(Class.forName(className,true, classLoader).newInstance().getClass());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			return JUnitCore.runClasses(Class.forName(className,true, classLoader).getClass());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Result();
			
	}
}
