package model.access;


/**
 * Legt die möglichen Benutzergruppen global fest.
 * @author workspace
 *
 */
public enum AccessGroup {
	TEACHER(new Teacher()),
	PUPIL(new Pupil()), 
	UNAUTHORIZED(new Unauthorized());
	
	private AccessInterface accessInterface;
	
	private AccessGroup(AccessInterface accessInterface){
		this.accessInterface=accessInterface;
	}
	
	public AccessInterface object(){
		return accessInterface;
	}
	
	}
