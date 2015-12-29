package utils.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface SortedList<E> extends Iterable<E>, List<E>, Cloneable, Serializable {
    public E getMin();
    public E getMax();

    /**
     * Add an element into this list with duplication control flag
     *
     * @param e element to be added
     * @param allowDuplicate duplication control flag, if false, duplicate element will be ignored
     * @return true as successful, false as unsuccessful
     */
    public default boolean add(E e, boolean allowDuplicate) {
        if (allowDuplicate || !contains(e)) {
            return add(e);
        } else {
            return false;
        }
    }

    /**
     * Simply redirect add(E, boolean), index is ignored as this is a sorted list
     *
     * @param index ignored
     * @param element data element
     * @param allowDuplicate allow for duplication, if false, duplicate element will be ignored
     */
    public default boolean add(int index, E element, boolean allowDuplicate) {
        // Index is ignored as this is a sortedList
        return add(element, allowDuplicate);
    }

    /**
     * Add the provided data collection with duplication control flag
     *
     * @param c data collection
     * @param allowDuplicate duplication control flag, false to ignore duplicated elements
     * @return true as successful (at least one element is successfully added), false otherwise
     */
    public boolean addAll(Collection<? extends E> c, boolean allowDuplicate);
    
    public void resort();
    
    public SortedList<E> clone();
    
    public SortedList<E> getSubList(int fromIndex, int toIndex);
}
