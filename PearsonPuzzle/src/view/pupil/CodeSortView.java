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
	private JList<String> dragDropList;
	private JList<String> saveDropList;
	private DefaultListModel <String> dragDropModel;
	private DefaultListModel <String> saveDropModel;
	private JButton enter;
	// Puzzlemodus 0: Reines Drag and Drop
	// Puzzlemodus 1: Elemente werden von rechts nach links "geschaufelt", mit zurückschaufeln
	// Puzzlemodus 2: Elemente werden von rechts nach links geschaufelt, ohne zurückschaufeln
	// Puzzlemodus 3: Elemente bleiben rechts vorhanden
	private int Puzzlemodus;
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
			
			dragDropModel=makeDefaultListModel(model.getCodeList());
			saveDropModel=new DefaultListModel<String>();
			dragDropList=new JList<String>(dragDropModel);
			saveDropList=new JList<String>(saveDropModel);
			dragDropList.setName("dragList");
			saveDropList.setName("dropList");
			//dragDropList.setDropMode(DropMode.ON);
			saveDropList.setDropMode(DropMode.ON_OR_INSERT);
			saveDropList.setFixedCellHeight(20);
			dragDropList.setFixedCellHeight(20);
			
			dragDropList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			dragDropList.setDragEnabled(true);
			saveDropList.setDragEnabled(true);
			
			JScrollPane scrollPanel_sDL = new JScrollPane(saveDropList);
			JScrollPane scrollPanel_dDL = new JScrollPane(dragDropList);
			scrollPanel_sDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_sDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_sDL.setPreferredSize(new Dimension(360,300));
			mainPanel.add(scrollPanel_sDL, BorderLayout.LINE_START);
			scrollPanel_dDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_dDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPanel_dDL.setPreferredSize(new Dimension(360,300));
			mainPanel.add(scrollPanel_dDL, BorderLayout.LINE_END);
			mainPanel.add(new JTextField("Hier erfolgt eine möglichst präzise Arbeitsanweisung für den Schüler"),BorderLayout.PAGE_END);
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
			dragDropList.setTransferHandler(new FromTransferHandler(dragDropModel, dragDropList, false));
			saveDropList.setTransferHandler(new ToSaveTransferHandler(TransferHandler.COPY, saveDropModel, saveDropList));
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
		for(String string : stringList){
			String tab=" ";
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
		dragDropModel=makeDefaultListModel(model.getCodeList());
		saveDropModel=new DefaultListModel <String>();
		dragDropList=new JList<String>(dragDropModel);
		saveDropList=new JList<String>(saveDropModel);
		dragDropList.setTransferHandler(new FromTransferHandler(dragDropModel, dragDropList, false));
		saveDropList.setTransferHandler(new ToSaveTransferHandler(TransferHandler.COPY, saveDropModel, saveDropList));	
	}

}
