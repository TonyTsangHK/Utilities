package utils.extensions.format

import utils.string.FormatUtils
import java.math.BigDecimal
import java.util.*

@JvmName("FormatExtensions")

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-01
 * Time: 10:55
 */

fun Float.toFormattedNumber(): String {
    return FormatUtils.formatNumber(this)
}

fun Float.toFormattedNumber(maxFractionDigits: Int): String {
    return FormatUtils.formatNumber(this, 2)
}

fun Float.toFormattedNumber(minFractionDigits: Int, maxFractionDigits: Int): String {
    return FormatUtils.formatNumber(this, minFractionDigits, maxFractionDigits)
}

fun Float.toFormattedNumber(maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatNumber(this, maxFractionDigits, grouping)
}

fun Float.toFormattedNumber(minFractionDigits: Int, maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatNumber(this, minFractionDigits, maxFractionDigits, grouping)
}

fun Double.toFormattedNumber(): String {
    return FormatUtils.formatNumber(this)
}

fun Double.toFormattedNumber(maxFractionDigits: Int): String {
    return FormatUtils.formatNumber(this, maxFractionDigits)
}

fun Double.toFormattedNumber(minFractionDigits: Int, maxFractionDigits: Int): String {
    return FormatUtils.formatNumber(this, minFractionDigits, maxFractionDigits)
}

fun Double.toFormattedNumber(maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatNumber(this, 2, maxFractionDigits, grouping)
}

fun Double.toFormattedNumber(minFractionDigits: Int, maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatNumber(this, minFractionDigits, maxFractionDigits, grouping)
}

fun BigDecimal.toFormattedNumber(): String {
    return FormatUtils.formatNumber(this)
}

fun BigDecimal.toFormattedNumber(maxFractionDigits: Int): String {
    return FormatUtils.formatNumber(this, maxFractionDigits)
}

fun BigDecimal.toFormattedNumber(minFractionDigits: Int, maxFractionDigits: Int): String {
    return FormatUtils.formatNumber(this, minFractionDigits, maxFractionDigits)
}

fun BigDecimal.toFormattedNumber(maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatNumber(this, maxFractionDigits, grouping)
}

fun BigDecimal.toFormattedNumber(minFractionDigits: Int, maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatNumber(this, minFractionDigits, maxFractionDigits, grouping)
}

fun Int.toFormattedInteger(): String {
    return FormatUtils.formatInteger(this)
}

fun Long.toFormattedInteger(): String {
    return FormatUtils.formatInteger(this)
}

fun Date?.toFormattedDate(): String {
    return FormatUtils.formatDate(this)
}

fun Date?.toFormattedDate(datePattern: String): String {
    return FormatUtils.formatDate(this, datePattern)
}

fun Long.toFormattedDateTime(): String {
    return FormatUtils.formatDateTime(this)
}

fun Long.toFormattedDateTime(dateTimeFormat: String): String {
    return FormatUtils.formatDateTime(this, dateTimeFormat)
}

fun Date?.toFormattedDateTime(): String {
    return FormatUtils.formatDateTime(this)
}

fun Date?.toFormattedDateTime(dateTimePattern: String): String {
    return FormatUtils.formatDateTime(this, dateTimePattern)
}

fun <E> List<E>.toFormattedList(): String {
    return FormatUtils.formatList(this)
}

fun <E> List<E>.toFormattedList(bracket: Boolean): String {
    return FormatUtils.formatList(this, bracket)
}

fun String.formatPossibleNumber(): String {
    return FormatUtils.formatPossibleNumber(this)
}

fun String.formatPossibleNumber(maxFractionDigits: Int): String {
    return FormatUtils.formatPossibleNumber(this, maxFractionDigits)
}

fun String.formatPossibleNumber(minFractionDigits: Int, maxFractionDigits: Int): String {
    return FormatUtils.formatPossibleNumber(this, minFractionDigits, maxFractionDigits)
}

fun String.formatPossibleNumber(maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatPossibleNumber(this, maxFractionDigits, grouping)
}

fun String.formatPossibleNumber(minFractionDigits: Int, maxFractionDigits: Int, grouping: Boolean): String {
    return FormatUtils.formatPossibleNumber(this, minFractionDigits, maxFractionDigits, grouping)
}

fun String.trimTrailingZeros(): String {
    return FormatUtils.trimTrailingZeros(this)
}