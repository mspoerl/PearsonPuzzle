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
		if(sourceCode.contains(" class ")){
			Integer indexOfClass = sourceCode.indexOf(" class ");
			Integer endIndex = sourceCode.indexOf("{",indexOfClass+6);
			if(endIndex == -1)
				substring = sourceCode.substring(indexOfClass+6).trim();
			else
				substring = sourceCode.substring(indexOfClass+6, endIndex).trim();
			Pattern pattern = Pattern.compile("\\s");
			Matcher matcher = pattern.matcher(substring);
			if(matcher.find())
				className = substring.substring(0, matcher.start());
			else if(!substring.isEmpty())
				className = substring;
		}
		return className;
	}

}
