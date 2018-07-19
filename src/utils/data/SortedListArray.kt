package utils.data;

import utils.data.sort.MergeSort
import utils.data.sort.MultiSort
import java.util.*

/**
 * Sorted list implemented as java list, (binary search tree in array form)
 * Extremely slow performance for insertion & removal when comparing to SortedListAvl for large data size, over 35,000 elements.
 * For small data size, less than 35,000 elements, it always performs better than or equals to SortedListAvl in all aspect. 
 * Best for random access & searching
 * 
 * General performance comparision between AVL & Array sorted list implementation (not formally tested):
 * Insertion:
 * AVL: 1, Array: ~30x (1,000,000 elements), ~4x (100,000 elements)
 * Removal:
 * AVL: 1, Array: ~50x (1,000,000 elements), ~2.5x (100,000 elements)
 * Random access:
 * AVL: ~3.5x, Array: 1
 * Search:
 * AVL: ~1.02x (1,000,000 elements), ~1.7x(100,000 elements), Array: 1
 * 
 * @author Tony Tsang
 *
 * @param <E> Data generic type
 */
class SortedListArray<E>: SortedList<E> {
    /**
     * Element array
     */
    private var elements: Array<Any?>
    
    /**
     * Modification count for concurrent modification check
     */
    private var modCount: Int = 0
    
    /**
     * Comparator used for sorting
     */
    private var comparator: Comparator<E>
    
    /**
     * List size
     */
    override var size: Int private set
    
    /**
     * Construct an Sorted Array list, default: ascending order, null as smaller value
     */
    constructor(): this(true, true)
    
    /**
     * Construct an Sorted Array List, providing sort order, default null as smaller value
     * 
     * @param asc in ascending order (true) or descending order (false)
     */
    constructor(asc: Boolean): this(asc, true)
    
    /**
     * Construct an Sorted Array List, provided sort order and null order
     * 
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(asc: Boolean, nullAsSmaller: Boolean) {
        comparator = DataComparator.buildComparator(asc, nullAsSmaller);
        elements = Array<Any?>(10, { null })
        this.size = 0
    }
    
    /**
     * Construct an Sorted Array List, providing comparator used for sorting, default: null as smaller value
     * 
     * @param comparator comparator used for sorting
     */
    constructor(comparator: Comparator<E>): this(comparator, true)
    
