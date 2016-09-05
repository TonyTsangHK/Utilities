package utils.data;

import java.io.*;
import java.util.*;

import utils.constants.MathValueType;

public class MinMaxQueue<T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    
    private SortedListAvl<T> elements;
    
    private MathValueType oversizeOutType = MathValueType.MIN;
    
    private int queueSize = -1;
    
    public MinMaxQueue() {
        this(-1, MathValueType.MIN, new MinMaxQueue.DefaultComparator<T>());
    }
    
    public MinMaxQueue(Comparator<T> comparator) {
        this(-1, MathValueType.MIN, comparator);
    }
    
    public MinMaxQueue(int maxSize, MathValueType oversizeOutType) {
        this(maxSize, oversizeOutType, new DefaultComparator<T>());
    }
    
    public MinMaxQueue(int maxSize, MathValueType oversizeOutType, Comparator<T> comparator) {
        elements = new SortedListAvl<T>(comparator);
        queueSize = maxSize;
        setOversizeOutType(oversizeOutType);
    }
    
    public void add(T ... values) {
        for (T value : values) {
            add(value);
        }
    }
    
    public void add(T o) {
        if (queueSize > 0 && size() == queueSize) {
            elements.add(o);
            if (oversizeOutType == MathValueType.MIN) {
                elements.pollMin();
            } else {
                elements.pollMax();
            }
        } else {
            elements.add(o);
        }
    }
    
    public void addAll(MinMaxQueue<T> otherQueue) {
        if (otherQueue != null) {
            for (int i = 0; i < otherQueue.size(); i++) {
                add(otherQueue.get(i));
            }
        }
    }
    
    public void addAll(Collection<T> values) {
        for (T value : values) {
            add(value);
        }
    }
    
    public int size() {
        return elements.size();
    }
    
    public T pollMin() {
        return elements.pollMin();
    }
    
    public T pollMax() {
        return elements.pollMax();
    }
    
    public T peekMin() {
        return elements.getMin();
    }
    
    public T peekMax() {
        return elements.getMax();
    }
    
    public T peekIndex(int i) {
        if (i >= 0 && i < elements.size()) {
            return elements.get(i);
        } else {
            return null;
        }
    }
    
    public T getMin() {
        return peekMin();
    }
    
    public T getMax() {
        return peekMax();
    }
    
    public T get(int i) {
        return elements.get(i);
    }
    
    public boolean remove(T t) {
        return elements.remove(t);
    }
    
    public T remove(int i) {
        return removeAt(i);
    }

    public T removeAt(int i) {
        return elements.removeAt(i);
    }
    
    public int indexOf(T o) {
        return elements.indexOf(o);
    }
    
    public T removeFirst() {
        return pollMin();
    }
    
    public T removeMin() {
        return pollMin();
    }
    
    public T removeLast() {
        return pollMax();
    }
    
    public T removeMax() {
        return pollMax();
    }
    
    public void clear() {
        elements.clear();
    }
    
    public void setQueueSize(int qs) {
        queueSize = qs;
        if (queueSize > 0 && size() > queueSize) {
            while (size() > queueSize) {
                if (oversizeOutType == MathValueType.MIN) {
                    pollMin();
                } else {
                    pollMax();
                }
            }
        }
    }
    
    public void setOversizeOutType(MathValueType oversizeOutType) {
        if (oversizeOutType != null) {
            this.oversizeOutType = oversizeOutType;
        }
    }
    
    public int getQueueSize() {
        return queueSize;
    }
    
    public MathValueType getOversizeOutType() {
        return oversizeOutType;
    }
    
    public SortedListAvl<T> getList() {
        return elements.clone();
    }
    
    public void reorder() {
        elements.resort();
    }
    
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public Iterator<T> iterator() {
        return elements.listIterator();
    }
    
    private static class DefaultComparator<T> implements Comparator<T> {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public int compare(T t1, T t2) {
            if (t1 instanceof Comparable) {
                int result = ((Comparable)t1).compareTo(t2); 
                return result;
            } else {
                throw new NotComparableException("Not comparable exception");
            }
        }
    }
}