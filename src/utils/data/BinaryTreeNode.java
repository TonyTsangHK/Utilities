package utils.data;

/**
 * Binary tree node, that linked with its left and right child together with its parent
 *
 * @param <E> Generic type of data element
 */
public class BinaryTreeNode<E> implements ValueHolder<E> {
    /**
     * Node type
     * 
     */
    public enum Type {
        /**
         * Left node type
         */
        LEFT("left"),
        /**
         * Right node type
         */
        RIGHT("right");
        
        /**
         * String represent of this node type
         */
        public final String desc;
        
        /**
         * Private constructor
         * 
         * @param desc string representation of this note type
         */
        private Type(String desc) {
            this.desc = desc;
        }
        
        /**
         * Get a string representation of this node type
         */
        @Override
        public String toString() {
            return desc;
        }
    }
    
    /**
     * Left child node
     */
    private BinaryTreeNode<E> left;
    /**
     * Right child node
     */
    private BinaryTreeNode<E> right;
    /**
     * Parent node
     */
    private BinaryTreeNode<E> parent;
    
    /**
     * Depth of left descendants
     */
    private int leftDepth;
    /**
     * Depth of right descendants
     */
    private int rightDepth;
    /**
     * Left node counts
     */
    private int leftNodeCount;
    /**
     * Right node counts
     */
    private int rightNodeCount;
    
    /**
     * Contained data element
     */
    private E element;
    
    /**
     * Construct a Binary tree node with contained data element
     * 
     * @param e contained data element
     */
    public BinaryTreeNode(E e) {
        this(e, null, null, null);
    }
    
    /**
     * Construct a Binary tree node with contained data element and its parent node
     * 
     * @param e data element
     * @param parent parent node
     */
    public BinaryTreeNode(E e, BinaryTreeNode<E> parent) {
        this(e, parent, null, null);
    }
    
    /**
     * Construct a Binary tree node with contained data element and its left & right child nodes
     * 
     * @param e data element
     * @param left left child node
     * @param right right child node
     */
    public BinaryTreeNode(E e, BinaryTreeNode<E> left, BinaryTreeNode<E> right) {
        this(e, null, left, right);
    }
    
    /**
     * Construct a Binary tree node with contained data element and left & right child nodes and its parent node
     * 
     * @param e data element
     * @param parent parent node
     * @param left left child node
     * @param right right child node
     */
    public BinaryTreeNode(E e, BinaryTreeNode<E> parent, BinaryTreeNode<E> left, BinaryTreeNode<E> right) {
        setParent(parent);
        setLeft (left);
        setRight(right);
        setElement(e);
        
        leftDepth  = 0;
        rightDepth = 0;
        
        leftNodeCount  = 0;
        rightNodeCount = 0;
    }
    
    /**
     * Get left descendants depth
     * 
     * @return depth of left child branch
     */
    public int getLeftDepth() {
        return leftDepth;
    }
    
    /**
     * Get right descendants depth
     * 
     * @return depth of right child branch
     */
    public int getRightDepth() {
        return rightDepth;
    }
    
    /**
     * Get depth of descendants
     * 
     * @return depth of descendants
     */
    public int getDepth() {
        return (leftDepth > rightDepth)? leftDepth : rightDepth;
    }
    
    /**
     * Get balance factor
     * 
     * @return balance factor, i.e. difference of left & right branch depth
     */
    public int getBalanceFactor() {
        return leftDepth - rightDepth;
    }
    
    /**
     * Check whether this node has parent
     * 
     * @return check result
     */
    public boolean hasParent() {
        return parent != null;
    }
    
    /**
     * Get the parent node
     * 
     * @return parent node
     */
    public BinaryTreeNode<E> getParent() {
        return parent;
    }
    
    /**
     * Check whether this node has left child
     * 
     * @return check result
     */
    public boolean hasLeft() {
        return left != null;
    }
    
    /**
     * Get the left child node
     * 
     * @return left child node
     */
    public BinaryTreeNode<E> getLeft() {
        return left;
    }
    
    /**
     * Check whether this node has right child
     * 
     * @return check result
     */
    public boolean hasRight() {
        return right != null;
    }
    
    /**
     * Get the right child node
     * 
     * @return right child node
     */
    public BinaryTreeNode<E> getRight() {
        return right;
    }
    
    /**
     * Check whether this node has sibling node
     * 
     * @return check result
     */
    public boolean hasSibling() {
        if (parent == null) {
            return false;
        } else {
            if (this == parent.left) {
                return parent.right != null;
            } else {
                return parent.left != null;
            }
        }
    }
    
