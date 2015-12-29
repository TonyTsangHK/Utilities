package utils.data;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ImmutableList<E> implements List<E> {
    private transient Object[] elements;
    
    public ImmutableList() {
        elements = new Object[0];
    }
    
    public ImmutableList(E ... elements) {
        this.elements = Arrays.copyOf(elements, elements.length);
    }
    
    public ImmutableList(Collection<? extends E> elements) {
        if (elements != null && elements.size() > 0) {
            this.elements = new Object[elements.size()];
            int i = 0;
            for (E element : elements) {
                this.elements[i++] = element;
            }
        } else {
            this.elements = new Object[0];
        }
    }
    
    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Immutable"); 
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Immutable");
    }
    
    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> iter = c.iterator();
        
        while (iter.hasNext()) {
            if (!contains(iter.next())) {
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
    
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < elements.length; i++) {
                if (elements[i]==null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < elements.length; i++) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator(0);
    }

    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = elements.length-1; i >= 0; i--) {
                if (elements[i]==null) {
                    return i;
                }
            }
        } else {
            for (int i = elements.length-1; i >= 0; i--) {
                if (o.equals(elements[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListIter(index);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Immutable"); 
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Immutable");
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return new ImmutableSubList<E>(this, fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, elements.length);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < elements.length) {
            return (T[]) Arrays.copyOf(elements, elements.length, a.getClass());
        }
        System.arraycopy(elements.length, 0, a, 0, elements.length);
        if (a.length > elements.length) {
            a[elements.length] = null;
        }
        return a;
    }
    
    private class ListIter implements ListIterator<E> {
        private int cur = -1;
        
        private ListIter(int index) {
            this.cur = index;
        }
        
        @Override
        public void add(E e) {
            throw new UnsupportedOperationException("Immutable");
        }

        @Override
        public boolean hasNext() {
            return cur != elements.length;
        }

        @Override
        public boolean hasPrevious() {
            return cur != 0;
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            E next = (E)elements[cur++];
            return next;
        }
        
        @Override
        public int nextIndex() {
            return cur;
        }

        @Override
        public E previous() {
            int i = cur - 1;
            E previous = get(i);
            cur = i;
            return previous;
        }

        @Override
        public int previousIndex() {
            return cur - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Immutable");
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("Immutable");
        }
    }
}

class ImmutableSubList<E> extends ImmutableList<E> {
    private List<E> l;
    private int offset;
    private int size;
    
    ImmutableSubList(List<E> list, int fromIndex, int toIndex) {
        super();
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        }
        if (toIndex > list.size()) {
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        }
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException(
                    "fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")"
            );
        }
        l = list;
        offset = fromIndex;
        size = toIndex - fromIndex;
    }

    public E set(int index, E element) {
        throw new UnsupportedOperationException("Immutable");
    }

    public E get(int index) {
        rangeCheck(index);
        return l.get(index+offset);
    }

    public int size() {
        return size;
    }

    public void add(int index, E element) {
        throw new UnsupportedOperationException("Immutable");
    }

    public E remove(int index) {
        throw new UnsupportedOperationException("Immutable");
    }

    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Immutable");
    }

    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Immutable");
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Immutable");
    }

    public Iterator<E> iterator() {
        return listIterator();
    }

    public ListIterator<E> listIterator(final int index) {
        if (index<0 || index>size) {
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size);
        }

        return new ListIterator<E>() {
            private ListIterator<E> i = l.listIterator(index+offset);
            
            public boolean hasNext() {
                return nextIndex() < size;
            }
            
            public E next() {
                if (hasNext())
                    return i.next();
                else
                    throw new NoSuchElementException();
            }
            
            public boolean hasPrevious() {
                return previousIndex() >= 0;
            }
            
            public E previous() {
                if (hasPrevious())
                    return i.previous();
                else
                    throw new NoSuchElementException();
            }
            
            public int nextIndex() {
                return i.nextIndex() - offset;
            }
            
            public int previousIndex() {
                return i.previousIndex() - offset;
            }
            
            public void remove() {
                throw new UnsupportedOperationException("Immutable");
            }
            
            public void set(E e) {
                throw new UnsupportedOperationException("Immutable");
            }
            
            public void add(E e) {
                throw new UnsupportedOperationException("Immutable");
            }
        };
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return new ImmutableSubList<E>(this, fromIndex, toIndex);
    }

    private void rangeCheck(int index) {
        if (index<0 || index>=size) {
            throw new IndexOutOfBoundsException("Index: " + index + ",Size: "+size);
        }
    }
}