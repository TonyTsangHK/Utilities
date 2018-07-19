package testng

import org.testng.Assert.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import utils.data.DoublyLinkedList
import utils.data.DoublyNode
import utils.exception.NonMemberNodeException
import java.util.*

class TestDoublySequence {
    private lateinit var defaultSequence: DoublyLinkedList<String?>
    private lateinit var n1: DoublyNode<String?>
    private lateinit var n2: DoublyNode<String?>
    private lateinit var n3: DoublyNode<String?>
    private lateinit var n4: DoublyNode<String?>
    private lateinit var n5: DoublyNode<String?>
    private lateinit var n6: DoublyNode<String?>
    private lateinit var n7: DoublyNode<String?>
    private var s1: String? = null
    private var s2: String? = null
    private var s3: String? = null
    private var s4: String? = null
    private var s5: String? = null
    private var s6: String? = null
    private var s7: String? = null

    @BeforeMethod
    fun setUp() {
        s1 = "n1"
        s2 = "n2"
        s3 = "n3"
        s4 = "n4"
        s5 = "n5"
        s6 = "n6"
        s7 = "n7"

        n1 = DoublyNode<String?>(s1)
        n2 = DoublyNode<String?>(s2)
        n3 = DoublyNode<String?>(s3)
        n4 = DoublyNode<String?>(s4)
        n5 = DoublyNode<String?>(s5)
        n6 = DoublyNode<String?>(s6)
        n7 = DoublyNode<String?>(s7)

        defaultSequence = DoublyLinkedList<String?>()
        defaultSequence.add(n1)
        defaultSequence.add(n2)
        defaultSequence.add(n3)
        defaultSequence.add(n4)
        defaultSequence.add(n5)
        defaultSequence.add(n6)
        defaultSequence.add(n7)
    }

    @Test
    fun TestNodeConstruct() {
        val p = DoublyNode("P")
        val n = DoublyNode("N")

        val n1 = DoublyNode("n1")
        assertNull(n1.previous)
        assertNull(n1.next)
        assertEquals(n1.data, "n1")

        val n2 = DoublyNode(null, n, "n2")
        assertNull(n2.previous)
        assertEquals(n2.next, n)
        assertEquals(n2.data, "n2")
        assertNull(n.next)
        assertEquals(n.data, "N")

        val n3 = DoublyNode(p, n, "n2")
        assertEquals(n3.previous, p)
        assertEquals(n3.next, n)
        assertEquals(n3.data, "n2")
        assertNull(n.next)
        assertEquals(n.data, "N")
        assertNull(p.previous)
        assertEquals(p.data, "P")
    }

    @Test
    fun TestNode() {
        assertEquals(defaultSequence.head, n1)
        assertEquals(n1.next, n2)
        assertEquals(n2.next, n3)
        assertEquals(n3.next, n4)
        assertEquals(n4.next, n5)
        assertEquals(n5.next, n6)
        assertEquals(n6.next, n7)
        assertNull(n7.next)
        assertEquals(n7.previous, n6)
        assertEquals(n6.previous, n5)
        assertEquals(n5.previous, n4)
        assertEquals(n4.previous, n3)
        assertEquals(n3.previous, n2)
        assertEquals(n2.previous, n1)
        assertNull(n1.previous)
    }
    
    private fun createIntegerList(range: IntRange): DoublyLinkedList<Int> {
        val list = DoublyLinkedList<Int>()
        
        for (i in range) {
            list.add(i)
        }
        
        return list
    }
    
    @Test
    fun TestRemoveRange() {
        var list = createIntegerList(0 .. 10)
        
        list.removeRange(2, 4)
        
        for (i in list.indices) {
            if (i < 2) {
                assertEquals(list.get(i), i)
            } else if (i >= 2) {
                assertEquals(list.get(i), i + 3)
            }
        }

        assertEquals(list.size, 8)
        
        list = createIntegerList(0 .. 10)
        
        list.removeRange(0, 10)
        
        assertEquals(list.size, 0)

        list = createIntegerList(0 .. 10)
        
        list.removeRange(0, 5)
        
        for (i in list.indices) {
            assertEquals(list.get(i), i+6)
        }

        assertEquals(list.size, 5)
        
        list = createIntegerList(0 .. 10)
        
        list.removeRange(5, 10)
        
        for (i in list.indices) {
            assertEquals(list.get(i), i)
        }
        
        assertEquals(list.size, 5)
    }
    
