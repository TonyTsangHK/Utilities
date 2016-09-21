package utils.extensions.type

import utils.data.DataManipulator
import utils.date.DateTimeParser
import utils.math.MathUtil
import java.math.BigDecimal
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-13
 * Time: 11:40
 */
fun String.toDate(pattern: String = DateTimeParser.NORMAL_DATETIME_FORMAT, default: Date = Date()): Date {
    return DateTimeParser.parse(this, pattern) ?: default;
}

fun String.toBigDecimal(default: BigDecimal = BigDecimal.ZERO): BigDecimal {
    return MathUtil.parseBigDecimalNotNull(this, default)
}

fun <K, V> Map<K, V>.getInt(key: K, default: Int = -1): Int {
    return DataManipulator.getIntValue(this, key, 10, default)
}

fun <K, V> Map<K, V>.getString(key: K, default: String = ""): String? {
    return DataManipulator.getStringValue(this, key, default)
}

fun <K, V> Map<K, V>.getBigDecimal(key: K, default: BigDecimal = BigDecimal.ZERO): BigDecimal? {
    return DataManipulator.getBigDecimal(this, key, default)
}

fun <K, V> Map<K, V>.getDate(key: K, default: Date = Date()): Date? {
    return DataManipulator.getDate(this, key, default)
}

fun <K, V> Map<K, V>.getBoolean(key: K, default: Boolean = false): Boolean {
    return DataManipulator.getBooleanValue(this, key, default)
}