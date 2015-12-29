package utils.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * AVL tree implemented as java list
 * 
 * @author Tony Tsang
 *
 * @param <E> Generic data type
 */
@SuppressWarnings("serial")
public class SortedListAvl<E> implements SortedList<E>{
    /**
     * Root node
     */
    private BinaryTreeNode<E> root;
    
    /**
     * List size
     */
    private int size;
    
    /**
     * Comparator used for sorting
     */
    private Comparator<E> comparator;
    
    /**
     * Modification count for concurrent modification check
     */
    protected int modCount = 0;
    
    /**
     * Private construct used for cloning
     * 
     * @param root root node
     * @param comparator data comparator
     * @param size list size
     */
    private SortedListAvl(BinaryTreeNode<E> root, Comparator<E> comparator, int size) {
        this.root = root;
        this.comparator = comparator;
        this.size = size;
    }
    
    /**
     * Construct an AVL Tree List, default: ascending order, null as smaller value
     */
    public SortedListAvl() {
        this(true, true);
    }
    
    /**
     * Construct an AVL Tree List, providing sort order, default null as smaller value
     * 
     * @param asc in ascending order (true) or descending order (false)
     */
    public SortedListAvl(boolean asc) {
        this(asc, true);
    }
    
    /**
     * Construct an AVL Tree List, provided sort order and null order
     * 
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListAvl(boolean asc, boolean nullAsSmaller) {
        comparator = DataComparator.buildComparator(asc, nullAsSmaller);
        root = null;
        size = 0;
    }
    
    /**
     * Construct an AVL Tree List, providing comparator used for sorting, default: null as smaller value
     * 
     * @param comparator comparator used for sorting
     */
    public SortedListAvl(Comparator<E> comparator) {
        this(comparator, true);
    }
    
    /**
     * Construct an AVL Tree List, providing comparator used for sorting and null order
     * 
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListAvl(Comparator<E> comparator, boolean nullAsSmaller) {
        this.comparator = DataComparator.buildComparator(comparator, nullAsSmaller);
        root = null;
        size = 0;
    }
    
    /**
     * Construct an AVL Tree List with existing data collection, default ascending order, null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     */
    public SortedListAvl(Collection<E> c) {
        this();
        addAll(c);
    }
    
    /**
     * Construct an AVL Tree List with existing data collection, proving sort order, default: null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     */
    public SortedListAvl(Collection<E> c, boolean asc) {
        this(asc);
        addAll(c);
    }
    
    /**
     * Construct an AVL Tree List with existing data collection, proving sort order & null order<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListAvl(Collection<E> c, boolean asc, boolean nullAsSmaller) {
        this(asc, nullAsSmaller);
        addAll(c);
    }
    
    /**
     * Construct an AVL Tree List with existing data collection, proving comparator used for sorted<br>
     * Default: null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param comparator comparator used for sorting
     */
    public SortedListAvl(Collection<E> c, Comparator<E> comparator) {
        this(c, comparator, true);
    }
    
    /**
     * Construct an AVL Tree List with existing data collection, proving comparator used for sorted & null order<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListAvl(Collection<E> c, Comparator<E> comparator, boolean nullAsSmaller) {
        this(comparator, nullAsSmaller);
        addAll(c);
    }
    
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
            BinaryTreeNode<E> currentNode = root, newNode;
            while (true) {
                int comp = comparator.compare(currentNode.getElement(), e);
                if (comp >= 0) {
                    if (currentNode.hasLeft()) {
                        currentNode = currentNode.getLeft();
                    } else {
                        newNode = new BinaryTreeNode<E>(e, currentNode, null, null);
                        currentNode.setLeft(newNode);
                        size++;
                        break;
                    }
                } else {
                    if (currentNode.hasRight()) {
                        currentNode = currentNode.getRight();
                    } else {
                        newNode = new BinaryTreeNode<E>(e, currentNode, null, null);
                        currentNode.setRight(newNode);
                        size++;
                        break;
                    }
                }
            }
            notifyNodeAdded(newNode, true);
            return true;
        }
    }
    
    /**
     * Add an element into this list
     *
     * @param e element to be added
     * @return true as successful, false as unsuccessful
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
                            notifyNodeRemoved(
                                    parent, parent.getParent(), BinaryTreeNode.Type.LEFT, rebalance
                            );
                        } else if (parent == parent.getParent().getRight()){
                            notifyNodeRemoved(
                                    parent, parent.getParent(), BinaryTreeNode.Type.RIGHT, rebalance
                            );
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
    
    /**
     * Find the maximum element
     * 
     * @return maximum element
     */
    public E findMax() {
        if (root == null) {
            return null;
        } else {
            return findMaxNode(root).getElement();
        }
    }
    
