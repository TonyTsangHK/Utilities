package utils.data

import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-05
 * Time: 10:33
 *
 * AVL tree implemented as java list
 *
 * @author Tony Tsang
 *
 * @param <E> Generic data type
 */
class SortedListAvl<E>: SortedList<E> {
    /**
     * Root node
     */
    private var root : BinaryTreeNode<E>?

    /**
     * List size
     */
    override var size: Int = 0

    /**
     * Comparator used for sorting
     */
    private lateinit var comparator: Comparator<E?>

    /**
     * Modification count for concurrent modification check
     */
    protected var modCount = 0

    /**
     * Private construct used for cloning
     *
     * @param root root node
     * @param comparator data comparator
     * @param size list size
     */
    private constructor(root: BinaryTreeNode<E>, comparator: Comparator<E?>, size: Int) {
        this.root = root
        this.comparator = comparator
        this.size = size
    }

    /**
     * Construct an AVL Tree List, default: ascending order, null as smaller value
     */
    constructor() : this(true)

    /**
     * Construct an AVL Tree List, providing sort order, default null as smaller value
     *
     * @param asc in ascending order (true) or descending order (false)
     */
    constructor(asc: Boolean): this(asc, true)

    /**
     * Construct an AVL Tree List, provided sort order and null order
     *
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(asc: Boolean, nullAsSmaller: Boolean) {
        comparator = DataComparator.buildComparator(asc, nullAsSmaller)
        root = null
        size = 0
    }

    /**
     * Construct an AVL Tree List, providing comparator used for sorting, default: null as smaller value
     *
     * @param comparator comparator used for sorting
     */
    constructor(comparator: Comparator<E?>): this(comparator, true)

    /**
     * Construct an AVL Tree List, providing comparator used for sorting and null order
     *
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(comparator: Comparator<E?>, nullAsSmaller: Boolean) {
        this.comparator = DataComparator.buildComparator(comparator, nullAsSmaller)
        root = null
        size = 0
    }

    /**
     * Construct an AVL Tree List with existing data collection, default ascending order, null as smaller value<br>
     * Data will be sorted after construction
     *
     * @param c existing data collection
     */
    constructor(c: Collection<E>): this() {
        addAll(c)
    }

    /**
     * Construct an AVL Tree List with existing data collection, proving sort order, default: null as smaller value<br>
     * Data will be sorted after construction
     *
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     */
    constructor(c: Collection<E>, asc: Boolean): this(asc) {
        addAll(c)
    }

    /**
     * Construct an AVL Tree List with existing data collection, proving sort order & null order<br>
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
     * Construct an AVL Tree List with existing data collection, proving comparator used for sorted<br>
     * Default: null as smaller value<br>
     * Data will be sorted after construction
     *
     * @param c existing data collection
     * @param comparator comparator used for sorting
     */
    constructor(c: Collection<E>, comparator: Comparator<E?>): this(c, comparator, true)

    /**
     * Construct an AVL Tree List with existing data collection, proving comparator used for sorted & null order<br>
     * Data will be sorted after construction
     *
     * @param c existing data collection
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    constructor(
            c: Collection<E>, comparator: Comparator<E?>, nullAsSmaller: Boolean
    ): this(comparator, nullAsSmaller) {
        addAll(c)
    }

    /**
     * Private add method, ignoring modification counter restriction
     *
     * @param e data element to be added
     *
     * @return true as successful, false as unsuccessful
     */
    private fun privateAdd(e: E): Boolean {
        if (root == null) {
            val node: BinaryTreeNode<E> = BinaryTreeNode<E>(e, null, null, null)
            root = node
            size = 1
            return true
        } else {
            var currentNode: BinaryTreeNode<E> = root!!
            val newNode: BinaryTreeNode<E>

            while (true) {
                val comp: Int = comparator.compare(currentNode.element, e)
                if (comp >= 0) {
                    if (currentNode.hasLeft()) {
                        currentNode = currentNode.left
                    } else {
                        newNode = BinaryTreeNode<E>(e, currentNode, null, null)
                        currentNode.left = newNode
                        size++
                        break
                    }
                } else {
                    if (currentNode.hasRight()) {
                        currentNode = currentNode.right
                    } else {
                        newNode = BinaryTreeNode<E>(e, currentNode, null, null)
                        currentNode.right = newNode
                        size++
                        break
                    }
                }
            }
            notifyNodeAdded(newNode, true)
            return true
        }
    }

    /**
     * Add an element into this list
     *
     * @param e element to be added
     * @return true as successful, false as unsuccessful
     */
    override fun add(e: E): Boolean {
        if (privateAdd(e)) {
            modCount++
            return true
        } else {
            return false
        }
    }

    /**
     * Add the provided elements to this list (var args)
     *
     * @param vals element values to be added
     *
     * @return true if modified, false otherwise
     */
    fun add(vararg vals: E): Boolean {
        var modified = false

        vals.forEach {
            modified = modified or add(it)
        }

        return modified
    }

