package utils.data

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-08-11
 * Time: 14:26
 */
class CircularInteger {
    var lowerBound: Int
        private set
    
    var upperBound: Int
        private set
    
    var value: Int
        set(v) {
            field = normalizeValue(v)
        }
    
    var range: Int
        private set

    constructor(lowerBound: Int, upperBound: Int): this(lowerBound, upperBound, lowerBound)

    constructor(lowerBound: Int, upperBound: Int, value: Int) {
        if (lowerBound >= upperBound) {
            throw RuntimeException("Lower bound cannot be equal or greater than the upper bound")
        } else {
            this.lowerBound = lowerBound
            this.upperBound = upperBound
            this.range = this.upperBound - this.lowerBound + 1
            this.value = value
        }
    }
    
    fun increment() {
        adjustValue(1)
    }

    fun decrement() {
        adjustValue(-1)
    }

    fun adjustValue(adjustment: Int) {
        this.value += adjustment
    }

    private fun normalizeValue(value: Int): Int {
        if (value > upperBound) {
            return ((value - upperBound) % range) + lowerBound - 1
        } else if (value < lowerBound) {
            return upperBound - ((lowerBound - value) % range) + 1
        } else {
            return value
        }
    }

    fun isValueWithinRange(v: Int, upperRange: Int, lowerRange: Int): Boolean {
        val nv = normalizeValue(v)
        val upDiff: Int
        val lowDiff: Int
        
        if (nv > this.value) {
            upDiff = nv - this.value
            lowDiff = (this.value - lowerBound) + (upperBound - nv) + 1
        } else if (nv < this.value) {
            lowDiff = this.value - nv
            upDiff = (upperBound - this.value) + (nv - lowerBound) + 1
        } else {
            return true
        }

        return upDiff <= upperRange || lowDiff <= lowerRange
    }
}