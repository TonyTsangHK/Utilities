package utils.data

import java.io.Serializable

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-01-03
 * Time: 10:03
 */
interface SortedList<E>: Iterable<E>, MutableList<E>, Cloneable, Serializable {
    /**
     * Get the minimum element of the list
     * 
     * @return minimum element of the list, null if empty
     */
    fun getMin(): E?

    /**
     * Get the maximum element of the list
     * 
     * @return maximum element of the list, null if empty
     */
    fun getMax(): E?

    /**
     * Add an element into this list with duplication control flag
     *
     * @param e element to be added
     * @param allowDuplicate duplication control flag, if false, duplicate element will be ignored
     * @return true as successful, false as unsuccessful
     */
    fun add(element: E, allowDuplicate: Boolean): Boolean {
        if (allowDuplicate || !contains(element)) {
            return add(element)
        } else {
            return false
        }
    }

    /**
     * Simply redirect to add(E, boolean), index is ignored as this is a sorted list
     *
     * @param index ignored
     * @param element data element
     * @param allowDuplicate allow for duplication, if false, duplicate element will be ignored
     */
    fun add(index: Int, element: E, allowDuplicate: Boolean) {
        // Index is ignored as this is a sortedList
        add(element, allowDuplicate)
    }

    /**
     * Add the provided data collection with duplication control flag
     *
     * @param c data collection
     * @param allowDuplicate duplication control flag, false to ignore duplicated elements
     * @return true as successful (at least one element is successfully added), false otherwise
     */
    fun addAll(elements: Collection<E>, allowDuplicate: Boolean): Boolean

    /**
     * Programatically trigger element sorting of this list
     */
    fun resort()

    /**
     * Create cloned list, override to change function modifier and return type
     */
    override public fun clone(): SortedList<E>

    /**
     * Get a sublist of this list, independent clone
     * 
     * @param fromIndex starting index of the sublist, inclusive
     * @param toIndex ending index of the sublist, exclusive
     *
     * @return new list consisting elements provided by from & to index
     */
    fun getSubList(fromIndex: Int, toIndex: Int): SortedList<E>

    /**
     * Find the first element index which value is greater than the searching value
     *
     * @param value searching value
     *
     * @return first element index with greater value of the searching value
     */
    fun greaterIndexOf(value: E): Int

    /**
     * Find the last element index which value is smaller than the searching value
     *
     * @param value searching value
     *
     * @return last element index with smaller value of the searching value
     */
    fun smallerIndexOf(value: E): Int

    /**
     * Find the first element index which value is greater or equals to the searching value
     *
     * @param value searching value
     *
     * @return first element index with greater or equals value of the searching value
     */
    fun greaterOrEqualsIndexOf(value: E): Int

    /**
     * Find the last element index which value is smaller or equals to the searching value
     *
     * @param value searching value
     *
     * @return last element index with smaller or equals value of the searching value
     */
    fun smallerOrEqualsIndexOf(value: E): Int
}