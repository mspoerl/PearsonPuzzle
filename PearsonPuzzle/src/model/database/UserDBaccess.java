package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

public class UserDBaccess {

	 Connection conn;
	 private final static int length_projectName = 36;
	 private final static int length_projectDescription = 1024;
	 public UserDBaccess() throws SQLException {
		String dbUrl = "jdbc:derby:database;create=true";
		conn = DriverManager.getConnection(dbUrl);
		//derby.ui.codeset
		//this.normalDbUsage();
		}
	 
	 public void connectionToDerby() {
	     // -------------------------------------------
	     // URL format is
	     // jdbc:derby:<local directory to save data>
	     // -------------------------------------------  
	 }
	 
	 public static int getLength_ProjectName(){
		 return length_projectName;
	 }
	 

		//------------------------------------------------------------------------------------
		//------------------------------- Teacher Student Tabellen ---------------------------
		//------------------------------------------------------------------------------------
	   
//	 Fügt nutzer mit namen username und passwort password zu der Tabelle table hinzu
	 public boolean addUser(String tablename, String username, String password){
		 Statement stmt;
		try {
			stmt = conn.createStatement();
		   stmt.executeUpdate("INSERT INTO "+tablename+" values ('"+username+"','"+password+"')");
		   return true;}
		   catch(SQLException e){	//username bereits vorhanden => false (falls handling erforderlich)
			   if(e.getSQLState().equals("23505")){
				   
				   return false;
			   }
			   return false;
		   }
		 
	 }
	 
//	 Sagt ob eine Person mit Namen username in einer der Tabellen students oder teacher existiert
	 public boolean doesUserExists(String username){
		 try{	  
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE username= '" + username+"'");	
			   ResultSet te = stmt.executeQuery("SELECT * FROM teachers WHERE username= '" + username+"'");	
			   
				   //stmt.close();
				   return rs.next()||te.next();
			   
		   }
		   catch(Exception e){
			   return false;
		   }
		   
	   }
	 
	 //gibt alle Namen der Tabelle table zurück
	 public Vector<String> getNames(String table){
		 Vector<String> namevector = new Vector<String>();
		 try{	  
			   Statement stmt = conn.createStatement();
			  
			   ResultSet rs = stmt.executeQuery("SELECT username FROM "+table);
			  
				while(rs.next()){
					namevector.add(rs.getString("username"));
				}
			   
		   }
		   catch(Exception e){
		   }
		 return namevector;
	 }
	 
	 public boolean deleteUser(String username, String table){
		 try{	  
			   Statement stmt = conn.createStatement();
			  
		 stmt.executeUpdate("DELETE FROM "+table+" WHERE username = '"+username+"'");
		 return true;
		 }
		 catch(Exception e){
			 return false;
			
		 }
	 }
	 

	 
	 
	 
	 
	   // ------------------------ Nutzername und Passwortvergleich -------------------------------
	   
	   
	  
	   
	   // -----  Nutzername und Passwort Vergleich Schüler
	   public boolean lookUpstudent(String name, String passwordstring){
		   try{	  
			   Statement stmt = conn.createStatement();
			   ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE username= '" + name + "' AND password = '"+passwordstring+"'");	
			 //stmt.close();
			   return rs.next();		     
			   
		   }
		   catch(Exception e){
			   return false;
		   }
		   
	   }
	   
	   // ----- Nutzername und Passwort Vergleich Lehrer
	   public boolean lookUpteacher(String name, String passwordstring) {
		   try{
			   
			   Statement stmt = conn.createStatement();
			   ResultSet te = stmt.executeQuery("SELECT * FROM teachers WHERE username = '" + name + "' AND password = '"+passwordstring+"'");	
			 //stmt.close();
			   return te.next();
		   } catch(Exception e){
			   		return false;}
	   }


