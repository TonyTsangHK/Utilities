package testng;

import static org.testng.Assert.*;

import java.util.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.data.*;

public class TestDoublySequence {
    private DoublyLinkedList<String> DefaultSequence;
    private DoublyNode<String> n1, n2, n3, n4, n5, n6, n7;
    private String s1, s2, s3, s4, s5, s6, s7;
    
    @BeforeMethod
    public void setUp() {
        s1 = "n1";
        s2 = "n2";
        s3 = "n3";
        s4 = "n4";
        s5 = "n5";
        s6 = "n6";
        s7 = "n7";
        
        n1 = new DoublyNode<String>(s1);
        n2 = new DoublyNode<String>(s2);
        n3 = new DoublyNode<String>(s3);
        n4 = new DoublyNode<String>(s4);
        n5 = new DoublyNode<String>(s5);
        n6 = new DoublyNode<String>(s6);
        n7 = new DoublyNode<String>(s7);
        
        DefaultSequence = new DoublyLinkedList<String>();
        DefaultSequence.add(n1);
        DefaultSequence.add(n2);
        DefaultSequence.add(n3);
        DefaultSequence.add(n4);
        DefaultSequence.add(n5);
        DefaultSequence.add(n6);
        DefaultSequence.add(n7);
    }
    
    @Test
    public void TestNodeConstruct() {
        DoublyNode<String> p = new DoublyNode<String>("P");
        DoublyNode<String> n = new DoublyNode<String>("N");
        
        DoublyNode<String> n1 = new DoublyNode<String>("n1");
        assertNull(n1.getPrevious());
        assertNull(n1.getNext());
        assertEquals(n1.getData(), "n1");
        
        DoublyNode<String> n2 = new DoublyNode<String>(null, n, "n2");
        assertNull(n2.getPrevious());
        assertEquals(n2.getNext(), n);
        assertEquals(n2.getData(), "n2");
        assertNull(n.getNext());
        assertEquals(n.getData(), "N");
        
        DoublyNode<String> n3 = new DoublyNode<String>(p, n, "n2");
        assertEquals(n3.getPrevious(), p);
        assertEquals(n3.getNext(), n);
        assertEquals(n3.getData(), "n2");
        assertNull(n.getNext());
        assertEquals(n.getData(), "N");
        assertNull(p.getPrevious());
        assertEquals(p.getData(), "P");
    }
    
    @Test
    public void TestNode() {
        assertEquals(DefaultSequence.getHead(), n1);
        assertEquals(n1.getNext(), n2);
        assertEquals(n2.getNext(), n3);
        assertEquals(n3.getNext(), n4);
        assertEquals(n4.getNext(), n5);
        assertEquals(n5.getNext(), n6);
        assertEquals(n6.getNext(), n7);
        assertNull(n7.getNext());
        assertEquals(n7.getPrevious(), n6);
        assertEquals(n6.getPrevious(), n5);
        assertEquals(n5.getPrevious(), n4);
        assertEquals(n4.getPrevious(), n3);
        assertEquals(n3.getPrevious(), n2);
        assertEquals(n2.getPrevious(), n1);
        assertNull(n1.getPrevious());
    }
    
    @Test
    public void TestInsertNode() {
        DoublyNode<String> newNode = new DoublyNode<String>("newNode");
        DefaultSequence.insertNode(0, newNode);
        
        assertEquals(DefaultSequence.getSize(), 8);
        assertEquals(DefaultSequence.getHead(), newNode);
        assertEquals(newNode.getNext(), n1);
        assertEquals(n1.getPrevious(), newNode);
        assertNull(newNode.getPrevious());
        
        DoublyNode<String> newNode2 = new DoublyNode<String>("newNode2");
        DefaultSequence.addToHead(newNode2);
        
        assertEquals(DefaultSequence.getSize(), 9);
        assertEquals(DefaultSequence.getHead(), newNode2);
        assertEquals(newNode2.getNext(), newNode);
        assertEquals(newNode.getNext(), n1);
        assertNull(newNode2.getPrevious());
        
        DoublyNode<String> newNode3 = new DoublyNode<String>("newNode3");
        DefaultSequence.addToTail(newNode3);
        
        assertEquals(DefaultSequence.getSize(), 10);
        assertEquals(newNode3.getPrevious(), n7);
        assertEquals(n7.getNext(), newNode3);
        assertNull(newNode3.getNext());
        
        DoublyNode<String> midNode = new DoublyNode<String>("midNode");
        DefaultSequence.insertNode(5, midNode);
        
        assertEquals(DefaultSequence.getSize(), 11);
        assertEquals(DefaultSequence.getNode(5), midNode);
        assertEquals(midNode.getPrevious(), n3);
        assertEquals(midNode.getNext(), n4);
        assertEquals(n3.getNext(), midNode);
        assertEquals(n4.getPrevious(), midNode);
    }
    
