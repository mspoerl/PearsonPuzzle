package controller_Test;

import static org.junit.Assert.fail;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

import model.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import view.JView;
import visitor.GUIBuilder;

/**
 * 
 * Es wird getestet, ob das Programm mit vielen aufeinanderfolgenden
 * Nutzeraktionen klarkommt.
 * 
 * @author workspace
 * 
 */
public class Robot_Button_Test {

    private final static Dimension screenSize = java.awt.Toolkit
	    .getDefaultToolkit().getScreenSize();
    private final static Integer NumberOfDragAndDrops_for_Overload = 20;
    private final static Integer NumberOfKlicks_for_Overload = 20;

    private Robot bot;
    private Integer projectsCount;

    @Before
    public void init() {
	GUIBuilder usr = new GUIBuilder();
	usr.setupGUI();
	Object view = usr.getController().getView();
	Model model = ((JView) view).getModel();
	projectsCount = model.getProjectVector().size();
	try {
	    bot = new Robot();
	    bot.setAutoDelay(10);
	    bot.setAutoWaitForIdle(true);
	} catch (AWTException e) {
	    fail("Robot konnte nicht  Initialisiert werden");
	}

	// "los gehts"
	bot.delay(500);
	bot.setAutoDelay(100);
	bot.mouseMove((int) screenSize.width / 2 + 50,
		(int) screenSize.height / 2 - 110);
	leftClick();

	// Projekt öffnen
	openProject(0);
	bot.delay(200);
	bot.setAutoDelay(10);
    }

    @Test(timeout = 550 * 10)
    public void overloadTest_DragAndDrop() {
	bot.setAutoDelay(5);
	randomDragAndDrop(NumberOfDragAndDrops_for_Overload);
	for (int i = 0; i < NumberOfKlicks_for_Overload; i++) {
	    clickCompile();
	    sleep(5);
	    clickTest();
	}
	for (int i = 0; i < NumberOfKlicks_for_Overload; i++) {
	    clickCompile();
	}
	sleep(200);
    }

    @Test(timeout = 550 * 10)
    public void overloadTest_selectProject() {
	bot.setAutoDelay(5);
	Random rand = new Random();
	for (int i = 0; i < NumberOfKlicks_for_Overload; i++) {
	    openProject(rand.nextInt(projectsCount));
	    openProjectList();
	}
    }

    /**
     * Wähle ein Projekt
     * 
     * @param projectNumber
     *            > 0
     */
    private void openProject(int projectNumber) {
	// Projekt auswählen
	bot.mouseMove((int) screenSize.width / 2 - 90, (int) screenSize.height
		/ 2 - 140 + projectNumber * 20);
	leftClick();
	// Projekt öffnen
	bot.mouseMove((int) screenSize.width / 2 + 120,
		(int) screenSize.height / 2 - 180);
	leftClick();
    }

    private void openProjectList() {
	bot.mouseMove((int) screenSize.width / 2 - 120,
		(int) screenSize.height / 2 - 180);
	leftClick();
    }

    private void leftClick() {
	bot.mousePress(InputEvent.BUTTON1_MASK);
	bot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    private void dragAndDrop(int xPos1, int yPos1, int xPos2, int yPos2) {
	bot.mouseMove(xPos1, yPos1);
	bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
	bot.mouseMove(xPos2, yPos2 + 2);
	bot.mouseMove(xPos2, yPos2);
	bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    private void randomDragAndDrop(int numberOfDrags) {
	numberOfDrags = Math.abs(numberOfDrags);

	int positionX = (int) screenSize.width / 2 - 90;
	int minY = (Integer) screenSize.height / 2 - 130;
	int maxY = (int) screenSize.height / 2 + 230;
	int heigthElement = 20;
	int steps = (maxY - minY) / heigthElement;

	Random rand = new Random();
	for (int i = 0; i < numberOfDrags; i++) {
	    int n = rand.nextInt(steps);
	    dragAndDrop(positionX, minY + n * heigthElement, positionX, minY
		    + rand.nextInt(steps) * heigthElement);
	}
    }

    private void clickCompile() {
	bot.mouseMove((int) screenSize.width / 2 - 80,
		(int) screenSize.height / 2 - 160);
	leftClick();
    }

    private void clickTest() {
	bot.mouseMove((int) screenSize.width / 2 + 50,
		(int) screenSize.height / 2 - 160);
	leftClick();
    }

    private static void sleep(int time) {
	try {
	    Thread.sleep(time);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @After
    public void exit() {
	bot.keyPress(InputEvent.ALT_MASK);
	bot.keyPress(KeyEvent.VK_F4);
	bot.keyRelease(InputEvent.ALT_MASK);
	bot.keyRelease(KeyEvent.VK_F4);
    }

}
