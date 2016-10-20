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

fun String.toDateOrNull(pattern: String = DateTimeParser.NORMAL_DATETIME_FORMAT, default: Date? = Date()): Date? {
    return DateTimeParser.parse(this, pattern) ?: default
}

fun String.toBigDecimal(default: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return MathUtil.parseBigDecimalNotNull(this, default)
}

fun String.toBigDecimalOrNull(default: BigDecimal? = BigDecimal.ZERO): BigDecimal? {
    return MathUtil.parseBigDecimal(this, default)
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

operator fun BigDecimal.plus(value: Number): BigDecimal = this.add(BigDecimal(value.toString()))
operator fun Number.plus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).add(value)

operator fun BigDecimal.minus(value: Number): BigDecimal = this.subtract(BigDecimal(value.toString()))
operator fun Number.minus(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).subtract(value)

operator fun BigDecimal.times(value: Number): BigDecimal = this.multiply(BigDecimal(value.toString()))
operator fun Number.times(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).multiply(value)

operator fun BigDecimal.div(value: Number): BigDecimal = MathUtil.divide(this, BigDecimal(value.toString()))
operator fun Number.div(value: BigDecimal): BigDecimal = MathUtil.divide(BigDecimal(this.toString()), value)

operator fun BigDecimal.mod(value: Number): BigDecimal = this.remainder(BigDecimal(value.toString()))
operator fun Number.mod(value: BigDecimal): BigDecimal = BigDecimal(this.toString()).remainder(value)

operator fun BigDecimal.dec(): BigDecimal = this.subtract(BigDecimal.ONE)

operator fun BigDecimal.inc(): BigDecimal = this.add(BigDecimal.ONE)
