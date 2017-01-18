package jUnitUmgebung;

import java.util.*;
import junit.textui.TestRunner;

public class MemClassLoader_JUnit extends ClassLoader
{
  private final Map<String, MemJavaFileObject> classFiles =
    new HashMap<String, MemJavaFileObject>();

  public MemClassLoader_JUnit()
  {
    //super( ClassLoader.getSystemClassLoader() );
	super(JUnitRunner.class.getClassLoader());
  }

  public void addClassFile( MemJavaFileObject memJavaFileObject )
  {
    classFiles.put( memJavaFileObject.getClassName(), memJavaFileObject );
  }
  
  @Override
  protected Class<?> findClass( String name ) throws ClassNotFoundException
  {
    MemJavaFileObject fileObject = classFiles.get( name );

    if ( fileObject != null )
    {
      byte[] bytes = fileObject.getClassBytes();
      return defineClass( name, bytes, 0, bytes.length );
    }
    System.out.println(name);

    return super.findClass( name );
  }
}
