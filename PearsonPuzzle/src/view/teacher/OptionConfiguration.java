package view.teacher;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import model.Model;
import view.JView;
import controller.Controller;
import controller.DCCommand;

/**
 * View, der den Lehrer grundsätzliche Einstellungen treffen lässt.
 * 
 * @author workspace
 */
public class OptionConfiguration extends JView {

    JCheckBox dbReset;
    JButton submit;

    public OptionConfiguration(Model model) {
	super(model);
	setupConfigPanel();
	menu = new MenuTeacher();
	this.addMenuToFrame(menu);
	mainPanel.revalidate();
    }

    private void setupConfigPanel() {
	JPanel configPanel = new JPanel(new GridLayout(/* 6 */0, 2, 6, 3));
	// configPanel.add(new JLabel("Datenbank auf Werkseinstellungen"));
	dbReset = new JCheckBox(
		"Datenbank auf Werkseinstellungen zurücksetzten");
	dbReset.setActionCommand("resetDB");
	configPanel.add(dbReset);
	mainPanel.add(configPanel, BorderLayout.CENTER);

	// - Damit Button im Border Layout (mainPanel) richtige Größe erhät,
	// wird er in eigenes Panel gekapselt
	submit = new JButton("Übernehmen");
	submit.setActionCommand(DCCommand.ResetDB.toString());
	JPanel submitPanel = new JPanel();
	submitPanel.add(submit);
	mainPanel.add(submitPanel, BorderLayout.SOUTH);
    }

    @Override
    public void addController(Controller controller) {
	dbReset.addItemListener(controller);
	menu.addActionListener(controller);
    }

    private void update() {
	dbReset.setSelected(model.isResetDB());
    }

    @Override
    public void update(Observable o, Object arg) {
	update();
    }
}
