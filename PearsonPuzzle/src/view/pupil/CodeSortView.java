package view.pupil;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Observable;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import view.JView;

import controller.Controller;

import Listener.FromTransferHandler;
import Listener.ToSaveTransferHandler;

import model.Model;

/**
 * Drag and Drop Liste für den Schüler. 
 * Hier kann gepuzzlet werden!
 * 
 * @author workspace
 */
public class CodeSortView extends JView {
	private JList<String> dragList;
	private JList<String> saveDropList;
	private DefaultListModel <String> dragModel;
	private DefaultListModel <String> saveDropModel;
	private String defaultDescription="Puzzle den Code in die richtige Reihenfolge!\n \nViel Spaß ;-)";
	private JButton enter;
	// Puzzlemodus 0: Reines Drag and Drop
	// Puzzlemodus 1: Elemente werden von rechts nach links "geschaufelt", mit zurückschaufeln
	// Puzzlemodus 2: Elemente werden von rechts nach links geschaufelt, ohne zurückschaufeln
	// Puzzlemodus 3: Elemente bleiben rechts vorhanden, mehrfach-Drag ist möglich
	private int Puzzlemodus=ToSaveTransferHandler.DnD_Bugger_OneWay;
	public CodeSortView(Model model) {
		super(model);
		// TODO: Arbeitsanweisungen für Schüler definieren und einfügen
		menu=new MenuPupil();
		this.addMenuToFrame(menu);
		setupCodeLists();
		setupButtons();
		draw();
		// TODO Auto-generated constructor stub
	}
	
		// private Methode, um die Drag and Drop Liste zu konstruieren
		private void setupCodeLists(){
			saveDropModel=new DefaultListModel<String>();
			saveDropList=new JList<String>(saveDropModel);
			dragModel=makeDefaultListModel(model.getCodeList());
			dragList=new JList<String>(dragModel);
			TransferHandler dragTransferH = new FromTransferHandler(dragModel, dragList);
			ToSaveTransferHandler dragDropTransferH = new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemodus);
			
			switch(Puzzlemodus){
				case 0:
					// reine Drag List (dragDropList wird entfernt
					saveDropModel=makeDefaultListModel(model.getCodeList());
					saveDropList=new JList<String>(saveDropModel);
					dragDropTransferH = new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemodus);
					dragModel= new DefaultListModel();
					dragList = new JList<String>();
					saveDropList.setDropMode(DropMode.INSERT);					
					saveDropList.setTransferHandler(dragDropTransferH);
					break;
				case 1:
					// TODO: umsetzten
					break;
				case 2:
					// Elemte werden rechts entfernt, können links nicht entfernt werden
					saveDropList.setDropMode(DropMode.INSERT);
					saveDropList.setTransferHandler(dragDropTransferH);
					dragList.setTransferHandler(dragTransferH);
					break;
				case 3:
					saveDropList.setDropMode(DropMode.ON_OR_INSERT);					
					saveDropList.setTransferHandler(dragDropTransferH);
					dragList.setTransferHandler(dragTransferH);
					break;
				default:
					Puzzlemodus=3;
					break;
					
			}
			
			dragList.setName("dragList");
			saveDropList.setName("dropList");
			//dragDropList.setDropMode(DropMode.ON);
			saveDropList.setFixedCellHeight(20);
			dragList.setFixedCellHeight(20);
			
			dragList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			dragList.setDragEnabled(true);
			saveDropList.setDragEnabled(true);
			
			JScrollPane scrollPanel_sDL = new JScrollPane(saveDropList);
			JScrollPane scrollPanel_dDL = new JScrollPane(dragList);
			scrollPanel_sDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_sDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_sDL.setPreferredSize(new Dimension(360,300));
			mainPanel.add(scrollPanel_sDL, BorderLayout.LINE_START);
			scrollPanel_dDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_dDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_dDL.setPreferredSize(new Dimension(360,300));
			if(Puzzlemodus!=0)
				mainPanel.add(scrollPanel_dDL, BorderLayout.LINE_END);
			JTextArea description=new JTextArea(defaultDescription);
			if(!model.getProjectDescription().trim().equals("")){
				description.setText(model.getProjectDescription());
			}
			description.setLineWrap(true);
			description.setWrapStyleWord(true);
			JScrollPane scrollPane_description = new JScrollPane(description);
			scrollPane_description.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPane_description.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane_description.setPreferredSize(new Dimension(360,100));
			mainPanel.add(scrollPane_description,BorderLayout.PAGE_END);
		}
		
		// private Methode, um die Buttons zu definieren
		private void setupButtons(){
			enter = new JButton("Projekt öffnen");
			JPanel topPanel=new JPanel(new BorderLayout());
			JButton compileButton=new JButton("Compile");
			topPanel.add(compileButton,BorderLayout.LINE_START);
			//topPanel.add(saveButton, BorderLayout.LINE_END);
			mainPanel.add(topPanel,BorderLayout.PAGE_START);
		}
		
		/**
		 * Wird vom Controller asugeführt, um Listener, Handler und <br>
		 * Controller hinzuzufügen
		 */
		public void addController(Controller controller){
			// TODO: In den offiziellen Controller auslagern
			saveDropList.addMouseListener(controller);
			enter.addActionListener(controller);
			enter.setActionCommand("openProject");
			
			menu.addActionListener(controller);
		}
		
	/**
	 * Soll noch in einen Presenter ausgelagert werden <br>
	 * ermöglicht, dass die Swing Komponenten ein Listen Model erhalten
	 * Liste aus dem Model @param stringList
	 * Liste für (swing) View @return
	 */
	private DefaultListModel<String> makeDefaultListModel(List<String> stringList){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		
		// Dies ist nötig, um bei JList Elementen die Tabbreite berücksichtigen zu können
		// Steht hier, weil es ein Problem von Swing ist, kein allgemeines Problem
		for(String string : stringList){
			String tab;
			if(model.getTabSize()==0)
				tab="";
			else
				tab=" ";
			for(int i=0;i<model.getTabSize();i++){
				tab=tab+" ";
			}
			String bString = string.replaceAll("\t", tab);
			listModel.add(listModel.size(),  bString);
		}
		return listModel;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		dragModel=makeDefaultListModel(model.getCodeList());
		saveDropModel=new DefaultListModel <String>();
		dragList=new JList<String>(dragModel);
		saveDropList=new JList<String>(saveDropModel);
		dragList.setTransferHandler(new FromTransferHandler(dragModel, dragList));
		saveDropList.setTransferHandler(new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemodus));	
	}

}
