package utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SimpleDateUtils {
    private SimpleDateUtils() {}

    private static void validateDate(Object date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    private static void validateDates(Object date1, Object date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
    }

    /**
     * Get a Calendar object with time set to provided Date object
     *
     * @param date provided date, current time will be used for null
     * @return newly created calendar with provided time
     */
    private static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar;
    }

    /**
     * Get year info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return year value of the date info
     */
    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    /**
     * Get year info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return year value of the date info
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get month info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return month value of the date info, result will be within 1 and 12
     */
    public static int getMonth(Date date) {
        // 1 compensated for 0-11
        return getCalendar(date).get(Calendar.MONTH)+1;
    }

    /**
     * Get month info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return month value of the calendar info, result will be within 1 and 12
     */
    public static int getMonth(Calendar calendar) {
        // 1 compensated for 0-11
        return calendar.get(Calendar.MONTH)+1;
    }

    /**
     * Get date (day of month) info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return date (day of month) value of the date info
     */
    public static int getDay(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get date (day of month) info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return date (day of month) value of the date info
     */
    public static int getDay(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get hour info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return hour value of the date info
     */
    public static int getHour(Date date) {
        return getCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Get hour info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return hour value of the date info
     */
    public static int getHour(Calendar calendar) {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * Get minute info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return minute value of the date info
     */
    public static int getMinute(Date date) {
        return getCalendar(date).get(Calendar.MINUTE);
    }

    /**
     * Get minute info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return minute value of the calendar info
     */
    public static int getMinute(Calendar calendar) {
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * Get second info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return second (time) value of the date info
     */
    public static int getSecond(Date date) {
        return getCalendar(date).get(Calendar.SECOND);
    }

    /**
     * Get second info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return second (time) value of the calendar info
     */
    public static int getSecond(Calendar calendar) {
        return calendar.get(Calendar.SECOND);
    }

    /**
     * Get day of week info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return day of week value of the date info (1 = Sunday, 2 = Monday ... 7 = Saturday)
     */
    public static int getDayOfWeek(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Get day of week info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointer exception.
     * @return day of week value of the calendar info
     */
    public static int getDayOfWeek(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Get day of month info from provided date info
     *
     * @param date provided date info, current time will be used for null
     * @return day of month value of the date info (1 = first day of month)
     */
    public static int getDayOfMonth(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get day of month info from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointerException
     * @return day of month value of the calendar info (1 = first day of month)
     */
    public static int getDayOfMonth(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Get day of year from provided date info
     *
     * @param date provided date info, current date time will be used for null value
     * @return day of year value of the date info (1 = first day of year)
     */
    public static int getDayOfYear(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Get day of year from provided calendar info
     *
     * @param calendar provided calendar info, null will trigger NullPointerException
     * @return day of year valu eof the calendar info (1 = first day of year)
     */
    public static int getDayOfYear(Calendar calendar) {
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Determine provided year is a leap year
     *
     * @param year provided year info
     */
    public static boolean isLeapYear(int year) {
        // No leap year on or before 1582, since leap year is added into calendar after 1582
        return year > 1582 && year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    public static Date getFirstDateOfMonth(int year, int month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            return dateFormat.parse(year + "-" + month + "-1");
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getLastDateOfMonth(int year, int month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(year + "-" + month + "-1");

            Calendar c = getCalendar(date);

            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

            return c.getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getMonthDays(int year, int month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = dateFormat.parse(year + "-" + month + "-1");

            Calendar c = getCalendar(date);

            return c.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            return -1;
        }
    }

    public static Date getDateByDayOfYear(int year, int dayOfYear) {
        Calendar calendar = getCalendar(new Date());

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);

        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = getCalendar(new Date());

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, day);

        return cal.getTime();
    }

    public static Date nextWeekDay(Date date, int weekDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentWeekDay = calendar.get(Calendar.DAY_OF_WEEK);

        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date nextWeekDate = calendar.getTime();

        if (currentWeekDay == weekDay) {
            return nextWeekDate;
        } else {
            Calendar nextCalendar = Calendar.getInstance();
            nextCalendar.setTime(nextWeekDate);
            nextCalendar.add(Calendar.DAY_OF_MONTH, weekDay-currentWeekDay);

            return nextCalendar.getTime();
        }
    }
}
