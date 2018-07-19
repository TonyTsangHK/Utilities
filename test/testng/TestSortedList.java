package testng;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.SortedList;
import utils.data.SortedListArray;
import utils.data.SortedListAvl;
import utils.data.sort.MultiSort;
import utils.math.MathUtil;

import java.util.*;

import static org.testng.Assert.*;

public class TestSortedList {
    private int testSize = 10000;

    private SortedList<Integer> listArray, listAvl;

    @BeforeMethod
    public void setup() {
        listArray = new SortedListArray<>();
        listAvl = new SortedListAvl<>();

        List<Integer> aList = new ArrayList<>(testSize);

        for (int i = 1; i <= testSize; i++) {
            aList.add(i);
        }

        while (!aList.isEmpty()) {
            int n = MathUtil.randomInteger(0, aList.size() - 1);

            Integer v = aList.get(n);
            listArray.add(v);
            listAvl.add(v);
            aList.remove(n);
        }
    }

    @Test
    public void testAdd() {
        SortedList<Integer> listArray = new SortedListArray<>(), listAvl = new SortedListAvl<>();

        List<Integer> tempList = new ArrayList<>(testSize);

        for (int i = 0; i < testSize; i++) {
            tempList.add(i + 1);
        }

        while (!tempList.isEmpty()) {
            int n = MathUtil.randomInteger(0, tempList.size() - 1);

            Integer v = tempList.get(n);
            listArray.add(v);
            listAvl.add(v);

            tempList.remove(n);
        }

        assertEquals(listArray.size(), testSize);
        assertEquals(listAvl.size(), testSize);

        Object[] array1 = listArray.toArray(), array2 = listAvl.toArray();

        assertEquals(listArray.size(), array1.length);
        assertEquals(listAvl.size(), array2.length);

        for (int i = 0; i < array1.length; i++) {
            assertEquals(array1[i], i + 1);
            assertEquals(array2[i], i + 1);
        }

        Integer[] iArray1 = new Integer[testSize], iArray2 = new Integer[testSize];

        listArray.toArray(iArray1);
        assertEquals(listArray.size(), iArray1.length);

        listAvl.toArray(iArray2);
        assertEquals(listAvl.size(), iArray2.length);

        for (int i = 0; i < iArray1.length; i++) {
            assertEquals(iArray1[i], new Integer(i + 1));
            assertEquals(iArray2[i], new Integer(i + 1));
        }
    }
    
