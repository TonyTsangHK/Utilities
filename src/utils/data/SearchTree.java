package utils.data;

import java.util.ArrayList;

public class SearchTree<V> implements AbstractTree<V> {
    private SearchTreeNode<V> rootNode;
    
    /**
     *  Create a search tree with null valued root node
     */
    public SearchTree() {
        rootNode = new SearchTreeNode<V>(SearchTreeNode.MIN_COST);
    }
    
    /**
     * Create a search tree with specified root node
     * 
     * @param node root node
     */
    public SearchTree(SearchTreeNode<V> node) {
        rootNode = node;
    }
    
    /**
     * Create a search tree with specified root data
     * 
     * @param rootData data of the root node
     */
    public SearchTree(V rootData) {
        rootNode = new SearchTreeNode<V>(rootData, SearchTreeNode.MIN_COST);
    }
    
    /**
     * Level determine helper method
     * 
     * @param node current node
     * @param currentLevel current determined level
     * @return total level of this tree 
     */
    private int findDepth(SearchTreeNode<V> node, int currentLevel) {
        if (node.isLeaf()) {
            return currentLevel;
        } else {
            int maxLevel = currentLevel;
            for (SearchTreeNode<V> n : node.getChildNodes()) {
                int level = findDepth(n, currentLevel+1);
                if (level > maxLevel) {
                    maxLevel = level;
                }
            }
            return maxLevel;
        }
    }
    
    /**
     * Node locator helper method
     * 
     * @param node current node
     * @param data data to be found
     * @return node with specified data (null if not found)
     */
    private SearchTreeNode<V> findTreeNode(SearchTreeNode<V> node, V data) {
        if (node.getData() == data) {
            return node;
        } else {
            if (node.hasChild()) {
                SearchTreeNode<V> resultNode = null;
                for (SearchTreeNode<V> n : node.getChildNodes()) {
                    resultNode = findTreeNode(n, data);
                    if (resultNode != null) {
                        break;
                    }
                }
                return resultNode;
            } else {
                return null;
            }
        }
    }
    
    /**
     * Locator a node with specified data
     * 
     * @param data data to be found
     * @return node with specified data (null if not found)
     */
    @Override
    public SearchTreeNode<V> findTreeNode(V data) {
        if (rootNode == null) {
            return null;
        } else {
            return findTreeNode(rootNode, data);
        }
    }
    
    /**
     * Get all leaf nodes
     * 
     * @return list of leaf nodes
     */
    @Override
    public ArrayList<SearchTreeNode<V>> getAllLeafNode() {
        return getLeaf(rootNode);
    }

    /**
     * Get all leaf nodes in sortedOrder
     *
     * @return sorted list of leaf nodes
     */
    public SortedListAvl<SearchTreeNode<V>> getAllLeafNodeInSortedOrder() {
        return getAllLeafNodeInSortedOrder(rootNode);
    }

    /**
     * Get all leaf nodes in sorted order provided with sorting order and null value interpretation from starting parent node
     *
     * @param node starting parent node
     * @param asc order of the list true as ascending
     * @param nullAsSmaller null value interpretation true inidicating null should be smallest
     * @return sorted list of leaf nodes
     */
    private SortedListAvl<SearchTreeNode<V>> getAllLeafNodeInSortedOrder(SearchTreeNode<V> node) {
        SortedListAvl<SearchTreeNode<V>> list = new SortedListAvl<SearchTreeNode<V>>(node.getNodeComparator());
        if (node.isLeaf()) {
            list.add(node);
        } else {
            for (SearchTreeNode<V> n : node.getChildNodes()) {
                list.addAll(getLeaf(n));
            }
        }
        return list;
    }
    
    /**
     * Get all leaf nodes of the specified node
     * 
     * @param node starting node
     * @return list of leaf nodes of specified node
     */
    private ArrayList<SearchTreeNode<V>> getLeaf(SearchTreeNode<V> node) {
        ArrayList<SearchTreeNode<V>> list = new ArrayList<SearchTreeNode<V>>();
        if (node.isLeaf()) {
            list.add(node);
        } else {
            for (SearchTreeNode<V> n : node.getChildNodes()) {
                list.addAll(getLeaf(n));
            }
        }
        return list;
    }
    
    /**
     * Get root node's data
     * 
     * @return root node's data
     */
    @Override
    public V getRootData() {
        return rootNode.getData();
    }
    
