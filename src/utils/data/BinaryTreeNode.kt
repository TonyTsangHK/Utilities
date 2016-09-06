package utils.data

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-05
 * Time: 17:26
 */
class BinaryTreeNode<E>: ValueHolder<E> {
    /**
     * Node type
     */
    enum class Type
    /**
     * Private constructor

     * @param desc string representation of this note type
     */
    constructor(
        /**
         * String represent of this node type
         */
        val desc: String
    ) {
        /**
         * Left node type
         */
        LEFT("left"),
        /**
         * Right node type
         */
        RIGHT("right");

        /**
         * Get a string representation of this node type
         */
        override fun toString(): String {
            return desc
        }
    }

    /**
     * Left child node
     */
    var left: BinaryTreeNode<E>? = null

    /**
     * Set left child node with option to keeping consistency, i.e. set child node's parent to this node

     * @param l new left child node
     * *
     * @param keepConsistency keep consistency or not
     */
    fun setLeft(l: BinaryTreeNode<E>?, keepConsistency: Boolean) {
        left = l
        if (keepConsistency && l != null && l.parent !== this) {
            l.parent = this
        }
    }
    
    /**
     * Right child node
     */
    var right: BinaryTreeNode<E>? = null

    /**
     * Set right child node with option to keeping consistency, i.e. set child node's parent to this node

     * @param r new right child node
     * *
     * @param keepConsistency keep consistency or not
     */
    fun setRight(r: BinaryTreeNode<E>?, keepConsistency: Boolean) {
        right = r
        if (keepConsistency && r != null && r.parent !== this) {
            r.parent = this
        }
    }
    
    /**
     * Parent node
     */
    var parent: BinaryTreeNode<E>? = null

    /**
     * Depth of left descendants
     */
    var leftDepth: Int = 0
        /**
         * Set the left branch depth
    
         * @param depth left branch depth
         */
        set(depth: Int) {
            if (depth >= 0) {
                field = depth
            }
        }
    
    /**
     * Depth of right descendants
     */
    var rightDepth: Int = 0
        /**
         * Set the right branch depth
    
         * @param depth right branch depth
         */
        set(depth: Int) {
            if (depth >= 0) {
                field = depth
            }
        }
    
    /**
     * Left node counts
     */
    var leftNodeCount: Int = 0
        /**
         * Set node count of the left branch
    
         * @param nodeCount node count of left branch
         */
        set(nodeCount: Int) {
            if (nodeCount >= 0) {
                field = nodeCount
            }
        }
    
    /**
     * Right node counts
     */
    var rightNodeCount: Int = 0
        /**
         * Set node count of the right branch
    
         * @param nodeCount node count of right branch
         */
        set(nodeCount: Int) {
            if (nodeCount >= 0) {
                field = nodeCount
            }
        }

    /**
     * Contained data element
     */
    var element: E? = null

    /**
     * Construct a Binary tree node with contained data element

     * @param e contained data element
     */
    constructor(e: E): this(e, null, null, null)

    /**
     * Construct a Binary tree node with contained data element and its parent node

     * @param e data element
     * *
     * @param parent parent node
     */
    constructor(e: E, parent: BinaryTreeNode<E>): this(e, parent, null, null)

    /**
     * Construct a Binary tree node with contained data element and its left & right child nodes

     * @param e data element
     * *
     * @param left left child node
     * *
     * @param right right child node
     */
    constructor(e: E, left: BinaryTreeNode<E>, right: BinaryTreeNode<E>): this(e, null, left, right)

    /**
     * Construct a Binary tree node with contained data element and left & right child nodes and its parent node

     * @param e data element
     * *
     * @param parent parent node
     * *
     * @param left left child node
     * *
     * @param right right child node
     */
    constructor(e: E?, parent: BinaryTreeNode<E>?, left: BinaryTreeNode<E>?, right: BinaryTreeNode<E>?) {
        this.parent = parent
        this.left = left
        this.right = right
        this.element = e

        leftDepth = 0
        rightDepth = 0

        leftNodeCount = 0
        rightNodeCount = 0
    }

    /**
     * Get depth of descendants

     * @return depth of descendants
     */
    fun getDepth(): Int {
        return if (leftDepth > rightDepth) leftDepth else rightDepth
    }

    /**
     * Get balance factor

     * @return balance factor, i.e. difference of left & right branch depth
     */
    fun getBalanceFactor(): Int {
        return leftDepth - rightDepth
    }

    /**
     * Check whether this node has parent

     * @return check result
     */
    fun hasParent(): Boolean {
        return parent != null
    }

    /**
     * Check whether this node has left child

     * @return check result
     */
    fun hasLeft(): Boolean {
        return left != null
    }

    /**
     * Check whether this node has right child

     * @return check result
     */
    fun hasRight(): Boolean {
        return right != null
    }

    /**
     * Check whether this node has sibling node

     * @return check result
     */
    fun hasSibling(): Boolean {
        // Just to avoid unnecessary use of sure (!!) operators
        val localParent = parent

        if (localParent == null) {
            return false
        } else {
            if (this === localParent.left) {
                return localParent.right != null
            } else {
                return localParent.left != null
            }
        }
    }

    /**
     * Get the sibling node

     * @return sibling node
     */
    fun getSibling(): BinaryTreeNode<E>? {
        // Just to avoid unnecessary use of sure (!!) operators
        val localParent = parent

        if (localParent != null) {
            return if (this === localParent.left) localParent.right else localParent.left
        } else {
            return null
        }
    }

    /**
     * Alias of [getElement][.getElement]

     * @return data element
     */
    override fun getValue(): E? {
        return element
    }

