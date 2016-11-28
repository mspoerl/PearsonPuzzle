package controller;

public enum DCCommand {
	submitPassword, editProject, newProject, openProject, projectList, saveChanges, saveProject, logout;
	
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
		default:
			return "doNothing";
		}
	}
	
}
