package mobileVersion.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Observable;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.Model;

import org.junit.runner.notification.Failure;

import controller.Controller;
import controller.DCCommand;
import controller.transferHandler.ToSaveTransferHandler;
import view.teacher.UnitEditor;

public class CodeSortAView extends AppletView {
	
	private static final long serialVersionUID = -1258749785785828266L;
	private final static int Puzzlemode=0;
	private static final String defaultDescription="Puzzle den Code in die richtige Reihenfolge!\n \nViel Spaß ;-)";
	
	private JList<String> saveDropList;
	private DefaultListModel <String> saveDropModel;
	private JButton compileButton;
	private JButton testButton;
	private JLabel messageBox;
	private Model model;
	private JButton backButton;
	
	
	public CodeSortAView(Model model) {
		super(model);
		this.model = model;
		
		setupCodeLists();
		setupButtons();
	}

	// private Methode, um die Drag and Drop Liste zu konstruieren
	private void setupCodeLists(){
		saveDropModel=new DefaultListModel<String>();
		saveDropList=new JList<String>(saveDropModel);
		
		ToSaveTransferHandler dragDropTransferH = new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemode, model);
		// Einzelne Drag and Drop List (nicht zwei)
				saveDropModel=makeDefaultListModel();
				saveDropList=new JList<String>(saveDropModel);
				dragDropTransferH = new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemode, model);
				saveDropList.setDropMode(DropMode.INSERT);				
				saveDropList.setTransferHandler(dragDropTransferH);
		
		// Linke Liste (Drop)
		saveDropList.setName("dropList");
		saveDropList.setFixedCellHeight(20);
		saveDropList.setDragEnabled(true);
		saveDropList.setMinimumSize(new Dimension(100,100));
		saveDropList.setPreferredSize(new Dimension(100,100));
		JScrollPane scrollPanel_sDL = new JScrollPane(saveDropList);
		scrollPanel_sDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_sDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_sDL.setMinimumSize(new Dimension(100,100));
		scrollPanel_sDL.setPreferredSize(new Dimension(360,300));
		this.add(scrollPanel_sDL, BorderLayout.CENTER);
		
		// Arbeitsanweisung und Ergebnisse
		messageBox = new JLabel(defaultDescription);
//		messageBox=new JTextArea(defaultDescription);
		if(!model.getProjectDescription().trim().equals(""))
			messageBox.setText(model.getProjectDescription());
//		messageBox.setLineWrap(true);
//		messageBox.setWrapStyleWord(true);
//		messageBox.setEditable(false);
		JScrollPane scrollPane_description = new JScrollPane(messageBox);
		scrollPane_description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane_description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane_description.setPreferredSize(new Dimension(650,100));
		this.add(scrollPane_description,BorderLayout.PAGE_END);
	}
	
	/**
	 * Buttoons werden definiert. Hizufügen eines Action Listeners noch notwendig.
	 */
	private void setupButtons(){
		compileButton=new JButton("Kompilieren");
		testButton = new JButton("Test starten");
		backButton = new JButton("Zurück");
		JPanel topPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(compileButton);
		topPanel.add(testButton);
		topPanel.add(backButton);
		this.add(topPanel,BorderLayout.PAGE_START);
	}
	
	/**
	 * Wird vom Controller asugeführt, um Listener, Handler und <br>
	 * Controller hinzuzufügen
	 */
	public void addController(Controller controller){
		// TODO: In den offiziellen Controller auslagern
		//saveDropList.addMouseListener((DefaultController)controller);
		compileButton.addActionListener(controller);
		compileButton.setActionCommand(DCCommand.Compile.toString());
		testButton.setActionCommand(DCCommand.TestCode.toString());
		testButton.addActionListener(controller);
		backButton.setActionCommand(DCCommand.ProjectList.toString());
		backButton.addActionListener(controller);
		//menu.addActionListener(controller);
	}
	@Override
	public void removeController(Controller controller) {
		compileButton.removeActionListener(controller);
		testButton.removeActionListener(controller);
	}
	
	/**
	 * Notwendig, um die Menge der Tabs richtig darzustellen. <br>
	 * Tabs werden hierzu in Leerzeichen umgewandelt.
	 * @param stringList Liste aus dem Model 
	 * @return DefaultListModel
	 */
	private DefaultListModel<String> makeDefaultListModel(){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		
		// Dies ist nötig, um bei JList Elementen die Tabbreite berücksichtigen zu können
		// Steht hier, weil es ein Problem von Swing ist, kein allgemeines Problem
		Vector<String> codeVector = model.getCodeVector(null);
		for(String string : codeVector){
			
			listModel.add(listModel.size(),  string);
		}
		return listModel;
	}

	public void update(Observable arg0, Object arg1) {
		if(arg1==DCCommand.Compile){
			// Fehlerbericht oder Erfolg ausgeben
			Vector<HashMap<String, String>> failures = model.getCompileFailures();
			if(failures.isEmpty())
				messageBox.setText("Kompilieren war erfolgreich!");
			else{
				String failureText = "<html><body>Kompilieren war nicht erfolgreich. <br>Aufgetretenen Fehler: ";
				for(HashMap<String, String> failure : failures){
					failureText = failureText+"<br> "+failure.get("Nachricht")+" in Zeile "+failure.get("Zeile");
				}
				messageBox.setText(failureText+"</body></html>");
			}
			saveDropList.setEnabled(false);
		}
		if(arg1==DCCommand.TestCode){
			String failureText = new String("<html><head><style type=\"text/css\"> .success {color:green;} .failure{color:red;} .increment {margin-left:24px;} .comment {font-style:italic;} .heading{font-style: oblique;}</style> </head><body>");
			if(model.getJUnitCode()!=null && !model.getJUnitCode().isEmpty() && !model.getJUnitCode().equals(UnitEditor.DEFAULT_UNIT_CODE)){
				String cssClass;
				if(model.getjUnitFailures().size()==0)
					 cssClass = " class=\"success\" ";
				else
					cssClass = " class=\"failure\" ";
				failureText+="<span class=\"heading\">Ergebnis des Unit-Test:</u><span"+cssClass+">"+model.getjUnitFailures().size()+" Fehler</span>";
				System.out.println(failureText);
				for(Failure failure: model.getjUnitFailures()){
					System.out.println(failure);
					failureText=failureText+"<div class=\"failure\">"+failure+"</div>";
				}
			}
					
			//failureText = failureText + "<br>";
			for(String key : model.getSuccessMap().keySet()){
				if(key.equals("Gruppentests") || key.contains("Reihenfolge"))
					failureText+="<div><span class=\"heading\">"+key+": </span>";
				else
					failureText+="<div class=\"increment\"><span class=\"heading\">"+key+": </span>";		
				
				if(model.getSuccessMap().get(key))
					failureText+="<span class=\"success\">Erfolgreich!";
				else 
					failureText+="<span class=\"failure\">Failed!";
				if(model.getOrderFailures(key)!=null
						&& !model.getOrderFailures(key).trim().isEmpty())
					failureText+="</span><span class=\"comment\"> ( "+model.getOrderFailures(key)+")</span>";
				failureText += "</span></div>";
			}
			messageBox.setText(failureText+"</body></html>");
			System.out.println(failureText);
		}
		update();
	}

	public void update() {
		saveDropModel=new DefaultListModel <String>();
		saveDropList=new JList<String>(saveDropModel);
		saveDropList.setTransferHandler(new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemode, model));	
	}

	@Override
	public Object get(String valueToReturn) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
