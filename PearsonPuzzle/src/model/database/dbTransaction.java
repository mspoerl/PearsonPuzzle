package model.database;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import view.pearsonPuzzleException;

import model.database.UserDBaccess;

/**
 * Klasse Kapselt die Zugrifsslogik auf die Datenbank.
 * @author workspace
 *
 */
public class dbTransaction implements Transaction{
	
	private final static int length_projectName = 36;
	private final static int length_projectDescription = 1024;
	private final static int MAX_line_length_Code = 8000;
	private final static int MIN_line_length_Code = 10;
	 
	private UserDBaccess userDBaccess;
	
	public dbTransaction() throws pearsonPuzzleException{
		try {
			userDBaccess = new UserDBaccess();
			//userDBaccess.resetAll();
		} catch (SQLException e) {
			if(((SQLException) e).getSQLState().equals("XJ040")){ // Failed to start database '<databaseName>', see the next exception for details.
				throw(new pearsonPuzzleException(pearsonPuzzleException.anotherInstanceIsRunnign));
			}
			else if(e.getSQLState().equals("XJ004")){ // Database '<databaseName>' not found.
				throw new pearsonPuzzleException(pearsonPuzzleException.noDatabaseExists);
			}
			else if(e.getSQLState().equals("42X05")){ // Table/View '<objectName>' does not exist.
				throw new pearsonPuzzleException(pearsonPuzzleException.noDatabaseExists);
			}
			else{
				//System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
	
	
	
	//------------------------------------------------------------------------------------
	//------------------------------- Teacher Student Tabellen ---------------------------
	//------------------------------------------------------------------------------------
		   
	public boolean addUser(String tablename, String username, String password){
		if(!userDBaccess.doesUserExists(username)){
		 return userDBaccess.addUser(tablename, username, password);
		}
		System.out.println("person already exists");
		return false;
	}
	
	//gibt nur die namen aus der tabelle table zurück
	public Vector<String> getNames(String table){
		 return userDBaccess.getNames(table);
	}
		
	//gibt alle namen von Lehrern und Schülern aus
	public Vector<String> getAllNames(){
		 Vector<String> namevector = userDBaccess.getNames("teachers");
		 namevector.addAll(userDBaccess.getNames("students"));
		 return namevector;
		 
	}
	
	
	 public boolean deleteUser(String username, String table){
		 if(userDBaccess.doesUserExists(username)){
		 boolean success = userDBaccess.deleteUser(username, table);
		 if(!success){
			 System.out.println("person does exist in other table, not deleted");}
		 return success;
		 }
		 else{
			 //die Person existiert nicht
			 System.out.println("person does not exist");
			 return false;
		 }
	 }
	 
	 public boolean deleteUser(String username){
		 if(userDBaccess.doesUserExists(username)){
			 if(!userDBaccess.deleteUser(username, "students")){
				 return userDBaccess.deleteUser(username, "teachers");
			 }
			 else{
				 return true;
			 }
		 }
			 
			 else{
				 //die Person existiert nicht
				 System.out.println("person does not exist");
				 return false;
			 }
		 
	 }
	
	
	public boolean lookUpstudent(String name, char[] password){
		String passwordstring = new String(password);
		return userDBaccess.lookUpstudent(name, passwordstring);
	   }
	
	public boolean lookUpteacher(String name, char[] password){
		String passwordstring = new String(password);
		return userDBaccess.lookUpteacher(name, passwordstring);
	   }
	
	//-----------------------------------------------------------------------------
	//------------------------------- Projekte Tabellen ---------------------------
	//-----------------------------------------------------------------------------
	
	
	private String unite(String[] codeStrings, boolean random, int tab){
		   if(random){
			   String buffer;
			   for(int i=codeStrings.length-1; i>0;i--){
					int randomInt = new java.util.Random().nextInt(i);
					buffer=codeStrings[randomInt];
					codeStrings[randomInt]=codeStrings[i];
					codeStrings[i]=buffer;
				}
		   }
		   StringBuffer stringBuffer = new StringBuffer();
		   for(String line: codeStrings){
			   if(!line.trim().equals("")){
				   // trim entfernt auch tabs, deshalb while schleife
				   line=line.replaceAll("\t ", "\t");
				   while(line.startsWith(" ")){
					   line=line.replaceFirst(" ", "");
				   }
				   stringBuffer.append(line+"\n");
			   }
		   }
		   return new String(stringBuffer);
	   }
	
	
	
	public void saveProject(String projectname, String codeString, String imports,
			String description, int tab) {
		   // TODO: Linelength umdefinieren (wird nicht zwingend benötigt, übergebene Integer kann aber evtl. Verwendung finden
		String[] codeStrings=codeString.split("\n");
		
		//linelength wird automatisch zugewiesen
		int linelength=10;
		for(int i=0;i<codeStrings.length;i++){
			linelength=Math.max(linelength,codeStrings[i].length());
		}
		
		if(linelength < 1){
			   linelength = MIN_line_length_Code;
		   }
		   else if(linelength >8000){
			   linelength = MAX_line_length_Code;
		   }
		   if(projectname==null || codeString==null){
			   return;
		   }
		   else if(projectname.length()==0){
			   return;
		   }
		   else if(projectname.length()>length_projectName){
			   projectname= new String(projectname.substring(0,length_projectName-1));
		   }
		   else if(description.length()>length_projectDescription){
			   description= new String(description.substring(0,length_projectDescription-1));
		   }
		   
		   // -- Code Array wird erzeugt und wieder zu einem String zusammengefasst
		   codeString = unite(codeStrings, false, tab);
		   ArrayList<Integer> randomKey = getRandomKeys(codeStrings.length);
		   try {
			userDBaccess.saveProject(projectname, codeStrings, imports, description, randomKey, tab, linelength);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   	}
	public void saveJUnitTest(String projectname,String jUnitCode) {
		if(jUnitCode.length()>1000)
			try {
				throw new pearsonPuzzleException("JUnit Code ist zu lang und konnte deshalb nicht gespeichert werden!");
			} catch (pearsonPuzzleException e) {
				e.printStackTrace();
				return;
			}
		userDBaccess.saveJUnitTest(projectname, jUnitCode);		
	}
	
	
	public ArrayList<Integer> getRandomKeys(int number){
		ArrayList<Integer> randomKey = new ArrayList<Integer>();
		int array[] = new int[number];
		for(int j=0;j<number;j++){
			array[j]=j;
		}
		
		//erstellt ein Array mit Randomkeys
		for(int i=number; i>0;i--){
			int randomInt = new java.util.Random().nextInt(i);
			int buffer = array[randomInt];
			array[randomInt]=array[i-1];
			array[i-1]=buffer;
		}
		
		//transformiert das Array in eine ArrayList<Integer>
		for (int index = 0; index < number; index++)
		{
		    randomKey.add(array[index]);
		}
		
		return randomKey;
	}
	
	public void updateDescription(String projectname, String description){
		userDBaccess.updateDescription(projectname, description);
		
	}
	
	public void renameProject(String oldName, String newName){
		userDBaccess.renameProject(oldName, newName);
	}
	
	
//	public void saveProject(String[] codeString, String projectname, int linelength, final int projectID) throws SQLException{
//		   projectname=new String(projectname.trim());
//		   if(linelength < 1){
//			   linelength =MIN_line_length_Code;
//		   }
//		   else if(linelength >8000){
//			   linelength = MAX_line_length_Code;
//		   }
//		   // leerer Projektname ist unzulässig
//		   if(projectname.equals("")){
//			   return;
//		   }
//		   
//		   
//		   // Zeilen zufällig anordnen
//		   String buffer;
//		   String[] mixedcodeString=codeString;
//			for(int i=mixedcodeString.length-1; i>0;i--){
//				int randomInt = new java.util.Random().nextInt(i);
//				buffer=mixedcodeString[randomInt];
//				mixedcodeString[randomInt]=mixedcodeString[i];
//				mixedcodeString[i]=buffer;
//			}
//			userDBaccess.saveProject(codeString, mixedcodeString, projectname, linelength, projectID);
//			
//			//-----------------------------testing------------------------------------
//		   
//	   }
	
	public void saveProjectSettings(String projectname, int tabSize, int grade){
		if(projectname.equals("")){
			   return;
		   }
		userDBaccess.saveProjectSettings(projectname, tabSize, grade);
	}
	
	
	
//	
//	public String[] getRandomCodeArray(String projectname) throws SQLException{  
//		   String[] codeStrings=userDBaccess.getCodeArray(projectname);  
//		   String[] randomCodeStrings = new String[codeStrings.length];
//		   ArrayList<Integer> randomKeys = userDBaccess.getRandomKeys(projectname);
//		   for(int i=0;i<randomKeys.size();i++){
//			   randomCodeStrings[i]=codeStrings[randomKeys.get(i)];
//		   }
//		   return randomCodeStrings;
//	   }
	
	public ArrayList <String> getCodeList(String projectname) throws SQLException{
		   
		   return userDBaccess.getCodeList(projectname);
	   }
	public String getCode(String projectname){
		try{
			return userDBaccess.getCode(projectname);
		} catch(SQLException e){
			if(e.getSQLState().equals("42X05")){ // Table/View '<objectName>' does not exist.
				try {
					throw new pearsonPuzzleException("Dieses Projekt enthält keinen Inhalt");
				} catch (pearsonPuzzleException e1) {} 
			}
			e.printStackTrace();
			return new String();
		}
	}
	
	public Vector<Vector<Integer>> getOrdervektor(String projectname){
		Vector<Vector<Integer>> ordervector = new Vector<Vector<Integer>>();
		//solange order nicht leer ist wird ordervector befüllt
		for(int ordernumber=0;!userDBaccess.getOrder(projectname, ordernumber).isEmpty();ordernumber++){
			ordervector.add(userDBaccess.getOrder(projectname, ordernumber));
		}
		return ordervector;
	}
	public void saveOrder(String projectname, Vector<Vector<Integer>> orderMatrix){
		deleteAllOrders(projectname);
		for(Vector<Integer> orderVector : orderMatrix){
			userDBaccess.addOrder(projectname, orderVector);
		}
	}
//	
//	private void addOrder(String projectname, Vector<Vector<Integer>> newOrdervektor){
//		for(int i = 0;i<newOrdervektor.size();i++){
//			userDBaccess.addOrder(projectname, newOrdervektor.get(i));
//		}	
//		}
//	
	@SuppressWarnings("unused")
	private boolean deleteOrder(final String projectname, final int ordernumber){
		   return userDBaccess.deleteOrder(projectname, ordernumber);
	 }
	
	private boolean deleteAllOrders(String projectname){
		int i = 0;
		boolean success = true;
		   while(success){
			   success = userDBaccess.deleteOrder(projectname, i);
			   i++;
		   }
		   return !success;
	   }
	
	
	 public ArrayList <String> getProjects(int grade) {
		 try {
			return userDBaccess.getProjects(grade);
		
		 } catch (SQLException e) {
			 e.printStackTrace();
			try {
				throw new pearsonPuzzleException("Keine Projekte vorhanden. \nBitte legen Sie ein neues Projekt an.");
			} catch (pearsonPuzzleException e1) {
				e1.showDialog();
			}
			return new ArrayList<String>();
		 }
	 }
	
	 public String getProjectDescription(String projectname) throws SQLException{
		 
		 return userDBaccess.getProjectDescription(projectname);
	 }
	 
	 public int getTabSize(String projectname) {
		 
		 return userDBaccess.getTabSize(projectname);
	 }
	 
	 private void createTable_Projects() throws SQLException{
		 userDBaccess.createTable_Projects();
		 saveProject(
					"HalloWorld", 
					"public static void main(String args[]){ \n\t System.out.println(\"hallo world\");\n }" , 
					"",
					"",
					0);
	 }
			
	 private void createTable_Students() throws SQLException{
		 userDBaccess.createTable_Students();
	 }
	 
	 private void createTable_Teachers() throws SQLException{
		 userDBaccess.createTable_Teachers();
	 }
	 
	 public void resetAll() throws SQLException{
		 createTable_Projects();
		 createTable_Students();
		 createTable_Teachers();
	 }
	 
	 public boolean delete(String projectname) {
		 return userDBaccess.delete(projectname);
	 }
	 
	 public boolean projectExists(String projectName) {
		 return userDBaccess.projectExists(projectName);
	 }



	public String getJUnitCode(String projectName) {
		try {
			return userDBaccess.getJUnitCode(projectName);
		} catch (SQLException e) {
			if(e.getSQLState().equals("42X04")){ // Table/View '<objectName>' does not exist.
				return null;
			}
			e.printStackTrace();
			return null;
		}
	}
}
