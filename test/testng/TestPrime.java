package testng;

import org.testng.annotations.Test;
import utils.math.PrimeUtil;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class TestPrime {
    @Test
    public void testPrimeMethod() {
        int testSize = 3000000;
        List<Integer> nums = new ArrayList<Integer>(testSize);
        for (int i = 0; i < testSize; i++) {
            nums.add(new Integer(i));
        }
        boolean[] s = PrimeUtil.getPrimeArray(testSize-1);
        for (int i = 0; i < nums.size(); i++) {
            if (s[i]) {
                assertTrue(PrimeUtil.isPrime(nums.get(i).intValue()));
            } else {
                assertFalse(PrimeUtil.isPrime(nums.get(i).intValue()));
            }
        }
    }
}