	   private void normalDbUsage() throws SQLException {
		   Statement stmt = conn.createStatement();

		   /*Student*/
		   ResultSet rsS;
		   try{
			   // query
			   rsS = stmt.executeQuery("SELECT * FROM students");
		   }
		   catch(SQLException e){
			   // Hier wird abgehandelt, wenn was schief läuft
			   // bzw. wenn die Tabelle (noch) nicht existiert 
			   try {
				   stmt.executeUpdate("Drop Table students");
			   } 
			   catch (SQLException ex) { // schmeißt keine Exception, da nur der Fall, dass bereits eine Tabelle existiert, abgefangen werden soll
			   }
			   // create table 
			   this.createTable_Students();
			   rsS = stmt.executeQuery("SELECT * FROM students");
		   }
		   // print out query result
		   while (rsS.next()) { 
			   //System.out.printf("%d\t%s\t%s\n", rs.getInt("id"), rs.getString("username"), rs.getString("password"));
		   }
	       
		   /*Teacher*/
		   ResultSet rsT;
		   try{
			   rsT = stmt.executeQuery("SELECT * FROM teachers");
		   }
		   catch(SQLException e){
			   // Hier wird abgehandelt, wenn was schief läuft
			   // bzw. wenn die Tabelle (noch) nicht existiert 
			   try {
				   stmt.executeUpdate("Drop Table teachers");
			   } 
			   catch (SQLException ex){ // Schmeißt keine Exception, da nur der Fall, dass keine Tabelle existiert, abgefangen werden soll.
			   }   
			   this.createTable_Teachers();
			   // query
			   rsT = stmt.executeQuery("SELECT * FROM teachers");
		   }
		   // print out query result
		   while (rsT.next()) { 
			   //System.out.printf("%d\t%s\t%s\n", rs.getInt("id"), rs.getString("username"), rs.getString("password"));			   
		   }
		   //stmt.close();
	   }
	   
	   

		//-----------------------------------------------------------------------------
		//------------------------------- Projekte Tabellen ---------------------------
		//-----------------------------------------------------------------------------
	   
	   
	   /**
	    * Speichert das übergebene Projekt 
	    * #depricated
	    * 
	    * @param codeString
	    * @param projectname
	    * @param linelength
	    * @param projectID
	    * @throws SQLException
	    */
	   public void saveProject(String projectname, String[] codeString, String imports, String description, 
			   					ArrayList<Integer> randomKeys, int tabsize, int linelength) throws SQLException{
		   
		   Statement stmt = conn.createStatement();
		   
		   //XXX doppelte Tabellennamen besser handeln
		   //FIXME imports länge = 100 ändern
		   
		   // ----- Speichert Projektnamen Tabelle
		   try{
			   stmt.executeUpdate("CREATE TABLE Projects ( " +
				   		"pName varchar("+length_projectName+") UNIQUE, " +   		
				   		"imports varchar(100) , " +  
				   		"description varchar("+length_projectDescription+"), " +
				   		"tabSize INT)");
		   }
		   catch(Exception e){ //Tabelle existiert bereits
			   }
		   
			  
		   //Projekt in Projects Tabelle abspeichern
		   try{stmt.execute("INSERT INTO Projects VALUES ("
		   		+ "'"+projectname+"',"
		   		+ "'"+imports+"',"
		   		+ "'"+description+"',"
		   		+ ""+tabsize+")");
		   
		   // Project wird in neuer Tabelle abgespeichert
		   createProject(projectname, codeString, randomKeys, linelength);
		   }
		   catch(SQLException e){	
			   //Projektname bereits vorhanden => Projekt wird aktualisiert
			   if(e.getSQLState().equals("23505")){
				   
				   stmt.executeUpdate("UPDATE Projects SET "
					   		+ "imports='"+imports+"',"
					   		+ "description='"+description+"',"
					   		+ "tabsize="+tabsize+" "
					   		+ "WHERE pName='"+projectname+"'");
				   
				   createProject(projectname, codeString, randomKeys, linelength);
		   }
		   }
		   
		   
			
			//-----------------------------testing------------------------------------
		   
	   }
	   
	   
	   
	 /**
	 * Speichert das übergebene Projekt in der Datenbank.<br>
	 * <b>Leere Zeilen werden beim Apspeichern gelöscht.</b>
	 * 
	 * @param projectname
	 * @param codeString
	 * @param linelength
	 * @param randomKey
	 */
	   