    @Test
    fun testIndexOutOfBoundException() {
        assertException(
            {
                defaultSequence.get(-1)
            },
            IndexOutOfBoundsException::class.java
        )
        
        assertException(
            {
                defaultSequence.get(defaultSequence.size)
            },
            IndexOutOfBoundsException::class.java
        )
        
        assertException(
            {
                defaultSequence.subList(-1, defaultSequence.size)
            },
            IndexOutOfBoundsException::class.java
        )
        
        assertException(
            {
                defaultSequence.subSequence(defaultSequence.getNode(2), defaultSequence.getNode(0))
            },
            NonMemberNodeException::class.java
        )
        
        assertException(
            {
                defaultSequence.subSequence(defaultSequence.getNode(0), DoublyNode<String?>(""))
            },
            NonMemberNodeException::class.java
        )
    }
    
    fun assertException(runner: () -> Unit, exceptionClass: Class<out Exception>, message: String? = null) {
        var caughtException: Exception? = null
        try {
            runner()
        } catch (e: Exception) {
            caughtException = e
        }
        
        if (caughtException == null || !exceptionClass.isInstance(caughtException)) {
            if (message != null) {
                fail(message)
            } else {
                fail("Excepting exception: ${exceptionClass.name}, but found ${caughtException?.javaClass?.name}")
            }
        }
    }

    @Test
    fun TestInsertNode() {
        val newNode = DoublyNode<String?>("newNode")
        defaultSequence.insertNode(0, newNode)

        assertEquals(defaultSequence.size, 8)
        assertEquals(defaultSequence.head, newNode)
        assertEquals(newNode.next, n1)
        assertEquals(n1.previous, newNode)
        assertNull(newNode.previous)

        val newNode2 = DoublyNode<String?>("newNode2")
        defaultSequence.addToHead(newNode2)

        assertEquals(defaultSequence.size, 9)
        assertEquals(defaultSequence.head, newNode2)
        assertEquals(newNode2.next, newNode)
        assertEquals(newNode.next, n1)
        assertNull(newNode2.previous)

        val newNode3 = DoublyNode<String?>("newNode3")
        defaultSequence.addToTail(newNode3)

        assertEquals(defaultSequence.size, 10)
        assertEquals(newNode3.previous, n7)
        assertEquals(n7.next, newNode3)
        assertNull(newNode3.next)

        val midNode = DoublyNode<String?>("midNode")
        defaultSequence.insertNode(5, midNode)

        assertEquals(defaultSequence.size, 11)
        assertEquals(defaultSequence.getNode(5), midNode)
        assertEquals(midNode.previous, n3)
        assertEquals(midNode.next, n4)
        assertEquals(n3.next, midNode)
        assertEquals(n4.previous, midNode)
    }

    @Test
    fun TestGetNode() {
        assertEquals(defaultSequence.getNode(0), n1)
        assertEquals(defaultSequence.getNode(1), n2)
        assertEquals(defaultSequence.getNode(2), n3)
        assertEquals(defaultSequence.getNode(3), n4)
        assertEquals(defaultSequence.getNode(4), n5)
        assertEquals(defaultSequence.getNode(5), n6)
        assertEquals(defaultSequence.getNode(6), n7)
    }

