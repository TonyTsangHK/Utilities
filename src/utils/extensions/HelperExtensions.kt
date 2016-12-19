package utils.extensions

import utils.data.DataManipulator

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-11-11
 * Time: 09:50
 */
/**
 * Check index is within range
 * 
 * @param idx index to check
 * @return check result
 */
fun <E> List<E>.checkIndex(idx: Int): Boolean = idx >= 0 && idx < this.size

/**
 * Check all indices is within range
 * 
 * @param indices indices to check
 * @return check result
 */
fun <E> List<E>.checkIndices(vararg indices: Int): Boolean = indices.all { checkIndex(it) }

/**
 * Check index is within range
 * 
 * @param index index to check
 * @return check result
 */
fun <E> Array<E>.checkIndex(idx: Int): Boolean = idx >= 0 && idx < this.size

/**
 * Check all indices is within range
 * 
 * @param indices indices to check
 * @return check result
 */
fun <E> Array<E>.checkIndices(vararg indices: Int): Boolean = indices.all { checkIndex(it) }

/**
 * Swap data on one index to another index
 * 
 * @param idx1 first index
 * @param idx2 second index
 * @return swap result, success: true, failure: false
 */
fun <E> MutableList<E>.swapData(idx1: Int, idx2: Int): Boolean {
    if (checkIndices(idx1, idx2)) {
        if (idx1 != idx2) {
            val element = this[idx1]
            this[idx1] = this[idx2]
            this[idx2] = element
        }
        return true
    } else {
        return false
    }
}

/**
 * Swap data on one index to another index
 * 
 * @param idx1 first index
 * @param idx2 second index
 * @return swap result, success: true, failure false
 */
fun <E> Array<E>.swapData(idx1: Int, idx2: Int): Boolean {
    if (checkIndices(idx1, idx2)) {
        if (idx1 != idx2) {
            val element = this[idx1]
            this[idx1] = this[idx2]
            this[idx2] = element
        }
        return true
    } else {
        return false
    }
}

/**
 * Bubbling an element to target index
 * 
 * @param fromIdx target element index
 * @param toIdx target destination index
 * @return result, success: true, failure false
 */
fun <E> MutableList<E>.moveData(fromIdx: Int, toIdx: Int): Boolean = DataManipulator.moveData(this, fromIdx, toIdx)

/**
 * Bubbling an element to target index
 *
 * @param fromIdx target element index
 * @param toIdx target destination index
 * @return result, success: true, failure false
 */
fun <E> Array<E>.moveData(fromIdx: Int, toIdx: Int): Boolean = DataManipulator.moveData(this, fromIdx, toIdx)

/**
 * Bubbling a range of elements to target index
 * 
 * @param start start index of target range
 * @param end end index of target range
 * @param to target destination index
 */
fun <E> MutableList<E>.moveDatas(start: Int, end: Int, to: Int) {
    DataManipulator.moveDatas(this, start, end, to)
}

/**
 * Bubbling a range of elements to target index
 *
 * @param start start index of target range
 * @param end end index of target range
 * @param to target destination index
 */
fun <E> Array<E>.moveDatas(start: Int, end: Int, to: Int) {
    DataManipulator.moveDatas(this, start, end, to)
}

/**
 * Shuffle all elements
 * 
 * @param start start index, default to first index
 * @param end end index, default to last index
 */
fun <E> MutableList<E>.shuffle(start: Int = 0, end: Int = this.size-1) {
    DataManipulator.shuffle(this, start, end)
}

/**
 * Shuffle all elements
 *
 * @param start start index, default to first index
 * @param end end index, default to last index
 */
fun <E> Array<E>.shuffle(start: Int = 0, end: Int = this.size - 1) {
    DataManipulator.shuffle(this, start, end)
}

/**
 * vararg form of MutableList.addAll function
 */
fun <E> MutableList<E>.addAll(vararg elements: E) {
    for (e in elements) {
        this.add(e)
    }
}

/**
 * vararg form of MutableList.removeAll function
 */
fun <E> MutableList<E>.removeAll(vararg elements: E) {
    for (e in elements) {
        this.remove(e)
    }
}

/**
 * Check map contains all provided keys
 * 
 * @param keys keys to check
 * @return check result
 */
fun <K, V> Map<K, V>.containsAllKeys(vararg keys: K): Boolean = keys.all { this.containsKey(it) }

/**
 * Check map contains any provided keys
 * 
 * @param keys keys to check
 * @return check result
 */
fun <K, V> Map<K, V>.containsAnyKeys(vararg keys: K): Boolean = keys.any { this.containsKey(it) }

/**
 * Find minimum value of the value collection
 * 
 * @exception IllegalArgumentException if collection is empty
 */
fun <E: Comparable<E>> Collection<E>.findMin(): E = DataManipulator.findMin(this)

/**
 * Find maximum value of the value collection
 * 
 * @exception IllegalArgumentException if collection is empty
 */
fun <E: Comparable<E>> Collection<E>.findMax(): E = DataManipulator.findMax(this)