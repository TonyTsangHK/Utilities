package utils.extensions.type

import utils.data.DataManipulator

import utils.date.DateProgression
import utils.date.DateRange
import utils.date.DateTimeParser
import utils.date.SimpleDateUtils

import utils.math.MathUtil
import java.math.BigDecimal
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-13
 * Time: 11:40
 */
val DAY_MILLIS = 86400000L

fun String.toDate(pattern: String = DateTimeParser.NORMAL_DATETIME_FORMAT, default: Date = Date()): Date {
    return DateTimeParser.parse(this, pattern) ?: default
}

fun String.toDateOrNull(pattern: String = DateTimeParser.NORMAL_DATETIME_FORMAT): Date? {
    return DateTimeParser.parse(this, pattern)
}

fun String.toBigDecimal(default: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return MathUtil.parseBigDecimalNotNull(this, default)
}

fun String.toBigDecimalOrNull(): BigDecimal? {
    return MathUtil.parseBigDecimal(this, null)
}

fun <K, V> Map<K, V>.getInt(key: K, default: Int = -1): Int {
    return DataManipulator.getIntValue(this, key, 10, default)
}

fun <K, V> Map<K, V>.getBoolean(key: K, default: Boolean = false): Boolean {
    return DataManipulator.getBooleanValue(this, key, default)
}

fun <K, V> Map<K, V>.getString(key: K, default: String = ""): String {
    return DataManipulator.getStringValue(this, key, default) ?: default
}

fun <K, V> Map<K, V>.getStringOrNull(key: K, defaultValue: String? = ""): String? {
    return DataManipulator.getStringValue(this, key, defaultValue)
}

fun <K, V> Map<K, V>.getBigDecimal(key: K, default: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return DataManipulator.getBigDecimal(this, key, default) ?: default
}

fun <K, V> Map<K, V>.getBigDecimalOrNull(key: K, default: BigDecimal? = BigDecimal.ZERO): BigDecimal? {
    return DataManipulator.getBigDecimal(this, key, default)
}

fun <K, V> Map<K, V>.getDouble(key: K, default: Double = 0.0): Double {
    return DataManipulator.getDoubleValue(this, key, default)
}

fun <K, V> Map<K, V>.getDate(key: K, default: Date = Date()): Date {
    return DataManipulator.getDate(this, key, default) ?: default
}

fun <K, V> Map<K, V>.getDateOrNull(key: K, default: Date? = Date()): Date? {
    return DataManipulator.getDate(this, key, default)
}

infix operator fun Date.plus(days: Int): Date {
    return Date(this.time + DAY_MILLIS * days)
}

infix operator fun Date.minus(days: Int): Date {
    return Date(this.time - DAY_MILLIS * days)
}

operator fun Date.inc(): Date {
    return this + 1
}

operator fun Date.dec(): Date {
    return this - 1
}

operator fun Date.minus(other: Date): Int {
    val thisCal = Calendar.getInstance()
    val otherCal = Calendar.getInstance()

    thisCal.time = this
    otherCal.time = other
    
    // Eliminate time zone offset, only concern about the local time (calculating day difference between time zones is meaningless)
    val thisMillisWithOffset = thisCal.timeInMillis + thisCal.get(Calendar.ZONE_OFFSET)
    val otherMillisWithOffset = otherCal.timeInMillis + otherCal.get(Calendar.ZONE_OFFSET)
    
    // Eliminate time part
    val thisMillisWithoutTimePart = thisMillisWithOffset - thisMillisWithOffset % DAY_MILLIS
    val otherMillisWithoutTimePart = otherMillisWithOffset - otherMillisWithOffset % DAY_MILLIS
    
    return ((thisMillisWithoutTimePart - otherMillisWithoutTimePart) / DAY_MILLIS).toInt()
}

infix operator fun Date.rangeTo(other: Date): DateRange {
    return DateRange(this, other)
}

fun Date.trimTimePart(): Date {
    val thisCal = Calendar.getInstance()
    
    thisCal.time = this

    thisCal.set(Calendar.HOUR_OF_DAY, 0)
    thisCal.set(Calendar.MINUTE, 0)
    thisCal.set(Calendar.SECOND, 0)
    thisCal.set(Calendar.MILLISECOND, 0)
    
    return thisCal.time
}

infix fun Date.downTo(to: Date): DateProgression {
    return DateProgression.fromClosedRange(this, to, -1)
}

infix fun DateProgression.step(step: Int): DateProgression {
    if (step == 0) {
        throw IllegalArgumentException("Step is zero.")
    }
    return DateProgression.fromClosedRange(first, last, if (this.step > 0) step else -step)
}

