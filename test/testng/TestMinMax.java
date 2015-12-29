package testng;

import static org.testng.Assert.*;

import java.util.Comparator;

import org.testng.annotations.Test;
import utils.data.MinMaxQueue;
import utils.data.NotComparableException;

public class TestMinMax {
    @Test
    public void testNormalIntegerMinMaxQueue() {
        MinMaxQueue<Integer> intQueue = new MinMaxQueue<Integer>();
        intQueue.add(5, 1, 9, 16, 6);
        assertEquals(intQueue.pollMin(), new Integer(1));
        assertEquals(intQueue.pollMin(), new Integer(5));
        assertEquals(intQueue.pollMin(), new Integer(6));
        assertEquals(intQueue.pollMin(), new Integer(9));
        assertEquals(intQueue.pollMin(), new Integer(16));
    }
    
    @Test
    public void testIntegerMinMaxQueueWidthTestingPurposeComparator() {
        MinMaxQueue<Integer> intQueue = new MinMaxQueue<Integer>(new TestingPurposeComparator());
        intQueue.add(5, 1, 9, 16, 6);
        assertEquals(intQueue.pollMin(), new Integer(16));
        assertEquals(intQueue.pollMin(), new Integer(9));
        assertEquals(intQueue.pollMin(), new Integer(6));
        assertEquals(intQueue.pollMin(), new Integer(5));
        assertEquals(intQueue.pollMin(), new Integer(1));
    }
    
    @Test
    public void testNonComparableObjQueue() {
        MinMaxQueue<DummyClass> objQueue = new MinMaxQueue<DummyClass>();
        boolean exceptionOccurred = false;
        try {
            objQueue.add(new DummyClass("1"), new DummyClass("6"), new DummyClass("16"), new DummyClass("9"));
        } catch (NotComparableException nce) {
            exceptionOccurred = true;
        }
        assertTrue(exceptionOccurred);
    }
    
    public static class DummyClass {
        public String d;
        
        public DummyClass(String d) {
            this.d = d;
        }
    }
    
    public static class TestingPurposeComparator implements Comparator<Integer> {
        public int compare(Integer i1, Integer i2) {
            return i2.intValue() - i1.intValue();
        }
    }
}