    /**
     * Notify a node is removed from this list
     *
     * @param n removed node
     * @param parent parent of removed node
     * @param nodeType node type of removed node to its parent node
     * @param rebalance this AVL Tree should be rebalanced or not
     */
    private fun notifyNodeRemoved(
        n: BinaryTreeNode<E>, parent: BinaryTreeNode<E>?, nodeType: BinaryTreeNode.Type, rebalance: Boolean
    ) {
        if (parent != null) {
            if (nodeType == BinaryTreeNode.Type.LEFT) {
                parent.decrementLeftNodeCount()
            } else if (nodeType == BinaryTreeNode.Type.RIGHT) {
                parent.decrementRightNodeCount()
            }
            if (rebalance) {
                if (nodeType == BinaryTreeNode.Type.LEFT) {
                    parent.decrementLeftDepth()
                } else if (nodeType == BinaryTreeNode.Type.RIGHT) {
                    parent.decrementRightDepth()
                }
                if (Math.abs(parent.balanceFactor) >= 2) {
                    val np: BinaryTreeNode<E>

                    if (parent.balanceFactor >= 2) {
                        if (parent.left.balanceFactor < 0) {
                            rotateLeft(parent.left)
                        }
                        np = rotateRight(parent)
                    } else {
                        if (parent.right.balanceFactor > 0) {
                            rotateRight(parent.right)
                        }
                        np = rotateLeft(parent)
                    }
                    if (np.parent != null) {
                        val rb: Boolean = Math.abs(np.balanceFactor) != 1
                        if (np == np.parent.left) {
                            notifyNodeRemoved(np, np.parent, BinaryTreeNode.Type.LEFT, rb)
                        } else if (np == np.parent.right) {
                            notifyNodeRemoved(np, np.parent, BinaryTreeNode.Type.RIGHT, rb)
                        }
                    }
                } else if (Math.abs(parent.balanceFactor) == 1) {
                    if (parent.parent != null) {
                        if (parent == parent.parent.left) {
                            notifyNodeRemoved(parent, parent.parent, BinaryTreeNode.Type.LEFT, false)
                        } else if (parent == parent.parent.right) {
                            notifyNodeRemoved(parent, parent.parent, BinaryTreeNode.Type.RIGHT, false)
                        }
                    }
                } else {
                    if (parent.parent != null) {
                        if (parent == parent.parent.left) {
                            notifyNodeRemoved(
                                    parent, parent.parent, BinaryTreeNode.Type.LEFT, rebalance
                            )
                        } else if (parent == parent.parent.right){
                            notifyNodeRemoved(
                                    parent, parent.parent, BinaryTreeNode.Type.RIGHT, rebalance
                            )
                        }
                    }
                }
            } else {
                if (parent.parent != null) {
                    if (parent == parent.parent.left) {
                        notifyNodeRemoved(parent, parent.parent, BinaryTreeNode.Type.LEFT, rebalance)
                    } else if (parent == parent.parent.right){
                        notifyNodeRemoved(parent, parent.parent, BinaryTreeNode.Type.RIGHT, rebalance)
                    }
                }
            }
        }
    }

    /**
     * Find the maximum element
     *
     * @return maximum element
     */
    fun findMax(): E? {
        return if(root == null) null else findMaxNode(root!!).element
    }

    /**
     * Find the minimum element
     *
     * @return minimum element
     */
    fun findMin(): E? {
        return if (root == null) null else findMinNode(root!!).element
    }

    /**
     * Find the maximum node starting from the provided node
     *
     * @param node starting node
     * @return maximum node
     */
    private fun findMaxNode(node: BinaryTreeNode<E>): BinaryTreeNode<E> {
        if (node.hasRight()) {
            return findMaxNode(node.right)
        } else {
            return node
        }
    }

    /**
     * Find the minimum node starting from the provided node
     *
     * @param node starting node
     * @return minimum node
     */
    private fun findMinNode(node: BinaryTreeNode<E>): BinaryTreeNode<E> {
        if (node.hasLeft()) {
            return findMinNode(node.left)
        } else {
            return node
        }
    }

    /**
     * Notify a node is added to this list
     *
     * @param n added node
     * @param rebalance this AVL Tree should be rebalanced or not
     */
    private fun notifyNodeAdded(n: BinaryTreeNode<E>, rebalance: Boolean) {
        val parent: BinaryTreeNode<E>? = n.parent
        if (parent != null) {
            if (n == parent.left) {
                parent.incrementLeftNodeCount()
            } else {
                parent.incrementRightNodeCount()
            }
            if (rebalance) {
                if (n == parent.left) {
                    parent.incrementLeftDepth()
                } else {
                    parent.incrementRightDepth()
                }
                if (Math.abs(parent.balanceFactor) >= 2) {
                    if (parent.balanceFactor >= 2) {
                        if (parent.left.balanceFactor < 0) {
                            rotateLeft(parent.left)
                        }
                        notifyNodeAdded(rotateRight(parent), false)
                    } else {
                        if (parent.right.balanceFactor > 0) {
                            rotateRight(parent.right)
                        }
                        notifyNodeAdded(rotateLeft(parent), false)
                    }
                } else if (parent.balanceFactor == 0) {
                    notifyNodeAdded(parent, false)
                } else {
                    notifyNodeAdded(parent, rebalance)
                }
            } else {
                notifyNodeAdded(parent, rebalance)
            }
        }
    }

