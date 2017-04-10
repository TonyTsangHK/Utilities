package utils.measurement.unit

import utils.math.MathUtil
import java.math.BigDecimal

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2017-04-10
 * Time: 16:36
 */

/**
 * Measurement interface
 */
interface Measurement<U> where U: MeasurementUnit {
    /**
     * Value of the measurement
     */
    val value: BigDecimal
    /**
     * Measurement unit
     */
    val unit: U

    /**
     * Create an instance of current measurement class
     * 
     * @param value measurement value
     */
    private fun createInstance(value: BigDecimal): Measurement<U> {
        return this.javaClass.getConstructor(BigDecimal::class.java).newInstance(value)
    }

    /**
     * Convert the measurement to other measurement unit
     * 
     * @param targetUnit target measurement unit
     */
    fun toMeasurementUnit(targetUnit: U): Measurement<U>
    
    operator fun plus(other: Measurement<U>): Measurement<U> {
        return createInstance(value.add(other.toMeasurementUnit(unit).value))
    }
    
    operator fun minus(other: Measurement<U>): Measurement<U> {
        return createInstance(value.subtract(other.toMeasurementUnit(unit).value))
    }
    
    operator fun times(value: Number): Measurement<U> {
        return createInstance(this.value.multiply(BigDecimal(value.toString())))
    }
    
    operator fun div(value: Number): Measurement<U> {
        return createInstance(MathUtil.divide(this.value, BigDecimal(value.toString())))
    }
    
    fun add(other: Measurement<U>, targetUnit: U): Measurement<U> {
        return toMeasurementUnit(targetUnit) + other
    }

    fun subtract(other: Measurement<U>, targetUnit: U): Measurement<U> {
        return toMeasurementUnit(targetUnit) - other
    }
}