    /**
     * Append this node to a parent node

     * @param parent target parent node
     * *
     * @param type type of this node
     */
    fun appendToParent(parent: BinaryTreeNode<E>?, type: Type) {
        this.parent = parent
        if (parent != null) {
            when (type) {
                BinaryTreeNode.Type.LEFT -> parent.left = this
                BinaryTreeNode.Type.RIGHT -> parent.right = this
            }
        }
    }

    /**
     * Alias of [setElement][.setElement]

     * @param e new data element
     */
    override fun setValue(e: E?) {
        this.element = e
    }

    /**
     * Increment the left branch depth by one
     */
    fun incrementLeftDepth() {
        leftDepth++
    }

    /**
     * Adjust the left branch depth

     * @param adjustment adjustment of left branch depth
     */
    fun adjustLeftDepth(adjustment: Int) {
        if (leftDepth + adjustment < 0) {
            leftDepth = 0
        } else {
            leftDepth += adjustment
        }
    }

    /**
     * Decrement the left branch depth by one
     */
    fun decrementLeftDepth() {
        if (leftDepth > 0) {
            leftDepth--
        }
    }

    /**
     * Increment the right branch depth by one
     */
    fun incrementRightDepth() {
        rightDepth++
    }

    /**
     * Adjust the right branch depth

     * @param adjustment adjustment of the right branch depth
     */
    fun adjustRightDepth(adjustment: Int) {
        if (rightDepth + adjustment < 0) {
            rightDepth = 0
        } else {
            rightDepth += adjustment
        }
    }

    /**
     * Decrement the right branch depth by one
     */
    fun decrementRightDepth() {
        if (rightDepth > 0) {
            rightDepth--
        }
    }

    /**
     * Increment the left branch node count by one
     */
    fun incrementLeftNodeCount() {
        leftNodeCount++
    }

    /**
     * Increment the right branch node count by one
     */
    fun incrementRightNodeCount() {
        rightNodeCount++
    }

    /**
     * Decrement the left branch node count by one
     */
    fun decrementLeftNodeCount() {
        if (leftNodeCount > 0) {
            leftNodeCount--
        }
    }

    /**
     * Decrement the right branch node count by one
     */
    fun decrementRightNodeCount() {
        if (rightNodeCount > 0) {
            rightNodeCount--
        }
    }

    /**
     * Adjust node count of the left branch

     * @param adjust node count adjustment
     */
    fun adjustLeftNodeCount(adjust: Int) {
        if (leftNodeCount + adjust < 0) {
            leftNodeCount = 0
        } else {
            leftNodeCount += adjust
        }
    }

    /**
     * Adjust node count of the right branch

     * @param adjust node count adjustment
     */
    fun adjustRightNodeCount(adjust: Int) {
        if (rightNodeCount + adjust < 0) {
            rightNodeCount = 0
        } else {
            rightNodeCount += adjust
        }
    }

    /**
     * Get total descendant node count

     * @return total descendant node count
     */
    fun getNodeCount(): Int {
        return leftNodeCount + rightNodeCount
    }

    /**
     * Check whether this node has child

     * @return check result
     */
    fun hasChild(): Boolean {
        return hasLeft() || hasRight()
    }

    /**
     * Get the representation string of data element
     */
    override fun toString(): String {
        return element?.toString() ?: "null"
    }

    /**
     * Get previous node, i.e. immediate smaller node (inorder tree traversal)

     * @return immediate smaller node
     */
    fun getPreviousNode(): BinaryTreeNode<E>? {
        if (left != null) {
            // Nodes on the left branch always smaller than the parent
            var prev = left

            // finding the largest node of this branch
            while (prev!!.right != null) {
                prev = prev.right
            }

            return prev
        } else if (parent != null) {
            // if no left branch, find the smaller parent, as data node on the right branch always greater
            if (this === parent!!.right) {
                return parent
            } else {
                var prev = parent

                while (prev!!.parent != null) {
                    if (prev === prev.parent!!.right) {
                        return prev.parent
                    } else {
                        prev = prev.parent
                    }
                }
            }
        }

        // Null if no smaller node found
        return null
    }

    /**
     * Get next node, i.e. immediate greater node (inorder tree traversal)

     * @return immediate greater node
     */
    fun getNextNode(): BinaryTreeNode<E>? {
        if (right != null) {
            // Nodes on the right branch always greater
            var next = right

            // finding the smallest node of this greater branch
            while (next!!.left != null) {
                next = next.left
            }

            return next
        } else if (parent != null) {
            // if no right branch find the greater parent, as data node on the left branch always greater
            if (this === parent!!.left) {
                return parent
            } else {
                var next = parent

                while (next!!.parent != null) {
                    if (next === next.parent!!.left) {
                        return next.parent
                    } else {
                        next = next.parent
                    }
                }
            }
        }
        // Null if no greater node found
        return null
    }

    /**
     * Refresh meta datas, i.e. depths, node counts
     */
    fun refreshMetaData() {
        if (left != null) {
            left!!.refreshMetaData()

            leftNodeCount = left!!.getNodeCount() + 1
            leftDepth = left!!.getDepth() + 1
        } else {
            leftNodeCount = 0
            leftDepth = 0
        }
        if (right != null) {
            right!!.refreshMetaData()

            rightNodeCount = right!!.getNodeCount() + 1
            rightDepth = right!!.getDepth() + 1
        } else {
            rightNodeCount = 0
            rightDepth = 0
        }
    }

    /**
     * Get a description string describing this node's structure

     * @return structure description string
     */
    fun getDesc(): String {
        return "parent: $parent, left: $left, right: $right"
    }
}