    /**
     * Get the sibling node
     * 
     * @return sibling node
     */
    public BinaryTreeNode<E> getSibling() {
        if (parent != null) {
            return (this == parent.left)? parent.right : parent.left;
        } else {
            return null;
        }
    }
    
    /**
     * Get the contained data element
     * 
     * @return data element
     */
    public E getElement() {
        return element;
    }

    /**
     * Alias of {@link #getElement() getElement}
     *
     * @return data element
     */
    public E getValue() {
        return getElement();
    }
    
    /**
     * Set the parent node
     * 
     * @param parent new parent node
     */
    public void setParent(BinaryTreeNode<E> parent) {
        this.parent = parent;
    }
    
    /**
     * Append this node to a parent node
     * 
     * @param parent target parent node
     * @param type type of this node
     */
    public void appendToParent(BinaryTreeNode<E> parent, Type type) {
        this.parent = parent;
        if (parent != null) {
            switch (type) {
                case LEFT:
                    parent.left = this;
                    break;
                case RIGHT:
                    parent.right = this;
            }
        }
    }
    
    /**
     * Set left child node
     * 
     * @param l new left child node
     */
    public void setLeft(BinaryTreeNode<E> l) {
        setLeft(l, false);
    }
    
    /**
     * Set left child node with option to keeping consistency, i.e. set child node's parent to this node 
     * 
     * @param l new left child node
     * @param keepConsistency keep consistency or not
     */
    public void setLeft(BinaryTreeNode<E> l, boolean keepConsistency) {
        left = l;
        if (keepConsistency && l != null && l.parent != this) {
            l.parent = this;
        }
    }
    
    /**
     * Set right child node
     * 
     * @param r new right child node
     */
    public void setRight(BinaryTreeNode<E> r) {
        setRight(r, false);
    }
    
    /**
     * Set right child node with option to keeping consistency, i.e. set child node's parent to this node
     * 
     * @param r new right child node
     * @param keepConsistency keep consistency or not
     */
    public void setRight(BinaryTreeNode<E> r, boolean keepConsistency) {
        right = r;
        if (keepConsistency && r != null && r.parent != this) {
            r.parent = this;
        }
    }
    
    /**
     * Set data element
     * 
     * @param e new data element
     */
    public void setElement(E e) {
        element = e;
    }

    /**
     * Alias of {@link #setElement(E) setElement}
     *
     * @param e new data element
     */
    public void setValue(E e) {
        this.setElement(e);
    }
    
    /**
     * Increment the left branch depth by one
     */
    public void incrementLeftDepth() {
        leftDepth++;
    }
    
    /**
     * Adjust the left branch depth
     * 
     * @param adjustment adjustment of left branch depth
     */
    public void adjustLeftDepth(int adjustment) {
        if (leftDepth + adjustment < 0) {
            leftDepth = 0;
        } else {
            leftDepth += adjustment;
        }
    }
    
    /**
     * Decrement the left branch depth by one
     */
    public void decrementLeftDepth() {
        if (leftDepth > 0) {
            leftDepth--;
        }
    }
    
    /**
     * Increment the right branch depth by one
     */
    public void incrementRightDepth() {
        rightDepth++;
    }
    
    /**
     * Adjust the right branch depth
     * 
     * @param adjustment adjustment of the right branch depth
     */
    public void adjustRightDepth(int adjustment) {
        if (rightDepth + adjustment < 0) {
            rightDepth = 0;
        } else {
            rightDepth += adjustment;
        }
    }
    
    /**
     * Decrement the right branch depth by one
     */
    public void decrementRightDepth() {
        if (rightDepth > 0) {
            rightDepth--;
        }
    }
    
    /**
     * Set the left branch depth
     * 
     * @param depth left branch depth
     */
    public void setLeftDepth(int depth) {
        if (depth >= 0) {
            leftDepth = depth;
        }
    }
    
    /**
     * Set the right branch depth
     * 
     * @param depth right branch depth
     */
    public void setRightDepth(int depth) {
        if (depth >= 0) {
            rightDepth = depth;
        }
    }
    
    /**
     * Set node count of the left branch
     * 
     * @param nodeCount node count of left branch
     */
    public void setLeftNodeCount(int nodeCount) {
        if (nodeCount >= 0) {
            leftNodeCount = nodeCount;
        }
    }
    
    /**
     * Set node count of the right branch
     * 
     * @param nodeCount node count of right branch
     */
    public void setRightNodeCount(int nodeCount) {
        if (nodeCount >= 0) {
            rightNodeCount = nodeCount;
        }
    }
    
