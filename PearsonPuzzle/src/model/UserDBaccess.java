package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDBaccess {

	 Connection conn;
	 private static final int length_projectName = 36;
	 private static final int length_projectDescription = 1024;
	 private final static int MAX_line_length_Code = 8000;
	 private final static int MIN_line_length_Code = 10;
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
	   
	   
	  
	   
	   // Nutzername und Passwort Vergleich Schüler
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
			   // Hier wird abgehandelt, wenn was schief läuft
			   // bzw. wenn die Tabelle (noch) nicht existiert 
			   try {
				   stmt.executeUpdate("Drop Table students");
			   } 
			   catch (SQLException ex) { // schmeißt keine Exception, da nur der Fall, dass bereits eine Tabelle existiert, abgefangen werden soll
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
			   // Hier wird abgehandelt, wenn was schief läuft
			   // bzw. wenn die Tabelle (noch) nicht existiert 
			   try {
				   stmt.executeUpdate("Drop Table teachers");
			   } 
			   catch (SQLException ex){ // Schmeißt keine Exception, da nur der Fall, dass keine Tabelle existiert, abgefangen werden soll.
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
	

	   public void saveProject(String[] codeString, String projectname, int linelength, final int projectID) throws SQLException{
		   if(linelength < 1){
			   linelength =MIN_line_length_Code;
		   }
		   else if(linelength >8000){
			   linelength = MAX_line_length_Code;
		   }
		   Statement stmt = conn.createStatement();
		   //XXX doppelte Tabellennamen besser handeln
		   /*	erster Versuch hierfür:
		   		try{
					   stmt.executeQuery("SELECT * FROM projekte");}
				   catch(Exception e1){
					   try{
						   stmt.executeUpdate("CREATE TABLE projekte (ID int primary key NOT NULL, projekt varchar("+length_projectName+"), klassenstufe int, beschreibung varchar("+length_projectDescription+") )");}
					   catch(Exception e2){}
				   }		
		   */
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
	   
	   
	   //-------------------------- Code aus Datenbank auslesen -------------------------------
	   public ArrayList <String> getProject(String projectname) throws SQLException{
		   Statement stmt = conn.createStatement();
		   
		   ResultSet te = stmt.executeQuery("SELECT codeline FROM "+projectname);
		   
		   ArrayList<String> codeliste = new ArrayList<String>();
		   
		   while (te.next()) { 
			   codeliste.add(te.getString("codeline"));
			     }
//		   Object[] codeString = new String[codeliste.size()];
//		   codeString=codeliste.toArray();
////		   String[] stringArray = new String[codeliste.size()];
////		   stringArray=(String[]) codeliste.toArray();
//		   for(int j =0;j<codeString.length;j++){
//			   System.out.println("Array"+codeString[j]);
//		   }
		   return codeliste;
	   }
	   
	}




/*
public class UserDBaccess {

	 Connection conn;
	 private static final int length_projectName = 36;
	 private static final int length_projectDescription = 1024;
	 private final static int MAX_line_length_Code = 8000;
	 private final static int MIN_line_length_Code = 10;
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
	   
	   
	  
	   
	   // Nutzername und Passwort Vergleich Schüler
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

		   //Student
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
	       
		   //Teacher
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
	

	   public void saveProject(String[] codeString, String projectname, int linelength, int projectID) throws SQLException{
		   if(linelength < 1){
			   linelength =MIN_line_length_Code;
		   }
		   else if(linelength >8000){
			   linelength = MAX_line_length_Code;
		   }
		   Statement stmt = conn.createStatement();
		   //XXX doppelte Tabellennamen besser handeln
		   
		   // Tabelle PROJEKTE 
		   try{
			   stmt.executeQuery("SELECT * FROM projekte");}
		   catch(Exception e1){
			   try{
				   stmt.executeUpdate("CREATE TABLE projekte (ID int primary key NOT NULL, projekt varchar("+length_projectName+"), klassenstufe int, beschreibung varchar("+length_projectDescription+") )");}
			   catch(Exception e2){}
		   }		   
		   
		   
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
//
//			public void saveProject(String[] codeString, String projectname, int linelength) throws SQLException{
//				   Statement stmt = conn.createStatement();
//				   
//				   //XXX doppelte Tabellennamen besser hendeln 
//				   try{
//					   stmt.executeUpdate("Create table "+projectname+"mixed (linenumber int primary key, codeline varchar("+linelength+"))");
//				   }
//				   catch(Exception e){ //falls Tabelle bereits existiert
//					   stmt.executeUpdate("DROP TABLE "+projectname);
//					   stmt.executeUpdate("CREATE TABLE "+projectname+" (linenumber int primary key, codeline varchar("+linelength+"))");
//				   }
//				   for(int i=0 ; i<codeString.length ; i++){
//						 stmt.executeUpdate("insert into "+projectname+" values ("+i+",'"+codeString[i]+"')");
//				   }
//				   
//				   
//				   String buffer;
//					for(int i=codeString.length-1; i>0;i--){
//						int randomInt = new java.util.Random().nextInt(i);
//						buffer=codeString[randomInt];
//						codeString[randomInt]=codeString[i];
//						codeString[i]=buffer;}
//				   
//					try{
//						   stmt.executeUpdate("Create table "+projectname+" (linenumber int primary key, codeline varchar("+linelength+"))");
//				   }
//				   catch(Exception e){
//					   stmt.executeUpdate("DROP TABLE "+projectname+"mixed");
//					   stmt.executeUpdate("CREATE TABLE "+projectname+"mixed (linenumber int primary key, codeline varchar("+linelength+"))");
//					   
//				   }
//					
//					for(int i=0 ; i<codeString.length ; i++){
//						 stmt.executeUpdate("insert into "+projectname+"mixed values ("+i+",'"+codeString[i]+"')");
//				   }
//		   
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
	   
	   
	   //-------------------------- Code aus Datenbank auslesen -------------------------------
	   public ArrayList <String> getProject(String projectname) throws SQLException{
		   Statement stmt = conn.createStatement();
		   
		   ResultSet te = stmt.executeQuery("SELECT codeline FROM "+projectname);
		   
		   ArrayList<String> codeliste = new ArrayList<String>();
		   
		   while (te.next()) { 
			   codeliste.add(te.getString("codeline"));
			     }
//		   Object[] codeString = new String[codeliste.size()];
//		   codeString=codeliste.toArray();
////		   String[] stringArray = new String[codeliste.size()];
////		   stringArray=(String[]) codeliste.toArray();
//		   for(int j =0;j<codeString.length;j++){
//			   System.out.println("Array"+codeString[j]);
//		   }
		   return codeliste;
	   }
	   
	}
*/
