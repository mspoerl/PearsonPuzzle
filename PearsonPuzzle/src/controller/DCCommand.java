package controller;

/**
 * Definiert globale Kommandos für ActionEvents u.Ä.<br>
 * (über deren lokale Wohldefiniertheit der Controller entscheidet)
 * @author workspace
 *
 */
public enum DCCommand {
	submitPassword, editProject, newProject, openProject, projectList, saveChanges, saveProject, logout, deleteProject, admin, resetDB, setConfig;
	
	@Override
	public String toString(){
		switch(this){
		case editProject:
			return "editProject";
		case newProject:
			return "newProject";
		case openProject:
			return "openProject";
		case projectList:
			return "projectList";
		case saveChanges:
			return "saveChanges";
		case saveProject:
			return "saveProject";
		case submitPassword:
			return "submitPassword";
		case logout:
			return "logout";
		case deleteProject:
			return "deleteProject";
		case admin:
			return "admin";
		case resetDB: 
			return "resetDB";
		case setConfig:
			return "setConfig";
		default:
			return "doNothing";
		}
	}
}
