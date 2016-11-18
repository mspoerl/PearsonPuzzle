package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import controller.Controller;
import model.Model;

public class TextEditor extends JView{
	private JMenu menu;
	private JButton save;
	private JEditorPane editorPane;
	public TextEditor(Model model) {
		super(model);
		menu = new JMenu("Datei");
		save=new JButton("Projekt speichern");
		setupMenu();
		setupTextEditor();
		draw();
	}
	// TODO: Es muss noch eine Prüfung erfolgen, ob vom Nutzer Html Tags eingegeben wurden!!!!
	private void setupTextEditor() {
		editorPane = new JEditorPane("text/html", model.getProjectHtml());
		editorPane.setEditable(true);
		editorPane.setPreferredSize(new Dimension(400,300));
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setPreferredSize(new Dimension(400,300));
		mainPanel.add(editorScrollPane, BorderLayout.CENTER);
		mainPanel.add(save, BorderLayout.SOUTH);
		
	}
	private void setupMenu(){
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
		JMenuItem enterProject = new JMenuItem("Projekte anzeigen");
		JMenuItem newProject = new JMenuItem("Projekt anlegen");
		JMenuItem enterClass = new JMenuItem("Klassen verwalten");
		JMenuItem logout = new JMenuItem("logout");
		menu.add(enterProject);
		menu.add(newProject);
		menu.add(enterClass);
		menu.add(logout);
		frame.setJMenuBar(menuBar);
	}
	public void addController(Controller controller){
		save.addActionListener(controller);
		save.setActionCommand("saveProject");
	}
	
	public String getCode() {
		return editorPane.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quitView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