    /**
     * Rotate the provided node to left
     *
     * @param node target node to rotate
     * @return the new parent of the rotated node
     */
    private fun rotateLeft(node: BinaryTreeNode<E>): BinaryTreeNode<E> {
        val tmp = node.right
        node.right = tmp.left
        if (tmp.left != null) {
            tmp.left.parent = node
        }
        tmp.parent = node.parent
        if (node.parent == null) {
            root = tmp
        } else {
            if (node == node.parent.left) {
                node.parent.left = tmp
            } else {
                node.parent.right = tmp
            }
        }
        tmp.left = node
        node.parent = tmp
        node.rightDepth = tmp.leftDepth
        node.rightNodeCount = tmp.leftNodeCount
        tmp.leftDepth = node.depth + 1
        tmp.leftNodeCount = node.nodeCount + 1
        return tmp
    }

    /**
     * Rotate the proved node to right
     *
     * @param node target node to rotate
     * @return the new parent of the rotated node
     */
    private fun rotateRight(node: BinaryTreeNode<E>): BinaryTreeNode<E> {
        val tmp = node.left
        node.left = tmp.right
        tmp.right?.parent = node
        tmp.parent = node.parent
        if (node.parent == null) {
            root = tmp
        } else {
            if (node == node.parent.right) {
                node.parent.right = tmp
            } else {
                node.parent.left = tmp
            }
        }
        tmp.right = node
        node.parent = tmp
        node.leftDepth = tmp.rightDepth
        node.leftNodeCount = tmp.rightNodeCount
        tmp.rightDepth = node.depth + 1
        tmp.rightNodeCount = node.nodeCount + 1
        return tmp
    }

    /**
     * Simply redirect to add(E), index is ignored as this is a sorted list
     *
     * @param index ignored
     * @param element data element
     */
    override fun add(index: Int, element: E) {
        // Index is ignored as this is a sortedList
        add(element) // modCount increment is handled
    }

    /**
     * Add the provided data collection
     *
     * @param c data collection
     *
     * @return true as successful, false as unsuccessful
     */
    override fun addAll(c: Collection<E>): Boolean {
        var modified = false
        c.forEach {
            if (privateAdd(it)) {
                modified = true
            }
        }

        if (modified) {
            modCount++
        }

        return modified
    }

    /**
     * {@inheritDoc}
     */
    override fun addAll(c: Collection<E>?, allowDuplicate: Boolean): Boolean {
        if (c != null) {
            var modified = false
            c.forEach {
                if (allowDuplicate || !contains(it)) {
                    if (privateAdd(it)) {
                        modified = true
                    }
                }
            }

            if (modified) {
                modCount++
            }
            return modified
        } else {
            return false
        }
    }

    /**
     * Simply redirect to allAll(), index is ignored as this is a sorted list
     *
     * @param index ignored
     * @param c data collection
     *
     * @return true as successful, false as unsuccessful
     */
    override fun addAll(index: Int, c: Collection<E>): Boolean {
        // Index is ignored as this is sortedList
        return addAll(c) // modCount incrementation is handled
    }

    /**
     * Clear this list
     */
    override fun clear() {
        root = null
        size = 0
        modCount++
    }

    /**
     * Check whether this list contains the provided data or not
     *
     * @param o target data
     *
     * @return check result
     */
    override fun contains(o: E): Boolean {
        var currentNode = root

        while (currentNode != null) {
            val c = comparator.compare(currentNode.element, o)
            if (c == 0) {
                return true
            } else if (c < 0) {
                currentNode = currentNode.right
            } else {
                currentNode = currentNode.left
            }
        }
        return false
    }

    /**
     * Check whether this list contains all of the provided data collection
     *
     * @param c data collection
     *
     * @return check result
     */
    override fun containsAll(c: Collection<E>): Boolean {
        c.forEach {
            if (!contains(it)) {
                return false
            }
        }

        return true
    }

