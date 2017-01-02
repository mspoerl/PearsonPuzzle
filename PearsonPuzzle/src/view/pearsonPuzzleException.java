package view;

import javax.swing.JOptionPane;

public class pearsonPuzzleException extends Exception{
	
	public final static String noDatabaseExists = "Es existiert noch keine Datenbank. \nWollen Sie eine neue Datenbank anlegen?";
	public final static String anotherDatasetExists = "Datenbank mit gleichem Namen existiert bereits. \nWollen sie die existierende Datenbank überschreiben?";
	public final static String anotherInstanceIsRunnign = "<html><body>Das Programm läuft bereits.<br>Sollte das Problem in einigen Minuten weiterhin auftreten,<br>versuchen Sie, das Programm über den Task Manager zu beenden.<br><b>Vorsicht:</b> dabei können ungespeicherte Änderungen verloren gehen.</body></html>";
	
	private JOptionPane optionsPane;
	private String answer;
	String[] yesNoCancelOptions = { "Ja", "Nein", "Abbrechen" };
	String[] yesNoOptions = {"Ja", "Nein"};
	
	//private final int exceptionKind;
	private final String reason;
	
	public pearsonPuzzleException(String exceptionKind) {
		super();
		this.reason = exceptionKind;
		showDialog();
		

	}
	@Override
	public String getMessage(){
		return reason;
	}
	
	public Integer showDialog(){
		Integer n;
		switch(reason){
		
		case noDatabaseExists:
			 optionsPane = new JOptionPane();
			 n = JOptionPane.showOptionDialog(null, noDatabaseExists, null , JOptionPane.YES_NO_CANCEL_OPTION,
			          JOptionPane.QUESTION_MESSAGE,  // Icon
			          null, yesNoCancelOptions,yesNoCancelOptions[0] );
			 answer = yesNoCancelOptions[n];
			 break;
		 case anotherDatasetExists:
			 optionsPane = new JOptionPane();
			 n = JOptionPane.showOptionDialog(null, anotherDatasetExists, null , JOptionPane.YES_NO_CANCEL_OPTION,
			          JOptionPane.QUESTION_MESSAGE,  // Icon
			          null, yesNoCancelOptions,yesNoCancelOptions[0] );
			 answer = yesNoCancelOptions[n];
			 break;
		 case anotherInstanceIsRunnign: 
			 optionsPane = new JOptionPane();
			 JOptionPane.showMessageDialog(null, anotherInstanceIsRunnign, 
			         null, JOptionPane.ERROR_MESSAGE  // Icon
					 );
			 n = 0;
			 answer = "OK";
			 break;
		 default:
			 JOptionPane.showMessageDialog(null, reason, null, JOptionPane.INFORMATION_MESSAGE);
			 answer = "";
			 n=null;
		 }
		return n;
	}
	
	public String getAnswer(){
		return answer;
	}
	
}
