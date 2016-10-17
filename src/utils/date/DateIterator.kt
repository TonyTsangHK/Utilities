package utils.date

import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-10-17
 * Time: 15:34
 */
abstract class DateIterator: Iterator<Date> {
    override final fun next() = nextDate()

    abstract fun nextDate(): Date
}