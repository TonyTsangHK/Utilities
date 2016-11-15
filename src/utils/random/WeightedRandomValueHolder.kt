package utils.random

import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-11-15
 * Time: 17:18
 */
/**
 * Weighted random value holder
 *
 */
class WeightedRandomValueHolder<E> {
    /**
     * total possible weight
     *
     * setter is set to private to ensure consistency of total weight (inconsistent total weight will be adjusted during addWeightedRandomValue)
     */
    var totalWeight: Int
        private set(value) {
            field = value
        }

    /**
     * default value to return, if generated weight value exceed all weight intervals
     *
     * This can be view as weighted value with weight = totalWeight - sum of other weighted value
     */
    val defaultValue: E

    /**
     * Weighted value list
     */
    val weightedRandomValueList = ArrayList<WeightedRandomValue<E>>()

    /**
     * Weight intervals to determine value with provided weight
     */
    private val weightIntervals = ArrayList<Int>()

    /**
     * construct a WeightedRandomValueHolder
     *
     * @param totalWeight total weight of the holder, if this is zero defaultValue will never be generated.
     * @param defaultValue default value, value to return if generated weight value exceed all other weighted values
     * @param weightedRandomValues all other weighted values
     */
    constructor(totalWeight: Int, defaultValue: E, vararg weightedRandomValues: WeightedRandomValue<E>) {
        this.totalWeight = totalWeight
        this.defaultValue = defaultValue
        addWeightedRandomValues(*weightedRandomValues)
    }

    /**
     * Add weighted values
     *
     * @param values weighted values
     */
    fun addWeightedRandomValues(vararg values: WeightedRandomValue<E>) {
        values.forEach {
            addWeightedRandomValue(it)
        }
    }

    /**
     * Add a weighted value
     *
     * @param value weighted value
     */
    fun addWeightedRandomValue(value: WeightedRandomValue<E>) {
        val lastInterval = if (weightIntervals.isEmpty()) 0 else weightIntervals.last()
        val newInterval = lastInterval + value.weight

        weightedRandomValueList.add(value)
        weightIntervals.add(newInterval)

        // inconsistent total weight, set to newInterval value (if this happen default value will no longer be returned)
        if (totalWeight < newInterval) {
            totalWeight = newInterval
        }
    }

    /**
     * Determine the final random with weight value
     *
     * @param value weight value
     * @return determined value, if weight value exceed all intervals, default value will be returned
     */
    fun determineValue(value: Int): E {
        weightIntervals.forEachIndexed {
            idx, interval ->
            if (value <= interval) {
                return weightedRandomValueList[idx].value
            }
        }

        return defaultValue
    }
}