package controller;

/**
 * Definiert globale Kommandos für ActionEvents u.Ä.<br>
 * (über deren lokale Wohldefiniertheit der Controller entscheidet)
 * @author workspace
 *
 */
public enum DCCommand {
	SubmitPassword, EditProject, EditUsers, EditJUnit, AddUser, NewProject, OpenProject, ProjectList, Save, Logout, 
	DeleteProject, Admin, ResetDB, SetConfig, ConfigureProject, ConnectedComponent, StartGroupSelection, CancelGroupSelection, Compile, TestCode, SaveProjectConfiguration, SaveGroupSelection, DeleteOrder;
	
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
		case SubmitPassword:
			return "SubmitPassword";
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
		case ConfigureProject:
			return "ConfigureProject";
		case ConnectedComponent:
			return "ConnectedComponent";
		case StartGroupSelection:
			return "StartGroupSelection";
		case Save:
			return "Save";
		case SaveProjectConfiguration:
			return "SaveProjectConfiguration";
		case EditJUnit:
			return "EditJUnit";
		case CancelGroupSelection:
			return "CancelGroupSelection";
		case Compile:
			return "Compile";
		case TestCode: 
			return "TestCode";
		default:
			return "DoNothing";
		}
	}
}
