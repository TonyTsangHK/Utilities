package utils.data;

import java.util.List;

public interface AbstractTree<V> {
    /**
     * Locator a node with specified data
     * 
     * @param data data to be found
     * @return node with specified data (null if not found)
     */
    public AbstractTreeNode<V> findTreeNode(V data);
    
    /**
     * Get all leaf nodes
     * 
     * @return list of leaf nodes
     */
    public List<? extends AbstractTreeNode<V>> getAllLeafNode();
    
    /**
     * Get root node's data
     * 
     * @return root node's data
     */
    public V getRootData();
    
    /**
     * Get root node
     * 
     * @return root node
     */
    public AbstractTreeNode<V> getRootNode();
    
    /**
     * Get the depth of this tree
     * 
     * @return depth of this tree
     */
    public int getTreeDepth();
    
    /**
     * Clear this tree
     */
    public void clear();
}
