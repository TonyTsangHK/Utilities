package testng;

import static org.testng.Assert.*;

import org.testng.annotations.Test;
import utils.math.MathUtil;

public class TestRoundUp {
    private double delta = 0.0000001;
    
    @Test
    public final void testRoundUpDoubleInt() {
        assertEquals(MathUtil.roundUp(100.3336, 2), 100.33, delta);
        assertEquals(MathUtil.roundUp(100.335, 2), 100.34, delta);
    }

    @Test
    public final void testRoundUpToUnit() {
        testRoundUpToUnit(100.33, 0.5, 100.5);
        testRoundUpToUnit(100.25, 0.5, 100.5);
        testRoundUpToUnit(100.24, 0.5, 100);
        testRoundUpToUnit(100.249999999999, 0.5, 100);
        testRoundUpToUnit(100.125, 0.25, 100.25);
        testRoundUpToUnit(100.001, 0.25, 100);
        testRoundUpToUnit(13363, 2, 13364);
        testRoundUpToUnit(13362, 2, 13362);
        testRoundUpToUnit(3365, 100, 3400);
        testRoundUpToUnit(3315, 100, 3300);
    }
    
    private void testRoundUpToUnit(double inputValue, double unit, double expectedResult) {
        double actualResult = MathUtil.roundUpToUnit(inputValue, unit);
        if (actualResult == expectedResult) {
            assertEquals(actualResult, expectedResult, delta);
        } else {
            fail("inputValue: " + inputValue + ", unit: " + unit + ", expectedResult: " + expectedResult + ", actualResult: " + actualResult);
        }
    }
}
