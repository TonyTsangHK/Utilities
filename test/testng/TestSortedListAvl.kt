package testng

import org.testng.Assert.*
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import utils.data.*
import utils.math.MathUtil
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-05
 * Time: 14:29
 */
class TestSortedListAvl {
    companion object {
        data class IntegerValue(var value: Int): Comparable<IntegerValue> {
            constructor() : this(-1)

            override fun toString(): String {
                return value.toString()
            }

            override fun compareTo(other: IntegerValue): Int {
                return value - other.value
            }
        }
    }

    private var testSize = 10000

    private lateinit var list: SortedList<Int?>

    @BeforeMethod
    fun setup() {
        list = SortedListAvl<Int?>()

        val aList = ArrayList<Int>(testSize)

        for (i in 1 .. testSize) {
            aList.add(i)
        }

        while (!aList.isEmpty()) {
            val n = MathUtil.randomInteger(0, aList.size-1)

            list.add(aList[n])
            aList.removeAt(n)
        }
    }

    @Test
    fun testAdd() {
        val list = SortedListAvl<Int>()
        val tempList = ArrayList<Int>(testSize)

        for (i in 0 .. testSize - 1) {
            tempList.add(i + 1)
        }

        while (!tempList.isEmpty()) {
            val n = MathUtil.randomInteger(0, tempList.size-1)

            list.add(tempList[n])
            tempList.removeAt(n)
        }

        assertEquals(list.size, testSize)

        val generalList = list.toGeneralList()

        assertEquals(generalList.size, list.size)

        for (i in 0 .. generalList.size - 1) {
            assertEquals(generalList[i], i + 1)
        }
    }

    @Test
    fun testAddDuplicate() {
        val list = SortedListAvl<Int>()

        for (i in 0 .. testSize - 1) {
            list.add(1, true)
        }

        assertEquals(list.size, testSize)

        var sum = list.reduce { p, v -> p + v }

        assertEquals(sum, testSize)

        list.clear()

        for (i in 0 .. testSize - 1) {
            if (i % 2 == 0) {
                list.add(1, false)
            } else {
                list.add(2, false)
            }
        }

        assertEquals(list.size, 2)

        sum = list.reduce { p, v -> p + v }

        assertEquals(sum, 3)
    }

    @Test
    fun testAddAllDuplicate() {
        val list = SortedListAvl<Int>()
        val duplicatedListElements = ArrayList<Int>(testSize*2)
        val uniqueListElements = ArrayList<Int>(testSize)

        for (i in 0 .. testSize-1) {
            duplicatedListElements.add(i)
            duplicatedListElements.add(i)
            uniqueListElements.add(i)
        }

        list.addAll(duplicatedListElements, true)

        assertEquals(list.size, duplicatedListElements.size)
        
        assertEquals(DataManipulator.integerSumOf(list).toLong(), MathUtil.sumOfSeq(0, testSize-1)*2)

        list.clear()

        list.addAll(duplicatedListElements, false)

        assertEquals(list.size, uniqueListElements.size)

        MathUtil.sumOfSeq(0, testSize)

        assertEquals(DataManipulator.integerSumOf(list).toLong(), MathUtil.sumOfSeq(0, testSize-1))
    }

    @Test
    fun testGet() {
        for (i in 0 .. list.size - 1) {
            assertEquals(list[i], i+1)
        }
    }

    @Test
    fun testRemove() {
        var count = 0
        var index:Int
        
        while (!list.isEmpty()) {
            try {
                index = MathUtil.randomInteger(0, list.size-1)
                val integer = list.removeAt(index)
                count++
                assertEquals(list.size, testSize-count)
                assertFalse(list.contains(integer), "Integer must not exist in list($count): $integer")
            } catch (e: Exception) {
                e.printStackTrace()
                fail("Exception occurred")
            }
        }

        assertEquals(list.size, 0)
        assertTrue(list.isEmpty())
    }

    @Test
    fun testIndexOf() {
        for (i in 0 .. testSize-1) {
            val n = MathUtil.randomInteger(1, testSize)
            val index = list.indexOf(n)
            assertEquals(index, n-1)
        }
    }
    
