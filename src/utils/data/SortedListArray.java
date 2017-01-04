package utils.data;

import java.util.*;

import utils.data.sort.MultiSort;
import utils.data.sort.MergeSort;

/**
 * Sorted list implemented as java list, (binary search tree in array form)
 * Extremely slow performance for insertion & removal when comparing to SortedListAvl for large data size, over 35,000 elements.
 * For small data size, less than 35,000 elements, it always performs better than or equals to SortedListAvl in all aspect. 
 * Best for random access & searching
 * 
 * General performance comparision between AVL & Array sorted list implementation (not formally tested):
 * Insertion:
 * AVL: 1, Array: ~30x (1,000,000 elements), ~4x (100,000 elements)
 * Removal:
 * AVL: 1, Array: ~50x (1,000,000 elements), ~2.5x (100,000 elements)
 * Random access:
 * AVL: ~3.5x, Array: 1
 * Search:
 * AVL: ~1.02x (1,000,000 elements), ~1.7x(100,000 elements), Array: 1
 * 
 * @author Tony Tsang
 *
 * @param <E> Data generic type
 */
@SuppressWarnings("serial")
public class SortedListArray<E> implements SortedList<E> {
    /**
     * Element array
     */
    private Object[] elements;
    
    /**
     * Modification count for concurrent modification check
     */
    private int modCount = 0;
    
    /**
     * Comparator used for sorting
     */
    private Comparator<E> comparator;
    
    /**
     * List size
     */
    private int size;
    
    /**
     * Construct an Sorted Array list, default: ascending order, null as smaller value
     */
    public SortedListArray() {
        this(true, true);
    }
    
    /**
     * Construct an Sorted Array List, providing sort order, default null as smaller value
     * 
     * @param asc in ascending order (true) or descending order (false)
     */
    public SortedListArray(boolean asc) {
        this(asc, true);
    }
    
    /**
     * Construct an Sorted Array List, provided sort order and null order
     * 
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListArray(boolean asc, boolean nullAsSmaller) {
        comparator = DataComparator.buildComparator(asc, nullAsSmaller);
        elements = new Object[10];
        this.size = 0;
    }
    
    /**
     * Construct an Sorted Array List, providing comparator used for sorting, default: null as smaller value
     * 
     * @param comparator comparator used for sorting
     */
    public SortedListArray(Comparator<E> comparator) {
        this(comparator, true);
    }
    
