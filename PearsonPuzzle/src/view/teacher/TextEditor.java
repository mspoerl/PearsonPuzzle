package view.teacher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import view.JView;

import controller.Controller;
import model.Model;

public class TextEditor extends JView{
	private JButton save;
	//private JEditorPane editorPane;
	private JTextArea textArea;
	private JTextField projectName;
	private ArrayList <JTextField> configFields;
	private ArrayList <JMenuItem> menuItems;

	public TextEditor(Model model) {
		super(model);
		save=new JButton("Projekt speichern");
		configFields = new ArrayList <JTextField>();
		menuItems = new ArrayList <JMenuItem>();
		setupMenu();
		setupTextEditor();
		setupConfigPanel();
		draw();
	}
	/**
	 * Text Editor Panel wird definiert
	 */
	// TODO: Es muss noch eine Prüfung erfolgen, ob vom Nutzer Html Tags eingegeben wurden!!!!
	private void setupTextEditor() {
		//editorPane = new JEditorPane("text/html", model.getProjectHtml());
		//editorPane.setEditable(true);
		//editorPane.setPreferredSize(new Dimension(400,300));
		//JScrollPane editorScrollPane = new JScrollPane(editorPane);
		//editorScrollPane.setVerticalScrollBarPolicy(
		//                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//editorScrollPane.setPreferredSize(new Dimension(400,300));
		//mainPanel.add(editorScrollPane, BorderLayout.CENTER);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		topPanel.add(new JLabel("Projekt: "));
		projectName = new JTextField(model.getProjectName(),15);
		topPanel.add(projectName);
		
		textArea = new JTextArea(model.getProjectCode());
		textArea.setEditable(true);
		textArea.setLineWrap(false);
		textArea.setTabSize(model.getTabSize());
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textScrollPane.setPreferredSize(new Dimension(400,300));
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(textScrollPane, BorderLayout.WEST);		
	}
	
	/**
	 * Menü wird definiert
	 * TODO: sollte ausgelagert werrden
	 * 
	 */
	private void setupMenu(){
		this.menuBar = new JMenuBar();
		JMenu mainMenu = new JMenu("Datei");
		JMenu projectMenu = new JMenu("Projekt");
		menuItems.add(new JMenuItem("Neues Projekt"));
		menuItems.get(menuItems.size()-1).setActionCommand("newProject");
		menuItems.add(new JMenuItem("Projekte anzeigen"));
		menuItems.get(menuItems.size()-1).setActionCommand("projectList");
		menuItems.add(new JMenuItem("Klassen verwalten"));
		menuItems.get(menuItems.size()-1).setActionCommand("editClass");
		menuItems.add(new JMenuItem("Logout"));
		menuItems.get(menuItems.size()-1).setActionCommand("logout");
		menuBar.add(mainMenu);
		menuBar.add(projectMenu);
		for(JMenuItem menuItem: menuItems){
			mainMenu.add(menuItem);
		}
		frame.setJMenuBar(menuBar);
	}	
	/**
	 * Konfigurationsmenü (linke Seite) wird definiert 
	 */
	private void setupConfigPanel(){
		JPanel configPanel = new JPanel();
		//configPanel.setSize(100,100);
		configPanel.setMaximumSize(new Dimension(50,100));		
		configPanel.setLayout(new GridLayout(/*6*/ 0,2, 6,3));

		ArrayList <JLabel> labels = new ArrayList <JLabel>();
		configFields.add(new JTextField(""+textArea.getTabSize()));
		labels.add(new JLabel("Tabbreite"));
		
		//tabWidthLabel.setLabelFor(tabWidth);
		
		//configPanel.add(new JLabel("Tabbreite"), BorderLayout.WEST);		
		configFields.add(new JTextField(""+model.getGrade()));
		labels.add(new JLabel("Klassenstufe"));
		labels.get(labels.size()-1).setToolTipText(new String("<html><p>Mögliche Werte:</p><table><tr><td>0</td><td>undefiniert</td></tr><tr><td>5</td><td>5. Jahrgangsstufe</td></tr><tr><td>6</td><td>6. Jahrgangsstufe</td><tr><td>7</td><td>7. Jahrgangsstufe</td><tr><td>8</td><td>8. Jahrgangsstufe</td><tr><td>9</td><td>9. Jahrgangsstufe</td><tr><td>10</td><td>10. Jahrgangsstufe</td><tr><td>11</td><td>11. Jahrgangsstufe</td><tr><td>12</td><td>12. Jahrgangsstufe</td></tr><tr><td>13</td><td>13. Jahrgangsstufe</td></tr><table><html>"));
		
		for(int i=0; i<labels.size() && i<configFields.size();i++){
			configPanel.add(labels.get(i));
			configPanel.add(configFields.get(i));
		}
		JLabel randomLabel = new JLabel("Zufallsmodus");
		JToggleButton randomButton= new JToggleButton();
		//randomButton.setSize(5, 5);
		randomButton.setSelected(model.getRandomMode());
		String toolTip = new String("<html>Wird dieser Haken gesetzt, <br> wird aus dem eingegebenen Text <br> per Zufallsmodus ein Puzzletext erstellt</html>");
		randomLabel.setToolTipText(toolTip);
		randomButton.setToolTipText(toolTip);
		configPanel.add(randomLabel);
		configPanel.add(randomButton);
		// TODO: Layoutanpassung: der folgende Abschnitt sollte noch durch einen angemessenen Platzhalter ersetzt werden
		for(int i = 0; i<20; i++){
		configPanel.add(Box.createHorizontalStrut(10));
		}
		configPanel.add(save);
		mainPanel.add(configPanel, BorderLayout.EAST);		
	}
	
	/**
	 * Für den Contoller, um bei Bedarf auf die Einstellungen zuzugreifen
	 * @return
	 */
	public ArrayList<JTextField> getInputComponents(){
		return configFields;
	}
	public String getProjectName(){
		return projectName.getText();
	}
	public void addController(Controller controller){
		save.addActionListener(controller);
		save.setActionCommand("saveProject");
		for(JTextField comp: configFields){
			comp.addActionListener(controller);
		}
		for(JMenuItem menuItem : menuItems){
	    	menuItem.addActionListener(controller);
	    }
	}
	
	/**
	 * Für den Controller, um Texteingaben an das Model weitergeben zu können.
	 * @return
	 */
	public String getCode() {
		return textArea.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void update() {
		configFields.get(0).setText(""+model.getTabSize());
		configFields.get(1).setText(""+model.getGrade());
		textArea.setText(model.getProjectCode());
		textArea.setTabSize(model.getTabSize());
		this.draw();
		// TODO Auto-generated method stub	
	}
}
