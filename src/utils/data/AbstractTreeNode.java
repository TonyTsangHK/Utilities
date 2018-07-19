package utils.data;

import java.util.List;

public interface AbstractTreeNode<V> {
    /**
     * Add a child node with specified child index and node data
     * 
     * @param i child index
     * @param node child node
     */
    public void addChildNode(int i, AbstractTreeNode<V> node);
    
    /**
     * Add a child node with specified node data to the last
     * 
     * @param node child node
     */
    public void addChildNode(AbstractTreeNode<V> node);
    
    /**
     * Get a child node with specified child index
     * 
     * @param i child index
     * @return child node of the specified index (null if out of bound)
     */
    public AbstractTreeNode<V> getChildNode(int i);
    
    /**
     * Get data of a child node with specified child index
     * 
     * @param i child index
     * @return data of child node of the specified index (null if out of bound)
     */
    public V getChildNodeData(int i);
    
    /**
     * Get all child nodes
     * 
     * @return list of child nodes
     */
    public List<? extends AbstractTreeNode<V>> getChildNodes();

    /**
     * Get the number of child nodes
     *
     * @return number of child nodes
     */
    public int getChildNodesCount();

    /**
     * Set the data of a child node
     *
     * @param i child node index
     * @param data data to be setted
     * @return status of set data operation
     */
    public boolean setChildNodeData(int i, V data);
    
    /**
     * Get the data of this node
     * 
     * @return data of this node
     */
    public V getData();
    
    /**
     * Get data of parent node
     * 
     * @return data of parent node
     */
    public V getParentData();
    
    /**
     * Get parent node of this node
     * 
     * @return parent node of this node
     */
    public AbstractTreeNode<V> getParentNode();
    
    /**
     * Determine child exist
     * 
     * @return child existence
     */
    public boolean hasChild();
    
    /**
     * Determine is leaf
     * 
     * @return result of determination
     */
    public boolean isLeaf();
    
    /**
     * Remove a child node with specified child index
     * 
     * @param i child index
     * @return removed child node
     */
    public AbstractTreeNode<V> removeChild(int i);
    
    /**
     * Remove a child node with specified child data
     * 
     * @param data child data
     * @return removed child node
     */
    public AbstractTreeNode<V> removeChild(V data);
    
    /**
     * Set child data
     * 
     * @param data child data
     */
    public void setData(V data);
    
    /**
     * Set parent node
     * 
     * @param parentNode parent node
     */
    public void setParent(AbstractTreeNode<V> parentNode);
}
