package utils.date

import utils.extensions.format.*
import utils.extensions.type.*
import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-10-17
 * Time: 15:35
 */
open class DateProgression internal constructor (start: Date, endInclusive: Date, val step: Int): Iterable<Date> {
    val first: Date = start

    val last: Date

    init {
        if (step == 0) {
            throw IllegalArgumentException("Step must be non-zero")
        }

        last = calLastDate(start, endInclusive, step)
    }

    private fun calLastDate(start: Date, end: Date, step: Int): Date {
        val diff = Math.abs(end - start)

        if (step > 0) {
            if (diff % step == 0) {
                return end
            } else {
                return end - (diff % step)
            }
        } else if (step < 0) {
            if (diff % step == 0) {
                return end
            } else {
                return end + (diff % -step)
            }
        } else {
            throw IllegalArgumentException("Step is zero.")
        }
    }

    override fun iterator(): DateIterator = DateProgressionIterator(first, last, step)

    open fun isEmpty(): Boolean = if (step > 0) first > last else first < last

    override fun equals(other: Any?): Boolean =
        other is DateProgression && 
            (isEmpty() && other.isEmpty() || first == other.first && last == other.last && step == other.step)

    override fun hashCode(): Int =
        if (isEmpty()) -1 else (31 * (31 * first.hashCode() + last.hashCode()) + step)

    override fun toString(): String = 
        if (step > 0) "${first.toFormattedDate()}..${last.toFormattedDate()} step $step"
        else "${first.toFormattedDate()} downTo ${last.toFormattedDate()} step ${-step}"

    companion object {
        fun fromClosedRange(rangeStart: Date, rangeEnd: Date, step: Int): DateProgression = DateProgression(rangeStart, rangeEnd, step)
    }

    internal class DateProgressionIterator(first: Date, last: Date, val step: Int): DateIterator() {
        private var next = first
        private val finalElement = last
        private var hasNext: Boolean = if (step > 0) first <= last else first >= last

        override fun hasNext(): Boolean = hasNext

        override fun nextDate(): Date {
            val value = next

            if (value == finalElement) {
                hasNext = false
            } else {
                next += step
            }

            return value
        }
    }
}