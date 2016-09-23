package utils.data

import java.io.Serializable
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-22
 * Time: 16:30
 */
class DoublyLinkedList<V>: AbstractList<V>, Iterable<V>, List<V>, Serializable {
    @Transient var head: DoublyNode<V>? = null
        
    @Transient var tail:DoublyNode<V>? = null
    @Transient override var size: Int = 0

    constructor() {
        head = null 
        tail = null
        size = 0
    }

    private fun createNode(data: V): DoublyNode<V> {
        return DoublyNode(data)
    }

    override fun clear() {
        head = null
        tail = null
        size = 0
    }

    fun replace(i: Int, data: V) {
        val node = getNode(i)
        if (node != null) {
            node.data = data
        }
    }

    private fun replace(node: DoublyNode<V>, size: Int, container: HeadTailContainer<V>) {
        if (head == tail && node == head) {
            head = container.head
            tail = container.tail
            this.size = size
        } else if (node == head) {
            container.tail?.next = head?.next
            head = container.head
            this.size += size - 1
        } else if (node == tail) {
            container.head?.previous = tail?.previous
            tail = container.tail
            this.size += size - 1
        } else {
            if (node.hasPrevious()) {
                node.previous!!.next = container.head
            }
            if (node.hasNext()) {
                node.next!!.previous = container.tail
            }
            this.size += size - 1
        }
    }

    fun replace(node: DoublyNode<V>?, list: List<V>?) {
        if (list == null || list.size == 0) {
            removeNode(node)
        } else {
            if (node != null && containsNode(node)) {
                val container = createNodeList(list)
                replace(node, list.size, container)
            }
        }
    }

    fun replace(node: DoublyNode<V>?, vararg datas: V) {
        if (datas.size == 0) {
            removeNode(node)
        } else {
            if (node != null && containsNode(node)) {
                val container = createNodeList(*datas)
                replace(node, datas.size, container)
            }
        }
    }

    fun replace(node: DoublyNode<V>?, datas: Collection<V>?) {
        if (datas == null || datas.size == 0) {
            removeNode(node)
        } else {
            if (node != null && containsNode(node)) {
                val container = createNodeList(datas)
                replace(node, datas.size, container)
            }
        }
    }

    fun replace(i: Int, vararg datas: V) {
        replace(getNode(i), *datas)
    }

    fun replace(i: Int, datas: Collection<V>) {
        replace(getNode(i), datas)
    }

    fun replace(i: Int, list: List<V>) {
        replace(getNode(i), list)
    }

    override fun add(index: Int, element: V) {
        insertData(index, element)
    }

    override public fun removeRange(fromIndex: Int, toIndex: Int) {
        checkIndex(fromIndex)
        checkIndex(toIndex)
        if (fromIndex <= toIndex) {
            if (fromIndex == toIndex) {
                removeAt(fromIndex)
            } else {
                val fromNode = getNode(fromIndex)!!
                val toNode = getNode(toIndex)!!
                
                if (fromNode === head && toNode === tail) {
                    clear()
                } else {
                    if (fromNode === head) {
                        head = toNode.next
                        head!!.previous = null
                    } else if (toNode === tail) {
                        tail = fromNode.previous
                        tail!!.next = null
                    } else {
                        fromNode.previous!!.next = toNode.next
                        toNode.next!!.previous = fromNode.previous
                    }
                    size -= toIndex - fromIndex + 1
                }
            }
        }
    }

    override fun toArray(): Array<out Any?> {
        return Array(size, {get(it) as Any?})
    }

    fun add(vararg datas: V) {
        for (data in datas) {
            add(data)
        }
    }

    fun add(n: DoublyNode<V>): DoublyNode<V>? {
        return addToTail(n)
    }

    fun addToHead(data: V): DoublyNode<V>? {
        return addToHead(createNode(data))
    }

    fun addToTail(data: V): DoublyNode<V>? {
        return addToTail(createNode(data))
    }

    fun addToTail(n: DoublyNode<V>): DoublyNode<V> {
        if (isEmpty()) {
            return resetToOneNode(n)
        } else {
            tail!!.next= n
            tail = n
            size++
            tail!!.next = null
            return tail!!
        }
    }

