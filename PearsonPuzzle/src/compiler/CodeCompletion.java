package compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeCompletion {
	
	public static String completeCode(String code, String onlineImports, String methodsImport){
		code=onlineImports+code;
		return code;
	}
	
	/**
	 * Sucht nach " class " und gibt den danach folgenden zusammenhängenden String wieder.
	 * Als Namensende kommt Whitespace oder '{' in Frage.
	 * @param sourceCode Sourcecode
	 * @return Klassenname (erster vorkommender)
	 */
	public static String extractClassName(String sourceCode){
		String className = null;
		String substring = null;
		Pattern pattern = Pattern.compile("(\\s+class+\\s)");
		Matcher matcher = pattern.matcher(sourceCode);
		if(matcher.find()){
			Integer indexOfClass = matcher.start();
			Integer endIndex = sourceCode.indexOf("{",indexOfClass+6);
			if(endIndex == -1)
				substring = sourceCode.substring(indexOfClass+6).trim();
			else
				substring = sourceCode.substring(indexOfClass+6, endIndex).trim();
			pattern = Pattern.compile("\\s");
			matcher = pattern.matcher(substring);
			if(matcher.find())
				className = substring.substring(0, matcher.start());
			else if(!substring.isEmpty())
				className = substring;
		}
		return className;
	}

	public static String extractDeclarationName(String line1, String line2, String expression) {
		if(line2==null)
			line2="";
		String declarationName=null;
		Pattern pattern = Pattern.compile("(;|\\s)+import+\\s");
		Matcher matcher = pattern.matcher(line1.trim());
		// Wenn die ertse Zeile nur aus dem zu suchenden String besteht
		if(line1.trim().equals(expression.trim())){
			if(line2.trim().contains(" "))
				declarationName = line2.trim().substring(0, line2.indexOf(" ")).trim();
			else if(line2.contains(";"))
				declarationName = line2.substring(0, line2.indexOf(";")).trim();
			else
				declarationName = line2.trim();
		}
		// Wenn die erste Zeile mit dem zu suchenden String beginnt
		else if(line1.trim().startsWith(expression)){
			if(line1.trim().contains(" "))
				line1=line1.replaceFirst(expression, "").trim();
				if(line1.trim().contains(" "))
					declarationName = line1.substring(0, line1.indexOf(" ")).trim();
				else if(line1.contains(";"))
					declarationName = line1.substring(0, line1.indexOf(";")).trim();
				else 
					declarationName = line2.trim();
					
		}
		// Wenn die erste Zeile mit dem zu suchenden String endet
		else if(line1.trim().endsWith(expression))
			declarationName = line2.trim();
				
		// whitespace oder ; + import + whitespace
		else if(matcher.find()){
			line1 = line1.substring(matcher.end()).trim();
			if(line1.trim().contains(" "))
				declarationName = line1.substring(0, line1.indexOf(" ")).trim();
			else if(line1.contains(";"))
				declarationName = line1.substring(0, line1.indexOf(";")).trim();
			else
				declarationName = line2.trim();
		}
		
		// Fall die Zweite zeile mehr enthält, als die Deklaration
		if(declarationName!=null && declarationName.contains(";"))
			declarationName = declarationName.substring(0, declarationName.indexOf(";"));
		if(declarationName!=null && declarationName.isEmpty())
			return null;
		return declarationName;				
	}

}
