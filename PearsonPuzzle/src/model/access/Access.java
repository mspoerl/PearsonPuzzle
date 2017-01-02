package model.access;

import javax.swing.text.View;

import model.UserDBaccess;

public class Access {
	
	private AccessGroup accessGroup;
	
	public Access(char[] password, String username, UserDBaccess database){
		if (database.lookUpstudent(username, password))
			accessGroup = AccessGroup.PUPIL;
		else if (database.lookUpteacher(username, password))
			accessGroup = AccessGroup.TEACHER;
		else
			accessGroup = AccessGroup.UNAUTHORIZED;
	}
	public Access(){
		accessGroup = AccessGroup.UNAUTHORIZED;
	}
	
	public AccessGroup getAccessGroup(){
		return accessGroup;
	}
	public void setAccessGroup(AccessGroup accessGroup){
		return;
	}
	public void verarbeite(){
		accessGroup.object().verarbeite();
	}

}