    @Test
    fun testSmallerIndexOf() {
        val list = SortedListAvl<Int>()

        assertEquals(list.smallerIndexOf(2), -1)
        
        list.add(1,2,3,3,5,7,9)
        
        assertEquals(list.smallerIndexOf(0), -1)
        assertEquals(list.smallerIndexOf(1), -1)
        assertEquals(list.smallerIndexOf(2), 0)
        assertEquals(list.smallerIndexOf(3), 1)
        assertEquals(list.smallerIndexOf(4), 3)
        assertEquals(list.smallerIndexOf(5), 3)
        assertEquals(list.smallerIndexOf(6), 4)
        assertEquals(list.smallerIndexOf(7), 4)
        assertEquals(list.smallerIndexOf(8), 5)
        assertEquals(list.smallerIndexOf(9), 5)
        assertEquals(list.smallerIndexOf(10), 6)
        assertEquals(list.smallerIndexOf(11), 6)
        assertEquals(list.smallerIndexOf(9999), 6)
        
        // Random value test
        val randomList = SortedListAvl<Int>()
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)
            
            randomList.add(v)
            
            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (j in 1 .. MathUtil.randomInteger(1,3)) {
                    randomList.add(v)
                }
            }
        }
        
        // Test non exist elements
        assertEquals(randomList.smallerIndexOf(0), -1)
        assertEquals(randomList.smallerIndexOf(299), -1)
        
        // Test last element
        assertEquals(randomList.smallerIndexOf(10000), randomList.size-1)
        
        // Test random values (value withing list values)
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)
            
            val idx = randomList.smallerIndexOf(v)
            
            if (idx != -1) {
                val listVal = randomList[idx]
                
                if (idx < randomList.size - 1) {
                    // Next element and searching value should be greater than the result value 
                    assertTrue(randomList[idx + 1] > listVal && v > listVal)
                } else {
                    // Result value is the last element, only check the searching value
                    assertTrue(v > listVal)
                }
            }
        }
    }
    
    @Test
    fun testSmallerOrEqualsIndexOf() {
        val list = SortedListAvl<Int>()

        assertEquals(list.smallerOrEqualsIndexOf(2), -1)

        list.add(1,2,3,3,5,7,9)

        assertEquals(list.smallerOrEqualsIndexOf(0), -1)
        assertEquals(list.smallerOrEqualsIndexOf(1), 0)
        assertEquals(list.smallerOrEqualsIndexOf(2), 1)
        assertEquals(list.smallerOrEqualsIndexOf(3), 3)
        assertEquals(list.smallerOrEqualsIndexOf(4), 3)
        assertEquals(list.smallerOrEqualsIndexOf(5), 4)
        assertEquals(list.smallerOrEqualsIndexOf(6), 4)
        assertEquals(list.smallerOrEqualsIndexOf(7), 5)
        assertEquals(list.smallerOrEqualsIndexOf(8), 5)
        assertEquals(list.smallerOrEqualsIndexOf(9), 6)
        assertEquals(list.smallerOrEqualsIndexOf(10), 6)
        assertEquals(list.smallerOrEqualsIndexOf(11), 6)
        assertEquals(list.smallerOrEqualsIndexOf(9999), 6)

        // Random value test
        val randomList = SortedListAvl<Int>()
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)

            randomList.add(v)

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (j in 1 .. MathUtil.randomInteger(1,3)) {
                    randomList.add(v)
                }
            }
        }

        // Test non exist elements
        assertEquals(randomList.smallerOrEqualsIndexOf(0), -1)
        assertEquals(randomList.smallerOrEqualsIndexOf(299), -1)
        
        // Test last element
        assertEquals(randomList.smallerOrEqualsIndexOf(randomList.last()), randomList.lastIndex)

        // Test random values (value withing list values)
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)

            val idx = randomList.smallerOrEqualsIndexOf(v)

            if (idx != -1) {
                val listVal = randomList[idx]

                if (idx < randomList.size - 1) {
                    // Next element is greater than and searching value should be greater or equals to the result value
                    // Since last index is guaranteed, next element should always be greater than the searching value
                    assertTrue(randomList[idx + 1] > listVal && v >= listVal)
                } else {
                    // Result value is the last element, only check the searching value
                    assertTrue(v >= listVal)
                }
            }
        }
    }
    
    @Test
    fun testGreaterIndexOf() {
        val list = SortedListAvl<Int>()

        assertEquals(list.greaterIndexOf(2), -1)

        list.add(1,2,3,3,5,7,9)

        assertEquals(list.greaterIndexOf(0), 0)
        assertEquals(list.greaterIndexOf(1), 1)
        assertEquals(list.greaterIndexOf(2), 2)
        assertEquals(list.greaterIndexOf(3), 4)
        assertEquals(list.greaterIndexOf(4), 4)
        assertEquals(list.greaterIndexOf(5), 5)
        assertEquals(list.greaterIndexOf(6), 5)
        assertEquals(list.greaterIndexOf(7), 6)
        assertEquals(list.greaterIndexOf(8), 6)
        assertEquals(list.greaterIndexOf(9), -1)
        assertEquals(list.greaterIndexOf(10), -1)
        assertEquals(list.greaterIndexOf(11), -1)
        assertEquals(list.greaterIndexOf(9999), -1)

        // Random value test
        val randomList = SortedListAvl<Int>()
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)

            randomList.add(v)

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (j in 1 .. MathUtil.randomInteger(1,3)) {
                    randomList.add(v)
                }
            }
        }

        // Test non exist elements
        assertEquals(randomList.greaterIndexOf(10000), -1)
        assertEquals(randomList.greaterIndexOf(99999), -1)

        // Test first element
        assertEquals(randomList.greaterIndexOf(randomList.first()-1), 0)

        // Test random values (value withing list values)
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)

            val idx = randomList.greaterIndexOf(v)

            if (idx != -1) {
                val listVal = randomList[idx]

                if (idx > 0) {
                    // Previous element and searching value should be smaller than the result value 
                    assertTrue(randomList[idx - 1] < listVal && v < listVal)
                } else {
                    // Result is the first element, only check the searching value
                    assertTrue(v < listVal)
                }
            }
        }
    }
    
    @Test
    fun testGreaterOrEqualsIndexOf() {
        val list = SortedListAvl<Int>()

        assertEquals(list.greaterOrEqualsIndexOf(2), -1)

        list.add(1,2,3,3,5,7,9)

        assertEquals(list.greaterOrEqualsIndexOf(0), 0)
        assertEquals(list.greaterOrEqualsIndexOf(1), 0)
        assertEquals(list.greaterOrEqualsIndexOf(2), 1)
        assertEquals(list.greaterOrEqualsIndexOf(3), 2)
        assertEquals(list.greaterOrEqualsIndexOf(4), 4)
        assertEquals(list.greaterOrEqualsIndexOf(5), 4)
        assertEquals(list.greaterOrEqualsIndexOf(6), 5)
        assertEquals(list.greaterOrEqualsIndexOf(7), 5)
        assertEquals(list.greaterOrEqualsIndexOf(8), 6)
        assertEquals(list.greaterOrEqualsIndexOf(9), 6)
        assertEquals(list.greaterOrEqualsIndexOf(10), -1)
        assertEquals(list.greaterOrEqualsIndexOf(11), -1)
        assertEquals(list.greaterOrEqualsIndexOf(9999), -1)

        // Random value test
        val randomList = SortedListAvl<Int>()
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)

            randomList.add(v)

            // Ensure duplicate elements
            if (i % 333 == 0) {
                for (j in 1 .. MathUtil.randomInteger(1,3)) {
                    randomList.add(v)
                }
            }
        }

        // Test non exist elements
        assertEquals(randomList.greaterOrEqualsIndexOf(10000), -1)
        assertEquals(randomList.greaterOrEqualsIndexOf(10299), -1)

        // Test first element
        assertEquals(randomList.greaterOrEqualsIndexOf(randomList.first()), 0)

        // Test random values (value withing list values)
        for (i in 1 .. 1000) {
            val v = MathUtil.randomInteger(300, 9999)

            val idx = randomList.greaterOrEqualsIndexOf(v)

            if (idx != -1) {
                val listVal = randomList[idx]

                if (idx > 0) {
                    // Previous element should be smaller than and searching value should be smaller or equals to the result value
                    // Since first index is guaranteed, previous element should always be smaller than the result value
                    assertTrue(randomList[idx - 1] < listVal && v <= listVal)
                } else {
                    // Result value is the first element, only check the searching value
                    assertTrue(v <= listVal)
                }
            }
        }
    }

    @Test
    fun testRemoveWithObject() {
        val list = SortedListAvl<Int>()

        for (i in 0 .. 9) {
            list.add(i + 1)
        }

        list.remove(4)

        var integerArray = Array<Int?>(list.size, { idx -> list[idx] })
        
        verifyFullArrayValues(integerArray, 1, 2, 3, 5, 6, 7, 8, 9, 10)

        list.remove(8)
        list.remove(2)
        list.remove(10)
        list.remove(1)

        integerArray = Array<Int?>(list.size, { idx -> list[idx] })

        verifyFullArrayValues(integerArray, 3, 5, 6, 7, 9)

        assertEquals(list.size, 5)
    }

    @Test
    fun testSameElementValue() {
        val list = SortedListAvl<Int>()

        list.add(1, 1, 2, 2, 3, 3, 4, 4, 5, 5)

        verifyFullListValues(list, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5)

        list.remove(1)
        list.remove(5)
        list.remove(4)
        list.remove(3)
        list.remove(2)
        list.remove(3)

        assertEquals(list.size, 4)

        verifyFullListValues(list, 1, 2, 4, 5)

        list.clear()

        for (i in 0 .. 9) {
            list.add(55)
        }

        for (i in 1 .. 55) {
            list.add(55 + i)
            list.add(55 - i)
        }

        assertEquals(list.size, 120)

        for (i in 0 .. list.size- 1) {
            if (i < 55) {
                assertEquals(list[i], i)
            } else if (i > 54 && i < 65) {
                assertEquals(list[i], 55)
            } else {
                assertEquals(list[i], i-9, "i: $i")
            }
        }

        assertEquals(list.indexOf(55), 55)
        assertEquals(list.lastIndexOf(55), 64)
    }

    @Test
    fun testTreeNodeNavigation() {
        val treeNodes = ArrayList<BinaryTreeNode<Int>>()

        for (i in 1 .. 13) {
            treeNodes.add(BinaryTreeNode<Int>(i, null, null, null))
        }

        val builder = BinaryTreeNodeBuilder<Int>()

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
            .right(13)

        val root = builder.rootNode

        var prev = root
        var next = root

        for (i in 6 downTo 0) {
            prev = prev.getPreviousNode()

            assertEquals(prev.element, i)
        }

        for (i in 8 .. 13) {
            next = next.getNextNode()

            assertEquals(next.element, i)
        }
        assertEquals(root.element, 7)

        for (i in 0 .. 13) {
            assertEquals(prev.element, i)

            prev = prev.getNextNode()
        }

        assertNull(prev)

        for (i in 13 downTo 0) {
            assertEquals(next.element, i)

            next = next.getPreviousNode()
        }

        assertNull(next)

        root.refreshMetaData()
        assertEquals(root.leftDepth, 4)
        assertEquals(root.rightDepth, 3)
        assertEquals(root.getDepth(), 4)

        assertEquals(root.leftNodeCount, 7)
        assertEquals(root.rightNodeCount, 6)
        assertEquals(root.getNodeCount(), 13)
    }

    @Test
    fun testIterator() {
        val iter = list.iterator()

        var count = 1
        while (iter.hasNext()) {
            val integer = iter.next()
            assertEquals(integer, count++)
        }

        var listIter = list.listIterator()

        count = 1
        while (listIter.hasNext()) {
            val integer = listIter.next()
            assertEquals(integer, count++)
        }

        count = testSize
        while (listIter.hasPrevious()) {
            val integer = listIter.previous()
            assertEquals(integer, count--)
        }

        listIter = list.listIterator(10)

        count = 11
        while (listIter.hasNext()) {
            val integer = listIter.next()
            assertEquals(integer, count++)
        }

        count = testSize
        while (listIter.hasPrevious()) {
            val integer = listIter.previous()
            assertEquals(integer, count--)
        }
    }

    @Test
    fun testSet() {
        val list = SortedListAvl<Int>()

        list.add(2, 5, 3, 4, 10, 1, 7, 8, 6, 9)

        list.set(5, 19)
        assertEquals(list.size, 10)

        verifyFullListValues(list, 1, 2, 3, 4, 5, 7, 8, 9, 10, 19)

        list.set(0, -8)
        assertEquals(list.size, 10)

        verifyFullListValues(list, -8, 2, 3, 4, 5, 7, 8, 9, 10, 19)
    }

    @Test
    fun testContains() {
        for (i in -testSize .. testSize) {
            if (i > 0) {
                assertTrue(list.contains(i), "Value must exist: $i")
            } else {
                assertFalse(list.contains(i), "Value must not exist: $i")
            }
        }
    }

    @Test
    fun testRemoveAll() {
        val size = MathUtil.randomInteger(testSize / 2, testSize)
        val arrayList = ArrayList<Int>(size)

        for (i in 0 .. size-1) {
            var v = MathUtil.randomInteger(1, testSize)
            while (arrayList.contains(v)) {
                v = MathUtil.randomInteger(1, testSize)
            }
            arrayList.add(v)
        }

        list.removeAll(arrayList)
        assertEquals(list.size, testSize-arrayList.size)

        for (i in 0 .. testSize-1) {
            val integer = i + 1
            if (arrayList.contains(integer)) {
                assertFalse(list.contains(integer), "Value must not exist: $integer")
            } else {
                assertTrue(list.contains(integer), "Value must exist: $integer")
            }
        }
    }

    @Test
    fun testRetainAll() {
        val size = MathUtil.randomInteger(testSize / 2, testSize)
        val arrayList = ArrayList<Int>(size)

        for (i in 0 .. size-1) {
            var v = MathUtil.randomInteger(1, testSize)
            while (arrayList.contains(v)) {
                v = MathUtil.randomInteger(1, testSize)
            }
            arrayList.add(v)
        }

        list.retainAll(arrayList)
        assertEquals(list.size, arrayList.size)

        for (i in 0 .. testSize-1) {
            val integer = i + 1
            if (arrayList.contains(integer)) {
                assertTrue(list.contains(integer), "Value must exist: $integer")
            } else {
                assertFalse(list.contains(integer), "Value must not exist: $integer")
            }
        }

        val avlSortedList = SortedListAvl<Int>()

        avlSortedList.add(10)
        avlSortedList.add(10)
        avlSortedList.add(10)

        val retainList = ArrayList<Int>()
        retainList.add(10)

        val avlListModified = avlSortedList.retainAll(retainList)

        assertTrue(avlListModified, "All values should be retained in SortedListAvl!")
    }

    @Test
    fun testClone() {
        val clonedList = list.clone()

        assertEquals(clonedList.size, list.size)

        for (i in 0 .. clonedList.size - 1) {
            assertEquals(list[i], clonedList[i])
        }

        val index = MathUtil.randomInteger(0, clonedList.size-1)
        val integer = clonedList.removeAt(index)

        assertEquals(clonedList.size, list.size - 1)
        assertEquals(integer, list[index])

        for (i in 0 .. list.size - 1) {
            if (i < index) {
                assertEquals(clonedList[i], list[i])
            } else if (i > index) {
                assertEquals(clonedList[i-1], list[i])
            }
        }

        clonedList.add(integer)

        for (i in 0 .. clonedList.size - 1) {
            assertEquals(list[i], clonedList[i])
        }
    }

    @Test
    fun testIterAddSetRemove() {
        val listIterator = list.listIterator()

        try {
            listIterator.remove()
            fail("Exception must occur")
        } catch (ise: IllegalStateException) {
            // dummy block
        }

        assertEquals(listIterator.next(), 1)
        listIterator.add(-990)
        assertEquals(list.indexOf(-990), 0)

        assertEquals(listIterator.previous(), -990)
        listIterator.remove()
        assertEquals(listIterator.next(), 1)
        listIterator.remove()
        assertEquals(listIterator.next(), 2)
        assertEquals(listIterator.next(), 3)
        assertEquals(listIterator.next(), 4)
        assertEquals(listIterator.next(), 5)
        assertEquals(listIterator.next(), 6)
        assertEquals(listIterator.next(), 7)
        assertEquals(listIterator.next(), 8)
        assertEquals(listIterator.next(), 9)
        assertEquals(listIterator.next(), 10)

        assertEquals(listIterator.previous(), 10)
        assertEquals(listIterator.previous(), 9)
        assertEquals(listIterator.previous(), 8)
        listIterator.remove()
        assertEquals(listIterator.next(), 9)
        assertEquals(listIterator.previous(), 9)
        assertEquals(listIterator.previous(), 7)
        assertEquals(listIterator.previous(), 6)
        assertEquals(listIterator.previous(), 5)
        assertEquals(listIterator.previous(), 4)
        assertEquals(listIterator.previous(), 3)
        assertEquals(listIterator.previous(), 2)
        assertFalse(listIterator.hasPrevious(), "Must not has previous")
        assertEquals(listIterator.next(), 2)
        assertEquals(listIterator.next(), 3)
        assertEquals(listIterator.next(), 4)
        assertEquals(listIterator.next(), 5)
        assertEquals(listIterator.next(), 6)
        assertEquals(listIterator.next(), 7)
        assertEquals(listIterator.next(), 9)
        assertEquals(listIterator.next(), 10)
    }

    @Test
    fun testIndexOfWithSameValues() {
        val sList = SortedListAvl<Int>()

        sList.add(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 3, 4, 6, 7, 10, 6)

        assertEquals(sList.size, 16)

        assertEquals(sList.indexOf(1), 0)
        assertEquals(sList.indexOf(2), 1)
        assertEquals(sList.indexOf(3), 2)
        assertEquals(sList.lastIndexOf(3), 3)
        assertEquals(sList.indexOf(4), 4)
        assertEquals(sList.lastIndexOf(4), 5)
        assertEquals(sList.indexOf(5), 6)
        assertEquals(sList.indexOf(6), 7)
        assertEquals(sList.lastIndexOf(6), 9)
        assertEquals(sList.indexOf(7), 10)
        assertEquals(sList.lastIndexOf(7), 11)
        assertEquals(sList.indexOf(8), 12)
        assertEquals(sList.indexOf(9), 13)
        assertEquals(sList.indexOf(10), 14)
        assertEquals(sList.lastIndexOf(10), 15)
    }

    @Test
    fun testGetSubList() {
        val fromIndex = MathUtil.randomInteger(0, list.size - 5)
        val toIndex = MathUtil.randomInteger(fromIndex+1, list.size-1)
        val subList = list.getSubList(fromIndex, toIndex)

        assertEquals(toIndex - fromIndex, subList.size)

        for (i in fromIndex .. toIndex - 1) {
            assertEquals(list[i], subList[i-fromIndex])
        }
    }

    @Test
    fun testForEachLoop() {
        var count = 1

        list.forEach{
            it ->
                assertEquals(it, count++)
        }
    }

    @Test
    fun testResort() {
        val arrayList = ArrayList<IntegerValue>()
        val sortedList = SortedListAvl<IntegerValue>()

        for (i in 0 .. testSize-1) {
            val v = IntegerValue(i)
            arrayList.add(v)
            sortedList.add(v)
        }

        arrayList.forEach {
            it ->
                it.value = MathUtil.randomInteger(0, testSize)
        }

        var allInOrder = true
        val prev = sortedList[0]
        for (i in 1 .. sortedList.size - 1) {
            val curr = sortedList[i]

            if (prev!! > curr!!) {
                allInOrder = false
                break
            }
        }
        assertFalse(allInOrder, "A random order order sortedList is expected!")

        Collections.sort(arrayList)

        sortedList.resort()
        val arr = Array<IntegerValue?>(sortedList.size, { idx -> sortedList[idx] })

        var i = 0

        while (i < arr.size || i < arrayList.size) {
            assertEquals(arr[i], arrayList[i])
            i++
        }
    }

    @Test
    fun testNullValues() {
        assertTrue(list.add(null))
        assertTrue(list.add(null))

        assertNull(list.getMin())
        assertNull(list[0])
        assertNull(list[1])

        for (i in 2 .. list.size-1) {
            assertEquals(list[i], i-1)
        }

        assertEquals(list.indexOf(null), 0)
        assertEquals(list.lastIndexOf(null), 1)

        assertEquals(list.indexOf(1), 2)

        assertTrue(list.contains(null))

        assertTrue(list.remove(null))

        assertEquals(list.indexOf(null), 0)
        assertEquals(list.lastIndexOf(null), 0)

        assertEquals(list.indexOf(1), 1)

        assertTrue(list.contains(null))

        assertTrue(list.remove(null))

        assertEquals(list.indexOf(null), -1)
        assertEquals(list.lastIndexOf(null), -1)

        assertEquals(list.indexOf(1), 0)

        assertFalse(list.contains(null))

        for (i in 1 .. list.size) {
            assertEquals(list[i-1], i)
        }
    }

    @Test
    fun testSortOrder() {
        val ascList = SortedListAvl<Int?>()

        ascList.add(null, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)

        assertNull(ascList[0])

        verifyListValues(ascList, 1, ascList.size-1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)

        val descList = SortedListAvl<Int?>(false)

        descList.add(null, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)

        assertNull(descList[10])

        verifyListValues(descList, 0, 9, 100, 90, 80, 70, 60, 50, 40, 30, 20, 10)

        val ascNgList = SortedListAvl<Int?>(true, false)

        ascNgList.add(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        assertNull(ascNgList[10])

        verifyListValues(ascNgList, 0, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        val descNgList = SortedListAvl<Int?>(false, false)

        descNgList.add(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        assertNull(descNgList[0])

        verifyListValues(descNgList, 1, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1)
    }

    @Test
    // Test for random data and verify list order, this is to expose error that is not covered (by chance)
    fun testRandom() {
        val repeat = 10
        val listSize = 1000
        val minBound = 1
        val maxBound = 500
        val nullPercentage = 10

        val nullThreshold = (maxBound - minBound + 1) * nullPercentage / 100 + minBound // integer division is enough

        for (i in 1 .. repeat) {
            val list = SortedListAvl<Int?>()

            for (j in 0 .. listSize - 1) {
                val v = MathUtil.randomInteger(minBound, maxBound)

                // Since zero null percentage's threshold will be minBound, therefore only set null for value lower than threshold
                list.add(if (v < nullThreshold) null else v)
            }

            verifyListOrder(list)
        }
    }

    fun verifyListOrder(list: List<Int?>) {
        val comparator = DataComparator.buildComparator<Int>(true, true)

        for (i in 1 .. list.size - 1) {
            assertFalse(comparator.compare(list[i-1], list[i]) > 0, "Order mismatch, i: $i, value @ i-1: ${list[i-1]}, value @ i: ${list[i]}, full list: \n${PrinterUtil.getListString(list)}")
        }
    }

    fun verifyListValues(list: List<Int?>, startIndex: Int, endIndex: Int, vararg vals: Int) {
        if (endIndex - startIndex + 1 == vals.size) {
            for (i in startIndex .. endIndex) {
                assertFalse(list[i] != vals[i-startIndex], "Expected: ${vals[i-startIndex]} but found: ${list[i]}, i: $i")
            }
        } else {
            fail("Values number mismatch, startIndex: $startIndex, endIndex: $endIndex, vals.size: ${vals.size}")
        }
    }
    
    fun verifyFullListValues(list: List<Int?>, vararg vals: Int) {
        verifyListValues(list, 0, vals.size - 1, *vals)
    }

    fun verifyArrayValues(arr: Array<Int?>, startIndex: Int, endIndex: Int, vararg vals: Int) {
        if (endIndex - startIndex + 1 == vals.size) {
            for (i in startIndex .. endIndex) {
                assertFalse(arr[i] != vals[i-startIndex], "Expected: ${vals[i-startIndex]} but found: ${arr[i]}, i: $i")
            }
        } else {
            fail("Values number mismatch, startIndex: $startIndex, endIndex: $endIndex, vals.size: ${vals.size}")
        }
    }

    fun verifyFullArrayValues(arr: Array<Int?>, vararg vals: Int) {
        verifyArrayValues(arr, 0, vals.size - 1, *vals)
    }
}