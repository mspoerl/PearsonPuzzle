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
	public void test() {
		//model.setProjectCode(pNames.get(1), pCodes.get(1), linelength.get(1));
		String pCodes="";
		//model.setProjectCode(pCodes);
		assertEquals(pCodes,model.getProjectCode());
	}
	
	@Test
	public void test2(){
		assertEquals(model.getProjectCode(), model.getProjectCode());
	}
}