    /**
     * Check the provided index is within range, otherwise IndexOutOfBoundsException is thrown
     *
     * @param index target index
     */
    private fun checkIndex(index: Int) {
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException("Index: $index, Size: $size")
        }
    }

    /**
     * Same as getMin
     *
     * @return first(minimum) node
     */
    fun getFirst(): E? {
        return getMin()
    }

    /**
     * Get the minimum node
     *
     * @return minimum node
     */
    override fun getMin(): E? {
        if (root == null) {
            return null
        } else {
            return findMinNode(root!!).element
        }
    }

    /**
     * Same as getMax
     *
     * @return last(maximum) node
     */
    fun getLast(): E? {
        return getMax()
    }

    /**
     * Get the maximum node
     *
     * @return maximum node
     */
    override fun getMax(): E? {
        if (root == null) {
            return null
        } else {
            return findMaxNode(root!!).element
        }
    }

    /**
     * Poll the first node out of the list
     *
     * @return the first node
     */
    fun poll(): E? {
        return pollMin()
    }

    /**
     * Poll the first node out of the list
     *
     * @return the first node
     */
    fun pollFirst(): E? {
        return pollMin()
    }

    /**
     * Poll the minimum node out of the list
     *
     * @return the minimum node
     */
    fun pollMin(): E? {
        if (root == null) {
            return null
        } else {
            return removeAt(0)
        }
    }

    /**
     * Poll the last node out of the list
     *
     * @return the maximum node
     */
    fun pollLast(): E? {
        return pollMax()
    }

    fun pollMax(): E? {
        if (root == null) {
            return null
        } else {
            return removeAt(size - 1)
        }
    }

    /**
     * Get the data element from index
     *
     * @param index target index
     *
     * @return the data element
     */
    override fun get(index: Int): E? {
        val n = getNode(index)
        if (n == null) {
            return null
        } else {
            return n.element
        }
    }

    /**
     * Search for first found index from binary search, no guarantee of first or last index
     *
     * @param o target value
     * @return first found index from binary search
     */
    fun binarySearch(o: E): Int {
        if (root == null) {
            return -1
        }

        return indexOf(o, root!!, root!!.leftNodeCount)
    }

    /**
     * Search for first found index from binary search with cross comparator, no guarantee of first or last index
     *
     * @param o target value
     * @param crossComparator cross comparator of target value type to list element type
     * @return first found index from binary search
     */
    fun <O> binarySearch(o: O, crossComparator: CrossComparator<O, E>): Int {
        if (root == null) {
            return -1
        } else {
            return indexOf(o, root!!, root!!.leftNodeCount, crossComparator)
        }
    }

    /**
     * Get the index of the target element
     *
     * @param o target element
     *
     * @return index of the target element, -1 if not found
     */
    @SuppressWarnings("unchecked")
    override fun indexOf(o: E): Int {
        if (root == null) {
            return -1
        }

        return indexOf(o, root!!, root!!.leftNodeCount, true)
    }

    /**
     * Get the index of the target value (in other type) with cross comparator
     *
     * @param o target value
     * @param crossComparator cross comparator of target value type to list element type
     *
     * @return index of the target element, -1 if not found
     */
    fun <O> indexOf(o: O, crossComparator: CrossComparator<O, E>): Int {
        if (root == null) {
            return -1
        } else {
            return indexOf(o, root!!, root!!.leftNodeCount, crossComparator, true)
        }
    }

    /**
     * Get the last index of the target value (in other type) with cross comparator
     *
     * @param o target value
     * @param crossComparator cross comparator of target value type to list element type
     *
     * @return index of the target element, -1 if not found
     */
    fun <O> lastIndexOf(o: O, crossComparator: CrossComparator<O, E>): Int {
        if (root == null) {
            return -1
        } else {
            return indexOf(o, root!!, root!!.leftNodeCount, crossComparator, false)
        }
    }

    /**
     * Find the index of the target value (in other type) with cross comparator
     *
     * @param o target value
     * @param node starting node
     * @param index index of the starting node
     * @param crossComparator cross comparator of target value type to list element type
     * @return index of the target element (first found index from binary search), -1 if not found
     */
    private fun <O> indexOf(o: O, node: BinaryTreeNode<E>, index: Int, crossComparator: CrossComparator<O, E>): Int {
        val compareResult = crossComparator.compare(o, node.element)
        if (compareResult == 0) {
            return index
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(o, node.left, index - node.left.rightNodeCount - 1, crossComparator)
            } else {
                return -1
            }
        } else {
            if (node.hasRight()) {
                return indexOf(o, node.right, index + node.right.leftNodeCount + 1, crossComparator)
            } else {
                return -1
            }
        }
    }

    /**
     * Find the index of the target value (in other type) with cross comparator
     *
     * @param o target value
     * @param node starting node
     * @param index index of the starting node
     * @param crossComparator cross comparator of target value type to list element type
     * @param findSmaller true to find smallest index if smaller value is found, otherwise find the greatest index
     * @return index of the target element, -1 if not found
     */
    private fun <O> indexOf(o: O, node: BinaryTreeNode<E>, index: Int, crossComparator: CrossComparator<O, E>, findSmaller: Boolean): Int {
        val compareResult = crossComparator.compare(o, node.element)

        if (compareResult == 0) {
            if (findSmaller) {
                if (node.hasLeft()) {
                    val smallerIndex =
                        indexOf(
                            o, node.left, index - node.left.rightNodeCount - 1, crossComparator, findSmaller
                        )
                    return if (smallerIndex != -1) smallerIndex else index
                } else {
                    return index
                }
            } else {
                if (node.hasRight()) {
                    val greaterIndex =
                        indexOf(
                            o, node.right, index + node.right.leftNodeCount + 1, crossComparator, findSmaller
                        )
                    return if (greaterIndex != -1) greaterIndex else index
                } else {
                    return index
                }
            }
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(o, node.left, index - node.left.rightNodeCount - 1, crossComparator, findSmaller)
            } else {
                return -1
            }
        } else {
            if (node.hasRight()) {
                return indexOf(o, node.right, index + node.right.leftNodeCount + 1, crossComparator, findSmaller)
            } else {
                return -1
            }
        }
    }

    /**
     * Find the index of the target element
     *
     * @param e target element
     * @param node starting node
     * @param index index of the starting node
     * @return index of the target element (first found index from binary search), -1 if not found
     */
    private fun indexOf(e: E, node: BinaryTreeNode<E>, index: Int): Int {
        val compareResult = comparator.compare(e, node.element)
        if (compareResult == 0) {
            return index
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(e, node.left, index - node.left.rightNodeCount - 1)
            } else {
                return -1
            }
        } else {
            if (node.hasRight()) {
                return indexOf(e, node.right, index + node.right.leftNodeCount + 1)
            } else {
                return -1
            }
        }
    }

    /**
     * Find the index of the target element
     *
     * @param e target element
     * @param node starting node
     * @param index index of the starting node
     * @param findSmaller true to find smallest index if smaller value is found, otherwise find the greatest index
     * @return index of the target element, -1 if not found
     */
    private fun indexOf(e: E, node: BinaryTreeNode<E>, index: Int, findSmaller: Boolean): Int {
        val compareResult = comparator.compare(e, node.element)
        if (compareResult == 0) {
            if (findSmaller) {
                if (node.hasLeft()) {
                    val smallerIndex =
                        indexOf(
                            e, node.left, index - node.left.rightNodeCount - 1, findSmaller
                        )
                    return if (smallerIndex != -1) smallerIndex else index
                } else {
                    return index
                }
            } else {
                if (node.hasRight()) {
                    val greaterIndex =
                        indexOf(
                            e, node.right, index + node.right.leftNodeCount + 1, findSmaller
                        )
                    return if (greaterIndex != -1) greaterIndex else index
                } else {
                    return index
                }
            }
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(e, node.left, index - node.left.rightNodeCount - 1, findSmaller)
            } else {
                return -1
            }
        } else {
            if (node.hasRight()) {
                return indexOf(e, node.right, index + node.right.leftNodeCount + 1, findSmaller)
            } else {
                return -1
            }
        }
    }

    /**
     * Check whether this list is empty or not
     *
     * @return true as empty, false otherwise
     */
    override fun isEmpty(): Boolean {
        return size == 0
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
     * Get the last index of the target element
     *
     * @param o target element
     *
     * @return index of the target element, -1 if not found
     */
    override fun lastIndexOf(o: E): Int {
        if (root == null) {
            return -1
        }

        return indexOf(o, root!!, root!!.leftNodeCount, false)
    }

    /**
     * Get the ListIterator of this list
     *
     * @return ListIterator of this list
     */
    override fun listIterator(): MutableListIterator<E> {
        return SortedListIterator()
    }

    /**
     * Get the ListIterator of this list with starting index
     *
     * @return ListIterator of this list with starting index
     */
    override fun listIterator(index: Int): MutableListIterator<E> {
        return SortedListIterator(index)
    }

    /**
     * Swap the element of the provided node
     *
     * @param n1 node 1
     * @param n2 node 2
     */
    private fun swapNodeElement(n1: BinaryTreeNode<E>, n2: BinaryTreeNode<E>) {
        val element = n1.element
        n1.element = n2.element
        n2.element = element
    }

    /**
     * Swap the element of the provided node index, p.s. only the element swapped but not the nodes
     *
     * @param i1 index 1
     * @param i2 index 2
     */
    private fun swapElement(i1: Int, i2: Int) {
        checkIndex(i1)
        checkIndex(i2)

        swapNodeElement(getNode(i1)!!, getNode(i2)!!)
    }

    /**
     * Get the node of the target index
     *
     * @param index target index
     * @return node of the target index
     */
    private fun getNode(index: Int): BinaryTreeNode<E>? {
        checkIndex(index)
        return locateNodeByRelativeIndex(root, index)
    }

    /**
     * Location node by the relative index from the reference node<br>
     * Relative index of a node is the left node count of that node
     *
     * @param node reference node
     * @param relativeIndex relative index to the node from reference node
     * @return target node
     */
    private fun locateNodeByRelativeIndex(node: BinaryTreeNode<E>?, relativeIndex: Int): BinaryTreeNode<E>? {
        if (node == null) {
            return null
        } else {
            val leftNodeCount = node.leftNodeCount
            if (relativeIndex == leftNodeCount) {
                return node
            } else if (relativeIndex < leftNodeCount) {
                return locateNodeByRelativeIndex(node.left, relativeIndex)
            } else if (relativeIndex > leftNodeCount) {
                return locateNodeByRelativeIndex(node.right, relativeIndex-leftNodeCount-1)
            } else {
                return null
            }
        }
    }

    /**
     * Locate a node with the provided element
     *
     * @param element target element
     *
     * @return target node, null if not found
     */
    private fun locateNode(element: E): BinaryTreeNode<E>? {
        var currentNode = root
        while (currentNode != null) {
            val comp = comparator.compare(element, currentNode.element)
            if (comp == 0) {
                return currentNode
            } else if (comp < 0) {
                if (currentNode.hasLeft()) {
                    currentNode = currentNode.left
                } else {
                    break
                }
            } else {
                if (currentNode.hasRight()) {
                    currentNode = currentNode.right
                } else {
                    break
                }
            }
        }
        return null
    }

    /**
     * Remove the provided node
     *
     * @param node node to be removed
     */
    private fun removeNode(node: BinaryTreeNode<E>?) {
        if (node == null) {
            return
        }
        if (node.hasChild()) {
            val s:BinaryTreeNode<E>
            val sParent: BinaryTreeNode<E>
            val parent = node.parent
            val left = node.left
            val right = node.right

            val nType: BinaryTreeNode.Type

            if (left != null) {
                s = findMaxNode(left)
                sParent = s.parent
                if (sParent.left == s) {
                    nType = BinaryTreeNode.Type.LEFT
                    sParent.left = null
                } else {
                    nType = BinaryTreeNode.Type.RIGHT
                    sParent.right = null
                }
                s.parent = parent
                s.right = right

                right?.parent = s
                if (left != s) {
                    if (s.left != null) {
                        sParent.right = s.left
                        s.left.parent = sParent
                    }
                    s.left = left
                    left.parent = s
                }
            } else {
                s = findMinNode(right)
                sParent = s.parent
                if (sParent.right == s) {
                    nType = BinaryTreeNode.Type.RIGHT
                    sParent.right = null
                } else {
                    nType = BinaryTreeNode.Type.LEFT
                    sParent.left = null
                }
                s.parent = parent
                s.left = left

                if (right != s) {
                    if (s.right != null) {
                        sParent.left = s.right
                        s.right.parent = sParent
                    }
                    s.right = right
                    right?.parent = s
                }
            }

            if (parent != null) {
                if (node == parent.right) {
                    parent.right = s
                } else {
                    parent.left = s
                }
            } else {
                root = s
            }

            s.leftDepth = node.leftDepth
            s.leftNodeCount = node.leftNodeCount
            s.rightDepth = node.rightDepth
            s.rightNodeCount = node.rightNodeCount
            size--
            if (sParent != node) {
                notifyNodeRemoved(s, sParent, nType, true)
            } else {
                notifyNodeRemoved(s, s, nType, true)
            }
            modCount++
        } else {
            if (node == root) {
                root = null
                size = 0
            } else {
                val parent = node.parent
                node.parent = null
                size--
                if (parent != null) {
                    if (node == parent.left) {
                        parent.left = null
                        notifyNodeRemoved(node, parent, BinaryTreeNode.Type.LEFT, true)
                    } else {
                        parent.right = null
                        notifyNodeRemoved(node, parent, BinaryTreeNode.Type.RIGHT, true)
                    }
                }
            }
            modCount++
        }
    }

    /**
     * Remove the provided element
     *
     * @param o target element
     *
     * @return true as successful remove, false as unsuccessful
     */
    override fun remove(o: E): Boolean {
        val node = locateNode(o)
        if (node != null) {
            removeNode(node) // modCount incrementation is handled
            return true
        } else {
            return false
        }
    }

    /**
     * Remove the provided elements (varargs)
     *
     * @param vals values to remove
     *
     * @return true if modified, false otherwise
     */
    fun remove(vararg vals: E): Boolean {
        var modified = false
        vals.forEach {
            modified = modified or remove(it)
        }

        return modified
    }

    /**
     * Remove the provided index
     *
     * @param index target index to be removed
     *
     * @return the removed element
     */
    override fun removeAt(index: Int): E {
        checkIndex(index)
        val node = locateNodeByRelativeIndex(root, index)!!

        val element = node.element
        removeNode(node) // modCount incrementation is handled
        return element
    }

    /**
     * Remove the provided data collection
     *
     * @param c the data collection to be removed
     *
     * @return true as successful, false as unsuccessful
     */
    override fun removeAll(c: Collection<E>): Boolean {
        var modified = false

        c.forEach(
            {
                if (remove(it)) {
                    modified = true
                }
            }
        )
        return modified
    }

    /**
     * Retain only the element contained within the provided data collection
     *
     * @param c target data collection
     *
     * @return true as successful, false as unsuccessful
     */
    @SuppressWarnings("unchecked")
    override fun retainAll(c: Collection<E>): Boolean {
        val s = SortedListAvl<E>(comparator)

        c.forEach(
            {
                val firstIndex = indexOf(it)

                if (firstIndex != -1 && !s.contains(it)) {
                    s.add(it)
                } else {
                    for (i in firstIndex .. lastIndex) {
                        s.add(it)
                    }
                }
            }
        )

        if (s.size < size) {
            root = s.root
            size = s.size
            modCount++
            return true
        } else {
            return false
        }
    }

    /**
     * This method is not the same as normal list set!<br>
     *
     * Element of the provided index is removed, then adding the new element into the list
     *
     * @param index element on this index will be removed then re-add, new element index may change!
     * @param element new data element to be added
     *
     * @return removed data element
     */
    override fun set(index: Int, element: E): E {
        checkIndex(index)
        // As this is a sorted list old element is removed and then re-add the element, index may change.

        val e = removeAt(index)
        privateAdd(element)
        return e
    }

    /**
     * Sublist is not supported, there is no easy way to implement sublist's add, remove, set feature without affecting the sort order of the original list!<br>
     * Use {@link #getSubList} instead
     *
     * @return nothing will be returned!
     */
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> {
        throw UnsupportedOperationException("SubList is not supported!")
    }

    /**
     * This is not the same as sublist but return a newly created AVL Tree List of the provided from & to index<br>
     * Add/remove/set to the resulted list will not affect the original list
     *
     * @param fromIndex starting index of the sublist, inclusive
     * @param toIndex ending index of the sublist, exclusive
     *
     * @return new AVL Tree List consisting elements provided by from & to index
     */
    override fun getSubList(fromIndex: Int, toIndex: Int): SortedListAvl<E> {
        val list = SortedListAvl<E>()
        if (root != null) {
            getSubList(list, root!!, root!!.leftNodeCount, fromIndex, toIndex)
        }
        return list
    }

    /**
     * Helper method for constructing sub list elements
     *
     * @param list sub list
     * @param node target node
     * @param index index of the target node
     * @param fromIndex starting index of the sublist, inclusive
     * @param toIndex ending index of the sublist, exclusive
     */
    private fun getSubList(
        list: SortedListAvl<E>, node: BinaryTreeNode<E>, index: Int, fromIndex: Int, toIndex: Int
    ) {
        if (index >= fromIndex && index < toIndex) {
            list.add(node.element)
        }
        if (node.hasLeft() && index > fromIndex) {
            getSubList(list, node.left, index - node.left.rightNodeCount - 1, fromIndex, toIndex)
        }
        if (node.hasRight() && index < toIndex - 1) {
            getSubList(list, node.right, index + node.right.leftNodeCount + 1, fromIndex, toIndex)
        }
    }

    /**
     * Get the depth of tree
     *
     * @return depth of tree
     */
    fun getTreeDepth():Int {
        if (root == null) {
            return 0
        } else {
            return root!!.depth + 1
        }
    }

    /**
     * Helper method to build an array
     *
     * @param <T> Data generic type
     * @param array target array
     * @param node current node
     * @param index current node index
     */
    private fun buildArray(array: Array<in E>, node: BinaryTreeNode<E>, index: Int) {
        if (index < array.size) {
            array[index] = node.element
        }
        if (node.hasLeft()) {
            buildArray(array, node.left, index - node.left.rightNodeCount -1)
        }
        if (node.hasRight()) {
            buildArray(array, node.right, index + node.right.leftNodeCount +1)
        }
    }

    /**
     * Print the tree structure, used for debug only
     */
    fun printList() {
        if (root == null) {
            println("- List Empty -")
        } else {
            inorder(root!!)
            System.out.println()
        }
    }

    /**
     * Print the node structure using inorder tree traversal technique
     *
     * @param node current node
     */
    private fun inorder(node: BinaryTreeNode<E>) {
        if (node.hasLeft()) {
            inorder(node.left)
        }
        print(
            "${node.element.toString()} -[${node.leftDepth}, ${node.rightDepth}, ${node.leftNodeCount}, ${node.rightNodeCount}](p: ${(if (node.parent!=null) node.parent.element else "null")}), (l: ${(if (node.left !=null) node.left.element else "null")}), (r: ${(if (node.right !=null) node.right.element else "null")})-"
        )
        if (node.hasRight()) {
            inorder(node.right)
        }
    }

    /**
     * Clone the node together with its structure
     *
     * @param copiedParent the cloned parent node
     * @param node node to be cloned
     *
     * @return the cloned node
     */
    private fun cloneNodes(copiedParent: BinaryTreeNode<E>?, node: BinaryTreeNode<E>): BinaryTreeNode<E> {
        val newNode = BinaryTreeNode<E>(node.element, copiedParent, null, null)

        newNode.leftDepth = node.leftDepth
        newNode.rightDepth = node.rightDepth
        newNode.leftNodeCount = node.leftNodeCount
        newNode.rightNodeCount = node.rightNodeCount

        if (node.left != null) {
            newNode.left = cloneNodes(newNode, node.left)
        }

        if (node.right != null) {
            newNode.right = cloneNodes(newNode, node.right)
        }

        return newNode
    }

    /**
     * Clone this AVL Tree List
     *
     * @return cloned list
     */
    override fun clone(): SortedListAvl<E> {
        if (root != null) {
            val newRoot = cloneNodes(null, root!!)
            return SortedListAvl<E>(newRoot, comparator, size)
        } else {
            return SortedListAvl<E>()
        }
    }

    /**
     * Helper method to set the node element provided by the element array
     *
     * @param sortedArray element array in sorted order
     * @param node current node
     * @param index current node index
     */
    private fun setNodeElement(sortedList : List<E?>, node: BinaryTreeNode<E>, index: Int) {
        node.element = sortedList[index]
        if (node.hasLeft()) {
            setNodeElement(sortedList, node.left, index - node.left.rightNodeCount - 1)
        }
        if (node.hasRight()) {
            setNodeElement(sortedList, node.right, index + node.right.leftNodeCount + 1)
        }
    }

    fun toGeneralList(): MutableList<E?> {
        val result = arrayListOf<E?>()

        val iter = listIterator()

        while (iter.hasNext()) {
            result.add(iter.next())
        }

        return result
    }

    /**
     * Resort this AVL Tree List in case the underlying element order is changed accidentally<br>
     * (<br/>
     * <p style="margin-left: 25px;">
     *     rare, as direct access to the underlying Tree node is restricted, there is no way to manipulate the element order directly other than changing the element value,<br/>
     *     it is recommended to set element value through {@link #set(int, E) set} method.<br/>
     * </p>
     * )
     *
     */
    override fun resort() {
        if (root != null) {
            val generalList = toGeneralList()

            Collections.sort(generalList, comparator)

            setNodeElement(generalList, root!!, root!!.leftNodeCount)
        }
    }

    /**
     * List Iterator for this specific AVL Tree List
     *
     * @author Tony Tsang
     *
     */
    private inner class SortedListIterator: MutableListIterator<E> {
        /**
         * Previous node cache
         */
        private var previousNode: BinaryTreeNode<E>?
        /**
         * Next node cache
         */
        private var nextNode: BinaryTreeNode<E>?

        /**
         * Previous index cache
         */
        private var previousIndex: Int
        /**
         * Next index cache
         */
        private var nextIndex: Int
        /**
         * Current index cache - maybe deleted
         */
        private var currentIndex: Int

        /**
         * Flag indicating call to remove is valid or not
         */
        private var canRemove: Boolean = false

        /**
         * Counter used to detect concurrent modification
         */
        private var expectedModCount:Int = modCount

        /**
         * Default iterator constructor to start from head
         */
        constructor() {
            nextIndex     =  0
            currentIndex  = -1
            previousIndex = -1

            previousNode = null

            if (root != null) {
                nextNode = findMinNode(root!!)
            } else {
                nextNode = null
            }
        }

        /**
         * Constructor a list iterator start from the provided index
         *
         * @param i starting index
         */
        constructor(i: Int) {
            checkIndex(i)
            nextIndex     = i
            previousIndex = i - 1
            currentIndex  = -1

            nextNode = getNode(i)
            previousNode = nextNode!!.previousNode
        }

        /**
         * Check for concurrent modification
         */
        private fun checkForComodification() {
            if (expectedModCount != modCount) {
                throw ConcurrentModificationException()
            }
        }

        /**
         * Add an element into the underlying AVL Tree list, (considering to change this method to unsupported)<br>
         * P.S. Element will not added to the current location of the iterator but in its sort order
         *
         */
        override fun add(e: E) {
            this@SortedListAvl.add(e)
            val index: Int = indexOf(e)
            if (index <= currentIndex) {
                currentIndex += 1
                nextIndex     = currentIndex + 1
                previousIndex = currentIndex - 1

                nextNode     = getNode(nextIndex)
                previousNode = nextNode!!.previousNode.previousNode
            }
            canRemove = true
            expectedModCount++
        }

        /**
         * Check whether there is any next element
         *
         * @return true has next, false otherwise
         */
        override fun hasNext(): Boolean {
            return nextIndex < size
        }

        /**
         * Check whether there is any previous element
         *
         * @return true has previous, false otherwise
         */
        override fun hasPrevious(): Boolean {
            return previousIndex >= 0
        }

        /**
         * Get the next element
         *
         * @return next element
         */
        override fun next(): E {
            checkForComodification()
            if (nextIndex < size) {
                canRemove = true
                currentIndex = nextIndex
                nextIndex++
                previousIndex = nextIndex - 1

                val element = nextNode!!.element

                previousNode = nextNode
                nextNode     = nextNode!!.nextNode

                return element
            } else {
                throw NoSuchElementException("No next element")
            }
        }

        /**
         * Get the next index
         *
         * @return next index
         */
        override fun nextIndex(): Int {
            if (nextIndex < size) {
                return nextIndex
            } else {
                return size
            }
        }

        /**
         * Get the previous element
         *
         * @return previous element
         */
        override fun previous(): E {
            checkForComodification()
            if (previousIndex >= 0) {
                currentIndex = previousIndex
                previousIndex--
                nextIndex = previousIndex + 1

                val element = previousNode!!.element

                nextNode     = previousNode
                previousNode = previousNode!!.previousNode

                canRemove = true
                return element
            } else {
                throw NoSuchElementException("No previous element (previousIndex: $previousIndex)")
            }
        }

        /**
         * Get the previous index
         *
         * @return previous index
         */
        override fun previousIndex(): Int {
            if (previousIndex >= 0) {
                return previousIndex
            } else {
                return -1
            }
        }

        /**
         * Remove the current element from the underlying AVL Tree List
         *
         */
        override fun remove() {
            if (!canRemove || currentIndex < 0 || currentIndex >= size) {
                throw IllegalStateException("Nothing to remove (currentIndex: $currentIndex)")
            } else {
                checkForComodification()
                removeAt(currentIndex)
                nextIndex = currentIndex
                previousIndex = currentIndex - 1

                if (nextIndex >= 0) {
                    if (nextIndex >= size) {
                        nextNode = null
                    } else {
                        nextNode = getNode(nextIndex)
                    }
                }
                if (previousIndex >= 0) {
                    previousNode = getNode(previousIndex)
                }

                canRemove = false
                expectedModCount++
            }
        }

        /**
         * Set is not supported, as it contradict with normal expectation of this method.
         */
        override fun set(e: E) {
            throw UnsupportedOperationException("Set operation is not supported (this is a sorted list)!")
        }
    }
}