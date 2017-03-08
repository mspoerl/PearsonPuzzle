import model_Test.DbTransactio_Value_Test;
import model_Test.DbTransaction_Export_Test;
import model_Test.OrderFailures_Test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import compiler_Test.CodeCompletion_Test;
import compiler_Test.TestCompiler_Test;

import controller_Test.Robot_Button_Test;
import controller_Test.ViewChange_Button_Test;

/**
 * Test Suite, die alle aufgeführeten JUnit Test-KLassen ausführt ausführt.
 * @author workspace
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({

DbTransaction_Export_Test.class, CodeCompletion_Test.class,
	TestCompiler_Test.class,

	ViewChange_Button_Test.class,

	OrderFailures_Test.class, Robot_Button_Test.class,
	DbTransactio_Value_Test.class, })
public class TestSuite {
    //
}