    @Test
    public void TestGetNode() {
        assertEquals(DefaultSequence.getNode(0), n1);
        assertEquals(DefaultSequence.getNode(1), n2);
        assertEquals(DefaultSequence.getNode(2), n3);
        assertEquals(DefaultSequence.getNode(3), n4);
        assertEquals(DefaultSequence.getNode(4), n5);
        assertEquals(DefaultSequence.getNode(5), n6);
        assertEquals(DefaultSequence.getNode(6), n7);
    }
    
    @Test
    public void TestRemoveNode() {
        DefaultSequence.removeHead();
        assertEquals(DefaultSequence.getSize(), 6);
        assertEquals(DefaultSequence.getNode(0), n2);
        assertEquals(DefaultSequence.getNode(1), n3);
        assertEquals(DefaultSequence.getNode(2), n4);
        assertEquals(DefaultSequence.getNode(3), n5);
        assertEquals(DefaultSequence.getNode(4), n6);
        assertEquals(DefaultSequence.getNode(5), n7);
        
        assertNull(n2.getPrevious());
        assertNull(n1.getPrevious());
        assertNull(n1.getNext());
        
        DefaultSequence.removeTail();
        assertEquals(DefaultSequence.getSize(), 5);
        assertEquals(DefaultSequence.getNode(0), n2);
        assertEquals(DefaultSequence.getNode(1), n3);
        assertEquals(DefaultSequence.getNode(2), n4);
        assertEquals(DefaultSequence.getNode(3), n5);
        assertEquals(DefaultSequence.getNode(4), n6);

        assertNull(n6.getNext());
        assertNull(n7.getPrevious());
        assertNull(n7.getNext());
        
        DefaultSequence.removeNode(n4);
        assertEquals(DefaultSequence.getSize(), 4);
        assertEquals(DefaultSequence.getNode(0), n2);
        assertEquals(DefaultSequence.getNode(1), n3);
        assertEquals(DefaultSequence.getNode(2), n5);
        assertEquals(DefaultSequence.getNode(3), n6);
        
        assertEquals(n3.getPrevious(), n2);
        assertEquals(n3.getNext(), n5);
        assertEquals(n2.getNext(), n3);
        assertEquals(n5.getPrevious(), n3);
        assertNull(n4.getPrevious());
        assertNull(n4.getNext());
    }
    
