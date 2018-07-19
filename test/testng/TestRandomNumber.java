package testng;

import org.testng.annotations.Test;
import utils.math.MathUtil;

import static org.testng.Assert.assertTrue;

public class TestRandomNumber {
    @Test
    public void testFallInRange() {
        double min = 1.23, max = 1.24;
        int repeatTimes = 100000;
        
        double minNum = Double.MAX_VALUE, maxNum = Double.MIN_VALUE;
        int minCount = 0, maxCount = 0;
        for (int i = 0; i < repeatTimes; i++) {
            double num = MathUtil.randomNumber(min, max);
            boolean result = num >= min && num <= max;
            if (minNum > num) {
                minNum = num;
                minCount++;
            }
            if (maxNum < num) {
                maxNum = num;
                maxCount++;
            }
            assertTrue(result);
        }
    }
}