    private void testAddAll(SortedList<Integer> list) {
        List<Integer> referenceList = new ArrayList<>();
        
        for (int i = 1; i <= testSize; i++) {
            list.add(i);
            referenceList.add(i);
        }
        
        List<Integer> valueList = new ArrayList<>();
        
        for (int i = 0; i < testSize; i++) {
            valueList.add(MathUtil.randomInteger(1, testSize));
        }
        
        list.addAll(valueList);
        referenceList.addAll(valueList);
        
        MultiSort.sort(referenceList);
        
        assertEquals(referenceList.size(), list.size());
        
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), referenceList.get(i));
        }
    }
    
    @Test
    public void testAddAll() {
        testAddAll(new SortedListArray<Integer>());
        testAddAll(new SortedListAvl<Integer>());
    }

    @Test
    public void testGet() {
        for (int i = 0; i < listArray.size(); i++) {
            assertEquals(listArray.get(i), new Integer(i + 1));
        }

        for (int i = 0; i < listAvl.size(); i++) {
            assertEquals(listAvl.get(i), new Integer(i + 1));
        }
    }

    private void testRemove(SortedList<Integer> list) {
        int count = 0, index = -1;
        while (!list.isEmpty()) {
            try {
                index = MathUtil.randomInteger(0, list.size() - 1);
                Integer integer = list.remove(index);
                count++;
                assertEquals(list.size(), testSize - count);
                assertFalse(list.contains(integer), "Integer must not exist in list(" + count + "): " + integer);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Exception ocurred");
            }
        }

        assertEquals(new Integer(list.size()), new Integer(0));
        assertTrue(list.isEmpty());
    }

    @Test
    public void testRemove() {
        testRemove(listArray);
        testRemove(listAvl);
    }

    @Test
    public void testIndexOf() {
        for (int i = 0; i < testSize; i++) {
            int n = MathUtil.randomInteger(1, testSize);
            int index1 = listArray.indexOf(n), index2 = listAvl
                    .indexOf(n);
            assertEquals(new Integer(index1), new Integer(n - 1));
            assertEquals(new Integer(index2), new Integer(n - 1));
        }
    }

    @Test
    public void testSmallerIndexOf() {
        testSmallerIndexOf(new SortedListAvl<Integer>());
        testSmallerIndexOf(new SortedListArray<Integer>());
    }
    
    private void testSmallerIndexOf(SortedList<Integer> list) {
        assertEquals(list.smallerIndexOf(2), -1);

        list.addAll(Arrays.asList(1,2,3,3,5,7,9));

        assertEquals(list.smallerIndexOf(0), -1);
        assertEquals(list.smallerIndexOf(1), -1);
        assertEquals(list.smallerIndexOf(2), 0);
        assertEquals(list.smallerIndexOf(3), 1);
        assertEquals(list.smallerIndexOf(4), 3);
        assertEquals(list.smallerIndexOf(5), 3);
        assertEquals(list.smallerIndexOf(6), 4);
        assertEquals(list.smallerIndexOf(7), 4);
        assertEquals(list.smallerIndexOf(8), 5);
        assertEquals(list.smallerIndexOf(9), 5);
        assertEquals(list.smallerIndexOf(10), 6);
        assertEquals(list.smallerIndexOf(11), 6);
        assertEquals(list.smallerIndexOf(9999), 6);

        // Random value test
        list.clear();
        
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            list.add(v);

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (int j = 1; j <= MathUtil.randomInteger(1,3); j++) {
                    list.add(v);
                }
            }
        }

        // Test non exist elements
        assertEquals(list.smallerIndexOf(0), -1);
        assertEquals(list.smallerIndexOf(299), -1);

        // Test last element
        assertEquals(list.smallerIndexOf(10000), list.size()-1);

        // Test random values (value withing list values)
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            int idx = list.smallerIndexOf(v);

            if (idx != -1) {
                int listVal = list.get(idx);

                if (idx < list.size() - 1) {
                    // Next element and searching value should be greater than the result value 
                    assertTrue(list.get(idx + 1) > listVal && v > listVal);
                } else {
                    // Result value is the last element, only check the searching value
                    assertTrue(v > listVal);
                }
            }
        }
    }

    @Test
    public void testSmallerOrEqualsIndexOf() {
        testSmallerOrEqualsIndexOf(new SortedListAvl<Integer>());
        testSmallerOrEqualsIndexOf(new SortedListArray<Integer>());
    }
    
    private void testSmallerOrEqualsIndexOf(SortedList<Integer> list) {
        assertEquals(list.smallerOrEqualsIndexOf(2), -1);

        list.addAll(Arrays.asList(1,2,3,3,5,7,9));

        assertEquals(list.smallerOrEqualsIndexOf(0), -1);
        assertEquals(list.smallerOrEqualsIndexOf(1), 0);
        assertEquals(list.smallerOrEqualsIndexOf(2), 1);
        assertEquals(list.smallerOrEqualsIndexOf(3), 3);
        assertEquals(list.smallerOrEqualsIndexOf(4), 3);
        assertEquals(list.smallerOrEqualsIndexOf(5), 4);
        assertEquals(list.smallerOrEqualsIndexOf(6), 4);
        assertEquals(list.smallerOrEqualsIndexOf(7), 5);
        assertEquals(list.smallerOrEqualsIndexOf(8), 5);
        assertEquals(list.smallerOrEqualsIndexOf(9), 6);
        assertEquals(list.smallerOrEqualsIndexOf(10), 6);
        assertEquals(list.smallerOrEqualsIndexOf(11), 6);
        assertEquals(list.smallerOrEqualsIndexOf(9999), 6);

        // Random value test
        list.clear();
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            list.add(v);

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (int j = 1; j <= MathUtil.randomInteger(1,3); j++) {
                    list.add(v);
                }
            }
        }

        // Test non exist elements
        assertEquals(list.smallerOrEqualsIndexOf(0), -1);
        assertEquals(list.smallerOrEqualsIndexOf(299), -1);

        // Test last element
        assertEquals(list.smallerOrEqualsIndexOf(list.get(list.size()-1)), list.size() - 1);

        // Test random values (value withing list values)
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            int idx = list.smallerOrEqualsIndexOf(v);

            if (idx != -1) {
                int listVal = list.get(idx);

                if (idx < list.size() - 1) {
                    // Next element is greater than and searching value should be greater or equals to the result value
                    // Since last index is guaranteed, next element should always be greater than the searching value
                    assertTrue(list.get(idx + 1) > listVal && v >= listVal);
                } else {
                    // Result value is the last element, only check the searching value
                    assertTrue(v >= listVal);
                }
            }
        }
    }

    @Test
    public void testGreaterIndexOf() {
        testGreaterIndexOf(new SortedListAvl<Integer>());
        testGreaterIndexOf(new SortedListArray<Integer>());
    }
    
    private void testGreaterIndexOf(SortedList<Integer> list) {
        assertEquals(list.greaterIndexOf(2), -1);

        list.addAll(Arrays.asList(1,2,3,3,5,7,9));

        assertEquals(list.greaterIndexOf(0), 0);
        assertEquals(list.greaterIndexOf(1), 1);
        assertEquals(list.greaterIndexOf(2), 2);
        assertEquals(list.greaterIndexOf(3), 4);
        assertEquals(list.greaterIndexOf(4), 4);
        assertEquals(list.greaterIndexOf(5), 5);
        assertEquals(list.greaterIndexOf(6), 5);
        assertEquals(list.greaterIndexOf(7), 6);
        assertEquals(list.greaterIndexOf(8), 6);
        assertEquals(list.greaterIndexOf(9), -1);
        assertEquals(list.greaterIndexOf(10), -1);
        assertEquals(list.greaterIndexOf(11), -1);
        assertEquals(list.greaterIndexOf(9999), -1);

        // Random value test
        list.clear();
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            list.add(v);

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (int j = 1; j <= MathUtil.randomInteger(1,3); j++) {
                    list.add(v);
                }
            }
        }

        // Test non exist elements
        assertEquals(list.greaterIndexOf(10000), -1);
        assertEquals(list.greaterIndexOf(99999), -1);

        // Test first element
        assertEquals(list.greaterIndexOf(list.get(0)-1), 0);

        // Test random values (value withing list values)
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            int idx = list.greaterIndexOf(v);

            if (idx != -1) {
                int listVal = list.get(idx);

                if (idx > 0) {
                    // Previous element and searching value should be smaller than the result value 
                    assertTrue(list.get(idx - 1) < listVal && v < listVal);
                } else {
                    // Result is the first element, only check the searching value
                    assertTrue(v < listVal);
                }
            }
        }
    }

    @Test
    public void testGreaterOrEqualsIndexOf() {
        testGreaterOrEqualsIndexOf(new SortedListAvl<Integer>());
        testGreaterOrEqualsIndexOf(new SortedListArray<Integer>());
    }
    
    private void testGreaterOrEqualsIndexOf(SortedList<Integer> list) {
        assertEquals(list.greaterOrEqualsIndexOf(2), -1);

        list.addAll(Arrays.asList(1,2,3,3,5,7,9));

        assertEquals(list.greaterOrEqualsIndexOf(0), 0);
        assertEquals(list.greaterOrEqualsIndexOf(1), 0);
        assertEquals(list.greaterOrEqualsIndexOf(2), 1);
        assertEquals(list.greaterOrEqualsIndexOf(3), 2);
        assertEquals(list.greaterOrEqualsIndexOf(4), 4);
        assertEquals(list.greaterOrEqualsIndexOf(5), 4);
        assertEquals(list.greaterOrEqualsIndexOf(6), 5);
        assertEquals(list.greaterOrEqualsIndexOf(7), 5);
        assertEquals(list.greaterOrEqualsIndexOf(8), 6);
        assertEquals(list.greaterOrEqualsIndexOf(9), 6);
        assertEquals(list.greaterOrEqualsIndexOf(10), -1);
        assertEquals(list.greaterOrEqualsIndexOf(11), -1);
        assertEquals(list.greaterOrEqualsIndexOf(9999), -1);

        // Random value test
        list.clear();
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            list.add(v);

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (int j = 1; j <= MathUtil.randomInteger(1,3); j++) {
                    list.add(v);
                }
            }
        }

        // Test non exist elements
        assertEquals(list.greaterOrEqualsIndexOf(10000), -1);
        assertEquals(list.greaterOrEqualsIndexOf(10299), -1);

        // Test first element
        assertEquals(list.greaterOrEqualsIndexOf(list.get(0)), 0);

        // Test random values (value withing list values)
        for (int i = 1; i <= 1000; i++) {
            int v = MathUtil.randomInteger(300, 9999);

            int idx = list.greaterOrEqualsIndexOf(v);

            if (idx != -1) {
                int listVal = list.get(idx);

                if (idx > 0) {
                    // Previous element should be smaller than and searching value should be smaller or equals to the result value
                    // Since first index is guaranteed, previous element should always be smaller than the result value
                    assertTrue(list.get(idx - 1) < listVal && v <= listVal);
                } else {
                    // Result value is the first element, only check the searching value
                    assertTrue(v <= listVal);
                }
            }
        }
    }

    @Test
    public void testRemoveWithObject() {
        SortedList<Integer> list = new SortedListAvl<Integer>();

        for (int i = 0; i < 10; i++) {
            list.add(i + 1);
        }

        list.remove(new Integer(4));

        Integer[] integerArray = new Integer[list.size()];
        list.toArray(integerArray);

        assertEquals(integerArray[0], new Integer(1));
        assertEquals(integerArray[1], new Integer(2));
        assertEquals(integerArray[2], new Integer(3));
        assertEquals(integerArray[3], new Integer(5));
        assertEquals(integerArray[4], new Integer(6));
        assertEquals(integerArray[5], new Integer(7));
        assertEquals(integerArray[6], new Integer(8));
        assertEquals(integerArray[7], new Integer(9));
        assertEquals(integerArray[8], new Integer(10));

        list.remove(new Integer(8));
        list.remove(new Integer(2));
        list.remove(new Integer(10));
        list.remove(new Integer(1));

        integerArray = new Integer[list.size()];
        list.toArray(integerArray);

        assertEquals(integerArray[0], new Integer(3));
        assertEquals(integerArray[1], new Integer(5));
        assertEquals(integerArray[2], new Integer(6));
        assertEquals(integerArray[3], new Integer(7));
        assertEquals(integerArray[4], new Integer(9));

        assertEquals(list.size(), 5);
    }

    @Test
    public void testSameElementValue() {
        SortedList<Integer> list = new SortedListAvl<Integer>();

        list.add(1);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(3);
        list.add(3);
        list.add(4);
        list.add(4);
        list.add(5);
        list.add(5);

        assertEquals(list.get(0), new Integer(1));
        assertEquals(list.get(1), new Integer(1));
        assertEquals(list.get(2), new Integer(2));
        assertEquals(list.get(3), new Integer(2));
        assertEquals(list.get(4), new Integer(3));
        assertEquals(list.get(5), new Integer(3));
        assertEquals(list.get(6), new Integer(4));
        assertEquals(list.get(7), new Integer(4));
        assertEquals(list.get(8), new Integer(5));
        assertEquals(list.get(9), new Integer(5));

        list.remove(new Integer(1));
        list.remove(new Integer(5));
        list.remove(new Integer(4));
        list.remove(new Integer(3));
        list.remove(new Integer(2));
        list.remove(new Integer(3));

        assertEquals(list.size(), 4);

        assertEquals(list.get(0), new Integer(1));
        assertEquals(list.get(1), new Integer(2));
        assertEquals(list.get(2), new Integer(4));
        assertEquals(list.get(3), new Integer(5));

        list.clear();

        for (int i = 0; i < 10; i++) {
            list.add(55);
        }

        for (int i = 1; i <= 55; i++) {
            list.add(55 + i);
            list.add(55 - i);
        }

        assertEquals(new Integer(list.size()), new Integer(120));

        for (int i = 0; i < list.size(); i++) {
            if (i < 55) {
                assertEquals(list.get(i), new Integer(i));
            } else if (i > 54 && i < 65) {
                assertEquals(list.get(i), new Integer(55));
            } else {
                assertEquals(list.get(i), new Integer(i - 9), "i: " + i);
            }
        }

        assertEquals(list.indexOf(55), 55);
        assertEquals(list.lastIndexOf(55), 64);
    }

    private void testIterator(SortedList<Integer> list) {
        Iterator<Integer> iter = list.iterator();

        int count = 1;
        while (iter.hasNext()) {
            Integer integer = iter.next();
            assertEquals(integer, new Integer(count++));
        }

        ListIterator<Integer> listIter = list.listIterator();

        count = 1;
        while (listIter.hasNext()) {
            Integer integer = listIter.next();
            assertEquals(integer, new Integer(count++));
        }

        count = testSize;
        while (listIter.hasPrevious()) {
            Integer integer = listIter.previous();
            assertEquals(integer, new Integer(count--));
        }

        listIter = list.listIterator(10);

        count = 11;
        while (listIter.hasNext()) {
            Integer integer = listIter.next();
            assertEquals(integer, new Integer(count++));
        }

        count = testSize;
        while (listIter.hasPrevious()) {
            Integer integer = listIter.previous();
            assertEquals(integer, new Integer(count--));
        }
    }

    @Test
    public void testIterator() {
        testIterator(listArray);
        testIterator(listAvl);
    }

    private void testSet(SortedList<Integer> list) {
        list.add(2);
        list.add(5);
        list.add(3);
        list.add(4);
        list.add(10);
        list.add(1);
        list.add(7);
        list.add(8);
        list.add(6);
        list.add(9);

        list.set(5, 19);
        assertEquals(new Integer(list.size()), new Integer(10));
        assertEquals(list.get(0), new Integer(1));
        assertEquals(list.get(1), new Integer(2));
        assertEquals(list.get(2), new Integer(3));
        assertEquals(list.get(3), new Integer(4));
        assertEquals(list.get(4), new Integer(5));
        assertEquals(list.get(5), new Integer(7));
        assertEquals(list.get(6), new Integer(8));
        assertEquals(list.get(7), new Integer(9));
        assertEquals(list.get(8), new Integer(10));
        assertEquals(list.get(9), new Integer(19));

        list.set(0, -8);
        assertEquals(new Integer(list.size()), new Integer(10));
        assertEquals(list.get(0), new Integer(-8));
        assertEquals(list.get(1), new Integer(2));
        assertEquals(list.get(2), new Integer(3));
        assertEquals(list.get(3), new Integer(4));
        assertEquals(list.get(4), new Integer(5));
        assertEquals(list.get(5), new Integer(7));
        assertEquals(list.get(6), new Integer(8));
        assertEquals(list.get(7), new Integer(9));
        assertEquals(list.get(8), new Integer(10));
        assertEquals(list.get(9), new Integer(19));
    }

    @Test
    public void testSet() {
        testSet(new SortedListArray<Integer>());
        testSet(new SortedListAvl<Integer>());
    }

    @Test
    public void testContains() {
        for (int i = -testSize; i <= testSize; i++) {
            if (i > 0) {
                assertTrue(listArray.contains(new Integer(i)), "Value must exist: " + i);
                assertTrue(listAvl.contains(new Integer(i)), "Value must exist: " + i);
            } else {
                assertFalse(listArray.contains(new Integer(i)), "Value must not exist: " + i);
                assertFalse(listAvl.contains(new Integer(i)), "Value must not exist: " + i);
            }
        }
    }

    private void testRemoveAll(SortedList<Integer> list) {
        int size = MathUtil.randomInteger(testSize / 2, testSize);
        ArrayList<Integer> arrayList = new ArrayList<Integer>(size);

        for (int i = 0; i < size; i++) {
            Integer v = MathUtil.randomInteger(1, testSize);
            while (arrayList.contains(v)) {
                v = MathUtil.randomInteger(1, testSize);
            }
            arrayList.add(v);
        }

        list.removeAll(arrayList);
        assertEquals(list.size(), testSize - arrayList.size());

        for (int i = 0; i < testSize; i++) {
            Integer integer = i + 1;
            if (arrayList.contains(integer)) {
                assertFalse(list.contains(integer), "Value must not exist: " + integer);
            } else {
                assertTrue(list.contains(integer), "Value must exist: " + integer);
            }
        }
    }

    @Test
    public void testRemoveAll() {
        testRemoveAll(listArray);
        testRemoveAll(listAvl);
    }

    private void testRetainAll(SortedList<Integer> list) {
        int size = MathUtil.randomInteger(testSize / 2, testSize);
        ArrayList<Integer> arrayList = new ArrayList<Integer>(size);

        for (int i = 0; i < size; i++) {
            Integer v = MathUtil.randomInteger(1, testSize);
            while (arrayList.contains(v)) {
                v = MathUtil.randomInteger(1, testSize);
            }
            arrayList.add(v);
        }

        list.retainAll(arrayList);
        assertEquals(list.size(), arrayList.size());

        for (int i = 0; i < testSize; i++) {
            Integer integer = i + 1;
            if (arrayList.contains(integer)) {
                assertTrue(list.contains(integer), "Value must exist: " + integer);
            } else {
                assertFalse(list.contains(integer), "Value must not exist: " + integer);
            }
        }

        SortedListArray<Integer> arraySortedList = new SortedListArray<Integer>();
        SortedListAvl<Integer> avlSortedList = new SortedListAvl<Integer>();

        arraySortedList.add(11);
        arraySortedList.add(11);
        arraySortedList.add(11);

        avlSortedList.add(11);
        avlSortedList.add(11);
        avlSortedList.add(11);

        List<Integer> retainList = new ArrayList<Integer>();
        retainList.add(11);
        retainList.add(11);

        boolean arrayListModified = arraySortedList.retainAll(retainList),
            avlListModified = avlSortedList.retainAll(retainList);

        if (arrayListModified) {
            fail("All values should be retained in SortedListArray!");
        }

        if (avlListModified) {
            fail("All values should be retained in SortedListAvl!");
        }
    }

    @Test
    public void testRetainAll() {
        testRetainAll(listArray);
        testRetainAll(listAvl);

    }

    private void testClone(SortedList<Integer> list) {
        SortedList<Integer> clonedList = list.clone();

        assertEquals(new Integer(clonedList.size()), new Integer(list.size()));

        for (int i = 0; i < clonedList.size(); i++) {
            assertEquals(list.get(i), clonedList.get(i));
        }

        int index = MathUtil.randomInteger(0, clonedList.size() - 1);
        Integer integer = clonedList.remove(index);

        assertEquals(new Integer(clonedList.size()), new Integer(list.size() - 1));
        assertEquals(list.get(index), integer);

        for (int i = 0; i < list.size(); i++) {
            if (i < index) {
                assertEquals(list.get(i), clonedList.get(i));
            } else if (i > index) {
                assertEquals(list.get(i), clonedList.get(i - 1));
            }
        }

        clonedList.add(integer);

        for (int i = 0; i < clonedList.size(); i++) {
            assertEquals(clonedList.get(i), list.get(i));
        }
    }

    @Test
    public void testClone() {
        testClone(listArray);
        testClone(listAvl);
    }

    private void testIterAddSetRemove(SortedList<Integer> list) {
        ListIterator<Integer> listIterator = list.listIterator();

        try {
            listIterator.remove();
            fail("Exception must occur");
        } catch (IllegalStateException ise) {
            // dummy block
        }

        assertEquals(listIterator.next(), new Integer(1));
        listIterator.add(-990);
        assertEquals(list.indexOf(-990), 0);

        assertEquals(listIterator.previous(), new Integer(-990));
        listIterator.remove();
        assertEquals(listIterator.next(), new Integer(1));
        listIterator.remove();
        assertEquals(listIterator.next(), new Integer(2));
        assertEquals(listIterator.next(), new Integer(3));
        assertEquals(listIterator.next(), new Integer(4));
        assertEquals(listIterator.next(), new Integer(5));
        assertEquals(listIterator.next(), new Integer(6));
        assertEquals(listIterator.next(), new Integer(7));
        assertEquals(listIterator.next(), new Integer(8));
        assertEquals(listIterator.next(), new Integer(9));
        assertEquals(listIterator.next(), new Integer(10));

        assertEquals(listIterator.previous(), new Integer(10));
        assertEquals(listIterator.previous(), new Integer(9));
        assertEquals(listIterator.previous(), new Integer(8));
        listIterator.remove();
        assertEquals(listIterator.next(), new Integer(9));
        assertEquals(listIterator.previous(), new Integer(9));
        assertEquals(listIterator.previous(), new Integer(7));
        assertEquals(listIterator.previous(), new Integer(6));
        assertEquals(listIterator.previous(), new Integer(5));
        assertEquals(listIterator.previous(), new Integer(4));
        assertEquals(listIterator.previous(), new Integer(3));
        assertEquals(listIterator.previous(), new Integer(2));
        assertFalse(listIterator.hasPrevious(), "Must not has previous");
        assertEquals(listIterator.next(), new Integer(2));
        assertEquals(listIterator.next(), new Integer(3));
        assertEquals(listIterator.next(), new Integer(4));
        assertEquals(listIterator.next(), new Integer(5));
        assertEquals(listIterator.next(), new Integer(6));
        assertEquals(listIterator.next(), new Integer(7));
        assertEquals(listIterator.next(), new Integer(9));
        assertEquals(listIterator.next(), new Integer(10));
    }

    @Test
    public void testIterAddSetRemove() {
        testIterAddSetRemove(listArray);
        testIterAddSetRemove(listAvl);
    }

    private void testIndexOfWithSameValues(SortedList<Integer> sList) {
        sList.add(1);
        sList.add(2);
        sList.add(3);
        sList.add(4);
        sList.add(5);
        sList.add(6);
        sList.add(7);
        sList.add(8);
        sList.add(9);
        sList.add(10);

        sList.add(3);
        sList.add(4);
        sList.add(6);
        sList.add(7);
        sList.add(10);

        sList.add(6);

        assertEquals(new Integer(sList.size()), new Integer(16));

        assertEquals(new Integer(sList.indexOf(1)), new Integer(0));
        assertEquals(new Integer(sList.indexOf(2)), new Integer(1));
        assertEquals(new Integer(sList.indexOf(3)), new Integer(2));
        assertEquals(new Integer(sList.lastIndexOf(3)), new Integer(3));
        assertEquals(new Integer(sList.indexOf(4)), new Integer(4));
        assertEquals(new Integer(sList.lastIndexOf(4)), new Integer(5));
        assertEquals(new Integer(sList.indexOf(5)), new Integer(6));
        assertEquals(new Integer(sList.indexOf(6)), new Integer(7));
        assertEquals(new Integer(sList.lastIndexOf(6)), new Integer(9));
        assertEquals(new Integer(sList.indexOf(7)), new Integer(10));
        assertEquals(new Integer(sList.lastIndexOf(7)), new Integer(11));
        assertEquals(new Integer(sList.indexOf(8)), new Integer(12));
        assertEquals(new Integer(sList.indexOf(9)), new Integer(13));
        assertEquals(new Integer(sList.indexOf(10)), new Integer(14));
        assertEquals(new Integer(sList.lastIndexOf(10)), new Integer(15));
    }

    @Test
    public void testIndexOfWithSameValues() {
        testIndexOfWithSameValues(new SortedListArray<Integer>());
        testIndexOfWithSameValues(new SortedListAvl<Integer>());
    }

    private void testGetSubList(SortedList<Integer> list) {
        int fromIndex = MathUtil.randomInteger(0, list.size() - 5), toIndex = MathUtil
                .randomInteger(fromIndex + 1, list.size() - 1);
        SortedList<Integer> subList = list.getSubList(fromIndex, toIndex);

        assertEquals(new Integer(toIndex - fromIndex), new Integer(subList.size()));

        for (int i = fromIndex; i < toIndex; i++) {
            assertEquals(list.get(i), subList.get(i - fromIndex));
        }
    }

    @Test
    public void testGetSubList() {
        testGetSubList(listArray);
        testGetSubList(listAvl);
    }

    private void testForEachLoop(SortedList<Integer> list) {
        int count = 1;
        for (Integer integer : list) {
            assertEquals(integer, new Integer(count++));
        }
    }

    @Test
    public void testForEachLoop() {
        testForEachLoop(listArray);
        testForEachLoop(listAvl);
    }

    public void testResort(SortedList<IntegerValue> sortedList) {
        ArrayList<IntegerValue> arrayList = new ArrayList<IntegerValue>();

        for (int i = 0; i < testSize; i++) {
            IntegerValue v = new IntegerValue(i);
            arrayList.add(v);
            sortedList.add(v);
        }

        for (IntegerValue anArrayList : arrayList) {
            anArrayList.setValue(MathUtil.randomInteger(0, testSize));
        }

        boolean allInOrder = true;
        IntegerValue prev = sortedList.get(0);
        for (int i = 1; i < sortedList.size(); i++) {
            IntegerValue curr = sortedList.get(i);
            if (prev.compareTo(curr) > 0) {
                allInOrder = false;
                break;
            }
        }
        assertFalse(allInOrder, "A random order sortedList is expected!");

        Collections.sort(arrayList);

        sortedList.resort();
        IntegerValue[] arr = new IntegerValue[sortedList.size()];
        sortedList.toArray(arr);
        for (int i = 0; i < sortedList.size() || i < arrayList.size(); i++) {
            assertEquals(sortedList.get(i), arrayList.get(i));
        }
    }

    @Test
    public void testResort() {
        testResort(new SortedListArray<IntegerValue>());
        testResort(new SortedListAvl<IntegerValue>());
    }

    private void testNullValues(SortedList<Integer> list) {
        assertTrue(list.add(null));
        assertTrue(list.add(null));

        assertNull(list.getMin());
        assertNull(list.get(0));
        assertNull(list.get(1));

        for (int i = 2; i < list.size(); i++) {
            assertEquals(list.get(i), new Integer(i - 1));
        }

        assertEquals(list.indexOf(null), 0);
        assertEquals(list.lastIndexOf(null), 1);

        assertEquals(list.indexOf(1), 2);

        assertTrue(list.contains(null));

        assertTrue(list.remove(null));

        assertEquals(list.indexOf(null), 0);
        assertEquals(list.lastIndexOf(null), 0);

        assertEquals(list.indexOf(1), 1);

        assertTrue(list.contains(null));

        assertTrue(list.remove(null));

        assertEquals(list.indexOf(null), -1);
        assertEquals(list.lastIndexOf(null), -1);

        assertEquals(list.indexOf(1), 0);

        assertFalse(list.contains(null));

        for (int i = 1; i <= list.size(); i++) {
            assertEquals(list.get(i - 1), new Integer(i));
        }
    }

    @Test
    public void testNullValues() {
        testNullValues(listArray);
        testNullValues(listAvl);
    }

    private void testSortOrder(SortedList<Integer> ascList) {
        ascList.add(null);
        ascList.add(10);
        ascList.add(20);
        ascList.add(30);
        ascList.add(40);
        ascList.add(50);
        ascList.add(60);
        ascList.add(70);
        ascList.add(80);
        ascList.add(90);
        ascList.add(100);

        assertNull(ascList.get(0));
        assertEquals(ascList.get(1), new Integer(10));
        assertEquals(ascList.get(2), new Integer(20));
        assertEquals(ascList.get(3), new Integer(30));
        assertEquals(ascList.get(4), new Integer(40));
        assertEquals(ascList.get(5), new Integer(50));
        assertEquals(ascList.get(6), new Integer(60));
        assertEquals(ascList.get(7), new Integer(70));
        assertEquals(ascList.get(8), new Integer(80));
        assertEquals(ascList.get(9), new Integer(90));
        assertEquals(ascList.get(10), new Integer(100));

        SortedList<Integer> descList = new SortedListAvl<Integer>(false);

        descList.add(null);
        descList.add(10);
        descList.add(20);
        descList.add(30);
        descList.add(40);
        descList.add(50);
        descList.add(60);
        descList.add(70);
        descList.add(80);
        descList.add(90);
        descList.add(100);

        assertNull(descList.get(10));
        assertEquals(descList.get(9), new Integer(10));
        assertEquals(descList.get(8), new Integer(20));
        assertEquals(descList.get(7), new Integer(30));
        assertEquals(descList.get(6), new Integer(40));
        assertEquals(descList.get(5), new Integer(50));
        assertEquals(descList.get(4), new Integer(60));
        assertEquals(descList.get(3), new Integer(70));
        assertEquals(descList.get(2), new Integer(80));
        assertEquals(descList.get(1), new Integer(90));
        assertEquals(descList.get(0), new Integer(100));

        SortedList<Integer> ascNgList = new SortedListAvl<Integer>(true, false);

        ascNgList.add(null);
        ascNgList.add(1);
        ascNgList.add(2);
        ascNgList.add(3);
        ascNgList.add(4);
        ascNgList.add(5);
        ascNgList.add(6);
        ascNgList.add(7);
        ascNgList.add(8);
        ascNgList.add(9);
        ascNgList.add(10);

        assertNull(ascNgList.get(10));
        assertEquals(new Integer(1), ascNgList.get(0));
        assertEquals(new Integer(2), ascNgList.get(1));
        assertEquals(new Integer(3), ascNgList.get(2));
        assertEquals(new Integer(4), ascNgList.get(3));
        assertEquals(new Integer(5), ascNgList.get(4));
        assertEquals(new Integer(6), ascNgList.get(5));
        assertEquals(new Integer(7), ascNgList.get(6));
        assertEquals(new Integer(8), ascNgList.get(7));
        assertEquals(new Integer(9), ascNgList.get(8));
        assertEquals(new Integer(10), ascNgList.get(9));

        SortedListAvl<Integer> descNgList = new SortedListAvl<>(false,
                false);

        descNgList.add((Integer)null);
        descNgList.add(1);
        descNgList.add(2);
        descNgList.add(3);
        descNgList.add(4);
        descNgList.add(5);
        descNgList.add(6);
        descNgList.add(7);
        descNgList.add(8);
        descNgList.add(9);
        descNgList.add(10);

        assertNull(descNgList.get(0));
        assertEquals(new Integer(1), descNgList.get(10));
        assertEquals(new Integer(2), descNgList.get(9));
        assertEquals(new Integer(3), descNgList.get(8));
        assertEquals(new Integer(4), descNgList.get(7));
        assertEquals(new Integer(5), descNgList.get(6));
        assertEquals(new Integer(6), descNgList.get(5));
        assertEquals(new Integer(7), descNgList.get(4));
        assertEquals(new Integer(8), descNgList.get(3));
        assertEquals(new Integer(9), descNgList.get(2));
        assertEquals(new Integer(10), descNgList.get(1));
    }

    @Test
    public void testSortOrder() {
        testSortOrder(new SortedListArray<Integer>());
        testSortOrder(new SortedListAvl<Integer>());
    }

    private static class IntegerValue implements Comparable<IntegerValue> {
        private int value;

        @SuppressWarnings("unused")
        public IntegerValue() {
            value = -1;
        }

        public IntegerValue(int v) {
            setValue(v);
        }

        public void setValue(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @Override
        public boolean equals(Object v) {
            return v instanceof IntegerValue && ((IntegerValue) v).getValue() == value;
        }

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public int compareTo(IntegerValue v) {
            return value - v.getValue();
        }
    }
}
