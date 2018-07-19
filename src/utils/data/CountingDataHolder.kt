package utils.data

import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-11-07
 * Time: 09:37
 * 
 * 
 */
class CountingDataHolder<E> {
    private val dataMap = HashMap<E, Int>()
    private val valueListMap = TreeMap<Int, MutableList<E>>()

    private fun refreshValueListMap(value: E, originalCounter: Int, newCounter: Int) {
        // Negative counter means no original counter
        if (originalCounter >= 0) {
            valueListMap[originalCounter]!!.remove(value)
        }

        // Only store positive counted values
        if (newCounter > 0) {
            if (valueListMap.containsKey(newCounter)) {
                valueListMap[newCounter]!! += value
            } else {
                valueListMap[newCounter] = arrayListOf(value)
            }
        }
    }

    /**
     * Adjust counter of target value
     * 
     * @param value target value
     * @param adjustment adjustment, positive for increasing counter, negative for decreasing counter
     */
    fun adjustCounter(value: E, adjustment: Int) {
        if (adjustment > 0) {
            val originalCounter: Int
            val newCounter: Int

            if (dataMap.containsKey(value)) {
                originalCounter = dataMap[value]!!
                newCounter = originalCounter + adjustment
            } else {
                newCounter = adjustment
                originalCounter = -1
            }

            refreshValueListMap(value, originalCounter, newCounter)

            dataMap[value] = newCounter
        } else if (adjustment < 0) {
            // Ignore non-exists value for decrement
            if (dataMap.containsKey(value)) {
                val originalCounter = dataMap[value]!!
                val newCounter = originalCounter + adjustment

                
                // Counter will never be negative
                if (newCounter > 0) {
                    refreshValueListMap(value, originalCounter, newCounter)
                    dataMap[value] = newCounter
                } else if (newCounter <= 0) {
                    refreshValueListMap(value, originalCounter, newCounter)
                    
                    // Remove zero counted value, since zero count means every value that is not counted, which is meaningless
                    dataMap.remove(value)
                }
            }
        }
    }

    /**
     * Increment value's counter
     * 
     * @param value target value
     */
    fun incrementCounter(value: E) {
        adjustCounter(value, 1)
    }

    /**
     * Decrement value's counter
     * 
     * @param value target value
     */
    fun decrementCounter(value: E) {
        adjustCounter(value, -1)
    }

    /**
     * Get maximum counter
     * 
     * @return maximum counter, zero for empty data map
     */
    fun getMaximumCounter(): Int {
        if (dataMap.isEmpty()) {
            // Zero means nothing counted
            return 0
        } else {
            return valueListMap.lastKey()
        }
    }

    /**
     * Get minimum counter
     * 
     * @return minimum counter, zero for empty data map
     */
    fun getMinimumCounter(): Int {
        if (dataMap.isEmpty()) {
            // Zero means nothing counted
            return 0
        } else {
            return valueListMap.firstKey()
        }
    }

    /**
     * Get majority values, which is most counted values
     * 
     * @return majority values
     */
    fun getMajorityValues(): List<E>? {
        if (dataMap.isEmpty()) {
            return null
        } else {
            return valueListMap.lastEntry().value
        }
    }

    /**
     * Get minority values, which is least counted values
     * 
     * @return minority values
     */
    fun getMinorityValues(): List<E>? {
        if (dataMap.isEmpty()) {
            return null
        } else {
            return valueListMap.firstEntry().value
        }
    }

    /**
     * Get all values
     */
    fun values(): Collection<E> {
        return dataMap.keys
    }

    /**
     * Check if value exists
     * 
     * @param value target value
     * @return existence of value
     */
    fun containsValue(value: E): Boolean {
        return dataMap.containsKey(value)
    }

    /**
     * Get all counters
     */
    fun getCounters(): Set<Int> {
        return valueListMap.keys
    }

    /**
     * Get counter of target value
     * 
     * @param value target value
     * @return counter of target value
     */
    fun getCounter(value: E): Int {
        if (dataMap.containsKey(value)) {
            return dataMap[value]!!
        } else {
            // Always return zero for non existing values
            return 0
        }
    }

    /**
     * Reset counting values
     */
    fun reset() {
        dataMap.clear()
        valueListMap.clear()
    }

    /**
     * Check whether this holder is empty
     * 
     * @return check result
     */
    fun isEmpty(): Boolean {
        return dataMap.isEmpty()
    }

    /**
     * Check whether this holder is not empty
     * 
     * @return check result
     */
    fun isNotEmpty(): Boolean {
        return dataMap.isNotEmpty()
    }
}