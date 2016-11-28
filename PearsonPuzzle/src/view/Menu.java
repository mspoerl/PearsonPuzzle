package view;

import javax.swing.JMenuBar;

import controller.Controller;

public abstract class Menu extends JMenuBar{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public abstract void addActionListener(Controller controller);
	protected abstract void setupMenu();

}