	   public void createProject(String projectname, String[] codeString, ArrayList<Integer> randomKeys, int linelength) {

		   //System.out.println("createProject");
		   try{
			   Statement stmt = conn.createStatement();
			   try{
				   stmt.executeUpdate("DROP TABLE "+projectname); 
			   }
				   catch(Exception e){
					   }
				   
			   stmt.execute("CREATE TABLE "+projectname+" ( " +
				   		"lineKey int PRIMARY KEY, " +	
				   		"codeLine varchar("+linelength+"), " +	
				   		"randomKey int)");

			   
			   for(int i = 0; i < codeString.length;i++){
				   stmt.executeUpdate("INSERT INTO "+projectname+" VALUES ("
				   		+ ""+i+","
				   		+ "'"+codeString[i]+"',"
				   		+ ""+randomKeys.get(i).intValue()+")");
			   }
		   }
		   catch(SQLException e){
			   				   return;
				   }
	
		   }
	   
	   
	   
//	   public void updateProject(String projectname, String[] codeString, ArrayList<Integer> randomKeys, int linelength) {
//		   
//		   try{
//			   Statement stmt = conn.createStatement();			   
//			   stmt.executeUpdate("ALTER TABLE "+projectname+" ALTER COLUMN codeLine SET DATA TYPE varchar("+linelength+")");
//			   for(int i = 0; i < codeString.length;i++){
//				   stmt.executeUpdate("UPDATE "+projectname+" SET "
//				   		+ "codeLine='"+codeString[i]+"',"
//				   		+ "randomKey="+randomKeys.get(i)+" "
//				   		+ "WHERE lineKey="+i);
//			   }
//			   
//			   ResultSet te = stmt.executeQuery("SELECT codeLine FROM "+projectname);
//			   while(te.next()) {//System.out.println(te.getString("codeLine"));}
//		   }
//		   catch(SQLException e){
//				   return;
//				   }
//	   }
	   
	   /**
	    * Aus dem String[] wird wieder ein String. Dieser wird entweder zufällig zusammengesetzt <br>
	    * oder aber so wie er im String[] abgespeichert ist. Leerzeichen am Anfang, Leerzeichen <br>
	    * nach Tabs sowie leere Zeilen werden vor dem Zusammensetzten entfernt-
	    * <br><br>
	    * Array mit Strings @param codeStrings<br>
	    * Zufällige Zusammensetung @param random<br>
	    * Tabbreite @param tab<br>
	    * Zusammengesetzter String @return<br>
	    */
	   
	   
	   public void updateDescription(String projectname, String description){
		   try{
			   Statement stmt = conn.createStatement();
			   stmt.execute("UPDATE Projects SET Description='"+description+"' WHERE pName='"+projectname+"'");
		   }
		   catch(SQLException e1){
			   e1.printStackTrace();
			   //TODO: Auto generated Method stub 
			   }
	   }
	   
	   /**
	    * Projekt umbenennen (wenn der Projektname geändert werden soll)<br>
	    * Alter Projektname@param oldName
	    * Neuer Projektname@param newName
	    */
	   public void renameProject(String oldName, String newName){
		   //System.out.println("renameProject");
		   Statement stmt;
		   try {
			   stmt = conn.createStatement();
			   stmt.execute("UPDATE Projects SET pName='"+newName+"' WHERE pName='"+oldName+"'");
			   stmt.execute("RENAME TABLE "+oldName+"TO"+newName);
		   } catch (SQLException e) {
			   // TODO Auto-generated catch block
			   e.printStackTrace();
		   }		   
	   }
	
	   
	   
	   
	   
