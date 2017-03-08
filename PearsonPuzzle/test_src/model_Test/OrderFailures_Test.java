package model_Test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;

import model.OrderFailures;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Test Prüft den Sortieralgorythmus, der die für die Abfolge von Codezeilen
 * festgelegten Regeln prüft.
 * 
 * @author workspace
 * 
 */
@RunWith(Parameterized.class)
public class OrderFailures_Test {

    private Vector<String> codeVector_normal;
    private LinkedList<Integer> sortedCode;
    private Vector<Vector<Integer>> codeLine_GroupMatrix;
    // private LinkedList<Boolean> groupFailures;
    private LinkedHashMap<String, Integer> codeMap;
    private Boolean[] success;

    @Before
    public void initialize() {
	//
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Parameterized.Parameters
    public static Collection<Object[]> sourceObject() {
	Collection source = new LinkedList<>();
	for (int i = 0; i < 8; i++) {
	    source.add(getTestCase(i).toArray());
	}
	return source;
    }

    public OrderFailures_Test(Vector<String> codeVector_normal,
	    LinkedList<Integer> sortedCode,
	    Vector<Vector<Integer>> codeLine_GroupMatrix, Boolean[] sucess,
	    LinkedHashMap<String, Integer> codeMap) {
	this.codeVector_normal = codeVector_normal;
	this.sortedCode = sortedCode;
	this.codeLine_GroupMatrix = codeLine_GroupMatrix;
	this.success = sucess;
	this.codeMap = codeMap;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Collection getTestCase(int testCase) {
	Vector<String> codeVector_normal = new Vector<String>();
	LinkedList<Integer> sortedCode = new LinkedList<Integer>();
	Vector<Vector<Integer>> codeLine_GroupMatrix = new Vector<Vector<Integer>>();
	LinkedHashMap<String, Integer> codeMap = new LinkedHashMap<String, Integer>();
	String[] codeLines;
	Integer[] sortedLines;
	Integer[][] ruleGroup;
	Boolean[] success;
	switch (testCase) {
	case 0:
	    codeLines = new String[] { "Line0", "Line1", "Line2", "Line3",
		    "Line4", "Line5" };
	    sortedLines = new Integer[] { 0, 1, 3, 2, 4, 5 };
	    ruleGroup = new Integer[][] { { 0, 1, 1, 2, 3, 0 },
		    { 1, 1, 1, 1, 1, 1 } };
	    success = new Boolean[] { false, true };
	    break;
	case 1:
	    codeLines = new String[] { "Line0", "Line0", "Line2", "Line3",
		    "Line2", "Line5" };
	    sortedLines = new Integer[] { 1, 0, 4, 2, 5, 3 };
	    ruleGroup = new Integer[][] { { 1, 2, 3, 0, 0, 4 },
		    { 1, 1, 2, 3, 0, 0 } };
	    success = new Boolean[] { true, true };
	    break;
	case 2:
	    codeLines = new String[] { "Line0", "Line0", "Line0", "Line1",
		    "Line2", "Line2" };
	    sortedLines = new Integer[] { 1, 0, 2, 5, 3, 4 };
	    ruleGroup = new Integer[][] { { 1, 2, 3, 4, 4, 6 },
		    { 1, 1, 1, 2, 2, 3 } };
	    success = new Boolean[] { true, true };
	    break;
	case 3:
	    codeLines = new String[] { "Line0", "Line0", "Line0", "Line1",
		    "Line2", "Line2" };
	    sortedLines = new Integer[] { 5, 4, 3, 2, 1, 0 };
	    ruleGroup = new Integer[][] { { 1, 2, 3, 4, 4, 6 },
		    { 1, 1, 1, 2, 2, 3 } };
	    success = new Boolean[] { false, false };
	    break;
	case 4:
	    codeLines = new String[] { null, null, null, "Line2", null, "Line2" };
	    sortedLines = new Integer[] { 0, 1, 2, 3, 5, 4 };
	    ruleGroup = new Integer[][] { { 1, 2, 3, 4, 4, 6 },
		    { 1, 1, 1, 2, 2, 3 }, { 1, 2, 2, 3, 3, 3 } };
	    success = new Boolean[] { false, false, true };
	    break;
	case 5:
	    codeLines = new String[] { "Line0", "Line1", "Line2", "Line3",
		    "Line4", "Line5" };
	    sortedLines = new Integer[] { 1, 0, 2, 3, 5, 4 };
	    // Null und 0 sind bei Regeln äquivalent
	    ruleGroup = new Integer[][] { { 1, 1, null, null, 4, 6 },
		    { 1, 1, 0, 0, 4, 6 }, { 1, 1, null, 2, 2, 3 },
		    { 1, 1, 0, 2, 2, 3 }, { 1, null, 2, 3, 4, 4 },
		    { 1, 0, 2, 3, 4, 4 } };
	    success = new Boolean[] { false, false, false, false, true, true };
	    break;
	case 6:
	    codeLines = new String[] { "Line0", "Line1", "Line2", "Line3",
		    "Line4", "Line5" };
	    sortedLines = new Integer[] { 0, 1, 2, 4 };
	    // Null und 0 sind bei Regeln äquivalent
	    ruleGroup = new Integer[][] { { 1, 2, 3, 4, 5, 0 },
		    { 1, 2, 3, 0, 0, 0 }, { 1, 1, 1, 0, 1, 0 },
		    { 0, 0, 0, 0, 0, 0 } };
	    success = new Boolean[] { false, true, true, true };
	    break;
	default:
	    codeLines = new String[] { "Line0", "Line1", "Line2", "Line1",
		    "Line4", "Line5" };
	    sortedLines = new Integer[] { 0, 2, 3, 4, 5 };
	    ruleGroup = new Integer[][] { { -1, -1, 3, 0, 5, 6 },
		    { 0, 0, 0, -1, 1, 1 }, { 0, -1, 0, 0, 1, 1 },
		    { 0, -1, 1, -1, 1, 1 } };
	    success = new Boolean[] { false, true, true, true };
	    break;
	}
	for (int i = 0; i < ruleGroup.length; i++) {
	    codeLine_GroupMatrix.add(new Vector<Integer>());
	}
	for (int i = 0; i < codeLines.length; i++) {
	    codeVector_normal.add(codeLines[i]);
	    if (sortedLines.length > i)
		sortedCode.add(sortedLines[i]);
	    for (int j = 0; j < codeLine_GroupMatrix.size(); j++) {
		codeLine_GroupMatrix.get(j).add(ruleGroup[j][i]);
	    }
	    if (!codeMap.containsKey(codeLines[i]))
		codeMap.put(codeLines[i], i);
	}
	LinkedList testSource = new LinkedList();
	testSource.add(codeVector_normal);
	testSource.add(sortedCode);
	testSource.add(codeLine_GroupMatrix);
	testSource.add(success);
	testSource.add(codeMap);

	return testSource;
    }

    @Test
    public void test() {
	LinkedList<Boolean> groupFailures = OrderFailures.testOrder_groups(
		sortedCode, codeLine_GroupMatrix, codeMap, codeVector_normal);
	for (int i = 0; i < success.length; i++) {
	    assertEquals("Fehler bei Regel Nummer " + (i + 1) + " von "
		    + success.length, success[i], groupFailures.get(i));
	}
    }

}
