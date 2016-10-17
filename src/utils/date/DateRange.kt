package utils.date

import utils.extensions.format.*
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-10-17
 * Time: 15:28
 */
class DateRange(start: Date, endInclusive: Date): DateProgression(start, endInclusive, 1), ClosedRange<Date> {
    override val start: Date get() = first
    override val endInclusive: Date get() = last

    override fun contains(value: Date): Boolean = first <= value && value <= last

    override fun isEmpty(): Boolean = first > last

    override fun equals(other: Any?): Boolean =
        other is DateRange && (isEmpty() && other.isEmpty() || first == other.first && last == other.last)

    override fun hashCode(): Int =
        if (isEmpty()) -1 else (31 * first.hashCode() + last.hashCode())

    /**
     * Compare this date range with input date

     * @param date input date
     * *
     * @return -1 for date greater than toDate, 1 for date smaller than fromDate, 0 for within fromDate & toDate
     */
    operator fun compareTo(date: Date): Int {
        if (start > date) {
            return 1
        } else if (endInclusive < date) {
            return -1
        } else {
            return 0
        }
    }

    override fun toString(): String {
        return toString(true)
    }

    fun toString(trimCommonPart: Boolean): String {
        if (start == endInclusive) {
            return start.toFormattedDate()
        } else {
            val s = start.toFormattedDate()
            val e = endInclusive.toFormattedDate()

            if (trimCommonPart) {
                val sy = SimpleDateUtils.getYear(start)
                val sm = SimpleDateUtils.getMonth(start)
                val ey = SimpleDateUtils.getYear(endInclusive)
                val em = SimpleDateUtils.getMonth(endInclusive)

                if (sy == ey && sm == em) {
                    return s + "~" + e.substring(8)
                } else if (sy == ey) {
                    return s + "~" + e.substring(5)
                } else {
                    return s + "~" + e
                }
            } else {
                return s + "~" + e
            }
        }
    }
}

