package view.teacher;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;

import Listener.FromTransferHandler;
import Listener.ToSaveTransferHandler;

import model.Model;

import controller.Controller;
import controller.DCCommand;
import view.JView;

public class PreViewEditor extends JView{

	// Puzzlemodus 0: Reines Drag and Drop
		// Puzzlemodus 1: Elemente werden von rechts nach links "geschaufelt", mit zurückschaufeln
		// Puzzlemodus 2: Elemente werden von rechts nach links geschaufelt, ohne zurückschaufeln
		// Puzzlemodus 3: Elemente bleiben rechts vorhanden, mehrfach-Drag ist möglich
		private int Puzzlemodus=1;
		private JButton randomize;
		private JButton save;
		
		private DefaultListModel<String> dragModel;
		private DefaultListModel<String> saveDropModel;
		private JList<String> dragList;
		private JList<String> saveDropList;
		private ButtonGroup dropMode;
		private JPanel studentPanel;
		
	public PreViewEditor(Model model) {
		super(model);
		menu = new MenuTeacher(3);
		addMenuToFrame(menu);
		setupPreviewPanel();
		setupEditPanel();
		setupButtons();
		draw();
	}
	
	private void setupPreviewPanel(){	
		saveDropModel=new DefaultListModel<String>();
		saveDropList=new JList<String>(saveDropModel);
		dragModel=makeDefaultListModel();
		dragList=new JList<String>(dragModel);
		FromTransferHandler dragTransferH = new FromTransferHandler(dragModel, dragList, model);
		ToSaveTransferHandler dragDropTransferH = new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemodus, model);
		
		switch(Puzzlemodus){
			case 0:
				// Einzelne Drag and Drop List (nicht zwei)
				saveDropModel=makeDefaultListModel();
				saveDropList=new JList<String>(saveDropModel);
				dragDropTransferH = new ToSaveTransferHandler(saveDropModel, saveDropList, Puzzlemodus, model);
				dragModel= new DefaultListModel<String>();
				dragList = new JList<String>();
				saveDropList.setDropMode(DropMode.INSERT);				
				saveDropList.setTransferHandler(dragDropTransferH);
				break;
			case 1:
				// Elemente werden rechts entfernt, können auch wieder zurück nach rechts transferiert werden
				saveDropList.setDropMode(DropMode.INSERT);
				dragList.setDropMode(DropMode.INSERT);
				saveDropList.setTransferHandler(dragDropTransferH);
				dragList.setTransferHandler(dragTransferH);
				break;
			case 2:
				// Elemte werden rechts entfernt, können links nicht entfernt werden
				saveDropList.setDropMode(DropMode.INSERT);
				dragTransferH.disableRevert();
				saveDropList.setTransferHandler(dragDropTransferH);
				dragList.setTransferHandler(dragTransferH);
				break;
			case 3:
				// Elemente bleiben rechts vorhanden, mehrfacher Drag&Drop möglich
				saveDropList.setDropMode(DropMode.ON_OR_INSERT);
				dragList.setDropMode(DropMode.ON_OR_INSERT);
				dragTransferH.disableRevert();
				dragTransferH.setAction(TransferHandler.COPY);
				saveDropList.setTransferHandler(dragDropTransferH);
				dragList.setTransferHandler(dragTransferH);
				break;
			default:
				break;	
		}
		
		// Linke Liste (Drop)
		saveDropList.setName("dropList");
		saveDropList.setFixedCellHeight(20);
		saveDropList.setDragEnabled(true);
		JScrollPane scrollPanel_sDL = new JScrollPane(saveDropList);
		scrollPanel_sDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_sDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_sDL.setPreferredSize(new Dimension(200,300));
		
