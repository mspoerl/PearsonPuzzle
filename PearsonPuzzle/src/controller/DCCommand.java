package controller;

/**
 * Definiert globale Kommandos für ActionEvents u.Ä.<br>
 * (über deren lokale Wohldefiniertheit der Controller entscheidet)
 * @author workspace
 *
 */
public enum DCCommand {
	// Text Component Connection
		ConnectedComponent,
	// Button Commands
		ShowHelp, Save, ResetDB, SetConfig, DeleteProject, AddOrder, Compile, TestCode, SaveProjectConfiguration,
	// View Changes
		Login, EditProject, EditUsers, EditJUnit, AddUser, NewProject, OpenProject, ProjectList,  Logout,	Admin, ConfigureProject, DeleteOrder ;
	
	@Override
	public String toString(){
		switch(this){
		case AddUser:
			return "AddUser";
		case EditUsers:
			return "EditUsers";
		case EditProject:
			return "EditProject";
		case NewProject:
			return "NewProject";
		case OpenProject:
			return "OpenProject";
		case ProjectList:
			return "ProjectList";
		case Login:
			return "Login";
		case Logout:
			return "Logout";
		case DeleteProject:
			return "DeleteProject";
		case DeleteOrder:
			return "DeleteOrder";
		case Admin:
			return "Admin";
		case ResetDB: 
			return "ResetDB";
		case SetConfig:
			return "SetConfig";
		case ShowHelp:
			return "ShowHelp";
		case ConfigureProject:
			return "ConfigureProject";
		case ConnectedComponent:
			return "ConnectedComponent";
		case AddOrder:
			return "AddOrder";
		case Save:
			return "Save";
		case SaveProjectConfiguration:
			return "SaveProjectConfiguration";
		case EditJUnit:
			return "EditJUnit";
		case Compile:
			return "Compile";
		case TestCode: 
			return "TestCode";
		default:
			return "DoNothing";
		}
	}
}