    @Test
    fun TestRemoveNode() {
        defaultSequence.removeHead()
        assertEquals(defaultSequence.size, 6)
        assertEquals(defaultSequence.getNode(0), n2)
        assertEquals(defaultSequence.getNode(1), n3)
        assertEquals(defaultSequence.getNode(2), n4)
        assertEquals(defaultSequence.getNode(3), n5)
        assertEquals(defaultSequence.getNode(4), n6)
        assertEquals(defaultSequence.getNode(5), n7)

        assertNull(n2.previous)
        assertNull(n1.previous)
        assertNull(n1.next)

        defaultSequence.removeTail()
        assertEquals(defaultSequence.size, 5)
        assertEquals(defaultSequence.getNode(0), n2)
        assertEquals(defaultSequence.getNode(1), n3)
        assertEquals(defaultSequence.getNode(2), n4)
        assertEquals(defaultSequence.getNode(3), n5)
        assertEquals(defaultSequence.getNode(4), n6)

        assertNull(n6.next)
        assertNull(n7.previous)
        assertNull(n7.next)

        defaultSequence.removeNode(n4)
        assertEquals(defaultSequence.size, 4)
        assertEquals(defaultSequence.getNode(0), n2)
        assertEquals(defaultSequence.getNode(1), n3)
        assertEquals(defaultSequence.getNode(2), n5)
        assertEquals(defaultSequence.getNode(3), n6)

        assertEquals(n3.previous, n2)
        assertEquals(n3.next, n5)
        assertEquals(n2.next, n3)
        assertEquals(n5.previous, n3)
        assertNull(n4.previous)
        assertNull(n4.next)
    }

    @Test
    fun TestIterator() {
        val iter = defaultSequence.listIterator()
        /*LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(s1);
        linkedList.add(s2);
        linkedList.add(s3);
        linkedList.add(s4);
        linkedList.add(s5);
        linkedList.add(s6);
        linkedList.add(s7);
        ListIterator<String> iter = linkedList.listIterator();*/

        assertTrue(iter.hasNext())
        assertFalse(iter.hasPrevious())
        assertEquals(iter.nextIndex(), 0)
        assertEquals(iter.next(), n1.data)
        assertEquals(iter.nextIndex(), 1)
        assertEquals(iter.next(), n2.data)
        assertEquals(iter.nextIndex(), 2)
        assertEquals(iter.next(), n3.data)
        assertEquals(iter.nextIndex(), 3)
        assertEquals(iter.next(), n4.data)
        assertEquals(iter.nextIndex(), 4)
        assertEquals(iter.next(), n5.data)
        assertEquals(iter.nextIndex(), 5)
        assertEquals(iter.next(), n6.data)
        assertEquals(iter.nextIndex(), 6)
        assertEquals(iter.next(), n7.data)
        assertEquals(iter.nextIndex(), 7)

        var nsee: NoSuchElementException? = null
        try {
            assertEquals(iter.next(), n7.data)
        } catch (e: NoSuchElementException) {
            nsee = e
        }

        assertNotNull(nsee)
        nsee = null

        assertEquals(iter.previousIndex(), 6)
        assertEquals(iter.previous(), n7.data)
        assertEquals(iter.previousIndex(), 5)
        assertEquals(iter.previous(), n6.data)
        assertEquals(iter.previousIndex(), 4)
        assertEquals(iter.previous(), n5.data)
        assertEquals(iter.previousIndex(), 3)
        assertEquals(iter.previous(), n4.data)
        assertEquals(iter.previousIndex(), 2)
        assertEquals(iter.previous(), n3.data)
        assertEquals(iter.previousIndex(), 1)
        assertEquals(iter.previous(), n2.data)
        assertEquals(iter.previousIndex(), 0)
        assertEquals(iter.previous(), n1.data)

        try {
            assertEquals(iter.previous(), n1.data)
        } catch (e: NoSuchElementException) {
            nsee = e
        }

        assertNotNull(nsee)
        nsee = null

        iter.add("newNode")
        assertEquals(iter.previous(), "newNode")
        assertEquals(iter.next(), "newNode")
        iter.remove()
        assertEquals(iter.next(), n1.data)
        assertEquals(iter.previous(), n1.data)
        iter.remove()
        var ise: IllegalStateException? = null
        try {
            iter.remove()
        } catch (e: IllegalStateException) {
            ise = e
        }

        assertNotNull(ise)
        ise = null
        assertEquals(iter.next(), n2.data)
    }

