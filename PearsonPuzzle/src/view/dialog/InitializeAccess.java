package view.dialog;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Model;
import model.access.AccessGroup;
import view.JView;
import controller.Controller;

@Deprecated
public class InitializeAccess extends JView {

    // private JPanel contentPanel;
    // private JDialog dialog;
    private JPanel dialogPanel;
    // private JButton moreUsers;
    private JPanel messagePanel;
    private JTextField userName;
    private JPasswordField password;
    private JComboBox<AccessGroup> accessGroup;

    public InitializeAccess(Model model) {
	super(model);
	setupFrame();
	setupDialogPanel();
	// showDialog(PPException.noDatabaseExists,true);
    }

    // private void setupDialog(){
    // dialog = new JDialog();
    // dialog.setModal(true);
    // dialog.setLayout(new FlowLayout());
    // dialog.setTitle("Nutzer Anlegen");
    // dialog.add(dialogPanel);
    // }

    private void setupDialogPanel() {
	dialogPanel = new JPanel(new BorderLayout());

	// submit = new JButton("Nutzer anlegend");
	// submit.setActionCommand(DCCommand.Save.toString());
	// submit.setName("Nutzer erstellen");
	//
	JPanel leftPanel = new JPanel();
	leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
	JPanel midPanel = new JPanel();
	midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
	JPanel topPanel = new JPanel();
	topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

	userName = new JTextField(15);
	password = new JPasswordField(15);

	accessGroup = new JComboBox<AccessGroup>();
	accessGroup.addItem(null);
	AccessGroup[] possibleAccessGroups = AccessGroup.values();
	for (int i = 0; i < possibleAccessGroups.length; i++) {
	    accessGroup.addItem(possibleAccessGroups[i]);
	}
	leftPanel.add(new JLabel("Nutzername"));
	leftPanel.add(userName);
	midPanel.add(new JLabel("Passwort"));
	midPanel.add(password);
	topPanel.add(new JLabel("Nutzergruppe"));
	topPanel.add(accessGroup);

	messagePanel = new JPanel();

	dialogPanel.add(leftPanel, BorderLayout.WEST);
	dialogPanel.add(midPanel, BorderLayout.CENTER);
	dialogPanel.add(topPanel, BorderLayout.NORTH);
	dialogPanel.add(messagePanel, BorderLayout.SOUTH);
	dialogPanel.setVisible(true);
    }

    public void addController(Controller controller) {

    }

    public void update(Observable o, Object arg) {
	messagePanel.removeAll();
	if (arg.equals("username_unset"))
	    messagePanel
		    .add(new JLabel("Bitte geben Sie einen Nutzernamen an"));
	else if (arg.equals("password_unset"))
	    messagePanel.add(new JLabel("Bitte geben Sie ein Passwort ein"));
	else if (arg.equals("accessgroup_unset"))
	    messagePanel
		    .add(new JLabel("Bitte geben Sie eine Nutzergruppe an"));

	if (arg.equals("username_toShort"))
	    messagePanel.add(new JLabel("Nutzername ist zu kurz."));
	else if (arg.equals("password_toShort"))
	    messagePanel
		    .add(new JLabel(
			    "<html>Das angegebene Passwort ist zu kurz, <br>bitte wählen Sie ein anderes.</html>"));

	if (arg.equals("password_unsave"))
	    messagePanel
		    .add(new JLabel(
			    "<html>Das angegebene Passwort ist unsicher. <br> Bitte verwenden Sie mindestens einen Großbuchstaben, eine Zahl und ein Sonderzeichen.</html>"));

    }

    public Object get(String variable) {
	if (variable.equals("password")) {
	    return password.getPassword();
	} else if (variable.equals("username")) {
	    return userName.getText();
	} else if (variable.equals("accessgroup")) {
	    return accessGroup.getSelectedItem();
	}
	return null;
    }

}