    /**
     * Construct an Sorted Array List, providing comparator used for sorting and null order
     * 
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(comparator: Comparator<E>, nullAsSmaller: Boolean) {
        this.comparator = DataComparator.buildComparator(comparator, nullAsSmaller)
        elements = Array<Any?>(10, { null })
        this.size = 0
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, default ascending order, null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     */
    constructor(c: Collection<E>): this() {
        addAll(c);
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving sort order, default: null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     */
    constructor(c: Collection<E>, asc: Boolean): this(asc) {
        addAll(c)
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving sort order & null order<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(c: Collection<E>, asc: Boolean, nullAsSmaller: Boolean): this(asc, nullAsSmaller) {
        addAll(c)
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving comparator used for sorted<br>
     * Default: null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param comparator comparator used for sorting
     */
    constructor(c: Collection<E>, comparator: Comparator<E>): this(c, comparator, true)
    
    /**
     * Construct an Sorted Array List with existing data collection, proving comparator used for sorted & null order<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(c: Collection<E>, comparator: Comparator<E>, nullAsSmaller: Boolean): this(comparator, nullAsSmaller) {
        addAll(c)
    }
    
    /**
     * Get the iterator of this list
     * 
     * @return iterator of this list
     */
    override fun iterator(): MutableIterator<E> {
        return SortedListIterator()
    }
    
    /**
     * Find the insertion index for the target element
     * 
     * @param e target element
     * 
     * @return insertion index
     */
    private fun findInsertionIndex(e: E): Int {
        var min = 0
        var max = size - 1
        
        while (min <= max) {
            if (min == max) {
                val comp = comparator.compare(e, elements[min] as E)
                if (comp > 0) {
                    return min + 1;
                } else {
                    return min;
                }
            }
            val mid = (max + min) ushr 1
            val comp = comparator.compare(e, elements[mid] as E)
            if (comp < 0) {
                max = mid - 1
            } else if (comp > 0) {
                min = mid + 1
            } else {
                return mid
            }
        }
        
        return min
    }
    
    /**
     * Ensure the underlying array's capacity is enough to the target size 
     * 
     * @param size target size/capacity
     */
    private fun ensureCapacity(size: Int) {
        var capacity = elements.size
        
        while (size > capacity) {
            capacity *= 2
        }
        
        if (capacity > elements.size) {
            val newElements = Array<Any?>(capacity, { if (it < elements.size) elements[it] else null })
            
            elements = newElements
            
            modCount++
        }
    }
    
    /**
     * Get capacity of the underlying array 
     * 
     * @return capacity of the underlying array
     */
    fun getCapacity(): Int {
        return elements.size
    }
    
    private fun insert(insertIndex: Int, e: E) {
        System.arraycopy(elements, insertIndex, elements, insertIndex+1, size-insertIndex)
        elements[insertIndex] = e
        size++
    }
    
    override fun add(element: E): Boolean {
        if (size == 0) {
            ensureCapacity(size+1)
            elements[0] = element
            size++
            return true
        } else {
            ensureCapacity(size + 1)
            modCount++
            insert(findInsertionIndex(element), element)
            return true
        }
    }
    
    override fun add(index: Int, element: E) {
        add(element)
    }
    
    override fun addAll(elements: Collection<E>): Boolean {
        if (elements.isNotEmpty()) {
            ensureCapacity(size + elements.size)
            
            val cList = ArrayList<E>(elements.size)
            
            for (e in elements) {
                cList += e
            }
            
            MergeSort.sort(cList, comparator);
            
            val minIndex = findInsertionIndex(cList[0])
            val maxIndex = findInsertionIndex(cList[cList.size-1])

            System.arraycopy(this.elements, maxIndex, this.elements, maxIndex + cList.size, size - maxIndex)
            
            var c1 = cList.size-1
            var c2 = maxIndex-1
            var currentIndex = maxIndex + cList.size - 1
            
            while (currentIndex >= minIndex) {
                if (c1 < 0) {
                    this.elements[currentIndex--] = this.elements[c2--]
                } else if (c2 < minIndex) {
                    this.elements[currentIndex--] = cList[c1--]
                } else {
                    val e1 = cList[c1]
                    val e2 = this.elements[c2] as E
                    
                    val cr = comparator.compare(e1, e2)
                    
                    if (cr >= 0) {
                        this.elements[currentIndex--] = cList[c1--]
                    } else {
                        this.elements[currentIndex--] = this.elements[c2--]
                    }
                }
            }
            
            size += cList.size
            
            modCount++
            
            return true
        } else {
            return false
        }
    }

    override fun addAll(elements: Collection<E>, allowDuplicate: Boolean): Boolean {
        var modified = false
        for (e in elements) {
            if (allowDuplicate || !contains(e)) {
                if (add(e)) {
                    modified = true
                }
            }
        }
        return modified
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        return addAll(elements)
    }

    override fun clear() {
        size = 0
        modCount++
    }
    
    override fun contains(element: E): Boolean {
        try {
            return findIndex(element) != -1;
        } catch (cce: ClassCastException) {
            return false
        }
    }
    
    private fun findIndex(e: E): Int {
        var min = 0
        var max = size - 1
        
        while (min <= max) {
            val mid = (max + min) ushr 1
            val comp = comparator.compare(e, elements[mid] as E)
            if (comp < 0) {
                max = mid - 1
            } else if (comp > 0) {
                min = mid + 1
            } else {
                return mid
            }
        }
        
        return -1
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        for (o in elements) {
            if (!contains(o)) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): E {
        return elements[index] as E
    }

    override fun indexOf(element: E): Int {
        try {
            var foundIndex = findIndex(element)
            
            if (foundIndex != -1) {
                for (i in foundIndex-1 downTo 0) {
                    if (comparator.compare(element, elements[i] as E) == 0) {
                        foundIndex = i
                    } else {
                        return foundIndex
                    }
                }
                return foundIndex
            }
            
            return foundIndex
        } catch (cce: ClassCastException) {
            return -1
        }
    }

    // Same as SortedList's default implementation, seems java does not accept kotlin interface's default implementation
    override fun add(element: E, allowDuplicate: Boolean): Boolean {
        if (allowDuplicate || !contains(element)) {
            return add(element)
        } else {
            return false
        }
    }

    // Same as SortedList's default implementation, seems java does not accept kotlin interface's default implementation
    override fun add(index: Int, element: E, allowDuplicate: Boolean) {
        // Index is ignored as this is a sortedList
        add(element, allowDuplicate)
    }

    override fun greaterIndexOf(value: E): Int {
        if (size == 0) {
            return -1
        }
        
        var min = 0
        var max = size - 1

        while (min <= max) {
            val mid = (max + min) ushr 1
            val comp = comparator.compare(value, elements[mid] as E)

            if (comp >= 0) {
                min = mid + 1
            } else {
                var foundIndex = mid

                max = mid - 1

                while (min <= max) {
                    val newMid = (max + min) ushr 1;
                    val newComp = comparator.compare(value, elements[newMid] as E)

                    if (newComp < 0) {
                        foundIndex = newMid
                        max = newMid - 1
                    } else {
                        min = newMid + 1
                    }
                }
                
                return foundIndex
            }
        }
        
        return -1
    }

    override fun greaterOrEqualsIndexOf(value: E): Int {
        if (size == 0) {
            return -1
        }

        var min = 0
        var max = size - 1

        while (min <= max) {
            val mid = (max + min) ushr 1
            val comp = comparator.compare(value, elements[mid] as E)

            if (comp > 0) {
                min = mid + 1;
            } else {
                var foundIndex = mid

                max = mid - 1

                while (min <= max) {
                    val newMid = (max + min) ushr 1
                    val newComp = comparator.compare(value, elements[newMid] as E)

                    if (newComp <= 0) {
                        foundIndex = newMid
                        max = newMid - 1
                    } else {
                        min = newMid + 1
                    }
                }

                return foundIndex
            }
        }

        return -1
    }

    override fun smallerIndexOf(value: E): Int {
        if (size == 0) {
            return -1
        }

        var min = 0
        var max = size - 1

        while (min <= max) {
            val mid = (max + min) ushr 1
            val comp = comparator.compare(value, elements[mid] as E)

            if (comp <= 0) {
                max = mid - 1
            } else {
                var foundIndex = mid

                min = mid + 1

                while (min <= max) {
                    val newMid = (max + min) ushr 1
                    val newComp = comparator.compare(value, elements[newMid] as E)

                    if (newComp > 0) {
                        foundIndex = newMid;
                        min = newMid + 1
                    } else {
                        max = newMid - 1
                    }
                }

                return foundIndex
            }
        }

        return -1
    }

    override fun smallerOrEqualsIndexOf(value: E): Int {
        if (size == 0) {
            return -1
        }

        var min = 0
        var max = size - 1

        while (min <= max) {
            val mid = (max + min) ushr 1
            val comp = comparator.compare(value, elements[mid] as E)

            if (comp < 0) {
                max = mid - 1;
            } else {
                var foundIndex = mid

                min = mid + 1

                while (min <= max) {
                    val newMid = (max + min) ushr 1;
                    val newComp = comparator.compare(value, elements[newMid] as E)

                    if (newComp >= 0) {
                        foundIndex = newMid
                        min = newMid + 1
                    } else {
                        max = newMid - 1
                    }
                }

                return foundIndex
            }
        }

        return -1
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun lastIndexOf(element: E): Int {
        try {
            var foundIndex = findIndex(element)
            
            if (foundIndex != -1) {
                for (i in foundIndex+1 .. size-1) {
                    if (comparator.compare(element, elements[i] as E) == 0) {
                        foundIndex = i
                    } else {
                        return foundIndex
                    }
                }
                return foundIndex
            }
            
            return foundIndex
        } catch (cce: ClassCastException) {
            return -1
        }
    }

    override fun listIterator(): MutableListIterator<E> {
        return SortedListIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        return SortedListIterator(index)
    }
    
    private fun removeElement(index: Int): E {
        val removed = elements[index] as E
        
        var len = size - index - 1
        if (len > 0) {
            System.arraycopy(elements, index+1, elements, index, len)
        }
        
        size--
        return removed
    }
    
    override fun remove(element: E): Boolean {
        val index = indexOf(element)
        if (index == -1) {
            return false
        } else {
            modCount++
            removeElement(index)
            return true
        }
    }

    override fun removeAt(index: Int): E {
        checkIndex(index)
        modCount++
        return removeElement(index)
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        val containedIndexes = ArrayList<Int>()
        
        for (o in elements) {
            val index = indexOf(o)
            if (index != -1) {
                containedIndexes.add(index)
            }
        }
        
        if (containedIndexes.size == 0) {
            return false
        } else {
            Collections.sort(containedIndexes)
            
            var index = containedIndexes[0]
            var currentIndex = index
            for (i in 1 .. containedIndexes.size-1) {
                val nIndex = containedIndexes[i]
                
                for (j in index+1 .. nIndex-1) {
                    this.elements[currentIndex++] = this.elements[j]
                }
                
                index = nIndex
            }
            
            for (j in index+1 .. this.elements.size-1) {
                this.elements[currentIndex++] = this.elements[j];
            }
            
            size -= containedIndexes.size
            
            modCount++
            return true
        }
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val containedIndexes = SortedListArray<Int>()
        
        for (o in elements) {
            val index = indexOf(o)
            val lastIndex = lastIndexOf(o)
            if (index != -1) {
                for (i in index .. lastIndex) {
                    if (!containedIndexes.contains(i)) {
                        containedIndexes.add(i)
                    }
                }
            }
        }
        
        if (containedIndexes.size == 0) {
            size = 0
            return true
        } else if (containedIndexes.size == size) {
            return false
        } else {
            for (i in 0 .. containedIndexes.size-1) {
                this.elements[i] = this.elements[containedIndexes[i]]
            }
            
            size = containedIndexes.size
            
            modCount ++
            return true
        }
    }

    override fun set(index: Int, element: E): E {
        checkIndex(index)
        
        val e = removeElement(index)
        insert(findInsertionIndex(element), element)
        return e
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        throw UnsupportedOperationException("SubList is not supported!")
    }
    
    override fun getSubList(fromIndex: Int, toIndex: Int): SortedListArray<E> {
        val sub = SortedListArray<E>(comparator);
        
        val len = toIndex - fromIndex
        sub.ensureCapacity(len)
        
        System.arraycopy(elements, fromIndex, sub.elements, 0, len)
        
        sub.size = len
        
        return sub
    }
    
    override fun getMin(): E? {
        return if (size == 0) {
            null
        } else {
            elements[0] as E
        }
    }
    
    override fun getMax(): E? {
        return if (size > 0) {
            elements[size-1] as E
        } else {
            null
        }
    }
    
    override fun resort() {
        MultiSort.sort(
            elements, {
                o1, o2 ->
                comparator.compare(o1 as E, o2 as E)
            }, 0, size-1
        )
    }
    
    override fun clone(): SortedListArray<E> {
        val clone = SortedListArray<E>(comparator)
        clone.ensureCapacity(size)
        System.arraycopy(elements, 0, clone.elements, 0, size)
        clone.size = size
        return clone
    }
    
    private fun checkIndex(index: Int) {
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
    }

    private inner class SortedListIterator: MutableListIterator<E> {
        private var previousIndex: Int
        private var nextIndex: Int
        private var currentIndex: Int
        private var canRemove: Boolean = false;
        private var expectedModCount: Int = modCount
        
        constructor() {
            nextIndex = 0
            currentIndex = -1
            previousIndex = -1
        }
        
        constructor(i: Int) {
            checkIndex(i)
            nextIndex = i
            previousIndex = i - 1
            currentIndex = -1
        }
        
        private fun checkForComodification() {
            if (expectedModCount != modCount) {
                throw ConcurrentModificationException();
            }
        }
        
        override fun add(e: E) {
            this@SortedListArray.add(e)
            val index = indexOf(e)
            if (index <= currentIndex) {
                currentIndex += 1
                nextIndex = currentIndex + 1
                previousIndex = currentIndex - 1
            }
            canRemove = true;
            expectedModCount++
        }
        
        override fun hasNext(): Boolean {
            return nextIndex < size
        }

        override fun hasPrevious(): Boolean {
            return previousIndex >= 0
        }
        
        override fun next(): E {
            checkForComodification()
            if (nextIndex < size) {
                canRemove = true;
                currentIndex = nextIndex
                val element: E = get(nextIndex++)
                previousIndex = nextIndex - 1
                return element
            } else {
                throw NoSuchElementException("No next element")
            }
        }

        override fun nextIndex(): Int {
            if (nextIndex < size) {
                return nextIndex
            } else {
                return size
            }
        }
        
        override fun previous(): E {
            checkForComodification()
            if (previousIndex >= 0) {
                currentIndex = previousIndex
                val element = get(previousIndex--)
                nextIndex = previousIndex + 1
                canRemove = true
                return element
            } else {
                throw NoSuchElementException("No previous element (previousIndex: " + previousIndex + ")");
            }
        }

        override fun previousIndex(): Int {
            if (previousIndex >= 0) {
                return previousIndex
            } else {
                return -1
            }
        }
        
        override fun remove() {
            if (!canRemove || currentIndex < 0 || currentIndex >= size) {
                throw IllegalStateException("Nothing to remove (currentIndex: "+ currentIndex + ")")
            } else {
                checkForComodification();
                this@SortedListArray.removeAt(currentIndex)
                nextIndex = currentIndex
                previousIndex = currentIndex - 1
                currentIndex = -1
                canRemove = false
                expectedModCount++
            }
        }
        
        override fun set(element: E) {
            throw UnsupportedOperationException("Set operation is not supported (this is a sorted list)!")
        }
    }
}
