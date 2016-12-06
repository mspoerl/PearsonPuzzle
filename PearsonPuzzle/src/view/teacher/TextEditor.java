package view.teacher;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import view.JView;

import controller.Controller;
import model.Model;

/**
 * View, der dem Lehrer das grafische Bearbeiten von Proekten ermöglicht.
 * @author workspace
 */
public class TextEditor extends JView{
	
	private JButton save;
	//private JEditorPane editorPane;
	private JTextArea textArea;
	private final static String defaultCode = "Hier müssen Sie den darzustellenden Inhalt einfügen";
	private JTextArea description;
	private final static String defaultDescription = "Hier können Sie eine kurze Beschreibung angeben";
	private JTextField projectName;
	private ArrayList <JTextField> configFields;

	public TextEditor(Model model) {
		super(model);
		save=new JButton("Projekt speichern");
		configFields = new ArrayList <JTextField>();
		menu = new MenuTeacher();
		this.addMenuToFrame(menu);
		
		textArea = new JTextArea(model.getProjectCode());
		if(textArea.getText().length()==0){
			textArea.setText(defaultCode);
		}
		
		description = new JTextArea(defaultDescription);
		
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
		projectName.setPreferredSize(new Dimension(200,25));
		topPanel.add(projectName);
		
		textArea.setEditable(true);
		textArea.setLineWrap(false);
		textArea.setTabSize(model.getTabSize());
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textScrollPane.setPreferredSize(new Dimension(400,350));
		textScrollPane.setMinimumSize(new Dimension(400,100));
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(textScrollPane, BorderLayout.WEST);		
	}
	
		
	/**
	 * Konfigurationsmenü (linke Seite) wird definiert 
	 */
	private void setupConfigPanel(){
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
		leftPanel.setPreferredSize(new Dimension(300,350));
		JPanel configDiv = new JPanel();
		JPanel configPanel = new JPanel();
		//configPanel.setSize(100,100);
		configPanel.setMaximumSize(new Dimension(50,100));		
		configPanel.setLayout(new GridLayout(/*6*/ 0,2, 6,3));

		ArrayList <JLabel> labels = new ArrayList <JLabel>();
		configFields.add(new JTextField(""+textArea.getTabSize()));
		labels.add(new JLabel("Tabbreite"));
		
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
		
		configPanel.add(Box.createHorizontalStrut(10));
		configDiv.add(configPanel);
		//configPanel.add(save);
		
		leftPanel.add(configDiv);
		
		JPanel descriptionPanel=new JPanel(new BorderLayout());
		
		
		description.setPreferredSize(new Dimension(300,50));
		descriptionPanel.add(new JLabel("Projektbeschreibung"), BorderLayout.BEFORE_FIRST_LINE);
		descriptionPanel.add(description, BorderLayout.CENTER);
		descriptionPanel.add(save, BorderLayout.AFTER_LAST_LINE);
		leftPanel.add(descriptionPanel);
		//leftPanel.add(configPanel);
		mainPanel.add(leftPanel, BorderLayout.EAST);		
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
		menu.addActionListener(controller);
	}
	
	/**
	 * Für den Controller, um Texteingaben an das Model weitergeben zu können.
	 * @return
	 */
	public String getCode() {
		if(textArea.getText().equals(defaultCode)){
			return "";
		}
		return textArea.getText();
	}
	public String getDescription(){
		if(description.getText().equals(defaultDescription)){
			return "";
		}
		return description.getText();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void update() {
		configFields.get(0).setText(""+model.getTabSize());
		configFields.get(1).setText(""+model.getGrade());
		textArea.setTabSize(model.getTabSize());
		this.draw();
		// TODO Auto-generated method stub	
	}
}