    @Test
    public void TestIterator() {
        ListIterator<String> iter = DefaultSequence.listIterator();
        /*LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(s1);
        linkedList.add(s2);
        linkedList.add(s3);
        linkedList.add(s4);
        linkedList.add(s5);
        linkedList.add(s6);
        linkedList.add(s7);
        ListIterator<String> iter = linkedList.listIterator();*/
        
        assertTrue(iter.hasNext());
        assertFalse(iter.hasPrevious());
        assertEquals(iter.nextIndex(), 0);
        assertEquals(iter.next(), n1.getData());
        assertEquals(iter.nextIndex(), 1);
        assertEquals(iter.next(), n2.getData());
        assertEquals(iter.nextIndex(), 2);
        assertEquals(iter.next(), n3.getData());
        assertEquals(iter.nextIndex(), 3);
        assertEquals(iter.next(), n4.getData());
        assertEquals(iter.nextIndex(), 4);
        assertEquals(iter.next(), n5.getData());
        assertEquals(iter.nextIndex(), 5);
        assertEquals(iter.next(), n6.getData());
        assertEquals(iter.nextIndex(), 6);
        assertEquals(iter.next(), n7.getData());
        assertEquals(iter.nextIndex(), 7);
        
        NoSuchElementException nsee = null;
        try {
            assertEquals(iter.next(), n7.getData());
        } catch (NoSuchElementException e) {
            nsee = e;
        }
        assertNotNull(nsee);
        nsee = null;
        
        assertEquals(iter.previousIndex(), 6);
        assertEquals(iter.previous(), n7.getData());
        assertEquals(iter.previousIndex(), 5);
        assertEquals(iter.previous(), n6.getData());
        assertEquals(iter.previousIndex(), 4);
        assertEquals(iter.previous(), n5.getData());
        assertEquals(iter.previousIndex(), 3);
        assertEquals(iter.previous(), n4.getData());
        assertEquals(iter.previousIndex(), 2);
        assertEquals(iter.previous(), n3.getData());
        assertEquals(iter.previousIndex(), 1);
        assertEquals(iter.previous(), n2.getData());
        assertEquals(iter.previousIndex(), 0);
        assertEquals(iter.previous(), n1.getData());
        
        try {
            assertEquals(iter.previous(), n1.getData());
        } catch (NoSuchElementException e) {
            nsee = e;
        }
        assertNotNull(nsee);
        nsee = null;
        
        iter.add("newNode");
        assertEquals(iter.previous(), "newNode");
        assertEquals(iter.next(), "newNode");
        iter.remove();
        assertEquals(iter.next(), n1.getData());
        assertEquals(iter.previous(), n1.getData());
        iter.remove();
        IllegalStateException ise = null;
        try {
            iter.remove();
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull(ise);
        ise = null;
        assertEquals(iter.next(), n2.getData());
    }
    
    @Test
    public void TestListIteratorWithStartIndex() {
        ListIterator<String> iter = DefaultSequence.listIterator(3);
        /*LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(s1);
        linkedList.add(s2);
        linkedList.add(s3);
        linkedList.add(s4);
        linkedList.add(s5);
        linkedList.add(s6);
        linkedList.add(s7);
        ListIterator<String> iter = linkedList.listIterator(3);*/
        
        assertTrue(iter.hasNext());
        assertTrue(iter.hasPrevious());
        assertEquals(iter.nextIndex(), 3);
        assertEquals(iter.next(), n4.getData());
        assertEquals(iter.nextIndex(), 4);
        assertEquals(iter.next(), n5.getData());
        assertEquals(iter.nextIndex(), 5);
        assertEquals(iter.next(), n6.getData());
        assertEquals(iter.nextIndex(), 6);
        assertEquals(iter.next(), n7.getData());
        assertEquals(iter.nextIndex(), 7);
        
        assertEquals(iter.previousIndex(), 6);
        assertEquals(iter.previous(), n7.getData());
        assertEquals(iter.previousIndex(), 5);
        assertEquals(iter.previous(), n6.getData());
        assertEquals(iter.previousIndex(), 4);
        assertEquals(iter.previous(), n5.getData());
        assertEquals(iter.previousIndex(), 3);
        assertEquals(iter.previous(), n4.getData());
        assertEquals(iter.previousIndex(), 2);
        assertEquals(iter.previous(), n3.getData());
        assertEquals(iter.previousIndex(), 1);
        assertEquals(iter.previous(), n2.getData());
        assertEquals(iter.previousIndex(), 0);
        assertEquals(iter.previous(), n1.getData());
        assertEquals(iter.previousIndex(), -1);
        
        NoSuchElementException nsee = null;
        try {
            assertEquals(iter.previous(), n1.getData());
        } catch (NoSuchElementException e) {
            nsee = e;
        }
        assertNotNull(nsee);
        nsee = null;
        
        assertEquals(iter.nextIndex(), 0);
        assertEquals(iter.next(), n1.getData());
        assertEquals(iter.nextIndex(), 1);
        assertEquals(iter.next(), n2.getData());
        assertEquals(iter.nextIndex(), 2);
        assertEquals(iter.next(), n3.getData());
        assertEquals(iter.nextIndex(), 3);
        assertEquals(iter.next(), n4.getData());
        assertEquals(iter.nextIndex(), 4);
        assertEquals(iter.next(), n5.getData());
        assertEquals(iter.nextIndex(), 5);
        assertEquals(iter.next(), n6.getData());
        assertEquals(iter.nextIndex(), 6);
        assertEquals(iter.next(), n7.getData());
        try {
            assertEquals(iter.next(), n7.getData());
        } catch (NoSuchElementException e) {
            nsee = e;
        }
        assertNotNull(nsee);
        nsee = null;
        
        iter.add("newNode");
        assertEquals(iter.previous(), "newNode");
        assertEquals(iter.previous(), n7.getData());
        assertEquals(iter.previousIndex(), 5);
        assertEquals(iter.nextIndex(), 6);
        iter.remove();
        assertEquals(iter.nextIndex(), 6);
        assertEquals(iter.previous(), n6.getData());
        iter.remove();
        IllegalStateException ise = null;
        try {
            iter.remove();
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull(ise);
        ise = null;
        assertEquals(iter.previous(), n5.getData());
    }
    
    @Test
    public void TestContainsNode() {
        assertTrue(DefaultSequence.containsNode(n1));
        assertTrue(DefaultSequence.containsNode(n2));
        assertTrue(DefaultSequence.containsNode(n3));
        assertTrue(DefaultSequence.containsNode(n4));
        assertTrue(DefaultSequence.containsNode(n5));
        assertTrue(DefaultSequence.containsNode(n6));
        assertTrue(DefaultSequence.containsNode(n7));
        
        assertFalse(DefaultSequence.containsNode(new DoublyNode<String>("n7")));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void TestContainsNodes() {
        assertTrue(DefaultSequence.containsNodes(n1, n2, n3, n4, n5, n6, n7));
        assertTrue(DefaultSequence.containsNodes(n1, n2, n3, n4, n6, n7));
        assertTrue(DefaultSequence.containsNodes(n1, n2, n7));
        assertTrue(DefaultSequence.containsNodes(n7, n1, n2, n3, n4, n5, n6, n7));
        assertFalse(DefaultSequence.containsNodes(n7, n1, n2, n3, n4, n5, n6, n7, new DoublyNode<String>("n5")));
    }
    
    @Test
    public void TestSubSequenceByIndex() {
        List<String> newSeq = DefaultSequence.subList(0, 7);
        assertEquals(newSeq.size(), 7);
        assertEquals(newSeq.get(0), n1.getData());
        assertEquals(newSeq.get(1), n2.getData());
        assertEquals(newSeq.get(2), n3.getData());
        assertEquals(newSeq.get(3), n4.getData());
        assertEquals(newSeq.get(4), n5.getData());
        assertEquals(newSeq.get(5), n6.getData());
        assertEquals(newSeq.get(6), n7.getData());
    }
    
    @Test 
    public void TestIndexOfNode() {
        assertEquals(DefaultSequence.indexOfNode(n1), 0);
        assertEquals(DefaultSequence.indexOfNode(n2), 1);
        assertEquals(DefaultSequence.indexOfNode(n3), 2);
        assertEquals(DefaultSequence.indexOfNode(n4), 3);
        assertEquals(DefaultSequence.indexOfNode(n5), 4);
        assertEquals(DefaultSequence.indexOfNode(n6), 5);
        assertEquals(DefaultSequence.indexOfNode(n7), 6);
        assertEquals(DefaultSequence.indexOfNode(null), -1);
        assertEquals(DefaultSequence.indexOfNode(new DoublyNode<String>("n1")), -1);
    }
    
    @Test
    public void TestIndexOfNodeWithStartIndex() {
        assertEquals(DefaultSequence.indexOfNode(n7, 4), 6);
        assertEquals(DefaultSequence.indexOfNode(n7, 5), 6);
        assertEquals(DefaultSequence.indexOfNode(n6, 6), -1);
    }
    
    @Test
    public void TestRemoveInteval() {
        DefaultSequence.removeInterval(n3, n6);
        
        assertEquals(DefaultSequence.getSize(), 3);
        
        assertEquals(DefaultSequence.getNode(0), n1);
        assertEquals(DefaultSequence.getNode(1), n2);
        assertEquals(DefaultSequence.getNode(2), n7);
        
        assertEquals(n2.getNext(), n7);
        assertEquals(n7.getPrevious(), n2);
    }
    
    @Test
    public void TestRemoveIntevalWithIndex() {
        DefaultSequence.removeInterval(2, 5);
        
        assertEquals(DefaultSequence.getSize(), 3);
        
        assertEquals(DefaultSequence.getNode(0), n1);
        assertEquals(DefaultSequence.getNode(1), n2);
        assertEquals(DefaultSequence.getNode(2), n7);
        
        assertEquals(n2.getNext(), n7);
        assertEquals(n7.getPrevious(), n2);
    }
    
    @Test
    public void TestSubSequenceByNode() {
        DoublyLinkedList<String> newSeq = DefaultSequence.subSequence(n7, n1);
        assertNull(newSeq);
        newSeq = DefaultSequence.subSequence(new DoublyNode<String>("n1"), n7);
        assertNull(newSeq);
        newSeq = DefaultSequence.subSequence(n1, n7);

        assertEquals(newSeq.getSize(), 7);
        assertNotSame(newSeq.getNode(0), n1);
        assertNotSame(newSeq.getNode(1), n2);
        assertNotSame(newSeq.getNode(2), n3);
        assertNotSame(newSeq.getNode(3), n4);
        assertNotSame(newSeq.getNode(4), n5);
        assertNotSame(newSeq.getNode(5), n6);
        assertNotSame(newSeq.getNode(6), n7);
        
        assertEquals(newSeq.getNode(0).getData(), n1.getData());
        assertEquals(newSeq.getNode(1).getData(), n2.getData());
        assertEquals(newSeq.getNode(2).getData(), n3.getData());
        assertEquals(newSeq.getNode(3).getData(), n4.getData());
        assertEquals(newSeq.getNode(4).getData(), n5.getData());
        assertEquals(newSeq.getNode(5).getData(), n6.getData());
        assertEquals(newSeq.getNode(6).getData(), n7.getData());
    }
    
    @Test
    public void TestAddAll() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(n1.getData());
        list.add(n4.getData());
        list.add(n6.getData());
        
        DefaultSequence.addAll(list);
        
        assertEquals(DefaultSequence.getNode(0).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(1).getData(), n2.getData());
        assertEquals(DefaultSequence.getNode(2).getData(), n3.getData());
        assertEquals(DefaultSequence.getNode(3).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(4).getData(), n5.getData());
        assertEquals(DefaultSequence.getNode(5).getData(), n6.getData());
        assertEquals(DefaultSequence.getNode(6).getData(), n7.getData());
        assertEquals(DefaultSequence.getNode(7).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(8).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(9).getData(), n6.getData());
        assertEquals(DefaultSequence.size(), 10);
    }
    
    @Test
    public void TestAddAllToHead() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(n1.getData());
        list.add(n4.getData());
        list.add(n6.getData());
        
        DefaultSequence.addAllToHead(list);
        
        assertEquals(DefaultSequence.getNode(0).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(1).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(2).getData(), n6.getData());
        assertEquals(DefaultSequence.getNode(3).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(4).getData(), n2.getData());
        assertEquals(DefaultSequence.getNode(5).getData(), n3.getData());
        assertEquals(DefaultSequence.getNode(6).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(7).getData(), n5.getData());
        assertEquals(DefaultSequence.getNode(8).getData(), n6.getData());
        assertEquals(DefaultSequence.getNode(9).getData(), n7.getData());
        assertEquals(DefaultSequence.size(), 10);
    }
    
    @Test
    public void TestAddAllWithIndex() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(n1.getData());
        list.add(n4.getData());
        list.add(n6.getData());
        
        DefaultSequence.addAll(3, list);
        
        assertEquals(DefaultSequence.getNode(0).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(1).getData(), n2.getData());
        assertEquals(DefaultSequence.getNode(2).getData(), n3.getData());
        assertEquals(DefaultSequence.getNode(3).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(4).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(5).getData(), n6.getData());
        assertEquals(DefaultSequence.getNode(6).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(7).getData(), n5.getData());
        assertEquals(DefaultSequence.getNode(8).getData(), n6.getData());
        assertEquals(DefaultSequence.getNode(9).getData(), n7.getData());
        assertEquals(DefaultSequence.size(), 10);
    }
    
    @Test
    public void TestRetainAll() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(n1.getData());
        list.add(n4.getData());
        list.add(n6.getData());
        
        DefaultSequence.addAll(list);
        DefaultSequence.retainAll(list);
        
        assertEquals(DefaultSequence.getNode(0).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(1).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(2).getData(), n6.getData());
        assertEquals(DefaultSequence.getNode(3).getData(), n1.getData());
        assertEquals(DefaultSequence.getNode(4).getData(), n4.getData());
        assertEquals(DefaultSequence.getNode(5).getData(), n6.getData());
        assertEquals(DefaultSequence.size(), 6);
    }
}
