package utils.data;

import java.util.Comparator;
import java.util.ListIterator;

public class SearchTreeNode<V> implements AbstractTreeNode<V> {
    public static final Cost MIN_COST = new Cost() {
        @Override
        public Cost add(Cost cost) {
            return cost.clone();
        }

        @Override
        public Cost minus(Cost cost) {
            return cost.clone();
        }

        @Override
        public int compareTo(Cost o) {
            if (o == this) {
                return 0;
            } else {
                return -1;
            }
        }
        
        @Override
        public Cost clone() {
            return MIN_COST;
        }
    };
    
    private SearchTreeNode<V> parentNode;
    private SortedListAvl<SearchTreeNode<V>> childNodes;
    private V data;
    private Cost cost;
    
    private final Comparator<SearchTreeNode<V>> nodeComparator = new Comparator<SearchTreeNode<V>>() {
        @Override
        public int compare(SearchTreeNode<V> n1, SearchTreeNode<V> n2) {
            if (n1 != null && n2 != null) {
                return n1.getCost().compareTo(n2.getCost());
            } else {
                return DataComparator.compareNull(n1, n2);
            }
        }
    };
    
    /**
     * Create a null valued tree node
     * 
     * @param cost Cost of this node
     */
    public SearchTreeNode(Cost cost) {
        setData(null);
        childNodes = new SortedListAvl<SearchTreeNode<V>>(nodeComparator);
        setCost(cost);
    }
    
    /**
     * Create a tree node with specified node data
     * 
     * @param data node data
     * @param cost Cost of this node
     */
    public SearchTreeNode(V data, Cost cost) {
        setData(data);
        childNodes = new SortedListAvl<SearchTreeNode<V>>(nodeComparator);
        setCost(cost);
    }

    public Comparator<SearchTreeNode<V>> getNodeComparator() {
        return nodeComparator;
    }
    
    /**
     * Set the cost of this node
     * 
     * @param cost Cost of this node
     */
    public void setCost(Cost cost) {
        this.cost = cost;
    }
    
    /**
     * Get the cost of this node
     * 
     * @return Cost of this node
     */
    public Cost getCost() {
        return cost;
    }
    
    /**
     * Add a child node with specified child index and node data
     * 
     * @param i child index
     * @param node Child node
     */
    @Override
    public void addChildNode(int i, AbstractTreeNode<V> node) {
        node.setParent(this);
        SearchTreeNode<V> n = (SearchTreeNode<V>) node;
        n.setCost(getCost().add(n.getCost()));
        childNodes.add(i, n);
    }
    
    /**
     * Add a child node with specified child index and node data
     * 
     * @param i child index
     * @param data node data
     * @param cost Cost of this node
     * @return the newly created child node
     */
    public SearchTreeNode<V> addChild(int i, V data, Cost cost) {
        SearchTreeNode<V> node = new SearchTreeNode<V>(data, getCost().add(cost));
        node.setParent(this);
        childNodes.add(i, node);
        return node;
    }
    
    /**
     * Add a child node with specified node data to the last
     * 
     * @param node Child node
     */
    @Override
    public void addChildNode(AbstractTreeNode<V> node) {
        node.setParent(this);
        SearchTreeNode<V> n = (SearchTreeNode<V>) node;
        n.setCost(getCost().add(n.getCost()));
        childNodes.add(n);
    }
    
    /**
     * Add a child node with specified node data to the last
     * 
     * @param data node data
     * @param cost Cost of this node
     * @return the newly created child node
     */
    public SearchTreeNode<V> addChildData(V data, Cost cost) {
        SearchTreeNode<V> node = new SearchTreeNode<V>(data, getCost().add(cost));
        node.setParent(this);
        childNodes.add(node);
        return node;
    }
    
    /**
     * Get a child node with specified child index
     * 
     * @param i child index
     * @return child node of the specified index (null if out of bound)
     */
    @Override
    public SearchTreeNode<V> getChildNode(int i) {
        if (i >= 0 && i < childNodes.size()) {
            return childNodes.get(i);
        } else {
            return null;
        }
    }
    
    /**
     * Get data of a child node with specified child index
     * 
     * @param i child index
     * @return data of child node of the specified index (null if out of bound)
     */
    @Override
    public V getChildNodeData(int i) {
        SearchTreeNode<V> node = getChildNode(i);
        return (node != null)? node.getData() : null;
    }
    
    /**
     * Get all child nodes
     * 
     * @return list of child nodes
     */
    @Override
    public SortedListAvl<SearchTreeNode<V>> getChildNodes() {
        return childNodes;
    }

    /**
     * Get the number of child nodes
     *
     * @return number of child nodes
     */
    @Override
    public int getChildNodesCount() {
        return childNodes.size();
    }
    
    /**
     * Get the data of this node
     * 
     * @return data of this node
     */
    @Override
    public V getData() {
        return data;
    }
    
    /**
     * Get data of parent node
     * 
     * @return data of parent node
     */
    @Override
    public V getParentData() {
        return parentNode.getData();
    }
    
    /**
     * Get parent node of this node
     * 
     * @return parent node of this node
     */
    @Override
    public SearchTreeNode<V> getParentNode() {
        return parentNode;
    }
    
    /**
     * Determine child exist
     * 
     * @return child existence
     */
    @Override
    public boolean hasChild() {
        return !childNodes.isEmpty();
    }
    
    /**
     * Determine is leaf
     * 
     * @return result of determination
     */
    @Override
    public boolean isLeaf() {
        return childNodes.isEmpty();
    }
    
    /**
     * Alias of removeChildAt, it is better to use removeChildAt instead of this method
     *
     * @param i child index
     * @return removed child node
     */
    @Override
    public SearchTreeNode<V> removeChild(int i) {
        return removeChildAt(i);
    }

    /**
     * Remove a child node with specified child index
     *
     * @param i child index
     * @return removed child node
     */
    public SearchTreeNode<V> removeChildAt(int i) {
        if (i >= 0 && i < childNodes.size()) {
            return childNodes.removeAt(i);
        } else {
            return null;
        }
    }
    
    /**
     * Remove a child node with specified child data
     * 
     * @param data child data
     * @return removed child node
     */
    @Override
    public SearchTreeNode<V> removeChild(V data) {
        ListIterator<SearchTreeNode<V>> iter = childNodes.listIterator();
        SearchTreeNode<V> nodeToReturn = null;
        while (iter.hasNext()) {
            SearchTreeNode<V> node = iter.next();
            if (node.data == data) {
                iter.remove();
                nodeToReturn = node;
                break;
            }
        }
        return nodeToReturn;
    }
    
    /**
     * Set child data
     * 
     * @param data child data
     */
    @Override
    public void setData(V data) {
        this.data = data;
    }

    /**
     * Set a child node's data
     *
     * @param i Index of the child node
     * @param data The data to be setted
     * @return Status of set data operation
     */
    @Override
    public boolean setChildNodeData(int i, V data) {
        if (i >= 0 && i < childNodes.size()) {
            childNodes.get(i).setData(data);
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Set parent node
     * 
     * @param parentNode parent node
     */
    @Override
    public void setParent(AbstractTreeNode<V> parentNode) {
        this.parentNode = (SearchTreeNode<V>)parentNode;
    }
}
