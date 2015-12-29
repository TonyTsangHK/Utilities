package utils.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListChainer <E> {
    private List<E> list;
    
    public ListChainer() {
        list = new ArrayList<E>();
    }
    
    public ListChainer(int initialCapacity) {
        list = new ArrayList<E>(initialCapacity);
    }
    
    public ListChainer(Collection<? extends E> elements) {
        list = new ArrayList<E>(elements);
    }
    
    public ListChainer(List<E> existingList) {
        this.list = existingList;
    }
    
    public ListChainer<E> add(E element) {
        list.add(element);
        return this;
    }

    public ListChainer<E> addNonNull(E element) {
        if (element != null) {
            list.add(element);
        }

        return this;
    }
    
    public ListChainer<E> add(E ... elements) {
        for (E e : elements) {
            list.add(e);
        }
        return this;
    }

    public ListChainer<E> addNonNull(E ... elements) {
        for (E e : elements) {
            if (e != null) {
                list.add(e);
            }
        }

        return this;
    }
    
    public ListChainer<E> add(int i, E element) {
        list.add(i, element);
        return this;
    }

    public ListChainer<E> add(int i, E element, boolean filterNull) {
        if (!filterNull || element != null) {
            list.add(i, element);
        }

        return this;
    }
    
    public ListChainer<E> addAll(Collection<? extends E> elements) {
        list.addAll(elements);
        return this;
    }
    
    public ListChainer<E> addAll(int i, Collection<? extends E> elements) {
        list.addAll(i, elements);
        return this;
    }


    
    public ListChainer<E> set(int i, E element) {
        list.set(i, element);
        return this;
    }

    public ListChainer<E> set(int i, E element, boolean filterNull) {
        if (!filterNull || element != null) {
            list.set(i, element);
        }
        return this;
    }
    
    public ListChainer<E> remove(int i) {
        list.remove(i);
        return this;
    }
    
    public ListChainer<E> remove(E element) {
        list.remove(element);
        return this;
    }
    
    public ListChainer<E> removeAll(Collection<? extends E> elements) {
        list.removeAll(elements);
        return this;
    }
    
    public ListChainer<E> retainAll(Collection<? extends E> elements) {
        list.retainAll(elements);
        return this;
    }
    
    public List<E> getList() {
        return list;
    }
}