    /**
     * Find the minimum element
     * 
     * @return minimum element
     */
    public E findMin() {
        if (root == null) {
            return null;
        } else {
            return findMinNode(root).getElement();
        }
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
     * Simply redirect to add(E), index is ignored as this is a sorted list
     * 
     * @param index ignored
     * @param element data element
     */
    @Override
    public void add(int index, E element) {
        // Index is ignored as this is a sortedList
        add(element); // modCount increment is handled
    }
    
    /**
     * Add the provided data collection
     * 
     * @param c data collection
     * 
     * @return true as successful, false as unsuccessful
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c != null) {
            boolean modified = false;
            for (E e : c) {
                if (privateAdd(e)) {
                    modified = true;
                }
            }
            if (modified) {
                modCount++;
            }
            return modified;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection<? extends E> c, boolean allowDuplicate) {
        if (c != null) {
            boolean modified = false;
            for (E e : c) {
                if (allowDuplicate || !contains(e)) {
                    if (privateAdd(e)) {
                        modified = true;
                    }
                }
            }
            if (modified) {
                modCount++;
            }
            return modified;
        } else {
            return false;
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
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // Index is ignored as this is sortedList
        return addAll(c); // modCount incrementation is handled
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
     * Check whether this list contains the provided data or not
     * 
     * @param o target data
     * 
     * @return check result
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        try {
            E e = (E)o;
            BinaryTreeNode<E> currentNode = root;
            while (currentNode != null) {
                int c = comparator.compare(currentNode.getElement(), e);
                if (c == 0) {
                    return true;
                } else if (c < 0) {
                    currentNode = currentNode.getRight();
                } else {
                    currentNode = currentNode.getLeft();
                }
            }
            return false;
        } catch (ClassCastException cce) {
            return false;
        }
    }
    
    /**
     * Check whether this list contains all of the provided data collection
     * 
     * @param c data collection
     * 
     * @return check result
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
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
     * Same as getMin
     * 
     * @return first(minimum) node
     */
    public E getFirst() {
        return getMin();
    }
    
    /**
     * Get the minimum node
     * 
     * @return minimum node
     */
    public E getMin() {
        if (root == null) {
            return null;
        } else {
            BinaryTreeNode<E> node = findMinNode(root);
            if (node != null) {
                return node.getElement();
            } else {
                return null;
            }
        }
    }
    
    /**
     * Same as getMax
     * 
     * @return last(maximum) node
     */
    public E getLast() {
        return getMax();
    }
    
    /**
     * Get the maximum node
     * 
     * @return maximum node
     */
    public E getMax() {
        if (root == null) {
            return null;
        } else {
            BinaryTreeNode<E> node = findMaxNode(root);
            if (node != null) {
                return node.getElement();
            } else {
                return null;
            }
        }
    }
    
    /**
     * Poll the first node out of the list
     * 
     * @return the first node
     */
    public E poll() {
        return pollMin();
    }
    
    /**
     * Poll the first node out of the list
     * 
     * @return the first node
     */
    public E pollFirst() {
        return pollMin();
    }
    
    /**
     * Poll the minimum node out of the list
     * 
     * @return the minimum node
     */
    public E pollMin() {
        if (root == null) {
            return null;
        } else {
            return remove(0);
        }
    }
    
    /**
     * Poll the last node out of the list
     * 
     * @return the maximum node
     */
    public E pollLast() {
        return pollMax();
    }
    
    public E pollMax() {
        if (root == null) {
            return null;
        } else {
            return remove(size - 1);
        }
    }
    
    /**
     * Get the data element from index
     * 
     * @param index target index
     * 
     * @return the data element
     */
    @Override
    public E get(int index) {
        BinaryTreeNode<E> n = getNode(index);
        if (n == null) {
            return null;
        } else {
            return n.getElement();
        }
    }

    /**
     * Search for first found index from binary search, no guarantee of first or last index
     *
     * @param o target value
     * @return first found index from binary search
     */
    public int binarySearch(Object o) {
        if (root == null) {
            return -1;
        }

        try {
            E e = (E) o;

            return indexOf(e, root, root.getLeftNodeCount());
        } catch (ClassCastException cce) {
            return -1;
        }
    }

    /**
     * Search for first found index from binary search with cross comparator, no guarantee of first or last index
     *
     * @param o target value
     * @param crossComparator cross comparator of target value type to list element type
     * @return first found index from binary search
     */
    public <O> int binarySearch(O o, CrossComparator<O, E> crossComparator) {
        if (root == null) {
            return -1;
        } else {
            return indexOf(o, root, root.getLeftNodeCount(), crossComparator);
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
    @Override
    public int indexOf(Object o) {
        if (root == null) {
            return -1;
        }
        try {
            E e = (E) o;
            return indexOf(e, root, root.getLeftNodeCount(), true);
        } catch (ClassCastException cce) {
            return -1;
        }
    }

    /**
     * Get the index of the target value (in other type) with cross comparator
     *
     * @param o target value
     * @param crossComparator cross comparator of target value type to list element type
     *
     * @return index of the target element, -1 if not found
     */
    public <O> int indexOf(O o, CrossComparator<O, E> crossComparator) {
        if (root == null) {
            return -1;
        } else {
            return indexOf(o, root, root.getLeftNodeCount(), crossComparator, true);
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
    public <O> int lastIndexOf(O o, CrossComparator<O, E> crossComparator) {
        if (root == null) {
            return -1;
        } else {
            return indexOf(o, root, root.getLeftNodeCount(), crossComparator, false);
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
    private <O> int indexOf(O o, BinaryTreeNode<E> node, int index, CrossComparator<O, E> crossComparator) {
        int compareResult = crossComparator.compare(o, node.getElement());
        if (compareResult == 0) {
            return index;
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(o, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, crossComparator);
            } else {
                return -1;
            }
        } else {
            if (node.hasRight()) {
                return indexOf(o, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, crossComparator);
            } else {
                return -1;
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
    private <O> int indexOf(O o, BinaryTreeNode<E> node, int index, CrossComparator<O, E> crossComparator, boolean findSmaller) {
        int compareResult = crossComparator.compare(o, node.getElement());

        if (compareResult == 0) {
            if (findSmaller) {
                if (node.hasLeft()) {
                    int smallerIndex =
                        indexOf(
                            o, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, crossComparator, findSmaller
                        );
                    return (smallerIndex != -1)? smallerIndex : index;
                } else {
                    return index;
                }
            } else {
                if (node.hasRight()) {
                    int greaterIndex =
                        indexOf(
                            o, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, crossComparator, findSmaller
                        );
                    return (greaterIndex != -1)? greaterIndex : index;
                } else {
                    return index;
                }
            }
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(o, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, crossComparator, findSmaller);
            } else {
                return -1;
            }
        } else {
            if (node.hasRight()) {
                return indexOf(o, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, crossComparator, findSmaller);
            } else {
                return -1;
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
    private int indexOf(E e, BinaryTreeNode<E> node, int index) {
        int compareResult = comparator.compare(e, node.getElement());
        if (compareResult == 0) {
            return index;
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(e, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1);
            } else {
                return -1;
            }
        } else {
            if (node.hasRight()) {
                return indexOf(e, node.getRight(), index + node.getRight().getLeftNodeCount() + 1);
            } else {
                return -1;
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
    private int indexOf(E e, BinaryTreeNode<E> node, int index, boolean findSmaller) {
        int compareResult = comparator.compare(e, node.getElement());
        if (compareResult == 0) {
            if (findSmaller) {
                if (node.hasLeft()) {
                    int smallerIndex = 
                        indexOf(
                            e, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, findSmaller
                        );
                    return (smallerIndex != -1)? smallerIndex : index;
                } else {
                    return index;
                }
            } else {
                if (node.hasRight()) {
                    int greaterIndex = 
                        indexOf(
                                e, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, findSmaller
                        );
                    return (greaterIndex != -1)? greaterIndex : index;
                } else {
                    return index;
                }
            }
        } else if (compareResult < 0) {
            if (node.hasLeft()) {
                return indexOf(e, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, findSmaller);
            } else {
                return -1;
            }
        } else {
            if (node.hasRight()) {
                return indexOf(e, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, findSmaller);
            } else {
                return -1;
            }
        }
    }
    
    /**
     * Check whether this list is empty or not
     * 
     * @return true as empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Get the iterator of this list
     * 
     * @return iterator of this list
     */
    @Override
    public Iterator<E> iterator() {
        return new SortedListIterator();
    }
    
    /**
     * Get the last index of the target element
     * 
     * @param o target element
     * 
     * @return index of the target element, -1 if not found
     */
    @SuppressWarnings("unchecked")
    @Override
    public int lastIndexOf(Object o) {
        if (root == null) {
            return -1;
        }
        try {
            E e = (E) o;
            return indexOf(e, root, root.getLeftNodeCount(), false);
        } catch (ClassCastException cce) {
            return -1;
        }
    }

    /**
     * Get the ListIterator of this list
     * 
     * @return ListIterator of this list
     */
    @Override
    public ListIterator<E> listIterator() {
        return new SortedListIterator();
    }
    
    /**
     * Get the ListIterator of this list with starting index
     * 
     * @return ListIterator of this list with starting index
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return new SortedListIterator(index);
    }
    
    /**
     * Swap the element of the provided node
     * 
     * @param n1 node 1
     * @param n2 node 2
     */
    private void swapNodeElement(BinaryTreeNode<E> n1, BinaryTreeNode<E> n2) {
        E element = n1.getElement();
        n1.setElement(n2.getElement());
        n2.setElement(element);
    }
    
    /**
     * Swap the element of the provided node index, p.s. only the element swapped but not the nodes
     * 
     * @param i1 index 1
     * @param i2 index 2
     */
    @SuppressWarnings("unused")
    private void swapElement(int i1, int i2) {
        swapNodeElement(getNode(i1), getNode(i2));
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
            int leftNodeCount = node.getLeftNodeCount();
            if (relativeIndex == leftNodeCount) {
                return node;
            } else if (relativeIndex < leftNodeCount) {
                return locateNodeByRelativeIndex(node.getLeft(), relativeIndex);
            } else if (relativeIndex > leftNodeCount) {
                return locateNodeByRelativeIndex(node.getRight(), relativeIndex-leftNodeCount-1);
            } else {
                return null;
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
    private BinaryTreeNode<E> locateNode(E element) {
        BinaryTreeNode<E> currentNode = root;
        while (currentNode != null) {
            int comp = comparator.compare(element, currentNode.getElement());
            if (comp == 0) {
                return currentNode;
            } else if (comp < 0) {
                if (currentNode.hasLeft()) {
                    currentNode = currentNode.getLeft();
                } else {
                    break;
                }
            } else {
                if (currentNode.hasRight()) {
                    currentNode = currentNode.getRight();
                } else {
                    break;
                }
            }
        }
        return null;
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
            BinaryTreeNode<E> s, sParent, parent = node.getParent(),
                left = node.getLeft(), right = node.getRight();
            BinaryTreeNode.Type nType;
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
     * Remove the provided element
     * 
     * @param o target element
     * 
     * @return true as successful remove, false as unsuccessful
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        try {
            E element = (E) o;
            BinaryTreeNode<E> node = locateNode(element);
            if (node != null) {
                removeNode(node); // modCount incrementation is handled
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException cce) {
            return false;
        }
    }
    
    /**
     * Remove the provided index
     * 
     * @param index target index to be removed
     * 
     * @return the removed element
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        BinaryTreeNode<E> node = locateNodeByRelativeIndex(root, index);
        if (node != null) {
            E element = node.getElement();
            removeNode(node); // modCount incrementation is handled
            return element;
        } else {
            return null;
        }
    }
    
    /**
     * Remove the provided data collection
     * 
     * @param c the data collection to be removed
     * 
     * @return true as successful, false as unsuccessful
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }
    
    /**
     * Retain only the element contained within the provided data collection
     * 
     * @param c target data collection
     * 
     * @return true as successful, false as unsuccessful
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(Collection<?> c) {
        SortedListAvl<E> s = new SortedListAvl<E>(comparator);
        
        for (Object o : c) {
            int firstIndex = indexOf(o);

            if (firstIndex != -1 && !s.contains(o)) {
                int lastIndex = lastIndexOf(o);

                if (lastIndex == firstIndex) {
                    s.add((E)o);
                } else {
                    for (int i = firstIndex; i <= lastIndex; i++) {
                        s.add((E)o);
                    }
                }
            }
        }
        
        if (s.size < size) {
            root = s.root;
            size = s.size;
            modCount++;
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * This method is not the same as normal list set!!<br>
     * 
     * Element of the provided index is removed, then adding the new element into the list
     * 
     * @param index element on this index will be removed then re-add, new element index may change!
     * @param element new data element to be added
     * 
     * @return removed data element
     */
    @Override
    public E set(int index, E element) {
        // As this is a sorted list old element is removed and then re-add the element, index may change.
        if (index >= 0 && index < size) {
            E e = remove(index);
            privateAdd(element);
            return e;
        } else {
            return null;
        }
    }
    
    /**
     * Get the size of this list
     * 
     * @return size of this list
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * Sublist is not supported, there is no easy way to implement sublist's add, remove, set feature without affecting the sort order of the original list!<br>
     * Use {@link #getSubList} instead
     * 
     * @return nothing will be returned!
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("SubList is not supported!");
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
    public SortedListAvl<E> getSubList(int fromIndex, int toIndex) {
        SortedListAvl<E> list = new SortedListAvl<E>();
        if (root != null) {
            getSubList(list, root, root.getLeftNodeCount(), fromIndex, toIndex);
        }
        return list;
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
    private void getSubList(
            SortedListAvl<E> list, BinaryTreeNode<E> node, int index, int fromIndex, int toIndex
    ) {
        if (index >= fromIndex && index < toIndex) {
            list.add(node.getElement());
        }
        if (node.hasLeft() && index > fromIndex) {
            getSubList(list, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1, fromIndex, toIndex);
        }
        if (node.hasRight() && index < toIndex - 1) {
            getSubList(list, node.getRight(), index + node.getRight().getLeftNodeCount() + 1, fromIndex, toIndex);
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
        for (int i = size; i < a.length; i++) {
            a[i] = null;
        }
        return a;
    }
    
    /**
     * Get the depth of tree
     * 
     * @return depth of tree
     */
    public int getTreeDepth() {
        if (root == null) {
            return 0;
        } else {
            return root.getDepth() + 1;
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
     * Print the tree structure, used for debug only
     */
    public void printList() {
        if (root == null) {
            System.out.println("- List Empty -");
        } else {
            inorder(root);
            System.out.println();
        }
    }
    
    /**
     * Print the node structure using inorder tree traversal technique
     * 
     * @param node current node
     */
    private void inorder(BinaryTreeNode<E> node) {
        if (node.hasLeft()) {
            inorder(node.getLeft());
        }
        System.out.print(node.getElement().toString() + "-[" + node.getLeftDepth() + "," + node.getRightDepth() + "," + node.getLeftNodeCount() + "," + node.getRightNodeCount() + "](p: " + ((node.getParent()!=null)? node.getParent().getElement() : "null") + "), (l: " + ((node.getLeft()!=null)? node.getLeft().getElement() : "null") + "), (r: " + ((node.getRight()!=null)? node.getRight().getElement():"null") + ")-");
        if (node.hasRight()) {
            inorder(node.getRight());
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
    private BinaryTreeNode<E> cloneNodes(BinaryTreeNode<E> copiedParent, BinaryTreeNode<E> node) {
        BinaryTreeNode<E> newNode = new BinaryTreeNode<E>(node.getElement(), copiedParent, null, null);
        
        newNode.setLeftDepth(node.getLeftDepth());
        newNode.setRightDepth(node.getRightDepth());
        newNode.setLeftNodeCount(node.getLeftNodeCount());
        newNode.setRightNodeCount(node.getRightNodeCount());
        
        if (node.getLeft() != null) {
            newNode.setLeft(cloneNodes(newNode, node.getLeft()));
        }
        
        if (node.getRight() != null) {
            newNode.setRight(cloneNodes(newNode, node.getRight()));
        }
        
        return newNode;
    }
    
    /**
     * Clone this AVL Tree List
     * 
     * @return cloned list
     */
    @Override
    public SortedListAvl<E> clone() {
        if (root != null) {
            BinaryTreeNode<E> newRoot = cloneNodes(null, root);
            return new SortedListAvl<E>(newRoot, comparator, size);
        } else {
            return new SortedListAvl<E>();
        }
    }
    
    /**
     * List Iterator for this specific AVL Tree List
     * 
     * @author Tony Tsang
     *
     */
    private class SortedListIterator implements ListIterator<E> {
        /**
         * Previous node cache
         */
        private BinaryTreeNode<E> previousNode;
        /**
         * Next node cache
         */
        private BinaryTreeNode<E> nextNode;
        
        /**
         * Previous index cache
         */
        private int previousIndex;
        /**
         * Next index cache
         */
        private int nextIndex;
        /**
         * Current index cache - maybe deleted
         */
        private int currentIndex;
        
        /**
         * Flag indicating call to remove is valid or not
         */
        private boolean canRemove = false;
        
        /**
         * Counter used to detect concurrent modification
         */
        private int expectedModCount = modCount;
        
        /**
         * Default iterator constructor to start from head
         */
        public SortedListIterator() {
            nextIndex     =  0;
            currentIndex  = -1;
            previousIndex = -1;
            
            previousNode = null;
            if (root != null) {                 
                nextNode = findMinNode(root);
            } else {
                nextNode = null;
            }
        }
        
        /**
         * Constructor a list iterator start from the provided index
         * 
         * @param i starting index
         */
        public SortedListIterator(int i) {
            checkIndex(i);
            nextIndex     = i;
            previousIndex = i - 1;
            currentIndex  = -1;
            
            nextNode = getNode(i);
            previousNode = nextNode.getPreviousNode();
        }
        
        /**
         * Check for concurrent modification
         */
        private void checkForComodification() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        /**
         * Add an element into the underlying AVL Tree list, (considering to change this method to unsupported)<br>
         * P.S. Element will not added to the current location of the iterator but in its sort order
         * 
         */
        @Override
        public void add(E e) {
            SortedListAvl.this.add(e);
            int index = indexOf(e);
            if (index <= currentIndex) {
                currentIndex += 1;
                nextIndex     = currentIndex + 1;
                previousIndex = currentIndex - 1;
                
                nextNode     = getNode(nextIndex);
                previousNode = nextNode.getPreviousNode().getPreviousNode();
            }
            canRemove = true;
            expectedModCount++;
        }
        
        /**
         * Check whether there is any next element
         * 
         * @return true has next, false otherwise
         */
        @Override
        public boolean hasNext() {
            return nextIndex < size();
        }
        
        /**
         * Check whether there is any previous element
         * 
         * @return true has previous, false otherwise
         */
        @Override
        public boolean hasPrevious() {
            return previousIndex >= 0;
        }
        
        /**
         * Get the next element
         * 
         * @return next element
         */
        @Override
        public E next() {
            checkForComodification();
            if (nextIndex < size()) {
                canRemove = true;
                currentIndex = nextIndex;
                nextIndex++;
                previousIndex = nextIndex - 1;
                
                E element = nextNode.getElement();
                
                previousNode = nextNode;
                nextNode     = nextNode.getNextNode();
                
                return element;
            } else {
                throw new NoSuchElementException("No next element");
            }
        }
        
        /**
         * Get the next index
         * 
         * @return next index
         */
        @Override
        public int nextIndex() {
            if (nextIndex < size()) {
                return nextIndex;
            } else {
                return size();
            }
        }
        
        /**
         * Get the previous element
         * 
         * @return previous element
         */
        @Override
        public E previous() {
            checkForComodification();
            if (previousIndex >= 0) {
                currentIndex = previousIndex;
                previousIndex--;
                nextIndex = previousIndex + 1;
                
                E element = previousNode.getElement();
                
                nextNode     = previousNode;
                previousNode = previousNode.getPreviousNode();
                
                canRemove = true;
                return element;
            } else {
                throw new NoSuchElementException("No previous element (previousIndex: " + previousIndex + ")");
            }
        }
        
        /**
         * Get the previous index
         * 
         * @return previous index
         */
        @Override
        public int previousIndex() {
            if (previousIndex >= 0) {
                return previousIndex;
            } else {
                return -1;
            }
        }
        
        /**
         * Remove the current element from the underlying AVL Tree List
         * 
         */
        @Override
        public void remove() {
            if (!canRemove || currentIndex < 0 || currentIndex >= size()) {
                throw new IllegalStateException("Nothing to remove (currentIndex: "+ currentIndex + ")");
            } else {
                checkForComodification();
                SortedListAvl.this.remove(currentIndex);
                nextIndex = currentIndex;
                previousIndex = currentIndex - 1;
                
                if (nextIndex >= 0) {
                    if (nextIndex >= size) {
                        nextNode = null;
                    } else {
                        nextNode = getNode(nextIndex);
                    }
                }
                if (previousIndex >= 0) {
                    previousNode = getNode(previousIndex);
                }
                
                canRemove = false;
                expectedModCount++;
            }
        }
        
        /**
         * Set is not supported, as it contradict with normal expectation of this method.
         */
        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("Set operation is not supported (this is a sorted list)!");
        }
    }
    
    /**
     * Considering to delete this obsolete method<br>
     * Helper method to build a node array from this list
     * 
     * @param nodeArray holder node array
     * @param node current node
     * @param index current node index
     */
    private void buildNodeArray(Object[] nodeArray, BinaryTreeNode<E> node, int index) {
        nodeArray[index] = node;
        if (node.hasLeft()) {
            buildNodeArray(nodeArray, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1);
        }
        if (node.hasRight()) {
            buildNodeArray(nodeArray, node.getRight(), index + node.getRight().getLeftNodeCount() + 1);
        }
    }
    
    /**
     * Helper method to set the node element provided by the element array
     * 
     * @param sortedArray element array in sorted order
     * @param node current node
     * @param index current node index
     */
    @SuppressWarnings("unchecked")
    private void setNodeElement(Object[] sortedArray, BinaryTreeNode<E> node, int index) {
        node.setElement((E)sortedArray[index]);
        if (node.hasLeft()) {
            setNodeElement(sortedArray, node.getLeft(), index - node.getLeft().getRightNodeCount() - 1);
        }
        if (node.hasRight()) {
            setNodeElement(sortedArray, node.getRight(), index + node.getRight().getLeftNodeCount() + 1);
        }
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
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void resort() {
        if (root != null) {
            Object[] elementArray = toArray();
            Arrays.sort(elementArray,
                new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        return comparator.compare((E)o1, (E)o2);
                    }
                }
            );
            setNodeElement(elementArray, root, root.getLeftNodeCount());
        }
    }
}