    @Test
    fun TestListIteratorWithStartIndex() {
        val iter = defaultSequence.listIterator(3)
        /*LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(s1);
        linkedList.add(s2);
        linkedList.add(s3);
        linkedList.add(s4);
        linkedList.add(s5);
        linkedList.add(s6);
        linkedList.add(s7);
        ListIterator<String> iter = linkedList.listIterator(3);*/

        assertTrue(iter.hasNext())
        assertTrue(iter.hasPrevious())
        assertEquals(iter.nextIndex(), 3)
        assertEquals(iter.next(), n4.data)
        assertEquals(iter.nextIndex(), 4)
        assertEquals(iter.next(), n5.data)
        assertEquals(iter.nextIndex(), 5)
        assertEquals(iter.next(), n6.data)
        assertEquals(iter.nextIndex(), 6)
        assertEquals(iter.next(), n7.data)
        assertEquals(iter.nextIndex(), 7)

        assertEquals(iter.previousIndex(), 6)
        assertEquals(iter.previous(), n7.data)
        assertEquals(iter.previousIndex(), 5)
        assertEquals(iter.previous(), n6.data)
        assertEquals(iter.previousIndex(), 4)
        assertEquals(iter.previous(), n5.data)
        assertEquals(iter.previousIndex(), 3)
        assertEquals(iter.previous(), n4.data)
        assertEquals(iter.previousIndex(), 2)
        assertEquals(iter.previous(), n3.data)
        assertEquals(iter.previousIndex(), 1)
        assertEquals(iter.previous(), n2.data)
        assertEquals(iter.previousIndex(), 0)
        assertEquals(iter.previous(), n1.data)
        assertEquals(iter.previousIndex(), -1)

        var nsee: NoSuchElementException? = null
        try {
            assertEquals(iter.previous(), n1.data)
        } catch (e: NoSuchElementException) {
            nsee = e
        }

        assertNotNull(nsee)
        nsee = null

        assertEquals(iter.nextIndex(), 0)
        assertEquals(iter.next(), n1.data)
        assertEquals(iter.nextIndex(), 1)
        assertEquals(iter.next(), n2.data)
        assertEquals(iter.nextIndex(), 2)
        assertEquals(iter.next(), n3.data)
        assertEquals(iter.nextIndex(), 3)
        assertEquals(iter.next(), n4.data)
        assertEquals(iter.nextIndex(), 4)
        assertEquals(iter.next(), n5.data)
        assertEquals(iter.nextIndex(), 5)
        assertEquals(iter.next(), n6.data)
        assertEquals(iter.nextIndex(), 6)
        assertEquals(iter.next(), n7.data)
        try {
            assertEquals(iter.next(), n7.data)
        } catch (e: NoSuchElementException) {
            nsee = e
        }

        assertNotNull(nsee)
        nsee = null

        iter.add("newNode")
        assertEquals(iter.previous(), "newNode")
        assertEquals(iter.previous(), n7.data)
        assertEquals(iter.previousIndex(), 5)
        assertEquals(iter.nextIndex(), 6)
        iter.remove()
        assertEquals(iter.nextIndex(), 6)
        assertEquals(iter.previous(), n6.data)
        iter.remove()
        var ise: IllegalStateException? = null
        try {
            iter.remove()
        } catch (e: IllegalStateException) {
            ise = e
        }

        assertNotNull(ise)
        ise = null
        assertEquals(iter.previous(), n5.data)
    }

    @Test
    fun TestContainsNode() {
        assertTrue(defaultSequence.containsNode(n1))
        assertTrue(defaultSequence.containsNode(n2))
        assertTrue(defaultSequence.containsNode(n3))
        assertTrue(defaultSequence.containsNode(n4))
        assertTrue(defaultSequence.containsNode(n5))
        assertTrue(defaultSequence.containsNode(n6))
        assertTrue(defaultSequence.containsNode(n7))

        assertFalse(defaultSequence.containsNode(DoublyNode("n7")))
    }