    /**
     * Get root node
     * 
     * @return root node
     */
    @Override
    public AbstractTreeNode<V> getRootNode() {
        return rootNode;
    }
    
    /**
     * Get the depth of this tree
     * 
     * @return depth of this tree
     */
    @Override
    public int getTreeDepth() {
        return findDepth(rootNode, 1);
    }
    
    /**
     * Clear this tree
     */
    @Override
    public void clear() {
        rootNode = new SearchTreeNode<V>(SearchTreeNode.MIN_COST);
    }
    
    /**
     * Get maximum cost leaf node
     * 
     * @return leaf node with maximum cost
     */
    public SearchTreeNode<V> getMaxCostLeaf() {
        if (rootNode.getData() != null) {
            return getMaxCostLeaf(rootNode);
        } else {
            return null;
        }
    }
    
    /**
     * Get get maximum cost leaf node with total cost
     * 
     * @param node Starting node
     * @return leaf node with maximum cost with total cost
     */
    private SearchTreeNode<V> getMaxCostLeaf(SearchTreeNode<V> node) {
        Cost maxCost = null;
        SearchTreeNode<V> maxNode = null;
        if (node.isLeaf()) {
            return node;
        } else {
            for (SearchTreeNode<V> c : node.getChildNodes()) {
                SearchTreeNode<V> n = getMaxCostLeaf(c);
                if (maxCost == null) {
                    maxCost = n.getCost();
                    maxNode = n;
                } else {
                    if (maxCost.compareTo(n.getCost()) < 0) {
                        maxCost = n.getCost();
                        maxNode = n;
                    }
                }
            }
            return maxNode;
        }
    }
    
    /**
     * Get minimum cost leaf node without minCost optimization
     * 
     * @return leaf node with minimum cost
     */
    public SearchTreeNode<V> getMinCostLeaf() {
        if (rootNode != null) {
            return getMinCostLeaf(rootNode);
        } else {
            return null;
        }
    }
    
    /**
     * Get minimum cost leaf node
     * 
     * @param costCanOnlyIncrease Whether the cost can only increase or not
     * @return leaf node with minimum cost
     */
    public SearchTreeNode<V> getMinCostLeaf(boolean costCanOnlyIncrease) {
        if (rootNode.getData() != null) {
            if (costCanOnlyIncrease) {
                return getMinCostLeafWithCostCanOnlyIncrease(rootNode);
            } else {
                return getMinCostLeaf(rootNode);
            }
        } else {
            return null;
        }
    }
    
    /**
     * Get minimum cost leaf node with total cost without minCost optimization
     * 
     * @param node Starting node
     * @return minimum cost leaf node with total cost
     */
    private SearchTreeNode<V> getMinCostLeaf(SearchTreeNode<V> node) {
        Cost minCost = null;
        SearchTreeNode<V> minNode = null;
        if (node.isLeaf()) {
            return node;
        } else {
            for (SearchTreeNode<V> c : node.getChildNodes()) {
                SearchTreeNode<V> n = getMinCostLeaf(c);
                if (minCost == null) {
                    minCost = n.getCost();
                    minNode = n;
                } else {
                    if (minCost.compareTo(n.getCost()) > 0) {
                        minCost = n.getCost();
                        minNode = n;
                    }
                }
            }
            return minNode;
        }
    }
    
    /**
     * Get minimum cost leaf node with total cost with minCost optimization
     * 
     * @param node Starting node
     * @return minimum cost leaf node with total cost
     */
    private SearchTreeNode<V> getMinCostLeafWithCostCanOnlyIncrease(SearchTreeNode<V> node) {
        if (node.isLeaf()) {
            return node;
        } else {
            SortedListAvl<SearchTreeNode<V>> childNodes = node.getChildNodes();
            Cost allowedCost = null;
            SearchTreeNode<V> minNode = null;
            for (int i = 0; i < childNodes.size(); i++) {
                SearchTreeNode<V> c = childNodes.get(i);
                if (i == 0) {
                    allowedCost = c.getCost();
                } else {
                    if (allowedCost.compareTo(c.getCost()) < 0) {
                        break;
                    }
                }
                SearchTreeNode<V> n = getMinCostLeafWithCostCanOnlyIncrease(c);
                if (minNode == null) {
                    minNode = n;
                } else if (minNode.getCost().compareTo(n.getCost()) > 0) {
                    minNode = n;
                }
            }
            return minNode;
        }
    }
}