	   public void saveProjectSettings(String projectname, int tabSize, int grade) {
		   
		   try {
			   //System.out.println("saveProjectSettings");
			   Statement stmt = conn.createStatement();
			   stmt.executeUpdate("UPDATE Projects SET tabSize="+tabSize+" WHERE pName='"+projectname+"'");
		   		} catch (SQLException e) {
		   			// XXX: Auto-generated catch block
		   			e.printStackTrace();
		   		}
		}
	   public void saveJUnitTest (String projectname, String jUnitCode) {
		   Statement stmt;
		   try {
			   stmt = conn.createStatement();
			   stmt.executeUpdate("UPDATE Projects SET jUnitCode='"+jUnitCode+"' WHERE pName='"+projectname+"'");
		   		} catch (SQLException e) {
		   			if(e.getSQLState().equals("42X14")){ // 42X14: 'JUNITCODE' is not a column in table or VTI 'APP.PROJECTS'
		   				try {
							stmt = conn.createStatement();
							stmt.executeUpdate("ALTER TABLE Projects ADD jUnitCode varchar(1000)");
						} catch (SQLException e1) {
						}
		   			}
		   			// XXX: Auto-generated catch block
		   			e.printStackTrace();
		   		}			
		}
	   
	   
	   //-------------------------- Code aus Datenbank auslesen -------------------------------	   
	   
	   public String getCode(String projectname) throws SQLException{

		   //System.out.println("getcode");
		   String codeString = new String();
		   Statement stmt = conn.createStatement();
		   ResultSet te = stmt.executeQuery("SELECT codeLine FROM "+projectname);
		  
		   while(te.next()){
			   //XXX evtl. muss noch ein leerzeichen eingefügt werden
			  codeString = codeString + te.getString("codeLine")+"\n";
		   }
		   //stmt.close();
		   //System.out.println(te.getString("Code"));
		   return codeString;  
	   }
	   
//	   public String[] getCodeArray(String projectname) throws SQLException{
//
//		   //System.out.println("getcodearray");
//		   Statement stmt = conn.createStatement();
//		   ResultSet te = stmt.executeQuery("SELECT codeLine FROM "+projectname);
//		   ArrayList<String> codeString = new ArrayList<String>();
//		   
//		   while(te.next()){
//			   //XXX evtl. muss noch ein leerzeichen eingefügt werden
//			  codeString.add(te.getString("codeLine"));
//		   }
//		   
//		   //stmt.close();
//		   //System.out.println(te.getString("Code"));
//		   return codeString.toArray(new String[0]);
//	   }
	   
	   public ArrayList<Integer> getRandomKeys(String projectname){
		   ArrayList<Integer> randomKeys = new ArrayList<Integer>();
		   Statement stmt;
		try {
			stmt = conn.createStatement();
		
		   ResultSet rk = stmt.executeQuery("SELECT randomKey FROM "+projectname);
		  
		   while(rk.next()){
			   randomKeys.add(rk.getInt("randomKey"));
		   }
		   return randomKeys;
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return randomKeys;
		}
	   }
	   