    /**
     * Construct an Sorted Array List, providing comparator used for sorting and null order
     * 
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListArray(Comparator<E> comparator, boolean nullAsSmaller) {
        this.comparator = DataComparator.buildComparator(comparator, nullAsSmaller);
        elements = new Object[10];
        this.size = 0;
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, default ascending order, null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     */
    public SortedListArray(Collection<E> c) {
        this();
        addAll(c);
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving sort order, default: null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     */
    public SortedListArray(Collection<E> c, boolean asc) {
        this(asc);
        addAll(c);
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving sort order & null order<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param asc in ascending order (true) or descending order (false)
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListArray(Collection<E> c, boolean asc, boolean nullAsSmaller) {
        this(asc, nullAsSmaller);
        addAll(c);
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving comparator used for sorted<br>
     * Default: null as smaller value<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param comparator comparator used for sorting
     */
    public SortedListArray(Collection<E> c, Comparator<E> comparator) {
        this(c, comparator, true);
    }
    
    /**
     * Construct an Sorted Array List with existing data collection, proving comparator used for sorted & null order<br>
     * Data will be sorted after construction
     * 
     * @param c existing data collection
     * @param comparator comparator used for sorting
     * @param nullAsSmaller null as smaller to any non null value, true for smaller, false for greater
     */
    public SortedListArray(Collection<E> c, Comparator<E> comparator, boolean nullAsSmaller) {
        this(comparator, nullAsSmaller);
        addAll(c);
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
     * Find the insertion index for the target element
     * 
     * @param e target element
     * 
     * @return insertion index
     */
    @SuppressWarnings("unchecked")
    private int findInsertionIndex(E e) {
        int min = 0, max = size - 1;
        
        while (min <= max) {
            if (min == max) {
                int comp = comparator.compare(e, (E) elements[min]);
                if (comp > 0) {
                    return min + 1;
                } else {
                    return min;
                }
            }
            int mid = (max + min) >>> 1;
            int comp = comparator.compare(e, (E) elements[mid]);
            if (comp < 0) {
                max = mid - 1;
            } else if (comp > 0) {
                min = mid + 1;
            } else {
                return mid;
            }
        }
        
        return min;
    }
    
    /**
     * Ensure the underlying array's capacity is enough to the target size 
     * 
     * @param size target size/capacity
     */
    private void ensureCapacity(int size) {
        int capacity = elements.length;
        
        while (size > capacity) {
            capacity *= 2;
        }
        
        if (capacity > elements.length) {
            Object[] newElements = new Object[capacity];
            
            System.arraycopy(elements, 0, newElements, 0, elements.length);
            
            elements = newElements;
            
            modCount++;
        }
    }
    
    /**
     * Get capacity of the underlying array 
     * 
     * @return capacity of the underlying array
     */
    public int getCapacity() {
        return elements.length;
    }
    
    private void insert(int insertIndex, E e) {
        System.arraycopy(elements, insertIndex, elements, insertIndex+1, size-insertIndex);
        elements[insertIndex] = e;
        size++;
    }
    
    @Override
    public boolean add(E e) {
        if (size == 0) {
            ensureCapacity(size+1);
            elements[0] = e;
            size++;
            return true;
        } else {
            ensureCapacity(size + 1);
            modCount++;
            insert(findInsertionIndex(e), e);
            return true;
        }
    }
    
    @Override
    public void add(int index, E e) {
        add(e);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c != null && c.size() > 0) {
            ensureCapacity(size + c.size());
            
            Object[] cArr = c.toArray();
            
            MergeSort.sort(
                cArr,
                (o1, o2) -> comparator.compare((E)o1, (E)o2)
            );
            
            int minIndex = findInsertionIndex((E)cArr[0]), maxIndex = findInsertionIndex((E)cArr[cArr.length-1]);

            System.arraycopy(elements, maxIndex, elements, maxIndex + cArr.length, size - maxIndex);
            
            int c1 = cArr.length-1, c2 = maxIndex-1, currentIndex = maxIndex + cArr.length - 1;
            
            while (currentIndex >= minIndex) {
                if (c1 < 0) {
                    elements[currentIndex--] = elements[c2--];
                } else if (c2 < minIndex) {
                    elements[currentIndex--] = cArr[c1--];
                } else {
                    E e1 = (E)cArr[c1], e2 = (E)elements[c2];
                    
                    int cr = comparator.compare(e1, e2);
                    
                    if (cr >= 0) {
                        elements[currentIndex--] = cArr[c1--];
                    } else {
                        elements[currentIndex--] = elements[c2--];
                    }
                }
            }
            
            size += cArr.length;
            
            modCount++;
            
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c, boolean allowDuplicate) {
        boolean modified = false;
        for (E e : c) {
            if (allowDuplicate || !contains(e)) {
                if (add(e)) {
                    modified = true;
                }
            }
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return addAll(c);
    }

    @Override
    public void clear() {
        size = 0;
        modCount++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        try {
            E e = (E) o;
            return findIndex(e) != -1;
        } catch (ClassCastException cce) {
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private int findIndex(E e) {
        int min = 0, max = size - 1;
        
        while (min <= max) {
            int mid = (max + min) >>> 1;
            int comp = comparator.compare(e, (E) elements[mid]);
            if (comp < 0) {
                max = mid - 1;
            } else if (comp > 0) {
                min = mid + 1;
            } else {
                return mid;
            }
        }
        
        return -1;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elements[index];
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public int indexOf(Object o) {
        try {
            E e = (E) o;
            int foundIndex = findIndex(e);
            
            if (foundIndex != -1) {
                for (int i = foundIndex-1; i >= 0; i--) {
                    if (comparator.compare(e, (E) elements[i]) == 0) {
                        foundIndex = i;
                    } else {
                        return foundIndex;
                    }
                }
                return foundIndex;
            }
            
            return foundIndex;
        } catch (ClassCastException cce) {
            return -1;
        }
    }

    @Override
    // Same as SortedList's default implementation, seems java does not accept kotlin interface's default implementation
    public boolean add(E element, boolean allowDuplicate) {
        if (allowDuplicate || !contains(element)) {
            return add(element);
        } else {
            return false;
        }
    }

    @Override
    // Same as SortedList's default implementation, seems java does not accept kotlin interface's default implementation
    public void add(int index, E element, boolean allowDuplicate) {
        // Index is ignored as this is a sortedList
        add(element, allowDuplicate);
    }

    @Override
    public int greaterIndexOf(E value) {
        if (size == 0) {
            System.out.println("from here 0");
            return -1;
        }
        
        int min = 0, max = size - 1;

        while (min <= max) {
            int mid = (max + min) >>> 1;
            int comp = comparator.compare(value, (E) elements[mid]);

            if (comp >= 0) {
                min = mid + 1;
            } else {
                int foundIndex = mid;

                max = mid - 1;

                while (min <= max) {
                    int newMid = (max + min) >>> 1;
                    int newComp = comparator.compare(value, (E) elements[newMid]);

                    if (newComp < 0) {
                        foundIndex = newMid;
                        max = newMid - 1;
                    } else {
                        min = newMid + 1;
                    }
                }
                
                return foundIndex;
            }
        }
        
        return -1;
    }

    @Override
    public int greaterOrEqualsIndexOf(E value) {
        if (size == 0) {
            return -1;
        }

        int min = 0, max = size - 1;

        while (min <= max) {
            int mid = (max + min) >>> 1;
            int comp = comparator.compare(value, (E) elements[mid]);

            if (comp > 0) {
                min = mid + 1;
            } else {
                int foundIndex = mid;

                max = mid - 1;

                while (min <= max) {
                    int newMid = (max + min) >>> 1;
                    int newComp = comparator.compare(value, (E) elements[newMid]);

                    if (newComp <= 0) {
                        foundIndex = newMid;
                        max = newMid - 1;
                    } else {
                        min = newMid + 1;
                    }
                }

                return foundIndex;
            }
        }

        return -1;
    }

    @Override
    public int smallerIndexOf(E value) {
        if (size == 0) {
            return -1;
        }

        int min = 0, max = size - 1;

        while (min <= max) {
            int mid = (max + min) >>> 1;
            int comp = comparator.compare(value, (E) elements[mid]);

            if (comp <= 0) {
                max = mid - 1;
            } else {
                int foundIndex = mid;

                min = mid + 1;

                while (min <= max) {
                    int newMid = (max + min) >>> 1;
                    int newComp = comparator.compare(value, (E) elements[newMid]);

                    if (newComp > 0) {
                        foundIndex = newMid;
                        min = newMid + 1;
                    } else {
                        max = newMid - 1;
                    }
                }

                return foundIndex;
            }
        }

        return -1;
    }

    @Override
    public int smallerOrEqualsIndexOf(E value) {
        if (size == 0) {
            return -1;
        }

        int min = 0, max = size - 1;

        while (min <= max) {
            int mid = (max + min) >>> 1;
            int comp = comparator.compare(value, (E) elements[mid]);

            if (comp < 0) {
                max = mid - 1;
            } else {
                int foundIndex = mid;

                min = mid + 1;

                while (min <= max) {
                    int newMid = (max + min) >>> 1;
                    int newComp = comparator.compare(value, (E) elements[newMid]);

                    if (newComp >= 0) {
                        foundIndex = newMid;
                        min = newMid + 1;
                    } else {
                        max = newMid - 1;
                    }
                }

                return foundIndex;
            }
        }

        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int lastIndexOf(Object o) {
        try {
            E e = (E) o;
            int foundIndex = findIndex(e);
            
            if (foundIndex != -1) {
                for (int i = foundIndex+1; i < size; i++) {
                    if (comparator.compare(e, (E) elements[i]) == 0) {
                        foundIndex = i;
                    } else {
                        return foundIndex;
                    }
                }
                return foundIndex;
            }
            
            return foundIndex;
        } catch (ClassCastException cce) {
            return -1;
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return new SortedListIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new SortedListIterator(index);
    }
    
    @SuppressWarnings("unchecked")
    private E removeElement(int index) {
        E removed = (E) elements[index];
        
        int len = size - index - 1;
        if (len > 0) {
            System.arraycopy(elements, index+1, elements, index, len);
        }
        
        size--;
        return removed;
    }
    
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index == -1) {
            return false;
        } else {
            modCount++;
            removeElement(index);
            return true;
        }
    }

    @Override
    public E remove(int index) {
        modCount++;
        checkIndex(index);
        return removeElement(index);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        List<Integer> containedIndexes = new ArrayList<Integer>();
        
        for (Object o : c) {
            int index = indexOf(o);
            if (index != -1) {
                containedIndexes.add(index);
            }
        }
        
        if (containedIndexes.size() == 0) {
            return false;
        } else {
            Collections.sort(containedIndexes);
            
            int index = containedIndexes.get(0), currentIndex = index;
            for (int i = 1; i < containedIndexes.size(); i++) {
                int nIndex = containedIndexes.get(i);
                
                for (int j = index+1; j < nIndex; j++) {
                    elements[currentIndex++] = elements[j];
                }
                
                index = nIndex;
            }
            
            for (int j = index+1; j < elements.length; j++) {
                elements[currentIndex++] = elements[j];
            }
            
            size -= containedIndexes.size();
            
            modCount++;
            return true;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        SortedListArray<Integer> containedIndexes = new SortedListArray<Integer>();
        
        for (Object o : c) {
            int index = indexOf(o), lastIndex = lastIndexOf(o);
            if (index != -1) {
                for (int i = index; i <= lastIndex; i++) {
                    if (!containedIndexes.contains(i)) {
                        containedIndexes.add(i);
                    }
                }
            }
        }
        
        if (containedIndexes.size() == 0) {
            size = 0;
            return true;
        } else if (containedIndexes.size() == size) {
            return false;
        } else {
            for (int i = 0; i < containedIndexes.size(); i++) {
                elements[i] = elements[containedIndexes.get(i)];
            }
            
            size = containedIndexes.size();
            
            modCount ++;
            return true;
        }
    }

    @Override
    public E set(int index, E element) {
        checkIndex(index);
        
        E e = removeElement(index);
        insert(findInsertionIndex(element), element);
        return e;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("SubList is not supported!");
    }
    
    @Override
    public SortedListArray<E> getSubList(int fromIndex, int toIndex) {
        SortedListArray<E> sub = new SortedListArray<E>(comparator);
        
        int len = toIndex - fromIndex;
        sub.ensureCapacity(len);
        
        System.arraycopy(elements, fromIndex, sub.elements, 0, len);
        
        sub.size = len;
        
        return sub;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(elements, 0, array, 0, size);
        return array;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] a) {
        for (int i = 0; i < a.length && i < size; i++) {
            a[i] = (T) elements[i];
        }
        return a;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public E getMin() {
        return (size == 0)? null : (E) elements[0];
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public E getMax() {
        return (size > 0)? (E) elements[size-1] : null;
    }
    
    @Override
    public void resort() {
        MultiSort.sort(
            elements, (o1, o2) -> comparator.compare((E) o1, (E) o2),0,size-1
        );
    }
    
    @Override
    public SortedListArray<E> clone() {
        SortedListArray<E> clone = new SortedListArray<E>(comparator);
        clone.ensureCapacity(size);
        System.arraycopy(elements, 0, clone.elements, 0, size);
        clone.size = size;
        return clone;
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    private class SortedListIterator implements ListIterator<E> {
        private int previousIndex, nextIndex, currentIndex;
        private boolean canRemove = false;
        private int expectedModCount = modCount;
        
        public SortedListIterator() {
            nextIndex = 0;
            currentIndex = -1;
            previousIndex = -1;
        }
        
        public SortedListIterator(int i) {
            checkIndex(i);
            nextIndex = i;
            previousIndex = i - 1;
            currentIndex = -1;
        }
        
        private void checkForComodification() {
            if (expectedModCount != modCount) {
                throw new ConcurrentModificationException();
            }
        }
        
        @Override
        public void add(E e) {
            SortedListArray.this.add(e);
            int index = indexOf(e);
            if (index <= currentIndex) {
                currentIndex += 1;
                nextIndex = currentIndex + 1;
                previousIndex = currentIndex - 1;
            }
            canRemove = true;
            expectedModCount++;
        }
        
        @Override
        public boolean hasNext() {
            return nextIndex < size();
        }

        @Override
        public boolean hasPrevious() {
            return previousIndex >= 0;
        }
        
        @Override
        public E next() {
            checkForComodification();
            if (nextIndex < size()) {
                canRemove = true;
                currentIndex = nextIndex;
                E element = get(nextIndex++);
                previousIndex = nextIndex - 1;
                return element;
            } else {
                throw new NoSuchElementException("No next element");
            }
        }

        @Override
        public int nextIndex() {
            if (nextIndex < size()) {
                return nextIndex;
            } else {
                return size();
            }
        }
        
        @Override
        public E previous() {
            checkForComodification();
            if (previousIndex >= 0) {
                currentIndex = previousIndex;
                E element = get(previousIndex--);
                nextIndex = previousIndex + 1;
                canRemove = true;
                return element;
            } else {
                throw new NoSuchElementException("No previous element (previousIndex: " + previousIndex + ")");
            }
        }

        @Override
        public int previousIndex() {
            if (previousIndex >= 0) {
                return previousIndex;
            } else {
                return -1;
            }
        }
        
        @Override
        public void remove() {
            if (!canRemove || currentIndex < 0 || currentIndex >= size()) {
                throw new IllegalStateException("Nothing to remove (currentIndex: "+ currentIndex + ")");
            } else {
                checkForComodification();
                SortedListArray.this.remove(currentIndex);
                nextIndex = currentIndex;
                previousIndex = currentIndex - 1;
                currentIndex = -1;
                canRemove = false;
                expectedModCount++;
            }
        }
        
        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("Set operation is not supported (this is a sorted list)!");
        }
    }
}