    fun addToHead(n: DoublyNode<V>): DoublyNode<V> {
        if (isEmpty()) {
            return resetToOneNode(n)
        } else {
            head!!.previous = n
            head = n
            size++
            head!!.previous = null
            return head!!
        }
    }

    fun insertData(i: Int, data: V): DoublyNode<V> {
        return insertNode(i, createNode(data))
    }

    fun insertNode(i: Int, n: DoublyNode<V>): DoublyNode<V> {
        if (i <= 0) {
            return addToHead(n)
        } else if (i >= size) {
            return addToTail(n)
        } else {
            val node = getNode(i)
            node!!.previous!!.next = n
            n.next = node
            size++
            return n
        }
    }

    fun getHeadData(): V? {
        if (isEmpty()) {
            return null
        } else {
            return head!!.data
        }
    }

    fun getTailData(): V? {
        if (isEmpty()) {
            return null
        } else {
            return tail!!.data
        }
    }

    fun indexOfData(data: V): Int {
        if (isEmpty()) {
            return -1
        } else {
            var node: DoublyNode<V>? = head
            var i = 0
            while (node != null) {
                if (node.data == data) {
                    return i
                }
                node = node.next
                i++
            }
            return -1
        }
    }

    fun indexOfNode(n: DoublyNode<V>?): Int {
        if (n == null) {
            return -1
        }
        
        if (isEmpty()) {
            return -1
        } else {
            var node: DoublyNode<V>? = head
            var i = 0
            while (node != null) {
                if (node == n) {
                    return i
                }
                node = node.next
                i++
            }
            return -1
        }
    }

    fun indexOfNode(n: DoublyNode<V>, i: Int): Int {
        if (isEmpty() || i < 0 || i >= size) {
            return -1
        } else {
            var node: DoublyNode<V>? = getNode(i)
            var c = i
            while (node != null) {
                if (node == n) {
                    return c
                }
                node = node.next
                c++
            }
            return -1
        }
    }

    fun setList(list: DoublyLinkedList<V>?) {
        if (list != null) {
            val listHead = list.head
            val listTail = list.tail
            this.head = listHead
            this.tail = listTail
            size = list.size
            var node = listHead
            while (node != null) {
                node = node.next
            }
        }
    }

    fun insertSubList(i: Int, list: DoublyLinkedList<V>?) {
        if (i >= 0 && i < size && list != null && list !== this) {
            size += list.size
            val h = list.head
            val t = list.tail
            val n = getNode(i)
            val p = n!!.previous
            if (p != null) {
                h!!.previous = p
            }
            t!!.next = n
            var node = h
            while (node != t) {
                node = node!!.next
            }
        } else if (size == 0) {
            setList(list)
        }
    }

    fun removeInterval(startNode: DoublyNode<V>, endNode: DoublyNode<V>) {
        if (startNode == endNode) {
            removeNode(startNode)
        } else {
            val startIndex = indexOfNode(startNode)
            val endIndex = indexOfNode(endNode, startIndex)
            if (startIndex > -1 && endIndex > -1) {
                val length = endIndex - startIndex + 1
                size -= length
                if (size == 0) {
                    head = null
                    tail = null
                } else if (head == startNode) {
                    head = endNode.next
                    head!!.previous = null
                    endNode.next = null
                } else if (tail == endNode) {
                    tail = startNode.previous
                    tail!!.next = null
                    startNode.previous = null
                } else {
                    startNode.previous!!.next = endNode.next
                    startNode.previous = null
                    endNode.next = null
                }
                var node: DoublyNode<V>? = startNode
                while (node != null) {
                    node = node.next
                }
            }
        }
    }

    fun removeInterval(startIndex: Int, endIndex: Int) {
        if (startIndex >= 0 && endIndex >= 0 && startIndex < size && endIndex < size) {
            val length = endIndex - startIndex + 1
            val startNode = getNode(startIndex)
            val endNode = getNode(endIndex)
            size -= length
            if (size == 0) {
                head = null
                tail = null
            } else if (startNode == head) {
                head = endNode!!.next
                head!!.previous = null
                endNode.next = null
            } else if (endNode == tail) {
                tail = startNode!!.previous
                tail!!.next = null
                startNode.previous = null
            } else {
                startNode!!.previous!!.next = endNode!!.next
                startNode.previous = null
                endNode.next = null
            }
            var node: DoublyNode<V>? = startNode
            while (node != null) {
                node = node.next
            }
        }
    }

