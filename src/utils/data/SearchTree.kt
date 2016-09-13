package utils.data

import java.util.ArrayList

class SearchTree<V> : AbstractTree<V> {
    private var rootNode: SearchTreeNode<V?>

    /**
     * Create a search tree with null valued root node
     */
    constructor() {
        rootNode = SearchTreeNode<V?>(SearchTreeNode.MIN_COST)
    }

    /**
     * Create a search tree with specified root node

     * @param node root node
     */
    constructor(node: SearchTreeNode<V?>) {
        rootNode = node
    }

    /**
     * Create a search tree with specified root data

     * @param rootData data of the root node
     */
    constructor(rootData: V) {
        rootNode = SearchTreeNode(rootData, SearchTreeNode.MIN_COST)
    }

    /**
     * Level determine helper method

     * @param node current node
     * *
     * @param currentLevel current determined level
     * *
     * @return total level of this tree
     */
    private fun findDepth(node: SearchTreeNode<V?>, currentLevel: Int): Int {
        if (node.isLeaf) {
            return currentLevel
        } else {
            var maxLevel = currentLevel
            for (n in node.childNodes) {
                val level = findDepth(n!!, currentLevel + 1)
                if (level > maxLevel) {
                    maxLevel = level
                }
            }
            return maxLevel
        }
    }

    /**
     * Node locator helper method

     * @param node current node
     * *
     * @param data data to be found
     * *
     * @return node with specified data (null if not found)
     */
    private fun findTreeNode(node: SearchTreeNode<V?>, data: V): SearchTreeNode<V?>? {
        if (node.data === data) {
            return node
        } else {
            if (node.hasChild()) {
                var resultNode: SearchTreeNode<V?>? = null
                for (n in node.childNodes) {
                    resultNode = findTreeNode(n!!, data)
                    if (resultNode != null) {
                        break
                    }
                }
                return resultNode
            } else {
                return null
            }
        }
    }

    /**
     * Locator a node with specified data

     * @param data data to be found
     * *
     * @return node with specified data (null if not found)
     */
    override fun findTreeNode(data: V): SearchTreeNode<V?>? {
        return findTreeNode(rootNode, data)
    }

    /**
     * Get all leaf nodes

     * @return list of leaf nodes
     */
    override fun getAllLeafNode(): ArrayList<SearchTreeNode<V?>>? {
        return getLeaf(rootNode)
    }

    /**
     * Get all leaf nodes in sortedOrder

     * @return sorted list of leaf nodes
     */
    val allLeafNodeInSortedOrder: SortedListAvl<SearchTreeNode<V?>>?
        get() = getAllLeafNodeInSortedOrder(rootNode)

    /**
     * Get all leaf nodes in sorted order provided with sorting order and null value interpretation from starting parent node

     * @param node starting parent node
     * *
     * @param asc order of the list true as ascending
     * *
     * @param nullAsSmaller null value interpretation true inidicating null should be smallest
     * *
     * @return sorted list of leaf nodes
     */
    private fun getAllLeafNodeInSortedOrder(node: SearchTreeNode<V?>): SortedListAvl<SearchTreeNode<V?>> {
        val list = SortedListAvl<SearchTreeNode<V?>>(node.getNodeComparator())
        if (node.isLeaf) {
            list.add(node)
        } else {
            for (n in node.childNodes) {
                list.addAll(getLeaf(n!!))
            }
        }
        return list
    }

    /**
     * Get all leaf nodes of the specified node

     * @param node starting node
     * *
     * @return list of leaf nodes of specified node
     */
    private fun getLeaf(node: SearchTreeNode<V?>): ArrayList<SearchTreeNode<V?>> {
        val list = ArrayList<SearchTreeNode<V?>>()
        if (node.isLeaf) {
            list.add(node)
        } else {
            for (n in node.childNodes) {
                list.addAll(getLeaf(n!!))
            }
        }
        return list
    }

    /**
     * Get root node's data

     * @return root node's data
     */
    override fun getRootData(): V? {
        return rootNode.data
    }

