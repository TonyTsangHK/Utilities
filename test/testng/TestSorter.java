package testng;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.sort.HeapSort;
import utils.data.sort.IntroSort;
import utils.data.sort.MergeSort;
import utils.data.sort.SinglePivotQuickSort;
import utils.math.MathUtil;

public class TestSorter {
    private int testSize = 1000000;
    private Integer[] array;
    
    @BeforeMethod
    public void setUp() {
        array = new Integer[testSize];
        
        for (int i = 0; i < array.length; i++) {
            array[i] = new Integer(MathUtil.randomInteger(0, testSize));
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
    
    /*
    @Test
    public void testSmoothSort() {
        SmoothSort.smoothSort(array);
        
        checkValidOrder();
    }
    */
    
    private void checkValidOrder() {
        for (int i = 1; i < array.length; i++) {
            assertTrue(array[i-1].intValue() <= array[i].intValue(), "(" + (i-1) + "): " + array[i-1] + ", (" + i + "): " + array[i]);
        }
    }
}