	   public ArrayList <String> getCodeList(String projectname) throws SQLException{
		   //System.out.println("getcodelist");
		   Statement stmt = conn.createStatement();
		   
		   ResultSet te = stmt.executeQuery("SELECT codeLine FROM "+projectname);
		   
		   ArrayList<String> codeliste = new ArrayList<String>();
		   
		   while (te.next()) { 
			   codeliste.add(te.getString("codeLine"));
			     }
		   // String[] codeString;
		   // codeString= codeliste.toArray(new String[0]);
		   // return codeString;
		   //stmt.close();
		   return codeliste;
	   }
	   
	   
//	   fügt eine einzelne neue Reihenfolge hinzu
	   public boolean addOrder(String projectname, Vector<Integer> neworder){
		   boolean success = false;
		   try {
			Statement stmt = conn.createStatement();
			// Hinzufügen der neuen Spalte order_X 
			
			int ordernumber=0;
			for(int i = 0;!success;i++){
			   try{
				   stmt.executeUpdate("ALTER TABLE "+projectname+" ADD COLUMN order_"+i+" int");
				   success = true;
				   ordernumber=i;
			   }catch(Exception e){}
			}
			
			for(int j = 0; j < neworder.size();j++){
				   stmt.executeUpdate("UPDATE "+projectname+" SET "
				   		+ "order_"+ordernumber+"="+neworder.get(j).intValue()+""
				   		+ "WHERE lineKey="+j);
				   
			   }
			
		} 	catch (SQLException e1) {
				// TODO Auto-generated catch block
			System.out.println("es gab ein Problem bei addOrder.");
				e1.printStackTrace();
			}
		return success;
	   }
	   
	   
//	   gibt eine reinfolge mit der nummer ordernumber zurück
	   public Vector<Integer> getOrder(String projectname, int ordernumber){
		   Statement stmt;
		   Vector<Integer> order = new Vector<Integer>();
		   try {
			stmt = conn.createStatement();
		
		   ResultSet rs = stmt.executeQuery("SELECT order_"+ordernumber+" FROM "+ projectname);	
		   while(rs.next()){
			   order.add(rs.getInt("order_"+ordernumber));
		   }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		} 
		   return order;
	   }
	   
	   
//	   löscht eine Reihenfolge an der stelle ordernumber
	   public boolean deleteOrder(String projectname, int ordernumber){
		   Statement stmt;
		  try {
			stmt = conn.createStatement();
		
		   stmt.executeUpdate("ALTER TABLE "+projectname+" DROP COLUMN order_"+ordernumber);	
		   return true;
		   
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		} 
	   }
	   
	   
	   /**
	    * Holt sich eine Projektliste aus der Datenbank.<br>
	    * Es könnten Projekte einer bestimmten Klassenstufe <br>
	    * abgefragt werden (müssen aber nicht).
	    * ProjektListe@return
	 * @throws SQLException 
	    */
	   public ArrayList <String> getProjects(int grade) throws SQLException {
		   //System.out.println("getprojects");
		   Statement stmt;
		   ResultSet te;
		   try {
			   stmt = conn.createStatement();
			   te = stmt.executeQuery("SELECT pName FROM Projects");
			   ArrayList<String> projectList = new ArrayList<String>();
			   while (te.next()) { 
				   projectList.add(te.getString("pName"));
				     }
			   //stmt.close();
			   return projectList;
			   
		   } catch (SQLException e1) {
			   try{
				   this.createTable_Projects();
				   return this.getProjects(grade);
			   } catch(SQLException e2){
				   throw(e2);
			   }
		   // String[] codeString;
		   // codeString= codeliste.toArray(new String[0]);
		   // return codeString;
		   }
	   	}
	   
	   /**
	    * Liefert zum übergenenen Projektnamen die Projektbeschreibung.
	    * Projektname@param projectname
	    * Beschreibung@return
	    * @throws SQLException
	    */
	   public String getProjectDescription(String projectname) throws SQLException{

		   //System.out.println("getprojectdescription");
		   Statement stmt;
		   ResultSet te;
		   stmt = conn.createStatement();
		   te = stmt.executeQuery("SELECT Description FROM Projects WHERE pName='"+projectname+"'");
		   te.next();
		   //stmt.close();
		   return te.getString("Description");
	   }
	   public int getTabSize(String projectname) {
		   Statement stmt;
		   ResultSet te;
		   try {
			stmt = conn.createStatement();
			te = stmt.executeQuery("SELECT tabsize FROM Projects WHERE pName='"+projectname+"'");
			   te.next();
			   //stmt.close();
			   return te.getInt("tabsize");
		   } catch (SQLException e) {
			   // TODO Auto-generated catch block
				e.printStackTrace();
			}
		   return 0;
		   
		}
	   protected void createTable_Projects() throws SQLException{
		   Statement stmt = conn.createStatement();
		   // TODO: Allert soll erfolgen, der abfrägt, ob dies das erst Projekt ist, das man erstellt. Erst bei ja soll DROP TABLE erfolgen (sonst werden eventuell vorhandenen DAten verworfen)
		   try{stmt.executeUpdate("DROP TABLE Projects");
		   //stmt.close();
		   }
		   catch(SQLException e){}
		   stmt.executeUpdate("CREATE TABLE Projects ( " +
		   		"pName varchar("+length_projectName+") UNIQUE, " +   		
		   		"imports varchar(100) , " +  
		   		"description varchar("+length_projectDescription+"), " +
		   		"tabSize INT)");
		   
		   //stmt.close();
	   }
	   
	   
	   /**
	    * Schülertabelle wird verworfen, falls vorhanden und neu kreiert.<br>
	    * <b>!!!ACHTUNG!!!</b> Löscht eventuell vorhanenden Daten!
	    * @throws SQLException
	    */
	   protected void createTable_Students() throws SQLException{
		   Statement stmt = conn.createStatement();
		   try{stmt.executeUpdate("DROP TABLE students");
		   //stmt.close();
		   }
		   catch(SQLException e){}
		   stmt.executeUpdate("Create table students (" +
		   		"username varchar(30) UNIQUE, " +
		   		"password varchar(8) NOT NULL default 'student')");  
		   // insert 2 rows
		   stmt.executeUpdate("insert into students values ('tom','tom')");
		   stmt.executeUpdate("insert into students values ('peter','peter')");
		   //stmt.close();
	   }
	   
