package utils.data

import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-13
 * Time: 15:22
 */
class SearchTreeNode<V>: AbstractTreeNode<V> {
    companion object {
        // Absolute minimum cost
        @JvmField
        val MIN_COST: Cost = object: Cost {
            override fun plus(cost: Cost): Cost {
                return cost.clone()
            }

            override fun minus(cost: Cost): Cost {
                return cost.clone()
            }

            override fun compareTo(other: Cost): Int {
                return if (other === this) 0 else -1
            }
            
            override fun clone(): Cost {
                return this
            }
        }
    }

    private var parentNode: SearchTreeNode<V>?
    private val childNodes: SortedListAvl<SearchTreeNode<V>>
    
    private var localData: V?
    
    var cost: Cost

    private val nodeComparator = object: Comparator<SearchTreeNode<V>> {
        override fun compare(n1: SearchTreeNode<V>?, n2: SearchTreeNode<V>?): Int {
            if (n1 != null && n2 != null) {
                return n1.cost.compareTo(n2.cost)
            } else {
                return DataComparator.compareNull(n1, n2)
            }
        }
    }
    
    override fun getData(): V? {
        return this.localData
    }
    
    override fun setData(value: V?) {
        this.localData = value
    }

    /**
     * Create a null valued tree node
     *
     * @param cost Cost of this node
     */
    constructor(cost: Cost): this(null, cost)

    /**
     * Create a tree node with specified node data
     *
     * @param data node data
     * @param cost Cost of this node
     */
    constructor(data: V?, cost: Cost) {
        this.parentNode = null
        this.localData = data
        this.childNodes = SortedListAvl<SearchTreeNode<V>>(nodeComparator)
        this.cost = cost
    }

    fun getNodeComparator(): Comparator<SearchTreeNode<V>> {
        return nodeComparator
    }

    /**
     * Add a child node with specified child index and node data
     *
     * @param i child index
     * @param node Child node
     */
    override fun addChildNode(i: Int, node: AbstractTreeNode<V>) {
        node.setParent(this)
        val n = node as SearchTreeNode<V>
        n.cost = this.cost + n.cost
        childNodes.add(i, n)
    }

    /**
     * Add a child node with specified child index and node data
     *
     * @param i child index
     * @param data node data
     * @param cost Cost of this node
     * @return the newly created child node
     */
    fun addChild(i: Int, data: V, cost: Cost): SearchTreeNode<V> {
        val node = SearchTreeNode<V>(data, this.cost + cost)
        node.setParent(this)
        childNodes.add(i, node)
        return node
    }

    /**
     * Add a child node with specified node data to the last
     *
     * @param node Child node
     */
    override fun addChildNode(node: AbstractTreeNode<V>) {
        node.setParent(this)
        val n = node as SearchTreeNode<V>
        n.cost = this.cost + n.cost
        childNodes.add(n)
    }

    /**
     * Add a child node with specified node data to the last
     *
     * @param data node data
     * @param cost Cost of this node
     * @return the newly created child node
     */
    fun addChildData(data: V, cost: Cost): SearchTreeNode<V> {
        val node = SearchTreeNode<V>(data, this.cost + cost)
        node.setParent(this)
        childNodes.add(node)
        return node
    }

    /**
     * Get a child node with specified child index
     *
     * @param i child index
     * @return child node of the specified index (null if out of bound)
     */
    override fun getChildNode(i: Int): SearchTreeNode<V>? {
        if (i >= 0 && i < childNodes.size) {
            return childNodes[i]
        } else {
            return null
        }
    }

    /**
     * Get data of a child node with specified child index
     *
     * @param i child index
     * @return data of child node of the specified index (null if out of bound)
     */
    override fun getChildNodeData(i: Int): V? {
        return getChildNode(i)?.data
    }

    /**
     * Get all child nodes
     *
     * @return list of child nodes
     */
    override fun getChildNodes(): SortedListAvl<SearchTreeNode<V>> {
        return childNodes
    }

    /**
     * Get the number of child nodes
     *
     * @return number of child nodes
     */
    override fun getChildNodesCount(): Int {
        return childNodes.size
    }

    /**
     * Get data of parent node
     *
     * @return data of parent node
     */
    override fun getParentData(): V? {
        return parentNode?.data
    }

    /**
     * Get parent node of this node
     *
     * @return parent node of this node
     */
    override fun getParentNode(): SearchTreeNode<V>? {
        return parentNode
    }

    /**
     * Determine child exist
     *
     * @return child existence
     */
    override fun hasChild(): Boolean {
        return !childNodes.isEmpty()
    }

    /**
     * Determine is leaf
     *
     * @return result of determination
     */
    override fun isLeaf(): Boolean {
        return childNodes.isEmpty()
    }

    /**
     * Alias of removeChildAt, it is better to use removeChildAt instead of this method
     *
     * @param i child index
     * @return removed child node
     */
    override fun removeChild(i: Int): SearchTreeNode<V>? {
        return removeChildAt(i)
    }

    /**
     * Remove a child node with specified child index
     *
     * @param i child index
     * @return removed child node
     */
    fun removeChildAt(i: Int): SearchTreeNode<V>? {
        return if (i >= 0 && i < childNodes.size) childNodes.removeAt(i) else null
    }

    /**
     * Remove a child node with specified child data
     *
     * @param data child data
     * @return removed child node
     */
    override fun removeChild(data: V?): SearchTreeNode<V>? {
        val iter = childNodes.listIterator()
        var nodeToReturn: SearchTreeNode<V>? = null
        while (iter.hasNext()) {
            val node = iter.next()
            if (node.data == data) {
                iter.remove()
                nodeToReturn = node
                break
            }
        }
        return nodeToReturn
    }

    /**
     * Set a child node's data
     *
     * @param i Index of the child node
     * @param data The data to be setted
     * @return Status of set data operation
     */
    override fun setChildNodeData(i: Int, data: V?): Boolean {
        if (i >= 0 && i < childNodes.size) {
            childNodes[i]?.setData(data)
            return true
        } else {
            return false
        }
    }

    /**
     * Set parent node
     *
     * @param parentNode parent node
     */
    override fun setParent(parentNode: AbstractTreeNode<V>) {
        this.parentNode = parentNode as SearchTreeNode<V>
    }
}