		// Rechte Liste (Drag)
		dragList.setName("dragList");
		dragList.setFixedCellHeight(20);
		dragList.setDragEnabled(true);
		dragList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPanel_dDL = new JScrollPane(dragList);
		scrollPanel_dDL.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_dDL.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel_dDL.setPreferredSize(new Dimension(200,300));
		
//		compileButton=new JButton("Kompilieren");
//		testButton = new JButton("Test starten");
//		JPanel topPanel=new JPanel(new FlowLayout(FlowLayout.LEFT));
//		topPanel.add(compileButton);
//		topPanel.add(testButton);
		studentPanel = new JPanel();
		studentPanel.add(scrollPanel_sDL);
		if(Puzzlemodus!=0)
			studentPanel.add(scrollPanel_dDL);
		studentPanel.setPreferredSize(new Dimension(450,310));
//		mainPanel.add(topPanel,BorderLayout.PAGE_START);
		mainPanel.add(studentPanel, BorderLayout.CENTER);
	}
	
	private void setupEditPanel(){
		JPanel selectDragAndDrop = new JPanel();
		selectDragAndDrop.setLayout(new BoxLayout(selectDragAndDrop, BoxLayout.Y_AXIS));
		dropMode = new ButtonGroup();
		ImageIcon[] dragIcons= {new ImageIcon("rsc/dragDrop_0.png"), new ImageIcon("rsc/dragDrop_2.png"), new ImageIcon("rsc/dragDrop_2.png"), new ImageIcon("rsc/dragDrop_2.png")};
		ImageIcon[] dragIcons_pressed = {new ImageIcon("rsc/dragDrop_0_pressed.png"), new ImageIcon("rsc/dragDrop_2_pressed.png"), new ImageIcon("rsc/dragDrop_2_pressed.png"), new ImageIcon("rsc/dragDrop_2_pressed.png")};
		for(int i=0; i<dragIcons.length;i++){
			JRadioButton radioButton = new JRadioButton();
			radioButton.setIcon(dragIcons[i]);
			radioButton.setSelectedIcon(dragIcons_pressed[i]);
			final Integer n  = new Integer(i);
			radioButton.addActionListener(new ActionListener() {	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Puzzlemodus = n;
					update();
				}
			});
			dropMode.add(radioButton);
			selectDragAndDrop.add(radioButton);
		}
		selectDragAndDrop.setAlignmentX(Component.LEFT_ALIGNMENT);
		mainPanel.add(selectDragAndDrop, BorderLayout.LINE_START);
	}
	private DefaultListModel<String> makeDefaultListModel(){
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		
		// Dies ist nötig, um bei JList Elementen die Tabbreite berücksichtigen zu können
		// Steht hier, weil es ein Problem von Swing ist, kein allgemeines Problem
		Vector<String> codeVector = model.getCodeVector(true);
		for(String string : codeVector){
			
			listModel.add(listModel.size(),  string);
		}
		return listModel;
	}
	private void setupButtons(){
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		
		randomize = new JButton("<html><body><p align=\"center\">Liste neu<br>randomisieren</p></body></html>.");
		save = new JButton("Speichern");
		randomize.setAlignmentX(Component.CENTER_ALIGNMENT);
		save.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		buttonPanel.add(randomize);
		buttonPanel.add(Box.createRigidArea(new Dimension(0,10)));
		buttonPanel.add(save);
		mainPanel.add(buttonPanel, BorderLayout.LINE_END);
		
	}
	/**
	 * Wird vom Controller ausgeführt, um Listener, Handler und <br>
	 * Controller hinzuzufügen
	 */
	public void addController(Controller controller){
		// TODO: In den offiziellen Controller auslagern
		//saveDropList.addMouseListener((DefaultController)controller);
//		compileButton.addActionListener(controller);
//		compileButton.setActionCommand(DCCommand.Compile.toString());
//		testButton.setActionCommand(DCCommand.TestCode.toString());
//		testButton.addActionListener(controller);
		
		randomize.setName("randomize");
		randomize.setActionCommand(DCCommand.Randomize.toString());
		randomize.addActionListener(controller);
		save.setActionCommand(DCCommand.Save.toString());
		save.addActionListener(controller);
		
		menu.addActionListener(controller);
	}
	
	public int getPuzzleModus(){
		return Puzzlemodus;
	}
	@Override
	public void update() {
		mainPanel.remove(studentPanel);
		setupPreviewPanel();
		draw();
	}

	@Override
	public void update(Observable o, Object arg) {
		update();		
	}

}
