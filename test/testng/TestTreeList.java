package testng;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.TreeList;
import utils.math.MathUtil;

public class TestTreeList {
    private int testSize = 10000;
    private TreeList<Integer> list;
    
    @BeforeMethod
    public void setUp() {
        list = new TreeList<Integer>();
        
        for (int i = 1; i <= testSize; i++) {
            list.add(new Integer(i));
        }
    }
    
    @Test
    public void testGet() {
        assertEquals(new Integer(list.size()), new Integer(testSize));
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), new Integer(i+1));
        }
    }
    
    @Test
    public void testContains() {
        for (int i = 1; i <= list.size()*2; i++) {
            Integer integer = new Integer(i);
            if (i <= list.size()) {
                assertTrue(list.contains(integer));
            } else {
                assertFalse(list.contains(integer));
            }
        }
    }
    
    @Test
    public void testRemoveByIndex() {
        while (!list.isEmpty()) {
            Integer integer = list.remove(MathUtil.randomInteger(0, list.size()-1));
            assertFalse(list.contains(integer));
        }
        assertEquals(new Integer(list.size()), new Integer(0));
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testRemoveByObject() {
        for (int i = 1; i <= testSize; i++) {
            Integer integer = new Integer(i);
            assertTrue(list.remove(integer));
            for (int j = 0; j < list.size(); j++) {
                assertEquals(list.get(j), new Integer(j+i+1));
            }
        }
        assertEquals(new Integer(list.size()), new Integer(0));
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testNullAndSameValues() {
        TreeList<Integer> list = new TreeList<Integer>();
        Integer[] values = new Integer[testSize];
        double nullChance = 0.05;
        for (int i = 0; i < values.length; i++) {
            if (Math.random() < nullChance) {
                values[i] = null;
            } else {
                values[i] = new Integer(MathUtil.randomInteger(0, testSize));
            }
            list.add(values[i]);
        }
    }
    
    @Test
    public void testIndexOf() {
        TreeList<Integer> list = new TreeList<Integer>();
        
        list.add(new Integer(1)); // 0
        list.add(new Integer(1));
        list.add(new Integer(1)); // 2
        
        list.add(new Integer(2)); // 3
        list.add(new Integer(2)); // 4
        
        list.add(new Integer(3)); // 5
        list.add(new Integer(3));
        list.add(new Integer(3)); // 7
        
        list.add(new Integer(4)); // 8
        
        list.add(new Integer(5)); // 9
        list.add(new Integer(5)); // 10
        
        list.add(new Integer(6)); // 11
        
        list.add(new Integer(7)); // 12
        list.add(new Integer(7));
        list.add(new Integer(7)); // 14
        
        list.add(new Integer(8)); // 15
        
        list.add(new Integer(9)); // 16
        list.add(new Integer(9));
        list.add(new Integer(9)); // 18
        
        list.add(new Integer(10)); // 19
        list.add(new Integer(10)); // 20
        
        assertEquals(new Integer(list.size()), new Integer(21));
        
        assertEquals(list.indexOf(new Integer(1)), 0);
        assertEquals(list.indexOf(new Integer(2)), 3);
        assertEquals(list.indexOf(new Integer(3)), 5);
        assertEquals(list.indexOf(new Integer(4)), 8);
        assertEquals(list.indexOf(new Integer(5)), 9);
        assertEquals(list.indexOf(new Integer(6)), 11);
        assertEquals(list.indexOf(new Integer(7)), 12);
        assertEquals(list.indexOf(new Integer(8)), 15);
        assertEquals(list.indexOf(new Integer(9)), 16);
        assertEquals(list.indexOf(new Integer(10)), 19);
        
        assertEquals(list.lastIndexOf(new Integer(1)), 2);
        assertEquals(list.lastIndexOf(new Integer(2)), 4);
        assertEquals(list.lastIndexOf(new Integer(3)), 7);
        assertEquals(list.lastIndexOf(new Integer(4)), 8);
        assertEquals(list.lastIndexOf(new Integer(5)), 10);
        assertEquals(list.lastIndexOf(new Integer(6)), 11);
        assertEquals(list.lastIndexOf(new Integer(7)), 14);
        assertEquals(list.lastIndexOf(new Integer(8)), 15);
        assertEquals(list.lastIndexOf(new Integer(9)), 18);
        assertEquals(list.lastIndexOf(new Integer(10)), 20);
    }
}