    @SuppressWarnings("unchecked")
    @Test
    fun TestContainsNodes() {
        assertTrue(defaultSequence.containsNodes(n1, n2, n3, n4, n5, n6, n7))
        assertTrue(defaultSequence.containsNodes(n1, n2, n3, n4, n6, n7))
        assertTrue(defaultSequence.containsNodes(n1, n2, n7))
        assertTrue(defaultSequence.containsNodes(n7, n1, n2, n3, n4, n5, n6, n7))
        assertFalse(defaultSequence.containsNodes(n7, n1, n2, n3, n4, n5, n6, n7, DoublyNode("n5")))
    }

    @Test
    fun TestSubSequenceByIndex() {
        val newSeq = defaultSequence.getSubList(0, 7)!!
        assertEquals(newSeq.size, 7)
        assertEquals(newSeq[0], n1.data)
        assertEquals(newSeq[1], n2.data)
        assertEquals(newSeq[2], n3.data)
        assertEquals(newSeq[3], n4.data)
        assertEquals(newSeq[4], n5.data)
        assertEquals(newSeq[5], n6.data)
        assertEquals(newSeq[6], n7.data)
    }

    @Test
    fun TestIndexOfNode() {
        assertEquals(defaultSequence.indexOfNode(n1), 0)
        assertEquals(defaultSequence.indexOfNode(n2), 1)
        assertEquals(defaultSequence.indexOfNode(n3), 2)
        assertEquals(defaultSequence.indexOfNode(n4), 3)
        assertEquals(defaultSequence.indexOfNode(n5), 4)
        assertEquals(defaultSequence.indexOfNode(n6), 5)
        assertEquals(defaultSequence.indexOfNode(n7), 6)
        assertEquals(defaultSequence.indexOfNode(null), -1)
        assertEquals(defaultSequence.indexOfNode(DoublyNode("n1")), -1)
    }

    @Test
    fun TestIndexOfNodeWithStartIndex() {
        assertEquals(defaultSequence.indexOfNode(n7, 4), 6)
        assertEquals(defaultSequence.indexOfNode(n7, 5), 6)
        assertEquals(defaultSequence.indexOfNode(n6, 6), -1)
    }

    @Test
    fun TestRemoveInteval() {
        defaultSequence.removeInterval(n3, n6)

        assertEquals(defaultSequence.size, 3)

        assertEquals(defaultSequence.getNode(0), n1)
        assertEquals(defaultSequence.getNode(1), n2)
        assertEquals(defaultSequence.getNode(2), n7)

        assertEquals(n2.next, n7)
        assertEquals(n7.previous, n2)
    }

    @Test
    fun TestRemoveIntevalWithIndex() {
        defaultSequence.removeInterval(2, 5)

        assertEquals(defaultSequence.size, 3)

        assertEquals(defaultSequence.getNode(0), n1)
        assertEquals(defaultSequence.getNode(1), n2)
        assertEquals(defaultSequence.getNode(2), n7)

        assertEquals(n2.next, n7)
        assertEquals(n7.previous, n2)
    }

    @Test
    fun TestSubSequenceByNode() {
        assertException(
            {
                defaultSequence.subSequence(n7, n1)
            },
            NonMemberNodeException::class.java
        )
        
        assertException(
            {
                defaultSequence.subSequence(DoublyNode("n1"), n7)
            },
            NonMemberNodeException::class.java
        )

        val newSeq: DoublyLinkedList<String?> = defaultSequence.subSequence(n1, n7)

        assertEquals(newSeq.size, 7)
        assertNotSame(newSeq.getNode(0), n1)
        assertNotSame(newSeq.getNode(1), n2)
        assertNotSame(newSeq.getNode(2), n3)
        assertNotSame(newSeq.getNode(3), n4)
        assertNotSame(newSeq.getNode(4), n5)
        assertNotSame(newSeq.getNode(5), n6)
        assertNotSame(newSeq.getNode(6), n7)

        assertEquals(newSeq.getNode(0).data, n1.data)
        assertEquals(newSeq.getNode(1).data, n2.data)
        assertEquals(newSeq.getNode(2).data, n3.data)
        assertEquals(newSeq.getNode(3).data, n4.data)
        assertEquals(newSeq.getNode(4).data, n5.data)
        assertEquals(newSeq.getNode(5).data, n6.data)
        assertEquals(newSeq.getNode(6).data, n7.data)
    }

