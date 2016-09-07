package utils.date

import java.util.*

/**
 * Created with IntelliJ IDEA.
 * User: Tony Tsang
 * Date: 2016-09-06
 * Time: 11:48
 */

/**
 * Record timer for time recording, replacing ReportTimer
 */
class RecordTimer {
    companion object {
        val secondMillis = 1000L
        val minuteMillis = 60000L
        val hourMillis = 3600000L
        val dayMillis = 86400000L

        /**
         * Convert millis second into human readable string
         *
         * @param millis
         */
        @JvmStatic
        @JvmOverloads
        fun getHumanReadableTimeString(millis: Long, includeMs: Boolean = true): String {
            val ms = millis % secondMillis
            val sec = millis % minuteMillis / secondMillis
            val min = millis % hourMillis / minuteMillis
            val hr = millis % dayMillis / hourMillis

            val result: String

            if (hr > 0) {
                result = "$hr hrs, $min mins, $sec seconds"
            } else if (min > 0) {
                result = "$min mins, $sec seconds"
            } else if (sec > 0) {
                result = "$sec seconds"
            } else {
                result = "0 seconds"
            }

            if (includeMs) {
                return "$result, $ms ms"
            } else {
                return result
            }
        }
    }

    // Record the last start time in millis
    private var startMillis = -1L
    private var elapsedTimeBeforePause = 0L
    private var intervalTimes = ArrayList<Long>()

    private var running = false

    fun isRunning(): Boolean {
        return running
    }

    /**
     * Start/resume the timer
     */
    fun start() {
        running = true
        startMillis = System.currentTimeMillis()
    }

    /**
     * Record elapsed time as interval
     */
    fun recordTime() {
        // Only record time if the timer is running/started
        if (running) {
            val millis = System.currentTimeMillis()

            intervalTimes.add(millis-startMillis+elapsedTimeBeforePause)

            // Mark the startMillis as the record time
            startMillis = millis
            // Reset elapsedTimeBeforePause
            elapsedTimeBeforePause = 0
        }
    }

    /**
     * Stop the timer and record the time
     */
    fun stop() {
        // Only stop if the timer is still running
        if (running) {
            recordTime()

            running = false
            startMillis = -1L
            elapsedTimeBeforePause = 0L
        }
    }

    /**
     * Get the elapsed time since last recordTime/start call
     *
     */
    fun getElapsedTimeSinceLastInterval(): Long {
        if (running) {
            return System.currentTimeMillis()-startMillis+elapsedTimeBeforePause
        } else {
            if (elapsedTimeBeforePause > 0) {
                // timer paused, return elapsed time before pause
                return elapsedTimeBeforePause
            } else {
                // Timer is not running and not paused, no elapsed time!
                return 0
            }
        }
    }

    /**
     * Pause the timer
     */
    fun pause() {
        // Only pause if the timer is still running
        if (running) {
            val millis = System.currentTimeMillis()
            running = false
            elapsedTimeBeforePause += millis - startMillis
            startMillis = -1
        }
    }

    /**
     * Reset the timer and clear all interval times
     */
    fun resetTimer() {
        startMillis = -1
        elapsedTimeBeforePause = 0
        running = false
        intervalTimes.clear()
    }

    /**
     * Get total recorded time
     */
    fun getTotalRecordedTime(): Long {
        return intervalTimes.sum()
    }

    /**
     * Get the recorded interval times
     */
    fun getIntervals(): List<Long> {
        return intervalTimes.clone() as List<Long>
    }
}