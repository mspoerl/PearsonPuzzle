package model;

import static org.junit.Assert.*;
import org.junit.*;

public class ModelTest {

	Model model;

	@Before
	public void initModel(){
		model=new Model();
	}
	
	@Test
	public void testProjectCode() {
		assertEquals("",model.getProjectCode());
	}
	
	@Test
	public void testProjectName(){
		assertEquals("", model.getProjectName());
	}
	
	
}
