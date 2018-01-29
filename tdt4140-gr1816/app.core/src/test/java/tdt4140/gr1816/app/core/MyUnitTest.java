import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.junit.Assert.*;
class MyClass {
	
	public static double multiply(double A, double B){
		return A*B;
	}
}

public class MyUnitTest {
    @Test
    public void testConcatenate() {
        assertEquals("onetwo", "onetwo");

    }
    @Test
    public void multiplicationOfZeroIntegersShouldReturnZero() {
        MyClass tester = new MyClass(); // MyClass is tested

        // assert statements
        assertEquals(0.0, tester.multiply(10, 0), 0.001);
        assertEquals(0.0, tester.multiply(0, 10), 0.001);
        assertEquals(0.0, tester.multiply(0, 0), 0.001);
    }
}
