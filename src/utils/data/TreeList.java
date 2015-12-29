package utils.data;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;

/**
 * Tree implement as java list
 * 
 * @author Tony Tsang
 *
 * @param <E> Data generic type
 */
@SuppressWarnings("serial")
public class TreeList<E> extends AbstractList<E> implements Iterable<E>, List<E>, Cloneable, Serializable {
    /**
     * Root node
     */
    private BinaryTreeNode<E> root;
    
    /**
     * List size
     */
    private int size;
    
    /**
     * Private add method, ignoring modification counter restriction
     * 
     * @param e data element to be added
     * 
     * @return true as successful, false as unsuccessful
     */
    private boolean privateAdd(E e) {
        if (root == null) {
            BinaryTreeNode<E> node = new BinaryTreeNode<E>(e, null, null, null);
            root = node;
            size = 1;
            return true;
        } else {
            BinaryTreeNode<E> currentNode = root, newNode = null;
            while (true) {
                if (currentNode.hasRight()) {
                    currentNode = currentNode.getRight();
                } else {
                    newNode = new BinaryTreeNode<E>(e, currentNode, null, null);
                    currentNode.setRight(newNode);
                    size++;
                    break;
                }
            }
            notifyNodeAdded(newNode, true);
            return true;
        }
    }
    
