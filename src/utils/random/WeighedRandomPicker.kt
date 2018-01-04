package utils.random

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2018-01-04
 * Time: 16:35
 */
/**
 * Random picker for weighed values, higher the weight, high the chance of picking
 */
class WeighedRandomPicker<E> {
    private val intervals = ArrayList<IntRange>()
    private val items = ArrayList<E>()
    
    private var totalWeight = 0

    /**
     * Add an item with weight, single element removal not implemented for simplicity
     */
    fun addItem(weight: Int, item: E) {
        // Only accept positive weight
        if (weight > 0) {
            val start = if (intervals.isEmpty()) 1 else intervals.last().last + 1
            val end = totalWeight + weight
            items += item
            totalWeight += weight
            intervals += start .. end
        }
    }

    /**
     * Binary search
     */
    private fun searchInterval(rand: Int): Int {
        if (rand < 1 || rand > totalWeight) {
            return -1
        } else {
            var l = 0
            var h = intervals.size-1

            while (l <= h) {
                val m = (h + l).ushr(1)

                val interval = intervals[m]

                if (interval.contains(rand)) {
                    return m
                } else if (interval.last < rand) {
                    l = m + 1
                } else if (interval.start > rand) {
                    h = m - 1
                }
            }

            return -1
        }
    }

    /**
     * Clear the picker
     */
    fun clear() {
        items.clear()
        totalWeight = 0
    }

    /**
     * Randomly pick an value
     */
    fun randomPick(): E {
        if (totalWeight == 0) {
            throw RuntimeException("Picker not populated yet!")
        }
        
        val r = RandomUtil.randomInteger(1, totalWeight)
        val idx = searchInterval(r)
        return items[idx]
    }
}