    override fun get(index: Int): V {
        if (index >= 0 && index < size) {
            val data = getNodeData(index)
            
            // Fix for nullable generic type, null check should not be handled here
            // e.g. null should be a valid return type for String? type
            return if (data==null) null as V else data
        } else {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
    }
    
    private fun checkIndex(index: Int) {
        if (index < 0 && index >= size) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
    }

    override fun containsAll(datas: Collection<V>): Boolean {
        for (obj in datas) {
            if (!contains(obj)) {
                return false
            }
        }
        return true
    }

    override fun contains(element: V): Boolean {
        return containsData(element)
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun subList(fromIndex: Int, toIndex: Int): DoublyLinkedList<V> {
        return getSubList(fromIndex, toIndex-fromIndex+1)!!
    }

    override fun indexOf(element: V): Int {
        if (isEmpty()) {
            return -1
        } else {
            var node: DoublyNode<V>? = head
            var index = 0
            while (node != null) {
                if (element == node.data) {
                    return index
                }
                node = node.next
                index++
            }
            return -1
        }
    }

    override fun lastIndexOf(element: V): Int {
        if (isEmpty()) {
            return -1
        } else {
            var node: DoublyNode<V>? = tail
            var index = size - 1
            while (node != null) {
                if (element == node.data) {
                    return index
                }
                node = node.previous
                index--
            }
            return -1
        }
    }
    
    override fun iterator(): MutableIterator<V> {
        return DoublyIterator()
    }

    override fun listIterator(): MutableListIterator<V> {
        return DoublyIterator()
    }

    override fun listIterator(index: Int): MutableListIterator<V> {
        return DoublyIterator(index)
    }

    fun getNodeData(i: Int): V? {
        if (i < 0 || i >= size) {
            throw IndexOutOfBoundsException("Index: $i, Size: $size")
        } else {
            val n = getNode(i)
            return n?.data
        }
    }
    
    fun getNode(i: Int): DoublyNode<V>? {
        if (i < 0 || i >= size) {
            throw IndexOutOfBoundsException("Index: " + i +
                ", Size: " + size)
        } else if (i == 0) {
            return head
        } else if (i == size - 1) {
            return tail
        } else {
            if (i < size shr 2) {
                var c = 1
                var n: DoublyNode<V>? = head
                while (n != null && c <= i) {
                    n = n.next
                    c++
                }
                return n
            } else {
                var c = size - 2
                var n: DoublyNode<V>? = tail
                while (n != null && c >= i) {
                    n = n.previous
                    c--
                }
                return n
            }
        }
    }

    fun removeNode(n: DoublyNode<V>?): DoublyNode<V>? {
        if (n != null) {
            if (isEmpty()) {
                return null
            } else if (head == n) {
                return removeHead()
            } else if (tail == n) {
                return removeTail()
            } else {
                val pre = n.previous
                val nxt = n.next
                pre!!.next = nxt
                size--
                n.previous = null
                n.next = null
                return n
            }
        } else {
            return null
        }
    }

    fun removeNode(i: Int): DoublyNode<V>? {
        return removeNode(getNode(i))
    }

    private fun resetToOneNode(n: DoublyNode<V>): DoublyNode<V> {
        head = n
        tail = n
        head!!.previous = null
        head!!.next = null
        size = 1
        return head!!
    }

    fun removeHead(): DoublyNode<V>? {
        if (isEmpty()) {
            return null
        } else if (head == tail) {
            val n = head
            head = null
            tail = null
            size = 0
            return n
        } else {
            val n = head
            head = head!!.next
            head!!.previous = null
            n!!.next = null
            size--
            return n
        }
    }

    fun removeTail(): DoublyNode<V>? {
        if (isEmpty()) {
            return null
        } else if (head == tail) {
            val n = tail
            head = null
            tail = null
            size = 0
            return n
        } else {
            val n = tail
            tail = tail!!.previous
            tail!!.next = null
            n!!.previous = null
            size--
            return n
        }
    }

    fun containsData(data: V): Boolean {
        var node: DoublyNode<V>? = head
        while (node != null) {
            if (data == node.data) {
                return true
            }
            node = node.next
        }
        return false
    }

    fun containsNode(n: DoublyNode<V>): Boolean {
        var node: DoublyNode<V>? = head
        while (node != null) {
            if (node == n) {
                return true
            }
            node = node.next
        }
        return false
    }

    fun containsNodes(vararg nodes: DoublyNode<V>): Boolean {
        var node: DoublyNode<V>? = head
        val l = ArrayList<DoublyNode<V>>(nodes.size)
        for (n in nodes) {
            l.add(n)
        }
        while (node != null) {
            val iter = l.listIterator()
            while (iter.hasNext()) {
                val n = iter.next()
                if (n == node) {
                    iter.remove()
                    if (l.isEmpty()) {
                        return true
                    } else {
                        continue
                    }
                }
            }
            node = node.next
        }
        return false
    }

    fun concatSequence(seq: DoublyLinkedList<V>) {
        var node = seq.head
        while (node != null) {
            add(node.data)
            node = node.next
        }
    }

    fun getSubList(startIndex: Int, length: Int): DoublyLinkedList<V>? {
        if (startIndex >= 0 && startIndex + length <= size) {
            val seq = DoublyLinkedList<V>()
            var n = getNode(startIndex)
            seq.add(n!!.data)
            var c = 1
            while (c < length) {
                n = n!!.next
                seq.add(n!!.data)
                c++
            }
            return seq
        } else {
            return null
        }
    }
    
    fun subSequence(startNode: DoublyNode<V>, endNode: DoublyNode<V>): DoublyLinkedList<V>? {
        if (containsNodes(startNode, endNode)) {
            val seq = DoublyLinkedList<V>()
            if (startNode === endNode) {
                seq.add(startNode.data)
            } else {
                var n: DoublyNode<V>? = startNode
                while (n != endNode) {
                    if (n != null) {
                        seq.add(n.data)
                        n = n.next
                    } else {
                        return null
                    }
                }
                seq.add(endNode.data)
            }
            return seq
        } else {
            return null
        }
    }

    fun clone(): DoublyLinkedList<V> {
        val seq = DoublyLinkedList<V>()
        seq.addAll(this)
        return seq
    }
    
    override fun add(element: V): Boolean {
        val n = add(createNode(element))
        return n != null
    }

    fun addAllToHead(datas: Collection<V>): Boolean {
        if (datas.size == 0) {
            return true
        }
        val container = createNodeList(datas)
        if (isEmpty()) {
            head = container.head
            tail = container.tail
            size = datas.size
        } else {
            head!!.previous = container.tail
            head = container.head
            size += datas.size
        }
        return true
    }

    override fun addAll(elements: Collection<V>): Boolean {
        if (elements.size == 0) {
            return true
        }
        val container = createNodeList(elements);
        if (isEmpty()) {
            head = container.head
            tail = container.tail
            size = elements.size
        } else {
            tail!!.next = container.head
            tail = container.tail
            size += elements.size
        }
        return true
    }

    override fun addAll(index: Int, elements: Collection<V>): Boolean {
        if (index < 0 || index > size) {
            return false
        } else {
            if (elements.size == 0) {
                return true
            }
            if (index == size) {
                addAll(elements)
            } else if (index == 0) {
                addAllToHead(elements)
            } else {
                val container = createNodeList(elements)
                val node = getNode(index)
                node!!.previous!!.next = container.head
                node.previous = container.tail
                size += elements.size
            }
            return true
        }
    }

    private fun createNodeList(list: List<V>): HeadTailContainer<V> {
        var h: DoublyNode<V>? = null
        var n: DoublyNode<V>? = null
        for (data in list) {
            val c = createNode(data)
            if (h == null) {
                h = c
                n = c
            } else {
                c.previous = n
                n = c
            }
        }
        return HeadTailContainer(h, n)
    }

    private fun createNodeList(vararg datas: V): HeadTailContainer<V> {
        var h: DoublyNode<V>? = null
        var n: DoublyNode<V>? = null
        for (data in datas) {
            val c = createNode(data)
            if (h == null) {
                h = c
                n = c
            } else {
                c.previous = n
                n = c
            }
        }
        return HeadTailContainer(h, n)
    }

    private fun createNodeList(datas: Collection<V>): HeadTailContainer<V> {
        var h: DoublyNode<V>? = null
        var n: DoublyNode<V>? = null
        val iter = datas.iterator()
        while (iter.hasNext()) {
            val data = iter.next()
            val c = createNode(data)
            if (h == null) {
                h = c
                n = c
            } else {
                c.previous = n
                n = c
            }
        }
        return HeadTailContainer(h, n)
    }

    private inner class DoublyIterator: MutableListIterator<V> {
        private var currentNode: DoublyNode<V>? = null
        private var nextNode: DoublyNode<V>? = null
        private var nextIndex: Int = 0

        constructor() {
            currentNode = null
            nextIndex = 0
            nextNode = head
        }

        constructor(i: Int) {
            currentNode = null
            nextIndex = i
            nextNode = getNode(i)
        }

        override fun add(element: V) {
            if (nextIndex < 1) {
                currentNode = addToHead(element)
                nextIndex++
            } else {
                currentNode = insertData(nextIndex, element)
                nextIndex++
            }
        }

        override fun hasNext(): Boolean {
            return nextIndex != size
        }

        override fun hasPrevious(): Boolean {
            return nextIndex != 0
        }

        override fun next(): V {
            if (hasNext()) {
                currentNode = nextNode
                nextNode = nextNode!!.next
                nextIndex++
                return currentNode!!.data
            } else {
                throw NoSuchElementException("No next element!")
            }
        }

        override fun nextIndex(): Int {
            return nextIndex
        }

        override fun previous(): V {
            if (hasPrevious()) {
                if (nextNode == null) {
                    nextNode = currentNode
                } else {
                    currentNode = nextNode!!.previous 
                    nextNode = nextNode!!.previous
                }
                nextIndex--
                return currentNode!!.data
            } else {
                throw NoSuchElementException("No previous element!")
            }
        }

        override fun previousIndex(): Int {
            return nextIndex - 1
        }

        override fun remove() {
            val lastNext = currentNode!!.next
            try {
                this@DoublyLinkedList.remove(currentNode!!.data)
            } catch (e: NoSuchElementException) {
                throw IllegalStateException()
            }

            if (nextNode == currentNode) {
                nextNode = lastNext
            } else {
                nextIndex--
                currentNode = head
            }
        }

        override fun set(element: V) {
            if (currentNode != null) {
                currentNode!!.data = element
            } else {
                throw IllegalStateException("Nothing to set!")
            }
        }
    }

    override fun remove(element: V): Boolean {
        if (isEmpty()) {
            throw NoSuchElementException()
        } else {
            var node = head
            while (node != null) {
                if (element != null && element.equals(node.data)) {
                    return removeNode(node) != null
                }
                
                node = node.next
            }
            throw NoSuchElementException()
        }
    }
    
    override fun removeAt(index: Int): V? {
        val n = removeNode(index)
        
        return n?.data
    }

    override fun removeAll(elements: Collection<V>): Boolean {
        elements.forEach { 
            remove(it)
        }
        return true
    }

    override fun retainAll(elements: Collection<V>): Boolean {
        if (isEmpty()) {
            return true
        } else {
            var node = head
            while (node != null) {
                val iter = elements.iterator()
                var canRemove = true
                while (iter.hasNext()) {
                    val o = iter.next()
                    if (o != null && o.equals(node.data)) {
                        canRemove = false;
                        break;
                    }
                }
                if (canRemove) {
                    if (node.hasPrevious()) {
                        node.previous!!.next = node.next
                    } else {
                        head = node.next
                    }
                    if (node.hasNext()) {
                        node.next!!.previous = node.previous
                    } else {
                        tail = node.previous
                    }
                    val n = node
                    node = node.next
                    n.previous = null
                    n.next = null
                    size--;
                } else {
                    node = node.next
                }
            }
            return true;
        }
    }

    override fun set(index: Int, element: V): V? {
        val node = getNode(index)
        var pData: V? = null
        if (node != null) {
            pData = node.data
            node.data = element
        }
        return pData
    }

    companion object {
        class HeadTailContainer<V> {
            var head: DoublyNode<V>? = null
            var tail: DoublyNode<V>? = null

            constructor(): this(null, null)

            constructor(n: DoublyNode<V>?): this(n, n)

            constructor(h: DoublyNode<V>?, t: DoublyNode<V>?) {
                head = h
                tail = t
            }
        }
    }
}