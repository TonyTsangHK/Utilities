package utils.data;

import java.io.*;
import java.util.*;

import utils.constants.MathValueType;

class MinMaxQueue<T>: Iterable<T>, Serializable {
    private val elements: SortedListAvl<T>
    
    var oversizeOutType = MathValueType.MIN
        private set
    
    var size: Int = -1
        set(value) {
            field = size
            if (field > 0 && elements.size > field) {
                while (elements.size > field) {
                    if (oversizeOutType == MathValueType.MIN) {
                        pollMin()
                    } else {
                        pollMax()
                    }
                }
            }
        }
        
    
    constructor(): this(-1, MathValueType.MIN, MinMaxQueue.DefaultComparator<T>())
    
    constructor(comparator: Comparator<T>): this(-1, MathValueType.MIN, comparator)
    
    constructor(maxSize: Int, oversizeOutType: MathValueType): this(maxSize, oversizeOutType, DefaultComparator<T>())
    
    constructor(maxSize: Int, oversizeOutType: MathValueType, comparator: Comparator<T>) {
        elements = SortedListAvl<T>(comparator)
        size = maxSize
        this.oversizeOutType = oversizeOutType
    }
    
    fun add(vararg values: T) {
        for (value in values) {
            add(value)
        }
    }
    
    fun add(element: T) {
        if (size > 0) {
            elements.add(element);
            if (oversizeOutType == MathValueType.MIN) {
                elements.pollMin();
            } else {
                elements.pollMax();
            }
        } else {
            elements.add(element);
        }
    }
    
    fun addAll(otherQueue: MinMaxQueue<T>) {
        for (i in 0 .. otherQueue.size - 1) {
            add(otherQueue.get(i))
        }
    }
    
    fun addAll(values: Collection<T>) {
        for (value: T in values) {
            add(value)
        }
    }
    
    fun pollMin(): T {
        return elements.pollMin()
    }
    
    fun pollMax(): T {
        return elements.pollMax()
    }
    
    fun peekMin(): T {
        return elements.getMin()
    }
    
    fun peekMax(): T {
        return elements.getMax()
    }
    
    fun peekIndex(index: Int): T? {
        if (index >= 0 && index < elements.size) {
            return elements.get(index)
        } else {
            return null
        }
    }
    
    fun getMin(): T {
        return peekMin()
    }
    
    fun getMax(): T {
        return peekMax()
    }
    
    fun get(index: Int): T {
        return elements.get(index)
    }
    
    fun remove(element: T): Boolean {
        return elements.remove(element)
    }
    
    fun removeAt(index: Int): T {
        return elements.removeAt(index);
    }
    
    fun indexOf(element: T): Int {
        return elements.indexOf(element)
    }
    
    fun removeFirst(): T {
        return pollMin()
    }
    
    fun removeMin(): T {
        return pollMin()
    }
    
    fun removeLast(): T {
        return pollMax()
    }
    
    fun removeMax(): T {
        return pollMax()
    }
    
    fun clear() {
        elements.clear()
    }
    
    fun getList(): SortedListAvl<T> {
        return elements.clone()
    }
    
    fun reorder() {
        elements.resort()
    }
    
    fun isEmpty(): Boolean {
        return elements.isEmpty()
    }
    
    fun isNotEmpty(): Boolean {
        return elements.isNotEmpty()
    }

    override fun iterator(): Iterator<T> {
        return elements.listIterator()
    }
    
    class DefaultComparator<T>: Comparator<T> {
        override fun compare(t1: T, t2: T): Int {
            if (t1 is Comparable<*>) {
                val result = (t1 as Comparable<T>).compareTo(t2); 
                return result
            } else {
                throw NotComparableException("Not comparable exception")
            }
        }
    }
}