operator fun BigDecimal.plus(value: Int): BigDecimal = this.add(BigDecimal(value.toString()))
operator fun BigDecimal.plus(value: Long): BigDecimal = this.add(BigDecimal(value.toString()))
operator fun BigDecimal.plus(value: Short): BigDecimal = this.add(BigDecimal(value.toString()))
operator fun BigDecimal.plus(value: Double): BigDecimal = this.add(BigDecimal(value.toString()))
operator fun BigDecimal.plus(value: Float): BigDecimal = this.add(BigDecimal(value.toString()))

operator fun Int.plus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).add(value)
operator fun Long.plus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).add(value)
operator fun Short.plus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).add(value)
operator fun Double.plus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).add(value)
operator fun Float.plus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).add(value)

operator fun BigDecimal.minus(value: Int): BigDecimal = this.subtract(BigDecimal(value.toString()))
operator fun BigDecimal.minus(value: Long): BigDecimal = this.subtract(BigDecimal(value.toString()))
operator fun BigDecimal.minus(value: Short): BigDecimal = this.subtract(BigDecimal(value.toString()))
operator fun BigDecimal.minus(value: Double): BigDecimal = this.subtract(BigDecimal(value.toString()))
operator fun BigDecimal.minus(value: Float): BigDecimal = this.subtract(BigDecimal(value.toString()))

operator fun Int.minus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).subtract(value)
operator fun Long.minus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).subtract(value)
operator fun Short.minus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).subtract(value)
operator fun Double.minus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).subtract(value)
operator fun Float.minus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).subtract(value)

operator fun BigDecimal.times(value: Int): BigDecimal = this.multiply(BigDecimal(value.toString()))
operator fun BigDecimal.times(value: Long): BigDecimal = this.multiply(BigDecimal(value.toString()))
operator fun BigDecimal.times(value: Short): BigDecimal = this.multiply(BigDecimal(value.toString()))
operator fun BigDecimal.times(value: Double): BigDecimal = this.multiply(BigDecimal(value.toString()))
operator fun BigDecimal.times(value: Float): BigDecimal = this.multiply(BigDecimal(value.toString()))

operator fun Int.times(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).multiply(value)
operator fun Long.times(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).multiply(value)
operator fun Short.times(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).multiply(value)
operator fun Double.times(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).multiply(value)
operator fun Float.times(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).multiply(value)

operator fun BigDecimal.div(value: Int): BigDecimal = MathUtil.divide(this, BigDecimal(value.toString()))
operator fun BigDecimal.div(value: Long): BigDecimal = MathUtil.divide(this, BigDecimal(value.toString()))
operator fun BigDecimal.div(value: Short): BigDecimal = MathUtil.divide(this, BigDecimal(value.toString()))
operator fun BigDecimal.div(value: Double): BigDecimal = MathUtil.divide(this, BigDecimal(value.toString()))
operator fun BigDecimal.div(value: Float): BigDecimal = MathUtil.divide(this, BigDecimal(value.toString()))

operator fun Int.div(value: BigDecimal): BigDecimal = MathUtil.divide(BigDecimal(this.toString()), value)
operator fun Long.div(value: BigDecimal): BigDecimal = MathUtil.divide(BigDecimal(this.toString()), value)
operator fun Short.div(value: BigDecimal): BigDecimal = MathUtil.divide(BigDecimal(this.toString()), value)
operator fun Double.div(value: BigDecimal): BigDecimal = MathUtil.divide(BigDecimal(this.toString()), value)
operator fun Float.div(value: BigDecimal): BigDecimal = MathUtil.divide(BigDecimal(this.toString()), value)

operator fun BigDecimal.mod(value: Int): BigDecimal = this.remainder(BigDecimal(value.toString()))
operator fun BigDecimal.mod(value: Long): BigDecimal = this.remainder(BigDecimal(value.toString()))
operator fun BigDecimal.mod(value: Short): BigDecimal = this.remainder(BigDecimal(value.toString()))
operator fun BigDecimal.mod(value: Double): BigDecimal = this.remainder(BigDecimal(value.toString()))
operator fun BigDecimal.mod(value: Float): BigDecimal = this.remainder(BigDecimal(value.toString()))

operator fun Int.mod(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).remainder(value)
operator fun Long.mod(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).remainder(value)
operator fun Short.mod(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).remainder(value)
operator fun Double.mod(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).remainder(value)
operator fun Float.mod(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).remainder(value)

operator fun BigDecimal.dec(): BigDecimal = this.subtract(BigDecimal.ONE)

operator fun BigDecimal.inc(): BigDecimal = this.add(BigDecimal.ONE)
