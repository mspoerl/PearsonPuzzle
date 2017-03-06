package compiler;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * String Java File Object hält den Sourcecode in kompilierbarer Form bereit.
 * 
 * @author workspace
 */
public class StringJavaFileObject extends SimpleJavaFileObject {
    private final CharSequence code;

    public StringJavaFileObject(String name, CharSequence code) {
	super(URI.create("string:///" + name.replace('.', '/')
		+ Kind.SOURCE.extension), Kind.SOURCE);
	this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
	return code;
    }
}