	   /**
	    * Lehrertabelle wird verworfen, falls vorhanden und neu kreiert.<br>
	    * <b>!!!ACHTUNG!!!</b> Löscht eventuell vorhanenden Daten!
	    * @throws SQLException
	    */
	   protected void createTable_Teachers() throws SQLException{
		   Statement stmt = conn.createStatement();
		   try{stmt.executeUpdate("DROP TABLE teachers");
		   //stmt.close();
		   }
		   catch(SQLException e){}
		   stmt.executeUpdate("CREATE TABLE teachers (" +
		   		"username varchar(30) UNIQUE, " +
		   		"password varchar(8)  NOT NULL default 'teacher')");
		   stmt.executeUpdate("insert into teachers values ('Herr','Herr')");
		   stmt.executeUpdate("insert into teachers values ('Frau','Frau')");
		   stmt.executeUpdate("insert into teachers values ('TUM','TUM')");
		   //stmt.close();
	   }
	   
	   /**
	    * <b>!!!ACHTUNG!!!</b> Löscht ALLE vorhanenden Daten!
	    * @throws SQLException
	    */
	   public void resetAll() throws SQLException{
		   createTable_Projects();
		   createTable_Students();
		   createTable_Teachers();
	   }

	   /**
	    * Löscht das angegebene Projekt.
	    * @param projectname
	    * @return
	    */
	   public boolean delete(String projectname) {
		   Statement stmt;
		   try{
			   stmt = conn.createStatement();
			   stmt.executeUpdate("Drop Table "+projectname);
			   stmt.executeUpdate("DELETE FROM Projects WHERE pName = '"+projectname+"'");
			   //stmt.close();	
			   return true;
		   } catch(SQLException e){
			   if(e.getSQLState().equals("42X05") || e.getSQLState().equals("42Y55")){// Table/View '<objectName>' does not exist.
				   try{
					   stmt = conn.createStatement();
					   stmt.executeUpdate("DELETE FROM Projects WHERE pName = '"+projectname+"'");
					   return true;
				   }
				   finally{ return false; }
			   }
			   else{
				   e.printStackTrace();
				   return false;
			   }
		   }
	   }

	   /**
	    * Gibt Aufschluss, ob ein Projekt mit diesem Namen existiert. 
	    * @param projectName
	    * @return
	    */
	   public boolean projectExists(String projectName) {
			try{
				Statement stmt = conn.createStatement();
				ResultSet rs=stmt.executeQuery("SELECT * FROM Projects WHERE pName='"+projectName+"'");
				rs.next();
				if(rs.getString("pName").equals("")){
					//stmt.close();
					return false;
				}
			}
			catch(SQLException e){
				if(e.getSQLState().equals("24000")) // Invalid cursor state - no current row
					return false;
				else
					e.printStackTrace();
			}
			return true;
	   }

	public String getJUnitCode(String projectName) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs=stmt.executeQuery("SELECT junitcode FROM Projects WHERE pName='"+projectName+"'");
		rs.next();
		return rs.getString("junitcode");
	}
}