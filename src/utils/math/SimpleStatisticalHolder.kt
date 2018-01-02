package utils.math

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2018-01-02
 * Time: 10:46
 */

/**
 * Only store sum, size, mean, min, max values without raw data
 * 
 * Therefore median & standard deviation is dropped and value removal is not allowed
 */
class SimpleStatisticalHolder {
    companion object {
        @JvmField
        val DEFAULT_MATH_CONTEXT = MathContext(16, RoundingMode.HALF_UP)
    }
    
    var sum: BigDecimal = BigDecimal.ZERO 
        private set 
    var mean: BigDecimal? = null
        private set
    var max: BigDecimal? = null
        private set
    var min: BigDecimal? = null
        private set
    var size: Int = 0
        private set
    
    @Synchronized
    fun addValue(value: Number) {
        val v: BigDecimal
        if (value is BigDecimal) {
            v = value
        } else {
            v = BigDecimal(value.toString())
        }
        
        size++
        sum += v
        mean = sum.divide(size.toBigDecimal(), DEFAULT_MATH_CONTEXT)
        if (max == null || max!! < v) {
            max = v
        }
        if (min == null || min!! > v) {
            min = v
        }
    }
    
    @Synchronized
    fun addValues(vararg values: Number) {
        if (values.isNotEmpty()) {
            var valueMax: BigDecimal? = max
            var valueMin: BigDecimal? = min
            
            for (value in values) {
                val v: BigDecimal
                if (value is BigDecimal) {
                    v = value
                } else {
                    v = BigDecimal(value.toString())
                }

                if (valueMax == null || valueMax < v) {
                    valueMax = v
                }
                if (valueMin == null || valueMin > v) {
                    valueMin = v
                }
                
                sum += v
                size++
            }

            mean = sum.divide(size.toBigDecimal(), DEFAULT_MATH_CONTEXT)
            max = valueMax
            min = valueMin
        }
    }
    
    @Synchronized
    fun clear() {
        size = 0
        sum = BigDecimal.ZERO
        mean = null
        max = null
        min = null
    }
}