    @Test
    fun TestAddAll() {
        val list = ArrayList<String?>()
        list.add(n1.data)
        list.add(n4.data)
        list.add(n6.data)

        defaultSequence.addAll(list)

        assertEquals(defaultSequence.getNode(0).data, n1.data)
        assertEquals(defaultSequence.getNode(1).data, n2.data)
        assertEquals(defaultSequence.getNode(2).data, n3.data)
        assertEquals(defaultSequence.getNode(3).data, n4.data)
        assertEquals(defaultSequence.getNode(4).data, n5.data)
        assertEquals(defaultSequence.getNode(5).data, n6.data)
        assertEquals(defaultSequence.getNode(6).data, n7.data)
        assertEquals(defaultSequence.getNode(7).data, n1.data)
        assertEquals(defaultSequence.getNode(8).data, n4.data)
        assertEquals(defaultSequence.getNode(9).data, n6.data)
        assertEquals(defaultSequence.size, 10)
    }

    @Test
    fun TestAddAllToHead() {
        val list = ArrayList<String?>()
        list.add(n1.data)
        list.add(n4.data)
        list.add(n6.data)

        defaultSequence.addAllToHead(list)

        assertEquals(defaultSequence.getNode(0).data, n1.data)
        assertEquals(defaultSequence.getNode(1).data, n4.data)
        assertEquals(defaultSequence.getNode(2).data, n6.data)
        assertEquals(defaultSequence.getNode(3).data, n1.data)
        assertEquals(defaultSequence.getNode(4).data, n2.data)
        assertEquals(defaultSequence.getNode(5).data, n3.data)
        assertEquals(defaultSequence.getNode(6).data, n4.data)
        assertEquals(defaultSequence.getNode(7).data, n5.data)
        assertEquals(defaultSequence.getNode(8).data, n6.data)
        assertEquals(defaultSequence.getNode(9).data, n7.data)
        assertEquals(defaultSequence.size, 10)
    }

    @Test
    fun TestAddAllWithIndex() {
        val list = ArrayList<String?>()
        list.add(n1.data)
        list.add(n4.data)
        list.add(n6.data)

        defaultSequence.addAll(3, list)

        assertEquals(defaultSequence.getNode(0).data, n1.data)
        assertEquals(defaultSequence.getNode(1).data, n2.data)
        assertEquals(defaultSequence.getNode(2).data, n3.data)
        assertEquals(defaultSequence.getNode(3).data, n1.data)
        assertEquals(defaultSequence.getNode(4).data, n4.data)
        assertEquals(defaultSequence.getNode(5).data, n6.data)
        assertEquals(defaultSequence.getNode(6).data, n4.data)
        assertEquals(defaultSequence.getNode(7).data, n5.data)
        assertEquals(defaultSequence.getNode(8).data, n6.data)
        assertEquals(defaultSequence.getNode(9).data, n7.data)
        assertEquals(defaultSequence.size, 10)
    }

    @Test
    fun TestRetainAll() {
        val list = ArrayList<String?>()
        list.add(n1.data)
        list.add(n4.data)
        list.add(n6.data)

        defaultSequence.addAll(list)
        defaultSequence.retainAll(list)

        assertEquals(defaultSequence.getNode(0).data, n1.data)
        assertEquals(defaultSequence.getNode(1).data, n4.data)
        assertEquals(defaultSequence.getNode(2).data, n6.data)
        assertEquals(defaultSequence.getNode(3).data, n1.data)
        assertEquals(defaultSequence.getNode(4).data, n4.data)
        assertEquals(defaultSequence.getNode(5).data, n6.data)
        assertEquals(defaultSequence.size, 6)
    }
}
