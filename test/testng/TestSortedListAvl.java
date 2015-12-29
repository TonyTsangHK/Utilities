package testng;

import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.*;
import utils.math.MathUtil;

public class TestSortedListAvl {
    private int testSize = 10000;
    
    private SortedList<Integer> list;
    
    @BeforeMethod
    public void setup() {
        list = new SortedListAvl<Integer>();
        
        ArrayList<Integer> aList = new ArrayList<>(testSize);
        
        for (int i = 1; i <= testSize; i++) {
            aList.add(i);
        }
        
        while (!aList.isEmpty()) {
            int n = MathUtil.randomInteger(0, aList.size()-1);
            
            list.add(aList.get(n));
            aList.remove(n);
        }
    }
    
    @Test
    public void testAdd() {
        SortedList<Integer> list = new SortedListAvl<>();
        ArrayList<Integer> tempList = new ArrayList<>(testSize);
        
        for (int i = 0; i < testSize; i++) {
            tempList.add(i + 1);
        }
        
        while (!tempList.isEmpty()) {
            int n = MathUtil.randomInteger(0, tempList.size()-1);
            
            list.add(tempList.get(n));
            tempList.remove(n);
        }
        
        assertEquals(list.size(), testSize);
        
        Object[] array = list.toArray();
        assertEquals(array.length, list.size());
        
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], i + 1);
        }
        
        Integer[] iArray = new Integer[testSize];
        list.toArray(iArray);
        assertEquals(list.size(), iArray.length);
        
        for (int i = 0; i < iArray.length; i++) {
            assertEquals(iArray[i], new Integer(i+1));
        }
    }

    @Test
    public void testAddDuplicate() {
        SortedList<Integer> list = new SortedListAvl<>();

        for (int i = 0; i < testSize; i++) {
            list.add(1, true);
        }

        assertEquals(list.size(), testSize);

        int sum = 0;
        for (int v : list) {
            sum += v;
        }

        assertEquals(sum, testSize);

        list.clear();

        for (int i = 0; i < testSize; i++) {
            if (i % 2 == 0) {
                list.add(1, false);
            } else {
                list.add(2, false);
            }
        }

        assertEquals(list.size(), 2);

        sum = 0;
        for (int v : list) {
            sum += v;
        }

        assertEquals(sum, 3);
    }

    @Test
    public void testAddAllDuplicate() {
        SortedListAvl<Integer> list = new SortedListAvl<>();
        List<Integer> duplicatedListElements = new ArrayList<>(testSize*2), uniqueListElements = new ArrayList<>(testSize);

        for (int i = 0; i < testSize; i++) {
            duplicatedListElements.add(i);
            duplicatedListElements.add(i);
            uniqueListElements.add(i);
        }

        list.addAll(duplicatedListElements, true);

        assertEquals(list.size(), duplicatedListElements.size());

        assertEquals(DataManipulator.integerSumOf(list), MathUtil.sumOfSeq(0, testSize-1)*2);

        list.clear();

        list.addAll(duplicatedListElements, false);

        assertEquals(list.size(), uniqueListElements.size());

        MathUtil.sumOfSeq(0, testSize);

        assertEquals(DataManipulator.integerSumOf(list), MathUtil.sumOfSeq(0, testSize-1));
    }
    
    @Test
    public void testGet() {
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), new Integer(i+1));
        }
    }
    
    @Test
    public void testRemove() {
        int count = 0, index;
        while (!list.isEmpty()) {
            try {
                index = MathUtil.randomInteger(0, list.size()-1);
                Integer integer = list.remove(index);
                count++;
                assertEquals(list.size(), testSize-count);
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
    public void testIndexOf() {
        for (int i = 0; i < testSize; i++) {
            int n = MathUtil.randomInteger(1, testSize);
            int index = list.indexOf(n);
            assertEquals(new Integer(index), new Integer(n-1));
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
                assertEquals(list.get(i), new Integer(i-9), "i: " + i);
            }
        }
        
        assertEquals(list.indexOf(55), 55);
        assertEquals(list.lastIndexOf(55), 64);
    }
    
    @Test
    public void testTreeNodeNavigation() {
        List<BinaryTreeNode<Integer>> treeNodes = new ArrayList<>();
        
        for (int i = 1; i <= 13; i++) {
            treeNodes.add(new BinaryTreeNode<>(i, null, null, null));
        }
        
        BinaryTreeNodeBuilder<Integer> builder = new BinaryTreeNodeBuilder<>();
        
        builder.begin(7)
            .left(4)
            .branch("a")
            .left(2)
            .left(1)
            .left(0)
            .parent()
            .parent()
            .right(3)
            .branchRoot("a")
            .right(6)
            .left(5)
            .root()
            .right(11)
            .branch("b")
            .left(9)
            .left(8)
            .parent()
            .right(10)
            .branchRoot("b")
            .right(12)
            .right(13);
        
        BinaryTreeNode<Integer> root = builder.getRootNode();
        
        BinaryTreeNode<Integer> prev = root, next = root;
        
        for (int i = 6; i >= 0; i--) {
            prev = prev.getPreviousNode();
            
            assertEquals(prev.getElement(), new Integer(i));
        }
        
        for (int i = 8; i <= 13; i++) {
            next = next.getNextNode();
            
            assertEquals(next.getElement(), new Integer(i));
        }
        assertEquals(root.getElement(), new Integer(7));
        
        for (int i = 0; i <= 13; i++) {
            assertEquals(prev.getElement(), new Integer(i));
            
            prev = prev.getNextNode();
        }
        
        assertNull(prev);
        
        for (int i = 13; i >= 0; i--) {
            assertEquals(next.getElement(), new Integer(i));
            
            next = next.getPreviousNode();
        }
        
        assertNull(next);
        
        root.refreshMetaData();
        assertEquals(root.getLeftDepth(), 4);
        assertEquals(root.getRightDepth(), 3);
        assertEquals(root.getDepth(), 4);
        
        assertEquals(root.getLeftNodeCount(), 7);
        assertEquals(root.getRightNodeCount(), 6);
        assertEquals(root.getNodeCount(), 13);
    }
    
    @Test
    public void testIterator() {
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
    public void testSet() {
        SortedList<Integer> list = new SortedListAvl<>();
        
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
    public void testContains() {
        for (int i = -testSize; i <= testSize; i++) {
            if (i > 0) {
                assertTrue(list.contains(i), "Value must exist: " + i);
            } else {
                assertFalse(list.contains(i), "Value must not exist: " + i);
            }
        }
    }
    
    @Test
    public void testRemoveAll() {
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
        assertEquals(list.size(), testSize-arrayList.size());
        
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
    public void testRetainAll() {
        int size = MathUtil.randomInteger(testSize / 2, testSize);
        ArrayList<Integer> arrayList = new ArrayList<>(size);
        
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

        SortedListAvl<Integer> avlSortedList = new SortedListAvl<>();

        avlSortedList.add(10);
        avlSortedList.add(10);
        avlSortedList.add(10);

        List<Integer> retainList = new ArrayList<>();
        retainList.add(10);

        boolean avlListModified = avlSortedList.retainAll(retainList);

        if (avlListModified) {
            fail("All values should be retained in SortedListAvl!");
        }
    }
    
    @Test
    public void testClone() {
        SortedList<Integer> clonedList = list.clone();
        
        assertEquals(new Integer(clonedList.size()), new Integer(list.size()));
        
        for (int i = 0; i < clonedList.size(); i++) {
            assertEquals(list.get(i), clonedList.get(i));
        }
        
        int index = MathUtil.randomInteger(0, clonedList.size()-1);
        Integer integer = clonedList.remove(index);
        
        assertEquals(new Integer(clonedList.size()), new Integer(list.size() - 1));
        assertEquals(integer, list.get(index));
        
        for (int i = 0; i < list.size(); i++) {
            if (i < index) {
                assertEquals(clonedList.get(i), list.get(i));
            } else if (i > index) {
                assertEquals(clonedList.get(i-1), list.get(i));
            }
        }
        
        clonedList.add(integer);
        
        for (int i = 0; i < clonedList.size(); i++) {
            assertEquals(list.get(i), clonedList.get(i));
        }
    }
    
    @Test
    public void testIterAddSetRemove() {
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
    public void testIndexOfWithSameValues() {
        SortedList<Integer> sList = new SortedListAvl<Integer>();
        
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
    public void testGetSubList() {
        int fromIndex = MathUtil.randomInteger(0, list.size() - 5),
            toIndex = MathUtil.randomInteger(fromIndex+1, list.size()-1);
        SortedList<Integer> subList = list.getSubList(fromIndex, toIndex);
        
        assertEquals(new Integer(toIndex - fromIndex), new Integer(subList.size()));
        
        for (int i = fromIndex; i < toIndex; i++) {
            assertEquals(list.get(i), subList.get(i-fromIndex));
        }
    }
    
    @Test
    public void testForEachLoop() {
        int count = 1;
        for (Integer integer : list) {
            assertEquals(integer, new Integer(count++));
        }
    }
    
    @Test
    public void testResort() {
        ArrayList<IntegerValue> arrayList = new ArrayList<IntegerValue>();
        SortedList<IntegerValue> sortedList = new SortedListAvl<IntegerValue>();
        
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
        assertFalse(allInOrder, "A random order order sortedList is expected!");
        
        Collections.sort(arrayList);
        
        sortedList.resort();
        IntegerValue[] arr = new IntegerValue[sortedList.size()];
        sortedList.toArray(arr);
        for (int i = 0; i < sortedList.size() || i < arrayList.size(); i++) {
            assertEquals(sortedList.get(i), arrayList.get(i));
        }
    }
    
    @Test
    public void testNullValues() {
        assertTrue(list.add(null));
        assertTrue(list.add(null));
        
        assertNull(list.getMin());
        assertNull(list.get(0));
        assertNull(list.get(1));
        
        for (int i = 2; i < list.size(); i++) {
            assertEquals(list.get(i), new Integer(i-1));
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
            assertEquals(list.get(i-1), new Integer(i));
        }
    }
    
    @Test
    public void testSortOrder() {
        SortedList<Integer> ascList = new SortedListAvl<Integer>();
        
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
        
        SortedListAvl<Integer> descNgList = new SortedListAvl<Integer>(false, false);
        
        descNgList.add(null);
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
