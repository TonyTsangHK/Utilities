package testng;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.sort.*;
import utils.math.MathUtil;

import static org.testng.Assert.assertTrue;

public class TestSorter {
    private int testSize = 1000000;
    private Integer[] array;
    
    @BeforeMethod
    public void setUp() {
        array = new Integer[testSize];
        
        for (int i = 0; i < array.length; i++) {
            array[i] = MathUtil.randomInteger(0, testSize);
        }
    }
    
    @Test
    public void testMergeSort() {
        MergeSort.sort(array);
        
        checkValidOrder();
    }
    
    @Test
    public void testQuickSort() {
        SinglePivotQuickSort.sort(array);
        
        checkValidOrder();
    }
    
    @Test
    public void testHeapSort() {
        HeapSort.sort(array);
        
        checkValidOrder();
    }
    
    @Test
    public void testIntroSort() {
        IntroSort.sort(array);
        
        checkValidOrder();
    }

    @Test
    public void testMultiSort() {
        MultiSort.sort(array);

        checkValidOrder();
    }
    
    private void checkValidOrder() {
        for (int i = 1; i < array.length; i++) {
            assertTrue(
                array[i - 1] <= array[i],
                String.format("Invalid order detected, (%d): %d > (%d): %d", i-1, array[i-1], i, array[i])
            );
        }
    }
}
