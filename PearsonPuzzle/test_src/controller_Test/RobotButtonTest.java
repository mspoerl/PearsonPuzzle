package controller_Test;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;

import model.Model;

import org.junit.*;

import visitor.GUIBuilder;
import view.JView;

public class RobotButtonTest {
	
	Robot bot;
	Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	Integer projectsCount;
	
	@Before
	public void init(){
		GUIBuilder usr = new GUIBuilder();
		usr.setupGUI();
		Object view = usr.getController().getView();
		Model model = ((JView)view).getModel();
		projectsCount = model.getProjectVector().size();
		try {
			bot=new Robot();
			bot.setAutoDelay(10);
		    bot.setAutoWaitForIdle(true);
		} 
		catch (AWTException e) {
			fail("Robot konnte nicht  Initialisiert werden");
		}
		
		// "los gehts"
		bot.delay(500);
		bot.setAutoDelay(100);
		bot.mouseMove((int)screenSize.width/2+50, (int)screenSize.height/2-110);
		leftClick();
		
		// Projekt öffnen
		openProject(0);
		bot.delay(200);
		bot.setAutoDelay(10);
	}

	@Test
	public void overloadTest_DragAndDrop() {
		randomDragAndDrop(100);
		for(int i =0; i<10;i++){
		clickCompile();
		sleep(10);
		clickTest();
		sleep(10);
		}
		for(int i=0; i<20;i++){
			clickCompile();
		}
		sleep(2000);
	}
	
	@Test
	public void overloadTest_selectProject(){
		bot.setAutoDelay(10);
		Random rand = new Random();
		for(int i=0; i<20;i++){
			openProject(rand.nextInt(projectsCount));
			openProjectList();
		}
	}
	
	/**
	 * Wähle ein Projekt 
	 * @param projectNumber > 0
	 */
	private void openProject(int projectNumber) {
		// Projekt auswählen
		bot.mouseMove((int)screenSize.width/2-90, (int)screenSize.height/2-140+projectNumber*20);
		leftClick();
		// Projekt öffnen
		bot.mouseMove((int)screenSize.width/2+120,(int)screenSize.height/2-180);
		leftClick();
	}
	
	private void openProjectList(){
		bot.mouseMove((int)screenSize.width/2-120,(int)screenSize.height/2-180);
		leftClick();
	}

	private void leftClick(){
	    bot.mousePress(InputEvent.BUTTON1_MASK);
	    bot.mouseRelease(InputEvent.BUTTON1_MASK);
	}
	
	private void dragAndDrop(int xPos1, int yPos1, int xPos2, int yPos2){
		bot.mouseMove(xPos1, yPos1);
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseMove(xPos2, yPos2+2);
		bot.mouseMove(xPos2, yPos2);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	private void randomDragAndDrop(int numberOfDrags){
		numberOfDrags = Math.abs(numberOfDrags);
		
		int positionX = (int)screenSize.width/2-90;
		int minY = (Integer)screenSize.height/2-130;
		int maxY = (int)screenSize.height/2+230;
		int heigthElement = 20;
		int steps = (maxY-minY)/heigthElement;
		
		Random rand = new Random();
		for(int i = 0; i<numberOfDrags;i++){
			int n = rand.nextInt(steps);
			dragAndDrop(positionX, minY+n*heigthElement, positionX, minY+rand.nextInt(steps)*heigthElement);
		}
	}
	
	private void clickCompile(){
		bot.mouseMove((int)screenSize.width/2-80,(int)screenSize.height/2-160);
		leftClick();
	}
	private void clickTest(){
		bot.mouseMove((int)screenSize.width/2+50,(int)screenSize.height/2-160);
		leftClick();
	}
	
//	@Test
//	public void enterTest(){
//		try {
//			bot=new Robot();} 
//		catch (AWTException e) {
//			fail("Robot konnte nicht  Initialisiert werden");
//		}
//		bot.keyPress(KeyEvent.VK_ENTER);
//		sleep(100);
//		bot.keyRelease(KeyEvent.VK_ENTER);
//		sleep(4000);
//	}
	
	private void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