    /**
     * Increment the left branch node count by one
     */
    public void incrementLeftNodeCount() {
        leftNodeCount++;
    }
    
    /**
     * Increment the right branch node count by one
     */
    public void incrementRightNodeCount() {
        rightNodeCount++;
    }
    
    /**
     * Decrement the left branch node count by one
     */
    public void decrementLeftNodeCount() {
        if (leftNodeCount > 0) {
            leftNodeCount--;
        }
    }
    
    /**
     * Decrement the right branch node count by one
     */
    public void decrementRightNodeCount() {
        if (rightNodeCount > 0) {
            rightNodeCount--;
        }
    }
    
    /**
     * Adjust node count of the left branch
     * 
     * @param adjust node count adjustment
     */
    public void adjustLeftNodeCount(int adjust) {
        if (leftNodeCount + adjust < 0) {
            leftNodeCount = 0;
        } else {
            leftNodeCount += adjust;
        }
    }
    
    /**
     * Adjust node count of the right branch
     * 
     * @param adjust node count adjustment
     */
    public void adjustRightNodeCount(int adjust) {
        if (rightNodeCount + adjust < 0) {
            rightNodeCount = 0;
        } else {
            rightNodeCount += adjust;
        }
    }
    
    /**
     * Get the left branch node count
     * 
     * @return node count of left branch
     */
    public int getLeftNodeCount() {
        return leftNodeCount;
    }
    
    /**
     * Get the right branch node count
     * 
     * @return node count of right branch
     */
    public int getRightNodeCount() {
        return rightNodeCount;
    }
    
    /**
     * Get total descendant node count
     * 
     * @return total descendant node count
     */
    public int getNodeCount() {
        return leftNodeCount + rightNodeCount;
    }
    
    /**
     * Check whether this node has child
     * 
     * @return check result
     */
    public boolean hasChild() {
        return hasLeft() || hasRight();
    }
    
    /**
     * Get the representation string of data element
     */
    @Override
    public String toString() {
        return (element != null)? element.toString() : "null";
    }
    
    /**
     * Get previous node, i.e. immediate smaller node (inorder tree traversal)
     * 
     * @return immediate smaller node
     */
    public BinaryTreeNode<E> getPreviousNode() {
        if (left != null) {
            // Nodes on the left branch always smaller
            BinaryTreeNode<E> prev = left;
            
            // finding the largest node of this smaller branch
            while (prev.right != null) {
                prev = prev.right;
            }
            
            return prev;
        } else if (parent != null) {
            // if no left branch, find the smaller parent, as data node on the right branch always greater
            if (this == parent.right) {
                return parent;
            } else {
                BinaryTreeNode<E> prev = parent;
                
                while (prev.parent != null) {
                    if (prev == prev.parent.right) {
                        return prev.parent;
                    } else {
                        prev = prev.parent;
                    }
                }
            }
        }
        
        // Null if no smaller node found
        return null;
    }
    
    /**
     * Get next node, i.e. immediate greater node (inorder tree traversal)
     * 
     * @return immediate greater node
     */
    public BinaryTreeNode<E> getNextNode() {
        if (right != null) {
            // Nodes on the right branch always greater
            BinaryTreeNode<E> next = right;
            
            // finding the smallest node of this greater branch
            while (next.left != null) {
                next = next.left;
            }
            
            return next;
        } else if (parent != null){
            // if no right branch find the greater parent, as data node on the left branch always greater
            if (this == parent.left) {
                return parent;
            } else {
                BinaryTreeNode<E> next = parent;
                
                while (next.parent != null) {
                    if (next == next.parent.left) {
                        return next.parent;
                    } else {
                        next = next.parent;
                    }
                }
            }
        }
        // Null if no greater node found
        return null;
    }
    
    /**
     * Refresh meta datas, i.e. depths, node counts
     */
    public void refreshMetaData() {
        if (left != null) {
            left.refreshMetaData();
            
            leftNodeCount = left.getNodeCount() + 1;
            leftDepth     = left.getDepth() + 1;
        } else {
            leftNodeCount = 0;
            leftDepth     = 0;
        }
        if (right != null) {
            right.refreshMetaData();
            
            rightNodeCount = right.getNodeCount() + 1;
            rightDepth     = right.getDepth() + 1;
        } else {
            rightNodeCount = 0;
            rightDepth     = 0;
        }
    }
    
    /**
     * Get a description string describing this node's structure
     * 
     * @return structure description string
     */
    public String getDesc() {
        return "parent: " + parent + ", left: " + left + ", right: " + right;
    }
}