    /**
     * Get root node

     * @return root node
     */
    override fun getRootNode(): AbstractTreeNode<V?>? {
        return rootNode
    }

    /**
     * Get the depth of this tree

     * @return depth of this tree
     */
    override fun getTreeDepth(): Int {
        return findDepth(rootNode, 1)
    }

    /**
     * Clear this tree
     */
    override fun clear() {
        rootNode = SearchTreeNode<V?>(SearchTreeNode.MIN_COST)
    }

    /**
     * Get maximum cost leaf node

     * @return leaf node with maximum cost
     */
    val maxCostLeaf: SearchTreeNode<V?>?
        get() {
            if (rootNode.data != null) {
                return getMaxCostLeaf(rootNode)
            } else {
                return null
            }
        }

    /**
     * Get get maximum cost leaf node with total cost

     * @param node Starting node
     * *
     * @return leaf node with maximum cost with total cost
     */
    private fun getMaxCostLeaf(node: SearchTreeNode<V?>): SearchTreeNode<V?> {
        var maxCost: Cost? = null
        var maxNode: SearchTreeNode<V?>? = null
        if (node.isLeaf) {
            return node
        } else {
            for (c in node.childNodes) {
                val n = getMaxCostLeaf(c!!)
                if (maxCost == null) {
                    maxCost = n.cost
                    maxNode = n
                } else {
                    if (maxCost.compareTo(n.cost) < 0) {
                        maxCost = n.cost
                        maxNode = n
                    }
                }
            }
            return maxNode!!
        }
    }

    /**
     * Get minimum cost leaf node without minCost optimization

     * @return leaf node with minimum cost
     */
    val minCostLeaf: SearchTreeNode<V?>?
        get() = getMinCostLeaf(rootNode)

    /**
     * Get minimum cost leaf node

     * @param costCanOnlyIncrease Whether the cost can only increase or not
     * *
     * @return leaf node with minimum cost
     */
    fun getMinCostLeaf(costCanOnlyIncrease: Boolean): SearchTreeNode<V?>? {
        if (rootNode.data != null) {
            if (costCanOnlyIncrease) {
                return getMinCostLeafWithCostCanOnlyIncrease(rootNode)
            } else {
                return getMinCostLeaf(rootNode)
            }
        } else {
            return null
        }
    }

    /**
     * Get minimum cost leaf node with total cost without minCost optimization

     * @param node Starting node
     * *
     * @return minimum cost leaf node with total cost
     */
    private fun getMinCostLeaf(node: SearchTreeNode<V?>): SearchTreeNode<V?> {
        var minCost: Cost? = null
        var minNode: SearchTreeNode<V?>? = null
        if (node.isLeaf) {
            return node
        } else {
            for (c in node.childNodes) {
                val n = getMinCostLeaf(c!!)
                if (minCost == null) {
                    minCost = n.cost
                    minNode = n
                } else {
                    if (minCost.compareTo(n.cost) > 0) {
                        minCost = n.cost
                        minNode = n
                    }
                }
            }
            return minNode!!
        }
    }

    /**
     * Get minimum cost leaf node with total cost with minCost optimization

     * @param node Starting node
     * *
     * @return minimum cost leaf node with total cost
     */
    private fun getMinCostLeafWithCostCanOnlyIncrease(node: SearchTreeNode<V?>): SearchTreeNode<V?> {
        if (node.isLeaf) {
            return node
        } else {
            val childNodes = node.childNodes
            var allowedCost: Cost? = null
            var minNode: SearchTreeNode<V?>? = null
            for (i in childNodes.indices) {
                val c = childNodes[i]
                if (i == 0) {
                    allowedCost = c!!.cost
                } else {
                    if (allowedCost!!.compareTo(c!!.cost) < 0) {
                        break
                    }
                }
                val n = getMinCostLeafWithCostCanOnlyIncrease(c)
                if (minNode == null) {
                    minNode = n
                } else if (minNode.cost.compareTo(n.cost) > 0) {
                    minNode = n
                }
            }
            return minNode!!
        }
    }
}