    /**
     * Add an element to the last
     */
    @Override
    public boolean add(E e) {
        if (privateAdd(e)) {
            modCount++;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Get an element at the specified index location
     * 
     * @return target element
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        return getNode(index).getElement();
    }
    
    /**
     * Remove an element from the list
     * 
     * @param o target element to remove
     * 
     * @return true as successful, false otherwise
     */
    @Override
    public boolean remove(Object o) {
        if (root == null) {
            return false;
        } else {
            BinaryTreeNode<E> node = locateNode(o, root);
            if (node != null) {
                removeNode(node);
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * Remove an element at the specified index location
     * 
     * @param index target index
     * 
     * @return removed element
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        BinaryTreeNode<E> node = locateNodeByRelativeIndex(root, index);
        E element = node.getElement();
        removeNode(node); // modCount incrementation is handled
        return element;
    }
    
    /**
     * Clear this list
     */
    @Override
    public void clear() {
        root = null;
        size = 0;
        modCount++;
    }
    
    /**
     * Get the list size
     * 
     * @return list size
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Locate the index of the specified element
     * 
     * @param o target element
     * 
     * @return found index
     */
    @Override
    public int indexOf(Object o) {
        if (root == null) {
            return -1;
        } else {
            return indexOf(o, root, root.getLeftNodeCount(), true);
        }
    }
    
    /**
     * Locate the last index of the specified element
     * 
     * @param o target element
     * 
     * @return found index
     */
    @Override
    public int lastIndexOf(Object o) {
        if (root == null) {
            return -1;
        } else {
            return indexOf(o, root, root.getLeftNodeCount(), false);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        if (root != null) {
            buildArray(array, root, root.getLeftNodeCount());
        }
        return array;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        buildArray(a, root, root.getLeftNodeCount());
        return a;
    }
    
    /**
     * Check whether this list contains the specified data or not
     * 
     * @param o target data
     * 
     * @return check result
     */
    @Override
    public boolean contains(Object o) {
        if (root == null) {
            return false;
        } else {
            return locateNode(o, root) != null;
        }
    }
    
    /**
     * Check the provided index is within range, otherwise IndexOutOfBoundsException is thrown
     * 
     * @param index target index
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    /**
     * Get the node of the target index
     * 
     * @param index target index
     * @return node of the target index
     */
    private BinaryTreeNode<E> getNode(int index) {
        checkIndex(index);
        return locateNodeByRelativeIndex(root, index);
    }
    
    /**
     * Find the maximum node starting from the provided node
     * 
     * @param node starting node
     * @return maximum node
     */
    private BinaryTreeNode<E> findMaxNode(BinaryTreeNode<E> node) {
        if (node.hasRight()) {
            return findMaxNode(node.getRight());
        } else {
            return node;
        }
    }
    
    /**
     * Find the minimum node starting from the provided node
     * 
     * @param node starting node
     * @return minimum node
     */
    private BinaryTreeNode<E> findMinNode(BinaryTreeNode<E> node) {
        if (node.hasLeft()) {
            return findMinNode(node.getLeft());
        } else {
            return node;
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
    @SuppressWarnings("unchecked")
    private <T> void buildArray(T[] array, BinaryTreeNode<E> node, int index) {
        if (index < array.length) {
            array[index] = (T)node.getElement();
        }
        if (node.hasLeft()) {
            buildArray(array, node.getLeft(), index - node.getLeft().getRightNodeCount()-1);
        }
        if (node.hasRight()) {
            buildArray(array, node.getRight(), index + node.getRight().getLeftNodeCount()+1);
        }
    }
    
    /**
     * Remove the provided node
     * 
     * @param node node to be removed
     */
    private void removeNode(BinaryTreeNode<E> node) {
        if (node == null) {
            return;
        }
        if (node.hasChild()) {
            BinaryTreeNode<E> s = null, sParent = null, parent = node.getParent(),
                left = node.getLeft(), right = node.getRight();
            BinaryTreeNode.Type nType = BinaryTreeNode.Type.LEFT;
            if (left != null) {
                s = findMaxNode(left);
                sParent = s.getParent();
                if (sParent.getLeft() == s) {
                    nType = BinaryTreeNode.Type.LEFT;
                    sParent.setLeft(null);
                } else {
                    nType = BinaryTreeNode.Type.RIGHT;
                    sParent.setRight(null);
                }
                s.setParent(parent);
                s.setRight(right);
                
                if (right != null) {
                    right.setParent(s);
                }
                if (left != s) {
                    if (s.getLeft() != null) {
                        sParent.setRight(s.getLeft());
                        s.getLeft().setParent(sParent);
                    }
                    s.setLeft(left);
                    if (left != null) {
                        left.setParent(s);
                    }
                }
            } else {
                s = findMinNode(right);
                sParent = s.getParent();
                if (sParent.getRight() == s) {
                    nType = BinaryTreeNode.Type.RIGHT;
                    sParent.setRight(null);
                } else {
                    nType = BinaryTreeNode.Type.LEFT;
                    sParent.setLeft(null);
                }
                s.setParent(parent);
                s.setLeft(left);
                
                if (right != s) {
                    if (s.getRight() != null) {
                        sParent.setLeft(s.getRight());
                        s.getRight().setParent(sParent);
                    }
                    s.setRight(right);
                    if (right != null) {
                        right.setParent(s);
                    }
                }
            }
            
            if (parent != null) {
                if (node == parent.getRight()) {
                    parent.setRight(s);
                } else {
                    parent.setLeft(s);
                }
            } else {
                root = s;
            }
            
            s.setLeftDepth(node.getLeftDepth());
            s.setLeftNodeCount(node.getLeftNodeCount());
            s.setRightDepth(node.getRightDepth());
            s.setRightNodeCount(node.getRightNodeCount());
            size--;
            if (sParent != node) {
                notifyNodeRemoved(s, sParent, nType, true);
            } else {
                notifyNodeRemoved(s, s, nType, true);
            }
            modCount++;
        } else {
            if (node == root) {
                root = null;
                size = 0;
            } else {
                BinaryTreeNode<E> parent = node.getParent();
                node.setParent(null);
                size--;
                if (parent != null) {
                    if (node == parent.getLeft()) {
                        parent.setLeft(null);
                        notifyNodeRemoved(node, parent, BinaryTreeNode.Type.LEFT, true);
                    } else {
                        parent.setRight(null);
                        notifyNodeRemoved(node, parent, BinaryTreeNode.Type.RIGHT, true);
                    }
                }
            }
            modCount++;
        }
    }
    
    /**
     * Find the index of the target element
     * 
     * @param e target element
     * @param node starting node
     * @param index index of the starting node
     * @param findSmaller true to find smallest index if smaller value is found, otherwise find the greatest index
     * 
     * @return index of the target element, -1 if not found
     */
    private int indexOf(Object o, BinaryTreeNode<E> node, int index, boolean findSmaller) {
        boolean equals = (o == null)? node.getElement() == null : o.equals(node.getElement());
        if (equals) {
            if (findSmaller) {
                if (node.hasLeft()) {
                    int smallerIndex = indexOf(o, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, findSmaller);
                    return (smallerIndex != -1)? smallerIndex : index;
                } else {
                    return index;
                }
            } else {
                if (node.hasRight()) {
                    int greaterIndex = indexOf(o, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, findSmaller);
                    return (greaterIndex != -1)? greaterIndex : index;
                } else {
                    return index;
                }
            }
        } else {
            int lIndex = -1, rIndex = -1;
            if (node.hasLeft()) {
                lIndex = indexOf(o, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, findSmaller);
            }
            if (node.hasRight()) {
                rIndex = indexOf(o, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, findSmaller);
            }
            if (lIndex == -1) {
                return rIndex;
            } else if (rIndex == -1) {
                return lIndex;
            } else {
                return (findSmaller)? ((lIndex < rIndex)? lIndex : rIndex) : ((lIndex > rIndex)? lIndex : rIndex);
            }
        }
    }
    
    /**
     * Helper method to locate the node with specified element
     * 
     * @param o target element
     * @param node current node
     * 
     * @return found node
     */
    private BinaryTreeNode<E> locateNode(Object o, BinaryTreeNode<E> node) {
        boolean equals = (o == null)? node.getElement() == null : o.equals(node.getElement());
        if (equals) {
            return node;
        } else {
            BinaryTreeNode<E> n = null;
            if (node.hasLeft()) {
                n = locateNode(o, node.getLeft());
            }
            if (n == null && node.hasRight()) {
                n = locateNode(o, node.getRight());
            }
            return n;
        }
    }
    
    /**
     * Location node by the relative index from the reference node<br>
     * Relative index of a node is the left node count of that node
     * 
     * @param node reference node
     * @param relativeIndex relative index to the node from reference node
     * @return target node
     */
    private BinaryTreeNode<E> locateNodeByRelativeIndex(BinaryTreeNode<E> node, int relativeIndex) {
        if (node == null) {
            return null;
        } else {
            int leftNodes = node.getLeftNodeCount();
            if (relativeIndex == leftNodes) {
                return node;
            } else if (relativeIndex < leftNodes) {
                return locateNodeByRelativeIndex(node.getLeft(), relativeIndex);
            } else if (relativeIndex > leftNodes) {
                return locateNodeByRelativeIndex(node.getRight(), relativeIndex-leftNodes-1);
            } else {
                return null;
            }
        }
    }
    
    /**
     * Rotate the provided node to left
     * 
     * @param node target node to rotate
     * @return the new parent of the rotated node
     */
    private BinaryTreeNode<E> rotateLeft(BinaryTreeNode<E> node) {
        BinaryTreeNode<E> tmp = node.getRight();
        node.setRight(tmp.getLeft());
        if (tmp.getLeft() != null) {
            tmp.getLeft().setParent(node);
        }
        tmp.setParent(node.getParent());
        if (node.getParent() == null) {
            root = tmp;
        } else {
            if (node == node.getParent().getLeft()) {
                node.getParent().setLeft(tmp);
            } else {
                node.getParent().setRight(tmp);
            }
        }
        tmp.setLeft(node);
        node.setParent(tmp);
        node.setRightDepth(tmp.getLeftDepth());
        node.setRightNodeCount(tmp.getLeftNodeCount());
        tmp.setLeftDepth(node.getDepth() + 1);
        tmp.setLeftNodeCount(node.getNodeCount() + 1);
        return tmp;
    }
    
    /**
     * Rotate the proved node to right
     * 
     * @param node target node to rotate
     * @return the new parent of the rotated node
     */
    private BinaryTreeNode<E> rotateRight(BinaryTreeNode<E> node) {
        BinaryTreeNode<E> tmp = node.getLeft();
        node.setLeft(tmp.getRight());
        if (tmp.getRight() != null) {
            tmp.getRight().setParent(node);
        }
        tmp.setParent(node.getParent());
        if (node.getParent() == null) {
            root = tmp;
        } else {
            if (node == node.getParent().getRight()) {
                node.getParent().setRight(tmp);
            } else {
                node.getParent().setLeft(tmp);
            }
        }
        tmp.setRight(node);
        node.setParent(tmp);
        node.setLeftDepth(tmp.getRightDepth());
        node.setLeftNodeCount(tmp.getRightNodeCount());
        tmp.setRightDepth(node.getDepth() + 1);
        tmp.setRightNodeCount(node.getNodeCount() + 1);
        return tmp;
    }
    
    /**
     * Notify a node is added to this list
     * 
     * @param n added node
     * @param rebalance this AVL Tree should be rebalanced or not
     */
    private void notifyNodeAdded(BinaryTreeNode<E> n, boolean rebalance) {
        BinaryTreeNode<E> parent = n.getParent();
        if (parent != null) {
            if (n == parent.getLeft()) {
                parent.incrementLeftNodeCount();
            } else {
                parent.incrementRightNodeCount();
            }
            if (rebalance) {
                if (n == parent.getLeft()) {
                    parent.incrementLeftDepth();
                } else {
                    parent.incrementRightDepth();
                }
                if (Math.abs(parent.getBalanceFactor()) >= 2) {
                    if (parent.getBalanceFactor() >= 2) {
                        if (parent.getLeft().getBalanceFactor() < 0) {
                            rotateLeft(parent.getLeft());
                        }
                        notifyNodeAdded(rotateRight(parent), false);
                    } else {
                        if (parent.getRight().getBalanceFactor() > 0) {
                            rotateRight(parent.getRight());
                        }
                        notifyNodeAdded(rotateLeft(parent), false);
                    }
                } else if (parent.getBalanceFactor() == 0) {
                    notifyNodeAdded(parent, false);
                } else {
                    notifyNodeAdded(parent, rebalance);
                }
            } else {
                notifyNodeAdded(parent, rebalance);
            }
        } else {
            return;
        }
    }
    
    /**
     * Notify a node is removed from this list
     * 
     * @param n removed node
     * @param parent parent of removed node
     * @param nodeType node type of removed node to its parent node
     * @param rebalance this AVL Tree should be rebalanced or not
     */
    private void notifyNodeRemoved(
            BinaryTreeNode<E> n, BinaryTreeNode<E> parent, BinaryTreeNode.Type nodeType, boolean rebalance
    ) {
        if (parent != null) {
            if (nodeType == BinaryTreeNode.Type.LEFT) {
                parent.decrementLeftNodeCount();
            } else if (nodeType == BinaryTreeNode.Type.RIGHT) {
                parent.decrementRightNodeCount();
            }
            if (rebalance) {
                if (nodeType == BinaryTreeNode.Type.LEFT) {
                    parent.decrementLeftDepth();
                } else if (nodeType == BinaryTreeNode.Type.RIGHT) {
                    parent.decrementRightDepth();
                }
                if (Math.abs(parent.getBalanceFactor()) >= 2) {
                    BinaryTreeNode<E> np = null;
                    if (parent.getBalanceFactor() >= 2) {
                        if (parent.getLeft().getBalanceFactor() < 0) {
                            rotateLeft(parent.getLeft());
                        }
                        np = rotateRight(parent);
                    } else {
                        if (parent.getRight().getBalanceFactor() > 0) {
                            rotateRight(parent.getRight());
                        }
                        np = rotateLeft(parent);
                    }
                    if (np.getParent() != null) {
                        boolean rb = Math.abs(np.getBalanceFactor()) != 1;
                        if (np == np.getParent().getLeft()) {
                            notifyNodeRemoved(np, np.getParent(), BinaryTreeNode.Type.LEFT, rb);
                        } else if (np == np.getParent().getRight()) {
                            notifyNodeRemoved(np, np.getParent(), BinaryTreeNode.Type.RIGHT, rb);
                        }
                    }
                } else if (Math.abs(parent.getBalanceFactor()) == 1) {
                    if (parent.getParent() != null) {
                        if (parent == parent.getParent().getLeft()) {
                            notifyNodeRemoved(parent, parent.getParent(), BinaryTreeNode.Type.LEFT, false);
                        } else if (parent == parent.getParent().getRight()) {
                            notifyNodeRemoved(parent, parent.getParent(), BinaryTreeNode.Type.RIGHT, false);
                        }
                    }
                } else {
                    if (parent.getParent() != null) {
                        if (parent == parent.getParent().getLeft()) {
                            notifyNodeRemoved(parent, parent.getParent(), BinaryTreeNode.Type.LEFT, rebalance);
                        } else if (parent == parent.getParent().getRight()){
                            notifyNodeRemoved(parent, parent.getParent(), BinaryTreeNode.Type.RIGHT, rebalance);
                        }
                    }
                }
            } else {
                if (parent.getParent() != null) {
                    if (parent == parent.getParent().getLeft()) {
                        notifyNodeRemoved(parent, parent.getParent(), BinaryTreeNode.Type.LEFT, rebalance);
                    } else if (parent == parent.getParent().getRight()){
                        notifyNodeRemoved(parent, parent.getParent(), BinaryTreeNode.Type.RIGHT, rebalance);
                    }
                }
            }
        }
    }
}
