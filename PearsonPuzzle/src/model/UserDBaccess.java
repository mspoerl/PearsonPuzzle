package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDBaccess {

	 Connection conn;
	 public UserDBaccess() throws SQLException {
		 String dbUrl = "jdbc:derby:database;create=true";
		 conn = DriverManager.getConnection(dbUrl);
		 this.normalDbUsage();
	 }
	 
	 public void connectionToDerby() {
	     // -------------------------------------------
	     // URL format is
	     // jdbc:derby:<local directory to save data>
	     // -------------------------------------------
	     
	 }
	 
	   
	   
	   // ------------------------ Nutzername und Passwortvergleich -------------------------------
	   
	   
	  
	   
	   // Nutzername und Passwort Vergleich SchÃ¼ler
	   public boolean lookUpstudent(String name, char[] password){
		   try{	  
			   Statement stmt = conn.createStatement();
				   			     
			   	//password char[] to String
			   String passwordstring = new String(password);
			   
			     // query
			     ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE username= '" + name + "' AND password = '"+passwordstring+"'");	
			     if(!rs.next() ){
			    	 return false;
			     }
			     else{
			    	 return true;
			     }		     
		   }
		   catch(Exception e){
			   return false;
		   }
	   }
	   
	   //Nutzername und Passwort Vergleich Lehrer
	   public boolean lookUpteacher(String name, char[] password) {
		   try{
			   	//password char[] to String
			   String passwordstring = new String(password);
			   
			     Statement stmt = conn.createStatement();
			     // query
			     ResultSet te = stmt.executeQuery("SELECT * FROM teachers WHERE username = '" + name + "' AND password = '"+passwordstring+"'");	
			     if(!te.next() ){
			    	 return false;
			     }
			     else{
			    	 return true;
			     }
		   }
		   catch(Exception e){
			   return false;
		   }
		   
	   }
	   

	// ------------------------ Tabellen in Datenbank anlegen (hardcoded) -------------------------------

	   private void normalDbUsage() throws SQLException {
		   Statement stmt = conn.createStatement();

		   /*Student*/
		   ResultSet rsS;
		   try{
			   // query
			   rsS = stmt.executeQuery("SELECT * FROM students");
		   }
		   catch(SQLException e){
			   // Hier wird abgehandelt, wenn was schief lÃ¤uft
			   // bzw. wenn die Tabelle (noch) nicht existiert 
			   try {
				   stmt.executeUpdate("Drop Table students");
			   } 
			   catch (SQLException ex) { // schmeiÃŸt keine Exception, da nur der Fall, dass bereits eine Tabelle existiert, abgefangen werden soll
			   }
			   // create table 
			   stmt.executeUpdate("Create table students (id int primary key, username varchar(30), password varchar(8) NOT NULL default 'student')");  
			   // insert 2 rows
			   stmt.executeUpdate("insert into students values (1,'tom','tom')");
			   stmt.executeUpdate("insert into students values (2,'peter','peter')");
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
			   // Hier wird abgehandelt, wenn was schief lÃ¤uft
			   // bzw. wenn die Tabelle (noch) nicht existiert 
			   try {
				   stmt.executeUpdate("Drop Table teachers");
			   } 
			   catch (SQLException ex){ // SchmeiÃŸt keine Exception, da nur der Fall, dass keine Tabelle existiert, abgefangen werden soll.
			   }   
			   // create table
			   stmt.executeUpdate("CREATE TABLE teachers (id int primary key, username varchar(30), password varchar(8)  NOT NULL default 'teacher')");
			   // insert 2 rows
			   stmt.executeUpdate("insert into teachers values (1,'Herr','Herr')");
			   stmt.executeUpdate("insert into teachers values (2,'Frau','Frau')");
			   stmt.executeUpdate("insert into teachers values (3,'TUM','TUM')");
			   // query
			   rsT = stmt.executeQuery("SELECT * FROM teachers");
		   }
		   // print out query result
		   while (rsT.next()) { 
			   //System.out.printf("%d\t%s\t%s\n", rs.getInt("id"), rs.getString("username"), rs.getString("password"));			   
		   }
	   }
	   
	   
	// ------------------------ Projekt in Datenbank einspeichern -------------------------------
	

	   public void saveProject(String[] codeString, String projectname, int linelength) throws SQLException{
		   Statement stmt = conn.createStatement();
		   
		   
		   // Projektnamen Tabelle
		   try{
			   stmt.executeUpdate("CREATE TABLE Projects ( Projectname varchar(40) UNIQUE)");
		   }
		   catch(Exception e){ //falls Tabelle bereits existiert
			   }
		   
		   try{stmt.execute("INSERT INTO Projects VALUES ('"+projectname+"')");
		   }
		   catch(Exception e){// TODO Projektname bereits vorhanden
		   }
		   
		   
		   
		   
		   //XXX doppelte Tabellennamen besser handeln
		   
		   
		   
		   
		   // Speichert normalen Code
		   try{
			   stmt.executeUpdate("DROP TABLE "+projectname);
		   }
		   catch(Exception e){ //falls Tabelle noch nicht existiert
		   }
		   stmt.executeUpdate("CREATE TABLE "+projectname+" (linenumber int primary key, codeline varchar("+linelength+"))");
		   for(int i=0 ; i<codeString.length ; i++){
				 stmt.executeUpdate("insert into "+projectname+" values ("+i+",'"+codeString[i]+"')");
		   }
		   
		   // Zeilen zufällig anordnen
		   String buffer;
			for(int i=codeString.length-1; i>0;i--){
				int randomInt = new java.util.Random().nextInt(i);
				buffer=codeString[randomInt];
				codeString[randomInt]=codeString[i];
				codeString[i]=buffer;
			}
			// Speichert zufällig angeordnetetn Code
			try{
				stmt.executeUpdate("DROP TABLE "+projectname+"_mixed");
			}
			catch(Exception e){ // Falls Tabelle noch nicht existiert
			}
			stmt.executeUpdate("CREATE TABLE "+projectname+"_mixed (linenumber int primary key, codeline varchar("+linelength+"))");	
			for(int i=0 ; i<codeString.length ; i++){
				 stmt.executeUpdate("insert into "+projectname+"_mixed values ("+i+",'"+codeString[i]+"')");
			}
/*
			public void saveProject(String[] codeString, String projectname, int linelength) throws SQLException{
				   Statement stmt = conn.createStatement();
				   
				   //XXX doppelte Tabellennamen besser hendeln 
				   try{
					   stmt.executeUpdate("Create table "+projectname+"mixed (linenumber int primary key, codeline varchar("+linelength+"))");
				   }
				   catch(Exception e){ //falls Tabelle bereits existiert
					   stmt.executeUpdate("DROP TABLE "+projectname);
					   stmt.executeUpdate("CREATE TABLE "+projectname+" (linenumber int primary key, codeline varchar("+linelength+"))");
				   }
				   for(int i=0 ; i<codeString.length ; i++){
						 stmt.executeUpdate("insert into "+projectname+" values ("+i+",'"+codeString[i]+"')");
				   }
				   
				   
				   String buffer;
					for(int i=codeString.length-1; i>0;i--){
						int randomInt = new java.util.Random().nextInt(i);
						buffer=codeString[randomInt];
						codeString[randomInt]=codeString[i];
						codeString[i]=buffer;}
				   
					try{
						   stmt.executeUpdate("Create table "+projectname+" (linenumber int primary key, codeline varchar("+linelength+"))");
				   }
				   catch(Exception e){
					   stmt.executeUpdate("DROP TABLE "+projectname+"mixed");
					   stmt.executeUpdate("CREATE TABLE "+projectname+"mixed (linenumber int primary key, codeline varchar("+linelength+"))");
					   
				   }
					
					for(int i=0 ; i<codeString.length ; i++){
						 stmt.executeUpdate("insert into "+projectname+"mixed values ("+i+",'"+codeString[i]+"')");
				   }
		   */
			//-----------------------------testing------------------------------------
		   
		 
		   

//			 ResultSet te = stmt.executeQuery("SELECT * FROM "+projectname);
//		   while (te.next()) { 
//				 System.out.printf("%d\t%s\n", te.getInt("linenumber"), te.getString("codeline"));			   
//			     }
//		   
//		   
//		   
//		   
//
//			 ResultSet me = stmt.executeQuery("SELECT * FROM "+projectname+"mixed");
//		   while (me.next()) { 
//				 System.out.printf("%d\t%s\n", me.getInt("linenumber"), me.getString("codeline"));			   
//			     }
	   }
	   
	   
	   //-------------------------- Projekte aus Datenbank auslesen -------------------------------
	   public ArrayList <String> getProject(String projectname) throws SQLException{
		   Statement stmt = conn.createStatement();
		   
		   ResultSet te = stmt.executeQuery("SELECT codeline FROM "+projectname);
		   
		   ArrayList<String> codeliste = new ArrayList<String>();

		   
		   while (te.next()) { 
			   codeliste.add(te.getString("codeline"));
			     }
		   
		   
//		   String[] codeString;
//		   codeString= codeliste.toArray(new String[0]);
		   
		   return codeliste;
	   }
	   
	   //-------------------------- Projektnamen aus Datenbank auslesen -------------------------------
	   public String[] getNamesofProjects() throws SQLException{
		   Statement stmt = conn.createStatement();
		   
		   //Für ersten aufruf
		   try{
			   stmt.executeUpdate("CREATE TABLE Projects ( Projectname varchar(40) UNIQUE)");
			   stmt.executeUpdate("INSERT INTO Projects values ('FirstTry')");
		   }
		   catch(Exception e){ //falls Tabelle bereits existiert
			   }
		   
		   
		   ResultSet te = stmt.executeQuery("SELECT Projectname FROM Projects");
		   
		   ArrayList<String> codeliste = new ArrayList<String>();

		   
		   while (te.next()) { 
			   codeliste.add(te.getString("Projectname"));
			     }
		   
		   
		   String[] codeString;
		   codeString= codeliste.toArray(new String[0]);
		   
		   return codeString;
	   }
	   
	}

